package org.sasha.db;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnectionImpl implements DbConnection {
    @Override
    public void select() {
        try {
            String request = "SELECT * FROM public.users";
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            System.out.println("ID | Login | Password | Admin Name | Admin Password");
            System.out.println("---------------------------------------------------");

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String adminName = resultSet.getString("admin_name");
                String adminPassword = resultSet.getString("admin_password");

                System.out.println(id + " | " + login + " | " + password + " | " + adminName + " | " + adminPassword);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении SELECT запроса: " + e.getMessage(), e);
        }
    }



    public int countObjects() {
        var size = 0;
        try {
            String request = "SELECT * FROM public.users";
            var statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            while (resultSet.next()) {
                size++;
            }
        } catch (SQLException e) {
            System.out.println("Cannot load data from db. Please try again.");
            throw new RuntimeException(e);
        }
        return size;
    }


}