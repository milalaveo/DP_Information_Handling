
package com.texthandler.composite;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextLeafTest {
    
    @Test
    void testCreateTextLeaf() {
        TextLeaf leaf = new TextLeaf(TextComponentType.WORD, "test");
        
        assertEquals(TextComponentType.WORD, leaf.getType());
        assertEquals("test", leaf.getContent());
    }
    
    @Test
    void testRestore() {
        TextLeaf leaf = new TextLeaf(TextComponentType.WORD, "hello");
        assertEquals("hello", leaf.restore());
    }
    
    @Test
    void testGetChildren() {
        TextLeaf leaf = new TextLeaf(TextComponentType.WORD, "test");
        assertTrue(leaf.getChildren().isEmpty());
    }
    
    @Test
    void testAddNotSupported() {
        TextLeaf leaf = new TextLeaf(TextComponentType.WORD, "test");
        TextComponent other = new TextLeaf(TextComponentType.WORD, "other");
        
        assertThrows(UnsupportedOperationException.class, () -> {
            leaf.add(other);
        });
    }
    
    @Test
    void testRemoveNotSupported() {
        TextLeaf leaf = new TextLeaf(TextComponentType.WORD, "test");
        TextComponent other = new TextLeaf(TextComponentType.WORD, "other");
        
        assertThrows(UnsupportedOperationException.class, () -> {
            leaf.remove(other);
        });
    }
    
    @Test
    void testGetChildNotSupported() {
        TextLeaf leaf = new TextLeaf(TextComponentType.WORD, "test");
        
        assertThrows(UnsupportedOperationException.class, () -> {
            leaf.getChild(0);
        });
    }
}