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

import java.util.List;
import java.util.function.Consumer;

public class BoekSelectieScreen extends Stage {

    // Deze popup toont een lijst met boeken op basis van een zoekterm.
    // Wanneer een boek wordt geselecteerd en bevestigd, wordt het doorgegeven via de 'onSelect'-callback.
    public BoekSelectieScreen(String term, Consumer<BoekController> onSelect) {
        setTitle("Selecteer een boek");                 // Titel van het popupvenster
        initModality(Modality.APPLICATION_MODAL);       // Zorgt dat dit venster modaal is (blokkeert onderliggend scherm)

        // Titel label
        Label title = new Label("Resultaten voor: " + term);

        // Lijst met zoekresultaten
        ListView<String> listView = new ListView<>();
        listView.setPrefHeight(300);
        listView.setPrefWidth(500);

        // Zoek boeken op basis van de ingevoerde term
        List<BoekController> boeken = BoekController.zoekBoeken(term);

        // Als er geen resultaten zijn, geef melding
        if (boeken.isEmpty()) {
            listView.getItems().add("Geen boeken gevonden.");
        } else {
            // Voeg elk gevonden boek toe aan de lijst
            for (BoekController boek : boeken) {
                listView.getItems().add(String.format(
                        "%s | %s | %s | Voorraad: %d",
                        boek.getISBN(), boek.getTitel(), boek.getAuteur(), boek.getVoorraad()
                ));
            }
        }

        // Knoppen
        Button selecteer = new Button("Selecteer");
        Button annuleren = new Button("Annuleren");

        selecteer.getStyleClass().add("button");
        annuleren.getStyleClass().add("button");

        // Selecteer-knop logica
        selecteer.setOnAction(e -> {
            // Haal de index van het geselecteerde item op
            int index = listView.getSelectionModel().getSelectedIndex();

            // Controleer of er een selectie is
            if (index < 0 || boeken.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Selecteer eerst een boek.").show();
                return;
            }

            // Haal het gekozen BoekController-object op
            BoekController gekozen = boeken.get(index);

            // Geef het geselecteerde boek terug aan de aanroepende code via de callback
            onSelect.accept(gekozen);

            // Sluit popup
            close();
        });

        // Annuleren-knop logica
        annuleren.setOnAction(e -> close());

        // Layout samenstellen
        VBox layout = new VBox(15, title, listView, new VBox(10, selecteer, annuleren));
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("klant-container"); // Hergebruik CSS-stijl voor consistentie

        // Scene aanmaken
        Scene scene = new Scene(layout, 600, 500);

        // Voeg stylesheet toe voor dezelfde look als andere schermen
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );

        setScene(scene);
        show(); // Toon popup direct
    }
}
