package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.BoekController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class BoekenZoekScreen extends NavBarScreen {

    // Belangrijke velden
    private final String searchTerm;           // De zoekterm die door de gebruiker is ingevoerd
    private final ListView<String> resultsList = new ListView<>(); // Lijst waarin resultaten getoond worden

    public BoekenZoekScreen(String searchTerm) {
        super("Zoekresultaten");   // Titel van het scherm (bovenaan via NavBar)
        this.searchTerm = searchTerm;

        // Titel bovenaan
        Label title = new Label("Zoekresultaten voor: " + searchTerm);
        title.getStyleClass().add("page-title");

        // Resultatenlijst instellen
        resultsList.setPrefHeight(350);
        resultsList.setPrefWidth(650);
        resultsList.getStyleClass().add("list-view");

        // Knoppen aanmaken
        Button aanpassen = new Button("Aanpassen");
        Button verwijderen = new Button("Verwijderen");
        Button terug = new Button("Terug");

        // Voeg stijlen toe
        aanpassen.getStyleClass().add("button");
        verwijderen.getStyleClass().add("button");
        terug.getStyleClass().add("button");

        // Plaats knoppen onder elkaar
        VBox buttonBox = new VBox(10, aanpassen, verwijderen, terug);
        buttonBox.setAlignment(Pos.CENTER);

        // Hoofdcontainer met lijst + knoppen
        VBox boekContainer = new VBox(20, resultsList, buttonBox);
        boekContainer.getStyleClass().add("klant-container"); // Hergebruik bestaande CSS-stijl
        boekContainer.setAlignment(Pos.CENTER);
        boekContainer.setPadding(new Insets(40));

        // Totale layout
        VBox layout = new VBox(30, title, boekContainer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        root.setCenter(layout);

        // Gegevens laden uit database
        loadResults();

        // Knop-acties
        terug.setOnAction(e -> BookstoreSystem.navigateTo(new BoekScreen().getScene())); // Terug naar hoofdscherm boeken
        verwijderen.setOnAction(e -> deleteSelectedBoek()); // Verwijder geselecteerd boek
        aanpassen.setOnAction(e -> editSelectedBoek());     // Pas geselecteerd boek aan
    }


    // Gegevens laden uit database en tonen in lijst
    private void loadResults() {
        resultsList.getItems().clear(); // Verwijder vorige resultaten

        // Haal lijst boeken op uit database op basis van zoekterm
        List<BoekController> boeken = BoekController.zoekBoeken(searchTerm);

        // Als er niets gevonden is, geef melding
        if (boeken.isEmpty()) {
            resultsList.getItems().add("Geen boeken gevonden voor: " + searchTerm);
            return;
        }

        // Voeg elk boek toe aan de lijst (met leesbare weergave)
        for (BoekController b : boeken) {
            // Weergegeven formaat: ISBN | Titel | Auteur | Voorraad
            resultsList.getItems().add(String.format("%s | Titel: %s | Auteur: %s | Voorraad: %d",
                    b.getISBN(), b.getTitel(), b.getAuteur(), b.getVoorraad()));
        }
    }

    // Gevonden boek verwijderen
    private void deleteSelectedBoek() {
        String selected = resultsList.getSelectionModel().getSelectedItem();

        // Controleer of gebruiker iets geselecteerd heeft
        if (selected == null || selected.startsWith("❌")) {
            new Alert(Alert.AlertType.WARNING, "Selecteer eerst een boek.").show();
            return;
        }

        // ISBN staat vooraan in de tekst, gescheiden door “|”
        String isbn = selected.split("\\|")[0].trim();

        // Probeer het boek te verwijderen
        if (BoekController.deleteBoek(isbn)) {
            new Alert(Alert.AlertType.INFORMATION, "Boek verwijderd!").show();
            loadResults(); // Lijst vernieuwen
        } else {
            new Alert(Alert.AlertType.ERROR, "Fout bij verwijderen boek.").show();
        }
    }

    // Boek bewerken
    private void editSelectedBoek() {
        String selected = resultsList.getSelectionModel().getSelectedItem();

        // Controleer of er iets geselecteerd is
        if (selected == null || selected.startsWith("❌")) {
            new Alert(Alert.AlertType.WARNING, "Selecteer eerst een boek.").show();
            return;
        }

        // ISBN halen uit de geselecteerde regel
        String isbn = selected.split("\\|")[0].trim();

        // Open een nieuw scherm om het boek te bewerken
        // De loadResults() wordt meegegeven als callback → lijst wordt automatisch herladen na aanpassen
        new EditBoekScreen(isbn, this::loadResults);
    }

    // Scene ophalen en style toepassen
    @Override
    public Scene getScene() {
        // Maak de Scene met de standaard app-afmetingen
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);

        // Voeg stylesheet toe voor consistente look
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );
        return scene;
    }
}
