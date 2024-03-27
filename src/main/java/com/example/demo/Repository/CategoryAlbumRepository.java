package com.example.demo.Repository;

import com.example.demo.Entity.CategoryAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryAlbumRepository extends JpaRepository<CategoryAlbumEntity, Integer> {

    CategoryAlbumEntity findCategoryAlbumById(int categoryId);
}
