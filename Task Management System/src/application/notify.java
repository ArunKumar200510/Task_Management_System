package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class notify {

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule the task to run every minute
        scheduler.scheduleAtFixedRate(notify::checkAndAddNotifications, 0, 1, TimeUnit.MINUTES);
    }

    private static void checkAndAddNotifications() {
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String password = "ak496496@571";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String selectQuery = "SELECT * FROM routine_task WHERE task_time <= ? AND checked = 'Not_checked'";
            String insertQuery = "INSERT INTO notifications (message) VALUES (?)";
            String updateQuery = "UPDATE routine_task SET checked = 'Checked' WHERE task_time <= ? AND checked = 'Not_checked'";

            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                LocalTime currentTime = LocalTime.now();

                // Set the current time as the upper limit
                selectStatement.setTime(1, java.sql.Time.valueOf(currentTime));
                updateStatement.setTime(1, java.sql.Time.valueOf(currentTime));

                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    String taskName = resultSet.getString("task_name");

                    String notificationMessage = "Now, it's time to " + taskName;

                    // Add a row to the notifications table
                    insertStatement.setString(1, notificationMessage);
                    insertStatement.executeUpdate();

                    // Update the checked column in routine_tasks
                    updateStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
