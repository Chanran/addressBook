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

public class EditPersonController extends Pane {
	
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
	private String personIdEdit;
	
	private static Stage editPersonStage = new Stage();
	private Parent root = null;
	public EditPersonController(String contactPersonId) {
		
		this.personIdEdit = contactPersonId;
		
		FXMLLoader editPersonFxml = new FXMLLoader(getClass().getResource("../view/editPerson.fxml"));
		editPersonFxml.setRoot(this);
		editPersonFxml.setController(this);
		try{
			root =  editPersonFxml.load();
			editPersonStage.setScene(new Scene(root));
			editPersonStage.setTitle("");
			editPersonStage.show();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		
		/*联系人基本信息*/
		this.name.setText(ContactPerson.getInstance().get(contactPersonId).getName());
		this.telephone.setText(ContactPerson.getInstance().get(contactPersonId).getTelephone());
		this.qq.setText(ContactPerson.getInstance().get(contactPersonId).getQq());
		this.wechat.setText(ContactPerson.getInstance().get(contactPersonId).getWechat());
		this.email.setText(ContactPerson.getInstance().get(contactPersonId).getEmail());
		this.weibo.setText(ContactPerson.getInstance().get(contactPersonId).getWeibo());
		this.blog.setText(ContactPerson.getInstance().get(contactPersonId).getBlog());
		this.github.setText(ContactPerson.getInstance().get(contactPersonId).getGithub());
		this.address.setText(ContactPerson.getInstance().get(contactPersonId).getAddress());
		/*联系组的groupsListView*/
		Iterator iterator = new ContactGroup().getInstance().entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			ContactGroup group = (ContactGroup) entry.getValue();
			groupsListView.getItems().add(group);
		}
		String groupSelectedIds = ContactPerson.getInstance().get(contactPersonId).getGroupIds(); //联系人的联系群
		
		try{
			groupsListView.setCellFactory(CheckBoxListCell.forListView(new Callback<ContactGroup, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(ContactGroup  group){
					//默认checkbox unselected
					BooleanProperty observable= new SimpleBooleanProperty();
					
					/*判断如果在联系组就将checkbox selected*/
					if(!groupSelectedIds.equals("")){
						String[] groupSelectedIdArr = groupSelectedIds.split("_");
						for(int j = 0; j < groupSelectedIdArr.length; j++){
							if (groupSelectedIdArr[j].equals(group.getId())){
								observable  = new SimpleBooleanProperty(true);
								break;
							}
						}
					}
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
	public void editPerson(){
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
		//判断名字是否为空
		if (!name.equals("")){
			ContactPerson newPerson = new ContactPerson();
			newPerson.setId(this.personIdEdit);
			newPerson.setIsCollected(ContactPerson.getInstance().get(this.personIdEdit).getIsCollected());
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

			ContactPerson.getInstance().put(this.personIdEdit, newPerson);
			try {
				AlertController alertController = new AlertController("编辑联系人成功!");
				FileHandler.writePersonsCache();
				MainController.freshContactPersons("");
				editPersonStage.close();
			} catch (Exception e) {
			}
		}else{
			try {
				AlertController alertController = new AlertController("联系人姓名不能为空!");
			} catch (Exception e) {
			
			}
		}
		//清除选中的联系人
		
		
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

	public static Stage getEditPersonStage() {
		return editPersonStage;
	}

	public static void setEditPersonStage(Stage editPersonStage) {
		EditPersonController.editPersonStage = editPersonStage;
	}
	
}
