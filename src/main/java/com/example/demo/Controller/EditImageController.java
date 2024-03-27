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

import java.io.IOException;
import java.util.List;

@Controller
public class EditImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryImageRepository categoryImageRepository;

    @GetMapping("/edit/{id}")
    public String editImage(@PathVariable int id, Model model) {
        // Sử dụng phương thức findById của ImageRepository để lấy ảnh theo ID
        // Giả sử ImageEntity của bạn có một trường "id" kiểu int
        ImageEntity imageEntity = imageRepository.findById(id).orElse(null);

        // Kiểm tra xem ảnh có được tìm thấy không
        if (imageEntity != null) {
            model.addAttribute("imageEntity", imageEntity);

            // Lấy danh sách tất cả các danh mục
            List<CategoryImageEntity> listCategories = categoryImageRepository.findAll();
            model.addAttribute("listCategories", listCategories);

            return "edit-image"; // Giả sử bạn có một tệp HTML có tên "edit-image.html"
        } else {
            // Xử lý trường hợp khi không tìm thấy ảnh, chuyển hướng hoặc hiển thị một trang lỗi
            return "redirect:/ListImage"; // Chuyển hướng đến danh sách ảnh, điều chỉnh tùy theo nhu cầu
        }
    }

    @PostMapping("/update/{id}")
    public String updateImage(@PathVariable int id,
                              @ModelAttribute ImageEntity updatedImage) {
        // Lấy ảnh từ cơ sở dữ liệu
        ImageEntity imageEntity = imageRepository.findById(id).orElse(null);

        // Kiểm tra xem ảnh có được tìm thấy không
        if (imageEntity != null) {
            // Cập nhật thông tin ảnh
            imageEntity.setInfo(updatedImage.getInfo());


            // Kiểm tra xem danh mục đã được chọn hay chưa
            if (updatedImage.getCategoryImage() != null) {
                // Lấy danh mục từ cơ sở dữ liệu dựa trên ID được chọn từ dropdown
                CategoryImageEntity selectedCategory = categoryImageRepository.findById(updatedImage.getCategoryImage().getId()).orElse(null);

                // Kiểm tra xem danh mục có tồn tại không
                if (selectedCategory != null) {
                    // Cập nhật danh mục cho ảnh
                    imageEntity.setCategoryImage(selectedCategory);
                }
            }

            // Lưu thay đổi vào cơ sở dữ liệu
            imageRepository.save(imageEntity);
//            System.out.println("sua anh thanh cong");
        }

        return "redirect:/ListImage";
    }


}
