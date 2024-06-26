package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="name")
    private String name;

    @Column(name ="email")
    private String email;

    @Column(name ="password")
    private String password;

    @Column(name ="descriptions")
    private String describe;

    @Column(name ="img_path")
    private String imgpath;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private List<LikeAlbumEntity> LikeAlbum;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private FavoriteAlbumEntity album;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private HistoryEntity history;

    public List<LikeAlbumEntity> getLikeAlbum() {
        return LikeAlbum;
    }

    public void setLikeAlbum(List<LikeAlbumEntity> likeAlbum) {
        LikeAlbum = likeAlbum;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }
}
