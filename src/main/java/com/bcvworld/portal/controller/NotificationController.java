package com.bcvworld.portal.controller;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.dto.NotificationItemDTO;
import com.bcvworld.portal.service.NotificationService;

@RestController
@RequestMapping("/api/admin/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

   
    @GetMapping("/recent")
    public ResponseEntity<List<NotificationItemDTO>> getRecentNotifications() {
        // Fetches top 20 recent items
        return ResponseEntity.ok(notificationService.getRecentNotifications());
    }
}