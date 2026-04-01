package com.example.zoo_bot.state;

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
    public void setShelterType(long chatId, String shelterType) {
        userShelterTypes.put(chatId, shelterType.toUpperCase());
    }

    /**
     * Получить выбранный приют пользователя
     */
    public String getShelterType(long chatId) {
        return userShelterTypes.get(chatId);
    }

    /**
     * Сбросить состояние пользователя (при /start)
     */
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