package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.VoorraadController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class VoorraadScreen extends NavBarScreen {

    private TextField titelBoek;           // toont de titel van het geselecteerde boek
    private TextField winkelvoorraad;      // huidige voorraad in de winkel
    private TextField aantalTeBestellen;   // aantal exemplaren om bij te bestellen
    private String selectedISBN;           // ISBN van het geselecteerde boek

    public VoorraadScreen() {
        super("Voorraadbeheer"); // Highlight "Voorraadbeheer" in de navigatiebalk

        // Titel
        Label title = new Label("Voorraadbeheer");
        title.getStyleClass().add("page-title");

        // Zoekveld en knop
        TextField zoekveld = new TextField();
        zoekveld.setPromptText("Zoek op titel of auteur");

        Button zoeken = new Button("Zoeken");

        VBox searchBox = new VBox(15, zoekveld, zoeken);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        // Voorraaddetails
        titelBoek = new TextField();
        titelBoek.setPromptText("Titel boek");
        titelBoek.setEditable(false); // kan alleen via selectie ingevuld worden

        winkelvoorraad = new TextField();
        winkelvoorraad.setPromptText("Winkelvoorraad");
        winkelvoorraad.setEditable(false);

        aantalTeBestellen = new TextField();
        aantalTeBestellen.setPromptText("Aantal te bestellen (toe te voegen)");

        Button bestellen = new Button("Bestellen");

        // Subbox voor voorraadinformatie
        VBox stockBox = new VBox(15,
                titelBoek,
                winkelvoorraad,
                aantalTeBestellen,
                bestellen
        );
        stockBox.getStyleClass().add("inner-stock-box");
        stockBox.setAlignment(Pos.CENTER);
        stockBox.setPadding(new Insets(20));

        // Gridlayout voor zoek- en voorraadsecties
        GridPane grid = new GridPane();
        grid.setHgap(60);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.add(searchBox, 0, 0);
        grid.add(stockBox, 1, 0);

        // Hoofdcontainer (met blauwe randstijl)
        VBox voorraadContainer = new VBox(grid);
        voorraadContainer.getStyleClass().add("klant-container");
        voorraadContainer.setAlignment(Pos.CENTER);
        voorraadContainer.setPadding(new Insets(40));

        VBox centerContent = new VBox(30, title, voorraadContainer);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40));

        root.setCenter(centerContent);

        // Knop logica

        // Zoek op boek en open selectiescherm
        zoeken.setOnAction(e -> {
            String term = zoekveld.getText().trim();
            if (term.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Voer een zoekterm in.").show();
                return;
            }

            // Open selectie-popup met resultaten
            new BoekSelectieScreen(term, boek -> {
                selectedISBN = boek.getISBN();
                titelBoek.setText(boek.getTitel());

                // Laad actuele voorraad uit database
                VoorraadController voorraad = VoorraadController.getVoorraadByISBN(selectedISBN);
                if (voorraad != null) {
                    winkelvoorraad.setText(String.valueOf(voorraad.getWinkelVoorraad()));
                    aantalTeBestellen.setText("0"); // standaardwaarde
                } else {
                    winkelvoorraad.setText("0");
                    aantalTeBestellen.setText("0");
                }
            });
        });

        // Bestellen — verhoog "Aantal te bestellen" in de database
        bestellen.setOnAction(e -> {
            if (selectedISBN == null) {
                new Alert(Alert.AlertType.WARNING, "Selecteer eerst een boek.").show();
                return;
            }

            String aantalStr = aantalTeBestellen.getText().trim();
            if (aantalStr.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Voer een aantal in.").show();
                return;
            }

            try {
                int aantal = Integer.parseInt(aantalStr);
                if (aantal <= 0) {
                    new Alert(Alert.AlertType.WARNING, "Aantal moet groter zijn dan 0.").show();
                    return;
                }

                // Werk voorraad bij via controller
                boolean success = VoorraadController.voegToeAanBestelling(selectedISBN, aantal);

                if (success) {
                    new Alert(Alert.AlertType.INFORMATION,
                            "✅ " + aantal + " toegevoegd aan bestelling!").show();

                    // Refresh voorraadweergave
                    VoorraadController voorraad = VoorraadController.getVoorraadByISBN(selectedISBN);
                    if (voorraad != null) {
                        winkelvoorraad.setText(String.valueOf(voorraad.getWinkelVoorraad()));
                        aantalTeBestellen.setText("0"); // reset invoerveld
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fout bij bijwerken van bestelling.").show();
                }

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Aantal moet een geldig getal zijn.").show();
            }
        });
    }

    // Scene ophalen met toegepaste stijlen
    @Override
    public Scene getScene() {
        Scene scene = new Scene(
                root,
                BookstoreSystem.applicationSize[0],
                BookstoreSystem.applicationSize[1]
        );

        scene.getStylesheets().add(
                BookstoreSystem.class.getResource(
                        "/com/example/bp2jinte/stylesheets/fullapp.css"
                ).toExternalForm()
        );
        return scene;
    }
}
