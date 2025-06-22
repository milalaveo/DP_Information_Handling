package com.texthandler.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Фабрика для создания операций над текстом
 */
public class TextOperationFactory {
    private static final Map<Integer, Class<? extends TextOperation>> OPERATIONS = new HashMap<>();
    
    static {
        OPERATIONS.put(1, SortParagraphsBySentenceCountOperation.class);
        OPERATIONS.put(2, FindSentencesWithLongestWordOperation.class);
        OPERATIONS.put(3, RemoveShortSentencesOperation.class);
        OPERATIONS.put(4, CountIdenticalWordsOperation.class);
        OPERATIONS.put(5, CountVowelsConsonantsOperation.class);
    }
    
    public static TextOperation createOperation(int operationNumber) {
        Class<? extends TextOperation> operationClass = OPERATIONS.get(operationNumber);
        if (operationClass == null) {
            throw new IllegalArgumentException("Неизвестная операция: " + operationNumber);
        }
        
        try {
            return operationClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания операции: " + operationNumber, e);
        }
    }
    
    public static Set<Integer> getAvailableOperations() {
        return OPERATIONS.keySet();
    }
    
    public static String getOperationMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== Доступные операции ===\n");
        
        try {
            for (int opNum : OPERATIONS.keySet()) {
                TextOperation operation = createOperation(opNum);
                menu.append(String.format("%d. %s\n", opNum, operation.getName()));
                menu.append(String.format("   %s\n", operation.getDescription()));
            }
        } catch (Exception e) {
            menu.append("Ошибка загрузки операций\n");
        }
        
        menu.append("0. Выход\n");
        return menu.toString();
    }
}