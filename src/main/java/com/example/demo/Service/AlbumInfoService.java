package com.example.demo.Service;

import com.example.demo.Entity.AlbumEntity;
import com.example.demo.Entity.AlbumInfoEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.AlbumInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumInfoService {
    @Autowired
    private AlbumInfoRepository albumInfoRepository;
    public List<AlbumInfoEntity> findAllMusicByAlbumId(int albumId){
        return albumInfoRepository.findByAlbumId(albumId);
    }

    public boolean insertMusic(int musicId, int albumId){
        try{
            MusicEntity musicEntity = new MusicEntity();
            musicEntity.setId(musicId);

            AlbumEntity albumEntity = new AlbumEntity();
            albumEntity.setId(albumId);

            AlbumInfoEntity albumInfoEntity = new AlbumInfoEntity();
            albumInfoEntity.setMusic(musicEntity);
            albumInfoEntity.setAlbum(albumEntity);

            albumInfoRepository.save(albumInfoEntity);
            return true;
        }catch (Exception e){
            System.out.println("Loi add music into album");
            return false;
        }

    }


    public boolean deleteAlbumInfoByAlbumId(int albumId) {
        try{
            List<AlbumInfoEntity> albumInfoList = albumInfoRepository.findByAlbumId(albumId);
            albumInfoRepository.deleteAll(albumInfoList);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean deleteAlbumInfoByMusicId(int musicId) {
        try{
            List<AlbumInfoEntity> albumInfoList = albumInfoRepository.findByMusicId(musicId);
            albumInfoRepository.deleteAll(albumInfoList);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }


    public boolean deleteAlbumInfoByMusicIdAndAlbumId(int musicId, int albumId) {

            // Kiểm tra xem albumInfo có tồn tại không
            AlbumInfoEntity albumInfo = albumInfoRepository.findByMusicIdAndAlbumId(musicId, albumId);
//            System.out.println("AlbumInfo: " + albumInfo);
            if (albumInfo != null) {
                albumInfoRepository.delete(albumInfo);
                return true;
            } else {
                // Trường hợp albumInfo không tồn tại
                return false;
            }

    }


}
