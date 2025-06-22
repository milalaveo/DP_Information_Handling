package com.texthandler.operation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class TextOperationFactoryTest {
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testCreateValidOperations(int operationNumber) {
        TextOperation operation = TextOperationFactory.createOperation(operationNumber);
        
        assertNotNull(operation);
        assertNotNull(operation.getName());
        assertNotNull(operation.getDescription());
        assertFalse(operation.getName().isEmpty());
        assertFalse(operation.getDescription().isEmpty());
    }
    
    @Test
    void testCreateInvalidOperation() {
        assertThrows(IllegalArgumentException.class, () -> {
            TextOperationFactory.createOperation(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            TextOperationFactory.createOperation(6);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            TextOperationFactory.createOperation(-1);
        });
    }
    
    @Test
    void testGetAvailableOperations() {
        var operations = TextOperationFactory.getAvailableOperations();
        
        assertNotNull(operations);
        assertEquals(5, operations.size());
        assertTrue(operations.contains(1));
        assertTrue(operations.contains(2));
        assertTrue(operations.contains(3));
        assertTrue(operations.contains(4));
        assertTrue(operations.contains(5));
    }
    
    @Test
    void testGetOperationMenu() {
        String menu = TextOperationFactory.getOperationMenu();
        
        assertNotNull(menu);
        assertFalse(menu.isEmpty());
        assertTrue(menu.contains("Доступные операции"));
        assertTrue(menu.contains("1."));
        assertTrue(menu.contains("2."));
        assertTrue(menu.contains("3."));
        assertTrue(menu.contains("4."));
        assertTrue(menu.contains("5."));
        assertTrue(menu.contains("0. Выход"));
    }
    
    @Test
    void testOperationTypes() {
        // Проверяем, что возвращаются правильные типы операций
        assertInstanceOf(SortParagraphsBySentenceCountOperation.class, TextOperationFactory.createOperation(1));
        assertInstanceOf(FindSentencesWithLongestWordOperation.class, TextOperationFactory.createOperation(2));
        assertInstanceOf(RemoveShortSentencesOperation.class, TextOperationFactory.createOperation(3));
        assertInstanceOf(CountIdenticalWordsOperation.class, TextOperationFactory.createOperation(4));
        assertInstanceOf(CountVowelsConsonantsOperation.class, TextOperationFactory.createOperation(5));
    }
}