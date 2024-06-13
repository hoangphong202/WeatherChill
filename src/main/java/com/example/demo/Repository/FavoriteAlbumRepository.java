package com.example.demo.Repository;

import com.example.demo.Entity.FavoriteAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteAlbumRepository extends JpaRepository<FavoriteAlbumEntity, Integer> {
}
