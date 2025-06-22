package com.texthandler.operation;

import com.texthandler.composite.TextComponent;

/**
 * Базовый интерфейс для операций над текстом
 */
public interface TextOperation {
    /**
     * Выполнить операцию над текстом
     * @param textComponent компонент текста для обработки
     * @return результат операции в виде строки
     */
    String execute(TextComponent textComponent);
    
    /**
     * Получить описание операции
     * @return описание операции
     */
    String getDescription();
    
    /**
     * Получить название операции
     * @return название операции
     */
    String getName();
}