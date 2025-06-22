package com.texthandler.operation;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import com.texthandler.composite.TextComposite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Операция удаления предложений с количеством слов меньше заданного
 */
public class RemoveShortSentencesOperation implements TextOperation {
    private static final Logger logger = LogManager.getLogger(RemoveShortSentencesOperation.class);
    private final int minWordCount;

    public RemoveShortSentencesOperation(int minWordCount) {
        this.minWordCount = minWordCount;
    }

    public RemoveShortSentencesOperation() {
        this.minWordCount = getMinWordCountFromUser();
    }

    @Override
    public String execute(TextComponent textComponent) {
        TextComponent filteredText = filterText(textComponent);
        
        StringBuilder result = new StringBuilder();
        result.append(String.format("=== Удалены предложения с количеством слов меньше %d ===\n\n", minWordCount));
        result.append("Результирующий текст:\n");
        result.append(filteredText.restore());
        
        return result.toString();
    }

    private TextComponent filterText(TextComponent component) {
        if (component.getType() == TextComponentType.TEXT) {
            TextComponent newText = new TextComposite(TextComponentType.TEXT);
            for (TextComponent paragraph : component.getChildren()) {
                TextComponent filteredParagraph = filterParagraph(paragraph);
                if (!filteredParagraph.getChildren().isEmpty()) {
                    newText.add(filteredParagraph);
                }
            }
            return newText;
        } else if (component.getType() == TextComponentType.PARAGRAPH) {
            return filterParagraph(component);
        }
        return component;
    }

    private TextComponent filterParagraph(TextComponent paragraph) {
        TextComponent newParagraph = new TextComposite(TextComponentType.PARAGRAPH);
        
        for (TextComponent sentence : paragraph.getChildren()) {
            if (sentence.getType() == TextComponentType.SENTENCE) {
                int wordCount = countWordsInSentence(sentence);
                if (wordCount >= minWordCount) {
                    newParagraph.add(sentence);
                    logger.info("Kept sentence with {} words: {}", wordCount, sentence.restore());
                } else {
                    logger.info("Removed sentence with {} words: {}", wordCount, sentence.restore());
                }
            }
        }
        
        return newParagraph;
    }

    private int countWordsInSentence(TextComponent sentence) {
        int wordCount = 0;
        
        for (TextComponent lexeme : sentence.getChildren()) {
            for (TextComponent word : lexeme.getChildren()) {
                if (word.getType() == TextComponentType.WORD) {
                    wordCount++;
                }
            }
        }
        
        return wordCount;
    }

    private int getMinWordCountFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите минимальное количество слов в предложении: ");
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            logger.warn("Invalid input, using default value 3");
            return 3; // значение по умолчанию
        }
    }

    @Override
    public String getDescription() {
        return String.format("Удаляет из текста все предложения с количеством слов меньше %d", minWordCount);
    }

    @Override
    public String getName() {
        return "Удаление коротких предложений";
    }
}