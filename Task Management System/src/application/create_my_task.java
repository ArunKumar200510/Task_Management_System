package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class create_my_task {

    @FXML
    private TextField taskTitle;

    @FXML
    private TextField taskDesc;

    @FXML
    private TextField taskDate;

    @FXML
    private TextField taskTime;

    @FXML
    private Button addTask;

    @FXML
    private Button cancelAdd;

    
    private static String userEmail; // User email variable

    // Setter method for user email
    public static void setUserEmail(String userEmail) {
        create_my_task.userEmail = userEmail;
    }

    @FXML
    void add_Task() {
        // Get values from the input fields
        String title = taskTitle.getText();
        String description = taskDesc.getText();
        String date = taskDate.getText();
        String time = taskTime.getText();

        // Establish the database connection
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String dbPassword = "ak496496@571";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
        	int newTaskNo = getNextTaskNumber();
            // Insert data into the task_details table
            String insertQuery = "INSERT INTO task_details (task_no,task_name, user_mail, task_desc, task_due_date, task_due_time) VALUES (?, ?, ?, ?, ?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            	 preparedStatement.setInt(1, newTaskNo);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, userEmail); // Replace with the actual user mail
                preparedStatement.setString(4, description);
                preparedStatement.setString(5, date);
                preparedStatement.setString(6, time);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert("Success","Task Created Successfully");
                    maintaskcontroller controller = (maintaskcontroller) Main.maintaskloader.getController();
                    controller.refreshTable();

                    // Change the scene
                    Main.cstage.setScene(Main.maintaskscene);
                    
                } else {
                	showAlert("Error","Adding task Failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failure");
        }
    }
    private int getNextTaskNumber() {
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String dbPassword = "ak496496@571";
        String selectQuery = "SELECT COALESCE(MAX(task_no), 0) + 1 AS nextTaskNo FROM task_details";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             Statement selectStatement = connection.createStatement();
             ResultSet resultSet = selectStatement.executeQuery(selectQuery)) {

            if (resultSet.next()) {
                return resultSet.getInt("nextTaskNo");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        // Return a default value if something goes wrong
        return 1;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void cancelAdd() {
        Main.cstage.setScene(Main.maintaskscene);
    }
    
    
}
