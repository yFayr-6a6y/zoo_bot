package com.example.zoo_bot.model.entity;

import com.example.zoo_bot.state.UserState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Основная сущность пользователя бота
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long chatId;

    private String firstName;
    private String lastName;
    private String username;

    @Enumerated(EnumType.STRING)
    private ShelterType shelterType;

    @Enumerated(EnumType.STRING)
    private UserState currentState;

    private LocalDateTime lastActivity;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private boolean isAdopter = false;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastActivity = LocalDateTime.now();
    }
}