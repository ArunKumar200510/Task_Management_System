package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dued_task_table {

    @FXML
    private TableView<dued_task_details> tableView;

    @FXML
    private TableColumn<dued_task_details, String> taskNameCol;

    @FXML
    private TableColumn<dued_task_details, String> taskDescCol;

    @FXML
    private TableColumn<dued_task_details, String> duedTimeCol;

    @FXML
    private TableColumn<dued_task_details, String> progressCol;

    private ObservableList<dued_task_details> duedTaskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        taskNameCol.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        taskDescCol.setCellValueFactory(cellData -> cellData.getValue().taskDescProperty());
        duedTimeCol.setCellValueFactory(cellData -> cellData.getValue().taskTimeProperty());
        progressCol.setCellValueFactory(cellData -> cellData.getValue().taskprogProperty());

        // Initialize the table with data
        loadTableData();
        tableView.setItems(duedTaskList);
    }

    private void loadTableData() {
        try (Connection connection = db_connectivity.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dued_task");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String taskTime = resultSet.getString("task_datetime");
                String taskName = resultSet.getString("task_name");
                String taskDesc = resultSet.getString("task_desc");
                String taskprog = resultSet.getString("progress");

                duedTaskList.add(new dued_task_details(taskName, taskDesc, taskTime, taskprog));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void back() {
        Main.cstage.setScene(Main.maintaskscene);
     }
}
