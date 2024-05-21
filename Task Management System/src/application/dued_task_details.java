
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

public class dued_task_details {

    private final StringProperty taskNameCol;
    private final StringProperty taskDescCol;
    private final StringProperty duedTimeCol;
    private final StringProperty progressCol;



    public dued_task_details(String taskNameCol, String taskDescCol, String duedTimeCol,String progressCol) {
        this.taskNameCol = new SimpleStringProperty(taskNameCol);
        this.taskDescCol = new SimpleStringProperty(taskDescCol);
        this.progressCol = new SimpleStringProperty(progressCol);
        this.duedTimeCol = new SimpleStringProperty(duedTimeCol);
        
       

        // Initialize the updateButton
     
    }

   
    // Getter for updateButton
  
    public StringProperty taskTimeProperty() {
        return duedTimeCol;
    }

    public StringProperty taskNameProperty() {
        return taskNameCol;
    }

    public StringProperty taskDescProperty() {
        return taskDescCol;
    }
    public StringProperty taskprogProperty() {
        return progressCol;
    }
}
