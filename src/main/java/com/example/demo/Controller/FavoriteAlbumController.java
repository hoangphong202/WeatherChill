package com.example.demo.Controller;

import com.example.demo.Service.FavoriteAlbumInfoService;
import com.example.demo.Service.FavoriteAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/favoriteAlbum")
public class FavoriteAlbumController {
    @Autowired
    private FavoriteAlbumService favoriteAlbumService;

    @PostMapping("/insertMusic")
    public String insertMusic(Model model, @RequestParam int musicId, @RequestParam int favoriteAlbumId){
        return "/login";
    }

    @PostMapping("/deleteMusic")
    public String deleteMusic(Model model, @RequestParam int musicId, @RequestParam int favoriteAlbumId) {
        favoriteAlbumService.deleteMusic(musicId, favoriteAlbumId);
        return "/login";
    }
}
