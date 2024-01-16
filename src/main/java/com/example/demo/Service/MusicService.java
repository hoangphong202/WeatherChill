package com.example.demo.Service;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private MusicStorageService musicStorageService;
    public List<MusicEntity> getAllMusic(){
        return musicRepository.findAll();
    }

    public boolean InsertMusic(MultipartFile file, String name, int categoryId){
        musicStorageService.save(file);
        MusicEntity musicEntity = new MusicEntity();
        musicEntity.setPath(file.getOriginalFilename());
        musicEntity.setName(name);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        musicEntity.setCategory(categoryEntity);
        try{
            musicRepository.save(musicEntity);
            return true;
        }catch (Exception e){
            System.out.println("Loi InsertMusic trong MusicService"+e.getLocalizedMessage());
            return false;
        }
    }

    public boolean deleteMusicById(int id){
        try{
            musicRepository.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    public boolean updateMusicById(int id, MultipartFile file, String name, int categoryId){
        Optional<MusicEntity> musicExisted = musicRepository.findById(id);
        if(musicExisted.isPresent()){
            try{
                musicStorageService.save(file);
                MusicEntity musicEntity = new MusicEntity();
                musicEntity = musicExisted.get();
                musicEntity.setName(name);
                musicEntity.setPath(file.getOriginalFilename());
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setId(categoryId);
                musicEntity.setCategory(categoryEntity);
                musicRepository.save(musicEntity);
                System.out.println("Service: Update music success");
                return true;
            }catch (Exception e){
                System.out.println("Service: Update music fail");
                return false;
            }
        }
        else {
            System.out.println("Service: Update music fail");
            return false;
        }
    }
    public List<MusicEntity> FilterByCategoryId(int categoryId){
        return musicRepository.findAllByCategory_Id(categoryId);
    }
}
