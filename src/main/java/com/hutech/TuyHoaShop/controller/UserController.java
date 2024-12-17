package com.hutech.TuyHoaShop.controller;

import com.hutech.TuyHoaShop.model.Order;
import com.hutech.TuyHoaShop.model.User;
import com.hutech.TuyHoaShop.repository.OrderDetailRepository;
import com.hutech.TuyHoaShop.service.OrderService;
import com.hutech.TuyHoaShop.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final OrderService orderService;
    private final OrderDetailRepository orderDetailRepository;

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        // Check for binding errors
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register";
        }

        // Check for duplicate email
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailError", "Email already exists.");
            return "users/register";
        }

        // Check for duplicate username
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("usernameError", "Username already exists.");
            return "users/register";
        }

        // Check for duplicate phone number
        if (userService.phoneExists(user.getPhone())) {
            model.addAttribute("phoneError", "Phone number already exists.");
            return "users/register";
        }

        // Save user and set default role
        userService.save(user);
        userService.setDefaultRole(user.getUsername());

        return "redirect:/login";
    }


    @GetMapping("/profile")
    public String userProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        return "users/profile";
    }

    @GetMapping("/profile/edit/{username}")
    public String editProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        return "users/editProfile";
    }

    @PostMapping("/profile/edit/{username}")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUserByUsername(user.getUsername(), user);
        return "users/profile";
    }

    @GetMapping("/profile/history")
    public String userHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        List<Order> orders = orderService.getOrdersByUsername(currentUsername);
        model.addAttribute("orders", orders);
        return "users/history";
    }
}
