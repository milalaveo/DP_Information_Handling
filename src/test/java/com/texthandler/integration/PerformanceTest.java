package com.texthandler.integration;

import com.texthandler.composite.TextComponent;
import com.texthandler.operation.*;
import com.texthandler.parser.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;

class PerformanceTest {
    private TextParser parserChain;
    private String largeText;
    
    @BeforeEach
    void setUp() {
        // Создание цепочки парсеров
        TextParser textParser = new TextLevelParser();
        TextParser paragraphParser = new ParagraphLevelParser();
        TextParser sentenceParser = new SentenceLevelParser();
        TextParser lexemeParser = new LexemeLevelParser();
        
        textParser.setNext(paragraphParser);
        paragraphParser.setNext(sentenceParser);
        sentenceParser.setNext(lexemeParser);
        
        parserChain = textParser;
        
        // Создание большого текста для тестирования производительности
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("This is sentence number ").append(i).append(" with some words. ");
            if (i % 5 == 0) {
                sb.append("\n\n");
            }
        }
        largeText = sb.toString();
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testParsingPerformance() {
        TextComponent result = parserChain.parse(largeText);
        assertNotNull(result);
        assertFalse(result.getChildren().isEmpty());
    }
    
    @Test
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testOperationsPerformance() {
        TextComponent parsedText = parserChain.parse(largeText);
        
        // Тестируем все операции на производительность
        new SortParagraphsBySentenceCountOperation().execute(parsedText);
        new FindSentencesWithLongestWordOperation().execute(parsedText);
        new RemoveShortSentencesOperation(3).execute(parsedText);
        new CountIdenticalWordsOperation().execute(parsedText);
        new CountVowelsConsonantsOperation().execute(parsedText);
    }
    
    @Test
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        TextComponent parsedText = parserChain.parse(largeText);
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;
        
        // Проверяем что использование памяти разумное (меньше 50MB)
        assertTrue(memoryUsed < 50 * 1024 * 1024, 
                   "Memory usage too high: " + memoryUsed + " bytes");
    }
}