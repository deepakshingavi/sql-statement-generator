# sql-statement-generator
Generates SQL statement based on user selection of fields

### Pre-Requisites
* Java 8+
* Maven 3.5.0

#### Project Details
* src/main/java - Contains Java source code
* src/test/java - Contains Java test source code  

#### Build
Executes the unit test case and build the jar 
```shell script
mvn clean install
```

#### Run unit tests
Executes the unit test case and build the jar 
```shell script
mvn test
```
    
    
Assumption and scope :
1. Scope of the project is to join max 2 tables with a relationship table in between.
2. Final SQL query will contain table names and field name in lower case.