
# Auto Generate Code Basic

### Basic configuration for the use of the generator

1.  add the dependencies *pom.xml*

<!-- -->

    <dependencies>
        <dependency>
          <groupId>com.github.basewicoder</groupId>
          <artifactId>wicoder</artifactId>
          <version>4b03cac714</version>
        </dependency>
    </dependencies>

2.  add the repository *pom.xml*

<!-- -->

    <repositories>
        <repository>
           <id>jitpack.io</id>
           <url>https://jitpack.io</url>
         </repository>
     </repositories>

3.  Install the package class main.

<!-- -->

     mvn install

4.  the package use.

<!-- -->

     CodeBuilder in = new CodeBuilder(); 
          in.setConfig("com.example.configuration")
           .setApplication("com.example.application", "/v1/")
           .setInfraestructure("com.example.infrastructure")
           .setDomain("com.example.domain")
           .setClassFlex(new Class[] {
           QuartzJob.class 
        }); 

6.  you can use spring-boot version

<!-- -->

    <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.4.2</version>
      <relativePath/> <!-- lookup parent from repository -->
    </parent>

8.  another way to use the library

<!-- -->

    <dependency>
      <groupId>org.wicoder</groupId>
      <artifactId>wicoder</artifactId>
      <version>1.1.2</version>
    </dependency>

9.  the application structure

.application
-controller
.config
.domain
-data
-port
-spi
-services
.infraestructure
-adapters
-entity
-mappers
-repository

10. optional libraries

<!-- -->

    <dependency>
       <groupId>org.mapstruct</groupId>
       <artifactId>mapstruct</artifactId>
       <version>1.4.2.Final</version>
    </dependency>

    <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-ui</artifactId>
       <version>1.5.8</version>
    </dependency>

    <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <optional>true</optional>
    </dependency>

    <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-tomcat</artifactId>
       <scope>provided</scope>
    </dependency>

add plugins

    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.7.0</version>
      <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <encoding>UTF-8</encoding>
        <annotationProcessorPaths>
         <path>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <version>1.18.12</version>
         </path>
         <path>
           <groupId>org.mapstruct</groupId>
           <artifactId>mapstruct-processor</artifactId>
           <version>1.4.2.Final</version>
         </path>
       </annotationProcessorPaths>
      </configuration>
    </plugin>
    <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
    <excludes>
    <exclude>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    </exclude>
    </excludes>
    </configuration>
    </plugin>

add in main project class

    @OpenAPIDefinition(info = @Info(title = "API", version = "2.0", description = "Information"))
    @ComponentScan(basePackages = {"com.examen"})

add Controller Class Notation

    @Operation(summary = "get all example", tags = { "example" })

    @Hidden

### *run application and generate code*

    mvn clean install spring-boot:run
