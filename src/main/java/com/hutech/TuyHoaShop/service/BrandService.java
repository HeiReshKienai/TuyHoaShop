package com.hutech.TuyHoaShop.service;

import com.hutech.TuyHoaShop.model.Brand;
import com.hutech.TuyHoaShop.repository.BrandRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing brands.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {
    private final BrandRepository brandRepository;

    /**
     * Retrieve all brands from the database.
     * @return a list of brands
     */
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    /**
     * Retrieve a brand by its id.
     * @param id the id of the brand to retrieve
     * @return an Optional containing the found brand or empty if not found
     */
    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    /**
     * Add a new brand to the database.
     * @param brand the brand to add
     */
    public void addBrand(Brand brand) {
        brandRepository.save(brand);
    }

    /**
     * Update an existing brand.
     * @param brand the brand with updated information
     */
    public void updateBrand(@NotNull Brand brand) {
        Brand existingBrand = brandRepository.findById(brand.getId())
                .orElseThrow(() -> new IllegalStateException("Brand with ID " +
                        brand.getId() + " does not exist."));
        existingBrand.setName(brand.getName());
        brandRepository.save(existingBrand);
    }

    /**
     * Delete a brand by its id.
     * @param id the id of the brand to delete
     */
    public void deleteBrandById(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new IllegalStateException("Brand with ID " + id + " does not exist.");
        }
        brandRepository.deleteById(id);
    }
}
