package com.example.demo.Controller;

import ch.qos.logback.core.net.SyslogOutputStream;
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

import java.security.PrivateKey;
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

    @Autowired
    private LikeAlbumService likeAlbumService;
    @GetMapping("")
    public String listAlbum( HttpSession session,
                             Model model){

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");


        List<AlbumEntity> listAlbum = albumService.getAllAlbum();


        // Sử dụng toán tử ba ngôi để kiểm tra và lấy tên, trả về chuỗi rỗng nếu loggedInUser là null
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        int userId = (loggedInUser != null) ? loggedInUser.getId() : 1;
        String imgpath = (loggedInUser != null) ? loggedInUser.getImgpath() : "";


        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);

        int likedAlbumsCount = 0; // Variable to count liked albums


        for (LikeAlbumEntity likeAlbum : listAlbumlike) {
            AlbumEntity album = likeAlbum.getAlbum();
            album.setLiked(true); // Đánh dấu album đã được like bởi người dùng hiện tại
            likedAlbumsCount++; // Increment the count of liked albums

        }

        // Lưu thông tin vào session
        session.setAttribute("likedAlbumsCount", likedAlbumsCount);

        Collections.reverse(listAlbum); // đảo list album
        model.addAttribute("imgpath", imgpath);
        model.addAttribute("listAlbum", listAlbum);
        model.addAttribute("userId", userId);
        model.addAttribute("ten", ten);

        return loggedInUser != null ? "list_album_user" : "list_album_client";

    }

    @GetMapping("/insertLikeAlbum/{userId}/{albumId}")
    public String insertImage(@PathVariable(name = "userId") int userId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){

        if(likeAlbumService.insertLikeAlbum(userId,albumId)){

            return "redirect:/ListAlbum";
        }
        return "redirect:/ListAlbum";

    }

    // xóa nhạc trong album
    @GetMapping("/deleteLikeAlbum/{userId}/{albumId}")
    public String deleteMusic(@PathVariable(name = "userId") int userId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){
        likeAlbumService.deleteLikeAlbum(userId, albumId);
        return "redirect:/ListAlbum";
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
                              @RequestParam int categoryId
                              , HttpSession session){

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        int userId = (loggedInUser != null) ? loggedInUser.getId() : 1;
        String imgpath = (loggedInUser != null) ? loggedInUser.getImgpath() : "";

            List<AlbumEntity> listAlbum = albumService.getAlbumsByCategory(categoryId);
            CategoryAlbumEntity category = categoryAlbumService.getCategoryAlbumById(categoryId);

        // Get the list of liked albums by the user
        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);

        // Đánh dấu album là thích nếu chúng nằm trong danh sách thích của người dùng
        for (AlbumEntity album : listAlbum) {
            for (LikeAlbumEntity likeAlbum : listAlbumlike) {
                if (likeAlbum.getAlbum().getId() == album.getId()) {
                    album.setLiked(true);
                    break;
                }
            }
        }


            model.addAttribute("listAlbum", listAlbum);
            model.addAttribute("category", category);

        model.addAttribute("imgpath", imgpath);
        model.addAttribute("userId", userId);
        model.addAttribute("ten", ten);

        return loggedInUser != null ? "list_album_search" : "list_album_search_unjoin" ;


    }

    @GetMapping("/TopView")
    public String albumTopView(Model model, HttpSession session) {

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        int userId = (loggedInUser != null) ? loggedInUser.getId() : 1;
        String imgpath = (loggedInUser != null) ? loggedInUser.getImgpath() : "";

        List<AlbumEntity> listAlbum = albumService.getAllAlbum();
        // Sắp xếp danh sách album theo số lượt xem giảm dần
        listAlbum.sort(Comparator.comparingInt(AlbumEntity::getViewCount).reversed());

        // Get the list of liked albums by the user
        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);

        // Đánh dấu album là thích nếu chúng nằm trong danh sách thích của người dùng
        for (AlbumEntity album : listAlbum) {
            for (LikeAlbumEntity likeAlbum : listAlbumlike) {
                if (likeAlbum.getAlbum().getId() == album.getId()) {
                    album.setLiked(true);
                    break;
                }
            }
        }

        model.addAttribute("listAlbum", listAlbum);
        model.addAttribute("imgpath", imgpath);
        model.addAttribute("userId", userId);
        model.addAttribute("ten", ten);

        return loggedInUser != null ? "list_album_top_view" : "list_album_top_view_unjoin";
    }


}
