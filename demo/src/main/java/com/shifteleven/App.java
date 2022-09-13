package com.shifteleven;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * JavaFX App
 */
public class App extends Application {

    private final static Injector injector = Guice.createInjector(new Module());
    
    private Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("app"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory(injector::getInstance);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}