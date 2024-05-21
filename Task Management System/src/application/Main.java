package application;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application; 
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
    public static Stage cstage;
    public static Scene loginscene;
    public static Scene homescene;
    public static Scene maintaskscene;
    public static Scene addtaskscene;
    public static Scene deltaskscene;
    public static Scene completetaskscene;
    public static Scene routinescene;
    public static Scene routineaddscene;
    public static Scene duescene;
    

    // Add a static field for maintaskloader

    public static FXMLLoader maintaskloader = new FXMLLoader(Main.class.getResource("main_task.fxml"));
    public static  FXMLLoader completetaskloader =  new FXMLLoader(Main.class.getResource("completed_Task.fxml"));
    public static  FXMLLoader routinetaskloader =  new FXMLLoader(Main.class.getResource("routinetask.fxml"));


    @Override
    public void start(Stage primaryStage) throws Exception {
        cstage = primaryStage;

        // Load FXML files
        FXMLLoader addtaskloader = new FXMLLoader(getClass().getResource("create_task.fxml"));
        Parent addtasksceneroot = addtaskloader.load();
        
        
        Parent routineroot = routinetaskloader.load();
        
        FXMLLoader routineaddloader = new FXMLLoader(getClass().getResource("routine_add.fxml"));
        Parent routineaddroot = routineaddloader.load();
        
        
       
        Parent completetasksceneroot = completetaskloader.load();

        FXMLLoader loginloader = new FXMLLoader(getClass().getResource("puchuku.fxml"));
        Parent loginroot = loginloader.load();

        FXMLLoader homeloader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homeroot = homeloader.load();
        
        FXMLLoader dueloader = new FXMLLoader(getClass().getResource("duedtasktable.fxml"));
        Parent dueroot = dueloader.load();

        // Use the static maintaskloader field to load the main_task.fxml file
        Parent maintasksceneroot = maintaskloader.load();
        

        FXMLLoader deltaskloader = new FXMLLoader(getClass().getResource("delete_task.fxml"));
        Parent deltasksceneroot = deltaskloader.load();

        // Set up scenes
        duescene = new Scene(dueroot);
        maintaskscene = new Scene(maintasksceneroot);
        loginscene = new Scene(loginroot);
        homescene = new Scene(homeroot);
        addtaskscene = new Scene(addtasksceneroot);
        deltaskscene = new Scene(deltasksceneroot);
        completetaskscene = new Scene(completetasksceneroot);
        routinescene = new Scene(routineroot);
        routineaddscene = new Scene(routineaddroot);

        // Set the initial scene
        cstage.setTitle("Login Page");
        dued_task_controller controller = new dued_task_controller();

        // Call the processTasks method
        controller.processTasks();
        cstage.setScene(loginscene);
        cstage.show();
    }
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
