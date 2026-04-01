package com.example.zoo_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Сервис для работы с волонтёрами
 */
@Service
public class VolunteerService {

    private final String volunteerChatId;

    public VolunteerService(@Value("${telegram.volunteer.chat-id:}") String volunteerChatId) {
        this.volunteerChatId = volunteerChatId;
    }

    /**
     * Вызвать волонтёра для конкретного пользователя
     */
    public void notifyVolunteer(TelegramLongPollingBot bot, Long userChatId, String userName, String message) {
        String notificationText = """
                🚨 Запрос от пользователя!
                
                Chat ID: %d
                Имя: %s
                Сообщение: %s
                
                Пожалуйста, свяжитесь с пользователем.
                """.formatted(userChatId, userName != null ? userName : "Не указано", message);

        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(volunteerChatId.isEmpty() ? userChatId.toString() : volunteerChatId)
                    .text(notificationText)
                    .build();

            bot.execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}