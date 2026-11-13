package com.example.testit.repository;

import com.example.testit.model.Book;
import com.example.testit.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.borrower.id = :memberId AND b.status = :status")
    List<Book> findByMemberAndStatus(@Param("memberId") Long memberId, @Param("status") Status status);

    List<Book> findByBorrowerId(Long memberId);
}
