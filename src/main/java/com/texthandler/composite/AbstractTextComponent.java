package com.texthandler.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактная реализация компонента текста
 */
public abstract class AbstractTextComponent implements TextComponent {
    protected List<TextComponent> children = new ArrayList<>();
    protected String content;
    protected TextComponentType type;

    public AbstractTextComponent(TextComponentType type) {
        this.type = type;
    }

    @Override
    public void add(TextComponent component) {
        children.add(component);
    }

    @Override
    public void remove(TextComponent component) {
        children.remove(component);
    }

    @Override
    public TextComponent getChild(int index) {
        return children.get(index);
    }

    @Override
    public List<TextComponent> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public TextComponentType getType() {
        return type;
    }

    @Override
    public String restore() {
        if (children.isEmpty()) {
            return content != null ? content : "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (TextComponent child : children) {
            sb.append(child.restore());
        }
        return sb.toString();
    }
}