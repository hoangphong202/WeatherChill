package com.example.demo.Controller;

import com.example.demo.Entity.AlbumEntity;
import com.example.demo.Entity.AlbumInfoEntity;
import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Service.AlbumInfoService;
import com.example.demo.Service.AlbumService;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.MusicService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ListAlbum")
public class AlbumClientController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicService musicService;


    @Autowired
    private AlbumInfoService albumInfoService;
    @GetMapping("")
    public String listAlbum( @RequestParam(name = "ten", required = false) String ten,Model model){
        List<AlbumEntity> listAlbum = albumService.getAllAlbum();
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        model.addAttribute("listAlbum", listAlbum);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("ten", ten);
        return "list_album_client";
    }

    @GetMapping("/listmusic/{albumId}")
    public String listMusic(@RequestParam(name = "ten", required = false) String ten,
                            HttpSession session,
                            @PathVariable int albumId,
                            Model model){
        List<AlbumInfoEntity> listMusicInAlbum = albumInfoService.findAllMusicByAlbumId(albumId);
        List<MusicEntity> listMusic = listMusicInAlbum.stream()
                .map(AlbumInfoEntity::getMusic)
                .collect(Collectors.toList());
        System.out.printf("List music size: "+listMusic.size());
        session.setAttribute("filterMusic",listMusic);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("albumId", albumId);
        model.addAttribute("ten", ten);
        return "album_music_client";
    }


    @PostMapping("/filter")
    public String albumFilter(@RequestParam(name = "ten", required = false) String ten,Model model,@RequestParam int categoryId){
        if(categoryId==0){
            return "redirect:/ListAlbum?ten=" + ten;
        }
        else{
            List<AlbumEntity> listAlbum = albumService.findAllAlbumByCategoryId(categoryId);
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            model.addAttribute("listAlbum", listAlbum);
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("ten", ten);
            return "list_album_client";
        }
    }

}
