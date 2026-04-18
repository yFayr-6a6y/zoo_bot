package com.example.zoo_bot.state;

import com.example.zoo_bot.model.entity.ShelterType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис для управления состояниями пользователей (Finite State Machine).
 * Хранит текущее состояние каждого пользователя по chatId.
 */
@Service
public class UserStateService {

    // Временное хранилище состояний в памяти
    // В дальнейшем можно перенести в базу данных
    private final Map<Long, UserState> userStates = new ConcurrentHashMap<>();
    private final Map<Long, String> userShelterTypes = new ConcurrentHashMap<>(); // "CAT" или "DOG"

    /**
     * Получить текущее состояние пользователя
     */
    public UserState getState(long chatId) {
        return userStates.getOrDefault(chatId, UserState.START);
    }

    /**
     * Установить новое состояние пользователю
     */
    public void setState(long chatId, UserState state) {
        userStates.put(chatId, state);
    }

    /**
     * Установить выбранный приют для пользователя
     */
    /**
     * Установить выбранный приют для пользователя (принимает enum)
     */
    public void setShelterType(long chatId, ShelterType shelterType) {
        if (shelterType != null) {
            userShelterTypes.put(chatId, shelterType.name());
        }
    }

    /**
     * Установить выбранный приют для пользователя (принимает String)
     */
    public void setShelterType(long chatId, String shelterTypeStr) {
        if (shelterTypeStr != null) {
            userShelterTypes.put(chatId, shelterTypeStr.toUpperCase());
        }
    }
    public void resetState(long chatId) {
        userStates.put(chatId, UserState.START);
        userShelterTypes.remove(chatId);
    }

    /**
     * Проверить, выбран ли приют
     */
    public boolean hasShelterChosen(long chatId) {
        return userShelterTypes.containsKey(chatId);
    }
}