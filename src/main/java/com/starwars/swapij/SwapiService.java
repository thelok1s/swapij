package com.starwars.swapij;

import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Интерфейс для работы с API swapi.dev.
 * Выполняет запросы к API и обрабатывает ответы, используя библиотеку Retrofit.
 */
public class SwapiService {
    private static final Logger logger = LogManager.getLogger(SwapiService.class);
    private final SwapiApi api;

    /**
     * Конструктор SwapiService.
     * Инициализирует объект Retrofit и создает реализацию интерфейса SwapiApi.
     */
    public SwapiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://swapi.dev/api/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        api = retrofit.create(SwapiApi.class);
    }

    /**
     * Выполняет поиск по API с заданной категорией и запросом.
     *
     * @param category  Категория поиска.
     * @param query     Текст поискового запроса.
     * @param callback  Колбэк для обработки результата поиска.
     */
    public void search(String category, String query, SearchCallback callback) {
        api.search(category, query).enqueue(new Callback<>() {
            /**
             * Обрабатывает успешный ответ от API.
             *
             * @param call     Исходный запрос.
             * @param response Ответ API, содержащий результаты поиска.
             */
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getResults() != null
                        && !response.body().getResults().isEmpty()) {
                    callback.onResult(new Result<>(response.body()));
                } else {
                    String error = "Ничего не найдено";
                    logger.error(error);
                    callback.onResult(new Result<>(error));
                }
            }

            /**
             * Обрабатывает ошибку запроса.
             *
             * @param call Исходный запрос.
             * @param t    Исключение, вызванное ошибкой.
             */
            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                String error = "проблема с сетью – " + t.getMessage();
                logger.error(error, t);
                callback.onResult(new Result<>(error));
            }
        });
    }
}