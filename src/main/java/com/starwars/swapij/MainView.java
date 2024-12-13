package com.starwars.swapij;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.List;

/**
 * Класс главного представления приложения. Обеспечивает взаимодействие с графическим интерфейсом.
 */
public class MainView extends VBox {
    private static final Logger logger = LogManager.getLogger(MainView.class);

    private static final Map<String, String> CATEGORIES = Map.of(
            "Персонажи", "people",
            "Планеты", "planets",
            "Звездолёты", "starships",
            "Транспорт", "vehicles",
            "Фильмы", "films",
            "Расы", "species"
    );

    private final TextField searchField;
    private final ComboBox<String> categoryComboBox;
    private final TextArea resultArea;
    private final SwapiService swapiService;

    /**
     * Конструктор. Создает элементы управления и настраивает их действия.
     */
    public MainView() {
        setPadding(new Insets(10));
        setSpacing(10);

        searchField = new TextField();
        searchField.setPromptText("Введите поисковый запрос");

        categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(CATEGORIES.keySet());
        categoryComboBox.setValue("Персонажи");

        Button searchButton = new Button("Поиск");
        Button dictionaryButton = new Button("Показать словарь");

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefRowCount(20);

        swapiService = new SwapiService();

        searchButton.setOnAction(e -> performSearch());
        dictionaryButton.setOnAction(e -> showDictionary());

        getChildren().addAll(
                new Label("Категория:"),
                categoryComboBox,
                new Label("Поиск:"),
                searchField,
                searchButton,
                dictionaryButton,
                resultArea
        );
    }

    /**
     * Выполняет поиск по указанной категории и запросу. Использует {@link SwapiService}.
     */
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String russianCategory = categoryComboBox.getValue();
        String apiCategory = CATEGORIES.get(russianCategory);

        if (searchTerm.isEmpty()) {
            showError("Введите поисковый запрос!");
            return;
        }

        String translatedTerm = SearchTranslate.translate(searchTerm);

        resultArea.setText("Поиск по запросу " + searchTerm + "...");

        swapiService.search(apiCategory, translatedTerm,
                result -> {
                    if (result.isSuccess()) {
                        Platform.runLater(() ->
                                resultArea.setText(formatResult(result.getData()))
                        );
                    } else {
                        Platform.runLater(() ->
                                resultArea.setText("Ошибка: " + result.getError())
                        );
                    }
                }
        );
    }

    /**
     * Отображает словарь доступных переводов для поиска.
     */
    private void showDictionary() {
        resultArea.setText(SearchTranslate.getDictionary());
    }

    /**
     * Форматирует результаты поиска в удобочитаемый текст.
     *
     * @param result результат поиска.
     * @return строка с форматированными данными.
     */
    private String formatResult(SearchResult result) {

        if (result.getResults() == null || result.getResults().isEmpty()) {
            return "Ничего не найдено";
        }

        Map<String, Object> data = result.getResults().getFirst();
        StringBuilder sb = new StringBuilder();

        String[] priorityFields = {"name", "title", "model"};
        for (String field : priorityFields) {
            if (data.containsKey(field)) {
                String value = data.get(field).toString();
                if (ExcludeValue(value)) {
                    addField(sb, field, value);
                }
            }
        }
        sb.append("\n");

        data.forEach((key, value) -> {
            if (!List.of(priorityFields).contains(key) && value != null) {
                String strValue = value.toString();
                if (ExcludeValue(strValue)) {
                    addField(sb, key, value);
                }
            }
        });

        return sb.toString();
    }

    /**
     * Проверяет, нужно ли исключить определенное значение из вывода (ссылки, вложенные значения).
     *
     * @param value проверяемое значение.
     * @return false, если значение должно быть исключено, иначе true.
     */
    private boolean ExcludeValue(String value) {
        if (value.contains("[") || value.contains("]") ||
                value.contains("{") || value.contains("}") ||
                value.contains("(") || value.contains(")")) {
            return false;
        }
        if (value.contains("http://") || value.contains("https://")) {
            return false;
        }
        if (value.contains("000Z")) {
            return false;
        }
        return true;
    }

    /**
     * Добавляет отформатированное поле в результат.
     *
     * @param sb    StringBuilder для накопления текста.
     * @param key   имя поля.
     * @param value значение поля.
     */
    private void addField(StringBuilder sb, String key, Object value) {
        String fieldName = Translator.translateField(key);
        String formattedValue = formatValue(value);
        sb.append(fieldName).append(": ").append(formattedValue).append("\n");
    }

    /**
     * Форматирует значение для вывода.
     *
     * @param value значение для форматирования.
     * @return отформатированное значение.
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "нет данных";
        }

        String strValue = value.toString();

        String translated = Translator.translateValue(strValue);
        if (!translated.equals(strValue)) {
            return translated;
        }

        if (strValue.matches("\\d+")) {
            try {
                long number = Long.parseLong(strValue);
                if (number > 1000) {
                    return String.format("%,d", number).replace(",", " ");
                }
                return strValue;
            } catch (NumberFormatException e) {
                return strValue;
            }
        }

        return strValue;
    }

    /**
     * Отображает ошибку в виде всплывающего окна.
     *
     * @param message текст ошибки.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        logger.error(message);
    }

}