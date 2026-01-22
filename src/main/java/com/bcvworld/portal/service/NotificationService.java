package com.bcvworld.portal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bcvworld.portal.dto.NotificationItemDTO;
import com.bcvworld.portal.repository.JobCommentRepository;
import com.bcvworld.portal.repository.SuggestionRepository;
import com.bcvworld.portal.repository.UserRepository;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobCommentRepository commentRepository;

    @Autowired
    private SuggestionRepository suggestionRepository; // Or SuggestionRepository

    
    public List<NotificationItemDTO> getRecentNotifications() {
        List<NotificationItemDTO> all = new ArrayList<>();
        PageRequest limit = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 1. Fetch recent Users
        userRepository.findAll(limit).forEach(u -> all.add(new NotificationItemDTO(
            String.valueOf(u.getId()), 
            "USER", 
            "NEW USER", 
            u.getEmail() + " joined", 
            "/admin/users", 
            u.getCreatedAt()
        )));

        // 2. Fetch recent Comments
        commentRepository.findAll(limit).forEach(c -> all.add(new NotificationItemDTO(
            String.valueOf(c.getId()), 
            "COMMENT", 
            "NEW COMMENT", 
            "Comment by " + c.getUserName(),
            "/admin/comments", 
            c.getCreatedAt()
        )));

     // 3. Fetch recent Suggestions/Messages
        suggestionRepository.findAllByOrderByDateDesc(PageRequest.of(0, 10))
            .forEach(m -> all.add(new NotificationItemDTO(
                String.valueOf(m.getId()),
                "SUGGESTION",
                "NEW MESSAGE",
                m.getEmail(),
                "/admin/messages",
                m.getDate()
            )));


        // 4. Combine, Sort by Date DESC, and Limit to top 20
        return all.stream()
                .sorted(Comparator.comparing(NotificationItemDTO::getCreatedAt).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }
}