package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity(name = "music")
public class MusicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "path")
    private String path;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private CategoryEntity category;


    @OneToMany(mappedBy = "music",fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private List<AlbumInfoEntity> albums;

    @OneToMany(mappedBy = "music")
    @ToString.Exclude
    @JsonIgnore
    private List<FavoriteAlbumInfoEntity> favoriteAlbums;

    @OneToMany(mappedBy = "music")
    @ToString.Exclude
    @JsonIgnore
    private List<HistoryInfoEntity> historys;
    public List<AlbumInfoEntity> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumInfoEntity> albums) {
        this.albums = albums;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
