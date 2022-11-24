# Persistence

## Lets add some Rest

Create a Class BookController.java

```java
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/title/{bookTitle}")
    public List<Book> findByTitle(@PathVariable String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
          throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }
}
```

Create the Missing Exception Classes

BookNotFoundException.java
```java
public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException() {

    }
    
}
```

BookIdMismatchException.java
```java
public class BookIdMismatchException extends RuntimeException {
    public BookIdMismatchException() {

    }
}
```

## Lets run it and check

http://localhost:8080/api/books
http://localhost:8080/api/books/1
http://localhost:8080/api/books/title/Buch 1

http://localhost:8080/api/books/3829837 -> Exception Error 500

## Lets add ExceptionHandling

RestExceptionHandler
```java
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ BookNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Book not found", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ BookIdMismatchException.class,ConstraintViolationException.class,DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(),new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
```
Check now the exception which is shown to the user


## Lets add tests

Read more here:
https://www.baeldung.com/spring-boot-testing (note it is java 4 with java5 with use ExtendWith and not RunWith)

### See how to run unit tests

Lets first run a classical unit test with mocking the repository - no spring involved at
BookControllerUnitWithMockitoTest.java
```java
@ExtendWith(MockitoExtension.class)
public class BookControllerUnitWithMockitoTest {
    

    @InjectMocks
    public BookController underTest;

    @Mock
    public BookRepository bookRepository;

    @Test
    public void testFindAll(){
        Book book = new Book();
        book.setId(1);
        book.setAuthor("A1");
        book.setTitle("T1");
        
        Book book2 = new Book();
        book.setId(2);
        book.setAuthor("A2");
        book.setTitle("T2");


        Mockito.when(bookRepository.findAll()).thenReturn(Arrays.asList(book,book2));

        Iterable<Book> found = underTest.findAll();

        Assertions.assertThat(found).hasSize(2);
    }

}
```

Lets have another test with spring flavor

BookControllerUnitWithSpringTest.java
```java
@ExtendWith(SpringExtension.class)
public class BookControllerUnitWithSpringTest {

    @TestConfiguration
    static class UnitTestconfiguration {

        @Bean
        public BookController bookController() {
            return new BookController();
        }
    }

    @Autowired
    public BookController underTest;

    @MockBean
    public BookRepository bookRepository;

    @Test
    public void testFindByTitle() {
        Book book = new Book();
        book.setId(1);
        book.setAuthor("A1");
        book.setTitle("T1");

        Mockito.when(bookRepository.findByTitle(Mockito.eq("T1"))).thenReturn(Arrays.asList(book));
        List<Book> found = underTest.findByTitle("T1");

        Assertions.assertThat(found).hasSize(1);
        Assertions.assertThat(found.get(0).getAuthor()).isEqualTo("A1");
    }

}
```
Lets have another one, but for unit tests not really recommended
```java
@SpringBootTest
public class BookControllerUnitWithSpringBootTest {

    @Autowired
    public BookController underTest;

    @MockBean
    public BookRepository bookRepository;

    @Test
    public void testDelete() {
        Book book = new Book();
        book.setId(1);
        book.setAuthor("A1");
        book.setTitle("T1");

        Mockito.when(bookRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(book));

        underTest.delete(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test()
    public void testDeleteUnknownThrowsException() {
        Book book = new Book();
        book.setId(1);
        book.setAuthor("A1");
        book.setTitle("T1");

        Mockito.when(bookRepository.findById(Mockito.eq(2L))).thenReturn(Optional.empty());

        Assertions.assertThrows(BookNotFoundException.class, () -> {
            underTest.delete(2L);
        });
    }

}
```

### Now lets see how we can make an integration test with spring.
    
#### Integration Testing with Rest-Assured
Rest assured is a test-library which simplifies calling rest apis and verifying the responses (more details here: https://rest-assured.io/ )

add to the pom.xml
```xml

		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>rest-assured</artifactId>
			<scope>test</scope>
		</dependency>
```

then lets write the restassured test

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestAssuredIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    public BookRepository bookRepository;

    public static Random rand = new Random();

    private Book createRandomBook() {
        Book book = new Book();

        book.setTitle(UUID.randomUUID().toString());
        book.setAuthor(UUID.randomUUID().toString());
        return book;
    }

    private String createBookAsUri(Book book) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(getAPIRoot());
        return getAPIRoot() + "/" + response.jsonPath().get("id");
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        System.out.println("LOCALPORT ------------------- " + getAPIRoot());
        Response response = RestAssured.get(getAPIRoot());
        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode());
        Assertions.assertThat(response.as(List.class)).hasSize(Math.toIntExact(bookRepository.count()));
    }

    @Test
    public void whenGetNotExistBookById_thenNotFound() {
        Response response = RestAssured.get(getAPIRoot() + "/9832093874618");
        Assertions.assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(response.getStatusCode());
    }

    @Test
    public void whenGetCreatedBookById_thenOK() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = RestAssured.get(location);

        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode());
        Assertions.assertThat(book.getTitle()).isEqualTo(response.jsonPath().get("title"));
    }

    private String getAPIRoot() {
        return "http://localhost:" + port + "/api/books";
    }

}
```

#### now lets write a mocked Integration Test with Spring

The difference is that we do not even start a webserver here, but spring only mocks it -> ergo faster

create a class 
and put inside
```java
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MockMVCIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenGetAllBooks_thenOK() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
```

