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

        Member m1 = new Member();
        m1 = memberRepository.save(m1);
        Member m2 = new Member();
        m2 = memberRepository.save(m2);


        Book b1 = new Book();
        b1.setStatus(Status.EMPRUNTE);
        b1.setBorrower(m1);
        b1 = bookRepository.save(b1);
        Book b2 = new Book();
        b2.setStatus(Status.EMPRUNTE);
        b2.setBorrower(m1);
        b2 = bookRepository.save(b2);
        Book b3 = new Book();
        b3.setStatus(Status.EMPRUNTE);
        b3.setBorrower(m2);
        b3 = bookRepository.save(b3);
        Book b4 = new Book();
        b4.setStatus(Status.EMPRUNTE);
        b4.setBorrower(m2);
        b4 = bookRepository.save(b4);

        //when
        List<Book> m1Books = bookRepository.findByMemberAndStatus(m1.getId(), Status.EMPRUNTE);

        List<Book> m2Books = bookRepository.findByMemberAndStatus(m2.getId(), Status.EMPRUNTE);

        //then

        assertThat(m1Books).extracting(Book::getId)
                .containsExactlyInAnyOrder(b1.getId(), b2.getId());
        assertThat(m2Books).extracting(Book::getId)
                .containsExactlyInAnyOrder(b3.getId(), b4.getId());




    }
}
