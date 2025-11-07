module com.example.bp2jinte {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;

    opens com.example.bp2jinte to javafx.fxml;
    exports com.example.bp2jinte;
}