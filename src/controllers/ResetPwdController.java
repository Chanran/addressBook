package controllers;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tools.CreateMD5;
import tools.CreateRandom;

public class ResetPwdController extends Pane {

	@FXML
	private Label name;
	@FXML
	private PasswordField pwd1;
	@FXML
	private PasswordField pwd2;
	
	private Stage showPwdStage = new Stage();
	private Parent root = null;
	
	public ResetPwdController(String name,String password){
		FXMLLoader showPwdFxml = new FXMLLoader(getClass().getResource("../view/resetPwd.fxml"));
		showPwdFxml.setRoot(this);
		showPwdFxml.setController(this);
		try{
			root = showPwdFxml.load();
			showPwdStage.setScene(new Scene(root));
			this.name.setText(name);
			showPwdStage.show();
		}catch(IOException exception){
			throw new RuntimeException(exception);
		}
	}
	
	@FXML
	public void resetPwd(){
		
		String username = this.name.getText();
		String password = this.pwd1.getText();
		String passwordAgain = this.pwd2.getText();
		
		/*判断表单是否为空*/
		if (username.equals("") || password.equals("")|| passwordAgain.equals("")){
			System.out.println("用户名或密码不能为空!");
			try{
				AlertController alertController = new AlertController("用户名或密码不能为空!");
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			
			/* 两次密码是否相同 */
			if (!password.equals(passwordAgain)) {
				System.out.println("两次密码不相同!");
				try {
					AlertController alertController2 = new AlertController("两次密码不相同!");
					// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				// mysql数据库连接:jdbc
				Connection conn = null;
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://localhost:3306/addressBook?"
						+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
				try {
					Class.forName(driver);
					Class.forName(driver).newInstance();
					System.out.println("成功加载mysql驱动程序"); // 加载驱动
					conn = DriverManager.getConnection(url);
					if (!conn.isClosed()) {
						System.out.println("成功连接mysql数据库"); // 判断是否连接成功
					}
					Statement statement = conn.createStatement();

					// 重置密码
					String salt = CreateRandom.getRandomChars(32);
					String encryptPassword = CreateMD5.getMd5(password + salt, 32);
					String sql = "update user set password = '"+encryptPassword+"',salt = '"+salt+"' where name = '"+username+"'";
					int result = statement.executeUpdate(sql);
					if (result != 0) {
						System.out.println("重置成功!");
						try {
							AlertController alertController5 = new AlertController("重置成功!");
							// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
						} catch (Exception e) {
							e.printStackTrace();
						}
						this.showPwdStage.close();
					} else {
						System.out.println("重置失败!");
						try {
							AlertController alertController5 = new AlertController("重置失败!");
							// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
						} catch (Exception e) {
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
	}
	@FXML
	public void reset() {
		this.name.setText("");
		this.pwd1.setText("");
		this.pwd2.setText("");
	}
}
