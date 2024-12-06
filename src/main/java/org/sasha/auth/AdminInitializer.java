package org.sasha.auth;

import org.sasha.db.DbConnectionImpl;
import org.sasha.dependencies.PasswordUtils;
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

        // Генерация соли и хэширование пароля
        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashPassword(defaultAdminPassword, salt);

        String checkAdminQuery = "SELECT COUNT(*) FROM public.admins WHERE login = ?";
        String insertAdminQuery = "INSERT INTO public.admins (id, login, password, salt) VALUES (?, ?, ?, ?)";
        String updateAdminQuery = "UPDATE public.admins SET password = ?, salt = ? WHERE login = ?";

        try (Connection connection = dbConnection.connect()) {
            PreparedStatement checkStatement = connection.prepareStatement(checkAdminQuery);
            checkStatement.setString(1, defaultAdminLogin);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) == 0) {
                // Если админ не существует, добавляем нового
                PreparedStatement insertStatement = connection.prepareStatement(insertAdminQuery);
                insertStatement.setString(1, id);
                insertStatement.setString(2, defaultAdminLogin);
                insertStatement.setString(3, hashedPassword);
                insertStatement.setString(4, salt);
                insertStatement.executeUpdate();
                connection.commit();
                System.out.println("Admin user created with salt.");
            } else {
                // Если админ уже существует, обновляем соль и пароль
                PreparedStatement updateStatement = connection.prepareStatement(updateAdminQuery);
                updateStatement.setString(1, hashedPassword);
                updateStatement.setString(2, salt);
                updateStatement.setString(3, defaultAdminLogin);
                updateStatement.executeUpdate();
                connection.commit();
                System.out.println("Admin user updated with new salt.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}