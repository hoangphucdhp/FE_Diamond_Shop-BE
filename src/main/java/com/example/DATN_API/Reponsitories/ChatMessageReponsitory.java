package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ChatMessageReponsitory extends JpaRepository<ChatMessage, Integer> {
    @Query("select chat from ChatMessage chat where chat.sender=?1")
    List<ChatMessage> getAllBySender(Account sender);

    @Query("select chat from ChatMessage chat where chat.receiver=?1")
    List<ChatMessage> getAllByReceiver(Account sender);

    @Query("select chat from ChatMessage chat where chat.sender.us=?1 and chat.receiver.us=?2")
    List<ChatMessage> getAll(String sender, String re);

    @Query(value = "INSERT INTO message (sender, receiver, image, message, time) VALUES (:sender, :receiver, CONVERT(varbinary(max), :image), :message, :time)", nativeQuery = true)
    void saveWithImage(@Param("sender") int sender, @Param("receiver") int receiver, @Param("image") String image, @Param("message") String message, @Param("time") Date time);

}