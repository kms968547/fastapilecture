package com.minsu.webservice.domain.book;

import com.minsu.webservice.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(nullable = false, unique = true, length = 17)
    private String isbn; // International Standard Book Number

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(length = 100)
    private String genre;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name = "total_copies")
    private Integer totalCopies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(nullable = false, length = 255) // Added publisher field
    private String publisher;

    @Column(length = 1000) // Added summary field, making it nullable for tests
    private String summary;

    @Column(nullable = false) // Added price field
    private Double price;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    protected Book() {}

    public Book(String title, String author, String isbn, Integer publicationYear, LocalDate publicationDate, String genre, Integer totalCopies, User seller, String publisher, String summary, Double price) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies; // Initially, all copies are available
        this.seller = seller;
        this.publisher = publisher; // Initialize publisher
        this.summary = summary; // Initialize summary
        this.price = price; // Initialize price
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public Integer getPublicationYear() { return publicationYear; }
    public LocalDate getPublicationDate() { return publicationDate; }
    public String getGenre() { return genre; }
    public Integer getAvailableCopies() { return availableCopies; }
    public Integer getTotalCopies() { return totalCopies; }
    public User getSeller() { return seller; } // Added getter for sellerId
    public String getPublisher() { return publisher; } // Added getter for publisher
    public String getSummary() { return summary; } // Added getter for summary
    public Double getPrice() { return price; } // Added getter for price
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters or update methods if needed (e.g., for lending/returning)
    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String author, Integer publicationYear, LocalDate publicationDate, String genre, Integer totalCopies, String publisher, String summary, Double price) {
        if (title != null) this.title = title;
        if (author != null) this.author = author;
        if (publicationYear != null) this.publicationYear = publicationYear;
        if (publicationDate != null) this.publicationDate = publicationDate;
        if (genre != null) this.genre = genre;
        if (totalCopies != null) {
            int oldTotal = this.totalCopies != null ? this.totalCopies : 0;
            int oldAvailable = this.availableCopies != null ? this.availableCopies : 0;
            this.totalCopies = totalCopies;
            this.availableCopies = Math.max(0, oldAvailable + (this.totalCopies - oldTotal)); // Adjust available copies
        }
        if (publisher != null) this.publisher = publisher; // Update publisher
        if (summary != null) this.summary = summary; // Update summary
        if (price != null) this.price = price; // Update price
        this.updatedAt = LocalDateTime.now();
    }
}
