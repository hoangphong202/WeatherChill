package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity(name = "album")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "img_path")
    private String imgPath;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "album",fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private List<AlbumInfoEntity> albums;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<AlbumInfoEntity> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumInfoEntity> albums) {
        this.albums = albums;
    }
}
