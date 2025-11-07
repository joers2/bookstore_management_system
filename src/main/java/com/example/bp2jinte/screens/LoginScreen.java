package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.LoginController;
import com.example.bp2jinte.models.Gebruiker;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginScreen {

    // Bouwt en retourneert het inlogscherm als een Scene.
    public Scene getScene() {
        // Titel van het loginvenster
        Label title = new Label("Bookstore Management System");
        title.getStyleClass().add("title-label");

        // Invoervelden voor gebruikersnaam en wachtwoord
        TextField usernameField = new TextField();
        usernameField.setPromptText("Gebruikersnaam");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Wachtwoord");

        // Knoppen voor inloggen en registreren
        Button loginButton = new Button("Inloggen");
        loginButton.getStyleClass().add("login-button");

        Button registerButton = new Button("Registreren");
        registerButton.getStyleClass().add("login-button");

        // Plaats knoppen naast elkaar
        HBox buttonRow = new HBox(10, loginButton, registerButton);
        buttonRow.setAlignment(Pos.CENTER);

        // Formulier layout
        VBox form = new VBox(12, usernameField, passwordField, buttonRow);
        form.setAlignment(Pos.CENTER);
        form.getStyleClass().add("login-box"); // CSS-stijl voor blauwe achtergrond met afgeronde randen

        // Hoofdstructuur van het loginvenster
        VBox layout = new VBox(60, title, form);
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(layout);
        root.getStyleClass().add("root");

        // Scene aanmaken
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/loginscreen.css").toExternalForm()
        );

        // Login knop logica
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Controleer of velden niet leeg zijn
            if (username.isEmpty() || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vul zowel gebruikersnaam als wachtwoord in.").show();
                return;
            }

            // Maak controller aan en probeer in te loggen
            LoginController controller = new LoginController();
            Gebruiker gebruiker = controller.handleLogin(username, password);

            // Als login lukt → sla gebruiker op en ga naar HomeScreen
            if (gebruiker != null) {
                BookstoreSystem.loggedInUser = gebruiker; // Globale opslag van ingelogde gebruiker
                BookstoreSystem.mainStage.setScene(new HomeScreen().getScene()); // Navigeer naar Home
            } else {
                // Login mislukt → foutmelding
                new Alert(Alert.AlertType.ERROR, "Ongeldige gebruikersnaam of wachtwoord!").show();
            }
        });

        // Registreer knop logica
        registerButton.setOnAction(e -> {
            // Navigeer naar registratiepagina
            BookstoreSystem.mainStage.setScene(new RegistrerenScreen().getScene());
        });

        // Geef de Scene terug aan BookstoreSystem (wordt getoond in mainStage)
        return scene;
    }
}
