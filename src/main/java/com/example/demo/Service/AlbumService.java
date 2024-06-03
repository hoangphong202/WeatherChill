package com.example.demo.Service;

import com.example.demo.Entity.*;
import com.example.demo.Repository.AlbumRepository;
import com.example.demo.Repository.LikeAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AlbumService {
    @Autowired
    private AlbumStorageService albumStorageService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private LikeAlbumRepository likeAlbumRepository;


    public List<AlbumEntity> searchAlbumsByName(String name) {
        return albumRepository.findByNameContaining(name);
    }


    public boolean InsertCover(MultipartFile file, String name, int categoryId){
        albumStorageService.save(file);

        AlbumEntity albumEntity = new AlbumEntity();
        albumEntity.setImgPath(file.getOriginalFilename());
        albumEntity.setName(name);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        albumEntity.setUser(userEntity);

        // Tạo đối tượng CategoryAlbumEntity để thiết lập quan hệ với album
        CategoryAlbumEntity categoryAlbum = new CategoryAlbumEntity();
        categoryAlbum.setId(categoryId);


        albumEntity.setCategoryAlbum(categoryAlbum);
        try{
            albumRepository.save(albumEntity);
            return true;
        }catch (Exception e){
            System.out.println("Loi Insert Album trong Album Service"+e.getLocalizedMessage());
            return false;
        }
    }

    public boolean InsertCoverClient(MultipartFile file, String name, int categoryId, int userId){
        albumStorageService.save(file);

        AlbumEntity albumEntity = new AlbumEntity();
        albumEntity.setImgPath(file.getOriginalFilename());
        albumEntity.setName(name);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        albumEntity.setUser(userEntity);

        // Tạo đối tượng CategoryAlbumEntity để thiết lập quan hệ với album
        CategoryAlbumEntity categoryAlbum = new CategoryAlbumEntity();
        categoryAlbum.setId(categoryId);


        albumEntity.setCategoryAlbum(categoryAlbum);
        try{
            albumRepository.save(albumEntity);
            return true;
        }catch (Exception e){
            System.out.println("Loi Insert Album trong Album Service"+e.getLocalizedMessage());
            return false;
        }
    }

    public List<AlbumEntity> getAllAlbum(){

        return albumRepository.findAll();
    }




    public List<AlbumEntity> getAllAlbumByUserId(int userId){

        return albumRepository.findAllAlbumByUser_Id(userId);
    }

    public AlbumEntity findAlbumById(int albumId){

        return albumRepository.findById(albumId);
    }

    //     lọc
    public List<AlbumEntity> getAlbumsByCategory(int categoryId) {
        // Gọi phương thức từ repository để lấy danh sách album theo category
        return albumRepository.findByCategoryAlbum_Id(categoryId);
    }




    public boolean deleteAlbumById(int id){
        try{
            albumRepository.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<AlbumEntity> getAlbumsByCategory(String categoryName){
        return albumRepository.findBycategoryAlbum_Name(categoryName);
    }

//    public List<AlbumEntity> findAllAlbumByCategoryId(int categoryId){
//        return albumRepository.findAllByCategoryId(categoryId);
//    }

}
