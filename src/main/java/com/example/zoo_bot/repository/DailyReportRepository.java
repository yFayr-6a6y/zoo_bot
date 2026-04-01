package com.example.zoo_bot.repository;

import com.example.zoo_bot.model.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

    List<DailyReport> findByAdopterIdOrderByReportDateDesc(Long adopterId);

    boolean existsByAdopterIdAndReportDateAfter(Long adopterId, LocalDateTime date);
}