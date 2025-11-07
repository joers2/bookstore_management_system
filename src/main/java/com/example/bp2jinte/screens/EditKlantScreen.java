package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.KlantController;
import com.example.bp2jinte.models.Klant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class EditKlantScreen extends Stage {

    // Opent een popup waarmee een bestaande klant bewerkt kan worden.
    // 'klantnr' bepaalt welke klant geladen wordt.
    // 'refreshCallback' wordt uitgevoerd om de klantlijst of zoekresultaten te vernieuwen.
    public EditKlantScreen(int klantnr, Runnable refreshCallback) {
        setTitle("Klant aanpassen");
        initModality(Modality.APPLICATION_MODAL); // Maak dit venster modaal (blokkeert onderliggende schermen)

        // Invoervelden
        TextField voornaam = new TextField();
        TextField achternaam = new TextField();
        TextField adres = new TextField();
        TextField postcode = new TextField();
        TextField email = new TextField();
        TextField telefoon = new TextField();

        // Placeholder-tekst voor duidelijkheid
        voornaam.setPromptText("Voornaam");
        achternaam.setPromptText("Achternaam");
        adres.setPromptText("Adres");
        postcode.setPromptText("Postcode");
        email.setPromptText("E-mailadres");
        telefoon.setPromptText("Telefoonnummer");

        // Klantgegevens laden uit database
        try {
            Connection conn = BookstoreSystem.db.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Klanten WHERE Klantnr = ?");
            stmt.setInt(1, klantnr);
            ResultSet rs = stmt.executeQuery();

            // Als de klant bestaat → velden invullen
            if (rs.next()) {
                voornaam.setText(rs.getString("Voornaam"));
                achternaam.setText(rs.getString("Achternaam"));
                adres.setText(rs.getString("Adres"));
                postcode.setText(rs.getString("Postcode"));
                email.setText(rs.getString("Email"));
                telefoon.setText(rs.getString("Telefoonnr"));
            } else {
                // Geen klant gevonden → melding en popup sluiten
                new Alert(Alert.AlertType.ERROR, "Klant niet gevonden in database.").show();
                close();
                return;
            }
        } catch (SQLException e) {
            // Foutmelding bij databaseprobleem
            new Alert(Alert.AlertType.ERROR, "Fout bij laden klantgegevens: " + e.getMessage()).show();
            close();
            return;
        }

        // Knoppen
        Button opslaan = new Button("Opslaan");
        Button annuleren = new Button("Annuleren");

        opslaan.getStyleClass().add("button");
        annuleren.getStyleClass().add("button");

        // Opslaan-logica
        opslaan.setOnAction(e -> {
            // Controleer of alle velden zijn ingevuld
            if (voornaam.getText().isEmpty() || achternaam.getText().isEmpty() ||
                    adres.getText().isEmpty() || postcode.getText().isEmpty() ||
                    email.getText().isEmpty() || telefoon.getText().isEmpty()) {

                new Alert(Alert.AlertType.WARNING, "Vul alle velden in.").show();
                return;
            }

            // Maak een nieuw Klant-object met de aangepaste waarden
            Klant updated = new Klant(
                    voornaam.getText(),
                    achternaam.getText(),
                    adres.getText(),
                    postcode.getText(),
                    email.getText(),
                    telefoon.getText()
            );

            // Probeer klant te updaten in database via KlantController
            if (KlantController.updateKlant(klantnr, updated)) {
                new Alert(Alert.AlertType.INFORMATION, "Klant succesvol aangepast!").show();

                // Roep de refreshCallback aan om scherm te vernieuwen
                refreshCallback.run();

                // Sluit popup
                close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fout bij updaten klant.").show();
            }
        });

        // Annuleren-logica
        annuleren.setOnAction(e -> close());

        // Layout
        VBox layout = new VBox(12,
                new Label("Voornaam:"), voornaam,
                new Label("Achternaam:"), achternaam,
                new Label("Adres:"), adres,
                new Label("Postcode:"), postcode,
                new Label("E-mail:"), email,
                new Label("Telefoonnummer:"), telefoon,
                new VBox(10, opslaan, annuleren) // Knoppen onderaan
        );

        // CSS en opmaak
        layout.getStyleClass().add("klant-container");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Scene ophalen
        Scene scene = new Scene(layout, 400, 600);
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );

        setScene(scene);
        show(); // Toon popup
    }
}
