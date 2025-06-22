package com.texthandler.operation;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Операция поиска предложений с самым длинным словом
 */
public class FindSentencesWithLongestWordOperation implements TextOperation {
    private static final Logger logger = LogManager.getLogger(FindSentencesWithLongestWordOperation.class);

    @Override
    public String execute(TextComponent textComponent) {
        List<SentenceInfo> allSentences = new ArrayList<>();
        collectSentences(textComponent, allSentences);

        if (allSentences.isEmpty()) {
            return "В тексте не найдено предложений";
        }

        // Находим максимальную длину слова
        int maxWordLength = allSentences.stream()
                .mapToInt(s -> s.longestWordLength)
                .max()
                .orElse(0);

        // Фильтруем предложения с максимальной длиной слова
        List<SentenceInfo> sentencesWithLongestWord = allSentences.stream()
                .filter(s -> s.longestWordLength == maxWordLength)
                .toList();

        StringBuilder result = new StringBuilder();
        result.append("=== Предложения с самым длинным словом ===\n");
        result.append(String.format("Максимальная длина слова: %d символов\n\n", maxWordLength));

        for (SentenceInfo info : sentencesWithLongestWord) {
            result.append(String.format("Предложение: %s\n", info.text));
            result.append(String.format("Самое длинное слово: \"%s\" (%d символов)\n\n", 
                         info.longestWord, info.longestWordLength));
            
            logger.info("Found sentence with longest word: {} ({})", info.longestWord, info.longestWordLength);
        }

        return result.toString();
    }

    private void collectSentences(TextComponent component, List<SentenceInfo> sentences) {
        if (component.getType() == TextComponentType.SENTENCE) {
            String sentenceText = component.restore();
            WordInfo longestWord = findLongestWordInSentence(component);
            if (longestWord != null) {
                sentences.add(new SentenceInfo(sentenceText, longestWord.word, longestWord.length));
            }
        } else {
            for (TextComponent child : component.getChildren()) {
                collectSentences(child, sentences);
            }
        }
    }

    private WordInfo findLongestWordInSentence(TextComponent sentence) {
        WordInfo longestWord = null;
        
        for (TextComponent lexeme : sentence.getChildren()) {
            for (TextComponent wordComponent : lexeme.getChildren()) {
                if (wordComponent.getType() == TextComponentType.WORD) {
                    String word = wordComponent.getContent();
                    if (word != null && (longestWord == null || word.length() > longestWord.length)) {
                        longestWord = new WordInfo(word, word.length());
                    }
                }
            }
        }
        
        return longestWord;
    }

    @Override
    public String getDescription() {
        return "Находит все предложения, содержащие самое длинное слово в тексте";
    }

    @Override
    public String getName() {
        return "Поиск предложений с самым длинным словом";
    }

    private static class SentenceInfo {
        final String text;
        final String longestWord;
        final int longestWordLength;

        SentenceInfo(String text, String longestWord, int longestWordLength) {
            this.text = text;
            this.longestWord = longestWord;
            this.longestWordLength = longestWordLength;
        }
    }

    private static class WordInfo {
        final String word;
        final int length;

        WordInfo(String word, int length) {
            this.word = word;
            this.length = length;
        }
    }
}