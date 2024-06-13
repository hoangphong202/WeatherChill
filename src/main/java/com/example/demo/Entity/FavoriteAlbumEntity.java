package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "favorite_album")
@Data
public class FavoriteAlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "album")
    private List<FavoriteAlbumInfoEntity> favoriteAlbums;
}
