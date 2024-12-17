package com.hutech.TuyHoaShop.service;
import com.hutech.TuyHoaShop.model.CartItem;
import com.hutech.TuyHoaShop.model.Order;
import com.hutech.TuyHoaShop.model.OrderDetail;
import com.hutech.TuyHoaShop.repository.OrderDetailRepository;
import com.hutech.TuyHoaShop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrder(String customerName, String phoneNumber, String address, String email, String note, String paymentMethod, String status, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setPhoneNumber(phoneNumber);
        order.setAddress(address);
        order.setPaymentDateTime(LocalDateTime.now());
        order.setEmail(email);
        order.setNote(note);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("Đặt hàng thành công");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        order.setUsername(currentUsername);

        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProductName(item.getProduct().getName());
            detail.setProductPrice(item.getProduct().getPrice());
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }
        cartService.clearCart();
        return order;
    }

    public double calculateTotalAmount(Order order) {
        return order.getOrderDetails().stream()
                .mapToDouble(detail -> detail.getProductPrice() * detail.getQuantity())
                .sum();
    }

    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        return null; // or throw an exception if order is not found
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUsername(username);
    }

}
