package com.example.zoo_bot.service;

import com.example.zoo_bot.model.entity.User;
import com.example.zoo_bot.model.entity.ShelterType;
import com.example.zoo_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreateUser(Long chatId, String firstName, String lastName, String username) {
        Optional<User> existing = userRepository.findByChatId(chatId);

        if (existing.isPresent()) {
            return existing.get();
        }

        User user = User.builder()
                .chatId(chatId)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .currentState(null)
                .build();

        return userRepository.save(user);
    }

    public void updateShelterType(Long chatId, ShelterType shelterType) {
        Optional<User> userOpt = userRepository.findByChatId(chatId);
        userOpt.ifPresent(user -> {
            user.setShelterType(shelterType);
            userRepository.save(user);
        });
    }

    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }
}