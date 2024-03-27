package com.example.demo.Service;

import com.example.demo.Entity.*;
import com.example.demo.Repository.AlbumInfoImageRepository;
import com.example.demo.Repository.AlbumInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumInfoImageService {
    @Autowired
    private AlbumInfoImageRepository albumInfoImageRepository;
    public List<AlbuminfoImageEntity> findAllImageByAlbumId(int albumId){
        return albumInfoImageRepository.findByAlbumId(albumId);
    }

    public boolean insertImage(int imageId, int albumId) {
        if (!albumInfoImageRepository.existsByImageIdAndAlbumId(imageId, albumId)) {
            try {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setId(imageId);

                AlbumEntity albumEntity = new AlbumEntity();
                albumEntity.setId(albumId);

                AlbuminfoImageEntity albumInfoImageEntity = new AlbuminfoImageEntity();
                albumInfoImageEntity.setImage(imageEntity);
                albumInfoImageEntity.setAlbum(albumEntity);




                albumInfoImageRepository.save(albumInfoImageEntity);// Lưu vào CSDL


                return true;
            } catch (Exception e) {
                System.out.println("Loi add music into album");
                return false;
            }

        }else {
            // Trả về false nếu image_id đã tồn tại
            System.out.println("Da co hinh trong album roi");
            return false;
        }

    }

    public boolean deleteAlbumInfoImageByAlbumId(int albumId) {
        try{
            List<AlbuminfoImageEntity> albumInfoList = albumInfoImageRepository.findByAlbumId(albumId);
            albumInfoImageRepository.deleteAll(albumInfoList);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }


    public boolean deleteAlbumInfoByImageIdAndAlbumId(int imageId, int albumId) {

        // Kiểm tra xem albumInfo có tồn tại không
        AlbuminfoImageEntity albumInfo = albumInfoImageRepository.findByImageIdAndAlbumId(imageId, albumId);

        if (albumInfo != null) {
            albumInfoImageRepository.delete(albumInfo);
            return true;
        } else {
            // Trường hợp albumInfo không tồn tại
            return false;
        }

    }

}
