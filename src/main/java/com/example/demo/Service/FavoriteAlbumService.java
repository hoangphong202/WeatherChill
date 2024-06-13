package com.example.demo.Service;

import com.example.demo.Entity.FavoriteAlbumEntity;
import com.example.demo.Entity.FavoriteAlbumInfoEntity;
import com.example.demo.Entity.HistoryEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.FavoriteAlbumInfoRepository;
import com.example.demo.Repository.FavoriteAlbumRepository;
import com.example.demo.Repository.HistoryRepository;
import com.example.demo.Repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteAlbumService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private FavoriteAlbumInfoRepository favoriteAlbumInfoRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private FavoriteAlbumRepository favoriteAlbumRepository;
    public Integer findFavoriteAlbumByHistoryId(int historyId){
        HistoryEntity history = historyRepository.findById(historyId).orElseThrow(()->new RuntimeException("HistoryId not found"));
        return history.getUser().getAlbum().getId();
    }

    public String insertMusic(int musicId, int favoriteAlbumId){
        MusicEntity music = musicRepository.findById(musicId).orElseThrow(()->new RuntimeException("MusicId not found"));
        FavoriteAlbumEntity favoriteAlbum = favoriteAlbumRepository.findById(favoriteAlbumId).orElseThrow(()->new RuntimeException("Favorite album not found"));
        boolean isExist = favoriteAlbumInfoRepository.existsByMusicIdAndFavoriteAlbumId(musicId, favoriteAlbumId);
        if(isExist){
            throw new RuntimeException("Music exist in favorite album");
        }
        FavoriteAlbumInfoEntity emp = new FavoriteAlbumInfoEntity();
        emp.setAlbum(favoriteAlbum);
        emp.setMusic(music);
        try{
            favoriteAlbumInfoRepository.save(emp);
            return "Add music success";
        }catch (Exception e){
            throw new RuntimeException("Error: "+e.getLocalizedMessage());
        }
    }

//    public String deleteMusic(int musicId, int favoriteAlbumId){
//        try{
//            favoriteAlbumInfoRepository.deleteByMusicIdAndFavoriteAlbumId(musicId, favoriteAlbumId);
//            return "Delete success";
//        }
//        catch (Exception e){
//            throw new RuntimeException("Error: "+ e.getLocalizedMessage());
//        }
//    }
}
