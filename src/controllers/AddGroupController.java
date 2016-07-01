package controllers;

import java.io.IOException;

import common.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ContactGroup;
import model.ContactUser;
import model.Main;

public class AddGroupController extends Pane {
	
	@FXML private Button confirm;
	@FXML private TextField groupName;
	
	private static Stage addGroupStage = new Stage();
	private Parent root = null;
	public AddGroupController(){ 
		
		FXMLLoader alertFxml = new FXMLLoader(getClass().getResource("../view/addGroup.fxml"));
		alertFxml.setRoot(this);
		alertFxml.setController(this);
		try{
			root =  alertFxml.load();
			addGroupStage.setScene(new Scene(root));
			addGroupStage.setTitle("添加组");
			addGroupStage.show();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	@FXML 
	public void addGroup(){
		String insertId = String.valueOf((Integer.parseInt(ContactGroup.getLastId())+1));
		String groupName = this.groupName.getText();
		if (groupName.equals("")){
			try {
				@SuppressWarnings("unused")
				AlertController alertController = new AlertController("联系组名不能为空!");
			} catch (Exception e) {

			}
		}else{
			String userId = ContactUser.getLoginUserId();
			ContactGroup newGroup = new ContactGroup();
			newGroup.setId(insertId);
			newGroup.setUserId(userId);
			newGroup.setName(groupName);
			ContactGroup.getInstance().put(insertId, newGroup);
			try {
				@SuppressWarnings("unused")
				AlertController alertController = new AlertController("添加联系组成功!");
				FileHandler.writeGroupsCache();
				MainController.freshContactGroups();
				MainController.freshContactPersons(MainController.getNowGroupId());
				this.shutdown();
			} catch (Exception e) {
				
			}
			
		}
	}
	
	@FXML
	public void shutdown(){
		AddGroupController.addGroupStage.close();
	}

	public static Stage getAddGroupStage() {
		return addGroupStage;
	}

	public static void setAddGroupStage(Stage addGroupStage) {
		AddGroupController.addGroupStage = addGroupStage;
	}
	
}
