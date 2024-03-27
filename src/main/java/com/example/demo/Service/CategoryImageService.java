package com.example.demo.Service;

import com.example.demo.Entity.CategoryImageEntity;
import com.example.demo.Repository.CategoryImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryImageService {
    @Autowired
    private CategoryImageRepository categoryImageRepository;

    public List<CategoryImageEntity> findAllCategoryImage(){
        return categoryImageRepository.findAll();
    }

}
