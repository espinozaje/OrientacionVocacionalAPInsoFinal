package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Chat;

import java.util.List;

public interface IChatSocketRepository {
    public int save(Chat chatMessageModel);
    public List<Chat> findByRoomId(String roomId);
    Chat findTopByUserNameOrderByTimestampDesc(String userId);
}
