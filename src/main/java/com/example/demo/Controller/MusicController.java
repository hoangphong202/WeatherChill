package com.example.demo.Controller;

import com.example.demo.DTO.FavoriteAlbumDTO;
import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.*;
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
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;
    @Autowired
    private AlbumInfoService albumInfoService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HistoryInfoService historyInfoService;
    @Autowired
    private FavoriteAlbumService favoriteAlbumService;
    @Autowired
    private FavoriteAlbumInfoService favoriteAlbumInfoService;
    @GetMapping("")
    public String GetAllMusic(HttpSession session,Model model){
        List<MusicEntity> listMusic = musicService.getAllMusic();
        List<CategoryEntity> listCategory = categoryService.getAllCategory();

        model.addAttribute("listMusic",listMusic);
        model.addAttribute("listCategory",listCategory);
        session.setAttribute("filterMusic",listMusic);
        return "list_music";
    }


    @GetMapping("/insert")
    public String InsertForm(Model model){
        model.addAttribute("listCategory", categoryService.getAllCategory());
        return "add_music";
    }

    @PostMapping("/insert")
    public String InsertMusic(@RequestParam("file") MultipartFile file, @RequestParam String name, @RequestParam String categoryId){
        try{
            System.out.println("Ktra: "+file.getOriginalFilename()+" - "+name+" - "+categoryId);
            if(musicService.InsertMusic(file, name, Integer.parseInt(categoryId))){
                System.out.println("Them nhac thanh cong");
                return "redirect:/music/insert";
            }
            else {
                System.out.println("Them nhac that bai");
                return "redirect:/music/insert";
            }
        }
        catch (Exception e){
            System.out.println("Loi them nhac: "+e.getLocalizedMessage());
            return "redirect:/music/insert";
        }
    }

    @GetMapping("/delete/{musicId}")
    public String deleteMusic(@PathVariable int musicId) {
        if(albumInfoService.deleteAlbumInfoByMusicId(musicId)){
            musicService.deleteMusicById(musicId);
            System.out.println("Xoa thanh cong");
            return "redirect:/music";
        }
        else {
            System.out.println("Xoa that bai");
            return "redirect:/music";
        }
    }

    @GetMapping("/update/{musicId}")
    public String updateMusic(Model model, @PathVariable int musicId) {
        System.out.println("MusicId: "+musicId);
        model.addAttribute("musicId", musicId);
        model.addAttribute("listCategory",categoryService.getAllCategory());
        return "update_music";
    }

    @PostMapping("/update")
    public String UpdateMusic(@RequestParam("file") MultipartFile file,
                              @RequestParam String name,
                              @RequestParam String categoryId,
                              @RequestParam String musicId){
        try{
            System.out.println("Ktra: "+ musicId +" - "+file.getOriginalFilename()+" - "+name+" - "+categoryId);
            if(musicService.updateMusicById(Integer.parseInt(musicId) , file, name, Integer.parseInt(categoryId))){
                System.out.println("Update nhac thanh cong");
                return "redirect:/music";
            }
            else {
                System.out.println("Them nhac that bai");
                return "redirect:/music";
            }
        }
        catch (Exception e){
            System.out.println("Loi update nhac: "+e.getLocalizedMessage());
            return "redirect:/music";
        }
    }

    @PostMapping("/filter")
    public String Filter(HttpSession session,Model model, @RequestParam int categoryId){
        System.out.println("Ktra: "+categoryId);
        if(categoryId==0){
            return "redirect:/music";
        }
        else{
            List<MusicEntity> listMusic = musicService.FilterByCategoryId(categoryId);
            session.setAttribute("filterMusic",listMusic);
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("listMusic", listMusic);
            model.addAttribute("listCategory", listCategory);
            return "list_music";
        }
    }

    @PostMapping("/filter/album")
    public String FilterAlbum(HttpSession session,Model model, @RequestParam int categoryId,@RequestParam int albumId){
        System.out.println("Ktra: "+categoryId);
        if(categoryId==0){
            List<MusicEntity> listMusic = musicService.getAllMusic();
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("listMusic", listMusic);
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("albumId", albumId);
            return "add_music_album";
        }
        else{
            List<MusicEntity> listMusic = musicService.FilterByCategoryId(categoryId);
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("listMusic", listMusic);
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("albumId", albumId);
            return "add_music_album";
        }
    }

    @GetMapping("/recommendMusic")
    public String  recommendMusic(HttpSession session, Model model) {
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        System.out.println(user.getName());
        int category = historyInfoService.getTopCategoryByMusicCount(user.getHistory().getId());
        int userFavoriteAlbumId = favoriteAlbumService.findFavoriteAlbumByHistoryId(user.getHistory().getId());
        List<FavoriteAlbumDTO> listFavoriteAlbum = favoriteAlbumInfoService.albumsHavingMostCategorySongs(category);
        List<MusicEntity> listMusic = musicService.listMusicRecommend(listFavoriteAlbum, userFavoriteAlbumId, category);
        System.out.println("List music recommend: ");
        for (MusicEntity music : listMusic) {
            System.out.println("MusicId: "+music.getId()+" Name: "+music.getName()+" Category name: "+music.getCategory().getName());
        }
        model.addAttribute("listMusic", listMusic);
        return "recommendMusic";
    }
}
