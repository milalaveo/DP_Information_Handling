package com.texthandler.operation;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Операция подсчета одинаковых слов без учета регистра
 */
public class CountIdenticalWordsOperation implements TextOperation {
    private static final Logger logger = LogManager.getLogger(CountIdenticalWordsOperation.class);

    @Override
    public String execute(TextComponent textComponent) {
        Map<String, Integer> wordCounts = new HashMap<>();
        collectWords(textComponent, wordCounts);

        if (wordCounts.isEmpty()) {
            return "В тексте не найдено слов";
        }

        // Фильтруем слова, которые встречаются более одного раза
        Map<String, Integer> duplicateWords = wordCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1
                ));

        StringBuilder result = new StringBuilder();
        result.append("=== Одинаковые слова в тексте (без учета регистра) ===\n\n");

        if (duplicateWords.isEmpty()) {
            result.append("Повторяющихся слов не найдено.\n");
        } else {
            result.append("Найдены повторяющиеся слова:\n");
            
            // Сортируем по количеству вхождений (по убыванию)
            duplicateWords.entrySet().stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                    .forEach(entry -> {
                        result.append(String.format("- \"%s\": %d раз(а)\n", 
                                    entry.getKey(), entry.getValue()));
                        logger.info("Word '{}' appears {} times", entry.getKey(), entry.getValue());
                    });
        }

        result.append(String.format("\nВсего уникальных слов: %d\n", wordCounts.size()));
        result.append(String.format("Повторяющихся слов: %d\n", duplicateWords.size()));

        return result.toString();
    }

    private void collectWords(TextComponent component, Map<String, Integer> wordCounts) {
        if (component.getType() == TextComponentType.WORD) {
            String word = component.getContent();
            if (word != null && !word.trim().isEmpty()) {
                String normalizedWord = word.toLowerCase().trim();
                wordCounts.put(normalizedWord, wordCounts.getOrDefault(normalizedWord, 0) + 1);
            }
        } else {
            for (TextComponent child : component.getChildren()) {
                collectWords(child, wordCounts);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Находит все одинаковые слова без учета регистра и подсчитывает их количество";
    }

    @Override
    public String getName() {
        return "Подсчет одинаковых слов";
    }
}