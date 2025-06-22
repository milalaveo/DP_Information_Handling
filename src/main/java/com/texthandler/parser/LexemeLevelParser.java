package com.texthandler.parser;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import com.texthandler.composite.TextComposite;
import com.texthandler.composite.TextLeaf;
import com.texthandler.util.RegexConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер уровня лексемы - разбивает на слова и знаки препинания
 */
public class LexemeLevelParser extends TextParser {

    @Override
    public TextComponent parse(String text) {
        TextComponent lexemeComponent = new TextComposite(TextComponentType.LEXEME);
        
        Pattern wordPattern = Pattern.compile(RegexConstants.WORD_PATTERN);
        Pattern punctuationPattern = Pattern.compile(RegexConstants.PUNCTUATION_PATTERN);
        
        int lastEnd = 0;
        Matcher wordMatcher = wordPattern.matcher(text);
        
        while (wordMatcher.find()) {
            // Добавляем знаки препинания перед словом
            if (wordMatcher.start() > lastEnd) {
                String punctuation = text.substring(lastEnd, wordMatcher.start());
                Matcher punctMatcher = punctuationPattern.matcher(punctuation);
                while (punctMatcher.find()) {
                    if (!punctMatcher.group().trim().isEmpty()) {
                        lexemeComponent.add(new TextLeaf(TextComponentType.PUNCTUATION, punctMatcher.group()));
                    }
                }
            }
            
            // Добавляем слово
            lexemeComponent.add(new TextLeaf(TextComponentType.WORD, wordMatcher.group()));
            lastEnd = wordMatcher.end();
        }
        
        // Добавляем оставшиеся знаки препинания
        if (lastEnd < text.length()) {
            String punctuation = text.substring(lastEnd);
            Matcher punctMatcher = punctuationPattern.matcher(punctuation);
            while (punctMatcher.find()) {
                if (!punctMatcher.group().trim().isEmpty()) {
                    lexemeComponent.add(new TextLeaf(TextComponentType.PUNCTUATION, punctMatcher.group()));
                }
            }
        }
        
        return lexemeComponent;
    }
}