package com.example.bp2jinte.screens;

import com.example.bp2jinte.controllers.BoekController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

public class BoekSelectiePopup extends Stage {

    // Deze popup laat de gebruiker een boek selecteren uit de database.
    // Zodra een boek wordt aangeklikt, wordt het teruggegeven via de 'onSelect' callback.
    public BoekSelectiePopup(Consumer<BoekController> onSelect) {
        setTitle("Selecteer Boek");                     // Titel van het venster
        initModality(Modality.APPLICATION_MODAL);       // Blokkeert andere vensters tot popup gesloten is

        // Zoekveld + knop
        TextField zoekveld = new TextField();
        zoekveld.setPromptText("Zoek boek...");

        Button zoeken = new Button("Zoeken");

        // Resultatenlijst
        ListView<String> results = new ListView<>();

        // Layout voor de popup
        VBox layout = new VBox(15, zoekveld, zoeken, results);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Zoek knop actie
        zoeken.setOnAction(e -> {
            results.getItems().clear(); // Maak vorige resultaten leeg

            // Haal boeken op op basis van de ingevoerde zoekterm
            List<BoekController> boeken = BoekController.zoekBoeken(zoekveld.getText().trim());

            // Toon melding als er geen resultaten zijn
            if (boeken.isEmpty()) {
                results.getItems().add("Geen boeken gevonden.");
            } else {
                // Voeg elk gevonden boek toe aan de lijst
                for (BoekController b : boeken) {
                    results.getItems().add(b.getISBN() + " | " + b.getTitel() + " (" + b.getAuteur() + ")");
                }
            }
        });

        // Gebruiker klikt op een resultaat
        results.setOnMouseClicked(e -> {
            String selected = results.getSelectionModel().getSelectedItem();

            // Controleer of iets is geselecteerd
            if (selected == null || selected.startsWith("❌")) return;

            // Haal het ISBN uit de geselecteerde regel (staat voor de eerste '|')
            String isbn = selected.split("\\|")[0].trim();

            // Zoek het bijbehorende BoekController-object op
            for (BoekController b : BoekController.zoekBoeken(isbn)) {
                if (b.getISBN().equals(isbn)) {
                    // Roep callback aan met het geselecteerde boek
                    onSelect.accept(b);
                    close(); // Sluit popup
                    break;
                }
            }
        });

        // Maak de scene aan en toon popup
        Scene scene = new Scene(layout, 400, 400);
        setScene(scene);
        show();
    }
}
