package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoorraadController {

    // Velden die de voorraadstatus van een boek weergeven
    private final String ISBN;              // Boek-ID (uniek)
    private final int winkelVoorraad;       // Huidige voorraad in de winkel
    private final int aantalTeBestellen;    // Aantal exemplaren dat nog besteld moet worden

    // Constructor om handmatig een voorraadobject aan te maken
    public VoorraadController(String ISBN, int winkelVoorraad, int aantalTeBestellen) {
        this.ISBN = ISBN;
        this.winkelVoorraad = winkelVoorraad;
        this.aantalTeBestellen = aantalTeBestellen;
    }

    // Constructor om voorraadgegevens direct uit een ResultSet (databasequery) op te bouwen
    public VoorraadController(ResultSet rs) throws SQLException {
        this.ISBN = rs.getString("ISBN");
        this.winkelVoorraad = rs.getInt("WinkelVoorraad");
        this.aantalTeBestellen = rs.getInt("AantalTeBestellen");
    }

    // Alleen uitlezen, niet aanpassen
    public String getISBN() { return ISBN; }
    public int getWinkelVoorraad() { return winkelVoorraad; }
    public int getAantalTeBestellen() { return aantalTeBestellen; }

    // Toon voorraadinfo in tekstvorm voor debuggen
    @Override
    public String toString() {
        return ISBN + " | Winkelvoorraad: " + winkelVoorraad + " | Te bestellen: " + aantalTeBestellen;
    }

    // Zoek voorraad op basis van boek-titel of auteur (via JOIN met Boeken-tabel)
    public static List<VoorraadController> zoekVoorraad(String term) {
        List<VoorraadController> voorraadLijst = new ArrayList<>();
        try {
            // Maak verbinding met database
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query: combineer Voorraad en Boeken, zoek op titel of auteur
            String sql = """
                SELECT v.*
                FROM Voorraad v
                JOIN Boeken b ON v.ISBN = b.ISBN
                WHERE b.Titel LIKE ? OR b.Auteur LIKE ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            String pattern = "%" + term + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            // Voer query uit
            ResultSet rs = stmt.executeQuery();

            // Maak VoorraadController-objecten van de resultaten
            while (rs.next()) {
                voorraadLijst.add(new VoorraadController(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fout bij zoeken voorraad: " + e.getMessage());
        }

        // Geef lijst terug (kan leeg zijn)
        return voorraadLijst;
    }

    // Haal één specifiek voorraadrecord op aan de hand van ISBN
    public static VoorraadController getVoorraadByISBN(String isbn) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query om één record te selecteren
            String sql = "SELECT * FROM Voorraad WHERE ISBN = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, isbn);

            ResultSet rs = stmt.executeQuery();

            // Als het record bestaat → return nieuw VoorraadController-object
            if (rs.next()) {
                return new VoorraadController(rs);
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen voorraad: " + e.getMessage());
        }

        // Geen resultaat gevonden
        return null;
    }

    // Update de winkelvoorraad (bijv. na levering van boeken)
    public static boolean updateWinkelVoorraad(String isbn, int nieuwAantal) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-update: pas de winkelvoorraad aan
            String sql = "UPDATE Voorraad SET WinkelVoorraad = ? WHERE ISBN = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nieuwAantal);
            stmt.setString(2, isbn);

            // Voer update uit
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij updaten winkelvoorraad: " + e.getMessage());
            return false;
        }
    }

    // Stel het aantal te bestellen direct in (bijv. door medewerker)
    public static boolean updateAantalTeBestellen(String isbn, int aantal) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-update: pas aantal te bestellen aan
            String sql = "UPDATE Voorraad SET AantalTeBestellen = ? WHERE ISBN = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, aantal);
            stmt.setString(2, isbn);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij updaten aantal te bestellen: " + e.getMessage());
            return false;
        }
    }

    // Voeg extra exemplaren toe aan het aantal te bestellen
    public static boolean voegToeAanBestelling(String isbn, int extraAantal) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL: verhoog AantalTeBestellen met een bepaald aantal
            String sql = """
                UPDATE Voorraad
                SET AantalTeBestellen = AantalTeBestellen + ?
                WHERE ISBN = ?
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, extraAantal);
            stmt.setString(2, isbn);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij bijwerken AantalTeBestellen: " + e.getMessage());
            return false;
        }
    }

    // Boeken ontvangen — tel het aantal te bestellen op bij de winkelvoorraad
    // en zet het aantal te bestellen weer op nul
    public static boolean ontvangBestelling(String isbn) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL: tel AantalTeBestellen op bij WinkelVoorraad en reset AantalTeBestellen
            String sql = """
                UPDATE Voorraad
                SET WinkelVoorraad = WinkelVoorraad + AantalTeBestellen,
                    AantalTeBestellen = 0
                WHERE ISBN = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, isbn);

            // Voer update uit
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij ontvangen bestelling: " + e.getMessage());
            return false;
        }
    }
}
