package com.example.demo.Controller;

import com.example.demo.Entity.ImageEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.MusicRepository;
import com.example.demo.Service.HistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private MusicRepository musicRepository;


    @PostMapping("/insert")
    public String insertHistory(Model model, @RequestParam String musicId, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        if(historyService.insertHistory(Integer.parseInt(musicId), user.getHistory().getId())){
            System.out.println("Insert history success");
            List<ImageEntity> LisImagesAll = imageRepository.findAll();
            List<ImageEntity> images = imageRepository.findAll();
            Random random = new Random();
            ImageEntity image = images.get(random.nextInt(images.size()));
            MusicEntity music = musicRepository.findById(Integer.parseInt(musicId)).orElseThrow(()->new RuntimeException("Music not found"));
            model.addAttribute("music",music);
            model.addAttribute("background",image);
            return "musicPlayer";
        }
        else {
            System.out.println("insert history fail");
            return "reditect:/ListMusic";
        }
    }


}
