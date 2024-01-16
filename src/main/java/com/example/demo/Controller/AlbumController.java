package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicService musicService;

    @Autowired
    private ImageService imageService;


    @Autowired
    private AlbumInfoService albumInfoService;
    @GetMapping("")
    public String listAlbum(Model model){
        List<AlbumEntity> listAlbum = albumService.getAllAlbum();
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        model.addAttribute("listAlbum", listAlbum);
        model.addAttribute("listCategory", listCategory);
        return "list_album";
    }

    @GetMapping("/insert")
    public String insertAlbum(Model model) {
        model.addAttribute("listCategory", categoryService.getAllCategory());
        return "add_album";
    }

    @PostMapping("/insert")
    public String insertAlbum(@RequestParam("file") MultipartFile file, @RequestParam String name, @RequestParam String categoryId){
        try{
            System.out.println("Ktra: "+file.getOriginalFilename()+" - "+name+" - "+categoryId);
            if(albumService.InsertCover(file, name, Integer.parseInt(categoryId))){
                System.out.println("Them album thanh cong");
                return "redirect:/album/insert";
            }
            else {
                System.out.println("Them album that bai");
                return "redirect:/album/insert";
            }
        }
        catch (Exception e){
            System.out.println("Loi them album: "+e.getLocalizedMessage());
            return "redirect:/album/insert";
        }
    }
    @GetMapping("/listmusic/{albumId}")
    public String listMusic(HttpSession session, @PathVariable int albumId, Model model){
        List<AlbumInfoEntity> listMusicInAlbum = albumInfoService.findAllMusicByAlbumId(albumId);

        List<ImageEntity> listImage  = imageService.getAllImage();

        List<MusicEntity> listMusic = listMusicInAlbum.stream()
                .map(AlbumInfoEntity::getMusic)
                .collect(Collectors.toList());
        System.out.printf("List music size: "+listMusic.size());
        session.setAttribute("filterMusic",listMusic);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("albumId", albumId);

        model.addAttribute("listImage", listImage );

        return "album_music";
    }

    @GetMapping("/insertMusic")
    public String insertMusic(@RequestParam int albumId, Model model){
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<MusicEntity> listMusic = musicService.getAllMusic();
        model.addAttribute("albumId", albumId);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listMusic", listMusic);
        return "add_music_album";
    }

    @GetMapping("/insertMusic/filter")
    public String insertMusicFilter(@RequestParam int albumId, Model model){
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<MusicEntity> listMusic = musicService.getAllMusic();
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("albumId",albumId);
        System.out.printf("Ktra"+albumId);
        return "add_music_album";
    }


    @GetMapping("/insertMusic/{musicId}/{albumId}")
    public String insertMusic(@PathVariable(name = "musicId") int musicId, @PathVariable(name = "albumId") int albumId, Model model){
        if(albumInfoService.insertMusic(musicId,albumId)){
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            List<MusicEntity> listMusic = musicService.getAllMusic();

            model.addAttribute("listCategory",listCategory);
            model.addAttribute("listMusic",listMusic);
            model.addAttribute("albumId", albumId);
            System.out.println("Them nhac vao album thanh cong");
            return "add_music_album";
        }
        else {
            System.out.println("Them nhac vao album that bai");
            return "redirect:/album";
        }

    }





    @GetMapping("/delete/{albumId}")
    public String deleteMusic(@PathVariable int albumId) {
        System.out.println("albumId: "+albumId);
        if(albumInfoService.deleteAlbumInfoByAlbumId(albumId)){
            albumService.deleteAlbumById(albumId);
            System.out.println("Xoa thanh cong");
            return "redirect:/album";
        }
        else{
            System.out.println("Xoa that bai");
            return "redirect:/album";
        }
    }


    @GetMapping("/deleteMusic/{musicId}/{albumId}")
    public String deleteMusic(@PathVariable(name = "musicId") int musicId, @PathVariable(name = "albumId") int albumId, Model model){
        if(albumInfoService.deleteAlbumInfoByMusicIdAndAlbumId(musicId,albumId)){
            System.out.println("Xoa thanh cong");
            return "redirect:/album/listmusic/"+albumId;
        }
        else {
            System.out.println("Xoa that bai");
            return "redirect:/album/listmusic/"+albumId;
        }
    }

    @PostMapping("/filter")
    public String albumFilter(Model model,@RequestParam int categoryId){
        if(categoryId==0){
            return "redirect:/album";
        }
        else{
            List<AlbumEntity> listAlbum = albumService.findAllAlbumByCategoryId(categoryId);
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            model.addAttribute("listAlbum", listAlbum);
            model.addAttribute("listCategory", listCategory);
            return "list_album";
        }
    }

}
