package com.example.bp2jinte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // --- Verbinding met de database ---
    private Connection connection;

    public DatabaseConnection() {
        try {
            // Probeer een verbinding te maken met de MySQL-database via JDBC
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:8889/portfolio_jinte", // URL van de database
                    "root",  // gebruikersnaam
                    "root"   // wachtwoord
            );

            System.out.println("Databaseverbinding succesvol opgezet!");

        } catch (SQLException e) {
            // Als de verbinding mislukt, gooi een foutmelding
            System.err.println("Fout bij verbinden met database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Geeft de actieve Connection terug, zodat andere klassen SQL-queries kunnen uitvoeren
    public Connection getConnection() {
        return connection;
    }
}
