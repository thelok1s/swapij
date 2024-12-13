package com.starwars.swapij;

import java.util.List;
import java.util.Map;

public class SearchResult {
    private List<Map<String, Object>> results;

    public List<Map<String, Object>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, Object>> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        if (results == null || results.isEmpty()) {
            return "Ничего не найдено";
        }

        Map<String, Object> result = results.getFirst();
        StringBuilder sb = new StringBuilder();

        String[] priorFields = {"name", "title", "model"};
        sb.append("Основная информация:\n");
        for (String field : priorFields) {
            if (result.containsKey(field)) {
                sb.append(field).append(": ")
                        .append(result.get(field))
                        .append("\n");
            }
        }

        sb.append("Прочая информация:\n");
        result.forEach((key, value) -> {
            if (!List.of(priorFields).contains(key)) {
                sb.append(key).append(": ")
                        .append(value)
                        .append("\n");
            }
        });

        return sb.toString();
    }
}
