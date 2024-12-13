module com.starwars.swapij {
    requires java.desktop;
    requires javafx.fxml;
    requires javafx.controls;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires com.google.gson;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens com.starwars.swapij to com.google.gson;
    exports  com.starwars.swapij to javafx.graphics;
}