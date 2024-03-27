package com.example.demo.Controller;

import com.example.demo.Entity.AlbumEntity;
import com.example.demo.Entity.CategoryAlbumEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.AlbumService;
import com.example.demo.Service.CategoryAlbumService;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home-page")
public class CategoryController {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private CategoryAlbumService categoryAlbumService;
    @Autowired
    private UserService userService;
    @Autowired
    private  UserRepository userRepository;

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            // Nếu người dùng đã đăng nhập, thêm thông tin người dùng vào model
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("ten", loggedInUser.getName());
            model.addAttribute("imgpath", loggedInUser.getImgpath());
        } else {
            // Nếu người dùng chưa đăng nhập, không cần thiết phải thêm thông tin người dùng vào model
            model.addAttribute("ten", "");
            model.addAttribute("imgpath", "");
        }

        // Lấy danh sách tất cả các thể loại
        List<CategoryAlbumEntity> listCategoryAlbum = categoryAlbumService.getAllCategoryAlbum();
        model.addAttribute("listCategoryAlbum", listCategoryAlbum);

        // Tạo một bản đồ để lưu trữ danh sách album top 5 theo thể loại
        Map<String, List<AlbumEntity>> top5AlbumsByCategory = new HashMap<>();
        for (CategoryAlbumEntity category : listCategoryAlbum) {
            // Lấy danh sách album trong thể loại và sắp xếp theo view count giảm dần
            List<AlbumEntity> albumsInCategory = albumService.getAlbumsByCategory(category.getName());

            // Sắp xếp danh sách album theo số lượt xem giảm dần
            albumsInCategory.sort(Comparator.comparingInt(AlbumEntity::getViewCount).reversed());

            // Lấy 5 album đầu tiên từ danh sách đã sắp xếp
            List<AlbumEntity> top5Albums = albumsInCategory.stream().limit(5).collect(Collectors.toList());

            // Lưu trữ danh sách 5 album vào bản đồ theo tên thể loại
            top5AlbumsByCategory.put(category.getName(), top5Albums);
        }



        List<AlbumEntity> albumEntities = albumService.getAllAlbum();
        // Sắp xếp danh sách album theo số lượt xem giảm dần
        albumEntities.sort(Comparator.comparingInt(AlbumEntity::getViewCount).reversed());
        // Lấy 5 album đầu tiên từ danh sách đã sắp xếp
        List<AlbumEntity> top5AlbumsByView= albumEntities.stream().limit(5).collect(Collectors.toList());



        // Truyền bản đồ top5AlbumsByCategory vào model
        model.addAttribute("albumsByCategory", top5AlbumsByCategory);

        model.addAttribute("top5AlbumsByView", top5AlbumsByView);

        // Trả về tên view tương ứng
        return loggedInUser != null ? "Category/category" : "Category/category_unjoined";
    }



}

