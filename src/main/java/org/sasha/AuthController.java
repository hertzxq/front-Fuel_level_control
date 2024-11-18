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
        String login = loginRequest.get("login");
        String password = loginRequest.get("password");

        Map<String, Object> response = new HashMap<>();
        try {
            // Проверяем, есть ли пользователь в таблице users
            String userQuery = "SELECT * FROM public.users WHERE login = ? AND password = ?";
            var connection = dbConnection.connect();
            var userStatement = connection.prepareStatement(userQuery);
            userStatement.setString(1, login);
            userStatement.setString(2, password);
            ResultSet userResult = userStatement.executeQuery();

            if (userResult.next()) {
                // Пользователь найден в таблице users
                response.put("status", "success");
                response.put("role", "user");
                response.put("job", userResult.getString("job"));
            } else {
                String adminQuery = "SELECT * FROM public.admins WHERE login = ? AND password = ?";
                var adminStatement = connection.prepareStatement(adminQuery);
                adminStatement.setString(1, login);
                adminStatement.setString(2, password);
                ResultSet adminResult = adminStatement.executeQuery();

                if (adminResult.next()) {
                    // Администратор найден в таблице admins
                    response.put("status", "success");
                    response.put("role", "admin");
                } else {
                    // Если ни в одной таблице пользователь не найден
                    response.put("status", "error");
                    response.put("message", "Invalid login or password");
                }
            }
        } catch (SQLException e) {
            // Обработка ошибок базы данных
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
        }

        return response;
    }
}
