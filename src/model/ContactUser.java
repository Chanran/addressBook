package model;


public class ContactUser {
	
	//全局的用户id
	private static String loginUserId;
	//全局的用户名
	private static String loginUserName;
	
	
	private ContactUser(){
		
	}
	
	/*登录*/
	public void login(String loginUserId,String loginUserName){
		setLoginUserId(loginUserId);
		setLoginUserName(loginUserName);
	}
	/*退出登录*/
	public void logout(){
		setLoginUserId("");
		setLoginUserName("");
	}
	public static String getLoginUserId() {
		return loginUserId;
	}
	public static void setLoginUserId(String loginUserId) {
		ContactUser.loginUserId = loginUserId;
	}
	public static String getLoginUserName() {
		return loginUserName;
	}
	public static void setLoginUserName(String loginUserName) {
		ContactUser.loginUserName = loginUserName;
	}
	
}
