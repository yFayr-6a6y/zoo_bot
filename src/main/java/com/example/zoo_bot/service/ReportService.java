package com.example.zoo_bot.service;

import com.example.zoo_bot.model.entity.Adopter;
import com.example.zoo_bot.model.entity.DailyReport;
import com.example.zoo_bot.repository.AdopterRepository;
import com.example.zoo_bot.repository.DailyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Сервис для работы с ежедневными отчётами усыновителей
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final AdopterRepository adopterRepository;
    private final DailyReportRepository dailyReportRepository;

    /**
     * Проверка, является ли пользователь усыновителем
     */
    public boolean isAdopter(Long chatId) {
        return adopterRepository.existsByUserChatId(chatId);
    }

    /**
     * Сохранить ежедневный отчёт
     */
    public DailyReport saveReport(Long chatId, String photoFileId, String ration,
                                  String wellBeing, String behaviorChanges) {

        Optional<Adopter> adopterOpt = adopterRepository.findByUserChatId(chatId);
        if (adopterOpt.isEmpty()) {
            return null;
        }

        Adopter adopter = adopterOpt.get();

        DailyReport report = DailyReport.builder()
                .adopter(adopter)
                .reportDate(LocalDateTime.now())
                .photoFileId(photoFileId)
                .ration(ration)
                .wellBeing(wellBeing)
                .behaviorChanges(behaviorChanges)
                .isComplete(true)
                .build();

        return dailyReportRepository.save(report);
    }

    /**
     * Проверить, отправлял ли пользователь отчёт сегодня
     */
    public boolean hasReportToday(Long chatId) {
        Optional<Adopter> adopterOpt = adopterRepository.findByUserChatId(chatId);
        if (adopterOpt.isEmpty()) {
            return false;
        }

        LocalDateTime todayStart = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return dailyReportRepository.existsByAdopterIdAndReportDateAfter(
                adopterOpt.get().getId(), todayStart);
    }
}