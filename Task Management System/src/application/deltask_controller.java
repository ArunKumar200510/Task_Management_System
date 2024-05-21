package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class deltask_controller {
	

    @FXML
    private TextField del_task_num;

    // Inject the main controller to access the table and refresh it
    private maintaskcontroller mainTaskController;
    


    // Setter method to inject the main controller
    public void setMainTaskController(maintaskcontroller mainTaskController) {
        this.mainTaskController = mainTaskController;
    }

    // Inject the stage to close the window
    private Stage stage;

    // Setter method to inject the stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void deleteTask() {
        String taskNoToDelete = del_task_num.getText();

        // Add logic to delete the task from the database based on taskNoToDelete
        db_connectivity.deleteTaskInDatabase(taskNoToDelete);
        renumberTasksAfterDelete(taskNoToDelete);
        

        // After successfully deleting the task, refresh the table in the main view
        mainTaskController.refreshTable();

        // Close the delete task window
        closeDeleteTaskWindow();
    }
    
    public static void renumberTasksAfterDelete(String deletedTaskNo) {
        // Shift down task numbers for tasks that come after the deleted task
        String updateQuery = "UPDATE task_details SET task_no = task_no - 1 WHERE task_no > ?";
        
        try (Connection connection = db_connectivity.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, deletedTaskNo);
            
            // Execute the update query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

   
    private void closeDeleteTaskWindow() {
        // Close the window using the injected stage
        if (stage != null) {
            stage.close();
        }
    }
    
    public void del_cancel()
    {
    	Main.cstage.setScene(Main.maintaskscene);
    }

}