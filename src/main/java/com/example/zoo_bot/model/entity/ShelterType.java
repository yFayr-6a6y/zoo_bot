package com.example.zoo_bot.model.entity;

/**
 * Тип приюта
 */
public enum ShelterType {
    CAT("Приют для кошек"),
    DOG("Приют для собак");

    private final String description;

    ShelterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}