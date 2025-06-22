package com.texthandler.operation;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import com.texthandler.composite.TextComposite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Операция сортировки абзацев по количеству предложений
 */
public class SortParagraphsBySentenceCountOperation implements TextOperation {
    private static final Logger logger = LogManager.getLogger(SortParagraphsBySentenceCountOperation.class);

    @Override
    public String execute(TextComponent textComponent) {
        if (textComponent.getType() != TextComponentType.TEXT) {
            return "Операция применима только к полному тексту";
        }

        List<ParagraphInfo> paragraphInfos = new ArrayList<>();
        
        // Собираем информацию о абзацах
        for (int i = 0; i < textComponent.getChildren().size(); i++) {
            TextComponent paragraph = textComponent.getChild(i);
            int sentenceCount = paragraph.getChildren().size();
            paragraphInfos.add(new ParagraphInfo(i, paragraph, sentenceCount));
        }

        // Сортируем по количеству предложений
        paragraphInfos.sort((a, b) -> Integer.compare(a.sentenceCount, b.sentenceCount));

        // Создаем новый отсортированный текст
        TextComponent sortedText = new TextComposite(TextComponentType.TEXT);
        StringBuilder result = new StringBuilder();
        result.append("=== Абзацы отсортированы по количеству предложений ===\n\n");

        for (ParagraphInfo info : paragraphInfos) {
            sortedText.add(info.paragraph);
            result.append(String.format("Абзац %d (предложений: %d):\n", 
                         info.originalIndex + 1, info.sentenceCount));
            result.append(info.paragraph.restore()).append("\n\n");
            
            logger.info("Paragraph {} has {} sentences", info.originalIndex + 1, info.sentenceCount);
        }

        return result.toString();
    }

    @Override
    public String getDescription() {
        return "Сортирует абзацы по количеству предложений (по возрастанию)";
    }

    @Override
    public String getName() {
        return "Сортировка абзацев";
    }

    private static class ParagraphInfo {
        final int originalIndex;
        final TextComponent paragraph;
        final int sentenceCount;

        ParagraphInfo(int originalIndex, TextComponent paragraph, int sentenceCount) {
            this.originalIndex = originalIndex;
            this.paragraph = paragraph;
            this.sentenceCount = sentenceCount;
        }
    }
}