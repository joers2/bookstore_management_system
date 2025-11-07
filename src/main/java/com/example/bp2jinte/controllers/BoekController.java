package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoekController {

    // Velden die eigenschappen van een boek voorstellen
    private final String ISBN;
    private final String titel;
    private final String auteur;
    private final String uitgever;
    private final int paginas;
    private final double verkoopprijs;
    private final int voorraad;

    // Constructor waarmee een boek handmatig aangemaakt wordt
    public BoekController(String ISBN, String titel, String auteur, String uitgever, int paginas, double verkoopprijs, int voorraad) {
        this.ISBN = ISBN;
        this.titel = titel;
        this.auteur = auteur;
        this.uitgever = uitgever;
        this.paginas = paginas;
        this.verkoopprijs = verkoopprijs;
        this.voorraad = voorraad;
    }

    // Constructor waarmee een boekobject direct uit een databasequery wordt opgebouwd
    public BoekController(ResultSet rs) throws SQLException {
        this.ISBN = rs.getString("ISBN");
        this.titel = rs.getString("Titel");
        this.auteur = rs.getString("Auteur");
        this.uitgever = rs.getString("Uitgever");
        this.paginas = rs.getInt("Paginas");
        this.verkoopprijs = rs.getDouble("Verkoopprijs");
        this.voorraad = rs.getInt("Voorraad");
    }

    // Methodes om de eigenschappen van het boek op te vragen
    public String getISBN() { return ISBN; }
    public String getTitel() { return titel; }
    public String getAuteur() { return auteur; }
    public String getUitgever() { return uitgever; }
    public int getPaginas() { return paginas; }
    public double getVerkoopprijs() { return verkoopprijs; }
    public int getVoorraad() { return voorraad; }

    // Methode die bepaalt hoe het boek weergegeven wordt in een lijst of log
    @Override
    public String toString() {
        return titel + " (" + auteur + ")";
    }

    // Zoek boeken op titel, auteur of ISBN
    public static List<BoekController> zoekBoeken(String term) {
        List<BoekController> boeken = new ArrayList<>();
        try {
            // Maak verbinding met de database
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query met LIKE-operatoren om te zoeken op meerdere velden
            String sql = """
                SELECT * FROM Boeken
                WHERE Titel LIKE ? OR Auteur LIKE ? OR ISBN LIKE ?
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Stel zoekpatroon in zonder vaste zoekterm
            String pattern = "%" + term + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            // Voer query uit en doorloop resultaat
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Voeg elk boek toe aan de lijst
                boeken.add(new BoekController(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Fout bij zoeken boeken: " + e.getMessage());
        }
        return boeken; // Geef alle gevonden boeken terug
    }

    // Boek toevoegen + voorraadregel aanmaken
    public static boolean addBoek(BoekController boek) {
        Connection conn = null;
        PreparedStatement boekStmt = null;
        PreparedStatement voorraadStmt = null;

        try {
            // Start databaseverbinding
            conn = BookstoreSystem.db.getConnection();
            conn.setAutoCommit(false); // Start transactie

            // Boek toevoegen aan Boeken-tabel
            String sqlBoek = """
            INSERT INTO Boeken (ISBN, Titel, Auteur, Uitgever, Paginas, Verkoopprijs, Voorraad)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
            boekStmt = conn.prepareStatement(sqlBoek);
            boekStmt.setString(1, boek.getISBN());
            boekStmt.setString(2, boek.getTitel());
            boekStmt.setString(3, boek.getAuteur());
            boekStmt.setString(4, boek.getUitgever());
            boekStmt.setInt(5, boek.getPaginas());
            boekStmt.setDouble(6, boek.getVerkoopprijs());
            boekStmt.setInt(7, boek.getVoorraad());
            boekStmt.executeUpdate();

            // Boek toevoegen aan Voorraad-tabel (zodat winkelvoorraad wordt bijgehouden)
            String sqlVoorraad = """
            INSERT INTO Voorraad (ISBN, WinkelVoorraad, AantalTeBestellen)
            VALUES (?, ?, 0)
        """;
            voorraadStmt = conn.prepareStatement(sqlVoorraad);
            voorraadStmt.setString(1, boek.getISBN());
            voorraadStmt.setInt(2, boek.getVoorraad()); // eventueel 0 als beginwaarde
            voorraadStmt.executeUpdate();

            // Commit beide queries als alles gelukt is
            conn.commit();
            return true;

        } catch (SQLException e) {
            // Foutmelding en rollback bij fout
            System.err.println("Fout bij toevoegen boek + voorraad: " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Herstel transactie
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback mislukt: " + rollbackEx.getMessage());
            }
            return false;

        } finally {
            // Sluit statements en herstel autocommit
            try {
                if (boekStmt != null) boekStmt.close();
                if (voorraadStmt != null) voorraadStmt.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    // Boek updaten
    public static boolean updateBoek(String isbn, BoekController boek) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query voor het bijwerken van een bestaand boek
            String sql = """
                UPDATE Boeken
                SET Titel=?, Auteur=?, Uitgever=?, Paginas=?, Verkoopprijs=?, Voorraad=?
                WHERE ISBN=?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, boek.getTitel());
            stmt.setString(2, boek.getAuteur());
            stmt.setString(3, boek.getUitgever());
            stmt.setInt(4, boek.getPaginas());
            stmt.setDouble(5, boek.getVerkoopprijs());
            stmt.setInt(6, boek.getVoorraad());
            stmt.setString(7, isbn);

            // Voer update uit
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij updaten boek: " + e.getMessage());
            return false;
        }
    }

    // Boek verwijderen
    public static boolean deleteBoek(String isbn) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // Simpele delete-query op basis van ISBN
            String sql = "DELETE FROM Boeken WHERE ISBN = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, isbn);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij verwijderen boek: " + e.getMessage());
            return false;
        }
    }
}
