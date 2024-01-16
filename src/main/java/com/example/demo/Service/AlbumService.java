package com.example.demo.Service;

import com.example.demo.Entity.AlbumEntity;
import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.AlbumRepository;
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

    public boolean InsertCover(MultipartFile file, String name, int categoryId){
        albumStorageService.save(file);
        AlbumEntity albumEntity = new AlbumEntity();
        albumEntity.setImgPath(file.getOriginalFilename());
        albumEntity.setName(name);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        albumEntity.setCategory(categoryEntity);
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

    public List<MusicEntity> getAllMusic(int albumId){
        return null;
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

    public List<AlbumEntity> findAllAlbumByCategoryId(int categoryId){
        return albumRepository.findAllByCategoryId(categoryId);
    }

}
