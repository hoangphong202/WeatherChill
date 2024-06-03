package com.example.demo.Repository;

import com.example.demo.Entity.AlbumInfoEntity;
import com.example.demo.Entity.AlbuminfoImageEntity;
import com.example.demo.Entity.LikeAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeAlbumRepository extends JpaRepository<LikeAlbumEntity, Integer> {
    List<LikeAlbumEntity> findByUserId(int userId);
    List<LikeAlbumEntity> findByAlbumId(int albumId);

    LikeAlbumEntity findByUserIdAndAlbumId(int userId, int albumId);
    List<LikeAlbumEntity> findAllByUserIdAndAlbumId(int userId, int albumId);

}
