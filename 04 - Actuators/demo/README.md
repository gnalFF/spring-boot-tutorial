#Actuators

## Lets now add Persistence
Reference: 
- https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
- https://www.baeldung.com/spring-data-repositories
- https://docs.spring.io/spring-data/data-commons/docs/2.7.6/reference/html/#repositories


Since we already added the data starter, we can directly start adding an Entity

```java
@Entity
public class Book {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String author;

    //... GETTER SETTER
}
```

Then we add a Repository for reading and writing data

```java
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitle(String title);
}
```


add the following to your application properties

```properties
spring.jpa.defer-datasource-initialization=true

#spring.datasource.driver-class-name=org.h2.Driver
#spring.datasource.url=jdbc:h2:mem:bootapp;#DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
#spring.datasource.username=sa
#spring.datasource.password=
```

lets initialize our database with some default data

data.sql
```sql
INSERT INTO book (id,title,author) VALUES (1,'Buch 1','A1');
INSERT INTO book (id,title,author) VALUES (2,'Buch 2','A2');
INSERT INTO book (id,title,author) VALUES (3,'Buch 3','A2');
```

##Lets write a small test to see if everything works

We reuse the templated test for that in my case it is DemoApplicationTest.java

```java
package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	BookRepository bookRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void dataInitialzed(){
		Iterable<Book> books = bookRepository.findAll();
		assertThat(books).hasSize(3);
	}

}

```