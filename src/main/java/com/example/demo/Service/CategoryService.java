package com.example.demo.Service;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<CategoryEntity> getAllCategory(){
        return categoryRepository.findAll();
    }
}
