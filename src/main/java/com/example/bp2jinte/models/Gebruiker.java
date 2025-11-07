package com.example.bp2jinte.models;

import com.example.bp2jinte.BookstoreSystem;
import java.sql.*;

public class Gebruiker extends Persoon {

    // Velden die een gebruiker uniek identificeren
    private final int gebruikerID;        // Primaire sleutel in de database
    private final String gebruikersnaam;  // Loginnaam van de gebruiker
    private final String wachtwoord;      // Wachtwoord van de gebruiker

    // Constructor die een Gebruiker-object opbouwt vanuit een ResultSet (na query)
    public Gebruiker(ResultSet result) throws SQLException {
        // Roep de superconstructor van Persoon aan (voornaam + achternaam)
        // Andere velden van Persoon (zoals adres, telefoon, enz.) worden hier niet gebruikt
        super(
                result.getString("Voornaam"),
                result.getString("Achternaam"),
                null,
                null
        );

        // Vul de specifieke velden van Gebruiker in
        this.gebruikerID = result.getInt("GebruikerID");
        this.gebruikersnaam = result.getString("Gebruikersnaam");
        this.wachtwoord = result.getString("Wachtwoord");
    }

    public int getGebruikerID() { return gebruikerID; }
    public String getGebruikersnaam() { return gebruikersnaam; }
    public String getWachtwoord() { return wachtwoord; }

    // Probeert een gebruiker te authenticeren op basis van gebruikersnaam en wachtwoord
    public static Gebruiker login(String gebruikersnaam, String wachtwoord) {
        try {
            // Maak verbinding met de database
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query: zoek naar een gebruiker met overeenkomende inloggegevens
            String sql = "SELECT * FROM Gebruiker WHERE Gebruikersnaam = ? AND Wachtwoord = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, gebruikersnaam);
            stmt.setString(2, wachtwoord);

            // Voer query uit
            ResultSet rs = stmt.executeQuery();

            // Als er een record gevonden is → maak een nieuw Gebruiker-object aan
            if (rs.next()) {
                return new Gebruiker(rs); // Succesvol ingelogd
            }

        } catch (SQLException e) {
            // Foutmelding in console
            System.err.println("Fout bij inloggen: " + e.getMessage());
        }

        // Geen gebruiker gevonden → ongeldige login
        return null;
    }
}
