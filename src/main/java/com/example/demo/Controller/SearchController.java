package com.example.demo.Controller;

import com.example.demo.Entity.AlbumEntity;
import com.example.demo.Entity.LikeAlbumEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.AlbumService;
import com.example.demo.Service.LikeAlbumService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private LikeAlbumService likeAlbumService;

    @GetMapping("")
    public String search(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes, HttpSession session) {
        List<AlbumEntity> searchResults = albumService.searchAlbumsByName(keyword);

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        int userId = (loggedInUser != null) ? loggedInUser.getId() : 1;

        // Get all liked albums by the user
        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);

        // Mark search results as liked if they are in the user's liked list
        for (AlbumEntity album : searchResults) {
            for (LikeAlbumEntity likeAlbum : listAlbumlike) {
                if (likeAlbum.getAlbum().getId() == album.getId()) {
                    album.setLiked(true);
                    break;
                }
            }
        }

        redirectAttributes.addFlashAttribute("listAlbum", searchResults);
        redirectAttributes.addFlashAttribute("keyword", keyword);
        return "redirect:/search/search-result";
    }

    @GetMapping("/search-result")
    public String showSearchResult(Model model, HttpSession session) {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

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

        model.addAttribute("imgpath", imgpath);
        model.addAttribute("userId", userId);
        model.addAttribute("ten", ten);

        return loggedInUser != null ? "search_result" : "search_result_unjoin";
    }

}
