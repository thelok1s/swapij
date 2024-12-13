package com.starwars.swapij;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Интерфейс для взаимодействия с API.
 * Использует аннотации Retrofit для выполнения HTTP-запросов.
 */
public interface SwapiApi {
    /**
     * Выполняет GET-запрос для поиска по заданной категории и текстовому запросу.
     *
     * @param category Категория данных (например, "people", "planets").
     * @param query    Поисковый запрос.
     * @return Объект {@link Call}, который возвращает {@link SearchResult}.
     */
    @GET("{category}/")
    Call<SearchResult> search(@Path("category") String category, @Query("search") String query);
}
