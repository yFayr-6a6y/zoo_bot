package com.example.zoo_bot.state;

/**
 * Все возможные состояния пользователя
 */
public enum UserState {

    // Этап 0
    START,
    CHOOSE_SHELTER,

    // Главное меню
    MAIN_MENU,

    // Этап 1 — Информация о приюте
    STAGE1_INFO_MENU,
    STAGE1_SCHEDULE,
    STAGE1_RULES,
    STAGE1_SAFETY,
    STAGE1_CONTACTS,

    // Этап 2 — Усыновление
    STAGE2_ADOPTION_MENU,
    STAGE2_DOCUMENTS,
    STAGE2_TRANSPORT,
    STAGE2_HOME_PUPPY_KITTEN,
    STAGE2_HOME_ADULT,
    STAGE2_HOME_SPECIAL_NEEDS,
    STAGE2_REASONS_FOR_REFUSAL,

    // Этап 3 — Отчёты
    STAGE3_REPORT_MENU,
    STAGE3_WAITING_FOR_PHOTO,
    STAGE3_WAITING_FOR_TEXT,

    // Служебные
    WAITING_VOLUNTEER,
    COLLECTING_CONTACTS
}