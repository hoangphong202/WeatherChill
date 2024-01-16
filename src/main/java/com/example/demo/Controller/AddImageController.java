package com.example.demo.Controller;

import com.example.demo.Entity.CategoryImageEntity;
import com.example.demo.Entity.ImageEntity;
import com.example.demo.Repository.CategoryImageRepository;
import com.example.demo.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
public class AddImageController {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryImageRepository categoryImageRepository;

    @GetMapping("/AddImage")
    public String showForm(Model model) {

        List<CategoryImageEntity> listCategories = categoryImageRepository.findAll();


        // Create a new Image instance and add it to the model
        model.addAttribute("imageEntity", new ImageEntity());
        model.addAttribute("listCategories", listCategories);
        return "addimage";
    }


    private static final String UPLOAD_DIR = "src/main/resources/static/Image/";

    @PostMapping("/AddImage")
    public String addImage(@ModelAttribute ImageEntity imageEntity,
                           @RequestParam("file") MultipartFile file,
                           @RequestParam("categoryFilter") Integer categoryId) throws IOException {
        // Save the file to the "src/main/resources/static/Image/" folder
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Path.of(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set the path in the Image object
            imageEntity.setPath("/Image/" + fileName);
        }

        // Set the categoryImage in the ImageEntity object using the repository
        CategoryImageEntity categoryImage = categoryImageRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found")); // Handle appropriately
        imageEntity.setCategoryImage(categoryImage);

        // Save the Image object to the database
        imageRepository.save(imageEntity);

        return "redirect:/ListImage";
    }





}
