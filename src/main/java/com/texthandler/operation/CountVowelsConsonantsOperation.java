package com.texthandler.operation;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Операция подсчета гласных и согласных букв в предложениях
 */
public class CountVowelsConsonantsOperation implements TextOperation {
    private static final Logger logger = LogManager.getLogger(CountVowelsConsonantsOperation.class);
    
    private static final String VOWELS = "аеёиоуыэюяaeiouy";
    private static final String CONSONANTS = "бвгджзйклмнпрстфхцчшщъьбвгджзклмнпрстфхцчшщbcdfghjklmnpqrstvwxyz";
    
    private static final Pattern VOWEL_PATTERN = Pattern.compile("[" + VOWELS + "]", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONSONANT_PATTERN = Pattern.compile("[" + CONSONANTS + "]", Pattern.CASE_INSENSITIVE);

    @Override
    public String execute(TextComponent textComponent) {
        List<SentenceAnalysis> analyses = new ArrayList<>();
        collectSentenceAnalyses(textComponent, analyses);

        if (analyses.isEmpty()) {
            return "В тексте не найдено предложений";
        }

        StringBuilder result = new StringBuilder();
        result.append("=== Анализ гласных и согласных букв в предложениях ===\n\n");

        int totalVowels = 0;
        int totalConsonants = 0;

        for (int i = 0; i < analyses.size(); i++) {
            SentenceAnalysis analysis = analyses.get(i);
            result.append(String.format("Предложение %d:\n", i + 1));
            result.append(String.format("\"%s\"\n", analysis.text));
            result.append(String.format("Гласных: %d, Согласных: %d\n\n", 
                         analysis.vowelCount, analysis.consonantCount));
            
            totalVowels += analysis.vowelCount;
            totalConsonants += analysis.consonantCount;
            
            logger.info("Sentence {}: vowels={}, consonants={}", 
                       i + 1, analysis.vowelCount, analysis.consonantCount);
        }

        result.append("=== Общая статистика ===\n");
        result.append(String.format("Всего предложений: %d\n", analyses.size()));
        result.append(String.format("Всего гласных: %d\n", totalVowels));
        result.append(String.format("Всего согласных: %d\n", totalConsonants));
        result.append(String.format("Среднее количество гласных на предложение: %.2f\n", 
                     (double) totalVowels / analyses.size()));
        result.append(String.format("Среднее количество согласных на предложение: %.2f\n", 
                     (double) totalConsonants / analyses.size()));

        return result.toString();
    }

    private void collectSentenceAnalyses(TextComponent component, List<SentenceAnalysis> analyses) {
        if (component.getType() == TextComponentType.SENTENCE) {
            String sentenceText = component.restore();
            LetterCount count = countLettersInText(sentenceText);
            analyses.add(new SentenceAnalysis(sentenceText, count.vowels, count.consonants));
        } else {
            for (TextComponent child : component.getChildren()) {
                collectSentenceAnalyses(child, analyses);
            }
        }
    }

    private LetterCount countLettersInText(String text) {
        int vowels = 0;
        int consonants = 0;
        
        for (char c : text.toCharArray()) {
            String charStr = String.valueOf(c);
            if (VOWEL_PATTERN.matcher(charStr).matches()) {
                vowels++;
            } else if (CONSONANT_PATTERN.matcher(charStr).matches()) {
                consonants++;
            }
        }
        
        return new LetterCount(vowels, consonants);
    }

    @Override
    public String getDescription() {
        return "Подсчитывает количество гласных и согласных букв в каждом предложении";
    }

    @Override
    public String getName() {
        return "Подсчет гласных и согласных";
    }

    private static class SentenceAnalysis {
        final String text;
        final int vowelCount;
        final int consonantCount;

        SentenceAnalysis(String text, int vowelCount, int consonantCount) {
            this.text = text;
            this.vowelCount = vowelCount;
            this.consonantCount = consonantCount;
        }
    }

    private static class LetterCount {
        final int vowels;
        final int consonants;

        LetterCount(int vowels, int consonants) {
            this.vowels = vowels;
            this.consonants = consonants;
        }
    }
}