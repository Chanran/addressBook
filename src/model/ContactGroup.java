package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ContactGroup {
	
	//全局的联系组对象
	private static Map<String , ContactGroup> groupsMap = new HashMap<>();
	private static String lastId = "0";
	
	private String id;															//联系组id
	private String userId;														//用户id
	private String name;														//联系组名
	
	public ContactGroup(){                                  
		
	}
	public ContactGroup(String id,String name){
		this.id = id;
		this.name = name;
	}
	
	/*重写toString用于在listView显示*/
	@Override
	public String toString(){
		return this.name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		//当插入的id大于最后一个数据的id时,将插入的id赋给lastId
		if (Integer.parseInt(this.id) > Integer.parseInt(getLastId())){
			setLastId(this.id);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public static Map<String , ContactGroup> getInstance() {
		return groupsMap;
	}
	
	public String getAllGroups(){
		String allGroups = "";
		Iterator iterator = ContactGroup.getInstance().entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			ContactGroup group = (ContactGroup) entry.getValue();
			allGroups+=group.getId();
			if(iterator.hasNext()){
				allGroups+="_";
			}
		}
		return allGroups;
	}
	
	public ContactGroup getGroup(String id){
		return ContactGroup.getInstance().get(id);
	}
	public void setGroup(String id,ContactGroup group){
		ContactGroup.getInstance().put(id, group);
	}
	public void delGroup(String id){
		ContactGroup.getInstance().remove(id);
	}
	public static String getLastId() {
		return lastId;
	}
	public static void setLastId(String lastId) {
		ContactGroup.lastId = lastId;
	}
}
