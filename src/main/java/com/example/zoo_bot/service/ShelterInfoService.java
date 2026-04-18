package com.example.zoo_bot.service;

import com.example.zoo_bot.model.entity.ShelterType;
import org.springframework.stereotype.Service;

/**
 * Сервис для предоставления информации о приюте
 */
@Service
public class ShelterInfoService {

    /**
     * Возвращает информацию о приюте в зависимости от типа
     */
    public String getShelterInfo(ShelterType shelterType, String section) {
        if (shelterType == null) {
            return "Сначала выберите приют.";
        }

        return switch (section.toLowerCase()) {
            case "schedule", "расписание" -> getScheduleInfo(shelterType);
            case "rules", "правила" -> getRulesInfo(shelterType);
            case "safety", "безопасность" -> getSafetyInfo();
            case "contacts", "охрана" -> getContactsInfo();
            default -> "Информация по данному разделу пока недоступна.";
        };
    }

    private String getScheduleInfo(ShelterType shelterType) {
        if (shelterType == ShelterType.CAT) {
            return """
                    📍 Приют для кошек
                    
                    Адрес: г. Москва, ул. Лесная, д. 45
                    Время работы: ежедневно с 10:00 до 18:00
                    Выходные: 1 января, 9 мая
                    """;
        } else {
            return """
                    📍 Приют для собак
                    
                    Адрес: г. Москва, ул. Полевая, д. 12
                    Время работы: ежедневно с 09:00 до 17:00
                    Выходные: 1 января, 9 мая
                    """;
        }
    }

    private String getRulesInfo(ShelterType shelterType) {
        return """
                📜 Общие правила посещения приюта
                
                • Обязательна предварительная запись
                • При себе иметь паспорт
                • Запрещено кормить животных без разрешения волонтёра
                • Запрещено самостоятельно открывать вольеры
                • Дети младше 14 лет — только в сопровождении взрослых
                • Фотографировать можно, но без вспышки
                """;
    }

    private String getSafetyInfo() {
        return """
                🛡️ Техника безопасности на территории
                
                • Не оставляйте вещи без присмотра
                • Соблюдайте дистанцию с животными
                • При укусе или царапине сразу обращайтесь к волонтёру
                • Используйте антисептик после контакта с животными
                """;
    }

    private String getContactsInfo() {
        return """
                📞 Контакты охраны
                
                Телефон: +7 (495) 123-45-67
                Звонок обязателен за 30 минут до прибытия.
                
                Для проезда на машине:
                • Назовите ФИО и номер паспорта
                • Укажите марку и номер автомобиля
                """;
    }
}