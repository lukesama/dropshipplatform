package com.jnu.dropshipplatform.repository;

import com.jnu.dropshipplatform.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
}
