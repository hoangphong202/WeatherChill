package com.example.demo.Controller;

import com.example.demo.Entity.AlbuminfoImageEntity;
import com.example.demo.Entity.ImageEntity;
import com.example.demo.Repository.AlbumInfoImageRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Service.AlbumInfoImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Controller
public class DeleteImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AlbumInfoImageRepository albumInfoImageRepository;
    @GetMapping("/delete/{id}")
    public String deleteImage(@PathVariable("id") int id,
                              RedirectAttributes redirectAttributes) {

        List<AlbuminfoImageEntity> albumInfoList = albumInfoImageRepository.findByImageId(id);
        albumInfoImageRepository.deleteAll(albumInfoList);

        // Kiểm tra xem ảnh có tồn tại hay không
        Optional<ImageEntity> optionalImage = imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            // Xóa file từ thư mục
            ImageEntity imageEntity = optionalImage.get();

            String imagePath = "src/main/resources/static" + imageEntity.getPath();
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }


            // Xóa ảnh từ cơ sở dữ liệu
            imageRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("success", "Đã xóa ảnh thành công");
        } else {
            // Nếu không tồn tại, thông báo lỗi
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy ảnh để xóa");
        }

        // Chuyển hướng về trang danh sách ảnh
        return "redirect:/ListImage";
    }
}
