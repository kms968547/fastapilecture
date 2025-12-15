package com.minsu.webservice.domain.order;

import com.minsu.webservice.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`") // Use backticks if "order" is a reserved keyword in your DB
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // e.g., PENDING, COMPLETED, CANCELLED

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Constructors
    protected Order() {}

    public Order(User user) {
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    // Helper method to add OrderItem
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // Setter for status (e.g., for updating order status)
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
