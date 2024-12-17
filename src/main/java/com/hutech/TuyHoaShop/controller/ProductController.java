package com.hutech.TuyHoaShop.controller;

import com.hutech.TuyHoaShop.model.Product;
import com.hutech.TuyHoaShop.service.BrandService;
import com.hutech.TuyHoaShop.service.CategoryService;
import com.hutech.TuyHoaShop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    // Display a list of all products
    @GetMapping("/products")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/products/product-list";
    }

    // For adding a new product
    @GetMapping("/products-add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        return "/products/add-product";
    }

    // Process the form for adding a new product
    @PostMapping("/products-add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }
        try {
            productService.addProduct(product, imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "/products/add-product";
        }
        return "redirect:/products";
    }

    // For editing a product
    @GetMapping("/products-edit-{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        return "/products/update-product";
    }

    // Process the form for updating a product
    @PostMapping("/products-update-{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            product.setId(id);
            return "/products/update-product";
        }
        try {
            productService.updateProduct(product, imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "/products/update-product";
        }
        return "redirect:/products";
    }

    // Handle request to delete a product
    @GetMapping("/products-delete-{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}
