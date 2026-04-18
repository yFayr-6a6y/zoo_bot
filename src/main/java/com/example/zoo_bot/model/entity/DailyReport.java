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
    private String photoFileId;

    @Column(columnDefinition = "TEXT")
    private String ration;

    @Column(columnDefinition = "TEXT")
    private String wellBeing;

    @Column(columnDefinition = "TEXT")
    private String behaviorChanges;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private boolean isComplete = false;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private boolean isViewed = false;
}