package com.minsu.webservice.domain.order;

import com.minsu.webservice.domain.book.Book;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price; // Price at the time of order

    // Constructors
    protected OrderItem() {}

    public OrderItem(Order order, Book book, Integer quantity, Double price) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public Book getBook() { return book; }
    public Integer getQuantity() { return quantity; }
    public Double getPrice() { return price; }

    // Setter for order (used in Order.addOrderItem)
    public void setOrder(Order order) {
        this.order = order;
    }
}
