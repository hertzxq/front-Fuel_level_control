package org.sasha;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final DbConnectionImpl dbConnection;

    public EmployeeController() {
        this.dbConnection = new DbConnectionImpl();
    }

    @PostMapping
    public Map<String, Object> addEmployee(@RequestBody Map<String, String> employeeData) {
        String login = employeeData.get("login").trim();
        String rawPassword = employeeData.get("password").trim();
        String job = employeeData.get("job").trim();

        // Генерируем соль и хэшируем пароль
        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashPassword(rawPassword, salt);

        Map<String, Object> response = new HashMap<>();
        String query = "INSERT INTO public.users (login, password, salt, job) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            statement.setString(2, hashedPassword);
            statement.setString(3, salt); // Сохраняем соль
            statement.setString(4, job);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                connection.commit();
                response.put("status", "success");
                response.put("message", "Employee added successfully");
            } else {
                response.put("status", "error");
                response.put("message", "Failed to add employee");
            }

        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
        }

        return response;
    }

}
