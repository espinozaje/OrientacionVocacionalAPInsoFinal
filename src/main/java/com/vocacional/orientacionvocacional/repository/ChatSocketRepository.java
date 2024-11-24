package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatSocketRepository implements IChatSocketRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Chat chatMessageModel) {
        String SQL = "INSERT INTO chats(user_name, message, room_id) VALUES(?,?,?)";
        return jdbcTemplate.update(SQL, new Object[]{
            chatMessageModel.getUser_name(),
            chatMessageModel.getMessage(),
            chatMessageModel.getRoom_id()
        });
    }

    @Override
    public List<Chat> findByRoomId(String roomId) {
        String SQL = "SELECT * FROM chats WHERE room_id = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(
                Chat.class
        ), roomId);
    }
}
