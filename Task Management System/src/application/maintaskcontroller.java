package application;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class maintaskcontroller {

    @FXML
    private TableColumn<task_details, Button> colUpdate;

    @FXML
    private TableView<task_details> tableView;

    @FXML
    private TableColumn<task_details, String> colTaskNo;

    @FXML
    private TableColumn<task_details, String> colTaskName;

    @FXML
    private TableColumn<task_details, String> colTaskDesc;

    @FXML
    private TableColumn<task_details, String> colTaskDueDateTime;

    @FXML
    private TableColumn<task_details, String> colProgress;

    @FXML
    public void initialize() {
        colTaskNo.setCellValueFactory(cellData -> cellData.getValue().taskNoProperty());
        colTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        colTaskDesc.setCellValueFactory(cellData -> cellData.getValue().taskDescProperty());
        colTaskDueDateTime.setCellValueFactory(cellData -> cellData.getValue().taskDueDateTimeProperty());
        colProgress.setCellValueFactory(cellData -> cellData.getValue().taskProgressProperty());

        loadTableData();
        colUpdate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUpdateButton()));
    }

    private void loadTableData() {
        try (Connection connection = db_connectivity.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM task_details");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String taskNo = resultSet.getString("task_no");
                String taskName = resultSet.getString("task_name");
                String taskDesc = resultSet.getString("task_desc");
                String taskDueTime = resultSet.getString("task_due_time");
                String taskDueDate = resultSet.getString("task_due_date");
                String taskDueDateTime = taskDueTime + ' ' + taskDueDate;
                String prog = resultSet.getString("progress");

                task_details taskDetails = new task_details(taskNo, taskName, taskDesc, taskDueDateTime, prog,this);
                tableView.getItems().add(taskDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        tableView.getItems().clear(); // Clear existing items
        loadTableData(); // Reload the table data
    }
    public void add_Task() {
        Main.cstage.setScene(Main.addtaskscene);
    }

    public void back_home() {
        Main.cstage.setScene(Main.homescene);
    }

    @FXML
    public void del_Task() {
        try {
            // Load the DeleteTask.fxml file
            FXMLLoader deleteTaskLoader = new FXMLLoader(getClass().getResource("delete_task.fxml"));
            Parent deleteTaskRoot = deleteTaskLoader.load();

            // Set the MainTaskController instance in the DeleteTaskController
            deltask_controller deleteTaskController = deleteTaskLoader.getController();
            deleteTaskController.setMainTaskController(this); // "this" refers to the MainTaskController

            // Create a new stage for the DeleteTask window
            Stage deleteTaskStage = new Stage();

            // Set the scene with the DeleteTaskRoot and show the stage
            deleteTaskStage.setScene(new Scene(deleteTaskRoot));

            // Set the stage in DeleteTaskController
            deleteTaskController.setStage(deleteTaskStage);

            // Show the DeleteTask window
            deleteTaskStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an alert or log the error
        }
    }

   

    public void showConfirmationDialog(task_details selectedTask) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Set to complete?");
        alert.setContentText("Do you really want to set this task to complete?");

        // Add Yes and No buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show and wait for user action
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                // User clicked Yes, move task to completed_tasks and delete from task_details
                moveTaskToCompleted(selectedTask);
            }
            // If No, do nothing
        });
    }
    public void moveTaskToCompleted(task_details selectedTask) {
        try (Connection connection = db_connectivity.connect()) {
        
            String maxTaskNoQuery = "SELECT COALESCE(MAX(task_no), 0) + 1 AS nextTaskNo FROM completed_task";
            int nextTaskNo = 1;

            try (PreparedStatement maxTaskNoStatement = connection.prepareStatement(maxTaskNoQuery);
                 ResultSet maxTaskNoResultSet = maxTaskNoStatement.executeQuery()) {

                if (maxTaskNoResultSet.next()) {
                    nextTaskNo = maxTaskNoResultSet.getInt("nextTaskNo");
                }
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO completed_task (task_no, task_name, task_desc) VALUES (?, ?, ?)")) {

                // Set values for the completed_tasks table
                insertStatement.setInt(1, nextTaskNo);
                insertStatement.setString(2, selectedTask.taskNameProperty().get());
                insertStatement.setString(3, selectedTask.taskDescProperty().get());
                // Add other columns as needed

                // Execute the insert query
                insertStatement.executeUpdate();

                // Now, delete the task from task_details
                deleteTaskFromTaskDetails(selectedTask);

                // Refresh the table after the update
                refreshTable();
                
                

            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    
    public void complete_task()
    {
    	CompletedTasksController controller = (CompletedTasksController) Main.completetaskloader.getController();
        controller.refreshTable();

        // Change the scene
       
    	Main.cstage.setScene(Main.completetaskscene);
    }
    
    public void go_due()
    {
    	dued_task_controller controller = new dued_task_controller();
        controller.processTasks();
        
 
    	Main.cstage.setScene(Main.duescene);
    }

    public void deleteTaskFromTaskDetails(task_details selectedTask) {
        String taskNo = selectedTask.taskNoProperty().get();
        db_connectivity.deleteTaskInDatabase(selectedTask); // Pass the entire task_details object
        deltask_controller.renumberTasksAfterDelete(taskNo);
        refreshTable();
    }


}