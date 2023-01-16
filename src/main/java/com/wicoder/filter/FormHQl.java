package com.wicoder.filter;


import com.wicoder.builder.Buffer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FormHQl<T extends Object> {

    private String sort = "";
    private String whereAndConditions = "";
    final static String AND = " AND %s";
    private String op = AND;
    private String operation = AND;

    final private List<String> names = new ArrayList();
    final private Map<String, Long> join = new HashMap();
    final private Map<String, Object> mapParams = new HashMap();

    private List<T> dataBody;
    private Boolean toDto=false;

    private RequestData RequestDatas;

    @PersistenceContext
    protected EntityManager manager;

    String setTableSql="";
    String setSelectSql="";
    String[] AliasName={"a","b","c","d","e","f"};
    int autoIndex=0;
    Boolean disableAlias=false;

    String Alias="a_";
    static String NameFormat="%1$s%2$s";
    final static String END = "s%s%s";
    final static String START = "e%s%s";

    final static String ASC = " ORDER BY %2$s.%1$s asc";
    final static String DESC = " ORDER BY %2$s.%1$s desc";
    final static String COUNT = "SELECT COUNT(1) %1$s %2$s";

    final static String isDeleted = "WHERE a_.deleted is false";

    private static String CLEAR = "^\\s(AND|OR)";

    private Class<T> entityClass;


    public FormHQl(EntityManager managers) {
        this.manager=managers;
    }

    public FormHQl table(Class<T> entity,Object... inParans){

        entityClass=entity;
        String setTable= classNameTable(entity,AliasName[autoIndex]);
        setTableSql +=String.format("%1$s %2$s", "FROM", setTable);
        autoIndex++;
        return this;
    }
    public FormHQl join(Class<T> entity,String idJoin,String idRef,String... aliasJoin){

        System.out.println(autoIndex+" ___ "+AliasName[autoIndex]);
        String setTable= classNameTable(entity,AliasName[autoIndex]);

        setTableSql +=String.format("\n%1$s %2$s ON %3$s=%4$s", "JOIN", setTable,idJoin,idRef);

        autoIndex++;
        disableAlias=true;
        return this;
    }

    private String classNameTable(Class<T> entity, String alias) {
        String temp = entity.getAnnotation(Entity.class).name();
        String className = temp.isEmpty() ? entity.getSimpleName() : temp;
        return String.format("%1$s %2$s_", className, alias);
    }
    final public String perareSql() { //"SELECT a%2$d_ %1$s"; SELECT a1_ FROM Usuario a1_
        String from =  String.format("SELECT %1$s %2$s", (setSelectSql==""?"a_":setSelectSql),setTableSql);
        System.out.println(ass()+"------");

        return String.format("%1$s %2$s%3$s", from, whereAndConditions.replaceAll(CLEAR, " WHERE"), sort);
    }

    final public List<T> list() {
        if(!toDto){
            dataBody = getQuery(perareSql()).getResultList();
        }
        return dataBody;
    }
    private TypedQuery<T> getQuery(String hql) {
        TypedQuery<T> query = manager.createQuery(hql, entityClass);
        mapParams.keySet().forEach(key -> query.setParameter(key, mapParams.get(key)));
        return query;
    }
    public  FormHQl dto(Function<T , T> instances){
        toDto=true;
        if(!toDto){

            int page=this.RequestDatas.getPq_curpage();
            int size=this.RequestDatas.getPq_rpp();
            dataBody= getQuery(perareSql()).setMaxResults(size)
                    .setFirstResult(page * size)
                    .getResultList();
        }else{
            dataBody=  getQuery(perareSql()).getResultList()
                    .parallelStream()
                    .map(instances)
                    .collect(Collectors.toList());
        }
        return  this;
    }

    final public FormHQl order(String by,String...  values) {
        System.out.println(values);
        by = by == null ? "id" : by;
        sort = String.format( by.matches("\\w+:desc") ? DESC : ASC,
                by.replaceAll(":(desc|asc)", ""), ass()
        );
        return this;
    }

    final public FormHQl map(List<SearchCriteria> data) {
        for (SearchCriteria row : data) {
            parse(row.getDataIndx(), row.getValue(),row.getCondition(),row.getDataType(), row.getValue2());
        }
        return this;
    }

    final public FormHQl request(RequestData request){
        this.RequestDatas=request;
        String sortV="asc";
        if(request.getPq_sort().get(0).getDir().matches("\\w+:down")){
            sortV="desc";
        }
        this.order(String.format("%1$s:%2$s",request.getPq_sort().get(0).getDataIndx(),sortV));
        this.map(request.getPq_filter().getData());
        return this;
    }

    final public Optional<T> get() {
        if(!toDto){
            dataBody = getQuery(perareSql()).getResultList();
        }
        return Optional.ofNullable(  dataBody.isEmpty() ? null : dataBody.get(0));
    }

    public ResponsePaginator paginate(){

        int pq_curpage=this.RequestDatas.getPq_curpage();
        int pq_rpp=this.RequestDatas.getPq_rpp();
        int skip = (pq_rpp * (pq_curpage - 1));

        Integer total_Records= Math.toIntExact(count());
        if (skip >= total_Records)
        {
            pq_curpage = (int)Math.ceil(((double)total_Records) / pq_rpp);
            skip = (pq_rpp * (pq_curpage - 1));
        }

        if(!toDto){
            dataBody= getQuery(perareSql())
                    .setMaxResults(pq_rpp)
                    .setFirstResult(skip)
                    .getResultList();
        }
        return new ResponsePaginator(dataBody, pq_curpage,total_Records );
    }

    /******************************* where ************************************************/

    final public FormHQl where(String name, Object...  values) {
        String condition=Arrays.stream(values).count()==2? values[1].toString():"=";
        //"%1$s%2$s"  nombrea1_=kaka
        mapParams.put(String.format(NameFormat, name, ass()), values[0]); //value setting
        //a1_.nombre = :nombrea1_
        return whereBuilder(String.format( "%3$s.%1$s %2$s :%1$s%3$s", name, condition,ass())); //prepare query
    }

    final public FormHQl like(String name,  Object...  values) {

        Object value=String.format((Arrays.stream(values).count()==2? values[1].toString():"%1$s%2$s%1$s"),"%",values[0].toString());

        mapParams.put(String.format(NameFormat, name, ass()), value.toString().toLowerCase()); //value setting
        return whereBuilder(String.format("LOWER(%2$s.%1$s) LIKE :%1$s%2$s", name, ass()));

    }
    final public FormHQl not_like(String name, Object... values) {
        Object value=String.format((Arrays.stream(values).count()==2? values[1].toString():"%1$s%2$s%1$s"),"%",values[0].toString());
        mapParams.put(String.format(NameFormat, name, ass()), value.toString().toLowerCase());
        return whereBuilder(String.format("LOWER(%2$s.%1$s) NOT LIKE :%1$s%2$s", name, ass()));
    }
    final public FormHQl in(String name, List<String> value) {
        mapParams.put(String.format(NameFormat, name, ass()), value);
        return whereBuilder(String.format("%2$s.%1$s in (:%1$s%2$s)", name, ass()));
    }

    final public FormHQl between(String name, Object start, Object end, String... values) {
        mapParams.put(String.format(START, name, ass()), start);
        mapParams.put(String.format(END, name, ass()), end);
        System.out.println("____init____between--------------");
        Buffer.log(mapParams);
        Buffer.log(String.format("%2$s.%1$s BETWEEN :e%1$s%2$s AND :s%1$s%2$s", name, ass()));
        System.out.println("____end____between--------------");
        return whereBuilder(String.format("%2$s.%1$s BETWEEN :e%1$s%2$s AND :s%1$s%2$s", name, ass()));
    }

    private FormHQl whereBuilder(String subquery) {//operation= " AND %s";  example  (AND a1_.username = :username1_)
        //AND %s
        whereAndConditions += String.format(operation, subquery);
        return this;
    }

   final private String ass(){
        return disableAlias?"":Alias;
    }


    private Long count() {

        Query query = this.manager.createQuery(String.format(COUNT, setTableSql, whereAndConditions.replaceAll(CLEAR, " WHERE"), ass()));

        mapParams.keySet().forEach(key -> query.setParameter(key, mapParams.get(key)));

        return (Long) query.getSingleResult();
    }

    /**************************** end *** where ************************************************/
    private Object to(Object value,String type,String condition){
       return toBuild(value,type,condition);
    }

    private Object toBuild(Object value,String type,String condition) {
           Object result = null;
                switch (type) {
                    case "string":
                        result =value;
                        break;
                    case "date":
                          result = LocalDate.parse(value.toString());
                          break;
                    case "localdatetime":
                        DateTimeFormatter formats = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime localdatetime = Timestamp.valueOf(value.toString()).toLocalDateTime();
                        result = localdatetime;
                        break;
                    case "timestamp":
                        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        Timestamp timestamp = Timestamp.valueOf(value.toString());
                        result = timestamp;
                        break;
                    case "datetime":
                        DateTimeFormatter format3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        Timestamp timestampw = Timestamp.valueOf(value.toString());
                        result = timestampw;
                        break;
                    case "integer"  :
                        if(condition.equals("range") || condition.equals("between")){
                            List<Integer> adds =  new ArrayList<Integer>();
                            for (Object row : (List<Object>) value) {
                                adds.add(Integer.parseInt(row.toString()));
                            }
                            result = adds;
                        }else{
                            result = Integer.parseInt(value.toString());
                        }
                        break;
                    case "bool":
                        result = Boolean.valueOf(value.toString());
                        break;
                    case "checkbock":
                        result = Boolean.valueOf(value.toString());
                        break;
                    case "long":
                          if(condition.equals("range") || condition.equals("between")){
                                List<Long> adds =  new ArrayList<Long>();
                                for (Object row : (List<Object>) value) {
                                    adds.add(Long.parseLong(row.toString()));
                                 }
                                 result = adds;
                            }else{
                                result = Long.parseLong(value.toString());
                            }
                        break;
                }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="Select">
    private FormHQl func(String mode, String name) {
        System.out.println("==========================1====================");
        System.out.println(String.format(mode, name, ass()));
        names.add(String.format(mode, name, ass()));

        //setSelectSql +=String.format(mode, name, ass());
        return this;
    }

    final public FormHQl select(String values) {
        String[] setparamas = values.split(",");
        for (String selected : setparamas) {
            func("%2$s.%1$s", selected);
        }
        setSelectSql = String.join(",", names);
        return this;
    }

    final public FormHQl avg(String name) {
        return func("AVG(a%2$d_.%1$s)", name);
    }

    final public FormHQl sum(String name) {
        return func("SUM(a%2$d_.%1$s)", name);
    }

    final public FormHQl year(String name) {
        return func("YEAR(a%2$d_.%1$s)", name);
    }

    final public FormHQl month(String name) {
        return func("MONTH(a%2$d_.%1$s)", name);
    }
    //</editor-fold>
    private void parse(String name, Object value,String condition,String type,Object value2) {
        Object setValue = to(value, type,condition);
        Object setValue2 = null;
        if(value2 != null){
            setValue2 = to(value2, type,condition);
        }

        Buffer.log(setValue2);
        Object result = null;
        switch (condition) {
            case "bool":
                where(name, setValue, "is");
                break;
            case "contain":
                like(name, "%" + setValue + "%");
                break;
            case "end":
                like(name, "%" + setValue);
                break;
            case "begin":
                like(name,  setValue + "%");
                break;
            case "notcontain":
                not_like(name,  "%" + setValue + "%" );
                break;
            case "equal":
                where(name, setValue, "=");
                break;
            case "notequal":
                where(name, setValue, "!=");
                break;
            case "empty":
                where(name, setValue, "=");
                break;
            case "notempty":
                where(name, setValue, "!=");
                break;
            case "less":
                where(name, setValue, "<");
                break;
            case "great":
                where(name, setValue, ">");
                break;
            case "range":
                List<String> setValue1= (List<String>) setValue;
                in(name, setValue1);
                break;
            case "between":
                between(name, setValue,setValue2,type);
                break;
        }
    }

    public class ResponsePaginator<R extends Object> {

        final private List<R> data;
        final private Integer curPage;
        final private Integer totalRecords;

        public List<R> getData() {
            return data;
        }

        public Integer getCurPage() {
            return curPage;
        }

        public Integer getTotalRecords() {
            return totalRecords;
        }

        public ResponsePaginator(List<R> data, Integer _curPage, Integer _totalRecords) {
            this.data = data;
           // Double temp = Math.ceil(_curPage);
            this.curPage=_curPage;
            this.totalRecords=_totalRecords;
        }

    }

}
