package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.Accounts.User;
import org.example.Management.SerializedManagement;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SerializedManagement serializedManagement = SerializedManagement.getInstance();

        Parent root;

        if (serializedManagement.getUsers().isEmpty()) {
           root =  FXMLLoader.load(getClass().getResource("digitallibrarymanagementsystem/SetupAdmin.fxml"));
        } else {
            root = FXMLLoader.load(getClass().getResource("digitallibrarymanagementsystem/start.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setTitle("Ardnaxela Library Management System");
        stage.setResizable(false);
        stage.setScene(scene);

        stage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/Pictures/SystemLogo.png"));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}