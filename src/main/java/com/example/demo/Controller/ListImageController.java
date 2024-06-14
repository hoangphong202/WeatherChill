package com.example.demo.Controller;

import com.example.demo.Entity.CategoryImageEntity;
import com.example.demo.Entity.ImageEntity;
import com.example.demo.Repository.CategoryImageRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Service.CategoryImageService;
import com.example.demo.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ListImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryImageService categoryImageService;

    @GetMapping("/ListImage")
    public String showImageList(@RequestParam(name = "categoryFilter", required = false) Integer categoryImageId,
                                Model model) {

        List<ImageEntity> listImage  = imageService.getAllImage();

        List<CategoryImageEntity> listCategories = categoryImageService.findAllCategoryImage();


        // Nếu có lọc theo danh mục, chỉ giữ lại ảnh thuộc danh mục đó
        if (categoryImageId != null) {
            listImage = listImage.stream()
                    .filter(image -> Objects.equals(image.getCategoryImage().getId(), categoryImageId))
                    .collect(Collectors.toList());
        }

        Collections.reverse(listImage); // đảo list image

        // Đưa danh sách ảnh vào Model để hiển thị trong Thymeleaf
        model.addAttribute("listImage", listImage);
        model.addAttribute("listCategories", listCategories);

        return "list_image";
    }





}
