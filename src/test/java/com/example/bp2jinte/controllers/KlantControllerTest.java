package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.DatabaseConnection;
import com.example.bp2jinte.models.Klant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KlantControllerTest {

    @BeforeAll
    static void setupDatabase() {
        BookstoreSystem.db = new DatabaseConnection(); // ✅ Database initialiseren
    }
    @Test
    void addKlant() {
        Klant klant = new Klant("Pieter", "Jansen", "Dorpsstraat 1", "1234AB", "pieter@test.com", "0612345678");
        boolean result = KlantController.addKlant(klant);
        assertTrue(result, "Klant moet succesvol worden toegevoegd");
    }
}