package com.example.bp2jinte.screens;

import com.example.bp2jinte.BookstoreSystem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public abstract class NavBarScreen {

    protected BorderPane root;   // Hoofdcontainer van het scherm (met zijbalk + inhoud)
    protected VBox navItems;     // Lijst met navigatieknoppen (links in de zijbalk)

    // Bouwt de navigatiebalk en layout voor elk scherm dat hiervan erft.
    public NavBarScreen(String activeMenu) {
        root = new BorderPane(); // Hoofdlayout met zijbalk links en content in het midden

        // Titel van de sidebar
        Label sidebarTitle = new Label("Bookstore\nManagement");
        sidebarTitle.getStyleClass().add("sidebar-title");
        sidebarTitle.setAlignment(Pos.CENTER);
        sidebarTitle.setWrapText(true);
        sidebarTitle.setPrefWidth(180);

        // Navigatie-items container
        navItems = new VBox(15);
        navItems.setPadding(new Insets(20, 0, 0, 20));
        navItems.setAlignment(Pos.TOP_LEFT);

        // Maak de navigatieopties aan
        Label home = new Label("Home");
        Label klanten = new Label("Klantgegevens");
        Label verkoop = new Label("Verkoop");
        Label boeken = new Label("Boekgegevens");
        Label voorraad = new Label("Voorraad");
        Label transacties = new Label("Transacties");

        // Plaats alle items in een array voor makkelijke herhaling
        Label[] menuItems = {home, klanten, verkoop, boeken, voorraad, transacties};

        // Loop door alle menu-items en voeg gedrag toe
        for (Label item : menuItems) {
            item.getStyleClass().add("nav-item"); // Basisstijl
            if (item.getText().equals(activeMenu)) {
                item.getStyleClass().add("active"); // Markeer het actieve scherm
            }
            // Klikbare navigatie
            item.setOnMouseClicked(e -> handleNavigation(item.getText()));
        }

        navItems.getChildren().addAll(menuItems);

        // Uitlogknop onderaan de sidebar
        ImageView logoutIcon = new ImageView(
                new Image(BookstoreSystem.class.getResourceAsStream("/com/example/bp2jinte/icons/ic_logout.png"))
        );
        logoutIcon.setFitWidth(18);
        logoutIcon.setFitHeight(18);

        Label logoutLabel = new Label("Uitloggen");
        logoutLabel.getStyleClass().add("logout-label");

        HBox logoutBox = new HBox(8, logoutIcon, logoutLabel);
        logoutBox.setAlignment(Pos.CENTER_LEFT);
        logoutBox.getStyleClass().add("logout-button");
        logoutBox.setPadding(new Insets(8, 12, 8, 12));

        // Klik op 'Uitloggen' → terug naar login
        logoutBox.setOnMouseClicked(e -> BookstoreSystem.navigateTo(new LoginScreen().getScene()));

        // Samenstellen van de sidebar
        BorderPane sidebar = new BorderPane();
        VBox sidebarContent = new VBox(sidebarTitle, navItems);
        sidebar.setTop(sidebarContent);     // Titel en menu bovenaan
        sidebar.setBottom(logoutBox);       // Uitlogknop onderaan
        sidebar.setPrefWidth(180);
        sidebar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D9D9D9;");

        // Plaats sidebar aan de linkerkant
        root.setLeft(sidebar);

        // Achtergrondkleur voor rest van de layout
        root.setStyle("-fx-background-color: #F5F5F5;");
    }

    // Retourneert een Scene met standaard styling en afmetingen
    public Scene getScene() {
        Scene scene = new Scene(root, BookstoreSystem.applicationSize[0], BookstoreSystem.applicationSize[1]);
        scene.getStylesheets().add(
                BookstoreSystem.class.getResource("/com/example/bp2jinte/stylesheets/fullapp.css").toExternalForm()
        );
        return scene;
    }

    // Navigatiehandler
    // Wordt aangeroepen wanneer de gebruiker op een item in de zijbalk klikt
    private void handleNavigation(String target) {
        switch (target) {
            case "Home" -> BookstoreSystem.navigateTo(new HomeScreen().getScene());
            case "Klantgegevens" -> BookstoreSystem.navigateTo(new KlantScreen().getScene());
            case "Verkoop" -> BookstoreSystem.navigateTo(new VerkoopScreen().getScene());
            case "Boekgegevens" -> BookstoreSystem.navigateTo(new BoekScreen().getScene());
            case "Voorraad" -> BookstoreSystem.navigateTo(new VoorraadScreen().getScene());
            case "Transacties" -> BookstoreSystem.navigateTo(new TransactieScreen().getScene());
            default -> System.out.println("Navigation not implemented: " + target);
        }
    }
}
