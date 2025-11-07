package com.example.bp2jinte;

import com.example.bp2jinte.models.Gebruiker;
import com.example.bp2jinte.screens.LoginScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookstoreSystem extends javafx.application.Application {

    public static final int[] applicationSize = {1200, 650};
    public static Stage mainStage;
    public static Scene loginScene;
    public static DatabaseConnection db;
    public static Gebruiker loggedInUser;

    @Override
    public void start(Stage stage) {

        db = new DatabaseConnection();

        mainStage = stage;
        mainStage.setTitle("Bookstore Management System");
        mainStage.setWidth(applicationSize[0]);
        mainStage.setHeight(applicationSize[1]);
        mainStage.setResizable(false);

        // Load login screen
        loginScene = new LoginScreen().getScene();
        mainStage.setScene(loginScene);
        mainStage.show();
    }

    // Helper method for switching scenes
    public static void navigateTo(Scene scene) {
        mainStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
