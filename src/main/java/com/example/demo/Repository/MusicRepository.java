package com.example.demo.Repository;

import com.example.demo.Entity.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, Integer> {
    List<MusicEntity> findAllByCategory_Id(int categoryId);

}
