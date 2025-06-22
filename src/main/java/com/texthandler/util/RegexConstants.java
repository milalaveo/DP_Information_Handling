package com.texthandler.util;

/**
 * Константы регулярных выражений для парсинга текста
 */
public final class RegexConstants {
    
    public static final String PARAGRAPH_SEPARATOR = "\\n\\s*\\n|\\n\\t|\\n\\s{4,}";
    
    public static final String SENTENCE_SEPARATOR = "(?<=[.!?…])\\s+(?=[А-ЯA-Z])";
    
    public static final String LEXEME_SEPARATOR = "\\S+";
    
    public static final String WORD_PATTERN = "\\d+\\.\\d+|\\b\\w+\\b";
    
    public static final String PUNCTUATION_PATTERN = "[\\p{Punct}\\s]+";
    
    private RegexConstants() {
        // Утилитарный класс
    }
}