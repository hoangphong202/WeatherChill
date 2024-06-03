package com.example.demo.Service;

import com.example.demo.DTO.FavoriteAlbumDTO;
import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.FavoriteAlbumEntity;
import com.example.demo.Entity.FavoriteAlbumInfoEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.FavoriteAlbumInfoRepository;
import com.example.demo.Repository.FavoriteAlbumRepository;
import com.example.demo.Repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private FavoriteAlbumRepository favoriteAlbumRepository;
    @Autowired
    private FavoriteAlbumInfoRepository favoriteAlbumInfoRepository;
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
    public Optional<MusicEntity> findMusicById(int musicId){
        return musicRepository.findById(musicId);
    }
    public List<MusicEntity> listMusicRecommend(List<FavoriteAlbumDTO> listFavoriteAlbum, int userFavoriteAlbumId, int categoryId){
        List<MusicEntity> listMusic = new ArrayList<>();
        List<Integer> listAlbumIdForRecommend = new ArrayList<>();
        FavoriteAlbumEntity userFavoriteAlbum = favoriteAlbumRepository.findById(userFavoriteAlbumId).orElseThrow(()->new RuntimeException("User favorite album not found"));
        List<FavoriteAlbumInfoEntity> listInfo = userFavoriteAlbum.getFavoriteAlbums();
        System.out.println("User favorite album: ");
        List<MusicEntity> userFavoriteMusics = new ArrayList<>();
        for (FavoriteAlbumInfoEntity music : listInfo){
            MusicEntity musicEmp = music.getMusic();
            if(musicEmp.getCategory().getId()==categoryId){
                System.out.println("MusicId: "+musicEmp.getId()+" Name"+musicEmp.getName()+" Category: "+musicEmp.getCategory().getName());
                userFavoriteMusics.add(music.getMusic());
            }
        }
        for (FavoriteAlbumDTO dto : listFavoriteAlbum) {
            listAlbumIdForRecommend.add(dto.getFavoriteAlbumId());
        }
        for (int albumId : listAlbumIdForRecommend) {
            if(listMusic.size()>=50){
                return listMusic;
            }
            List<MusicEntity> listRecommendMusic = favoriteAlbumInfoRepository.findMusicByAlbumIdAndCategoryId(albumId, categoryId);
            for (MusicEntity music: listRecommendMusic) {
                if(isMusicExistInFavoriteMusic(music.getId(),userFavoriteMusics)==false && isMusicExistInListRecommend(music.getId(), listMusic)==false){
                    listMusic.add(music);
                }
            }
        }
        return listMusic;
    }

    public boolean isMusicExistInFavoriteMusic(int musicId, List<MusicEntity> listFavoriteMusic){
        try{
            if(listFavoriteMusic.size()<=0){
                return false;
            }
            for (MusicEntity music : listFavoriteMusic) {
                if(music.getId()==musicId){
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            throw new RuntimeException("Error: "+e.getLocalizedMessage());
        }
    }

    public boolean isMusicExistInListRecommend(int musicId, List<MusicEntity> listMusicRecommend){
        try{
            if(listMusicRecommend.size()<=0){
                return false;
            }
            for (MusicEntity music : listMusicRecommend) {
                if(music.getId()==musicId){
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            throw new RuntimeException("Error: "+e.getLocalizedMessage());
        }
    }
}
