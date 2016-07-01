package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactPerson {
	
	//全局的联系人对象
	private static Map<String , ContactPerson> personsMap = new HashMap<>();
	private static String lastId = "0";
	
	private String id; 				//联系人id
	private String userId;  		//用户id
	private String groupIds; 		//联系组id
	private String name; 			//联系人姓名
	private String telephone; 		//联系人手机
	private String qq; 				//联系人qq
	private String wechat; 			//联系人微信
	private String weibo;			//联系人微博
	private String blog;			//联系人博客
	private String github;			//联系人github
	private String email;			//联系人邮箱
	private String address;			//联系人地址
	private String isCollected;    //是否收藏
	
	public ContactPerson(){
		
	}
	public ContactPerson(String id,String userId,String groupId,String name){
		this.id = id;
		this.userId = userId;
		this.groupIds = groupId;
		this.name = name;
	}
	public ContactPerson(String id,String userId,String groupId,String name,String telephone,String qq,String wechat,String weibo,String blog,String github,String email,String address){
		this.id = id;
		this.name = name;
		this.telephone = telephone;
		this.qq = qq;
		this.wechat = wechat;
		this.weibo = weibo;
		this.blog = blog;
		this.github = github;
		this.email = email;
		this.address =address;
	}

	/*重写toString用于在tableView显示*/
	@Override
	public String toString(){
		return this.name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		if(Integer.parseInt(this.id) > Integer.parseInt(getLastId())){
			setLastId(this.id);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getGithub() {
		return github;
	}
	public void setGithub(String github) {
		this.github = github;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	//用来在界面展示
	public String getGroupsName(){
		String groupsNames = "";
		if (!groupIds.equals("")){
			String[] groupIdsArr = this.groupIds.split("_");
			for (int i = 0; i < groupIdsArr.length; i++){	
				try {
					groupsNames+=ContactGroup.getInstance().get(groupIdsArr[i]).getName();
				} catch (NullPointerException e) {
				}
				
				if(i < groupIdsArr.length-1 && groupIdsArr.length !=1){
					groupsNames+="|";
				}
			}
		}
		return groupsNames;
	}
	
	public static Map<String, ContactPerson> getInstance() {
		return personsMap;
	}
	
	//返回id对应的ContactPerson(联系人)
	public ContactPerson getPerson(String id){
		return ContactPerson.getInstance().get(id);
	}
	//设置id对应的ContactPerson(联系人)
	public void setPerson(String id,ContactPerson person){
		ContactPerson.getInstance().put(id, person);
	}
	//删除id对应的ContactPerson(联系人)
	public void delPerson(String id){
		ContactGroup.getInstance().remove(id);
	}
	public static String getLastId() {
		return lastId;
	}
	public static void setLastId(String lastId) {
		ContactPerson.lastId = lastId;
	}
	public String getIsCollected() {
		return isCollected;
	}
	public void setIsCollected(String isCollected) {
		this.isCollected = isCollected;
	}
}
