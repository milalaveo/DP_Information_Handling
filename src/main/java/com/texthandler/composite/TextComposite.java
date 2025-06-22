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
            
            // Добавляем разделители в зависимости от типа
            if (type == TextComponentType.TEXT && i < children.size() - 1) {
                sb.append("\n");
            } else if (type == TextComponentType.SENTENCE && i < children.size() - 1) {
                sb.append(" ");
            }
        }
        
        return sb.toString();
    }
}