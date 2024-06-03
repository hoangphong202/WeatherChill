package com.example.demo.Controller;

import com.example.demo.Entity.AlbumEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.AlbumService;
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

    @GetMapping("")
    public String search(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes) {
        List<AlbumEntity> searchResults = albumService.searchAlbumsByName(keyword);
        redirectAttributes.addFlashAttribute("listAlbum", searchResults);
        redirectAttributes.addFlashAttribute("keyword", keyword);
        return "redirect:/search/search-result";
    }

    @GetMapping("/search-result")
    public String showSearchResult(Model model, HttpSession session) {

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        return loggedInUser != null ? "search_result" : "search_result_unjoin";


    }
}
