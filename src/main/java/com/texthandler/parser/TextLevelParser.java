
package com.texthandler.parser;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import com.texthandler.composite.TextComposite;
import com.texthandler.util.RegexConstants;

/**
 * Парсер уровня документа - разбивает на абзацы
 */
public class TextLevelParser extends TextParser {

    @Override
    public TextComponent parse(String text) {
        TextComponent textComponent = new TextComposite(TextComponentType.TEXT);
        
        String[] paragraphs = text.split(RegexConstants.PARAGRAPH_SEPARATOR);
        
        for (String paragraph : paragraphs) {
            if (!paragraph.trim().isEmpty()) {
                TextComponent paragraphComponent = 
                    nextParser != null ? nextParser.parse(paragraph.trim()) 
                                       : new TextComposite(TextComponentType.PARAGRAPH);
                textComponent.add(paragraphComponent);
            }
        }
        
        return textComponent;
    }
}