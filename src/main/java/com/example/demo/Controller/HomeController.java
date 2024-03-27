package com.example.demo.Controller;

import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Home")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private  UserRepository userRepository;

    @GetMapping("")
    public String home(Model model,
                       HttpSession session){

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (session.getAttribute("loggedInUser") != null) {
            UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
            model.addAttribute("loggedInUser", loggedInUser);
        }

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";

        model.addAttribute("ten", ten);
        return "home";
    }

    @GetMapping("/Logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate(); // Xóa tất cả các thuộc tính của session

        // Sau khi đăng xuất, chuyển hướng người dùng đến trang đăng nhập
        return "redirect:/login";
    }

    @GetMapping("/Account")
    public String Account(Model model,
                          HttpSession session) {


        if (session.getAttribute("loggedInUser") != null) {
            UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

            model.addAttribute("loggedInUser", loggedInUser);

            return "account";
        } else {
            // Người dùng chưa đăng nhập, chuyển hướng về trang đăng nhập
            return "redirect:/login";
        }
    }

    @PostMapping("/updateAccount")
    public String updateAccount(@ModelAttribute("loggedInUser") UserEntity updatedUser,
                                HttpSession session) {

        if (session.getAttribute("loggedInUser") != null) {

            UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

            // Cập nhật thông tin người dùng từ dữ liệu được gửi từ form
            loggedInUser.setName(updatedUser.getName());
            loggedInUser.setEmail(updatedUser.getEmail());

            // Kiểm tra và cập nhật mật khẩu mới nếu nó được gửi từ form
            if (!updatedUser.getPassword().isEmpty()) {

                String newPassword = updatedUser.getPassword();

                loggedInUser.setPassword(newPassword);
            }

            // Lưu thông tin người dùng đã cập nhật vào session
            session.setAttribute("loggedInUser", loggedInUser);

            // Thực hiện lưu thông tin người dùng vào cơ sở dữ liệu (nếu cần)

            userRepository.save(loggedInUser);

            // Redirect về trang Account sau khi cập nhật
            return "redirect:/Home";
        } else {
            // Người dùng chưa đăng nhập, chuyển hướng về trang đăng nhập
            return "redirect:/login";
        }
    }






}
