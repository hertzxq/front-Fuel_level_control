package org.sasha;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final DbConnectionImpl dbConnection;

    public AdminInitializer() {
        this.dbConnection = new DbConnectionImpl();
    }

    @Override
    public void run(String... args) {
        String id = "0";
        String defaultAdminLogin = "admin";
        String defaultAdminPassword = "admin";
        String hashedPassword = PasswordUtils.hashPassword(defaultAdminPassword);

        System.out.println("Initializing default admin...");
        String checkAdminQuery = "SELECT COUNT(*) FROM public.admins WHERE login = ?";
        String insertAdminQuery = "INSERT INTO public.admins (id, login, password) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.connect()) {
            // Check if the admin already exists
            PreparedStatement checkStatement = connection.prepareStatement(checkAdminQuery);
            checkStatement.setString(1, defaultAdminLogin);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) == 0) {
                PreparedStatement insertStatement = connection.prepareStatement(insertAdminQuery);
                insertStatement.setString(1, id);
                insertStatement.setString(2, defaultAdminLogin);
                insertStatement.setString(3, hashedPassword);
                int rowsInserted = insertStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Default admin created successfully.");
                    connection.commit();
                } else {
                    System.out.println("Failed to create default admin.");
                }
            } else {
                System.out.println("Default admin already exists.");
            }

        } catch (SQLException e) {
            System.err.println("Database error while initializing default admin: " + e.getMessage());
        }
    }
}
