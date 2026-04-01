package com.example.zoo_bot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Ежедневный отчёт усыновителя
 */
@Entity
@Table(name = "daily_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adopter_id", nullable = false)
    private Adopter adopter;

    private LocalDateTime reportDate;

    @Column(columnDefinition = "TEXT")
    private String photoFileId;        // file_id из Telegram

    @Column(columnDefinition = "TEXT")
    private String ration;             // рацион

    @Column(columnDefinition = "TEXT")
    private String wellBeing;          // самочувствие и привыкание

    @Column(columnDefinition = "TEXT")
    private String behaviorChanges;    // изменения в поведении

    private boolean isComplete = false;
}