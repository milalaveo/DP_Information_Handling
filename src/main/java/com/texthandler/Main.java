package com.texthandler;

import com.texthandler.composite.TextComponent;
import com.texthandler.operation.TextOperation;
import com.texthandler.operation.TextOperationFactory;
import com.texthandler.parser.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Главный класс приложения для обработки текста
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            // Чтение файла
            String inputText = Files.readString(Paths.get("src/main/resources/input.txt"));
            logger.info("Text loaded from file");
            
            // Создание цепочки парсеров
            TextParser textParser = createParserChain();
            
            // Парсинг текста
            TextComponent parsedText = textParser.parse(inputText);
            logger.info("Text parsed successfully");
            
            System.out.println("=== СИСТЕМА ОБРАБОТКИ ТЕКСТА ===\n");
            System.out.println("Исходный текст:");
            System.out.println(parsedText.restore());
            System.out.println("\n" + "=".repeat(50) + "\n");
            
            // Главное меню
            boolean running = true;
            while (running) {
                System.out.println(TextOperationFactory.getOperationMenu());
                System.out.print("Выберите операцию (0-5): ");
                
                try {
                    int choice = scanner.nextInt();
                    
                    if (choice == 0) {
                        running = false;
                        System.out.println("До свидания!");
                        continue;
                    }
                    
                    if (!TextOperationFactory.getAvailableOperations().contains(choice)) {
                        System.out.println("Неверный выбор. Пожалуйста, выберите число от 0 до 5.\n");
                        continue;
                    }
                    
                    // Выполнение операции
                    TextOperation operation = TextOperationFactory.createOperation(choice);
                    System.out.println("\n" + "=".repeat(60));
                    System.out.println("Выполняется: " + operation.getName());
                    System.out.println("=".repeat(60));
                    
                    String result = operation.execute(parsedText);
                    System.out.println(result);
                    
                    System.out.println("=".repeat(60));
                    System.out.println("Операция завершена. Нажмите Enter для продолжения...");
                    scanner.nextLine(); // Очистка буфера
                    scanner.nextLine(); // Ожидание Enter
                    
                } catch (Exception e) {
                    logger.error("Error executing operation", e);
                    System.out.println("Ошибка выполнения операции: " + e.getMessage());
                    scanner.nextLine(); // Очистка буфера
                }
            }
            
        } catch (IOException e) {
            logger.error("Error reading input file", e);
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing text", e);
            System.out.println("Ошибка обработки текста: " + e.getMessage());
        }
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