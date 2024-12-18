package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.model.entity.Chat;
import com.vocacional.orientacionvocacional.model.entity.User;
import com.vocacional.orientacionvocacional.repository.ChatSocketRepository;
import com.vocacional.orientacionvocacional.repository.IChatSocketRepository;
import com.vocacional.orientacionvocacional.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatSocketRepository chatSocketRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final IChatSocketRepository iChatSocketRepository;

    public ChatService(ChatSocketRepository chatSocketRepository, EmailService emailService, UserRepository userRepository, IChatSocketRepository ichatSocketRepository) {
        this.chatSocketRepository = chatSocketRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.iChatSocketRepository = ichatSocketRepository;
    }


    public String getReceiverEmail(Integer receiverId) {
        // Buscar al usuario por su ID
        User receiver = userRepository.findById(receiverId).orElse(null);
        // Retornar el correo si el usuario existe, de lo contrario null
        return receiver != null ? receiver.getEmail() : null;
    }


    public boolean shouldSendEmail(Integer receiverId) {
        // Obtener el último mensaje enviado al receptor
        Chat lastMessage = iChatSocketRepository.findTopByUserNameOrderByTimestampDesc(String.valueOf(receiverId));

        // Si no hay mensajes previos, enviar correo
        if (lastMessage == null) {
            return true;
        }
        System.out.println(lastMessage);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMessageTime = lastMessage.getTimestamp();

        if (!lastMessageTime.toLocalDate().equals(now.toLocalDate())) {
            return true;
        }


        Duration duration = Duration.between(lastMessageTime, now);
        return duration.toHours() >= 2;
    }
}
