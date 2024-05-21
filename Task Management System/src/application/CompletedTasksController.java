package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompletedTasksController {

    @FXML
    private TableView<completed_task_details> completedTasksTableView;

    @FXML
    private TableColumn<completed_task_details, String> colCompletedTaskNo;

    @FXML
    private TableColumn<completed_task_details, String> colCompletedTaskName;

    @FXML
    private TableColumn<completed_task_details, String> colCompletedTaskDesc;

    @FXML
    private TableColumn<completed_task_details, String> colCompletedTaskProgress;

    @FXML
    public void initialize() {
        colCompletedTaskNo.setCellValueFactory(cellData -> cellData.getValue().taskNoProperty());
        colCompletedTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        colCompletedTaskDesc.setCellValueFactory(cellData -> cellData.getValue().taskDescProperty());
               colCompletedTaskProgress.setCellValueFactory(cellData -> cellData.getValue().taskProgressProperty());

        loadCompletedTasks();
    }
    
    public void go_back()
    {
    	Main.cstage.setScene(Main.maintaskscene);
    }

    private void loadCompletedTasks() {
        try (Connection connection = db_connectivity.connect()) {
            String selectQuery = "SELECT * FROM completed_task";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String taskName = resultSet.getString("task_name");
                    String taskNo = resultSet.getString("task_no");
                    String taskDesc = resultSet.getString("task_desc");
                    String prog = resultSet.getString("progress");

                    completed_task_details completedTask = new completed_task_details(taskNo, taskName, taskDesc, prog);
                    completedTasksTableView.getItems().add(completedTask);
                  
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
    	completedTasksTableView.getItems().clear(); // Clear existing items
    	loadCompletedTasks(); // Reload the table data
    }

}
