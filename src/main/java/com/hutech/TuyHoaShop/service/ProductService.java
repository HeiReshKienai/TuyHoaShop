package com.hutech.TuyHoaShop.service;

import com.hutech.TuyHoaShop.model.Product;
import com.hutech.TuyHoaShop.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    private final String uploadDir = "src/main/resources/static/uploads/";

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    // Retrieve all products from the database
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieve a product by its id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Add a new product to the database
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String imageName = saveImage(imageFile);
            product.setImageUrl(imageName);
        }
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(@NotNull Product product, MultipartFile imageFile) throws IOException {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " + product.getId() + " does not exist."));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setBrand(product.getBrand());

        if (!imageFile.isEmpty()) {
            String imageName = saveImage(imageFile);
            existingProduct.setImageUrl(imageName);
        }

        return productRepository.save(existingProduct);
    }

    // Delete a product by its id
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    // Save the image to the filesystem
    private String saveImage(MultipartFile imageFile) throws IOException {
        String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir + imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, imageFile.getBytes());

        return imageName;
    }
    public List<Product> getTop6Products() {
        Pageable top6 = PageRequest.of(0, 6);
        return productRepository.findTopProducts(top6);
    }
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    public List<Product> getProductsByBrandId(Long brandId) {
        return productRepository.findByBrandId(brandId);
    }
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
}
