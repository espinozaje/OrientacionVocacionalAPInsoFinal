package com.vocacional.orientacionvocacional.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String user_name;
    private String message;
    private String room_id;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
