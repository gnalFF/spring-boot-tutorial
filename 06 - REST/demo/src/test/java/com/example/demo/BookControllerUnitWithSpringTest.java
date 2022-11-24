package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
