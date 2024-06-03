package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "favorite_album_info")
@Data
public class FavoriteAlbumInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "favorite_album_id")
    private FavoriteAlbumEntity album;

    @ManyToOne
    @JoinColumn(name = "music_id")
    private MusicEntity music;
}
