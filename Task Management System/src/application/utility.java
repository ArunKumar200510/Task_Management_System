package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class utility {

    public static void moveDueTasks() {
        String selectQuery = "SELECT * FROM task_details WHERE CONCAT(task_due_date, ' ', task_due_time) <= NOW() AND progress = 'onprogress'";

        try (Connection connection = db_connectivity.connect();
             Statement selectStatement = connection.createStatement();
             ResultSet resultSet = selectStatement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                String taskNo = resultSet.getString("task_no");
                String taskName = resultSet.getString("task_name");
                String user_mail = resultSet.getString("user_mail");
                String task_desc = resultSet.getString("task_desc");
                String task_due_date = resultSet.getString("task_due_date");
                String task_due_time = resultSet.getString("task_due_time");

                String formattedTaskDueDateTime = convertToCorrectDateTimeFormat(task_due_date, task_due_time);

                insertDueTaskIntoNewTable(taskNo, taskName, user_mail, task_desc, formattedTaskDueDateTime);
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static int getNextDueTaskNumber() {
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String dbPassword = "ak496496@571";
        String selectQuery = "SELECT MAX(d_task_no) + 1 AS nextTaskNo FROM dued_task";

        try (Connection connection = db_connectivity.connect();
             Statement selectStatement = connection.createStatement();
             ResultSet resultSet = selectStatement.executeQuery(selectQuery)) {

            if (resultSet.next()) {
                return resultSet.getInt("nextTaskNo");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    private static void insertDueTaskIntoNewTable(String taskNo, String taskName, String user_mail, String task_desc, String task_due_date_time) {
        int newDueTaskNo = getNextDueTaskNumber();
        String insertQuery = "INSERT INTO dued_task (d_task_no, task_name, user_mail, task_desc, task_due_date_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = db_connectivity.connect();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            insertStatement.setInt(1, newDueTaskNo);
            insertStatement.setString(2, taskName);
            insertStatement.setString(3, user_mail);
            insertStatement.setString(4, task_desc);
            insertStatement.setString(5, task_due_date_time);

            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMovedRows() {
        String deleteQuery = "DELETE FROM task_details WHERE CONCAT(task_due_date, ' ', task_due_time) <= NOW() AND progress = 'Onprogress'";

        try (Connection connection = db_connectivity.connect();
             Statement deleteStatement = connection.createStatement()) {

            deleteStatement.executeUpdate(deleteQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public static void executeTaskMoveProcess() {
        moveDueTasks();
        deleteMovedRows();
    }

    private static String convertToCorrectDateTimeFormat(String task_due_date, String task_due_time) throws ParseException {
        // Combine and format date and time to 'YYYY-MM-DD HH:mm:ss'
        String formattedDateTime = task_due_date + " " + task_due_time + ":00";
        
        // Parse the string into a Date object
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = inputFormat.parse(formattedDateTime);

        // Format the Date object into the desired format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
