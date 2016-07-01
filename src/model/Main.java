package model;
	
import common.FileHandler;
import controllers.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
	
	public Main() {
		checkRun();
	}
	
	//退出的时候自动将联系组和联系人的信息写入本地缓存
	private void checkRun(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				try {
					FileHandler.writeGroupsCache();
					FileHandler.writePersonsCache();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	
	@Override
	public void start(Stage loginStage) {
		try {
			LoginController loginController =  new LoginController();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
