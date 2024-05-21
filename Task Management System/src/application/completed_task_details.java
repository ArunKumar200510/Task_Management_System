package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class completed_task_details extends task_details {

	private final StringProperty taskNo;
    private final StringProperty taskName;
    private final StringProperty taskDesc;
    private final StringProperty taskProgress;

    public completed_task_details(String taskNo, String taskName, String taskDesc, String prog) {
        // Pass null for the mainController parameter since it's not used
        super(taskNo, taskName, taskDesc,null, prog, null);

        // Override the updateButton behavior
        this.taskNo = new SimpleStringProperty(taskNo);
        this.taskName = new SimpleStringProperty(taskName);
        this.taskDesc = new SimpleStringProperty(taskDesc);
        this.taskProgress = new SimpleStringProperty(prog);
    }

    @Override
    public StringProperty taskNoProperty() {
        return taskNo;
    }

    @Override
    public StringProperty taskNameProperty() {
        return taskName;
    }

    @Override
    public StringProperty taskDescProperty() {
        return taskDesc;
    }

    @Override
    public StringProperty taskProgressProperty() {
        return taskProgress;
    }
}
