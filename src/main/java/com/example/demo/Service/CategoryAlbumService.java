package com.example.demo.Service;

import com.example.demo.Entity.CategoryAlbumEntity;
import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Repository.CategoryAlbumRepository;
import com.example.demo.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryAlbumService {
    @Autowired
    private CategoryAlbumRepository categoryAlbumRepository;
    public List<CategoryAlbumEntity> getAllCategoryAlbum(){
        return categoryAlbumRepository.findAll();
    }

    public CategoryAlbumEntity getCategoryAlbumById(int categoryId){
        return categoryAlbumRepository.findCategoryAlbumById(categoryId);
    }
}
