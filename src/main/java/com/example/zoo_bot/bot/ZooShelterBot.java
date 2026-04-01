package com.example.zoo_bot.bot;

import com.example.zoo_bot.keyboard.KeyboardFactory;
import com.example.zoo_bot.model.entity.ShelterType;
import com.example.zoo_bot.service.ReportService;
import com.example.zoo_bot.state.UserState;
import com.example.zoo_bot.state.UserStateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ZooShelterBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final UserStateService userStateService;
    private final KeyboardFactory keyboardFactory;
    private final ReportService reportService;

    public ZooShelterBot(@Value("${telegram.bot.token}") String botToken,
                         @Value("${telegram.bot.username}") String botUsername,
                         UserStateService userStateService,
                         KeyboardFactory keyboardFactory,
                         ReportService reportService) {
        super(botToken);
        this.botUsername = botUsername;
        this.userStateService = userStateService;
        this.keyboardFactory = keyboardFactory;
        this.reportService = reportService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();

            try {
                if (update.getMessage().hasText()) {
                    handleTextMessage(chatId, update.getMessage().getText().trim());
                } else if (update.getMessage().hasPhoto()) {
                    handlePhotoMessage(chatId, update.getMessage().getPhoto().get(0).getFileId());
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTextMessage(long chatId, String messageText) throws TelegramApiException {
        UserState currentState = userStateService.getState(chatId);

        if (messageText.equals("/start")) {
            userStateService.resetState(chatId);
            sendMessage(chatId, "Добрый день!\n\nЯ — официальный бот приюта животных.\nВыберите приют:",
                    keyboardFactory.createShelterChoiceKeyboard());
            userStateService.setState(chatId, UserState.CHOOSE_SHELTER);
            return;
        }

        switch (currentState) {
            case START, CHOOSE_SHELTER -> handleShelterChoice(chatId, messageText);
            case MAIN_MENU -> handleMainMenu(chatId, messageText);
            case STAGE1_INFO_MENU -> handleStage1Menu(chatId, messageText);
            case STAGE1_SCHEDULE, STAGE1_RULES, STAGE1_SAFETY, STAGE1_CONTACTS -> handleStage1Back(chatId);
            case STAGE2_ADOPTION_MENU -> handleStage2Menu(chatId, messageText);
            case STAGE2_DOCUMENTS, STAGE2_TRANSPORT, STAGE2_HOME_PUPPY_KITTEN,
                 STAGE2_HOME_ADULT, STAGE2_HOME_SPECIAL_NEEDS, STAGE2_REASONS_FOR_REFUSAL ->
                    handleStage2Back(chatId);
            case STAGE3_REPORT_MENU -> handleStage3Menu(chatId, messageText);
            case STAGE3_WAITING_FOR_TEXT -> handleReportText(chatId, messageText);
            default -> sendMessage(chatId, "Пожалуйста, используйте кнопки меню или введите /start.");
        }
    }

    private void handlePhotoMessage(long chatId, String photoFileId) throws TelegramApiException {
        UserState currentState = userStateService.getState(chatId);

        if (currentState == UserState.STAGE3_WAITING_FOR_PHOTO) {
            // Сохраняем фото и переходим к ожиданию текста
            userStateService.setState(chatId, UserState.STAGE3_WAITING_FOR_TEXT);
            // Здесь можно сохранить fileId временно (в будущем через сервис)
            sendMessage(chatId, "Фото получено. Теперь отправьте текстовую часть отчёта:\n" +
                    "• Рацион\n• Самочувствие и привыкание\n• Изменения в поведении");
        } else {
            sendMessage(chatId, "Фото можно отправлять только при подаче ежедневного отчёта.");
        }
    }

    // ==================== Этап 0 ====================
    private void handleShelterChoice(long chatId, String messageText) throws TelegramApiException {
        String shelterType = null;
        if (messageText.contains("кошек") || messageText.contains("🐱")) shelterType = "CAT";
        else if (messageText.contains("собак") || messageText.contains("🐶")) shelterType = "DOG";

        if (shelterType != null) {
            userStateService.setShelterType(chatId, shelterType);
            userStateService.setState(chatId, UserState.MAIN_MENU);
            String name = "CAT".equals(shelterType) ? "для кошек" : "для собак";
            sendMessage(chatId, "✅ Приют " + name + " выбран.\n\nВыберите раздел:",
                    keyboardFactory.createMainMenuKeyboard());
        } else {
            sendMessage(chatId, "Пожалуйста, выберите приют кнопками.",
                    keyboardFactory.createShelterChoiceKeyboard());
        }
    }

    // ==================== Главное меню ====================
    private void handleMainMenu(long chatId, String messageText) throws TelegramApiException {
        if (messageText.contains("информацию о приюте") || messageText.contains("📋")) {
            userStateService.setState(chatId, UserState.STAGE1_INFO_MENU);
            sendMessage(chatId, "📋 Информация о приюте\nВыберите пункт:",
                    keyboardFactory.createStage1MenuKeyboard());

        } else if (messageText.contains("взять животное") || messageText.contains("📝")) {
            userStateService.setState(chatId, UserState.STAGE2_ADOPTION_MENU);
            sendMessage(chatId, "📝 Как взять животное\nВыберите раздел:",
                    keyboardFactory.createStage2MenuKeyboard());

        } else if (messageText.contains("отчёт о питомце") || messageText.contains("📊")) {
            if (reportService.isAdopter(chatId)) {
                userStateService.setState(chatId, UserState.STAGE3_REPORT_MENU);
                sendMessage(chatId, "📊 Подача ежедневного отчёта\nОтправьте фото животного:",
                        null); // без клавиатуры, ждём фото
                userStateService.setState(chatId, UserState.STAGE3_WAITING_FOR_PHOTO);
            } else {
                sendMessage(chatId, "Подача отчётов доступна только зарегистрированным усыновителям.\n" +
                                "Для регистрации обратитесь к волонтёру.",
                        keyboardFactory.createMainMenuKeyboard());
            }
        } else if (messageText.contains("волонтёра") || messageText.contains("👤")) {
            sendMessage(chatId, "👤 Волонтёр уведомлён. Ожидайте связи.");
        } else {
            sendMessage(chatId, "Выберите действие кнопками.",
                    keyboardFactory.createMainMenuKeyboard());
        }
    }

    // ==================== Этап 1 (Информация о приюте) ====================
    private void handleStage1Menu(long chatId, String messageText) throws TelegramApiException {
        if (messageText.contains("Адрес и расписание")) {
            userStateService.setState(chatId, UserState.STAGE1_SCHEDULE);
            sendMessage(chatId, "📍 Адрес и расписание приюта (зависит от типа приюта).",
                    keyboardFactory.createBackToStage1Keyboard());
        } else if (messageText.contains("Правила посещения")) {
            userStateService.setState(chatId, UserState.STAGE1_RULES);
            sendMessage(chatId, "📜 Правила посещения приюта.",
                    keyboardFactory.createBackToStage1Keyboard());
        } else if (messageText.contains("Техника безопасности")) {
            userStateService.setState(chatId, UserState.STAGE1_SAFETY);
            sendMessage(chatId, "🛡️ Техника безопасности.",
                    keyboardFactory.createBackToStage1Keyboard());
        } else if (messageText.contains("Контакты охраны")) {
            userStateService.setState(chatId, UserState.STAGE1_CONTACTS);
            sendMessage(chatId, "📞 Контакты охраны.",
                    keyboardFactory.createBackToStage1Keyboard());
        } else if (messageText.contains("Вернуться в главное меню")) {
            userStateService.setState(chatId, UserState.MAIN_MENU);
            sendMessage(chatId, "Возврат в главное меню.", keyboardFactory.createMainMenuKeyboard());
        }
    }

    private void handleStage1Back(long chatId) throws TelegramApiException {
        userStateService.setState(chatId, UserState.STAGE1_INFO_MENU);
        sendMessage(chatId, "Меню информации о приюте:", keyboardFactory.createStage1MenuKeyboard());
    }

    // ==================== Этап 2 (Усыновление) ====================
    private void handleStage2Menu(long chatId, String messageText) throws TelegramApiException {
        if (messageText.contains("Правила знакомства")) {
            sendMessage(chatId, "📋 Правила знакомства:\nПеред усыновлением проводятся 2–3 встречи с животным под контролем волонтёра.",
                    keyboardFactory.createBackToStage2Keyboard());
        } else if (messageText.contains("Необходимые документы")) {
            sendMessage(chatId, "📄 Необходимые документы:\n• Паспорт РФ\n• Анкета усыновителя\n• Договор ответственного содержания",
                    keyboardFactory.createBackToStage2Keyboard());
        } else if (messageText.contains("Транспортировка")) {
            sendMessage(chatId, "🚗 Рекомендации по транспортировке:\nИспользуйте переноску. Для собак — поводок и намордник.",
                    keyboardFactory.createBackToStage2Keyboard());
        } else if (messageText.contains("Обустройство дома")) {
            sendMessage(chatId, "🏠 Выберите тип животного:", keyboardFactory.createHomeSetupMenuKeyboard());
        } else if (messageText.contains("Причины возможного отказа")) {
            sendMessage(chatId, "🚫 Основные причины отказа в усыновлении перечислены в меню.",
                    keyboardFactory.createBackToStage2Keyboard());
        } else if (messageText.contains("Вернуться в главное меню")) {
            userStateService.setState(chatId, UserState.MAIN_MENU);
            sendMessage(chatId, "Возврат в главное меню.", keyboardFactory.createMainMenuKeyboard());
        }
    }

    private void handleStage2Back(long chatId) throws TelegramApiException {
        userStateService.setState(chatId, UserState.STAGE2_ADOPTION_MENU);
        sendMessage(chatId, "Меню усыновления:", keyboardFactory.createStage2MenuKeyboard());
    }

    // ==================== Этап 3 — Ежедневные отчёты ====================
    private void handleStage3Menu(long chatId, String messageText) throws TelegramApiException {
        sendMessage(chatId, "Отправьте фото животного для начала отчёта.");
        userStateService.setState(chatId, UserState.STAGE3_WAITING_FOR_PHOTO);
    }

    private void handleReportText(long chatId, String messageText) throws TelegramApiException {
        // Здесь в будущем будет сохранение полного отчёта через ReportService
        sendMessage(chatId, "Отчёт получен. Спасибо!\n\nВолонтёры проверят его после 21:00.",
                keyboardFactory.createMainMenuKeyboard());
        userStateService.setState(chatId, UserState.MAIN_MENU);
    }

    // ==================== Методы отправки сообщений ====================
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