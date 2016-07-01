package controllers;

import java.io.IOException;
import java.sql.*;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FindPwdController extends Pane {

	@FXML
	private TextField name;

	private Stage findPwdStage = new Stage();
	private Parent root = null;

	public FindPwdController() {
		FXMLLoader forgetFxml = new FXMLLoader(getClass().getResource("../view/findPwd.fxml"));
		forgetFxml.setRoot(this);
		forgetFxml.setController(this);
		try {
			root = forgetFxml.load();
			findPwdStage.setScene(new Scene(root));
			findPwdStage.show();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	public void find() {
		String username = this.name.getText();
		if (username.equals("")) {
			System.out.println("用户名不能为空!");
			try {
				AlertController alertController = new AlertController("用户名不能为空!");
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
				ResultSet query1 = statement.executeQuery("select name,password from user");
				boolean isNameExist = false;
				String dbPassword = null;
				String dbSalt = null;
				while (query1.next()) {
					if (username.equals(query1.getString("name"))) {
						isNameExist = true;
						dbPassword = query1.getString("password");
						break;
					}
				}

				/* 用户存在,显示密码 */
				if (isNameExist) {
					try {
						ResetPwdController showPwdController = new ResetPwdController(username, dbPassword);
						this.findPwdStage.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("用户不存在!");
					/* 弹出用户不存在的警示框 */
					try {
						AlertController alertController1 = new AlertController("用户不存在!");
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

	@FXML
	public void reset() {
		this.name.setText("");
	}

}
