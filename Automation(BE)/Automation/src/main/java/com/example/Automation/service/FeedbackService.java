package com.example.Automation.service;

import com.example.Automation.entity.Feedback;
import com.example.Automation.repository.FeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);
    @Autowired
    private FeedbackRepository feedbackRepository;

    // This method cleans and prepares the raw data fetched from Google Sheets
    public List<Feedback> cleanAndPrepareData(List<List<Object>> rawData) {
        List<Feedback> feedbackList = new ArrayList<>();
        for (List<Object> row : rawData) {
            Feedback feedback = new Feedback();
            // Assuming the feedback columns in Google Sheets follow a certain structure (customize as needed)
            feedback.setName((String) row.get(0)); // Assuming first column is name
            feedback.setEmail((String) row.get(1)); // Assuming second column is email
            feedback.setComments((String) row.get(2)); // Assuming third column is comments
            feedback.setRating((String) row.get(3)); // Assuming fourth column is rating

            feedback.setTimestamp((String) row.get(4)); // Assuming fifth column is timestamp

            feedbackList.add(feedback);
        }
        return feedbackList;
    }

    public void saveFeedbackData(List<Feedback> feedbackList) {
        try {
            List<Feedback> filteredFeedbackList = feedbackList.stream()
                    .filter(feedback -> {
                        // Check if the feedback with the same timestamp already exists
                        Optional<Feedback> existingFeedback = feedbackRepository.findByTimestamp(feedback.getTimestamp());
                        return existingFeedback.isEmpty(); // Only allow feedback that doesn't already exist
                    })
                    .collect(Collectors.toList());

            // Log and save only the new feedback entries
            if (!filteredFeedbackList.isEmpty()) {
                feedbackRepository.saveAll(filteredFeedbackList);
                log.info("Saved {} new feedback records", filteredFeedbackList.size());
            } else {
                log.info("No new feedback records to save");
            }

        } catch (Exception e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
    }



    // Retrieve all feedback from the database
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}
