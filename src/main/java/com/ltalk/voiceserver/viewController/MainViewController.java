package com.ltalk.voiceserver.viewController;

import com.ltalk.voiceserver.controller.ChatServerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    TextArea textArea;
    @FXML
    Button button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void buttonClick() throws IOException {
        System.out.println("serverStart");

    }

}
