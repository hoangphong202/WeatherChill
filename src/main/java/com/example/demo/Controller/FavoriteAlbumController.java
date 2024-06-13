package com.example.demo.Controller;

import com.example.demo.Entity.MusicEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.FavoriteAlbumInfoService;
import com.example.demo.Service.FavoriteAlbumService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/favoriteAlbum")
public class FavoriteAlbumController {
    @Autowired
    private FavoriteAlbumService favoriteAlbumService;

    @Autowired
    private FavoriteAlbumInfoService favoriteAlbumInfoService;

    @PostMapping("/insertMusic")
    public String insertMusic(Model model, HttpSession session, @RequestParam int musicId){
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        String result = favoriteAlbumInfoService.insertMusic(musicId, user.getAlbum().getId());
        System.out.println(result);
        return "redirect:/favoriteAlbum";
    }

    @PostMapping("/deleteMusic")
    public String deleteMusic(Model model,HttpSession session ,@RequestParam int musicId){
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        String result = favoriteAlbumInfoService.deleteMusic(musicId, user.getAlbum().getId());
        System.out.println(result);
        return "redirect:/favoriteAlbum";
    }
//    @PostMapping("/deleteMusic")
//    public String deleteMusic(Model model, @RequestParam int musicId, @RequestParam int favoriteAlbumId) {
//        favoriteAlbumService.deleteMusic(musicId, favoriteAlbumId);
//        return "/login";
//    }
    @GetMapping("")
    public String listFavorite(Model model, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        List<MusicEntity> listMusic = favoriteAlbumInfoService.getMusicByFavoriteAlbumId(user.getAlbum().getId());
        model.addAttribute("listMusic", listMusic);
        return "listFavorite";
    }
}
