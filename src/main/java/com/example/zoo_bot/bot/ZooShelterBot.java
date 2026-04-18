package com.example.zoo_bot.bot;

import com.example.zoo_bot.keyboard.KeyboardFactory;
import com.example.zoo_bot.model.entity.ShelterType;
import com.example.zoo_bot.service.ReportService;
import com.example.zoo_bot.service.ShelterInfoService;
import com.example.zoo_bot.service.VolunteerService;
import com.example.zoo_bot.state.UserState;
import com.example.zoo_bot.state.UserStateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Основной класс Telegram-бота для приюта животных.
 * Простая и стабильная версия для сдачи работы.
 */
@Component
public class ZooShelterBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final UserStateService userStateService;
    private final KeyboardFactory keyboardFactory;
    private final ShelterInfoService shelterInfoService;
    private final ReportService reportService;
    private final VolunteerService volunteerService;

    public ZooShelterBot(@Value("${telegram.bot.token}") String botToken,
                         @Value("${telegram.bot.username}") String botUsername,
                         UserStateService userStateService,
                         KeyboardFactory keyboardFactory,
                         ShelterInfoService shelterInfoService,
                         ReportService reportService,
                         VolunteerService volunteerService) {
        super(botToken);
        this.botUsername = botUsername;
        this.userStateService = userStateService;
        this.keyboardFactory = keyboardFactory;
        this.shelterInfoService = shelterInfoService;
        this.reportService = reportService;
        this.volunteerService = volunteerService;

        System.out.println("✅ Zoo Bot успешно инициализирован");
        System.out.println("Username: @" + botUsername);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;

        long chatId = update.getMessage().getChatId();

        try {
            if (update.getMessage().hasText()) {
                String text = update.getMessage().getText().trim();
                System.out.println("Сообщение от " + chatId + ": " + text);
                handleTextMessage(chatId, text);
            } else if (update.getMessage().hasPhoto()) {
                System.out.println("Получено фото от " + chatId);
                handlePhotoMessage(chatId);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при обработке сообщения от " + chatId);
            e.printStackTrace();
        }
    }

    private void handleTextMessage(long chatId, String messageText) throws TelegramApiException {
        UserState currentState = userStateService.getState(chatId);

        if (messageText.equals("/start")) {
            userStateService.resetState(chatId);
            sendMessage(chatId, "Добрый день!\n\nЯ — бот приюта для собак.\nВыберите приют:",
                    keyboardFactory.createShelterChoiceKeyboard());
            userStateService.setState(chatId, UserState.CHOOSE_SHELTER);
            return;
        }

        switch (currentState) {
            case START, CHOOSE_SHELTER -> handleShelterChoice(chatId, messageText);
            case MAIN_MENU -> handleMainMenu(chatId, messageText);
            case STAGE1_INFO_MENU -> handleStage1Menu(chatId, messageText);
            case STAGE3_WAITING_FOR_PHOTO -> sendMessage(chatId, "Отправьте фото питомца для отчёта.");
            case WAITING_VOLUNTEER -> {
                volunteerService.notifyVolunteer(this, chatId, null, messageText);
                sendMessage(chatId, "Запрос отправлен волонтёру.", keyboardFactory.createMainMenuKeyboard());
                userStateService.setState(chatId, UserState.MAIN_MENU);
            }
            default -> sendMessage(chatId, "Используйте кнопки меню или введите /start.");
        }
    }

    private void handlePhotoMessage(long chatId) throws TelegramApiException {
        sendMessage(chatId, "Фото получено. Теперь отправьте текст отчёта.");
    }

    private void handleShelterChoice(long chatId, String messageText) throws TelegramApiException {
        ShelterType type = null;
        if (messageText.contains("собак") || messageText.contains("🐶")) type = ShelterType.DOG;

        if (type != null) {
            userStateService.setShelterType(chatId, type);
            userStateService.setState(chatId, UserState.MAIN_MENU);
            sendMessage(chatId, "✅ Приют для собак выбран.\n\nВыберите раздел:",
                    keyboardFactory.createMainMenuKeyboard());
        } else {
            sendMessage(chatId, "Пожалуйста, выберите приют кнопками.",
                    keyboardFactory.createShelterChoiceKeyboard());
        }
    }

    private void handleMainMenu(long chatId, String messageText) throws TelegramApiException {
        if (messageText.contains("информацию о приюте") || messageText.contains("📋")) {
            userStateService.setState(chatId, UserState.STAGE1_INFO_MENU);
            sendMessage(chatId, "📋 Информация о приюте\nВыберите пункт:",
                    keyboardFactory.createStage1MenuKeyboard());
        } else if (messageText.contains("взять животное") || messageText.contains("📝")) {
            sendMessage(chatId, "Раздел усыновления в разработке.",
                    keyboardFactory.createMainMenuKeyboard());
        } else if (messageText.contains("отчёт о питомце") || messageText.contains("📊")) {
            if (reportService.isAdopter(chatId)) {
                userStateService.setState(chatId, UserState.STAGE3_WAITING_FOR_PHOTO);
                sendMessage(chatId, "📊 Отправьте фото питомца:");
            } else {
                sendMessage(chatId, "Отчёты доступны только усыновителям.",
                        keyboardFactory.createMainMenuKeyboard());
            }
        } else if (messageText.contains("волонтёра") || messageText.contains("👤")) {
            userStateService.setState(chatId, UserState.WAITING_VOLUNTEER);
            sendMessage(chatId, "Напишите ваш вопрос.");
        } else {
            sendMessage(chatId, "Выберите действие кнопками.",
                    keyboardFactory.createMainMenuKeyboard());
        }
    }

    private void handleStage1Menu(long chatId, String messageText) throws TelegramApiException {
        sendMessage(chatId, "Информация о приюте (в разработке).",
                keyboardFactory.createBackToStage1Keyboard());
    }

    private void handleStage1Back(long chatId) throws TelegramApiException {
        userStateService.setState(chatId, UserState.STAGE1_INFO_MENU);
        sendMessage(chatId, "Меню информации о приюте:", keyboardFactory.createStage1MenuKeyboard());
    }

    private void sendMessage(long chatId, String text) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        execute(message);
    }

    private void sendMessage(long chatId, String text, Object replyMarkup) throws TelegramApiException {
        SendMessage.SendMessageBuilder builder = SendMessage.builder()
                .chatId(chatId)
                .text(text);

        if (replyMarkup != null) {
            builder.replyMarkup((org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard) replyMarkup);
        }

        execute(builder.build());
    }
}