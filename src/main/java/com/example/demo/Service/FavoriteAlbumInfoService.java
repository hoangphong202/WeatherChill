package com.example.demo.Service;

import com.example.demo.DTO.FavoriteAlbumDTO;
import com.example.demo.Repository.FavoriteAlbumInfoRepository;
import com.example.demo.Repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteAlbumInfoService {
    @Autowired
    private FavoriteAlbumInfoRepository favoriteAlbumInfoRepository;
    @Autowired
    private HistoryRepository historyRepository;
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

}
