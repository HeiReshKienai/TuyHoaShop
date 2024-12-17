package com.hutech.TuyHoaShop.controller;

import com.hutech.TuyHoaShop.model.CartItem;
import com.hutech.TuyHoaShop.model.Order;
import com.hutech.TuyHoaShop.service.CartService;
import com.hutech.TuyHoaShop.service.EmailService;
import com.hutech.TuyHoaShop.service.OrderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/order/checkout")
    public String checkout() {
        return "/cart/checkout";
    }

    @PostMapping("/order/submit")
    public String submitOrder(
            String customerName,
            String phoneNumber,
            String address,
            String email,
            String note,
            String paymentMethod,
            String status,
            RedirectAttributes redirectAttributes) throws MessagingException {

        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect nếu giỏ hàng rỗng
        }

        // Tạo đơn hàng
        Order order = orderService.createOrder(customerName, phoneNumber, address, email, note, paymentMethod, status, cartItems);
        redirectAttributes.addAttribute("orderId", order.getId());

        long orderId1 = order.getId();


        // Gửi email xác nhận với thông tin đơn hàng chi tiết
        emailService.sendOrderConfirmationEmail(email, orderId1);

        return "redirect:/order/confirmation";
    }




    @GetMapping("/order/confirmation")
    public String orderConfirmation(Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/cart";
        }
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", order.getOrderDetails());
        model.addAttribute("totalAmount", orderService.calculateTotalAmount(order));
        return "cart/order-confirmation";
    }

    @GetMapping("/order-list")
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("orderService", orderService); // Add orderService to the model
        return "order/order-list";
    }
    @GetMapping("/order-create")
    public String showCreateOrderForm(Model model) {
        // Populate dropdowns for cities, districts, wards
//        model.addAttribute("cities", cityService.getAllCities());
//        model.addAttribute("districts", districtService.getAllDistricts());
//        model.addAttribute("wards", wardService.getAllWards());
        return "order/order-create";
    }
    @PostMapping("/order-update-status")
    public String updateOrderStatus(Long orderId, String newStatus, RedirectAttributes redirectAttributes) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        if (updatedOrder == null) {
            // Handle if order is not found
            return "redirect:/order/list";
        }
        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/order/confirmation";
    }
}
