package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoekControllerTest {

    @BeforeAll
    static void setupDatabase() {
        BookstoreSystem.db = new DatabaseConnection(); // ✅ Database initialiseren
    }
    @Test
    void addBoek() {
        BoekController nieuwBoek = new BoekController("999-TEST", "Test Titel", "Auteur X", "Uitgever Y", 250, 19.99, 10);
        boolean result = BoekController.addBoek(nieuwBoek);
        assertTrue(result, "Boek moet succesvol worden toegevoegd");
    }
}
