package com.wicoder.filter;


public class SearchCriteria {

    private String dataIndx;
    private String dataType;
    private String condition;
    private Object value;
    private Object value2;

    public String getDataIndx() {
        return dataIndx;
    }

    public void setDataIndx(String dataIndx) {
        this.dataIndx = dataIndx;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }

    public SearchCriteria(){}

    public static SearchCriteria add(String dataIndx,String condition,String dataType, Object value,Object value2) {
        SearchCriteria data = new SearchCriteria();
        data.setDataIndx(dataIndx);
        data.setDataType(dataType);
        data.setCondition(condition);
        data.setValue(value);
        data.setValue2(value2);
        return data;
    }

    public static SearchCriteria add(String dataIndx,String condition,String dataType, Object value) {
        SearchCriteria data = new SearchCriteria();
        data.setDataIndx(dataIndx);
        data.setDataType(dataType);
        data.setCondition(condition);
        data.setValue(value);
        return data;
    }

}
