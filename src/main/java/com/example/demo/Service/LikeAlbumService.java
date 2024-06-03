package com.example.demo.Service;

import com.example.demo.Entity.*;
import com.example.demo.Repository.AlbumRepository;
import com.example.demo.Repository.LikeAlbumRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeAlbumService {
    @Autowired
    private LikeAlbumRepository likeAlbumRepository;

    public List<LikeAlbumEntity> getAllLikeAlbumByIdUser(int userId){
        return likeAlbumRepository.findByUserId(userId);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlbumRepository albumRepository;

    public boolean insertLikeAlbum(int userId, int albumId) {

        UserEntity user = userRepository.findById(userId);
        AlbumEntity album = albumRepository.findById(albumId);

            try {

                LikeAlbumEntity likeAlbumEntity = new LikeAlbumEntity();
                likeAlbumEntity.setUser(user);
                likeAlbumEntity.setAlbum(album);




                likeAlbumRepository.save(likeAlbumEntity);// Lưu vào CSDL


                return true;
            } catch (Exception e) {
                System.out.println("Loi add album");
                return false;
            }



    }

    public boolean deleteLikeAlbum(int userId, int albumId) {
        List<LikeAlbumEntity> likeAlbums = likeAlbumRepository.findAllByUserIdAndAlbumId(userId, albumId);
        if (!likeAlbums.isEmpty()) {
            likeAlbumRepository.deleteAll(likeAlbums);
            return true;
        }
        return false;
    }



}
