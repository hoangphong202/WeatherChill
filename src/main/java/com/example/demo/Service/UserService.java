package com.example.demo.Service;

import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    private static final String UPLOAD_DIR = "src/main/resources/static/Avatar/";

    public void saveAvatar(MultipartFile file) {
        Path rootPath = Paths.get(UPLOAD_DIR);
        try {
            if (!Files.exists(rootPath)){
                Files.createDirectories(rootPath);
            }
            Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Loi upload hinh album vao static: " +e.getLocalizedMessage());
        }
    }



    public UserEntity GetUserByIdUser (int userId){
        return userRepository.findById(userId);
    }

    public boolean EditUser(UserEntity userEntity, int userId){
        UserEntity existingUser = GetUserByIdUser(userId);

        if (existingUser != null) {


            existingUser.setName(userEntity.getName());

            existingUser.setEmail(userEntity.getEmail());

            existingUser.setDescribe(userEntity.getDescribe());

            userRepository.save(existingUser);

            return true;
        } else {
            System.out.println("user rỗng");
            return false;
        }

    }

    public boolean EditUserFile(UserEntity userEntity, int userId, MultipartFile file){
        UserEntity existingUser = GetUserByIdUser(userId);

        if (existingUser != null) {

            saveAvatar(file);

            existingUser.setImgpath(file.getOriginalFilename());

            userRepository.save(existingUser);

            return true;
        } else {
            System.out.println("user rỗng");
            return false;
        }

    }
}
