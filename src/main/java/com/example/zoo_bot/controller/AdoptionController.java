package com.example.zoo_bot.controller;

import com.example.zoo_bot.model.entity.ShelterType;
import com.example.zoo_bot.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller для Этапа 2 — Консультация с потенциальным хозяином (усыновление).
 */
@RestController
@RequestMapping("/api/adoption")
@Tag(name = "Adoption", description = "API для консультаций по усыновлению животных из приюта")
@RequiredArgsConstructor
public class AdoptionController {

    private final AdoptionService adoptionService;

    /**
     * Получить список необходимых документов
     */
    @Operation(summary = "Список необходимых документов для усыновления")
    @GetMapping("/documents")
    public ResponseEntity<String> getRequiredDocuments(@RequestParam ShelterType shelterType) {
        String documents = adoptionService.getRequiredDocuments(shelterType);
        return ResponseEntity.ok(documents);
    }

    /**
     * Получить рекомендации по обустройству дома
     */
    @Operation(summary = "Рекомендации по обустройству дома")
    @GetMapping("/home-setup")
    public ResponseEntity<Map<String, String>> getHomeSetupRecommendations(
            @RequestParam ShelterType shelterType,
            @RequestParam String animalType) {

        Map<String, String> recommendations = adoptionService.getHomeSetupRecommendations(shelterType, animalType);
        return ResponseEntity.ok(recommendations);
    }

    /**
     * Получить рекомендации по транспортировке
     */
    @Operation(summary = "Рекомендации по транспортировке животного")
    @GetMapping("/transport")
    public ResponseEntity<String> getTransportRecommendations() {
        String transport = adoptionService.getTransportRecommendations();
        return ResponseEntity.ok(transport);
    }

    /**
     * Получить причины возможного отказа
     */
    @Operation(summary = "Причины возможного отказа в усыновлении")
    @GetMapping("/refusal-reasons")
    public ResponseEntity<String> getRefusalReasons(@RequestParam ShelterType shelterType) {
        String reasons = adoptionService.getRefusalReasons(shelterType);
        return ResponseEntity.ok(reasons);
    }

    /**
     * Получить советы кинолога (только для собак)
     */
    @Operation(summary = "Советы кинолога по первичному общению с собакой")
    @GetMapping("/kinolog-advice")
    public ResponseEntity<String> getKinologAdvice() {
        String advice = adoptionService.getKinologAdvice();
        return ResponseEntity.ok(advice);
    }

    /**
     * Получить список проверенных кинологов
     */
    @Operation(summary = "Рекомендуемые кинологи")
    @GetMapping("/kinologs")
    public ResponseEntity<String> getRecommendedKinologs() {
        String kinologs = adoptionService.getRecommendedKinologs();
        return ResponseEntity.ok(kinologs);
    }
}