package com.example.zoo_bot.controller;

import com.example.zoo_bot.model.entity.DailyReport;
import com.example.zoo_bot.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller для работы с ежедневными отчётами усыновителей (Этап 3).
 *
 * Реализует требования технического задания:
 * - Получение необработанных отчётов
 * - Отметка отчёта как просмотренного
 * - Получение отчёта по ID
 * - Отправка предупреждения усыновителю
 */
@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "API для управления ежедневными отчётами усыновителей")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Получить список всех необработанных (непросмотренных) отчётов.
     * Используется волонтёрами для проверки отчётов после 21:00.
     */
    @Operation(summary = "Получить все необработанные отчёты")
    @GetMapping("/unprocessed")
    public ResponseEntity<List<DailyReport>> getUnprocessedReports() {
        List<DailyReport> reports = reportService.getUnprocessedReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Отметить отчёт как просмотренный волонтёром.
     */
    @Operation(summary = "Отметить отчёт как просмотренный")
    @PutMapping("/{reportId}/viewed")
    public ResponseEntity<Void> markAsViewed(@PathVariable Long reportId) {
        boolean success = reportService.markAsViewed(reportId);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Получить отчёт по его идентификатору.
     */
    @Operation(summary = "Получить отчёт по ID")
    @GetMapping("/{reportId}")
    public ResponseEntity<DailyReport> getReportById(@PathVariable Long reportId) {
        return reportService.findById(reportId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Отправить предупреждение усыновителю о ненадлежащем заполнении отчёта.
     */
    @Operation(summary = "Отправить предупреждение усыновителю")
    @PostMapping("/{reportId}/warning")
    public ResponseEntity<Void> sendWarning(
            @PathVariable Long reportId,
            @RequestBody String warningMessage) {

        boolean success = reportService.sendWarningToAdopter(reportId, warningMessage);
        return success
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    /**
     * Получить все отчёты конкретного усыновителя по chatId.
     */
    @Operation(summary = "Получить все отчёты усыновителя")
    @GetMapping("/adopter/{chatId}")
    public ResponseEntity<List<DailyReport>> getReportsByAdopter(@PathVariable Long chatId) {
        List<DailyReport> reports = reportService.getReportsByAdopter(chatId);
        return ResponseEntity.ok(reports);
    }
}