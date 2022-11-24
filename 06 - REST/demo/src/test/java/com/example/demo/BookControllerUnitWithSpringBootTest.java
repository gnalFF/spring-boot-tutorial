package com.example.demo;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
