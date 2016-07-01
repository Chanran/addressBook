package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class AlertController extends Pane {
//	private boolean flag = false;  //停止在对话框
	@FXML private Label label;
	private Stage alertStage = new Stage();
	private Parent root = null;
	public AlertController(String text){ 
		
		FXMLLoader alertFxml = new FXMLLoader(getClass().getResource("../view/alert.fxml"));
		alertFxml.setRoot(this);
		alertFxml.setController(this);
		try{
			root =  alertFxml.load();
			alertStage.setScene(new Scene(root));
			alertStage.setTitle("提示");
			this.label.setText(text);
			alertStage.show();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	@FXML
	protected void shutdown(ActionEvent event){
		//this.setFlag(true);
		alertStage.close();
	}
	/*
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	public boolean getFlag(){
		return this.flag;
	}
	*/
}
