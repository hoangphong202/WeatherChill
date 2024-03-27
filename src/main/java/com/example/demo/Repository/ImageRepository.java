package com.example.demo.Repository;

import com.example.demo.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    // Các phương thức tùy chỉnh nếu cần
    ImageEntity findByid(int id);

    List<ImageEntity> findAllByCategoryImage_Id(int categoryImageId);
}
