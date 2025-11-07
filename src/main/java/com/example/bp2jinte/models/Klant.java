package com.example.bp2jinte.models;

import com.example.bp2jinte.BookstoreSystem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Modelklasse die een klant (Klant) in het systeem voorstelt.
// Deze klasse erft van Persoon, waardoor ze toegang heeft tot
// gemeenschappelijke velden zoals voornaam, achternaam, e-mail en telefoonnummer.
// Bevat ook statische methodes om klanten toe te voegen, te verwijderen,
// te updaten en te zoeken in de database.
public class Klant extends Persoon {

    private final int klantnr;     // Uniek klantnummer (primair in database)
    private final String adres;    // Straat en huisnummer
    private final String postcode; // Postcode

    // Constructor voor het inladen van een klant vanuit een database-resultaat (ResultSet).
    // Wordt gebruikt bij zoekopdrachten en lijsten.

    public Klant(ResultSet rs) throws SQLException {
        // Roep de superconstructor aan van Persoon om voornaam, achternaam, e-mail en telefoon in te stellen
        super(
                rs.getString("Voornaam"),
                rs.getString("Achternaam"),
                rs.getString("Email"),
                rs.getString("Telefoonnr")
        );

        // Stel specifieke klantvelden in
        this.klantnr = rs.getInt("Klantnr");
        this.adres = rs.getString("Adres");
        this.postcode = rs.getString("Postcode");
    }

    // Constructor voor het aanmaken van een nieuwe klant
    // Klantnummer wordt nog niet meegegeven, omdat de database dit automatisch genereert

    public Klant(String voornaam, String achternaam, String adres, String postcode, String email, String telefoonnummer) {
        super(voornaam, achternaam, email, telefoonnummer);
        this.klantnr = 0; // Wordt later toegewezen door de database
        this.adres = adres;
        this.postcode = postcode;
    }

    public int getKlantnr() { return klantnr; }
    public String getAdres() { return adres; }
    public String getPostcode() { return postcode; }

    @Override
    public String toString() {
        // Handige weergave voor in lijsten of debuglogs
        return String.format(
                "%d | Naam: %s | Adres: %s %s | Email: %s | Tel: %s",
                klantnr, getVolledigeNaam(), adres, postcode, email, telefoonnummer
        );
    }

    // Voeg een nieuwe klant toe aan de database
    // @return true als de klant succesvol is toegevoegd, anders false

    public static boolean add(String voornaam, String achternaam, String adres, String postcode, String email, String telefoonnummer) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();
            String sql = """
                INSERT INTO Klanten (Voornaam, Achternaam, Adres, Postcode, Email, Telefoonnr)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, voornaam);
            stmt.setString(2, achternaam);
            stmt.setString(3, adres);
            stmt.setString(4, postcode);
            stmt.setString(5, email);
            stmt.setString(6, telefoonnummer);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij toevoegen klant: " + e.getMessage());
            return false;
        }
    }

    // Verwijder een klant uit de database op basis van klantnummer.
    // @param klantnr het klantnummer van de te verwijderen klant
    // @return true als verwijderen lukt, anders false

    public static boolean delete(int klantnr) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();
            String sql = "DELETE FROM Klanten WHERE Klantnr = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, klantnr);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij verwijderen klant: " + e.getMessage());
            return false;
        }
    }


    // Update de gegevens van een klant in de database.
    // @param klantnr klantnummer van de te updaten klant
    // @return true als de update succesvol is uitgevoerd, anders false

    public static boolean update(int klantnr, String voornaam, String achternaam, String adres, String postcode, String email, String telefoonnummer) {
        try {
            Connection conn = BookstoreSystem.db.getConnection();
            String sql = """
                UPDATE Klanten
                SET Voornaam = ?, Achternaam = ?, Adres = ?, Postcode = ?, Email = ?, Telefoonnr = ?
                WHERE Klantnr = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, voornaam);
            stmt.setString(2, achternaam);
            stmt.setString(3, adres);
            stmt.setString(4, postcode);
            stmt.setString(5, email);
            stmt.setString(6, telefoonnummer);
            stmt.setInt(7, klantnr);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Fout bij aanpassen klant: " + e.getMessage());
            return false;
        }
    }


    // Zoek klanten op naam of e-mailadres.
    // @param searchTerm tekst die overeen moet komen met voornaam, achternaam of e-mail
    // @return lijst van gevonden klanten

    public static List<Klant> search(String searchTerm) {
        List<Klant> klanten = new ArrayList<>();

        try {
            Connection conn = BookstoreSystem.db.getConnection();
            String sql = """
                SELECT * FROM Klanten
                WHERE Voornaam LIKE ? OR Achternaam LIKE ? OR Email LIKE ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            String pattern = "%" + searchTerm + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            ResultSet rs = stmt.executeQuery();

            // Maak Klant-objecten voor elk resultaat
            while (rs.next()) {
                klanten.add(new Klant(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fout bij zoeken klant: " + e.getMessage());
        }

        return klanten;
    }
}
