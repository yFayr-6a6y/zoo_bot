package com.example.zoo_bot.service;

import com.example.zoo_bot.model.entity.Adopter;
import com.example.zoo_bot.model.entity.DailyReport;
import com.example.zoo_bot.repository.AdopterRepository;
import com.example.zoo_bot.repository.DailyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с ежедневными отчётами усыновителей (Этап 3)
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
     * Получить усыновителя по chatId
     */
    public Optional<Adopter> findAdopterByChatId(Long chatId) {
        return adopterRepository.findByUserChatId(chatId);
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

        DailyReport report = DailyReport.builder()
                .adopter(adopterOpt.get())
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
     * Получить все необработанные отчёты (для волонтёров)
     */
    public List<DailyReport> getUnprocessedReports() {
        return dailyReportRepository.findByIsCompleteTrueAndIsViewedFalse();
    }

    /**
     * Отметить отчёт как просмотренный
     */
    public boolean markAsViewed(Long reportId) {
        Optional<DailyReport> reportOpt = dailyReportRepository.findById(reportId);
        if (reportOpt.isPresent()) {
            DailyReport report = reportOpt.get();
            report.setViewed(true);
            dailyReportRepository.save(report);
            return true;
        }
        return false;
    }

    /**
     * Получить отчёт по ID
     */
    public Optional<DailyReport> findById(Long reportId) {
        return dailyReportRepository.findById(reportId);
    }

    /**
     * Получить все отчёты конкретного усыновителя
     */
    public List<DailyReport> getReportsByAdopter(Long chatId) {
        Optional<Adopter> adopterOpt = adopterRepository.findByUserChatId(chatId);
        return adopterOpt.map(adopter ->
                        dailyReportRepository.findByAdopterIdOrderByReportDateDesc(adopter.getId()))
                .orElse(List.of());
    }

    /**
     * Отправить предупреждение усыновителю (вызывается волонтёром)
     */
    public boolean sendWarningToAdopter(Long reportId, String warningMessage) {
        Optional<DailyReport> reportOpt = dailyReportRepository.findById(reportId);
        if (reportOpt.isPresent()) {
            // Здесь в будущем можно отправить сообщение через бот
            System.out.println("Предупреждение отправлено усыновителю: " + warningMessage);
            return true;
        }
        return false;
    }

    /**
     * Проверить, сдал ли пользователь отчёт сегодня
     */
    public boolean hasReportToday(Long chatId) {
        Optional<Adopter> adopterOpt = adopterRepository.findByUserChatId(chatId);
        if (adopterOpt.isEmpty()) return false;

        LocalDateTime todayStart = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return dailyReportRepository.existsByAdopterIdAndReportDateAfter(
                adopterOpt.get().getId(), todayStart);
    }
}