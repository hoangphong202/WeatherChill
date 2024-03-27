package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.AlbumInfoRepository;
import com.example.demo.Repository.AlbumRepository;
import com.example.demo.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ListAlbum")
public class AlbumClientController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryAlbumService categoryAlbumService;
    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private MusicService musicService;

    @Autowired
    private AlbumInfoRepository albumInfoRepository;

    @Autowired
    private AlbumInfoImageService albumInfoImageService;

    @Autowired
    private AlbumInfoService albumInfoService;

//    @GetMapping("")
//    public String listAlbum( @RequestParam(name = "ten", required = false) String ten,
//                             HttpSession session,
//                             Model model){
//        List<AlbumEntity> listAlbum = albumService.getAllAlbum();
//
//
//        model.addAttribute("listAlbum", listAlbum);
//
//        model.addAttribute("ten", ten);
//        return "list_album_client";
//    }
    @GetMapping("")
    public String listAlbum( HttpSession session,
                             Model model){
        List<AlbumEntity> listAlbum = albumService.getAllAlbum();

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        // Sử dụng toán tử ba ngôi để kiểm tra và lấy tên, trả về chuỗi rỗng nếu loggedInUser là null
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";

        Collections.reverse(listAlbum); // đảo list album

        model.addAttribute("listAlbum", listAlbum);

        model.addAttribute("ten", ten);
        return "list_album_client";
    }

    @GetMapping("/listmusic/{albumId}")
    public String listMusic(@RequestParam(name = "ten", required = false) String ten,
                            HttpSession session,
                            @PathVariable int albumId,
                            Model model){

        // Lấy album cụ thể theo albumId và tăng lượt view cho album đó
        AlbumEntity album = albumService.findAlbumById(albumId);
        if (album != null) {
            album.setViewCount(album.getViewCount() + 1);
            albumRepository.save(album);
        }

        List<AlbumInfoEntity> listMusicInAlbum = albumInfoService.findAllMusicByAlbumId(albumId);
        List<AlbuminfoImageEntity> listImageInAlbum = albumInfoImageService.findAllImageByAlbumId(albumId);

        List<MusicEntity> listMusic = listMusicInAlbum.stream()
                .map(AlbumInfoEntity::getMusic)
                .collect(Collectors.toList());

        List<ImageEntity> listImage = listImageInAlbum.stream()
                .map(AlbuminfoImageEntity::getImage)
                .collect(Collectors.toList());

        System.out.printf("List music size: "+listMusic.size());

        session.setAttribute("filterMusic",listMusic);
        session.setAttribute("filterImage",listImage);

        model.addAttribute("listImage", listImage);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("albumId", albumId);
        model.addAttribute("ten", ten);
        return "album_music_client";
    }

    @GetMapping("/listimage/{albumId}")
    public String listImage(@RequestParam(name = "ten", required = false) String ten,
                            HttpSession session,
                            @PathVariable int albumId,
                            Model model){

        // Lấy album cụ thể theo albumId và tăng lượt view cho album đó
        AlbumEntity album = albumService.findAlbumById(albumId);
        if (album != null) {
            album.setViewCount(album.getViewCount() + 1);
            albumRepository.save(album);
        }

        List<AlbumInfoEntity> listMusicInAlbum = albumInfoService.findAllMusicByAlbumId(albumId);
        List<AlbuminfoImageEntity> listImageInAlbum = albumInfoImageService.findAllImageByAlbumId(albumId);

        List<MusicEntity> listMusic = listMusicInAlbum.stream()
                .map(AlbumInfoEntity::getMusic)
                .collect(Collectors.toList());

        List<ImageEntity> listImage = listImageInAlbum.stream()
                .map(AlbuminfoImageEntity::getImage)
                .collect(Collectors.toList());

        System.out.printf("List music size: "+listMusic.size());

        session.setAttribute("filterMusic",listMusic);
        session.setAttribute("filterImage",listImage);

        model.addAttribute("listImage", listImage);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("albumId", albumId);
        model.addAttribute("ten", ten);
        return "album_image_client";
    }


    @GetMapping("/contentsSearch")
    public String albumFilter(Model model,
                              @RequestParam int categoryId){

            List<AlbumEntity> listAlbumByCategory = albumService.getAlbumsByCategory(categoryId);
            CategoryAlbumEntity category = categoryAlbumService.getCategoryAlbumById(categoryId);

            model.addAttribute("listAlbum", listAlbumByCategory);
            model.addAttribute("category", category);

            return "list_album_search_client";

    }

    @GetMapping("/TopView")
    public String albumTopView(Model model){

        List<AlbumEntity> listAlbum = albumService.getAllAlbum();
        // Sắp xếp danh sách album theo số lượt xem giảm dần
        listAlbum.sort(Comparator.comparingInt(AlbumEntity::getViewCount).reversed());

        model.addAttribute("listAlbum", listAlbum);

        return "list_album_top_view_client";

    }

}
