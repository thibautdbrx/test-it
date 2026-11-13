package com.example.testit.repository;

import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class BookRepositoryIT {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void findMembersTest(){
        //given
        Book b1 = new Book();
        b1.setStatus(Status.EMPRUNTE);
        b1 = bookRepository.save(b1);

        Book b2 = new Book();
        b2.setStatus(Status.EMPRUNTE);
        b2 = bookRepository.save(b2);

        Book b3 = new Book();
        b3.setStatus(Status.EMPRUNTE);
        b3 = bookRepository.save(b3);

        Book b4 = new Book();
        b4.setStatus(Status.EMPRUNTE);
        b4 = bookRepository.save(b4);

        Member m1 = new Member();
        m1.setBooks(List.of(b1, b2));
        m1 = memberRepository.save(m1);

        Member m2 = new Member();
        m2.setBooks(List.of(b3, b4));
        m2 = memberRepository.save(m2);

        //when
        List<Book> m1Books = bookRepository.findByMemberAndStatus(m1.getId(), Status.EMPRUNTE);

        List<Book> m2Books = bookRepository.findByMemberAndStatus(m2.getId(), Status.EMPRUNTE);

        //then

        assertThat(m1Books).isEqualTo(List.of(b1,b2));
        assertThat(m2Books).isEqualTo(List.of(b3,b4));




    }
}
