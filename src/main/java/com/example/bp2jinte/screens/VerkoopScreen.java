package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.TransactieController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class VerkoopScreen extends NavBarScreen {

    public VerkoopScreen() {
        super("Verkoop"); // Highlight "Verkoop" in navbar

        // Titel
        Label title = new Label("Verkoopregistratie");
        title.getStyleClass().add("page-title");

        // Invoervelden
        TextField klantField = new TextField();
        klantField.setPromptText("Klant selecteren...");
        klantField.setEditable(false); // alleen via popup invullen

        TextField boekField = new TextField();
        boekField.setPromptText("Boek selecteren...");
        boekField.setEditable(false);

        TextField prijsField = new TextField();
        prijsField.setPromptText("Verkoopprijs (€)");

        // Knoppen
        Button selectKlant = new Button("Selecteer Klant");
        Button selectBoek = new Button("Selecteer Boek");
        Button toevoegen = new Button("Transactie toevoegen");

        // Grid layout
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Klant:"), 0, 0);
        grid.add(klantField, 1, 0);
        grid.add(selectKlant, 2, 0);

        grid.add(new Label("Boek:"), 0, 1);
        grid.add(boekField, 1, 1);
        grid.add(selectBoek, 2, 1);

        grid.add(new Label("Verkoopprijs:"), 0, 2);
        grid.add(prijsField, 1, 2);
        grid.add(toevoegen, 2, 2);

        // Hoofdcontainer (blauwe box-stijl)
        VBox container = new VBox(30, title, grid);
        container.getStyleClass().add("verkoop-container");
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));

        root.setCenter(container);

        // Gekozen klant en boek bijhouden
        final int[] selectedKlantnr = {-1};   // -1 = nog geen klant geselecteerd
        final String[] selectedISBN = {null}; // null = nog geen boek geselecteerd

        // Kies klant via popup
        selectKlant.setOnAction(e ->
                new KlantSelectiePopup(klant -> {
                    selectedKlantnr[0] = klant.getKlantnr();
                    klantField.setText(klant.getVoornaam() + " " + klant.getAchternaam());
                })
        );

        // Kies boek via popup
        selectBoek.setOnAction(e ->
                new BoekSelectiePopup(boek -> {
                    selectedISBN[0] = boek.getISBN();
                    boekField.setText(boek.getTitel() + " (" + boek.getAuteur() + ")");
                    prijsField.setText(String.valueOf(boek.getVerkoopprijs()));
                })
        );

        // Transactie toevoegen
        toevoegen.setOnAction(e -> {
            // Controleer of klant en boek gekozen zijn
            if (selectedKlantnr[0] == -1 || selectedISBN[0] == null) {
                new Alert(Alert.AlertType.WARNING, "Selecteer eerst een klant en boek.").show();
                return;
            }

            try {
                double prijs = Double.parseDouble(prijsField.getText().trim());

                // Voeg nieuwe transactie toe via controller
                boolean success = TransactieController.addTransactie(selectedKlantnr[0], selectedISBN[0], prijs);

                if (success) {
                    new Alert(Alert.AlertType.INFORMATION, "Transactie succesvol toegevoegd!").show();

                    // Reset velden
                    klantField.clear();
                    boekField.clear();
                    prijsField.clear();
                    selectedKlantnr[0] = -1;
                    selectedISBN[0] = null;

                } else {
                    new Alert(Alert.AlertType.ERROR, "Fout bij toevoegen transactie.").show();
                }

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Verkoopprijs moet een getal zijn.").show();
            }
        });
    }

    // Scene ophalen met stylesheet
    @Override
    public Scene getScene() {
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );
        return scene;
    }
}
