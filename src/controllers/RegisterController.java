package controllers;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tools.CreateMD5;
import tools.CreateRandom;

public class RegisterController extends Pane {

	@FXML
	private TextField name;
	@FXML
	private PasswordField pwd1;
	@FXML
	private PasswordField pwd2;
	
	private Stage registerStage = new Stage();
	private Parent root = null;

	public RegisterController() {
		FXMLLoader registerFxml = new FXMLLoader(getClass().getResource("../view/register.fxml"));
		registerFxml.setRoot(this);
		registerFxml.setController(this);
		try {
			root = registerFxml.load();
			registerStage.setScene(new Scene(root));
			registerStage.show();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	public void register() {

		String username = this.name.getText();
		String password = this.pwd1.getText();
		String passwordAgain = this.pwd2.getText();

		/* 表单是否为空 */
		if (username.equals("") || password.equals("") || passwordAgain.equals("")) {
			System.out.println("用户名或密码不能为空!");
			try {
				AlertController alertController1 = new AlertController("用户名或密码不能为空!");
				// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			/* 用户名必须4位以上,密码必须6位以上 */
			if (username.length() < 4 || password.length() < 6 || passwordAgain.length() < 6) {
				System.out.println("用户名必须4位以上,密码必须6位以上!");
				try {
					AlertController alertController2 = new AlertController("用户名必须4位以上,密码必须6位以上!");
					// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				/* 两次密码是否相同 */
				if (!password.equals(passwordAgain)) {
					System.out.println("两次密码不相同!");
					try {
						AlertController alertController3 = new AlertController("两次密码不相同!");
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

						/* 判断用户是否存在 */
						ResultSet query1 = statement.executeQuery("select name from user");
						boolean isNameExist = false;
						while (query1.next()) {
							if (username.equals(query1.getString("name"))) {
								isNameExist = true;
								break;
							}
						}

						/* 用户存在,进行数据库查询,检查密码是否正确 */
						if (isNameExist) {

							System.out.println("用户已存在!");
							/* 弹出用户已存在的警示框 */
							try {
								AlertController alertController4 = new AlertController("用户已存在!");
								// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {

							// 注册
							String salt = CreateRandom.getRandomChars(32);
							String encryptPassword = CreateMD5.getMd5(password + salt, 32);
							String sql = "insert into user (name,password,salt) values ('" + username + "','"
									+ encryptPassword + "','" + salt + "')";
							int result = statement.executeUpdate(sql);
							if (result != 0) {
								System.out.println("注册成功!");
								try {
									AlertController alertController5 = new AlertController("注册成功!");
									// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
									this.registerStage.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								System.out.println("注册失败!");
								try {
									AlertController alertController5 = new AlertController("注册失败!");
									// 这里应该还要做一个直到窗口被关闭才执行下列代码方法,alertController已经有flag值
								} catch (Exception e) {
									e.printStackTrace();
								}
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
	}

	@FXML
	public void reset() {
		this.name.setText("");
		this.pwd1.setText("");
		this.pwd2.setText("");
	}
}
