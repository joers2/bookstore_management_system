package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class HomeScreen extends NavBarScreen {

    // Dit is het startscherm (dashboard) van de applicatie.
    // Van hieruit kan de gebruiker navigeren naar andere onderdelen zoals klanten, boeken, voorraad, transacties en verkoop.
    public HomeScreen() {
        super("Home"); // Highlight 'Home' in de navigatiebalk

        // Welkomstbericht aanpassen
        // Als er een gebruiker is ingelogd, toon persoonlijke begroeting.
        String titleText = "Home";
        if (BookstoreSystem.loggedInUser != null) {
            titleText = "Welkom " + BookstoreSystem.loggedInUser.getVoornaam() + " " + BookstoreSystem.loggedInUser.getAchternaam();
        }

        // Label voor de paginatitel
        Label title = new Label(titleText);
        title.getStyleClass().add("page-title");

        // Dashboard-inhoud
        // Gebruik een GridPane om de tegels netjes in een raster weer te geven
        GridPane contentGrid = new GridPane();
        contentGrid.setHgap(120);   // Horizontale ruimte tussen tegels
        contentGrid.setVgap(100);   // Verticale ruimte tussen tegels
        contentGrid.setAlignment(Pos.CENTER);

        // Maak de tegels aan
        Label klantenBox = createTile("Klantgegevens");
        Label boekenBox = createTile("Boekgegevens");
        Label voorraadBox = createTile("Voorraad");
        Label transactiesBox = createTile("Transacties");
        Label verkoopBox = createTile("Verkoop");

        // Voeg de tegels toe aan het raster (3x2 lay-out)
        contentGrid.add(klantenBox, 0, 0);
        contentGrid.add(boekenBox, 1, 0);
        contentGrid.add(voorraadBox, 0, 1);
        contentGrid.add(transactiesBox, 1, 1);
        contentGrid.add(verkoopBox, 0, 2);

        // Combineer titel en inhoud in een verticale box
        VBox center = new VBox(40, title, contentGrid);
        center.setAlignment(Pos.TOP_CENTER);
        center.setPadding(new Insets(40));

        // Plaats de inhoud in het midden van het hoofdscherm
        root.setCenter(center);
    }

    // Methode om een tegel te maken voor het dashboard
    // Deze methode maakt een klikbare tegel aan die navigeert naar het juiste scherm.
    private Label createTile(String text) {
        Label tile = new Label(text);
        tile.getStyleClass().add("tile"); // CSS-stijl voor uniforme weergave

        // Voeg klikgedrag toe aan de tegel
        tile.setOnMouseClicked(e -> {
            switch (text) {
                case "Klantgegevens" -> BookstoreSystem.navigateTo(new KlantScreen().getScene());
                case "Boekgegevens" -> BookstoreSystem.navigateTo(new BoekScreen().getScene());
                case "Voorraad" -> BookstoreSystem.navigateTo(new VoorraadScreen().getScene());
                case "Transacties" -> BookstoreSystem.navigateTo(new TransactieScreen().getScene());
                case "Verkoop" -> BookstoreSystem.navigateTo(new VerkoopScreen().getScene());
            }
        });

        return tile;
    }
}
