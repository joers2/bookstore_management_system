package com.example.bp2jinte.screens;

import com.example.bp2jinte.controllers.KlantController;
import com.example.bp2jinte.models.Klant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

public class KlantSelectiePopup extends Stage {

    // Deze popup laat de gebruiker een klant selecteren uit de database.
    // De geselecteerde klant wordt teruggegeven via de 'onSelect'-callback.
    public KlantSelectiePopup(Consumer<Klant> onSelect) {
        setTitle("Selecteer Klant");                  // Titel van het popupvenster
        initModality(Modality.APPLICATION_MODAL);     // Maak het venster modaal (blokkeert andere schermen)

        // Invoerveld + knop
        TextField zoekveld = new TextField();
        zoekveld.setPromptText("Zoek klant...");      // Placeholdertekst

        Button zoeken = new Button("Zoeken");         // Start de zoekactie
        ListView<String> results = new ListView<>();  // Lijst met zoekresultaten

        // Layout
        VBox layout = new VBox(15, zoekveld, zoeken, results);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Zoek logica
        zoeken.setOnAction(e -> {
            results.getItems().clear(); // Maak de lijst leeg voordat je nieuwe resultaten toont

            // Vraag klanten op via controller
            List<Klant> klanten = KlantController.zoekKlanten(zoekveld.getText().trim());

            // Toon melding als er geen resultaten zijn
            if (klanten.isEmpty()) {
                results.getItems().add("Geen klanten gevonden.");
            } else {
                // Voeg elke klant toe aan de lijst met ID en naam
                for (Klant k : klanten) {
                    results.getItems().add(k.getKlantnr() + " | " + k.getVoornaam() + " " + k.getAchternaam());
                }
            }
        });

        // Selectie logica
        results.setOnMouseClicked(e -> {
            String selected = results.getSelectionModel().getSelectedItem();

            // Controleer of een geldige klant is geselecteerd
            if (selected == null || selected.startsWith("❌")) return;

            // Haal klantnummer uit de geselecteerde regel
            int klantnr = Integer.parseInt(selected.split("\\|")[0].trim());

            // Haal volledige klantgegevens op uit database
            Klant klant = KlantController.getKlantById(klantnr);

            // Als klant bestaat → geef door aan callback en sluit popup
            if (klant != null) {
                onSelect.accept(klant);
                close();
            }
        });

        // Scene + opmaak
        Scene scene = new Scene(layout, 400, 400);
        setScene(scene);
        show(); // Toon popup direct
    }
}
