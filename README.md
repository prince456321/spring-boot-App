# Excersise Spring Boot, Mysql, JPA and Hibernate Docker

CRUD using Spring Boot, Mysql, JPA and Hibernate using docker-compose.

## Requirement
Install the following requirement

```bash
1. Java - 11

2. Maven - 3.x.x

3. Mysql - 10.x.x-MariaDB
```

## Installing 

Clone the project

```c
git clone https://github.com/many-pichr/spring-boot-jpa-mysql-docker.git
```

### Change database connection `application.properties`

```c
server.port=8088
spring.datasource.url = jdbc:mysql://localhost:3306/movie_app?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
spring.datasource.username = root
spring.datasource.password =
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update

```
Run Spring boot application

```c
mvn spring-boot:run
```

## Output

``
http://localhost:8088/swagger-ui.html
``

![App Screenshot](https://firebasestorage.googleapis.com/v0/b/many-93eb4.appspot.com/o/Screen%20Shot%202022-01-11%20at%202.42.19%20PM.png?alt=media&token=cae060a4-e3eb-4456-9ee2-68f6502dbc5d)

## JUnit test using @DataJpaTest annotation

```
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieRepositoryTests {
    @Autowired
    private MovieRepository movieRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveMovieTest(){
        Movie track = new Movie();
        Category category = new Category();
        track.setName("Spider Man");
        track.setRating(1.0);
        category.setId(1L);
        track.setCategory(category);
        movieRepository.save(track);
        Assertions.assertThat(track.getId()).isGreaterThan(0);
    }
```
### Result

![App Screenshot](https://firebasestorage.googleapis.com/v0/b/many-93eb4.appspot.com/o/Screen%20Shot%202022-01-11%20at%2011.24.05%20AM.png?alt=media&token=2f105375-51b6-4522-9b1b-5fd407445f74)

# Deployment with Docker

#### Required Install Docker Engine

### Dockerfile
```c
FROM openjdk:11
EXPOSE 8088
WORKDIR /app
# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn
# Copy the pom.xml file
COPY pom.xml .
# Copy the project source
COPY ./src ./src
COPY ./pom.xml ./pom.xml
RUN chmod 755 /app/mvnw
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw package -DskipTests
RUN ls -al
ENTRYPOINT ["java","-jar","target/exercise-0.0.1.jar"]
```

### docker-compose.yml

```c
version: '3.3'

services:
  #service 1: definition of mysql database
  db:
    image: mysql:latest
    container_name: mysql-db
    environment:
      - MYSQL_ROOT_PASSWORD=password
    ports:
      - "3306:3306"
    restart: always
  
  #service 2: definition of your spring-boot app
  customerservice:                        #it is just a name, which will be used only in this file.
    image: spring-boot-image               #name of the image after dockerfile executes
    container_name: spring-boot-app  #name of the container created from docker image
    build:
      context: .                          #docker file path (. means root directory)
      dockerfile: Dockerfile              #docker file name
    ports:
      - "8088:8088"                       #docker containter port with your os port
    restart: always
    
    depends_on:                           #define dependencies of this app
      - db                                #dependency name (which is defined with this name 'db' in this file earlier)
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql-db:3306/movie_app?createDatabaseIfNotExist=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: password
```

### Deploy application using docker-compose

```c
docker-compose up
```
### Docker Images

```
REPOSITORY          TAG       IMAGE ID       CREATED       SIZE
spring-boot-image   latest    f07b5a307763   3 hours ago   842MB
mysql               latest    3218b38490ce   3 weeks ago   516MB
```

### Docker Containers

```
CONTAINER ID   IMAGE               COMMAND                  CREATED       STATUS              PORTS                               NAMES
ccec75facb86   spring-boot-image   "java -jar target/ex…"   3 hours ago   Up About a minute   0.0.0.0:8088->8088/tcp              spring-boot-app
6114f1c7b86f   mysql:latest        "docker-entrypoint.s…"   4 hours ago   Up About a minute   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql-db

```
