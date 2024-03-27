package com.example.demo.Repository;

import com.example.demo.Entity.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Integer> {
    List<AlbumEntity> findBycategoryAlbum_Name(String categoryName);
    List<AlbumEntity> findByCategoryAlbum_Id(int categoryId);
    List<AlbumEntity> findAllAlbumByUser_Id(int userId);
    AlbumEntity findById(int albumId);
}
