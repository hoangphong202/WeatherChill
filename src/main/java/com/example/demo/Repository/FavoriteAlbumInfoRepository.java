package com.example.demo.Repository;

import com.example.demo.Entity.FavoriteAlbumInfoEntity;
import com.example.demo.Entity.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteAlbumInfoRepository extends JpaRepository<FavoriteAlbumInfoEntity, Integer> {
    //Lay danh sach tat ca album co the loai duoc truyen vao la nhieu nhat trong album do
    @Query(value = "SELECT fai.favorite_album_id, COUNT(*) AS pop_count, c.name AS category_name " +
            "FROM favorite_album_info fai " +
            "JOIN music m ON fai.music_id = m.id " +
            "JOIN category c ON m.category_id = c.id " +
            "WHERE c.id = :categoryId " +
            "GROUP BY fai.favorite_album_id, c.name " +
            "HAVING COUNT(*) >= ALL ( " +
            "    SELECT COUNT(*) " +
            "    FROM favorite_album_info fai_inner " +
            "    JOIN music m_inner ON fai_inner.music_id = m_inner.id " +
            "    JOIN category c_inner ON m_inner.category_id = c_inner.id " +
            "    WHERE c_inner.id != :categoryId " +
            "    AND fai_inner.favorite_album_id = fai.favorite_album_id " +
            "    GROUP BY fai_inner.favorite_album_id " +
            ")", nativeQuery = true)
    List<Object[]> findAlbumsHavingMostCategorySongs(@Param("categoryId") int categoryId);

//    @Query(value = "SELECT m.* FROM favorite_album_info fai " +
//            "JOIN music m ON fai.music_id = m.id " +
//            "WHERE fai.favorite_album_id = :albumFavoriteId " +
//            "AND m.category_id = :categoryId", nativeQuery = true)
//    List<MusicEntity> findMusicByAlbumIdAndCategoryId(@Param("albumFavoriteId") int albumFavoriteId, @Param("categoryId") int categoryId);

    @Query(value = "SELECT m FROM favorite_album_info fai JOIN fai.music m " +
            "WHERE fai.album.id = :albumFavoriteId AND m.category.id = :categoryId")
    List<MusicEntity> findMusicByAlbumIdAndCategoryId(@Param("albumFavoriteId") int albumFavoriteId, @Param("categoryId") int categoryId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM favorite_album_info WHERE music_id = :musicId AND favorite_album_id = :favoriteAlbumId)", nativeQuery = true)
    boolean existsByMusicIdAndFavoriteAlbumId(@Param("musicId") int musicId, @Param("favoriteAlbumId") int favoriteAlbumId);

    void deleteByMusic_IdAndAlbum_Id(int musicId, int favoriteAlbumId);
}
