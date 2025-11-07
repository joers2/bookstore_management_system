package com.example.bp2jinte.controllers;

import com.example.bp2jinte.models.Gebruiker;

public class LoginController {

    // Probeer in te loggen met gebruikersnaam en wachtwoord
    public Gebruiker handleLogin(String gebruikersnaam, String wachtwoord) {
        // Roep de statische login-methode van de Gebruiker-klasse aan
        // Deze methode doorzoekt de database en retourneert een Gebruiker-object
        // als de combinatie van gebruikersnaam en wachtwoord geldig is.
        Gebruiker gebruiker = Gebruiker.login(gebruikersnaam, wachtwoord);

        // Controleer of een gebruiker is gevonden
        if (gebruiker != null) {
            // Login succesvol: toon bevestiging in de console
            System.out.println("Ingelogd als: " + gebruiker.getVoornaam() + " " + gebruiker.getAchternaam());
        } else {
            // Login mislukt: toon foutmelding in de console
            System.out.println("Ongeldige gebruikersnaam of wachtwoord");
        }

        // Geef het Gebruiker-object terug (of null als login is mislukt)
        return gebruiker;
    }
}
