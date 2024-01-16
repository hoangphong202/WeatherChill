package com.example.demo.Controller;

import com.example.demo.Entity.ImageEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Entity.TkChuEntity;
import com.example.demo.Entity.TkKhachEntity;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.MusicRepository;
import com.example.demo.Repository.TkChuRepository;
import com.example.demo.Repository.TkKhachRepository;
import com.example.demo.Service.MusicService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Collections;
import java.util.List;

@Controller

public class ChillController {

//	@GetMapping("/main")
//	public String main(Model main) {
//		return "main";
//	}



    @Autowired
    private ImageRepository imageRepository;


//    public String getImagePath() {
//        // Thực hiện truy vấn để lấy đường dẫn ảnh từ cơ sở dữ liệu
//        // Trong trường hợp này, giả sử bạn có một phương thức như getImagePath() trong repository.
//        // Bạn cần triển khai nó dựa trên framework JPA hoặc Hibernate.
//        ImageEntity image = imageRepository.findById(2).orElse(null);
//
//
//        return (image != null) ? image.getPath() : null;
//    }

    @Autowired
    private TkKhachRepository tkKhachRepository;

    @Autowired
    private MusicService musicService;

    @GetMapping("/showImage")
    public String showImage(@RequestParam(name = "ten", required = false) String ten,
                            HttpSession session,
                            Model model) {
//        String imagePath = getImagePath();
        List<ImageEntity> LisImages = imageRepository.findAll();
        List<MusicEntity> listMusic =  (List<MusicEntity>) session.getAttribute("filterMusic");
        List<MusicEntity> listMusicAll = musicService.getAllMusic();


        session.invalidate();


        // Trộn danh sách
        Collections.shuffle(LisImages);

//        System.out.println(""+listMusic.size());

        model.addAttribute("LisImages", LisImages);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("listMusicAll", listMusicAll);
        model.addAttribute("ten", ten);
        return "index";
    }

}
