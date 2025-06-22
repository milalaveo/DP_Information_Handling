package com.texthandler.parser;

import com.texthandler.composite.TextComponent;
import com.texthandler.composite.TextComponentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ParserChainTest {
    private TextParser parserChain;
    
    @BeforeEach
    void setUp() {
        TextParser textParser = new TextLevelParser();
        TextParser paragraphParser = new ParagraphLevelParser();
        TextParser sentenceParser = new SentenceLevelParser();
        TextParser lexemeParser = new LexemeLevelParser();
        
        textParser.setNext(paragraphParser);
        paragraphParser.setNext(sentenceParser);
        sentenceParser.setNext(lexemeParser);
        
        parserChain = textParser;
    }
    
    @Test
    void testParseSimpleText() {
        String input = "Hello world!";
        TextComponent result = parserChain.parse(input);
        
        assertNotNull(result);
        assertEquals(TextComponentType.TEXT, result.getType());
        assertFalse(result.getChildren().isEmpty());
    }
    
    @Test
    void testParseMultipleParagraphs() {
        String input = "First paragraph.\n\nSecond paragraph.";
        TextComponent result = parserChain.parse(input);
        
        assertNotNull(result);
        assertEquals(TextComponentType.TEXT, result.getType());
        assertEquals(2, result.getChildren().size());
    }
    
    @Test
    void testParseWithArithmetic() {
        String input = "Result is (2+3) points.";
        TextComponent result = parserChain.parse(input);
        
        assertNotNull(result);
        String restored = result.restore();
        assertEquals("Result is 5.0 points.", restored);
    }

    @Test
    void testParseWithAdvancedArithmetic() {
        String input = "Result is 2+2*2 points.";
        TextComponent result = parserChain.parse(input);

        assertNotNull(result);
        String restored = result.restore();
        assertEquals("Result is 6.0 points.", restored);
    }
    
    @Test
    void testParseEmptyString() {
        String input = "";
        TextComponent result = parserChain.parse(input);
        
        assertNotNull(result);
        assertEquals(TextComponentType.TEXT, result.getType());
        assertTrue(result.getChildren().isEmpty());
    }
    
    @Test
    void testParseWhitespaceOnly() {
        String input = "   \n\n   ";
        TextComponent result = parserChain.parse(input);
        
        assertNotNull(result);
        assertEquals(TextComponentType.TEXT, result.getType());
    }
}