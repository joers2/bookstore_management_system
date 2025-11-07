package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.controllers.BoekController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BoekScreen extends NavBarScreen {

    public BoekScreen() {
        super("Boekgegevens"); // Zet de titel in de navigatiebalk

        // Pagina-titel
        Label title = new Label("Boekgegevens");
        title.getStyleClass().add("page-title");

        // Inputvelden voor boekgegevens
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        TextField titelField = new TextField();
        titelField.setPromptText("Titel");

        TextField auteurField = new TextField();
        auteurField.setPromptText("Auteur");

        TextField uitgeverField = new TextField();
        uitgeverField.setPromptText("Uitgever");

        TextField paginasField = new TextField();
        paginasField.setPromptText("Pagina’s");

        TextField verkoopprijsField = new TextField();
        verkoopprijsField.setPromptText("Verkoopprijs");

        TextField voorraadField = new TextField();
        voorraadField.setPromptText("Voorraad");

        // Zoekveld bovenaan (voor snel zoeken naar boeken)
        TextField zoekveld = new TextField();
        zoekveld.setPromptText("Zoeken");

        // Knoppen voor zoeken en toevoegen
        Button zoeken = new Button("Zoeken");
        Button toevoegen = new Button("Toevoegen");

        // Layout met GridPane voor nette uitlijning
        GridPane grid = new GridPane();
        grid.setHgap(30); // horizontale ruimte tussen kolommen
        grid.setVgap(20); // verticale ruimte tussen rijen
        grid.setAlignment(Pos.CENTER);

        // Plaats velden en knoppen in grid
        grid.add(titelField, 0, 0);
        grid.add(zoekveld, 1, 0);
        grid.add(auteurField, 0, 1);
        grid.add(zoeken, 1, 1);
        grid.add(isbnField, 0, 2);
        grid.add(uitgeverField, 0, 3);
        grid.add(toevoegen, 1, 3);
        grid.add(paginasField, 0, 4);
        grid.add(verkoopprijsField, 0, 5);
        grid.add(voorraadField, 0, 6);

        // Container met wat extra padding en centrering
        VBox boekContainer = new VBox(grid);
        boekContainer.getStyleClass().add("klant-container"); // Hergebruik CSS-stijl
        boekContainer.setAlignment(Pos.CENTER);
        boekContainer.setPadding(new Insets(40));

        // Hoofdinhoud van het scherm (titel + grid)
        VBox centerContent = new VBox(30, title, boekContainer);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40));

        // Plaats alles in het midden van het NavBar-layout
        root.setCenter(centerContent);

        // Toevoeg-logica
        toevoegen.setOnAction(e -> {
            // Haal waarden uit de invoervelden
            String isbn = isbnField.getText().trim();
            String titel = titelField.getText().trim();
            String auteur = auteurField.getText().trim();
            String uitgever = uitgeverField.getText().trim();
            String paginasStr = paginasField.getText().trim();
            String prijsStr = verkoopprijsField.getText().trim();
            String voorraadStr = voorraadField.getText().trim();

            // Controleer of alle velden zijn ingevuld
            if (isbn.isEmpty() || titel.isEmpty() || auteur.isEmpty() || uitgever.isEmpty()
                    || paginasStr.isEmpty() || prijsStr.isEmpty() || voorraadStr.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vul alle velden in voordat je een boek toevoegt.").show();
                return;
            }

            try {
                // Converteer numerieke waarden
                int paginas = Integer.parseInt(paginasStr);
                double prijs = Double.parseDouble(prijsStr);
                int voorraad = Integer.parseInt(voorraadStr);

                // Maak een nieuw BoekController-object
                BoekController nieuwBoek = new BoekController(isbn, titel, auteur, uitgever, paginas, prijs, voorraad);

                // Probeer het boek toe te voegen aan de database
                boolean success = BoekController.addBoek(nieuwBoek);

                if (success) {
                    new Alert(Alert.AlertType.INFORMATION, "Boek toegevoegd!").show();

                    // Maak velden leeg na succesvolle toevoeging
                    isbnField.clear();
                    titelField.clear();
                    auteurField.clear();
                    uitgeverField.clear();
                    paginasField.clear();
                    verkoopprijsField.clear();
                    voorraadField.clear();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fout bij toevoegen boek!").show();
                }

            } catch (NumberFormatException ex) {
                // Foutmelding bij verkeerde numerieke invoer
                new Alert(Alert.AlertType.WARNING, "Pagina’s, prijs en voorraad moeten numerieke waarden zijn.").show();
            }
        });

        // Zoek-logica
        zoeken.setOnAction(e -> {
            String term = zoekveld.getText().trim();

            // Controleer of er iets is ingevuld
            if (term.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vul iets in om te zoeken.").show();
            } else {
                // Navigeer naar het zoekresultatenscherm
                BookstoreSystem.navigateTo(new BoekenZoekScreen(term).getScene());
            }
        });
    }

    // Scene instellen en styling toepassen
    @Override
    public Scene getScene() {
        // Maak een nieuwe scene met de standaard applicatiegrootte
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);

        // Voeg de CSS-stylesheet toe voor consistente styling
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );

        return scene;
    }
}
