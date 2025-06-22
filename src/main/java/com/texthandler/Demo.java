
package com.texthandler;

import com.texthandler.composite.TextComponent;
import com.texthandler.operation.*;
import com.texthandler.parser.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Демонстрационный класс - показывает работу всех операций без интерактивного ввода
 */
public class Demo {
    private static final Logger logger = LogManager.getLogger(Demo.class);
    
    public static void main(String[] args) {
        try {
            // Чтение и парсинг текста
            String inputText = Files.readString(Paths.get("src/main/resources/input.txt"));
            TextComponent parsedText = createParserChain().parse(inputText);
            
            System.out.println("=== АВТОМАТИЧЕСКАЯ ДЕМОНСТРАЦИЯ ВСЕХ ОПЕРАЦИЙ ===\n");
            System.out.println("Исходный текст:");
            System.out.println(parsedText.restore());
            System.out.println("\n" + "=".repeat(80) + "\n");
            
            // ПРЯМЫЕ ВЫЗОВЫ ВСЕХ ОПЕРАЦИЙ:
            
            // 1. Сортировка абзацев по количеству предложений
            executeOperation("ОПЕРАЦИЯ 1: СОРТИРОВКА АБЗАЦЕВ", 
                           new SortParagraphsBySentenceCountOperation(), parsedText);
            
            // 2. Поиск предложений с самым длинным словом
            executeOperation("ОПЕРАЦИЯ 2: ПОИСК САМЫХ ДЛИННЫХ СЛОВ", 
                           new FindSentencesWithLongestWordOperation(), parsedText);
            
            // 3. Удаление коротких предложений (с ЗАДАННЫМ параметром для демо)
            executeOperation("ОПЕРАЦИЯ 3: УДАЛЕНИЕ КОРОТКИХ ПРЕДЛОЖЕНИЙ (минимум 5 слов)", 
                           new RemoveShortSentencesOperation(5), parsedText);
            
            // 4. Подсчет одинаковых слов
            executeOperation("ОПЕРАЦИЯ 4: ПОДСЧЕТ ОДИНАКОВЫХ СЛОВ", 
                           new CountIdenticalWordsOperation(), parsedText);
            
            // 5. Подсчет гласных и согласных
            executeOperation("ОПЕРАЦИЯ 5: АНАЛИЗ ГЛАСНЫХ И СОГЛАСНЫХ", 
                           new CountVowelsConsonantsOperation(), parsedText);
            
            System.out.println("🎉 ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА!");
            
        } catch (IOException e) {
            logger.error("Error reading input file", e);
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error in demo", e);
            System.out.println("Ошибка демонстрации: " + e.getMessage());
        }
    }
    
    private static void executeOperation(String title, TextOperation operation, TextComponent text) {
        System.out.println("🔸 " + title);
        System.out.println("Описание: " + operation.getDescription());
        System.out.println("-".repeat(60));
        
        try {
            String result = operation.execute(text);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Ошибка выполнения операции: " + e.getMessage());
            logger.error("Error executing operation: " + operation.getName(), e);
        }
        
        System.out.println("=".repeat(80) + "\n");
    }
    
    private static TextParser createParserChain() {
        TextParser textParser = new TextLevelParser();
        TextParser paragraphParser = new ParagraphLevelParser();
        TextParser sentenceParser = new SentenceLevelParser();
        TextParser lexemeParser = new LexemeLevelParser();
        
        textParser.setNext(paragraphParser);
        paragraphParser.setNext(sentenceParser);
        sentenceParser.setNext(lexemeParser);
        
        return textParser;
    }
}