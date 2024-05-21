
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

public class RoutineTask {

    private final StringProperty taskTime;
    private final StringProperty taskName;
    private final StringProperty taskDesc;


    public RoutineTask(String taskTime, String taskName, String taskDesc) {
        this.taskTime = new SimpleStringProperty(taskTime);
        this.taskName = new SimpleStringProperty(taskName);
        this.taskDesc = new SimpleStringProperty(taskDesc);
       

        // Initialize the updateButton
     
    }

   
    // Getter for updateButton
  
    public StringProperty taskTimeProperty() {
        return taskTime;
    }

    public StringProperty taskNameProperty() {
        return taskName;
    }

    public StringProperty taskDescProperty() {
        return taskDesc;
    }
}
