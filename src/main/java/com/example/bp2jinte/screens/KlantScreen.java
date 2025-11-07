package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.KlantController;
import com.example.bp2jinte.models.Klant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class KlantScreen extends NavBarScreen {

    // Dit scherm wordt gebruikt om klanten toe te voegen en te zoeken in de database.
    public KlantScreen() {
        super("Klantgegevens"); // Highlight 'Klantgegevens' in de navigatiebalk

        // Titel
        Label title = new Label("Klantgegevens");
        title.getStyleClass().add("page-title");

        // Invoervelden
        TextField voornaam = new TextField();
        voornaam.setPromptText("Voornaam");

        TextField achternaam = new TextField();
        achternaam.setPromptText("Achternaam");

        TextField adres = new TextField();
        adres.setPromptText("Adres");

        TextField postcode = new TextField();
        postcode.setPromptText("Postcode");

        TextField email = new TextField();
        email.setPromptText("E-mailadres");

        TextField telefoon = new TextField();
        telefoon.setPromptText("Telefoonnummer");

        TextField zoekveld = new TextField();
        zoekveld.setPromptText("Zoeken");

        // Knoppen
        Button zoeken = new Button("Zoeken");
        Button toevoegen = new Button("Toevoegen");

        // Grid voor layout
        GridPane grid = new GridPane();
        grid.setHgap(30); // horizontale afstand tussen kolommen
        grid.setVgap(20); // verticale afstand tussen rijen
        grid.setAlignment(Pos.CENTER);

        // Voeg velden en knoppen toe aan het grid (2 kolommen)
        grid.add(voornaam, 0, 0);
        grid.add(zoekveld, 1, 0);
        grid.add(achternaam, 0, 1);
        grid.add(zoeken, 1, 1);
        grid.add(adres, 0, 2);
        grid.add(postcode, 0, 3);
        grid.add(toevoegen, 1, 3);
        grid.add(email, 0, 4);
        grid.add(telefoon, 0, 5);

        // Container opbouw
        VBox klantContainer = new VBox(grid);
        klantContainer.getStyleClass().add("klant-container"); // Hergebruik bestaande blauwe styling
        klantContainer.setAlignment(Pos.CENTER);
        klantContainer.setPadding(new Insets(40));

        VBox centerContent = new VBox(30, title, klantContainer);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40));

        root.setCenter(centerContent); // Plaats in het midden van het scherm

        // Toevoegen-logica
        toevoegen.setOnAction(e -> {
            // Lees de waarden van de tekstvelden
            String vnaam = voornaam.getText().trim();
            String anaam = achternaam.getText().trim();
            String adr = adres.getText().trim();
            String pc = postcode.getText().trim();
            String mail = email.getText().trim();
            String tel = telefoon.getText().trim();

            // Controleer of alle velden ingevuld zijn
            if (vnaam.isEmpty() || anaam.isEmpty() || adr.isEmpty() ||
                    pc.isEmpty() || mail.isEmpty() || tel.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vul alle velden in voordat je een klant toevoegt.").show();
                return;
            }

            // --- E-mailadres validatie ---
            if (!mail.contains("@") || !mail.contains(".")) {
                new Alert(Alert.AlertType.WARNING, "Voer een geldig e-mailadres in.").show();
                return;
            }

            // Maak een nieuw klantobject aan
            Klant nieuweKlant = new Klant(vnaam, anaam, adr, pc, mail, tel);

            // Voeg klant toe via de controller
            boolean success = KlantController.addKlant(nieuweKlant);
            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "Klant toegevoegd!").show();

                // Maak de invoervelden leeg
                voornaam.clear();
                achternaam.clear();
                adres.clear();
                postcode.clear();
                email.clear();
                telefoon.clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fout bij toevoegen klant!").show();
            }
        });

        // Zoek logica
        zoeken.setOnAction(e -> {
            String term = zoekveld.getText().trim();

            if (term.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vul iets in om te zoeken.").show();
            } else {
                // Navigeer naar het zoekresultatenscherm met de ingevoerde term
                BookstoreSystem.navigateTo(new KlantenZoekScreen(term).getScene());
            }
        });
    }

    // Scene ophalen met stylesheet
    @Override
    public Scene getScene() {
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);

        // Voeg de stylesheet toe voor consistent uiterlijk
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );

        return scene;
    }
}
