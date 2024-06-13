package com.example.demo.Service;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.HistoryInfoEntity;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.HistoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryInfoService {
    @Autowired
    private HistoryInfoRepository historyInfoRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    public List<HistoryInfoEntity> getLatestHistoryInfoByHistoryId(int historyId) {
        return historyInfoRepository.findTop10ByHistoryIdOrderByIdDesc(historyId);
    }

    public Map<String, Long> countMusicByCategory(List<HistoryInfoEntity> latestHistory) {
        // Đếm số lượng nhạc theo thể loại
        return latestHistory.stream()
                .collect(Collectors.groupingBy(
                        hi -> hi.getMusic().getCategory().getName(),
                        Collectors.counting()
                ));
    }

    public Integer getTopCategoryByMusicCount(int historyId) {
        List<HistoryInfoEntity> latestHistory = getLatestHistoryInfoByHistoryId(historyId);
        System.out.println("Latest History Info:");
        for (HistoryInfoEntity historyInfo : latestHistory) {
            System.out.println("HistoryId: "+historyInfo.getHistory().getId()+" MusicId: "+historyInfo.getMusic().getId()+" Category: "+historyInfo.getMusic().getCategory().getName());
        }

        // Đếm số lượng nhạc theo thể loại
        Map<String, Long> musicCountByCategory = countMusicByCategory(latestHistory);

        System.out.println("Music Count By Category:");
        musicCountByCategory.forEach((category, count) ->
                System.out.println(category + ": " + count)
        );

        // Tìm thể loại có số lượng nhạc nhiều nhất
        Optional<Map.Entry<String, Long>> topCategoryOptional = musicCountByCategory.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        CategoryEntity category = categoryRepository.findByName(topCategoryOptional.map(Map.Entry::getKey).orElse("No top category found"));
        System.out.println("Top category: "+category.getName());
        // Trả về tên của thể loại có số lượng nhạc nhiều nhất
        return category.getId();
    }


}
