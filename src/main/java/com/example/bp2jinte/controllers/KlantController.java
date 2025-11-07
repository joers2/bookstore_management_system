package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.models.Klant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KlantController {

    // Haal alle klanten op die overeenkomen met een zoekterm (voornaam, achternaam of e-mail)
    public static List<Klant> zoekKlanten(String term) {
        List<Klant> klanten = new ArrayList<>();
        try {
            // Maak verbinding met de database
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query met LIKE-operatoren om te zoeken in meerdere kolommen
            String sql = """
                SELECT * FROM Klanten
                WHERE Voornaam LIKE ? OR Achternaam LIKE ? OR Email LIKE ?
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Voeg wildcard toe aan zoekterm voor gedeeltelijke overeenkomsten
            String pattern = "%" + term + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            // Voer query uit
            ResultSet rs = stmt.executeQuery();

            // Maak voor elke gevonden rij een nieuw Klant-object
            while (rs.next()) {
                klanten.add(new Klant(rs));
            }

        } catch (SQLException e) {
            // Foutmelding bij databaseproblemen
            System.err.println("Fout bij zoeken klanten: " + e.getMessage());
        }

        // Geef lijst van klanten terug (kan leeg zijn)
        return klanten;
    }

    // Voeg een nieuwe klant toe aan de database
    public static boolean addKlant(Klant klant) {
        try {
            // Verbind met de database
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL INSERT-query om nieuwe klant toe te voegen
            String sql = """
                INSERT INTO Klanten (Voornaam, Achternaam, Adres, Postcode, Email, Telefoonnr)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            // Vul queryparameters in op basis van klantgegevens
            stmt.setString(1, klant.getVoornaam());
            stmt.setString(2, klant.getAchternaam());
            stmt.setString(3, klant.getAdres());
            stmt.setString(4, klant.getPostcode());
            stmt.setString(5, klant.getEmail());
            stmt.setString(6, klant.getTelefoonnummer());

            // Voer query uit
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij toevoegen klant: " + e.getMessage());
            return false;
        }
    }

    // Update bestaande klant op basis van klantnummer (Klantnr)
    public static boolean updateKlant(int klantnr, Klant klant) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL UPDATE-query om klantgegevens te wijzigen
            String sql = """
                UPDATE Klanten
                SET Voornaam=?, Achternaam=?, Adres=?, Postcode=?, Email=?, Telefoonnr=?
                WHERE Klantnr=?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            // Stel nieuwe waarden in voor de kolommen
            stmt.setString(1, klant.getVoornaam());
            stmt.setString(2, klant.getAchternaam());
            stmt.setString(3, klant.getAdres());
            stmt.setString(4, klant.getPostcode());
            stmt.setString(5, klant.getEmail());
            stmt.setString(6, klant.getTelefoonnummer());
            stmt.setInt(7, klantnr);

            // Voer update uit
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij updaten klant: " + e.getMessage());
            return false;
        }
    }

    // Verwijder klant uit database via klantnummer
    public static boolean deleteKlant(int klantnr) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL DELETE-query verwijdert klant op basis van klantnummer
            String sql = "DELETE FROM Klanten WHERE Klantnr = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, klantnr);

            // Verwijder klant
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij verwijderen klant: " + e.getMessage());
            return false;
        }
    }

    // Haal één specifieke klant op via klantnummer (bijv. voor detailweergave)
    public static Klant getKlantById(int klantnr) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // Query om specifieke klant te selecteren
            String sql = "SELECT * FROM Klanten WHERE Klantnr = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, klantnr);

            ResultSet rs = stmt.executeQuery();

            // Als de klant bestaat, maak dan een nieuw Klant-object
            if (rs.next()) {
                return new Klant(rs);
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen klant: " + e.getMessage());
        }

        // Geen klant gevonden
        return null;
    }
}
