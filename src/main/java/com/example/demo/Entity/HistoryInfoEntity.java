package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity(name = "history_info")
@Data
public class HistoryInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "music_id")
    private MusicEntity music;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "history_id")
    private HistoryEntity history;

}
