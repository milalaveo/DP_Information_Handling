package com.texthandler.interpreter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Обработчик арифметических выражений в тексте
 * Находит выражения в скобках и отдельные арифметические выражения, заменяет их вычисленными значениями
 */
public class ArithmeticExpressionProcessor {
    private static final Logger logger = LogManager.getLogger(ArithmeticExpressionProcessor.class);
    
    // Паттерн для поиска выражений в круглых скобках (приоритет)
    private static final Pattern PARENTHESES_PATTERN = Pattern.compile("\\(([^()]*[+\\-*/][^()]*)\\)");
    
    // Паттерн для поиска арифметических выражений без скобок
    private static final Pattern ARITHMETIC_PATTERN = Pattern.compile("\\b\\d+(?:\\.\\d+)?(?:\\s*[+\\-*/]\\s*\\d+(?:\\.\\d+)?)+\\b");
    
    /**
     * Обрабатывает все арифметические выражения в тексте
     * @param text исходный текст
     * @return текст с вычисленными выражениями
     */
    public String processExpressions(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        String result = text;
        
        // Сначала обрабатываем выражения в скобках
        result = processParenthesesExpressions(result);
        
        // Затем обрабатываем обычные арифметические выражения
        result = processArithmeticExpressions(result);
        
        return result;
    }
    
    /**
     * Обрабатывает выражения в скобках
     */
    private String processParenthesesExpressions(String text) {
        String result = text;
        Matcher matcher = PARENTHESES_PATTERN.matcher(result);
        
        while (matcher.find()) {
            String fullMatch = matcher.group(0);  // Полное совпадение с скобками
            String expression = matcher.group(1); // Выражение без скобок
            
            try {
                double value = evaluateExpression(expression);
                result = result.replace(fullMatch, String.valueOf(value));
                logger.info("Processed parentheses expression: {} = {}", expression, value);
                
                // Обновляем matcher для продолжения поиска
                matcher = PARENTHESES_PATTERN.matcher(result);
            } catch (Exception e) {
                logger.warn("Failed to evaluate parentheses expression: {}, error: {}", expression, e.getMessage());
            }
        }
        
        return result;
    }
    
    /**
     * Обрабатывает простые арифметические выражения без скобок
     */
    private String processArithmeticExpressions(String text) {
        String result = text;
        Matcher matcher = ARITHMETIC_PATTERN.matcher(result);
        
        while (matcher.find()) {
            String expression = matcher.group(0);
            
            try {
                double value = evaluateExpression(expression);
                result = result.replace(expression, String.valueOf(value));
                logger.info("Processed arithmetic expression: {} = {}", expression, value);
                
                // Обновляем matcher для продолжения поиска
                matcher = ARITHMETIC_PATTERN.matcher(result);
            } catch (Exception e) {
                logger.warn("Failed to evaluate arithmetic expression: {}, error: {}", expression, e.getMessage());
            }
        }
        
        return result;
    }
    
    private double evaluateExpression(String expression) {
        // Сначала обрабатываем все вложенные скобки
        String processed = processNestedParentheses(expression);
        
        // Затем вычисляем результат
        return parseExpression(processed.trim());
    }

    private String processNestedParentheses(String expression) {
        while (expression.contains("(")) {
            // Находим самые внутренние скобки
            int start = -1;
            int end = -1;
            
            for (int i = 0; i < expression.length(); i++) {
                if (expression.charAt(i) == '(') {
                    start = i;
                } else if (expression.charAt(i) == ')') {
                    end = i;
                    break;
                }
            }
            
            if (start == -1 || end == -1) break;
            
            // Вычисляем выражение внутри скобок
            String innerExpr = expression.substring(start + 1, end);
            double result = parseExpression(innerExpr);
            
            // Заменяем скобки на результат
            expression = expression.substring(0, start) + result + expression.substring(end + 1);
        }
        
        return expression;
    }
    
    /**
     * Парсит и вычисляет выражение с учетом приоритета операций
     */
    private double parseExpression(String expression) {
        // Убираем лишние пробелы
        expression = expression.replaceAll("\\s+", "");
        return parseAddSubtract(expression, new ParsePosition(0));
    }
    
    /**
     * Парсит операции сложения и вычитания (наименьший приоритет)
     */
    private double parseAddSubtract(String expression, ParsePosition pos) {
        double result = parseMultiplyDivide(expression, pos);
        
        while (pos.index < expression.length()) {
            char operator = expression.charAt(pos.index);
            if (operator == '+' || operator == '-') {
                pos.index++; // пропускаем оператор
                double right = parseMultiplyDivide(expression, pos);
                if (operator == '+') {
                    result += right;
                } else {
                    result -= right;
                }
            } else {
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Парсит операции умножения и деления (средний приоритет)
     */
    private double parseMultiplyDivide(String expression, ParsePosition pos) {
        double result = parseFactor(expression, pos);
        
        while (pos.index < expression.length()) {
            char operator = expression.charAt(pos.index);
            if (operator == '*' || operator == '/') {
                pos.index++; // пропускаем оператор
                double right = parseFactor(expression, pos);
                if (operator == '*') {
                    result *= right;
                } else {
                    if (right == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    result /= right;
                }
            } else {
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Парсит числа, унарные операторы и выражения в скобках (наивысший приоритет)
     */
    private double parseFactor(String expression, ParsePosition pos) {
        if (pos.index >= expression.length()) {
            throw new IllegalArgumentException("Unexpected end of expression");
        }
        
        char ch = expression.charAt(pos.index);
        
        // Унарный минус
        if (ch == '-') {
            pos.index++;
            return -parseFactor(expression, pos);
        }
        
        // Унарный плюс
        if (ch == '+') {
            pos.index++;
            return parseFactor(expression, pos);
        }
        
        // Выражение в скобках
        if (ch == '(') {
            pos.index++; // пропускаем '('
            double result = parseAddSubtract(expression, pos);
            if (pos.index >= expression.length() || expression.charAt(pos.index) != ')') {
                throw new IllegalArgumentException("Missing closing parenthesis");
            }
            pos.index++; // пропускаем ')'
            return result;
        }
        
        // Число
        return parseNumber(expression, pos);
    }
    
    /**
     * Парсит число (целое или с плавающей точкой)
     */
    private double parseNumber(String expression, ParsePosition pos) {
        int start = pos.index;
        
        // Читаем цифры и точку
        while (pos.index < expression.length()) {
            char ch = expression.charAt(pos.index);
            if (Character.isDigit(ch) || ch == '.') {
                pos.index++;
            } else {
                break;
            }
        }
        
        if (start == pos.index) {
            throw new IllegalArgumentException("Expected number at position " + pos.index);
        }
        
        String numberStr = expression.substring(start, pos.index);
        try {
            return Double.parseDouble(numberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + numberStr);
        }
    }
    
    /**
     * Вспомогательный класс для отслеживания позиции при парсинге
     */
    private static class ParsePosition {
        int index;
        
        ParsePosition(int index) {
            this.index = index;
        }
    }
}