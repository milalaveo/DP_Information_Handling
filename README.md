# DP_Information_Handling

Приложение для обработки текста с поддержкой арифметических выражений и различных операций над текстовыми компонентами.

## Описание
Text DP_Information_Handling - это Java-приложение, которое:
- Парсит текст, разбивая его на иерархическую структуру (текст → абзацы → предложения → лексемы → слова/символы)
- Вычисляет арифметические выражения в скобках
- Предоставляет возможности для манипуляции текстом (сортировка, замена и др.)
- Использует паттерны Composite и Chain of Responsibility

## Архитектура
### Основные компоненты:
- **TextComponent** - базовый интерфейс для всех текстовых элементов
- **TextComposite** - составной компонент (текст, абзац, предложение)
- **TextLeaf** - листовой компонент (слово, символ)
- **ParserChain** - цепочка парсеров для разбора текста на разных уровнях
- **ArithmeticExpressionProcessor** - вычислитель арифметических выражений

### Структура проекта:
```
src/
├── main/java/com/texthandler/
│   ├── composite/          # Компоненты паттерна Composite
│   ├── parser/             # Парсеры текста
│   ├── evaluator/          # Вычислитель арифметических выражений
│   └── Main.java           # Главный класс
|   └── Demo.java           # Класс демонстрации (запуска всех возможностей)
├── test/java/              # Тесты
```

## Требования
- Java 21+
- JUnit 5 (для тестов)

## Сборка и запуск
### Компиляция:

```shell
javac -cp "lib/*" src/main/java/com/texthandler/**/*.java
```

### Запуск:

```shell
java -cp "lib/*:src/main/java" com.texthandler.Main
```

### Тесты:

```shell
mvn test
```

## Примеры использования
### Базовый парсинг текста:
``` java
// Создание цепочки парсеров
TextParser textParser = new TextLevelParser();
TextParser paragraphParser = new ParagraphLevelParser();
TextParser sentenceParser = new SentenceLevelParser();
TextParser lexemeParser = new LexemeLevelParser();

textParser.setNext(paragraphParser);
paragraphParser.setNext(sentenceParser);
sentenceParser.setNext(lexemeParser);

// Парсинг текста
String input = "Hello world! This is a test.";
TextComponent result = textParser.parse(input);

// Восстановление текста
String restored = result.restore();
System.out.println(restored); // "Hello world! This is a test."
```
### Вычисление арифметических выражений:
``` java
String input = "The result is (2+3*4) points.";
TextComponent result = textParser.parse(input);
String restored = result.restore();
System.out.println(restored); // "The result is 14.0 points."
```
### Работа с файлами:
``` java
// Чтение из файла
String content = Files.readString(Paths.get("input.txt"));
TextComponent result = textParser.parse(content);

// Обработка и сохранение
String processed = result.restore();
Files.writeString(Paths.get("output.txt"), processed);
```
## Поддерживаемые операции
### Арифметические выражения:
- Сложение: `(2+3)` → `5.0`
- Вычитание: `(10-3)` → `7.0`
- Умножение: `(4*5)` → `20.0`
- Деление: `(15/3)` → `5.0`
- Комбинированные: `(2+3*4)` → `14.0`

### Текстовые операции:
- Парсинг в иерархическую структуру
- Восстановление исходного текста
- Обход компонентов (паттерн Visitor)

## Примеры входных данных
### Простой текст:
``` 
Hello world! This is a simple text.
```
### Текст с арифметикой:
``` 
The calculation (2+3*4) gives us the result.
Another example: (100/5) equals twenty.
```
### Многоабзацный текст:
``` 
First paragraph with some text.

Second paragraph with calculation (5+5).

Third paragraph concludes the document.
```

### Основные тестовые сценарии:
- Парсинг различных типов текста
- Вычисление арифметических выражений
- Восстановление текста из компонентов
- Обработка граничных случаев
- Работа с файлами ресурсов
