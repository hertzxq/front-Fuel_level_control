package org.sasha;

import org.sasha.DbConnectionImpl;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final DbConnectionImpl dbConnection;

    public AuthController() {
        this.dbConnection = new DbConnectionImpl();
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest) {
        String login = loginRequest.get("login").trim();
        String rawPassword = loginRequest.get("password").trim(); // Введённый пароль
        String hashedPassword = PasswordUtils.hashPassword(rawPassword); // Хэшируем введённый пароль

        System.out.println("Login attempt:");
        System.out.println("Login: " + login);
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);

        Map<String, Object> response = new HashMap<>();
        try {
            var connection = dbConnection.connect();

            // Проверяем таблицу admins
            if (authenticateUser(connection, "public.admins", login, hashedPassword, "admin", response)) {
                return response;
            }

            // Проверяем таблицу users
            if (authenticateUser(connection, "public.users", login, hashedPassword, "user", response)) {
                return response;
            }

            // Если пользователь не найден ни в одной таблице
            System.out.println("User not found in both tables.");
            response.put("status", "error");
            response.put("message", "Invalid login or password");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
        }

        return response;
    }

    private boolean authenticateUser(
            java.sql.Connection connection,
            String tableName,
            String login,
            String hashedPassword,
            String role,
            Map<String, Object> response
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE login = ?";
        var statement = connection.prepareStatement(query);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            System.out.println("Stored Password in " + tableName + ": " + storedPassword);

            if (storedPassword.equals(hashedPassword)) {
                System.out.println(role + " authentication successful.");
                response.put("status", "success");
                response.put("role", role);
                return true;
            } else {
                System.out.println("Password mismatch for " + role + ".");
            }
        } else {
            System.out.println(role + " not found in " + tableName + ".");
        }
        return false;
    }
}
