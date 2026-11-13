package com.example.testit.service;

import com.example.testit.adapter.mail.MailService;
import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceIT {

    BookService bookService;

    BookRepository bookRepository;
    MemberRepository memberRepository;
    MailService mailService;


    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        memberRepository = mock(MemberRepository.class);
        mailService = mock(MailService.class);
        bookService = new BookService(bookRepository, memberRepository, mailService);
    }

    @Test
    public void EmprunterEtRendreLivreTest(){

        //given
        Book livre = new Book();
        livre.setId(1L);
        livre.setStatus(Status.DISPONIBLE);
        Member member = new Member();
        member.setId(1L);

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepository.save(livre)).thenReturn(livre);

        Book borrowedBook = bookService.borrowBook(livre.getId(), member.getId());

        Book returnedBook = bookService.returnBook(borrowedBook.getId(), member.getId());

        //then

        assertThat(returnedBook.getStatus()).isEqualTo(Status.RENDU);

        verify(mailService, times(2)).sendMail(any(),any(),any());



    }
}
