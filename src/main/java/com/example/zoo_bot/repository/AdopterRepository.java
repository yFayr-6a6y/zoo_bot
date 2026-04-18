package com.example.zoo_bot.repository;

import com.example.zoo_bot.model.entity.Adopter;
import com.example.zoo_bot.model.entity.ShelterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    Optional<Adopter> findByUserChatId(Long chatId);

    boolean existsByUserChatId(Long chatId);

    Optional<Adopter> findByUserChatIdAndShelterType(Long chatId, ShelterType shelterType);
}