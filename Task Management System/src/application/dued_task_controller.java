package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dued_task_controller {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/user";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "ak496496@571";

    // Date and time format
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm";

    // Function to process tasks
    public static void processTasks() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Query to fetch tasks from task_details table
            String selectQuery = "SELECT * FROM task_details";

            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                // Execute the query
                var resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    // Extract task details
                    String taskName = resultSet.getString("task_name");
                    String taskDesc = resultSet.getString("task_desc");
                    String dateString = resultSet.getString("task_due_date");
                    String timeString = resultSet.getString("task_due_time");

                    // Convert date and time to LocalDateTime
                    LocalDateTime taskDateTime = parseDateTime(dateString, timeString);

                    // Check if the task is overdue
                    if (taskDateTime.isBefore(LocalDateTime.now())) {
                        // Move the task to dued_task table
                        moveTaskToDuedTaskTable(connection, taskName, taskDesc, taskDateTime);

                        // Delete the task from task_details table
                        deleteTaskFromTaskDetailsTable(connection, taskName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to parse date and time strings and create LocalDateTime
    private static LocalDateTime parseDateTime(String dateString, String timeString) {
        String dateTimeString = dateString + " " + timeString;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT + " " + TIME_FORMAT);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    // Function to move a task to dued_task table
    private static void moveTaskToDuedTaskTable(Connection connection, String taskName, String taskDesc, LocalDateTime taskDateTime) {
        try {
            // Query to insert a task into dued_task table
            String insertQuery = "INSERT INTO dued_task (task_name, task_desc, task_datetime) VALUES (?, ?, ?)";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                // Set parameters
                insertStatement.setString(1, taskName);
                insertStatement.setString(2, taskDesc);
                insertStatement.setObject(3, taskDateTime);

                // Execute the insert query
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to delete a task from task_details table
    private static void deleteTaskFromTaskDetailsTable(Connection connection, String taskName) {
        try {
            // Query to delete a task from task_details table
            String deleteQuery = "DELETE FROM task_details WHERE task_name = ?";

            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                // Set parameter
                deleteStatement.setString(1, taskName);

                // Execute the delete query
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Call the function to process tasks
        processTasks();
    }
    
}
