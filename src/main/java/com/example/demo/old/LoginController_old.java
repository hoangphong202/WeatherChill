package com.example.demo.old;

import com.example.demo.Entity.TkChuEntity;
import com.example.demo.Entity.TkKhachEntity;
import com.example.demo.Repository.TkChuRepository;
import com.example.demo.Repository.TkKhachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


//@Controller
public class LoginController_old {

    @Autowired
    private TkChuRepository tkChuRepository;
    @Autowired
    private TkKhachRepository tkKhachRepository;
    private RedirectAttributes redirectAttributes;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("tkChuEntity", new TkChuEntity());
        model.addAttribute("tkKhachEntity", new TkKhachEntity());


        return "login";
    }

    @PostMapping("/login")
    public String LoginChu(RedirectAttributes redirectAttributes,@ModelAttribute TkChuEntity tkChuEntity,@ModelAttribute TkKhachEntity tkKhachEntity ,Model model) {


        // Perform login logic here
        TkChuEntity tkchu = tkChuRepository.findByTaikhoanAndMatkhau(tkChuEntity.getTaikhoan(), tkChuEntity.getMatkhau());
        TkKhachEntity tkkhach = tkKhachRepository.findByTaikhoanAndMatkhau(tkKhachEntity.getTaikhoan(), tkKhachEntity.getMatkhau());


        // Check if the user exists
        if (tkchu != null) {

            redirectAttributes.addFlashAttribute("ten", tkchu.getTen());

            // Redirect to the appropriate page based on the result
            return "redirect:/ListImage";
        }else if (tkkhach != null) {

            redirectAttributes.addFlashAttribute("ten", tkkhach.getTen());

            // Redirect to the appropriate page based on the result
            return "redirect:/showImage";
//            return "redirect:/chat";
        } else {
            // User not found, display error message
            model.addAttribute("error", "Tài khoản hoặc mật khẩu sai !");
            return "login";
        }
    }



//    @PostMapping("/login")
//    public String loginProcess(TkChuEntity tkChuEntity) {
//        // Perform login logic here
//        // You may need to check the username and password against the database
//        // Redirect to the appropriate page based on the result
//
//        return "redirect:/home"; // Replace "/home" with the actual landing page
//    }
}
