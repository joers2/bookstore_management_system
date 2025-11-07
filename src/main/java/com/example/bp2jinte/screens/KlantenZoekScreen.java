package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.KlantController;
import com.example.bp2jinte.models.Klant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class KlantenZoekScreen extends NavBarScreen {
    private final String searchTerm;                 // De zoekterm die de gebruiker heeft ingevoerd
    private final ListView<String> resultsList = new ListView<>(); // Lijst om de zoekresultaten te tonen

    // Toont zoekresultaten van klanten op basis van een ingevoerde zoekterm.
    // Gebruiker kan vanuit dit scherm klanten aanpassen of verwijderen.
    public KlantenZoekScreen(String searchTerm) {
        super("Zoekresultaten");
        this.searchTerm = searchTerm;

        // Titel van de pagina
        Label title = new Label("Zoekresultaten voor: " + searchTerm);
        title.getStyleClass().add("page-title");

        // Lijst met resultaten
        resultsList.setPrefHeight(350);
        resultsList.setPrefWidth(650);
        resultsList.getStyleClass().add("list-view");

        // Actieknoppen
        Button aanpassen = new Button("Aanpassen");
        Button verwijderen = new Button("Verwijderen");
        Button terug = new Button("Terug");

        // Stijlknoppen met CSS
        aanpassen.getStyleClass().add("button");
        verwijderen.getStyleClass().add("button");
        terug.getStyleClass().add("button");

        // Plaats knoppen verticaal onder elkaar
        VBox buttonBox = new VBox(10, aanpassen, verwijderen, terug);
        buttonBox.setAlignment(Pos.CENTER);

        // Container met lijst en knoppen
        VBox klantContainer = new VBox(20, resultsList, buttonBox);
        klantContainer.getStyleClass().add("klant-container"); // Hergebruik van bestaande blauwe containerstijl
        klantContainer.setAlignment(Pos.CENTER);
        klantContainer.setPadding(new Insets(40));

        // Algemene layout
        VBox layout = new VBox(30, title, klantContainer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        root.setCenter(layout); // Plaats alles in het midden van het hoofdscherm

        // Data laden
        loadResults();

        // Knoppenfunctionaliteit
        terug.setOnAction(e -> BookstoreSystem.navigateTo(new KlantScreen().getScene()));
        verwijderen.setOnAction(e -> deleteSelectedKlant());
        aanpassen.setOnAction(e -> editSelectedKlant());
    }

    // Klanten ophalen en tonen in de lijst
    private void loadResults() {
        resultsList.getItems().clear(); // Maak de lijst leeg voor nieuwe resultaten

        // Haal klanten op via de controller
        List<Klant> klanten = KlantController.zoekKlanten(searchTerm);

        // Toon melding als er niets gevonden is
        if (klanten.isEmpty()) {
            resultsList.getItems().add("Geen klanten gevonden voor: " + searchTerm);
            return;
        }

        // Voeg elke klant als string toe aan de lijst
        for (Klant k : klanten) {
            resultsList.getItems().add(k.toString()); // Gebruik toString() van Klant-model
        }
    }

    // Klant verwijderen op basis van selectie in de lijst
    private void deleteSelectedKlant() {
        String selected = resultsList.getSelectionModel().getSelectedItem();

        // Controleer of er een klant geselecteerd is
        if (selected == null || selected.startsWith("❌")) {
            new Alert(Alert.AlertType.WARNING, "Selecteer eerst een klant.").show();
            return;
        }

        // Haal klantnummer uit het geselecteerde item (eerste veld vóór de |)
        int klantnr = Integer.parseInt(selected.split("\\|")[0].trim());

        // Probeer klant te verwijderen via controller
        if (KlantController.deleteKlant(klantnr)) {
            new Alert(Alert.AlertType.INFORMATION, "Klant verwijderd!").show();
            loadResults(); // Herlaad lijst om wijziging te tonen
        } else {
            new Alert(Alert.AlertType.ERROR, "Fout bij verwijderen klant.").show();
        }
    }

    // Klant aanpassen op basis van selectie in de lijst
    private void editSelectedKlant() {
        String selected = resultsList.getSelectionModel().getSelectedItem();

        // Controleer of er een klant geselecteerd is
        if (selected == null || selected.startsWith("❌")) {
            new Alert(Alert.AlertType.WARNING, "Selecteer eerst een klant.").show();
            return;
        }

        // Haal klantnummer uit de geselecteerde regel
        int klantnr = Integer.parseInt(selected.split("\\|")[0].trim());

        // Open het EditKlantScreen voor bewerking
        new EditKlantScreen(klantnr, this::loadResults);
    }

    // Scene ophalen met toegevoegde CSS-stijl
    @Override
    public Scene getScene() {
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);

        // Voeg CSS-stijl toe voor consistent uiterlijk
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );

        return scene;
    }
}
