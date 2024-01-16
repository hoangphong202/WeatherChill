package com.example.demo.old;

import com.example.demo.Entity.TkKhachEntity;
import com.example.demo.Repository.TkKhachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//@Controller
public class RegisterController_old {
    @Autowired
    private TkKhachRepository tkKhachRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("tkKhachEntity", new TkKhachEntity());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute TkKhachEntity tkKhachEntity, Model model) {
        // Kiểm tra xem người dùng có tồn tại hay không
        if (tkKhachRepository.findByTen(tkKhachEntity.getTen()) != null) {
            model.addAttribute("error", "Nick Name đã tồn tại!");
            return "register";
        }
        else if (tkKhachRepository.findByTaikhoan(tkKhachEntity.getTaikhoan()) != null) {
            model.addAttribute("error", "Tài khoản đã tồn tại!");
            return "register";
        }

        // Lưu người dùng vào cơ sở dữ liệu
        tkKhachRepository.save(tkKhachEntity);

        // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
        return "redirect:/login";
    }
}
