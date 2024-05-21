package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;


public class Homepage {

	public void backToBeforeHome(MouseEvent event)
	{
		Main.cstage.setScene(Main.loginscene);
	}
	
	public void maintask()
	{
		Main.cstage.setScene(Main.maintaskscene);
	}
	public void routinpage()
	{
		Main.cstage.setScene(Main.routinescene);
	}

}
