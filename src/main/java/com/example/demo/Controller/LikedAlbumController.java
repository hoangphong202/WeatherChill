package com.example.demo.Controller;

import com.example.demo.Entity.CategoryImageEntity;
import com.example.demo.Entity.ImageEntity;
import com.example.demo.Entity.LikeAlbumEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.LikeAlbumService;
import jakarta.persistence.Access;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/LikedAlbum")
public class LikedAlbumController {


    @Autowired
    private LikeAlbumService likeAlbumService;
    @GetMapping("")
    public String listLikeAlbum( HttpSession session,
                                 Model model){


        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        // Sử dụng toán tử ba ngôi để kiểm tra và lấy tên, trả về chuỗi rỗng nếu loggedInUser là null
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        int userId = (loggedInUser != null) ? loggedInUser.getId() : 1;

        List<LikeAlbumEntity> listAlbum = likeAlbumService.getAllLikeAlbumByIdUser(userId);
        Collections.reverse(listAlbum); // đảo list album

        model.addAttribute("listAlbum", listAlbum);

        model.addAttribute("userId", userId);
        return "list_LikedAlbum_client";
    }


    @GetMapping("/insertLikeAlbum/{userId}/{albumId}")
    public String insertImage(@PathVariable(name = "userId") int userId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){

        likeAlbumService.insertLikeAlbum(userId,albumId);

            return "redirect:/LikedAlbum";


    }

    // xóa nhạc trong album
    @GetMapping("/deleteLikeAlbum/{userId}/{albumId}")
    public String deleteMusic(@PathVariable(name = "userId") int userId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){
        likeAlbumService.deleteLikeAlbum(userId, albumId);
        return "redirect:/LikedAlbum";
    }



}
