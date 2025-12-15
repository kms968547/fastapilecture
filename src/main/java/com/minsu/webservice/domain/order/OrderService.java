package com.minsu.webservice.domain.order;

import com.minsu.webservice.domain.book.Book;
import com.minsu.webservice.domain.book.BookRepository;
import com.minsu.webservice.domain.order.dto.CreateOrderRequest;
import com.minsu.webservice.domain.order.dto.OrderItemDto;
import com.minsu.webservice.domain.order.dto.OrderResponse;
import com.minsu.webservice.domain.user.User;
import com.minsu.webservice.domain.user.UserRepository;
import com.minsu.webservice.global.dto.PageRequest;
import com.minsu.webservice.global.dto.SortCriteria;
import com.minsu.webservice.global.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.minsu.webservice.global.error.ErrorCode.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        Order order = new Order(user);
        order = orderRepository.save(order); // Save order first to get an ID

        for (OrderItemDto itemDto : request.items()) {
            Book book = bookRepository.findById(itemDto.bookId())
                    .orElseThrow(() -> new ApiException(BOOK_NOT_FOUND));
            
            // Basic stock check
            if (book.getAvailableCopies() < itemDto.quantity()) {
                throw new ApiException(BAD_REQUEST); // TODO: Specific error code for insufficient stock
            }
            book.setAvailableCopies(book.getAvailableCopies() - itemDto.quantity());
            bookRepository.save(book); // Update book stock

            OrderItem orderItem = new OrderItem(order, book, itemDto.quantity(), book.getPrice());
            order.addOrderItem(orderItem); // Add to order's item list
            orderItemRepository.save(orderItem);
        }

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ORDER_NOT_FOUND)); // Use specific error

        if (!order.getUser().getId().equals(userId)) {
            throw new ApiException(FORBIDDEN); // User can only view their own orders
        }

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> listOrders(Long userId, OrderStatus status, PageRequest pageRequest, SortCriteria sortCriteria) {
        Specification<Order> spec = OrderSpecification.filterByUser(userId);
        if (status != null) {
            spec = spec.and(OrderSpecification.filterByStatus(status));
        }

        Pageable pageable = pageRequest.toPageable(sortCriteria.toSort());
        return orderRepository.findAll(spec, pageable).map(OrderResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> adminListOrders(OrderStatus status, PageRequest pageRequest, SortCriteria sortCriteria) {
        Specification<Order> spec = Specification.where(null);
        if (status != null) {
            spec = spec.and(OrderSpecification.filterByStatus(status));
        }

        Pageable pageable = pageRequest.toPageable(sortCriteria.toSort());
        return orderRepository.findAll(spec, pageable).map(OrderResponse::from);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, Long userId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId)) {
            throw new ApiException(FORBIDDEN);
        }

        // Add business logic for status transitions if needed
        order.setStatus(newStatus);
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public void deleteOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId)) {
            throw new ApiException(FORBIDDEN);
        }
        
        // Before deleting order, restore book stock (if PENDING/PROCESSING)
        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PROCESSING) {
            for (OrderItem item : order.getOrderItems()) {
                Book book = item.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + item.getQuantity());
                bookRepository.save(book);
            }
        }

        orderRepository.delete(order);
    }

    @Transactional
    public void adminDeleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ORDER_NOT_FOUND));

        // Before deleting order, restore book stock (if PENDING/PROCESSING)
        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PROCESSING) {
            for (OrderItem item : order.getOrderItems()) {
                Book book = item.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + item.getQuantity());
                bookRepository.save(book);
            }
        }
        orderRepository.delete(order);
    }
}
