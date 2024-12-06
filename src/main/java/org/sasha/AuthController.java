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

        Map<String, Object> response = new HashMap<>();
        try (var connection = dbConnection.connect()) {

            // Проверяем таблицу admins
            if (authenticateUser(connection, "public.admins", login, rawPassword, "admin", response)) {
                return response;
            }

            // Проверяем таблицу users
            if (authenticateUser(connection, "public.users", login, rawPassword, "user", response)) {
                return response;
            }

            // Если пользователь не найден ни в одной таблице
            response.put("status", "error");
            response.put("message", "Invalid login or password");

        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
        }

        return response;
    }

    private boolean authenticateUser(
            java.sql.Connection connection,
            String tableName,
            String login,
            String rawPassword,
            String role,
            Map<String, Object> response
    ) throws SQLException {
        String query = "SELECT password, salt FROM " + tableName + " WHERE login = ?";
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, login.trim());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                String salt = resultSet.getString("salt");

                if (PasswordUtils.verifyPassword(rawPassword, salt, storedPassword)) {
                    response.put("status", "success");
                    response.put("role", role);
                    return true;
                }
            }
        }
        return false;
    }


}
