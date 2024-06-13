package com.example.demo.Service;

import com.example.demo.DTO.FavoriteAlbumDTO;
import com.example.demo.Entity.FavoriteAlbumEntity;
import com.example.demo.Entity.FavoriteAlbumInfoEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.FavoriteAlbumInfoRepository;
import com.example.demo.Repository.FavoriteAlbumRepository;
import com.example.demo.Repository.HistoryRepository;
import com.example.demo.Repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteAlbumInfoService {
    @Autowired
    private FavoriteAlbumInfoRepository favoriteAlbumInfoRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private FavoriteAlbumRepository favoriteAlbumRepository;

    public List<FavoriteAlbumDTO> albumsHavingMostCategorySongs(int categoryId) {
        List<Object[]> results = favoriteAlbumInfoRepository.findAlbumsHavingMostCategorySongs(categoryId);
        List<FavoriteAlbumDTO> dtos = new ArrayList<>();
        System.out.println("");
        System.out.println("List Favorite Album: ");
        for (Object[] result : results) {
            FavoriteAlbumDTO dto = new FavoriteAlbumDTO();
            dto.setFavoriteAlbumId(((Number) result[0]).intValue());
            dto.setCategoryCount(((Number) result[1]).intValue());
            dto.setCategoryName((String) result[2]);
            System.out.println("Favorite Album ID: " + dto.getFavoriteAlbumId() + ", Pop Count: " + dto.getCategoryCount() + ", Category Name: " + dto.getCategoryName());
            dtos.add(dto);
        }
        return dtos;
    }
    public List<FavoriteAlbumInfoEntity> getFavoriteAlbumInfoByFavoriteAlbumId(int favoriteAlbumId) {
        return favoriteAlbumInfoRepository.findByAlbumId(favoriteAlbumId);
    }

    public List<MusicEntity> getMusicByFavoriteAlbumId(int favoriteAlbumId) {
        List<FavoriteAlbumInfoEntity> favoriteAlbumInfoEntities = getFavoriteAlbumInfoByFavoriteAlbumId(favoriteAlbumId);
        return favoriteAlbumInfoEntities.stream()
                .map(FavoriteAlbumInfoEntity::getMusic)
                .collect(Collectors.toList());
    }

    public String insertMusic(int musicId, int favoriteAlbumId){
        if(favoriteAlbumInfoRepository.findByAlbumIdAndMusicId(favoriteAlbumId, musicId).isPresent()){
            return "Music Exist";
        }
        FavoriteAlbumInfoEntity favoriteAlbumInfo = new FavoriteAlbumInfoEntity();
        MusicEntity music = musicRepository.findById(musicId).orElseThrow(()->new RuntimeException("Music not found"));
        FavoriteAlbumEntity favoriteAlbum = favoriteAlbumRepository.findById(favoriteAlbumId).orElseThrow(()->new RuntimeException("Album not found"));
        favoriteAlbumInfo.setMusic(music);
        favoriteAlbumInfo.setAlbum(favoriteAlbum);
        favoriteAlbumInfoRepository.save(favoriteAlbumInfo);
        return "Insert music success";
    }

    public String deleteMusic(int musicId, int favoriteAlbumId){
        FavoriteAlbumInfoEntity favoriteAlbumInfoEntity = favoriteAlbumInfoRepository.findByAlbumIdAndMusicId(favoriteAlbumId, musicId).orElseThrow(()->new RuntimeException("Info not found"));
        favoriteAlbumInfoRepository.delete(favoriteAlbumInfoEntity);
        return "Delete success";
    }
}
