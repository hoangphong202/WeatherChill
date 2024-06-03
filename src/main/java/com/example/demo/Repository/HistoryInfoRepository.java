package com.example.demo.Repository;

import com.example.demo.Entity.HistoryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryInfoRepository extends JpaRepository<HistoryInfoEntity, Integer> {
    @Query(value = "SELECT hi.* FROM history_info hi JOIN history h ON hi.history_id = h.id WHERE hi.history_id = :historyId GROUP BY hi.id ORDER BY hi.id DESC LIMIT 10", nativeQuery = true)
    List<HistoryInfoEntity> findTop10ByHistoryIdOrderByIdDesc(int historyId);
}
