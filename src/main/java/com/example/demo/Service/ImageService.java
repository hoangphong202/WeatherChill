package com.example.demo.Service;

import com.example.demo.Entity.ImageEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    // Lấy danh sách ảnh từ Repository
    public List<ImageEntity> getAllImage(){
        return imageRepository.findAll();
    }

    public List<ImageEntity> findImagesByCategoryId(int categoryImageId){
        return imageRepository.findAllByCategoryImage_Id(categoryImageId);
    }

}
