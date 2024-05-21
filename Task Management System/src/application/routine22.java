package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class routine22 {
	
	
	 @FXML
	    private TextField task_Name;

	    @FXML
	    private TextField task_Des;

	    @FXML
	    private TextField task_TIme;
	    
	    
	    
	@FXML
	public void back_routine()
	{
		Main.cstage.setScene(Main.routinescene);
	}
	    
	@FXML
    public void add_routine() {
        // Get values from the input fields
        String name = task_Name.getText();
        String description = task_Des.getText();
        String time = task_TIme.getText();

        // Establish the database connection
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String dbPassword = "ak496496@571";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            // Insert data into the routine_tasks table
            String insertQuery = "INSERT INTO routine_task (task_name, task_desc, task_time) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, time);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Data added successfully
                    showAlert("Success", "Routine Created Successfully");
                } else {
                    // Failed to add data
                    showAlert("Error", "Adding routine Failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        routine_controller controller = (routine_controller) Main.routinetaskloader.getController();
        controller.refreshTable();
        Main.cstage.setScene(Main.routinescene);
    }
}
