package com.example.bp2jinte;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void getConnection() {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        assertNotNull(conn, "Databaseverbinding mag niet null zijn");
    }
}