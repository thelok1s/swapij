package com.starwars.swapij;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Основной класс приложения, запускающий JavaFX приложение.
 */
public class MainApplication extends Application {
    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    /**
     * Точка входа для JavaFX приложения. Инициализирует главное окно и сцену.
     *
     * @param primaryStage основной контейнер для отображения UI.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            MainView mainView = new MainView();
            Scene scene = new Scene(mainView, 600, 400);
            primaryStage.setTitle("SWAPIJ");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            logger.error("Ошибка при запуске", e);
        }
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
    }
}