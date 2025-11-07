package com.example.bp2jinte.models;

public class Persoon {

    // Basisklasse die algemene eigenschappen van een persoon bevat.
    // Wordt gebruikt als superklasse voor o.a. Klant en Gebruiker,
    // zodat die klassen velden zoals naam, e-mail en telefoon kunnen hergebruiken.

    protected String voornaam;        // Voornaam van de persoon
    protected String achternaam;      // Achternaam van de persoon
    protected String email;           // E-mailadres
    protected String telefoonnummer;  // Telefoonnummer

    // Maakt een nieuw Persoon-object aan met de opgegeven gegevens.
    public Persoon(String voornaam, String achternaam, String email, String telefoonnummer) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.email = email;
        this.telefoonnummer = telefoonnummer;
    }

    public String getVoornaam() { return voornaam; }         // Geeft de voornaam terug
    public String getAchternaam() { return achternaam; }     // Geeft de achternaam terug
    public String getEmail() { return email; }               // Geeft het e-mailadres terug
    public String getTelefoonnummer() { return telefoonnummer; } // Geeft het telefoonnummer terug

    // Combineert voor- en achternaam in één string.
    public String getVolledigeNaam() {
        return voornaam + " " + achternaam;
    }

    // Retourneert een leesbare tekstrepresentatie van de persoon.
    @Override
    public String toString() {
        return getVolledigeNaam() + " (" + email + ")";
    }
}
