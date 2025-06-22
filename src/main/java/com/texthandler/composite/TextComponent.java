package com.texthandler.composite;

import java.util.List;

/**
 * Базовый интерфейс для всех компонентов текста
 */
public interface TextComponent {
    void add(TextComponent component);
    void remove(TextComponent component);
    TextComponent getChild(int index);
    List<TextComponent> getChildren();
    String getContent();
    void setContent(String content);
    TextComponentType getType();
    String restore();
}