package com.starwars.swapij;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * Утилитарный класс для перевода полей и значений интерфейса.
 */
public class Translator {
    private static final Logger logger = LogManager.getLogger(Translator.class);
    static Map<String, Map<String, String>> translations;

    static {
        loadTranslations();
    }

    private static void loadTranslations() {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(Translator.class.getResourceAsStream("/ui_dict.json")),
                StandardCharsets.UTF_8)) {
            translations = new Gson().fromJson(reader,
                    new TypeToken<Map<String, Map<String, String>>>() {}.getType());
        } catch (Exception e) {
            logger.error("Ошибка загрузки перевода", e);
            translations = Map.of();
        }
    }

    /**
     * Переводит имя поля.
     *
     * @param field имя поля.
     * @return переведенное имя.
     */
    public static String translateField(String field) {
        return translations.get("fields").getOrDefault(field, field);
    }

    /**
     * Переводит значение поля.
     *
     * @param value значение поля.
     * @return переведенное значение.
     */
    public static String translateValue(String value) {
        if (value == null) {
            return "нет данных";
        }
        return translations.get("values").getOrDefault(value.toLowerCase(), value);
    }

}
