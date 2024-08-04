package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message postMessage(Message message) {
        int message_length = message.getMessageText().length();
        int user_id = message.getPostedBy();
        if ((message_length == 0)||(message_length > 255)||(messageRepository.findMessageByPostedBy(user_id) == null)) {
            return null;
        }
        else {
            Message new_message = messageRepository.save(message);
            return new_message;
        }
    }

    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    public Message findMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public int deleteMessageById(int messageId) {
        if (messageRepository.findById(messageId).isPresent()) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        else {
            return 0;
        }
    }

    public int patchMessageById(int messageId, String messageText) {
        Optional<Message> opt_message = messageRepository.findById(messageId);
        if ((opt_message.isEmpty())||(messageText.length() < 20)||(messageText.length() > 255)) {
            return 0;
        }
        else {
            Message message = opt_message.get();
            message.setMessageText(messageText);
            messageRepository.save(message);
            return 1;
        }
    }

    public List<Message> findAllMessagesByUser(int accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }
}
