package com.example.demo.Repository;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.CategoryImageEntity;
import com.example.demo.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryImageRepository extends JpaRepository<CategoryImageEntity, Integer> {
    CategoryImageEntity findByid(int id);
}
