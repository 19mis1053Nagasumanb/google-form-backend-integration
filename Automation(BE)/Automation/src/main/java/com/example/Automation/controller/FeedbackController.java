package com.example.Automation.controller;

import com.example.Automation.GoogleSheetService;
import com.example.Automation.entity.Feedback;
import com.example.Automation.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@Slf4j
public class FeedbackController {

    @Autowired
    private GoogleSheetService googleSheetService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/sync")
    @Scheduled(cron = "0 */1 * * * *")
    public ResponseEntity<String> syncFeedbackData() {
        try {
            List<List<Object>> rawData = googleSheetService.fetchFeedbackData();
            List<Feedback> feedbackList = feedbackService.cleanAndPrepareData(rawData);
            log.info("feedbackList begin:{}",feedbackList);
            feedbackService.saveFeedbackData(feedbackList);
            return ResponseEntity.ok("Feedback data synchronized successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error syncing data: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }
}
