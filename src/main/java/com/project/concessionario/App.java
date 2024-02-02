 package com.project.concessionario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


 public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
        //Attach the icon to the stage/window
        stage.getIcons().add((new Image(App.class.getResource("icona.png").toExternalForm())));
        Scene scene = new Scene(fxmlLoader.load(), 700, 350);
        stage.setMaximized(true);
        stage.setTitle("Concessionario");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}