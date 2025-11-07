package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.BoekController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditBoekScreen extends Stage {

    // Opent een popupvenster waarmee een bestaand boek aangepast kan worden.
    // Het 'isbn' bepaalt welk boek wordt geladen.
    // De 'refreshCallback' wordt aangeroepen om het vorige scherm te verversen na opslaan.
    public EditBoekScreen(String isbn, Runnable refreshCallback) {
        setTitle("Boek aanpassen");
        initModality(Modality.APPLICATION_MODAL); // Maak dit venster modaal (blokkeert andere schermen)

        // Invoervelden
        TextField titel = new TextField();
        TextField auteur = new TextField();
        TextField uitgever = new TextField();
        TextField paginas = new TextField();
        TextField verkoopprijs = new TextField();
        TextField voorraad = new TextField();

        // Voeg placeholder-tekst toe voor duidelijkheid
        titel.setPromptText("Titel");
        auteur.setPromptText("Auteur");
        uitgever.setPromptText("Uitgever");
        paginas.setPromptText("Pagina’s");
        verkoopprijs.setPromptText("Verkoopprijs");
        voorraad.setPromptText("Voorraad");

        // Bestaande boekgegevens laden vanuit database
        try {
            Connection conn = BookstoreSystem.db.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Boeken WHERE ISBN = ?");
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            // Als het boek bestaat → vul de velden in
            if (rs.next()) {
                titel.setText(rs.getString("Titel"));
                auteur.setText(rs.getString("Auteur"));
                uitgever.setText(rs.getString("Uitgever"));
                paginas.setText(String.valueOf(rs.getInt("Paginas")));
                verkoopprijs.setText(String.valueOf(rs.getDouble("Verkoopprijs")));
                voorraad.setText(String.valueOf(rs.getInt("Voorraad")));
            } else {
                // Geen boek gevonden → foutmelding en sluit popup
                new Alert(Alert.AlertType.ERROR, "Boek niet gevonden in database.").show();
                close();
                return;
            }
        } catch (SQLException e) {
            // Fout bij het laden van boekgegevens
            new Alert(Alert.AlertType.ERROR, "Fout bij laden boekgegevens: " + e.getMessage()).show();
            close();
            return;
        }

        // Knoppen
        Button opslaan = new Button("Opslaan");
        Button annuleren = new Button("Annuleren");

        // Stijlen toepassen
        opslaan.getStyleClass().add("button");
        annuleren.getStyleClass().add("button");

        // Opslaan-logica
        opslaan.setOnAction(e -> {
            // Controleer of alle velden ingevuld zijn
            if (titel.getText().isEmpty() || auteur.getText().isEmpty() ||
                    uitgever.getText().isEmpty() || paginas.getText().isEmpty() ||
                    verkoopprijs.getText().isEmpty() || voorraad.getText().isEmpty()) {

                new Alert(Alert.AlertType.WARNING, "Vul alle velden in.").show();
                return;
            }

            try {
                // Converteer numerieke waarden
                int paginasInt = Integer.parseInt(paginas.getText().trim());
                double prijsDouble = Double.parseDouble(verkoopprijs.getText().trim());
                int voorraadInt = Integer.parseInt(voorraad.getText().trim());

                // Maak een bijgewerkt BoekController-object
                BoekController updated = new BoekController(
                        isbn,
                        titel.getText().trim(),
                        auteur.getText().trim(),
                        uitgever.getText().trim(),
                        paginasInt,
                        prijsDouble,
                        voorraadInt
                );

                // Probeer update uit te voeren
                if (BoekController.updateBoek(isbn, updated)) {
                    new Alert(Alert.AlertType.INFORMATION, "Boek succesvol aangepast!").show();

                    // Vernieuw het vorige scherm (zoals BoekenZoekScreen)
                    refreshCallback.run();

                    // Sluit popup
                    close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fout bij updaten boek.").show();
                }

            } catch (NumberFormatException ex) {
                // Foutmelding bij ongeldige numerieke invoer
                new Alert(Alert.AlertType.WARNING, "Pagina’s, prijs en voorraad moeten numeriek zijn.").show();
            }
        });

        // Annuleren-logica
        annuleren.setOnAction(e -> close());

        // Layout
        VBox layout = new VBox(12,
                new Label("Titel:"), titel,
                new Label("Auteur:"), auteur,
                new Label("Uitgever:"), uitgever,
                new Label("Pagina’s:"), paginas,
                new Label("Verkoopprijs:"), verkoopprijs,
                new Label("Voorraad:"), voorraad,
                new VBox(10, opslaan, annuleren) // Knoppen onderaan
        );

        // CSS + styling consistent houden met andere schermen
        layout.getStyleClass().add("klant-container");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Scene aanmaken
        Scene scene = new Scene(layout, 400, 600);
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );

        // Toon het venster
        setScene(scene);
        show();
    }
}
