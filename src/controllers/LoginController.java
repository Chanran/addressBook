package controllers;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ContactUser;
import tools.CreateMD5;

public class LoginController extends Pane {

	@FXML private TextField username;
	@FXML private PasswordField password;
	
	private static Stage loginStage = new Stage();
	private Parent root = null;

	/*
	 * 登录页面控制器
	 * @author blue
	 */
	public LoginController() {
		
		FXMLLoader loginFxml = new FXMLLoader(getClass().getResource("../view/login.fxml"));
		loginFxml.setRoot(this);
		loginFxml.setController(this);
		try{
			root =  loginFxml.load();
			loginStage.setScene(new Scene(root));
			loginStage.setTitle("登录");
			loginStage.show();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	/*
	 * 忘记密码
	 * @author blue
	 */
	@FXML
	protected void forgetPwd() {
		try {
			FindPwdController findPwdController = new FindPwdController();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 注册
	 * @author blue
	 */
	@FXML
	protected void register() {
		try {
			RegisterController registerController = new RegisterController();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 登录
	 * @author blue
	 */
	@FXML
	protected void submit(ActionEvent event) {
		String username = this.username.getText();
		String password = this.password.getText();
		
		if (username.equals("") || password.equals("")){
			System.out.println("用户名或者密码不能为空!");
			try{
				AlertController alertController = new AlertController("用户名或者密码不能为空!");
				//这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			//mysql数据库连接:jdbc
			Connection conn = null;
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/addressBook?"+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
			try {
				Class.forName(driver);
				Class.forName(driver).newInstance();
				System.out.println("成功加载mysql驱动程序");  //加载驱动
				conn = DriverManager.getConnection(url);
				if (!conn.isClosed()) {
					System.out.println("成功连接mysql数据库");  //判断是否连接成功
				}
				Statement statement = conn.createStatement();
				
				/*判断用户是否存在*/
				ResultSet query1 = statement.executeQuery("select id,name,password,salt from user");
				boolean isNameExist = false;
				String dbPassword = null;
				String dbSalt = null;
				String userId = null;
				while(query1.next()){
					if (username.equals(query1.getString("name"))){
						isNameExist = true;
						userId = query1.getString("id");
						dbPassword = query1.getString("password");
						dbSalt = query1.getString("salt");
						break;
					}
				}
				
				/*用户存在,检查密码是否正确*/
				if(isNameExist){
					
					//检查密码是否正确
					String encryptPassword = CreateMD5.getMd5(password+dbSalt, 32);
					if (encryptPassword.equals(dbPassword)){
						System.out.println("登录成功!");
						SuccessController loginController = new SuccessController("登录成功!");
						LoginController.getLoginStage().close();
						/*登录,设置用户全局变量*/
						ContactUser.setLoginUserId(userId);
						ContactUser.setLoginUserName(username);
					
					}else{
						System.out.println("密码错误!");
						AlertController alertController = new AlertController("密码错误!");
					}
					
				}else{
					System.out.println("用户不存在!");
					/*弹出用户不存在的警示框*/
					try{
						AlertController alertController1 = new AlertController("用户不存在!");
						//这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				// 数据库驱动类异常处理
				System.out.println("Sorry,can't find the Driver!");
				e.printStackTrace();
			} catch (SQLException e) {
				// 数据库连接失败异常处理
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@FXML
	protected void reset() {
		username.setText("");
		password.setText("");
	}

	public static Stage getLoginStage() {
		return loginStage;
	}

	public static void setLoginStage(Stage loginStage) {
		LoginController.loginStage = loginStage;
	}

}
