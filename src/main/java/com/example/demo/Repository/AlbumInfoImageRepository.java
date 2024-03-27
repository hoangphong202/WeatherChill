package com.example.demo.Repository;

import com.example.demo.Entity.AlbumInfoEntity;
import com.example.demo.Entity.AlbuminfoImageEntity;
import com.example.demo.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumInfoImageRepository extends JpaRepository<AlbuminfoImageEntity, Integer> {

    List<AlbuminfoImageEntity> findByAlbumId(int albumId);

    List<AlbuminfoImageEntity> findByImageId(int id);

    AlbuminfoImageEntity findByImageIdAndAlbumId(int imageId, int albumId);

    boolean existsByImageIdAndAlbumId(int imageId, int albumId);
}
