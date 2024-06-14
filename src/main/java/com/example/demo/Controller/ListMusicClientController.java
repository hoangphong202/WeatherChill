package com.example.demo.Controller;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/ListMusic")
public class ListMusicClientController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private CategoryService categoryService;
    @GetMapping("")
    public String GetAllMusic(@RequestParam(name = "ten", required = false) String ten,Model model, HttpSession session){
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        List<MusicEntity> listMusic = musicService.getAllMusic();
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        model.addAttribute("listMusic",listMusic);
        model.addAttribute("listCategory",listCategory);
        model.addAttribute("ten", ten);
        return loggedInUser != null ? "list_music_user" : "list_music_unjoin";


    }



    @PostMapping("/filter")
    public String Filter(@RequestParam(name = "ten", required = false) String ten,HttpSession session,Model model, @RequestParam int categoryId){
        System.out.println("Ktra: "+categoryId);
        if(categoryId==0){
            return "redirect:/ListMusic?ten=" + ten;
        }
        else{
            List<MusicEntity> listMusic = musicService.FilterByCategoryId(categoryId);
            session.setAttribute("filterMusic",listMusic);
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("listMusic", listMusic);
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("ten", ten);
            return "list_music_client";
        }
    }

}
