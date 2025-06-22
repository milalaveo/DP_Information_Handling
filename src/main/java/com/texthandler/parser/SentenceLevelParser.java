package com.texthandler.parser;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import com.texthandler.composite.TextComposite;
import com.texthandler.util.RegexConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер уровня предложения - разбивает на лексемы
 */
public class SentenceLevelParser extends TextParser {

    @Override
    public TextComponent parse(String text) {
        TextComponent sentenceComponent = new TextComposite(TextComponentType.SENTENCE);
        
        Pattern lexemePattern = Pattern.compile(RegexConstants.LEXEME_SEPARATOR);
        Matcher matcher = lexemePattern.matcher(text);
        
        while (matcher.find()) {
            String lexeme = matcher.group().trim();
            if (!lexeme.isEmpty()) {
                TextComponent lexemeComponent = 
                    nextParser != null ? nextParser.parse(lexeme) 
                                       : new TextComposite(TextComponentType.LEXEME);
                sentenceComponent.add(lexemeComponent);
            }
        }
        
        return sentenceComponent;
    }
}