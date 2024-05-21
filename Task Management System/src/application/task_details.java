package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class task_details {

    private final StringProperty taskNo;
    private final StringProperty taskName;
    private final StringProperty taskDesc;
    private final StringProperty taskDueDateTime;
    private final StringProperty taskProgress;
    private final Button updateButton;
    private final maintaskcontroller mainController;

    public task_details(String taskNo, String taskName, String taskDesc, String taskDueDateTime, String taskProgress, maintaskcontroller mainController) {
        this.taskNo = new SimpleStringProperty(taskNo);
        this.taskName = new SimpleStringProperty(taskName);
        this.taskDesc = new SimpleStringProperty(taskDesc);
        this.taskDueDateTime = new SimpleStringProperty(taskDueDateTime);
        this.taskProgress = new SimpleStringProperty(taskProgress);
        this.mainController = mainController;

        // Initialize the updateButton
        updateButton = new Button("Update");
        updateButton.setOnAction(this::handleUpdate);
    }

    private void handleUpdate(ActionEvent event) {
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
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
                // User clicked Yes, call methods from maintaskcontroller
                
                mainController.moveTaskToCompleted(this);
                mainController.deleteTaskFromTaskDetails(this);
            }
            // If No, do nothing
        });
    }

    // Getter for updateButton
    public Button getUpdateButton() {
        return updateButton;
    }

    public StringProperty taskNoProperty() {
        return taskNo;
    }

    public StringProperty taskNameProperty() {
        return taskName;
    }

    public StringProperty taskDescProperty() {
        return taskDesc;
    }

    public StringProperty taskDueDateTimeProperty() {
        return taskDueDateTime;
    }

    public StringProperty taskProgressProperty() {
        return taskProgress;
    }
}
