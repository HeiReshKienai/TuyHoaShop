package com.hutech.TuyHoaShop.controller;

import com.hutech.TuyHoaShop.model.Brand;
import com.hutech.TuyHoaShop.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BrandController {
    @Autowired
    private final BrandService brandService;

    @GetMapping("/brands-add")
    public String showAddForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "/brands/add-brand";
    }

    @PostMapping("/brands-add")
    public String addBrand(@Valid Brand brand, BindingResult result) {
        if (result.hasErrors()) {
            return "/brands/add-brand";
        }
        brandService.addBrand(brand);
        return "redirect:/brands";
    }

    // Hiển thị danh sách thương hiệu
    @GetMapping("/brands")
    public String listBrands(Model model) {
        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        return "/brands/brands-list";
    }

    // GET request to show brand edit form
    @GetMapping("/brands-edit-{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Brand brand = brandService.getBrandById(id).orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + id));
        model.addAttribute("brand", brand);
        return "/brands/update-brand";
    }

    // POST request to update brand
    @PostMapping("/brands-update-{id}")
    public String updateBrand(@PathVariable("id") Long id, @Valid Brand brand, BindingResult result, Model model) {
        if (result.hasErrors()) {
            brand.setId(id);
            return "/brands/update-brand";
        }
        brandService.updateBrand(brand);
        model.addAttribute("brands", brandService.getAllBrands());
        return "redirect:/brands";
    }

    // GET request for deleting brand
    @GetMapping("/brands-delete-{id}")
    public String deleteBrand(@PathVariable("id") Long id, Model model) {
        Brand brand = brandService.getBrandById(id).orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + id));
        brandService.deleteBrandById(id);
        model.addAttribute("brands", brandService.getAllBrands());
        return "redirect:/brands";
    }
}
