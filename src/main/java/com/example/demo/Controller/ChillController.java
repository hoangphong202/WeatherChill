package com.example.demo.Controller;

import com.example.demo.Entity.ImageEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Service.MusicService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Collections;
import java.util.List;

@Controller
public class ChillController {

    @Autowired
    private ImageRepository imageRepository;


    @Autowired
    private MusicService musicService;

    @GetMapping("/showImage/{albumId}")
    public String showImage(@RequestParam(name = "ten", required = false) String ten,
                            @PathVariable int albumId,
                            HttpSession session,
                            Model model) {
        // Fetch all images from the repository
        List<ImageEntity> LisImagesAll = imageRepository.findAll();

        // Get filtered images from the session (if any)
        List<ImageEntity> LisImages = (List<ImageEntity>) session.getAttribute("filterImage");

        // Get filtered music from the session (if any)
        List<MusicEntity> listMusic = (List<MusicEntity>) session.getAttribute("filterMusic");

        // Get all music
        List<MusicEntity> listMusicAll = musicService.getAllMusic();

        // Shuffle the list based on the condition
        if (LisImages != null) {
            Collections.shuffle(LisImages);
        } else {
            Collections.shuffle(LisImagesAll);
        }

        // Add attributes to the model
        model.addAttribute("LisImagesAll", LisImagesAll);
        model.addAttribute("LisImages", LisImages);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("listMusicAll", listMusicAll);
        model.addAttribute("albumId", albumId);
        model.addAttribute("ten", ten);

        // Return the view name (in this case, "index")
        return "index";
    }


    @GetMapping("/showImage-admin/{albumId}")
    public String showImageAdmin(@RequestParam(name = "ten", required = false) String ten,
                            @PathVariable int albumId,
                            HttpSession session,
                            Model model) {
        // Fetch all images from the repository
        List<ImageEntity> LisImagesAll = imageRepository.findAll();

        // Get filtered images from the session (if any)
        List<ImageEntity> LisImages = (List<ImageEntity>) session.getAttribute("filterImage");

        // Get filtered music from the session (if any)
        List<MusicEntity> listMusic = (List<MusicEntity>) session.getAttribute("filterMusic");

        // Get all music
        List<MusicEntity> listMusicAll = musicService.getAllMusic();

        // Shuffle the list based on the condition
        if (LisImages != null) {
            Collections.shuffle(LisImages);
        } else {
            Collections.shuffle(LisImagesAll);
        }

        // Add attributes to the model
        model.addAttribute("LisImagesAll", LisImagesAll);
        model.addAttribute("LisImages", LisImages);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("listMusicAll", listMusicAll);
        model.addAttribute("albumId", albumId);
        model.addAttribute("ten", ten);

        // Return the view name (in this case, "index")
        return "index_admin";
    }


}
