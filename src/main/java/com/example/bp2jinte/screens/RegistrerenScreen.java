package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrerenScreen {

    // Bouwt en retourneert het registratiescherm
    public Scene getScene() {
        // Titel
        Label title = new Label("Registreren");
        title.getStyleClass().add("title-label");

        // Invoervelden
        TextField voornaamField = new TextField();
        voornaamField.setPromptText("Voornaam");
        voornaamField.getStyleClass().add("text-field");

        TextField achternaamField = new TextField();
        achternaamField.setPromptText("Achternaam");
        achternaamField.getStyleClass().add("text-field");

        TextField gebruikersnaamField = new TextField();
        gebruikersnaamField.setPromptText("Gebruikersnaam");
        gebruikersnaamField.getStyleClass().add("text-field");

        PasswordField wachtwoordField = new PasswordField();
        wachtwoordField.setPromptText("Wachtwoord");
        wachtwoordField.getStyleClass().add("password-field");

        // Knoppen
        Button registerButton = new Button("Registreren");
        registerButton.getStyleClass().add("login-button");

        Button backButton = new Button("Terug");
        backButton.getStyleClass().add("login-button");

        // Knoppen naast elkaar
        HBox buttonRow = new HBox(12, registerButton, backButton);
        buttonRow.setAlignment(Pos.CENTER);

        // Formulier layout
        VBox form = new VBox(15,
                voornaamField,
                achternaamField,
                gebruikersnaamField,
                wachtwoordField,
                buttonRow
        );
        form.setAlignment(Pos.CENTER);
        form.getStyleClass().add("register-box"); // Stijl voor registratiebox

        // Hoofdlayout
        VBox layout = new VBox(50, title, form);
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(layout);
        root.getStyleClass().add("root"); // Achtergrondstijl

        // Validatie-helper voor styling van velden
        class Validator {
            void setInvalid(TextInputControl c, boolean invalid) {
                if (invalid) {
                    if (!c.getStyleClass().contains("invalid")) c.getStyleClass().add("invalid");
                } else {
                    c.getStyleClass().remove("invalid");
                }
            }
        }
        Validator validator = new Validator();

        // Registratieknop actie
        registerButton.setOnAction(e -> {
            boolean ok = true;

            // Lees waarden
            String voornaam = voornaamField.getText().trim();
            String achternaam = achternaamField.getText().trim();
            String gebruikersnaam = gebruikersnaamField.getText().trim();
            String wachtwoord = wachtwoordField.getText().trim();

            // Reset validatie-styling
            validator.setInvalid(voornaamField, false);
            validator.setInvalid(achternaamField, false);
            validator.setInvalid(gebruikersnaamField, false);
            validator.setInvalid(wachtwoordField, false);

            // Basisvalidatie (verplicht + minimale lengte)
            if (voornaam.isEmpty()) { validator.setInvalid(voornaamField, true); ok = false; }
            if (achternaam.isEmpty()) { validator.setInvalid(achternaamField, true); ok = false; }
            if (gebruikersnaam.isEmpty() || gebruikersnaam.length() < 3) {
                validator.setInvalid(gebruikersnaamField, true); ok = false;
            }
            if (wachtwoord.isEmpty() || wachtwoord.length() < 4) {
                validator.setInvalid(wachtwoordField, true); ok = false;
            }

            // Toon melding bij fouten
            if (!ok) {
                new Alert(Alert.AlertType.ERROR,
                        "Controleer je invoer:\n• Alle velden zijn verplicht\n• Gebruikersnaam ≥ 3 tekens\n• Wachtwoord ≥ 4 tekens")
                        .show();
                return;
            }

            // Probeer gebruiker te registreren
            boolean success = addGebruiker(voornaam, achternaam, gebruikersnaam, wachtwoord);

            if (success) {
                // Succesmelding en terug naar login
                new Alert(Alert.AlertType.INFORMATION, "Gebruiker succesvol geregistreerd!").showAndWait();
                BookstoreSystem.navigateTo(new LoginScreen().getScene());
            }
        });

        // Terugknop → terug naar login
        backButton.setOnAction(e -> BookstoreSystem.navigateTo(new LoginScreen().getScene()));

        // Scene + stylesheet
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/registrerenscreen.css").toExternalForm()
        );

        return scene;
    }

    // Gebruiker toevoegen aan database
    // Voegt een nieuwe gebruiker toe na controle op unieke gebruikersnaam.
    private boolean addGebruiker(String voornaam, String achternaam, String gebruikersnaam, String wachtwoord) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // 🔍 Controleer of gebruikersnaam al bestaat
            String checkSql = "SELECT * FROM Gebruiker WHERE Gebruikersnaam = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, gebruikersnaam);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                new Alert(Alert.AlertType.WARNING, "Gebruikersnaam bestaat al! Kies een andere.").show();
                return false;
            }

            // Insert nieuwe gebruiker
            String insertSql = "INSERT INTO Gebruiker (Voornaam, Achternaam, Gebruikersnaam, Wachtwoord) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, voornaam);
            insertStmt.setString(2, achternaam);
            insertStmt.setString(3, gebruikersnaam);
            insertStmt.setString(4, wachtwoord); // vervang door gehashte waarde in productie
            insertStmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Fout bij registratie: " + ex.getMessage()).show();
            ex.printStackTrace();
            return false;
        }
    }
}
