package com.texthandler.parser;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import com.texthandler.composite.TextComposite;
import com.texthandler.interpreter.ArithmeticExpressionProcessor;
import com.texthandler.util.RegexConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер уровня абзаца - разбивает на предложения
 */
public class ParagraphLevelParser extends TextParser {
    private final ArithmeticExpressionProcessor expressionProcessor;

    public ParagraphLevelParser() {
        this.expressionProcessor = new ArithmeticExpressionProcessor();
    }

    @Override
    public TextComponent parse(String text) {
        TextComponent paragraphComponent = new TextComposite(TextComponentType.PARAGRAPH);
        
        // Сначала обрабатываем арифметические выражения
        text = expressionProcessor.processExpressions(text);
        
        Pattern sentencePattern = Pattern.compile(RegexConstants.SENTENCE_SEPARATOR);
        Matcher matcher = sentencePattern.matcher(text);
        
        int lastEnd = 0;
        while (matcher.find()) {
            String sentenceText = text.substring(lastEnd, matcher.end()).trim();
            if (!sentenceText.isEmpty()) {
                TextComponent sentenceComponent = 
                    nextParser != null ? nextParser.parse(sentenceText) 
                                       : new TextComposite(TextComponentType.SENTENCE);
                paragraphComponent.add(sentenceComponent);
            }
            lastEnd = matcher.end();
        }
        
        // Обрабатываем последнее предложение, если оно есть
        if (lastEnd < text.length()) {
            String sentenceText = text.substring(lastEnd).trim();
            if (!sentenceText.isEmpty()) {
                TextComponent sentenceComponent = 
                    nextParser != null ? nextParser.parse(sentenceText) 
                                       : new TextComposite(TextComponentType.SENTENCE);
                paragraphComponent.add(sentenceComponent);
            }
        }
        
        return paragraphComponent;
    }
}