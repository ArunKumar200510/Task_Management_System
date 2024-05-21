package application;

import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class javadbms_controller {
	
	

    @FXML
    private PasswordField signupPasswordField;
    
    @FXML
    private TextField mailField;
    
    @FXML
    private TextField userIdField;
    
    @FXML
    private TextField userNum;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField loginPasswordField;

    @FXML
    void loginAction() {
    	String mail = usernameField.getText();
    	String password = loginPasswordField.getText();
    	
    	if (mail.isEmpty() || password.isEmpty() ) {
            showAlert("Error", "All fields must be filled out.");
        }
    	else
    	{
    		if(!isloginUserExists(mail,password))
    		{
    			showAlert("Error","No user Found");
    		}
    		else
    		{
    	       create_my_task.setUserEmail(mail);
    	       Main.cstage.setScene(Main.homescene);
    	       
    		}
    	}
        
    }

    @FXML
    void signupAction() {
    	String num = userNum.getText();
        String mail = mailField.getText();
        String password = signupPasswordField.getText();
        String userId = userIdField.getText();

        if (mail.isEmpty() || password.isEmpty() || userId.isEmpty() || num.isEmpty()) {
            showAlert("Error", "All fields must be filled out.");
        } else {
        	if(isUserExists(userId,mail))
        	{
        		showAlert("Error","User with the given ID or mail already exitsts.");
        	}
        	else {
            // Sign up storing
            try {
                // Load JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // connection
                String url = "jdbc:mysql://localhost:3306/user";
                String user = "root";
                String dbPassword = "ak496496@571";
                
                Connection connection = DriverManager.getConnection(url, user, dbPassword);
                
                String sql = "INSERT INTO user_details (user_id, user_mail, user_password,user_num) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, mail);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, num);

                preparedStatement.executeUpdate();

          
                preparedStatement.close();
                connection.close();

                showAlert("Success", "User registered successfully!");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while registering the user.");
            }
        	}
        }
    }
    
    private boolean isloginUserExists(String mail,String password)
    {
    	try
    	{
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		
    		String url = "jdbc:mysql://localhost:3306/user";
    		String user = "root";
    		String dbPassword = "ak496496@571";
    		Connection connection = DriverManager.getConnection(url,user,dbPassword);
    		
    		String sql = "SELECT * FROM user_details WHERE user_mail = ? AND user_password = ?";
    		try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
    		{
    			
    			preparedStatement.setString(1, mail);
                preparedStatement.setString(2, password);

  
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean isUserExists(String userId, String mail)
    {
    	try
    	{
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		
    		String url = "jdbc:mysql://localhost:3306/user";
    		String user = "root";
    		String dbPassword = "ak496496@571";
    		Connection connection = DriverManager.getConnection(url,user,dbPassword);
    		
    		String sql = "SELECT * FROM user_details WHERE user_id = ? OR user_mail = ?";
    		try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
    		{
    			
    			preparedStatement.setString(1, userId);
                preparedStatement.setString(2, mail);

  
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    	}
    

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
