package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.TransactieController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class TransactieScreen extends NavBarScreen {

    private final ListView<String> transactieList = new ListView<>(); // lijst met transacties
    private final TextField zoekveld = new TextField();               // zoekveld voor filteren
    private final DatePicker dateFrom = new DatePicker();             // beginperiode
    private final DatePicker dateTo = new DatePicker();               // eindperiode

    public TransactieScreen() {
        super("Transacties"); // active menu highlighting in sidebar

        // Titel
        Label title = new Label("Transacties");
        title.getStyleClass().add("page-title");

        // Zoekselectie
        zoekveld.setPromptText("Zoek op klant, ISBN of titel...");
        Button zoeken = new Button("Zoeken");
        Button vernieuwen = new Button("Vernieuwen");

        HBox searchRow = new HBox(10, zoekveld, zoeken, vernieuwen);
        searchRow.setAlignment(Pos.CENTER);

        // Datumfilter
        Label vanLabel = new Label("Van:");
        Label totLabel = new Label("Tot:");
        vanLabel.getStyleClass().add("date-label");
        totLabel.getStyleClass().add("date-label");

        dateFrom.setPromptText("Van datum");
        dateTo.setPromptText("Tot datum");

        Button filterDate = new Button("Filter op periode");
        filterDate.setMinWidth(140);

        HBox dateRow = new HBox(10, vanLabel, dateFrom, totLabel, dateTo, filterDate);
        dateRow.setAlignment(Pos.CENTER);

        // Combineer zoekbalk en datumfilter
        VBox searchBox = new VBox(15, searchRow, dateRow);
        searchBox.setAlignment(Pos.CENTER);

        // Lijstweergave voor transacties
        transactieList.setPrefHeight(400);
        transactieList.setPrefWidth(750);
        transactieList.getStyleClass().add("list-view");

        // Hoofdcontainer
        VBox container = new VBox(25, title, searchBox, transactieList);
        container.getStyleClass().add("klant-container"); // hergebruikt styling
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));

        // Plaats in het midden van de layout
        VBox layout = new VBox(30, container);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        root.setCenter(layout);

        // Start met laden van alle transacties
        loadTransacties();

        // Knopacties
        zoeken.setOnAction(e -> zoekTransacties());
        vernieuwen.setOnAction(e -> {
            zoekveld.clear();
            dateFrom.setValue(null);
            dateTo.setValue(null);
            loadTransacties(); // toon alles opnieuw
        });
        filterDate.setOnAction(e -> filterByDateRange());
    }

    // Laad en zoek functies

    // Laadt alle transacties uit de database
    private void loadTransacties() {
        transactieList.getItems().clear();
        List<TransactieController> transacties = TransactieController.getAllTransacties();

        if (transacties.isEmpty()) {
            transactieList.getItems().add("Geen transacties gevonden.");
            return;
        }

        // Voeg elke transactie toe aan de lijst
        for (TransactieController t : transacties) {
            transactieList.getItems().add(formatTransactie(t));
        }
    }

    // Zoek transacties op naam, ISBN of titel
    private void zoekTransacties() {
        String term = zoekveld.getText().trim();
        transactieList.getItems().clear();

        if (term.isEmpty()) {
            loadTransacties();
            return;
        }

        List<TransactieController> transacties = TransactieController.zoekTransacties(term);

        if (transacties.isEmpty()) {
            transactieList.getItems().add("Geen transacties gevonden voor: " + term);
            return;
        }

        for (TransactieController t : transacties) {
            transactieList.getItems().add(formatTransactie(t));
        }
    }

    // Datumfilter tussen twee datums logica

    private void filterByDateRange() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();

        // Controleer dat beide datums ingevuld zijn
        if (from == null || to == null) {
            new Alert(Alert.AlertType.WARNING, "Selecteer zowel een begin- als einddatum.").show();
            return;
        }

        // Controleer dat einddatum niet vóór begindatum ligt
        if (to.isBefore(from)) {
            new Alert(Alert.AlertType.WARNING, "De einddatum kan niet vóór de begindatum liggen.").show();
            return;
        }

        transactieList.getItems().clear();
        List<TransactieController> transacties = TransactieController.getTransactiesBetween(from, to);

        if (transacties.isEmpty()) {
            transactieList.getItems().add("Geen transacties tussen " + from + " en " + to);
            return;
        }

        for (TransactieController t : transacties) {
            transactieList.getItems().add(formatTransactie(t));
        }
    }

    // Opmaak

    private String formatTransactie(TransactieController t) {
        return String.format(
                "%s | %s | €%.2f | %s",
                t.getKlantNaam(),
                t.getBoekTitel(),
                t.getVerkoopprijs(),
                t.getDatum().toLocalDateTime().toLocalDate()
        );
    }

    // Scene ophalen en styling toepassen

    @Override
    public Scene getScene() {
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );
        return scene;
    }
}
