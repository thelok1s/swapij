package com.starwars.swapij;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Утилитарный класс для перевода поисковых запросов.
 */
public class SearchTranslate {
    private static final Logger logger = LogManager.getLogger(SearchTranslate.class);
    private static Map<String, TranslationEntry> translations;


    static class TranslationEntry {
        String display;
        List<String> search;
    }

    static {
        loadTranslations();
    }

    private static void loadTranslations() {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(SearchTranslate.class.getResourceAsStream("/dictionary.json")),
                StandardCharsets.UTF_8)) {
            translations = new Gson().fromJson(reader, new TypeToken<Map<String, TranslationEntry>>() {}.getType());
        } catch (Exception e) {
            logger.error("Ошибка загрузки перевода", e);
            translations = Map.of();
        }
    }

    /**
     * Переводит пользовательский запрос на английский язык.
     *
     * @param query поисковый запрос.
     * @return переведенный запрос.
     */
    public static String translate(String query) {
        if (query == null || query.trim().isEmpty()) {
            return query;
        }

        String lowercaseQuery = query.toLowerCase().trim();

        for (Map.Entry<String, TranslationEntry> entry : translations.entrySet()) {
            if (entry.getValue().search.contains(lowercaseQuery)) {
                return entry.getKey();
            }
        }
        return query;
    }

    /**
     * Возвращает текст словаря для отображения пользователю.
     *
     * @return строка со словарем.
     */
    public static String getDictionary() {
        StringBuilder sb = new StringBuilder("Словарь поиска:\n\n");

        translations.forEach((englishTerm, entry) -> sb.append(entry.display)
                .append(" → ")
                .append(englishTerm)
                .append("\n"));

        return sb.toString();
    }
}

