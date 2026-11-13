package com.example.testit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DISPONIBLE;

    /** Dernier membre ayant emprunté le livre (null tant qu'il n'a jamais été emprunté). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Member borrower;

    public Book() {}

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Member getBorrower() { return borrower; }
    public void setBorrower(Member borrower) { this.borrower = borrower; }
}
