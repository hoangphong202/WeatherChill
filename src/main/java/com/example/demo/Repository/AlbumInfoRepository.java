package com.example.demo.Repository;

import com.example.demo.Entity.AlbumInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumInfoRepository extends JpaRepository<AlbumInfoEntity, Integer> {
    List<AlbumInfoEntity> findByAlbumId(int albumId);
    List<AlbumInfoEntity> findByMusicId(int musicId);

    AlbumInfoEntity findByMusicIdAndAlbumId(int musicId, int albumId);

    boolean existsByMusicIdAndAlbumId(int musicId, int albumId);
}
