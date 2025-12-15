package com.minsu.webservice.domain.order;

import com.minsu.webservice.domain.order.dto.CreateOrderRequest;
import com.minsu.webservice.domain.order.dto.OrderResponse;
import com.minsu.webservice.global.dto.PageRequest;
import com.minsu.webservice.global.dto.SortCriteria;
import com.minsu.webservice.global.error.ApiResponse;
import com.minsu.webservice.global.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // User Endpoints
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid CreateOrderRequest request
    ) {
        OrderResponse createdOrder = orderService.createOrder(userPrincipal.userId(), request);
        return ApiResponse.created("ORDER_CREATED", "Order created successfully", createdOrder);
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponse> getOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        OrderResponse order = orderService.getOrder(id, userPrincipal.userId());
        return ApiResponse.ok("ORDER_FOUND", "Order found successfully", order);
    }

    @GetMapping("/orders")
    public ApiResponse<Page<OrderResponse>> listMyOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "orderDate,DESC") String sort
    ) {
        PageRequest pageRequest = new PageRequest(page, size);
        SortCriteria sortCriteria = new SortCriteria(sort);
        Page<OrderResponse> orders = orderService.listOrders(userPrincipal.userId(), status, pageRequest, sortCriteria);
        return ApiResponse.ok("USER_ORDERS_LISTED", "User orders listed successfully", orders);
    }

    @PatchMapping("/orders/{id}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam OrderStatus newStatus // Assuming a simple status update (e.g., CANCEL)
    ) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, userPrincipal.userId(), newStatus);
        return ApiResponse.ok("ORDER_STATUS_UPDATED", "Order status updated successfully", updatedOrder);
    }

    @DeleteMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        orderService.deleteOrder(id, userPrincipal.userId());
        return ApiResponse.noContent("ORDER_DELETED", "Order deleted successfully");
    }

    // Admin Endpoints
    @GetMapping("/admin/orders")
    public ApiResponse<Page<OrderResponse>> adminListOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "orderDate,DESC") String sort
    ) {
        PageRequest pageRequest = new PageRequest(page, size);
        SortCriteria sortCriteria = new SortCriteria(sort);
        Page<OrderResponse> orders = orderService.adminListOrders(status, pageRequest, sortCriteria);
        return ApiResponse.ok("ADMIN_ORDERS_LISTED", "Admin orders listed successfully", orders);
    }

    @DeleteMapping("/admin/orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> adminDeleteOrder(@PathVariable Long id) {
        orderService.adminDeleteOrder(id);
        return ApiResponse.noContent("ADMIN_ORDER_DELETED", "Admin deleted order successfully");
    }
}
