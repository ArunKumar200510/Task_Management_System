package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class routine_controller {

    @FXML
    private TableView<RoutineTask> routineTableView;

    @FXML
    private TableColumn<RoutineTask, String> colTaskName;

    @FXML
    private TableColumn<RoutineTask, String> colTaskDesc;

    @FXML
    private TableColumn<RoutineTask, String> colTaskTime;

   

    @FXML
    public void initialize() {
        colTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        colTaskDesc.setCellValueFactory(cellData -> cellData.getValue().taskDescProperty());
        colTaskTime.setCellValueFactory(cellData -> cellData.getValue().taskTimeProperty());

        loadTableData();
    }

    private void loadTableData() {
        try (Connection connection = db_connectivity.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM routine_task");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String taskTime = resultSet.getString("task_time");
                String taskName = resultSet.getString("task_name");
                String taskDesc = resultSet.getString("task_desc");

                RoutineTask routineDetails = new RoutineTask(taskTime, taskName, taskDesc);
                routineTableView.getItems().add(routineDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void refreshTable() {
    	routineTableView.getItems().clear(); // Clear existing items
    	loadTableData(); // Reload the table data
    }
    
    public void routineadd() {
    	routine_controller controller = (routine_controller) Main.routinetaskloader.getController();
        controller.refreshTable();

        // Change the scene
        Main.cstage.setScene(Main.routineaddscene);
    }
    
    public void go_back()
    {
    	Main.cstage.setScene(Main.homescene);
    }

    
}
