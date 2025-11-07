package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactieController {

    // Velden die de gegevens van een transactie voorstellen
    private final int transactieID;
    private final int klantnr;
    private final String klantNaam;
    private final String ISBN;
    private final String boekTitel;
    private final double verkoopprijs;
    private final Timestamp datum;

    // Constructor waarmee een volledig transactie-object handmatig aangemaakt kan worden
    public TransactieController(int transactieID, int klantnr, String klantNaam, String ISBN, String boekTitel, double verkoopprijs, Timestamp datum) {
        this.transactieID = transactieID;
        this.klantnr = klantnr;
        this.klantNaam = klantNaam;
        this.ISBN = ISBN;
        this.boekTitel = boekTitel;
        this.verkoopprijs = verkoopprijs;
        this.datum = datum;
    }

    // Constructor die een transactie opbouwt uit een databasequery (ResultSet)
    public TransactieController(ResultSet rs) throws SQLException {
        this.transactieID = rs.getInt("TransactieID");
        this.klantnr = rs.getInt("Klantnr");
        this.klantNaam = rs.getString("KlantNaam");
        this.ISBN = rs.getString("ISBN");
        this.boekTitel = rs.getString("Titel");
        this.verkoopprijs = rs.getDouble("Verkoopprijs");
        this.datum = rs.getTimestamp("Datum");
    }

    // Enkele toegangsmethoden (getters)
    public int getTransactieID() { return transactieID; }
    public int getKlantnr() { return klantnr; }
    public String getKlantNaam() { return klantNaam; }
    public String getISBN() { return ISBN; }
    public String getBoekTitel() { return boekTitel; }
    public double getVerkoopprijs() { return verkoopprijs; }
    public Timestamp getDatum() { return datum; }

    // ToString geeft een samenvatting van de transactie
    @Override
    public String toString() {
        return String.format("Transactie #%d | %s | %s | €%.2f | %s",
                transactieID,
                klantNaam,
                boekTitel,
                verkoopprijs,
                datum.toLocalDateTime().toLocalDate() // Alleen de datum, zonder tijd
        );
    }

    // Voeg een nieuwe transactie toe aan de database
    public static boolean addTransactie(int klantnr, String isbn, double verkoopprijs) {
        try {
            // Maak verbinding met de database
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query: voeg een nieuwe transactie toe met huidige tijd via NOW()
            String sql = """
                INSERT INTO Transacties (Klantnr, ISBN, Verkoopprijs, Datum)
                VALUES (?, ?, ?, NOW())
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, klantnr);
            stmt.setString(2, isbn);
            stmt.setDouble(3, verkoopprijs);

            // Voer de insert uit
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Toon foutmelding als iets misgaat
            System.err.println("Fout bij toevoegen transactie: " + e.getMessage());
            return false;
        }
    }

    // Haal alle transacties op met klantnaam en boektitel
    public static List<TransactieController> getAllTransacties() {
        List<TransactieController> lijst = new ArrayList<>();
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query die transacties koppelt aan klanten en boeken via JOINs
            String sql = """
                SELECT t.TransactieID, t.Klantnr,
                       CONCAT(k.Voornaam, ' ', k.Achternaam) AS KlantNaam,
                       t.ISBN, b.Titel,
                       t.Verkoopprijs, t.Datum
                FROM Transacties t
                JOIN Klanten k ON t.Klantnr = k.Klantnr
                JOIN Boeken b ON t.ISBN = b.ISBN
                ORDER BY t.Datum DESC
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Voor elke rij een nieuw TransactieController-object aanmaken
            while (rs.next()) {
                lijst.add(new TransactieController(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen transacties: " + e.getMessage());
        }
        return lijst; // Geef lijst met transacties terug
    }

    // Zoek transacties op basis van klantnaam, boektitel, ISBN of datum
    public static List<TransactieController> zoekTransacties(String term) {
        List<TransactieController> lijst = new ArrayList<>();
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query met meerdere LIKE-condities om op naam, titel of datum te zoeken
            String sql = """
                SELECT t.TransactieID, t.Klantnr,
                       CONCAT(k.Voornaam, ' ', k.Achternaam) AS KlantNaam,
                       t.ISBN, b.Titel,
                       t.Verkoopprijs, t.Datum
                FROM Transacties t
                JOIN Klanten k ON t.Klantnr = k.Klantnr
                JOIN Boeken b ON t.ISBN = b.ISBN
                WHERE CONCAT(k.Voornaam, ' ', k.Achternaam) LIKE ?
                   OR b.Titel LIKE ?
                   OR t.ISBN LIKE ?
                   OR DATE_FORMAT(t.Datum, '%Y-%m-%d') LIKE ?
                ORDER BY t.Datum DESC
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);

            // Voeg wildcard toe aan zoekterm
            String pattern = "%" + term + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);

            // Voer query uit
            ResultSet rs = stmt.executeQuery();

            // Verwerk resultaat
            while (rs.next()) {
                lijst.add(new TransactieController(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fout bij zoeken transacties: " + e.getMessage());
        }
        return lijst;
    }

    // Filter transacties tussen twee datums
    public static List<TransactieController> getTransactiesBetween(LocalDate from, LocalDate to) {
        List<TransactieController> lijst = new ArrayList<>();
        try {
            Connection conn = BookstoreSystem.db.getConnection();

            // SQL-query met BETWEEN voor datumfilter
            String sql = """
            SELECT t.TransactieID, t.Klantnr,
                   CONCAT(k.Voornaam, ' ', k.Achternaam) AS KlantNaam,
                   t.ISBN, b.Titel,
                   t.Verkoopprijs, t.Datum
            FROM Transacties t
            JOIN Klanten k ON t.Klantnr = k.Klantnr
            JOIN Boeken b ON t.ISBN = b.ISBN
            WHERE DATE(t.Datum) BETWEEN ? AND ?
            ORDER BY t.Datum DESC
        """;

            PreparedStatement stmt = conn.prepareStatement(sql);

            // Converteer LocalDate naar SQL Date
            stmt.setDate(1, java.sql.Date.valueOf(from));
            stmt.setDate(2, java.sql.Date.valueOf(to));

            // Voer query uit
            ResultSet rs = stmt.executeQuery();

            // Verwerk alle resultaten
            while (rs.next()) {
                lijst.add(new TransactieController(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fout bij filteren transacties op datum: " + e.getMessage());
        }
        return lijst;
    }
}
