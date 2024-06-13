package com.example.demo.Service;

import com.example.demo.Entity.HistoryEntity;
import com.example.demo.Entity.HistoryInfoEntity;
import com.example.demo.Entity.MusicEntity;
import com.example.demo.Repository.HistoryInfoRepository;
import com.example.demo.Repository.HistoryRepository;
import com.example.demo.Repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    @Autowired
    private HistoryInfoRepository historyInfoRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private HistoryRepository historyRepository;


    public Boolean insertHistory(int musicId, int historyId){

        HistoryInfoEntity historyInfoEntity = new HistoryInfoEntity();
        MusicEntity musicEntity = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Khong tim thay nhac"));
        HistoryEntity historyEntity = historyRepository.findById(historyId).orElseThrow(() -> new RuntimeException("Khong tim thay lich su"));
        historyInfoEntity.setMusic(musicEntity);
        historyInfoEntity.setHistory(historyEntity);
        try{
            historyInfoRepository.save(historyInfoEntity);
            return true;
        }catch (Exception e){
            System.out.println("HistoryService insertHistory Error: "+e.getLocalizedMessage());
            return false;
        }
    }
}
