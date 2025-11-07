package com.example.bp2jinte.controllers;

import com.example.bp2jinte.BookstoreSystem;
import com.example.bp2jinte.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactieControllerTest {

    @BeforeAll
    static void setupDatabase() {
        BookstoreSystem.db = new DatabaseConnection(); // ✅ Database initialiseren
    }

    @Test
    void addTransactie() {
        boolean result = TransactieController.addTransactie(2, "9781529051438", 21.00);
        assertTrue(result, "Transactie moet worden toegevoegd");
    }
}