package com.hutech.TuyHoaShop.controller;

import com.hutech.TuyHoaShop.model.Product;
import com.hutech.TuyHoaShop.service.BrandService;
import com.hutech.TuyHoaShop.service.CategoryService;
import com.hutech.TuyHoaShop.service.ProductService;
import com.hutech.TuyHoaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("currentUser", currentUsername);
        model.addAttribute("products", productService.getTop6Products());
        return "home/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Product product = productService.getProductById(id).get();
        model.addAttribute("product", product);
        return "home/detail";
    }

    @GetMapping("/about")
    public String about() {

        return "home/about";
    }

    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(value = "category", required = false) Long categoryId,
                       @RequestParam(value = "brand", required = false) Long brandId,
                       @RequestParam(value = "q", required = false) String q) {
        if (categoryId != null) {
            model.addAttribute("products", productService.getProductsByCategoryId(categoryId));
        } else if (brandId != null) {
            model.addAttribute("products", productService.getProductsByBrandId(brandId));
        } else if (q != null) {
            model.addAttribute("products", productService.getProductsByName(q));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        return "home/shop";
    }

}