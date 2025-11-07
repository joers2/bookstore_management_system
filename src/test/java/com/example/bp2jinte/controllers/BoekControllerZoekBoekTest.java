package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoekControllerZoekBoekTest {

    @BeforeAll
    static void setupDatabase() {
        BookstoreSystem.db = new DatabaseConnection(); // ✅ Database initialiseren
    }

    @Test
    void zoekBoeken() {
        var resultaten = BoekController.zoekBoeken("Test");
        assertNotNull(resultaten, "Zoekresultaat mag niet null zijn");
    }
}