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
        // Получаем данные из запроса
        String login = employeeData.get("login");
        String rawPassword = employeeData.get("password");
        String job = employeeData.get("job");

        // Хэшируем пароль перед сохранением
        String hashedPassword = PasswordUtils.hashPassword(rawPassword);

        // Отладочные сообщения
        System.out.println("Данные, полученные с фронтенда:");
        System.out.println("Login: " + login);
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);
        System.out.println("Job: " + job);

        Map<String, Object> response = new HashMap<>();
        String query = "INSERT INTO public.users (login, password, job) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            statement.setString(2, hashedPassword); // Сохраняем хэшированный пароль
            statement.setString(3, job);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Успешно добавлено " + rowsInserted + " строк.");
                connection.commit();
                response.put("status", "success");
                response.put("message", "Employee added successfully");
            } else {
                System.out.println("Данные не добавлены.");
                response.put("status", "error");
                response.put("message", "Failed to add employee");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка SQL-запроса: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
        }

        return response;
    }
}
