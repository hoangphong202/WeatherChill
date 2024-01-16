package com.example.demo.Repository;

import com.example.demo.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    // Các phương thức tùy chỉnh nếu cần
    ImageEntity findByid(int id);
}
