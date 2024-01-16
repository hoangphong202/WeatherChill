package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.ToString;

import java.util.List;

@Entity(name = "Image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Column(name = "path")
    private String path;

	@Column(name = "info")
    private String info;

	@ManyToOne
	@JoinColumn(name = "category_image_id")
	private CategoryImageEntity categoryImage;

	@OneToMany(mappedBy = "image",fetch = FetchType.EAGER)
	@ToString.Exclude
	@JsonIgnore
	private List<AlbumInfoEntity> albums;

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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public CategoryImageEntity getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(CategoryImageEntity categoryImage) {
		this.categoryImage = categoryImage;
	}
}
