package com.texthandler.interpreter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Процессор для обработки арифметических выражений в тексте
 */
public class ArithmeticExpressionProcessor {
    private static final Logger logger = LogManager.getLogger(ArithmeticExpressionProcessor.class);
    
    private static final String ARITHMETIC_EXPRESSION_PATTERN = "\\([0-9+\\-*/()\\s]+\\)";
    
    public String processExpressions(String text) {
        Pattern pattern = Pattern.compile(ARITHMETIC_EXPRESSION_PATTERN);
        Matcher matcher = pattern.matcher(text);
        
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String expression = matcher.group();
            try {
                double value = evaluateExpression(expression);
                matcher.appendReplacement(result, String.valueOf(value));
                logger.info("Evaluated expression {} = {}", expression, value);
            } catch (Exception e) {
                logger.error("Error evaluating expression: {}", expression, e);
                matcher.appendReplacement(result, expression); // Оставляем исходное выражение
            }
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    private double evaluateExpression(String expression) {
        // Удаляем внешние скобки и пробелы
        expression = expression.replaceAll("[\\s()]", "");
        
        return new ExpressionEvaluator().evaluate(expression);
    }
    
    private static class ExpressionEvaluator {
        private DoubleBinaryOperator add = Double::sum;
        private DoubleBinaryOperator subtract = (a, b) -> a - b;
        private DoubleBinaryOperator multiply = (a, b) -> a * b;
        private DoubleBinaryOperator divide = (a, b) -> a / b;
        private DoubleUnaryOperator negate = a -> -a;
        
        public double evaluate(String expression) {
            return parseExpression(expression, 0).value;
        }
        
        private Result parseExpression(String expr, int pos) {
            Result left = parseTerm(expr, pos);
            pos = left.pos;
            
            while (pos < expr.length()) {
                char op = expr.charAt(pos);
                if (op == '+' || op == '-') {
                    pos++;
                    Result right = parseTerm(expr, pos);
                    if (op == '+') {
                        left = new Result(add.applyAsDouble(left.value, right.value), right.pos);
                    } else {
                        left = new Result(subtract.applyAsDouble(left.value, right.value), right.pos);
                    }
                    pos = left.pos;
                } else {
                    break;
                }
            }
            
            return left;
        }
        
        private Result parseTerm(String expr, int pos) {
            Result left = parseFactor(expr, pos);
            pos = left.pos;
            
            while (pos < expr.length()) {
                char op = expr.charAt(pos);
                if (op == '*' || op == '/') {
                    pos++;
                    Result right = parseFactor(expr, pos);
                    if (op == '*') {
                        left = new Result(multiply.applyAsDouble(left.value, right.value), right.pos);
                    } else {
                        left = new Result(divide.applyAsDouble(left.value, right.value), right.pos);
                    }
                    pos = left.pos;
                } else {
                    break;
                }
            }
            
            return left;
        }
        
        private Result parseFactor(String expr, int pos) {
            if (pos < expr.length() && expr.charAt(pos) == '-') {
                Result result = parseFactor(expr, pos + 1);
                return new Result(negate.applyAsDouble(result.value), result.pos);
            }
            
            if (pos < expr.length() && expr.charAt(pos) == '(') {
                Result result = parseExpression(expr, pos + 1);
                return new Result(result.value, result.pos + 1); // +1 для закрывающей скобки
            }
            
            return parseNumber(expr, pos);
        }
        
        private Result parseNumber(String expr, int pos) {
            int start = pos;
            while (pos < expr.length() && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) {
                pos++;
            }
            return new Result(Double.parseDouble(expr.substring(start, pos)), pos);
        }
        
        private static class Result {
            double value;
            int pos;
            
            Result(double value, int pos) {
                this.value = value;
                this.pos = pos;
            }
        }
    }
}