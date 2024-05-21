package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class db_connectivity {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/user";
    private static final String USER = "root";
    private static final String PASSWORD = "ak496496@571";

    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        }
    }
    

    public static void deleteTaskInDatabase(String taskNoToDelete) {
        String deleteQuery = "DELETE FROM task_details WHERE task_no = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setString(1, taskNoToDelete);
            
            // Execute the delete query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
    public static void deleteTaskInDatabase(task_details task) {
        String deleteQuery = "DELETE FROM task_details WHERE task_no = ?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setString(1, task.taskNoProperty().get());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }


}
