package controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


import common.FileHandler;
import common.PinyinHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ContactGroup;
import model.ContactPerson;

public class MainController extends Pane {
	
	
	/*页面元素*/
	private static Stage MainStage = new Stage();
	private Parent root = null;
    @FXML private Pane pane; 														//页面根元素,用于添加或删除node
    private static Label allPersonsLabel = new Label();
    private static Label noGroupPersonsLabel = new Label();
    private static ListView<ContactGroup> groupsListView = new ListView<>();      //联系组的listView
    private static TableView<Person> personsTableView = new TableView<>();			//联系人的tableView	
    @FXML private TextField keyword;
    @FXML private Button searchBtn;
    
    /*页面元素--结束*/
    
    /*联系人tableView的列*/
    private static TableColumn<Person,CheckBox> isSelected = new TableColumn<>("");
	private static TableColumn<Person,String> isCollected = new TableColumn<>("收藏");
	private static TableColumn<Person,String> name = new TableColumn<>("姓名");
	private static TableColumn<Person,String> telephone = new TableColumn<>("手机");
	private static TableColumn<Person,String> qq = new TableColumn<>("QQ");
	private static TableColumn<Person,String> wechat = new TableColumn<>("微信");
	private static TableColumn<Person,String> email = new TableColumn<>("Email");
	private static TableColumn<Person,String> weibo = new TableColumn<>("微博");
	private static TableColumn<Person,String> blog = new TableColumn<>("博客");
	private static TableColumn<Person,String> github = new TableColumn<>("github");
	private static TableColumn<Person,String> address = new TableColumn<>("地址");
	private static TableColumn<Person,String> groupsNames = new TableColumn<>("分组");
	/*联系人tableView的列--结束*/
	
	
	/*逻辑层*/
	private static ArrayList<String> groupsForDel = new ArrayList<>();						//要删除的联系组
	private static ArrayList<String> personsSelected = new ArrayList<>();   				//选择的联系人
	private static ObservableList<Person> persons = FXCollections.observableArrayList();   //联系人的数据
	private static int addressBookStatus = 1;  //1为所有联系人,2为未分组,3为指定联系组
	private static String nowGroupId = "";      //当前联系组id       
	/*逻辑层--结束*/
	
	//构造函数
	public MainController(){ 
		
		FXMLLoader mainFxml = new FXMLLoader(getClass().getResource("../view/main.fxml"));
		mainFxml.setRoot(this);
		mainFxml.setController(this);
		try{
			root = mainFxml.load();
			MainStage.setScene(new Scene(root));
			MainStage.setTitle("通讯录");
			MainStage.show();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		
		FileHandler.readGroupsCache();	//读取联系组缓存
		FileHandler.readPersonsCache();	//读取联系人缓存
	
		freshContactGroups(); 			//刷新联系组列表
		freshContactPersons("");		//刷新联系人列表
	}
	
	//初始化页面
	@FXML protected void initialize(){ 
		
		//所有联系人的label
		allPersonsLabel.setLayoutX(125.0);
		allPersonsLabel.setLayoutY(48.0);
		allPersonsLabel.setPrefHeight(30);
		allPersonsLabel.setPrefWidth(30);
		pane.getChildren().add(allPersonsLabel);
		//未分组联系人的label
		noGroupPersonsLabel.setLayoutX(125.0);
		noGroupPersonsLabel.setLayoutY(78.0);
		noGroupPersonsLabel.setPrefHeight(30);
		noGroupPersonsLabel.setPrefWidth(30);
		pane.getChildren().add(noGroupPersonsLabel);
		
		//联系组listView的布局
		groupsListView.setLayoutX(4.0);
		groupsListView.setLayoutY(149.0);
		groupsListView.setPrefHeight(459.0);
		groupsListView.setPrefWidth(187.0);
		pane.getChildren().add(groupsListView);  //添加listView到页面上
		
		//联系人tableView的布局
		personsTableView.setLayoutX(206.0);
		personsTableView.setLayoutY(63.0);
		personsTableView.setPrefHeight(567.0);
		personsTableView.setPrefWidth(1450.0);
		//联系人tableView的列名
		personsTableView.getColumns().addAll(isSelected,isCollected,name,telephone,qq,wechat,email,weibo,blog,github,address,groupsNames);
		isSelected.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
		isSelected.setMaxWidth(30.0);
		isCollected.setCellValueFactory(new PropertyValueFactory<>("isCollected"));
		isCollected.setMaxWidth(45.0);
		isCollected.setStyle("-fx-alignment:center");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.setMaxWidth(50.0);
		telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
		telephone.setMinWidth(100.0);
		qq.setCellValueFactory(new PropertyValueFactory<>("qq"));
		qq.setMinWidth(100.0);
		wechat.setCellValueFactory(new PropertyValueFactory<>("wechat"));
		wechat.setMinWidth(100.0);
		email.setCellValueFactory(new PropertyValueFactory<>("email"));
		email.setMinWidth(100.0);
		weibo.setCellValueFactory(new PropertyValueFactory<>("weibo"));
		weibo.setMinWidth(100.0);
		blog.setCellValueFactory(new PropertyValueFactory<>("blog"));
		blog.setMinWidth(100.0);
		github.setCellValueFactory(new PropertyValueFactory<>("github"));
		github.setMinWidth(100.0);
		address.setCellValueFactory(new PropertyValueFactory<>("address"));
		address.setMinWidth(120.0);
		groupsNames.setCellValueFactory(new PropertyValueFactory<>("groupsNames"));
		groupsNames.setMinWidth(135.0);
		pane.getChildren().add(personsTableView);  //添加到页面上
		
		//回车之后自动搜索
		keyword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER){
					searchBtn.fire();
				}
			}
		});
	}
	
	//刷新页面上的联系组信息
	public static void freshContactGroups(){
		groupsListView.getItems().clear();  //清除页面上现有的联系组
		FileHandler.readGroupsCache();  //读取联系组缓存
		Iterator iterator = new ContactGroup().getInstance().entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			ContactGroup group = (ContactGroup) entry.getValue();
			MainController.groupsListView.getItems().add(group);
		}
		try{
			groupsListView.setCellFactory(CheckBoxListCell.forListView(new Callback<ContactGroup, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(ContactGroup  group){
					BooleanProperty observable = new SimpleBooleanProperty();
					observable.addListener((obs,wasSelected,isNowSelected)->
						{
							if (isNowSelected){
								groupsForDel.add(group.getId());
							}else{
								groupsForDel.remove(group.getId());
							}
						}
					);
					return observable;
				}
			}));
			groupsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ContactGroup>() {
	            public void changed(ObservableValue<? extends ContactGroup> observable,
	            		ContactGroup oldValue, ContactGroup newValue) {
		            	try {
		            		setAddressBookStatus(3);
		            		MainController.nowGroupId = newValue.getId();
		            		freshContactPersons(newValue.getId());
						} catch (NullPointerException e) {
						}
		            		
		            }
	          });
		}catch(NullPointerException e){
		}
	}
	
	//刷新页面上的联系人信息
	public static void freshContactPersons(String groupId){
				//所有联系人
				allPersonsLabel.setText(String.valueOf(ContactPerson.getInstance().size()));
				//未分组联系人
				int personCounter = 0;
				Iterator iterator1 = ContactPerson.getInstance().entrySet().iterator();
				while(iterator1.hasNext()){
					Map.Entry entry = (Map.Entry)iterator1.next();
					ContactPerson person = (ContactPerson) entry.getValue();
					if (person.getGroupIds().equals("")){
						personCounter++;
					} 
				}
				noGroupPersonsLabel.setText(String.valueOf(personCounter));
		
		personsSelected.clear();
		personsTableView.getItems().clear(); //清除现有的联系人
		persons.removeAll();
		Iterator iterator = ContactPerson.getInstance().entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			ContactPerson contactPerson = (ContactPerson) entry.getValue();
			Person person = new Person(
					contactPerson.getId(),
					contactPerson.getUserId(),
					contactPerson.getGroupIds(), 
					contactPerson.getIsCollected(),
					contactPerson.getName(), 
					contactPerson.getTelephone(),
					contactPerson.getQq(),
					contactPerson.getWechat(),
					contactPerson.getEmail(), 
					contactPerson.getWeibo(), 
					contactPerson.getBlog(), 
					contactPerson.getGithub(), 
					contactPerson.getAddress(),
				    contactPerson.getGroupsName()
					);
			if (addressBookStatus == 1){   				//所有联系人
				persons.add(person);
				setAddressBookStatus(1);
				setNowGroupId("");
			}else if (addressBookStatus == 3){   		//指定组联系人
				String[] groupIdsArr = contactPerson.getGroupIds().split("_");
				for (int i = 0; i <groupIdsArr.length; i++ ){
						if (groupId.equals(groupIdsArr[i])){
							setAddressBookStatus(3);
							setNowGroupId(groupId);
							persons.add(person);
							break;
						}
				}
			}else{  													//未分组联系人
				if (person.getGroupIds().equals("")){ 
					setAddressBookStatus(2);
					setNowGroupId("");
					persons.add(person);
				}
			}
		}
		personsTableView.setItems(persons);
	}
	
	/*用于给tableView展示的一个静态Person类*/
	public static class Person {
		private  String id;
		private  String userId;
		private  String groupIds;
		private  CheckBox isSelected;
		private  SimpleStringProperty isCollected; 
        private  SimpleStringProperty name;
        private  SimpleStringProperty telephone;
        private  SimpleStringProperty qq;
        private  SimpleStringProperty wechat;
        private  SimpleStringProperty email;
        private  SimpleStringProperty weibo;
        private  SimpleStringProperty blog;
        private  SimpleStringProperty github;
        private  SimpleStringProperty address;
        private  SimpleStringProperty groupsNames;
        
        private Person(String id,String userId,String groupIds,String isCollected,String name, String telephone,String qq,String wechat, String email,String weibo,String blog,String github,String address,String groupsNames) {
            this.id = id;
            this.userId = userId;
            this.groupIds = groupIds;
            this.isSelected = new CheckBox();
            this.isCollected = new SimpleStringProperty(isCollected);
        	this.name = new SimpleStringProperty(name);
            this.telephone = new SimpleStringProperty(telephone);
            this.qq = new SimpleStringProperty(qq);
            this.wechat = new SimpleStringProperty(wechat);
            this.email = new SimpleStringProperty(email);
            this.weibo = new SimpleStringProperty(weibo);
            this.blog = new SimpleStringProperty(blog);
            this.github = new SimpleStringProperty(github);
            this.address = new SimpleStringProperty(address);
            this.groupsNames = new SimpleStringProperty(groupsNames);
            
            this.isSelected.selectedProperty().addListener(new ChangeListener<Boolean>() {
            	@Override
            	public void changed(ObservableValue<? extends Boolean> ov,Boolean notSeleted,Boolean selected){
            		if (selected){
            			MainController.personsSelected.add(getId());
            		}else{
            			MainController.personsSelected.remove(getId());
            		}
            	}
            });
        }
        public String getId() {
			return id;
		}
		public String getUserId() {
			return userId;
		}
		public String getGroupIds() {
			return groupIds;
		}
		public CheckBox getIsSelected(){
			return isSelected;
		}
		public String getIsCollected(){
			String retStr = "";
			if (isCollected.get().equals("1")){
				 retStr = "√";
			}
			return retStr ;
		}
		public String getName() {
			return name.get();
		}
		public String getTelephone() {
			return telephone.get();
		}
		public String getQq() {
			return qq.get();
		}
		public String getWechat() {
			return wechat.get();
		}
		public String getEmail() {
			return email.get();
		}
		public String getWeibo() {
			return weibo.get();
		}
		public String getBlog() {
			return blog.get();
		}
		public String getGithub() {
			return github.get();
		}
		public String getAddress() {
			return address.get();
		}
		public String getGroupsNames() {
			String retStr = "未分组";
			if (!groupsNames.get().equals("")){
				retStr = groupsNames.get();
			}
			return retStr;
		}
    }
	/*静态Person类结束*/
	
	//全部联系人
	@FXML
	public void allPersons(){
		setAddressBookStatus(1);
		setNowGroupId("");
		freshContactPersons("");
	}
	//未分组联系人
	@FXML
	public void noGroupPersons(){
		setAddressBookStatus(2);
		setNowGroupId("");
		freshContactPersons("");
	}
	
	/*对联系组的操作*/
	//添加联系组
	@FXML
	public void addGroup(){
		try {
			AddGroupController addGroupController = new AddGroupController();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//删除联系组
	@FXML
	public void delGroup(){
		/*
		//废弃的弹窗删除组,改为在主页面直接删除
		try {
			DelGroupController delGroupController = new DelGroupController();
		}catch(Exception e) {
			e.printStackTrace();
		}
		*/
		Iterator<String> iterator = groupsForDel.iterator();
		if (iterator.hasNext()){
			while(iterator.hasNext()){
				String tmpGroupId = iterator.next();
				ContactGroup.getInstance().remove(tmpGroupId);
			}
			
			Iterator iterator1 =ContactPerson.getInstance().entrySet().iterator(); 
			while(iterator1.hasNext()){
				Map.Entry entry = (Map.Entry) iterator1.next();
				ContactPerson person = (ContactPerson) entry.getValue();
				String[] personGroupArr = person.getGroupIds().split("_");
				String newGroupIds = "";
				for (int i=0; i < personGroupArr.length; i++){
					for(int j = 0; j < groupsForDel.size(); j++){
						if (!personGroupArr[i].equals(groupsForDel.get(j))){
							newGroupIds+=personGroupArr[i];
							System.out.println("newgroupIds:  "+newGroupIds);
							System.out.println("groupForDEL:  "+groupsForDel.get(j));
							if(i < personGroupArr.length-1 && personGroupArr.length >= 1 ){
								newGroupIds+="_";
							}
						}
					}
				}
				System.out.println("last:  "+newGroupIds);
				ContactPerson.getInstance().get(person.getId()).setGroupIds(newGroupIds);
			}
			//删除成功后写入缓存
			FileHandler.writeGroupsCache();
			//删除成功后刷新组groupsListView
			freshContactGroups();
			if(getAddressBookStatus() == 1){
				setNowGroupId("");
				freshContactPersons("");
			}else if (getAddressBookStatus() == 2){
				setNowGroupId("");
				freshContactPersons("");
			}else{
				freshContactPersons(getNowGroupId());
			}
			//弹出删除成功的弹窗
			try {
				AlertController alertController = new AlertController("删除组成功!");
			} catch (Exception e) {
			}
		}else{
			//弹出删除失败的弹窗
			try {
				AlertController alertController = new AlertController("未选中组!");
			} catch (Exception e) {
			}
		}
	}
	
	/*对联系人的操作*/
	//添加联系人
	@FXML
	public void addPerson(){
		try {
			AddPersonController addPersonController = new AddPersonController();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//编辑联系人
	@FXML
	public void editPerson(){
		try {
			if(personsSelected.size() > 1){
				try {
					AlertController alertController = new AlertController("一次只能编辑一个联系人!");
				} catch (Exception e) {
				}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
			}else if(personsSelected.size() == 0){
				try {
					AlertController alertController = new AlertController("请选择要编辑的联系人!");
				} catch (Exception e) {
				}
			}else{
				EditPersonController editPersonController = new EditPersonController(personsSelected.get(0));
				personsSelected.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//删除联系人
	@FXML
	public void delPerson(){
		Iterator<String> iterator = personsSelected.iterator();
		if (iterator.hasNext()){
			while(iterator.hasNext()){
				String PersonIdForDel = iterator.next();
				ContactPerson.getInstance().remove(PersonIdForDel);
			}
			//删除成功后写入缓存
			FileHandler.writePersonsCache();
			//删除成功后刷新组groupsListView
			freshContactPersons("");
			//弹出删除成功的弹窗
			try {
				AlertController alertController = new AlertController("删除联系人成功!");
			} catch (Exception e) {
			}
		}else{
			try {
				AlertController alertController = new AlertController("请选中要删除的联系人!");
			} catch (Exception e) {
			}
		}
	}
	
	//收藏联系人
	@FXML
	public void collect(){
		Iterator<String> iterator = personsSelected.iterator();
		int flag = 1; //默认为收藏
		if(iterator.hasNext()){
			while(iterator.hasNext()){
				String personId = iterator.next();
				String isCollectedNow = ContactPerson.getInstance().get(personId).getIsCollected();
				if (isCollectedNow.equals("1")){
					ContactPerson.getInstance().get(personId).setIsCollected("0");
					MainController.freshContactPersons("");
					flag = 0;
				}else if(isCollectedNow.equals("0")){
					ContactPerson.getInstance().get(personId).setIsCollected("1");
					MainController.freshContactPersons("");
					flag = 1;
				}
			}
			if(flag == 1){
				try {
					AlertController alertController = new AlertController("收藏成功!");
				} catch (Exception e) {
				}
			}else{
				try {
					AlertController alertController = new AlertController("取消收藏成功!");
				} catch (Exception e) {
				}
			}
			personsSelected.clear();
		}else{
			try {
				AlertController alertController= new AlertController("请选中联系人进行收藏!");
			} catch (Exception e) {
			}
		}
	}
	
	//查询
	@FXML
	public void search(){
		String keyword = this.keyword.getText();
		
		String chinese = PinyinHandler.cn2Spell(keyword);
		
		personsTableView.getItems().clear();
		persons.clear();
		Iterator iterator = ContactPerson.getInstance().entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			ContactPerson contactPerson = (ContactPerson) entry.getValue();
			if(		
					//非中文关键字匹配
					contactPerson.getName().indexOf(keyword)>=0  && !contactPerson.getName().equals("")||
					contactPerson.getTelephone().indexOf(keyword) >=0 && !contactPerson.getTelephone().equals("") ||
					contactPerson.getQq().indexOf(keyword) >0 && !contactPerson.getQq().equals("")  ||
					contactPerson.getWechat().indexOf(keyword) >=0 && !contactPerson.getWechat().equals("")  ||
					contactPerson.getEmail().indexOf(keyword)>=0 && !contactPerson.getEmail().equals("")  ||
					contactPerson.getWeibo().indexOf(keyword) >= 0 && !contactPerson.getWeibo().equals("")  ||
					contactPerson.getBlog().indexOf(keyword)  >=0 && !contactPerson.getBlog().equals("")  ||
					contactPerson.getGithub().indexOf(keyword) >=0 && !contactPerson.getGithub().equals("")  ||
					contactPerson.getAddress().indexOf(keyword) >= 0 && !contactPerson.getAddress().equals("")  ||
					contactPerson.getGroupsName().indexOf(keyword)>= 0 && !contactPerson.getGroupsName().equals("") ||
							//中文关键字匹配
							PinyinHandler.cn2Spell(contactPerson.getName()).indexOf(chinese)>=0  && !contactPerson.getName().equals("")||
							PinyinHandler.cn2Spell(contactPerson.getTelephone()).indexOf(chinese) >=0 && !contactPerson.getTelephone().equals("") ||
							PinyinHandler.cn2Spell(contactPerson.getQq()).indexOf(chinese) >0 && !contactPerson.getQq().equals("")  ||
							PinyinHandler.cn2Spell(contactPerson.getWechat()).indexOf(chinese) >=0 && !contactPerson.getWechat().equals("")  ||
							PinyinHandler.cn2Spell(contactPerson.getEmail()).indexOf(chinese)>=0 && !contactPerson.getEmail().equals("")  ||
							PinyinHandler.cn2Spell(contactPerson.getWeibo()).indexOf(chinese) >= 0 && !contactPerson.getWeibo().equals("")  ||
							PinyinHandler.cn2Spell(contactPerson.getBlog()).indexOf(chinese) >=0 && !contactPerson.getBlog().equals("")  ||
							PinyinHandler.cn2Spell(contactPerson.getGithub()).indexOf(chinese) >=0 && !contactPerson.getGithub().equals("")  ||
							PinyinHandler.cn2Spell(contactPerson.getAddress()).indexOf(chinese) >= 0 && !contactPerson.getAddress().equals("")  ||
							PinyinHandler.cn2Spell(contactPerson.getGroupsName()).indexOf(chinese) >= 0 && !contactPerson.getGroupsName().equals("") ||
					keyword.equals("") 
							){
				Person person = new Person(
						contactPerson.getId(),
						contactPerson.getUserId(),
						contactPerson.getGroupIds(), 
						contactPerson.getIsCollected(),
						contactPerson.getName(), 
						contactPerson.getTelephone(),
						contactPerson.getQq(),
						contactPerson.getWechat(),
						contactPerson.getEmail(), 
						contactPerson.getWeibo(), 
						contactPerson.getBlog(), 
						contactPerson.getGithub(), 
						contactPerson.getAddress(),
					    contactPerson.getGroupsName()
						);
				persons.add(person);
			}
		}
		personsTableView.setItems(persons);
	}
	
	//导出CSV	
	@FXML      
	public void export_CSV(){      
		ImportExportController in = new ImportExportController();  
		in.exportCSVScene();
	}
	//导入csv      
	@FXML      
	public void import_CSV(){   
		ImportExportController in = new ImportExportController();      
		in.importCSV();     
		FileHandler.writePersonsCache();      freshContactPersons("");      
		}            
	//导出vcard      
	@FXML   
	public void export_VCF(){  
		ImportExportController in = new ImportExportController();         
		in.exportVCFScene();  
		}     
	//导入   
	@FXML   
	public void import_VCF() throws FileNotFoundException{    
		ImportExportController in = new ImportExportController();      
		in.importVCF();         
	FileHandler.writePersonsCache();        
	freshContactPersons("");   
	}                                    
	
	@FXML
	public void aboutUs(){
		try {
			AboutUsController aboutUsController = new AboutUsController();
		} catch (Exception e) {
		}
		
	}

	public static String getNowGroupId() {
		return nowGroupId;
	}

	public static void setNowGroupId(String nowGroupId) {
		MainController.nowGroupId = nowGroupId;
	}

	public static int getAddressBookStatus() {
		return addressBookStatus;
	}

	public static void setAddressBookStatus(int addressBookStatus) {
		MainController.addressBookStatus = addressBookStatus;
	}
	
}
