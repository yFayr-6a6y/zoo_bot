package com.example.zoo_bot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Усыновитель животного
 */
@Entity
@Table(name = "adopters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ShelterType shelterType;

    private Long animalId;                    // ID животного в приюте
    private String animalName;

    private LocalDate adoptionDate;
    private LocalDate trialEndDate;           // конец испытательного срока (30 дней по умолчанию)

    @Column(columnDefinition = "boolean default true")
    private boolean isOnTrial = true;         // находится ли на испытательном сроке
}