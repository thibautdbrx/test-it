package com.example.testit.service;

import com.example.testit.adapter.mail.MailService;
import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MailService mailService;

    public BookService(BookRepository bookRepository, MemberRepository memberRepository, MailService mailService) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.mailService = mailService;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findByMemberId(Long memberId) {
        return bookRepository.findByBorrowerId(memberId);
    }

    public Book createBook(String title, String author) {
        return bookRepository.save(new Book(title, author));
    }

    public Book updateBook(Book book) {
        if (book.getId() == null || !bookRepository.existsById(book.getId())) {
            throw new IllegalArgumentException("Book not found");
        }
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        if (book.getStatus() == Status.EMPRUNTE) {
            throw new IllegalStateException("Book can only be borrowed if status is DISPONIBLE");
        }
        // Vérifier si le membre a déjà un livre EMPRUNTE
        List<Book> ongoingLoans = bookRepository.findByMemberAndStatus(memberId, Status.RENDU);
        if (!ongoingLoans.isEmpty()) {
            throw new IllegalStateException("Member cannot borrow more than one book at a time");
        }
        mailService.sendMail(member,"Emprunt livre " + book.getTitle(), "Vous avez emprunter le livre");
        book.setStatus(Status.EMPRUNTE);
        book.setBorrower(member);
        Book saved = bookRepository.save(book);
        return saved;
    }

    public Book returnBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getStatus() != Status.EMPRUNTE) {
            throw new IllegalStateException("Book can only be returned if status is EMPRUNTE");
        }
        Member member = book.getBorrower();
        book.setStatus(Status.RENDU);
        Book saved = bookRepository.save(book);
        mailService.sendMail(member, "Livre rendu", "Merci d'avoir rendu '" + book.getTitle() + "'.");
        // Prévenir aussi le bibliothécaire référent si présent
        if (member.getReferent() != null) {
            mailService.sendMail(member, "Livre rendu par un de vos membres",
                    "'" + book.getTitle() + "' a été rendu par " + member.getUsername() + ".");
        }
        return saved;
    }
}
