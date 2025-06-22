package com.texthandler.composite;

/**
 * Составной компонент текста (документ, абзац, предложение)
 */
public class TextComposite extends AbstractTextComponent {
    
    public TextComposite(TextComponentType type) {
        super(type);
    }

    @Override
    public String restore() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < children.size(); i++) {
            TextComponent child = children.get(i);
            sb.append(child.restore());
            
            // Добавляем разделители только если они не представлены явно
            if (i < children.size() - 1) {
                TextComponent nextChild = children.get(i + 1);
                
                // Если следующий элемент не является символом-разделителем, добавляем пробел
                if (type == TextComponentType.SENTENCE && 
                    (nextChild.getType() == TextComponentType.WORD || 
                     nextChild.getType() == TextComponentType.LEXEME)) {
                    sb.append(" ");
                }
            }
        }
        
        return sb.toString();
    }
}