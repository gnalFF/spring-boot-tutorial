package com.example.demo;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;

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
