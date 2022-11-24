package com.example.demo;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
