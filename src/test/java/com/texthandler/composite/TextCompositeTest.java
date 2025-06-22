package com.texthandler.composite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TextCompositeTest {
    private TextComposite textComposite;
    
    @BeforeEach
    void setUp() {
        textComposite = new TextComposite(TextComponentType.TEXT);
    }
    
    @Test
    void testCreateTextComposite() {
        assertEquals(TextComponentType.TEXT, textComposite.getType());
        assertTrue(textComposite.getChildren().isEmpty());
    }
    
    @Test
    void testAddChild() {
        TextComponent child = new TextLeaf(TextComponentType.WORD, "test");
        textComposite.add(child);
        
        assertEquals(1, textComposite.getChildren().size());
        assertEquals(child, textComposite.getChild(0));
    }
    
    @Test
    void testRemoveChild() {
        TextComponent child = new TextLeaf(TextComponentType.WORD, "test");
        textComposite.add(child);
        textComposite.remove(child);
        
        assertTrue(textComposite.getChildren().isEmpty());
    }
    
    @Test
    void testRestore() {
        TextComponent word1 = new TextLeaf(TextComponentType.WORD, "Hello");
        TextComponent space = new TextLeaf(TextComponentType.SYMBOL, " ");
        TextComponent word2 = new TextLeaf(TextComponentType.WORD, "World");
        
        textComposite.add(word1);
        textComposite.add(space);
        textComposite.add(word2);
        
        assertEquals("Hello World", textComposite.restore());
    }
    
    @Test
    void testGetChildOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            textComposite.getChild(0);
        });
    }
}