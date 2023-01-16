package com.wicoder.builder;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CodeBuilder {


    private static final String IMPLEMENTATION_SUFFIX = "Impl";

    private static   String APPLICATION_SUFFIX = "";
    private static   String DOMAIN_SUFFIX = "";
    private static   String INFRAESTRUCTURE_SUFFIX = "";
    private static   String CONFIG_SUFFIX = "";
    private static   String BASE_DIR_SUFFIX = "";

    private static   String BASE_DIR =  System.getProperty("user.dir");

    private static   String FILENAME_SUFFIX = "";

    private static   String FORMAT_SUFFIX = "%1$s/src/main/java/%2$s/%3$s";
    private static   String STRING_CONFIG = "";


    public CodeBuilder setApplication(String application, String base_ruoter){
        APPLICATION_SUFFIX=application;
        BASE_DIR_SUFFIX=base_ruoter;
        return this;
    }
    public CodeBuilder setConfig(String config){
        CONFIG_SUFFIX=config;
        return this;
    }
    public CodeBuilder setDomain(String controller){
        DOMAIN_SUFFIX=controller;
        return this;
    }
    public CodeBuilder setInfraestructure(String infraestructure) {
        INFRAESTRUCTURE_SUFFIX=infraestructure;
        return this;
    }

    public  <T> Class<T> setClassFlex(Class[] clazz) {
        Class<T> classFlex = null;
        STRING_CONFIG="";
        try {

            for(Class<T> cla : clazz){

                classFlex = getClassFlex(cla, cla.getClassLoader());

                if(DOMAIN_SUFFIX!=""){
                    builderDomain(classFlex);
                }
                if(INFRAESTRUCTURE_SUFFIX!=""){
                    builderInfraestructure(classFlex);
                }
                if(APPLICATION_SUFFIX!=""){
                    builderApplication(classFlex);
                }
            }
            if(CONFIG_SUFFIX!=""){
                builderConfig();
            }

        }
        catch ( ClassNotFoundException | NoSuchMethodException e ) {
            throw new RuntimeException( e );
        }
        return classFlex;
    }

    private  void builderConfig( ) {
        String NameConfig     = String.format("%1$s/src/main/java/%2$s/%3$s", BASE_DIR,CONFIG_SUFFIX,"BuilderConfig").replace( '.', '/' ).concat( ".java" );
        System.out.println(NameConfig);
        try {
            PrintWriter writerConfig = createNewFile(NameConfig);
            writerConfig.println(String.format("%1$s %2$s;","package",CONFIG_SUFFIX));
            writerConfig.println();
            printImport(writerConfig, new String[]{"org.springframework.context.annotation.*",
                    DOMAIN_SUFFIX.concat(".port.spi.*"),
                    INFRAESTRUCTURE_SUFFIX.concat(".adapters.*")
            });
            writerConfig.println();
            printAnnotation(writerConfig, new String[]{"Configuration"});
            writerConfig.println(String.format("%1$s %2$s{\n","public class","BuilderConfig"));
            writerConfig.println(STRING_CONFIG);
            writerConfig.append("}");
            writerConfig.close();
            writerConfig.flush();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    private <T> void builderDomain(Class clazz){

        String NameData      =  String.format(FORMAT_SUFFIX,BASE_DIR,DOMAIN_SUFFIX.concat(".data"),clazz.getSimpleName().concat("Dto")).replace( '.', '/' ).concat( ".java" );
        String NamePortSpi     =  String.format(FORMAT_SUFFIX,BASE_DIR,DOMAIN_SUFFIX.concat(".port.spi"),clazz.getSimpleName().concat("PersistencePort")).replace( '.', '/' ).concat( ".java" );

        try {

            PrintWriter writerData = createNewFile(NameData);
            writerData.println(String.format("%1$s %2$s;","package",DOMAIN_SUFFIX.concat(".data")));
            writerData.println();
            printImport(writerData, new String[]{"lombok.*", "javax.validation.constraints.*"});
            printClassImport(clazz,writerData);
            writerData.println();
            printAnnotation(writerData, new String[]{"Data", "AllArgsConstructor","NoArgsConstructor"});
            writerData.println(String.format("%1$s %2$s{\n","public class",clazz.getSimpleName().concat("Dto")));
            printElement(clazz,writerData);
            writerData.println();
            writerData.append("}");
            writerData.close();
            writerData.flush();



            PrintWriter writerPortSpi = createNewFile(NamePortSpi);
            writerPortSpi.println(String.format("%1$s %2$s;","package",DOMAIN_SUFFIX.concat(".port.spi")));
            writerPortSpi.println();
            printImport(writerPortSpi, new String[]{ "java.util.List","com.wicoder.persistence.BasePersistence", DOMAIN_SUFFIX.concat(".data.")+clazz.getSimpleName().concat("Dto"), clazz.getName()});
            writerPortSpi.println();
            String nameClass=  clazz.getSimpleName().concat("PersistencePort");
            writerPortSpi.println(String.format("%1$s %2$s extends %3$s{\n","public interface",nameClass,"BasePersistence"));

            STRING_CONFIG +="    @Bean\n" +
                    "    public "+nameClass+" "+nameClass.toLowerCase()+"() {\n" +
                    "        return new "+clazz.getSimpleName().concat("JpaAdapter")+"();\n" +
                    "    }\n";


            writerPortSpi.println(String.format("   %1$s findBy(Long id);",clazz.getSimpleName().concat("Dto")));
            writerPortSpi.println();
            writerPortSpi.println(String.format("   List<%1$s> findAll();",clazz.getSimpleName().concat("Dto")));
            writerPortSpi.println();
            writerPortSpi.println(String.format("   %1$s save(%1$s %2$s);",clazz.getSimpleName().concat("Dto"),clazz.getSimpleName().toLowerCase().concat("Dto")));
            writerPortSpi.println();
            writerPortSpi.println(String.format("   %1$s update(%1$s %2$s);",clazz.getSimpleName().concat("Dto"),clazz.getSimpleName().toLowerCase().concat("Dto")));
            writerPortSpi.println();
            writerPortSpi.append("}");
            writerPortSpi.close();
            writerPortSpi.flush();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void builderInfraestructure(Class clazz) {

        String NameMapper       =  FILENAME_SUFFIX=String.format(FORMAT_SUFFIX,BASE_DIR,INFRAESTRUCTURE_SUFFIX.concat(".mappers"),clazz.getSimpleName().concat("Mapper")).replace( '.', '/' ).concat( ".java" );
        String NameAdapter      =  FILENAME_SUFFIX=String.format(FORMAT_SUFFIX,BASE_DIR,INFRAESTRUCTURE_SUFFIX.concat(".adapters"),clazz.getSimpleName().concat("JpaAdapter")).replace( '.', '/' ).concat( ".java" );
        String NameRepository   =  FILENAME_SUFFIX=String.format(FORMAT_SUFFIX,BASE_DIR,INFRAESTRUCTURE_SUFFIX.concat(".repository"),clazz.getSimpleName().concat("Repository")).replace( '.', '/' ).concat( ".java" );

        try {

            PrintWriter writerAdapter = createNewFile(NameAdapter);
            writerAdapter.println(String.format("%1$s %2$s;","package",INFRAESTRUCTURE_SUFFIX.concat(".adapters")));
            writerAdapter.println();

            printImport(writerAdapter, new String[]{"org.springframework.beans.factory.annotation.Autowired","java.util.Optional" ,
                    "java.util.List","java.util.stream.Collectors","com.wicoder.adapters.BaseJpaAdapter",
                    DOMAIN_SUFFIX.concat(".port.spi.").concat(clazz.getSimpleName()+"PersistencePort"),
                    DOMAIN_SUFFIX.concat(".data.")+clazz.getSimpleName().concat("Dto"),
                    clazz.getName(),
                    INFRAESTRUCTURE_SUFFIX.concat(".mappers.").concat(clazz.getSimpleName()+"Mapper"),
                    INFRAESTRUCTURE_SUFFIX.concat(".repository.").concat(clazz.getSimpleName()+"Repository")});

            writerAdapter.println();
            writerAdapter.println(String.format("%1$s %2$s extends BaseJpaAdapter implements %3$s{\n","public class ",clazz.getSimpleName().concat("JpaAdapter"),clazz.getSimpleName().concat("PersistencePort")));

            writerAdapter.println("   @Autowired");
            writerAdapter.println(String.format("   protected %1$sRepository repository;",clazz.getSimpleName()));
            writerAdapter.println();

            writerAdapter.println("   @Override");
            writerAdapter.println(String.format("   public %1$s findBy(Long id){",clazz.getSimpleName().concat("Dto")));
            writerAdapter.println(String.format("       Optional<%1$s> %2$sOptional = repository.findById(id);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writerAdapter.println(String.format("       return %1$sOptional.map(%2$sMapper.INSTANCE::%1$sTo%2$sDto).orElse(null);", clazz.getSimpleName().toLowerCase(),clazz.getSimpleName()));
            writerAdapter.println("   }");

            writerAdapter.println();
            writerAdapter.println("   @Override");
            writerAdapter.println(String.format("   public List<%1$s> findAll(){",clazz.getSimpleName().concat("Dto")));
            writerAdapter.println(String.format("       return repository.findAll().stream().map(%1$sMapper.INSTANCE::%2$sTo%1$sDto).collect(Collectors.toList());",clazz.getSimpleName(), clazz.getSimpleName().toLowerCase()));
            writerAdapter.println("   }");

            writerAdapter.println();
            writerAdapter.println("   @Override");
            writerAdapter.println(String.format("   public %1$s save(%1$s %2$s){",clazz.getSimpleName().concat("Dto"),clazz.getSimpleName().toLowerCase().concat("Dto")));
            writerAdapter.println(String.format("         %1$s %2$s = %1$sMapper.INSTANCE.%2$sDtoTo%1$s(%2$sDto);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writerAdapter.println(String.format("         %1$s %2$sSaved = repository.save(%2$s);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writerAdapter.println(String.format("         return %1$sMapper.INSTANCE.%2$sTo%1$sDto(%2$sSaved);",clazz.getSimpleName(), clazz.getSimpleName().toLowerCase()));
            writerAdapter.println("   }");
            writerAdapter.println();

            writerAdapter.println("   @Override");
            writerAdapter.println(String.format("   public %1$s update(%1$s %2$s){",clazz.getSimpleName().concat("Dto"),clazz.getSimpleName().toLowerCase().concat("Dto")));
            writerAdapter.println(String.format("         %1$s %2$s = %1$sMapper.INSTANCE.%2$sDtoTo%1$s(%2$sDto);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writerAdapter.println(String.format("         %1$s %2$sSaved = repository.save(%2$s);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writerAdapter.println(String.format("         return %1$sMapper.INSTANCE.%2$sTo%1$sDto(%2$sSaved);",clazz.getSimpleName(), clazz.getSimpleName().toLowerCase()));
            writerAdapter.println("   }");
            writerAdapter.println();

            writerAdapter.append(String.format("%1$s","}"));
            writerAdapter.close();
            writerAdapter.flush();



            PrintWriter writerMapper = createNewFile(NameMapper);
            writerMapper.println(String.format("%1$s %2$s;","package",INFRAESTRUCTURE_SUFFIX.concat(".mappers")));
            writerMapper.println();
            printImport(writerMapper, new String[]{
                    "org.mapstruct.Mapper","org.mapstruct.factory.Mappers" ,
                    DOMAIN_SUFFIX.concat(".data.")+clazz.getSimpleName().concat("Dto"),
                    clazz.getName()
            });
            writerMapper.println();
            writerMapper.println("@Mapper");
            writerMapper.println(String.format("%1$s %2$s{\n","public interface ",clazz.getSimpleName().concat("Mapper")));
            writerMapper.println(String.format("     %1$sMapper INSTANCE= Mappers.getMapper(%1$sMapper.class);",clazz.getSimpleName()));
            writerMapper.println(String.format("     %1$sDto %2$sTo%1$sDto(%1$s %2$s);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writerMapper.println(String.format("     %1$s %2$sDtoTo%1$s(%1$sDto %2$sDto);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writerMapper.println("}");
            writerMapper.close();
            writerMapper.flush();



            PrintWriter writerRepository = createNewFile(NameRepository);
            writerRepository.println(String.format("package %1$s;",INFRAESTRUCTURE_SUFFIX.concat(".repository")));
            printImport(writerRepository, new String[]{"org.springframework.data.jpa.repository.JpaRepository","org.springframework.stereotype.Repository",clazz.getName()});
            writerRepository.println();
            writerRepository.println("@Repository");
            //writerRepository.println(String.format("public interface %1$sRepository  extends BaseRepository<%1$s>{",clazz.getSimpleName()));
            writerRepository.println(String.format("public interface %1$sRepository  extends JpaRepository<%1$s, Long>{",clazz.getSimpleName()));
            writerRepository.println(String.format("%1$s","}"));
            writerRepository.close();
            writerRepository.flush();


        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void builderApplication(Class clazz){

        String NameController       =  FILENAME_SUFFIX=String.format(FORMAT_SUFFIX,BASE_DIR,APPLICATION_SUFFIX.concat(".controller"),clazz.getSimpleName().concat("Controller")).replace( '.', '/' ).concat( ".java" );

        System.out.println(FILENAME_SUFFIX);
        try {
            PrintWriter writercontroller = createNewFile(NameController);

            //builderException(clazz);

            writercontroller.println(String.format("%1$s %2$s;\n","package",APPLICATION_SUFFIX.concat(".controller")));

            printImport(writercontroller, new String[]{
                    "org.springframework.web.bind.annotation.*","org.springframework.http.ResponseEntity","java.util.List","com.wicoder.filter.*",
                    clazz.getName(),
                    DOMAIN_SUFFIX.concat(".data."+clazz.getSimpleName()+"Dto"),
                    DOMAIN_SUFFIX.concat(".port.spi."+clazz.getSimpleName()+"PersistencePort"),
                    "com.wicoder.exception.ResourceNotFoundException"
                    //APPLICATION_SUFFIX.concat(".exception.ResourceNotFoundException"),
                    //DOMAIN_SUFFIX.concat(".data.filter.*")//opcional
            });


            writercontroller.println();
            printAnnotation(writercontroller, new String[]{"RestController", "RequestMapping(\""+BASE_DIR_SUFFIX+clazz.getSimpleName().toLowerCase()+"\")","CrossOrigin(origins = \"*\", maxAge = 20000)"});
            writercontroller.println(String.format("%1$s %2$sController{\n","public class",clazz.getSimpleName()));

            writercontroller.println(String.format("    private  final %1$sPersistencePort service;",clazz.getSimpleName()));
            writercontroller.println();
            writercontroller.println(String.format("    public %1$sController(%1$sPersistencePort service) {",clazz.getSimpleName()));
            writercontroller.println(String.format("        this.service = service;",clazz.getSimpleName()));
            writercontroller.println("    }");


            writercontroller.println();
            writercontroller.println("    @PostMapping(\"/paginate\")");
            writercontroller.println(String.format("    public Object paginate%1$s(@RequestBody RequestData request) {",clazz.getSimpleName()));
            writercontroller.println("        return service.db().table("+clazz.getSimpleName()+".class).request(request).paginate();");
            writercontroller.println("    }");
            writercontroller.println();

            writercontroller.println();
            writercontroller.println("    @GetMapping(\"/all\")");
            writercontroller.println(String.format("    public ResponseEntity<List<%1$sDto>> all%1$s() {",clazz.getSimpleName()));
            writercontroller.println("        return ResponseEntity.ok().body(service.findAll());");
            writercontroller.println("    }");
            writercontroller.println();

            writercontroller.println("    @GetMapping(\"/{id}\")");
            writercontroller.println(String.format("    public ResponseEntity<%1$sDto> findBy(@PathVariable(value = \"id\") Long id) throws Exception {",clazz.getSimpleName()));
            writercontroller.println(String.format("        %1$sDto %2$sDto = service.findBy(id);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writercontroller.println(String.format("        if (%1$sDto == null) {",clazz.getSimpleName().toLowerCase()));
            writercontroller.println(String.format("            throw new ResourceNotFoundException(\"No existe %1$s con ese codigo:: \" + id);",clazz.getSimpleName().toLowerCase()));
            writercontroller.println("        }");
            writercontroller.println(String.format("        return ResponseEntity.ok().body(%1$sDto);",clazz.getSimpleName().toLowerCase()));
            writercontroller.println("    }");
            writercontroller.println();

            writercontroller.println("    @PostMapping()");
            writercontroller.println(String.format("    public %1$sDto create(@RequestBody %1$sDto %2$sDto) throws Exception {",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writercontroller.println(String.format("        return service.save(%1$sDto);",clazz.getSimpleName().toLowerCase()));
            writercontroller.println("    }");
            writercontroller.println();
            writercontroller.println("    @PutMapping(\"/{id}\")");
            writercontroller.println(String.format("    public ResponseEntity<%1$sDto> update(@PathVariable(value = \"id\") Long id,@RequestBody %1$sDto %2$sDto) throws Exception {",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writercontroller.println(String.format("     %1$sDto data = service.update(%2$sDto);",clazz.getSimpleName(),clazz.getSimpleName().toLowerCase()));
            writercontroller.println("       if(data == null) {");
            writercontroller.println(String.format("         throw new ResourceNotFoundException(\"No existe %1$s con ese codigo:: \" + id);",clazz.getSimpleName()));
            writercontroller.println("       }");
            writercontroller.println(String.format("       return ResponseEntity.ok(%1$sDto);",clazz.getSimpleName().toLowerCase()));
            writercontroller.println("    }");
            writercontroller.println();
            writercontroller.println("}");

            writercontroller.flush();
            writercontroller.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void builderException(Class clazz){
        String NameException      =  FILENAME_SUFFIX=String.format(FORMAT_SUFFIX,BASE_DIR,APPLICATION_SUFFIX,"exception").replace( '.', '/' ).concat( "/ResourceNotFoundException.java" );
        String NameExceptionDB      =  FILENAME_SUFFIX=String.format(FORMAT_SUFFIX,BASE_DIR,APPLICATION_SUFFIX,"exception").replace( '.', '/' ).concat( "/DBException.java" );

        System.out.println(FILENAME_SUFFIX);
        try {
            PrintWriter writerException = createNewFile(NameExceptionDB);
            writerException.println(String.format("%1$s %2$s;\n","package",APPLICATION_SUFFIX.concat(".exception")));
            writerException.println("import lombok.Getter;\n" +
                    "import org.springframework.http.HttpStatus;\n" +
                    "import org.springframework.web.bind.annotation.ResponseStatus;\n" +
                    "\n" +
                    "@Getter\n" +
                    "@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)\n" +
                    "public class DBException extends RuntimeException {\n" +
                    "\n" +
                    "    private static final long serialVersionUID = 1L;\n" +
                    "\n" +
                    "    final private static String MSG = \"The register with %s = %s not exists in table %s\";\n" +
                    "    final private static String REQUIRED = \"El campo \\\"%s\\\" es requerido!...\";\n" +
                    "\n" +
                    "    final private Object value;\n" +
                    "    final private String tableName;\n" +
                    "    final private String columnName;\n" +
                    "\n" +
                    "    public DBException() {\n" +
                    "\n" +
                    "        super(\"The class could not be implemented\");\n" +
                    "\n" +
                    "        this.columnName = null;\n" +
                    "        this.tableName = null;\n" +
                    "        this.value = null;\n" +
                    "    }\n" +
                    "\n" +
                    "    public DBException(String column) {\n" +
                    "\n" +
                    "        super(String.format(REQUIRED, column));\n" +
                    "\n" +
                    "        this.columnName = null;\n" +
                    "        this.tableName = null;\n" +
                    "        this.value = null;\n" +
                    "    }\n" +
                    "\n" +
                    "    public DBException(String table, Object value) {\n" +
                    "\n" +
                    "        super(String.format(MSG, \"ID\", value, table));\n" +
                    "\n" +
                    "        this.tableName = table;\n" +
                    "        this.columnName = \"ID\";\n" +
                    "        this.value = value;\n" +
                    "    }\n" +
                    "\n" +
                    "    public DBException(String table, String column, Object value) {\n" +
                    "\n" +
                    "        super(String.format(MSG, column, value, table));\n" +
                    "\n" +
                    "        this.tableName = table;\n" +
                    "        this.columnName = column;\n" +
                    "        this.value = value;\n" +
                    "    }\n" +
                    "\n" +
                    "}\n");
            writerException.flush();
            writerException.close();


            PrintWriter writerExceptionR = createNewFile(NameException);
            writerExceptionR.println(String.format("%1$s %2$s;\n","package",APPLICATION_SUFFIX.concat(".exception")));
            writerExceptionR.println("import org.springframework.http.HttpStatus;\n" +
                    "import org.springframework.web.bind.annotation.ResponseStatus;\n" +
                    "\n" +
                    "@ResponseStatus(value = HttpStatus.NOT_FOUND)\n" +
                    "public class ResourceNotFoundException extends RuntimeException {\n" +
                    "    private static final long serialVersionUID = 1L;\n" +
                    "\n" +
                    "    public ResourceNotFoundException(String message) {\n" +
                    "        super(message);\n" +
                    "    }\n" +
                    "\n" +
                    "}");
            writerExceptionR.flush();
            writerExceptionR.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private <T> void printElement(Class clazz,PrintWriter writerData) throws ClassNotFoundException {
        Field flds[] = clazz.getDeclaredFields();
        for (int i = 0; i < flds.length; i++) {
            if(flds[i].getGenericType().toString().matches("(.*)class(.*)")){
                Class<T> classloader = (Class<T>) this.getClass().getClassLoader().loadClass(flds[i].getGenericType().getTypeName());
                try {
                    Class<T> classloader1 = (Class<T>) this.getClass().getClassLoader().loadClass(DOMAIN_SUFFIX.concat(".data.").concat(classloader.getSimpleName()+"Dto"));
                    writerData.println(String.format("      %1$s %2$s %3$s;","private",classloader1.getSimpleName(),flds[i].getName()));
                }catch(Exception a){
                    writerData.println(String.format("      %1$s %2$s %3$s;","private",classloader.getSimpleName(),flds[i].getName()));
                }
            }else{
                writerData.println(String.format("      %1$s %2$s %3$s;","private",flds[i].getGenericType(),flds[i].getName()));
            }
        }
    }
    private <T> void printClassImport(Class clazz,PrintWriter writerData) throws ClassNotFoundException {
        Field flds[] = clazz.getDeclaredFields();
        for (int i = 0; i < flds.length; i++) {
            if(flds[i].getGenericType().toString().matches("(.*)class(.*)")){
                Class<T> classloaders = (Class<T>) this.getClass().getClassLoader().loadClass(flds[i].getGenericType().getTypeName());
                try {
                    Class<T> classloader = (Class<T>) this.getClass().getClassLoader().loadClass(DOMAIN_SUFFIX.concat(".data.").concat(classloaders.getSimpleName()+"Dto"));
                    writerData.println(String.format("import %1$s;",classloader.getTypeName()));
                }catch(Exception a){
                    writerData.println(String.format("import %1$s;",classloaders.getTypeName()));
                }
            }
        }
    }
    private  void printImport(PrintWriter writerData,String... imports)  {
        for (int i = 0; i < imports.length; i++) {
          writerData.println(String.format("import %1$s;",imports[i]));
        }
    }
    private  void printAnnotation(PrintWriter writerData,String... imports)  {
        for (int i = 0; i < imports.length; i++) {
            writerData.println(String.format("@%1$s",imports[i]));
        }
    }


    private PrintWriter createNewFile(String nameFile) throws IOException {

        File myObj = new File(nameFile);
        if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
        } else {
            myObj.delete();
            System.out.println("File already exists.");
            myObj.createNewFile();
            System.out.println("File created: " + myObj.getName());
        }
        PrintWriter out = new PrintWriter(new FileWriter(nameFile));

       return out;
    }


    private static <T> Class<T> getClassFlex(Class<T> mapperType, ClassLoader classLoaders) throws NoSuchMethodException, ClassNotFoundException {
            Class<T> mapper = dogetClassFlex( mapperType, classLoaders);
            if ( mapper != null ) {
                return mapper;
            }
        throw new ClassNotFoundException("No se puede encontrar la implementación para " + mapperType.getName() );
    }

    private static <T> Class<T> dogetClassFlex(Class<T> clazz, ClassLoader classLoader) {
        try {
            @SuppressWarnings( "unchecked" )
            Class<T> implementation = (Class<T>) classLoader.loadClass(clazz.getName());
            Constructor<T> constructor = implementation.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            return implementation;
        }
        catch (Exception e) {
            return null;
        }
    }


}
