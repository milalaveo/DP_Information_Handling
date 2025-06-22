package com.texthandler.parser;

import com.texthandler.composite.TextComponent;

/**
 * Базовый интерфейс для парсеров текста
 */
public abstract class TextParser {
    protected TextParser nextParser;

    public void setNext(TextParser parser) {
        this.nextParser = parser;
    }

    public abstract TextComponent parse(String text);
}