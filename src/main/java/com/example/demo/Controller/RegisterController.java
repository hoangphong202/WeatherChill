package com.example.demo.Controller;

import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }



//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute UserEntity userEntity,
//                               Model model) {
//
//        // Kiểm tra rỗng cho name, email, và password
//        if (userEntity.getName().isEmpty() || userEntity.getEmail().isEmpty() ||userEntity.getPassword().isEmpty()) {
//            model.addAttribute("error", "Vui lòng điền đầy đủ thông tin!");
//            return "register";
//        }
//
//
//        // Kiểm tra xem người dùng có tồn tại hay không
//        if (userRepository.findByName(userEntity.getName()) != null) {
//            model.addAttribute("error", "Nick Name đã tồn tại!");
//            return "register";
//        }
//        else if (userRepository.findByEmail(userEntity.getEmail()) != null) {
//            model.addAttribute("error", "Tài khoản đã tồn tại!");
//            return "register";
//        }
//
//        // Set the role for the user
//        RoleEntity role = new RoleEntity();
//        role.setId(2);  // Assuming 2 is the ID for "Client" role
//        userEntity.setRole(role);
//
//
//        // Lưu người dùng vào cơ sở dữ liệu
//        userRepository.save(userEntity);
//        // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
//        return "redirect:/login";
//    }


//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute UserEntity userEntity,
//                               Model model) {
//
//        // Kiểm tra rỗng cho name, email, và password
//        if (userEntity.getName().isEmpty() || userEntity.getEmail().isEmpty() ||userEntity.getPassword().isEmpty()) {
//            model.addAttribute("error", "Dang ký thất bại!");
//            return "register";
//        }
//
//
//        // Kiểm tra xem người dùng có tồn tại hay không
//        if (userRepository.findByName(userEntity.getName()) != null) {
//            model.addAttribute("error", "Dang ký thất bại!");
//            return "register";
//        }
//        else if (userRepository.findByEmail(userEntity.getEmail()) != null) {
//            model.addAttribute("error", "Dang ký thất bại!");
//            return "register";
//        }
//
//        // Set the role for the user
//        RoleEntity role = new RoleEntity();
//        role.setId(2);  // Assuming 2 is the ID for "Client" role
//        userEntity.setRole(role);
//
//
//        // Lưu người dùng vào cơ sở dữ liệu
//        userRepository.save(userEntity);
//        // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
//        return "redirect:/login";
//    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserEntity userEntity,
                               Model model) {

        // Kiểm tra rỗng cho name, email, và password
        if (userEntity.getName().isEmpty()  ) {
            model.addAttribute("error", "Tên không được để trống!");
            return "register";
        } else if( userEntity.getEmail().isEmpty()){
            model.addAttribute("error", "Email không được để trống!");
            return "register";
        } else if( userEntity.getPassword().isEmpty()){
            model.addAttribute("error", "Mật khẫu không được để trống!");
            return "register";
        }


        // Kiểm tra xem người dùng có tồn tại hay không
        if (userRepository.findByName(userEntity.getName()) != null) {
            model.addAttribute("error", "tên đã tồn tại!");
            return "register";
        }
        else if (userRepository.findByEmail(userEntity.getEmail()) != null) {
            model.addAttribute("error", "Tài khoản đã tồn tại!");
            return "register";
        }

        // Set the role for the user
        RoleEntity role = new RoleEntity();
        role.setId(2);  // Assuming 2 is the ID for "Client" role
        userEntity.setRole(role);


        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(userEntity);
        // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
        return "redirect:/login";
    }

}
