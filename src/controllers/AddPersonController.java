package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import common.FileHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ContactGroup;
import model.ContactPerson;
import model.ContactUser;

public class AddPersonController extends Pane {
	
	@FXML private TextField name;
	@FXML private TextField telephone;
	@FXML private TextField qq;
	@FXML private TextField wechat;
	@FXML private TextField email;
	@FXML private TextField weibo;
	@FXML private TextField blog;
	@FXML private TextField github;
	@FXML private TextField address;
	@FXML private ListView<ContactGroup> groupsListView; 
	
	private ArrayList<String> groupsSelected = new ArrayList<>();
	
	private static Stage addPersonStage = new Stage();
	private Parent root = null;
	public AddPersonController(){ 
		
		FXMLLoader addPersonFxml = new FXMLLoader(getClass().getResource("../view/addPerson.fxml"));
		addPersonFxml.setRoot(this);
		addPersonFxml.setController(this);
		try{
			root =  addPersonFxml.load();
			addPersonStage.setScene(new Scene(root));
			addPersonStage.setTitle("");
			addPersonStage.show();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		
		/*联系组的groupsListView*/
		Iterator iterator = new ContactGroup().getInstance().entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			ContactGroup group = (ContactGroup) entry.getValue();
			groupsListView.getItems().add(group);
		}
		try{
			groupsListView.setCellFactory(CheckBoxListCell.forListView(new Callback<ContactGroup, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(ContactGroup  group){
					BooleanProperty observable = new SimpleBooleanProperty();
					observable.addListener((obs,wasSelected,isNowSelected)->
						{
							if (isNowSelected){
								groupsSelected.add(group.getId());
							}else{
								groupsSelected.remove(group.getId());
							}
							System.out.println("Check box for "+group.getId()+" name is "+group.getName()+" changed from "+wasSelected+" to "+isNowSelected);
						}
					);
					return observable;
				}
			}));
		}catch(Exception e){
			
		}
	}
	
	@FXML 
	public void addPerson(){
		String name = this.name.getText();
		String telephone = this.telephone.getText();
		String qq = this.qq.getText();
		String wechat = this.wechat.getText();
		String email = this.email.getText();
		String weibo = this.weibo.getText();
		String blog = this.blog.getText();
		String github = this.github.getText();
		String address = this.address.getText();
		String groupIds = "";
		//用"_"区分联系组id
		Iterator<String> iterator = groupsSelected.iterator();
		while(iterator.hasNext()){
			groupIds+=iterator.next();
			if (iterator.hasNext()){
				groupIds+="_";
			}
		}
		
		if (!name.equals("")){
			ContactPerson newPerson = new ContactPerson();
			String insertId = String.valueOf((Integer.valueOf(ContactPerson.getLastId())+1));
			newPerson.setId(insertId);
			newPerson.setUserId(ContactUser.getLoginUserId());
			newPerson.setGroupIds(groupIds);
			newPerson.setName(name);
			newPerson.setTelephone(telephone);
			newPerson.setQq(qq);
			newPerson.setWechat(wechat);
			newPerson.setEmail(email);
			newPerson.setWeibo(weibo);
			newPerson.setBlog(blog);
			newPerson.setGithub(github);
			newPerson.setAddress(address);
			newPerson.setIsCollected("0");

			ContactPerson.getInstance().put(insertId, newPerson);
			try {
				AlertController alertController = new AlertController("添加联系人成功!");
				FileHandler.writePersonsCache();
				MainController.freshContactPersons("");
				addPersonStage.close();
			} catch (Exception e) {
				
			}
			
		}else{
			try {
				AlertController alertController = new AlertController("联系人姓名不能为空!");
			} catch (Exception e) {
			
			}
		}
		
	}
	
	@FXML
	public void reset(){
		this.name.setText("");
		this.telephone.setText("");
		this.qq.setText("");
		this.wechat.setText("");
		this.email.setText("");
		this.weibo.setText("");
		this.blog.setText("");
		this.github.setText("");
		this.address.setText("");
	}

	public static Stage getAddPersonStage() {
		return addPersonStage;
	}

	public static void setAddPersonStage(Stage addPersonStage) {
		AddPersonController.addPersonStage = addPersonStage;
	}
	
}
