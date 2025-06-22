package com.texthandler.composite;

/**
 * Листовой компонент текста (слово, знак препинания, символ)
 */
public class TextLeaf extends AbstractTextComponent {
    
    public TextLeaf(TextComponentType type, String content) {
        super(type);
        this.content = content;
    }

    @Override
    public void add(TextComponent component) {
        throw new UnsupportedOperationException("Cannot add child to leaf component");
    }

    @Override
    public void remove(TextComponent component) {
        throw new UnsupportedOperationException("Cannot remove child from leaf component");
    }

    @Override
    public TextComponent getChild(int index) {
        throw new UnsupportedOperationException("Leaf component has no children");
    }
}