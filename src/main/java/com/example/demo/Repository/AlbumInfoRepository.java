package com.example.demo.Repository;

import com.example.demo.Entity.AlbumInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumInfoRepository extends JpaRepository<AlbumInfoEntity, Integer> {
    List<AlbumInfoEntity> findByAlbumId(int albumId);
    List<AlbumInfoEntity> findByMusicId(int musicId);

    AlbumInfoEntity findByMusicIdAndAlbumId(int musicId, int albumId);

}
