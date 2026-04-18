package com.example.zoo_bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Фабрика клавиатур для бота.
 * Содержит все основные меню для Этапов 0, 1 и 2.
 */
@Component
public class KeyboardFactory {

    /**
     * Клавиатура выбора приюта (Этап 0)
     */
    public ReplyKeyboardMarkup createShelterChoiceKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("🐱 Приют для кошек"));
        keyboard.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("🐶 Приют для собак"));
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Главное меню (после выбора приюта)
     */
    public ReplyKeyboardMarkup createMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📋 Узнать информацию о приюте");
        row1.add("📝 Как взять животное");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("📊 Прислать отчёт о питомце");
        row2.add("👤 Позвать волонтёра");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Меню Этапа 1 — Информация о приюте
     */
    public ReplyKeyboardMarkup createStage1MenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📍 Адрес и расписание");
        row1.add("📜 Правила посещения");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("🛡️ Техника безопасности");
        row2.add("📞 Контакты охраны");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("🔙 Вернуться в главное меню");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Клавиатура возврата в меню Этапа 1
     */
    public ReplyKeyboardMarkup createBackToStage1Keyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("🔙 Вернуться в меню информации о приюте");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Клавиатура Этапа 2 — Как взять животное из приюта
     */
    public ReplyKeyboardMarkup createStage2MenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📋 Правила знакомства с животным");
        row1.add("📄 Необходимые документы");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("🚗 Транспортировка животного");
        row2.add("🏠 Обустройство дома");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("🚫 Причины возможного отказа");
        row3.add("🔙 Вернуться в главное меню");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Подменю обустройства дома
     */
    public ReplyKeyboardMarkup createHomeSetupMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("🐾 Для щенка / котёнка");
        row1.add("🐕 Для взрослого животного");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("♿ Для животного с ограниченными возможностями");
        row2.add("🔙 Вернуться в меню усыновления");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Клавиатура возврата в меню Этапа 2
     */
    public ReplyKeyboardMarkup createBackToStage2Keyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("🔙 Вернуться в меню усыновления");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}