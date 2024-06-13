package com.example.demo.Controller;

import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    private RedirectAttributes redirectAttributes;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String LoginChu(RedirectAttributes redirectAttributes,
                           @ModelAttribute UserEntity userEntity,
                           HttpSession session,
                           Model model) {

        if (userEntity.getEmail().isEmpty()  ) {
            model.addAttribute("error", "email không được để trống!");
            return "login";
        }

        if(userEntity.getPassword().isEmpty()){
            model.addAttribute("error", "password không được để trống!");
            return "login";
        }

        UserEntity usertk = userRepository.findByEmail(userEntity.getEmail());
        if(usertk == null){
            model.addAttribute("error", "tai khoản không tồn tại!");
            return "login";
        }

        UserEntity user = userRepository.findByEmailAndPassword(userEntity.getEmail(),userEntity.getPassword());

        if (user != null) {
            // Lưu thông tin người dùng vào session
            session.setAttribute("loggedInUser", user);

            if (user.getRole().getId() == 1) {
                return "redirect:/Home-admin";
            }
            String ten = user.getName();
            redirectAttributes.addFlashAttribute("ten", user.getName());
            return "redirect:/home-page";
        } else {
            // User not found, display error message
            model.addAttribute("error", "Thông tin đăng nhập không hợp lệ!");
            return "login";
        }

    }


}
