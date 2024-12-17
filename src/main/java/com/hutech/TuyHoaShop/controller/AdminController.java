package com.hutech.TuyHoaShop.controller;

import com.hutech.TuyHoaShop.service.OrderService;
import com.hutech.TuyHoaShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping
    public String dashboard(Model model) {
        long totalAccounts = userService.getTotalAccounts();
        long accountsWithPurchase = userService.getAccountsWithPurchase();
        long totalSalesAmount = userService.getTotalSalesAmount();
        long totalOrders = userService.getTotalOrders();
        model.addAttribute("totalAccounts", totalAccounts);
        model.addAttribute("accountsWithPurchase", accountsWithPurchase);
        model.addAttribute("totalSalesAmount", totalSalesAmount);
        model.addAttribute("totalOrders", totalOrders);

        return "admin/index";
    }
}
