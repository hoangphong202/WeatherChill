package com.example.demo.Controller;

import com.example.demo.Service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PostMapping("/insert")
    public String insertHistory(@RequestParam String musicId, @RequestParam String historyId ){
        if(historyService.insertHistory(Integer.parseInt(musicId), Integer.parseInt(historyId))){
            System.out.println("Insert history success");
            return "redirect:/music/play/"+musicId;
        }
        else {
            System.out.println("insert history fail");
            return "reditect:/music";
        }
    }


}
