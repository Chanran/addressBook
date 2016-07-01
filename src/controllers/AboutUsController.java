package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AboutUsController  extends Pane{
	private Stage aboutUsStage = new Stage();
	private Parent root = null;
	public AboutUsController(){ 
		FXMLLoader aboutUsFxml = new FXMLLoader(getClass().getResource("../view/aboutUs.fxml"));
		aboutUsFxml.setRoot(this);
		aboutUsFxml.setController(this);
		try{
			root =  aboutUsFxml.load();
			aboutUsStage.setScene(new Scene(root));
			aboutUsStage.setTitle("关于我们");
			aboutUsStage.show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@FXML
	protected void shutdown(ActionEvent event){
		//this.setFlag(true);
		aboutUsStage.close();
	}
}
