package com.ltalk.voiceserver;

import com.ltalk.voiceserver.controller.ChatServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ServerMain.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        ChatServerController.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}
