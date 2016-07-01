package controllers;

import common.FileHandler;
import static controllers.MainController.freshContactPersons;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import model.ContactPerson;
import model.ContactUser;

public class ImportExportController extends Pane {

	@FXML
	private TextField fileName;
	@FXML
	private TextField fileName1;

	private static Stage exportStage = new Stage();
	private static Parent root = null;
	private static Stage importStage = new Stage();
	private static Parent root1 = null;

	// 导出CSV的stage
	public void exportCSVScene() {
		FXMLLoader exportCSVFxml = new FXMLLoader(getClass().getResource("../view/exportCSV.fxml"));
		exportCSVFxml.setRoot(this);
		exportCSVFxml.setController(this);
		try {
			root = exportCSVFxml.load();
			exportStage.setScene(new Scene(root));
			exportStage.setTitle("导出CSV");
			exportStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 导出VCard的stage
	public void exportVCFScene() {
		FXMLLoader exportVCFFxml = new FXMLLoader(getClass().getResource("../view/exportVCF.fxml"));
		exportVCFFxml.setRoot(this);
		exportVCFFxml.setController(this);
		try {
			root = exportVCFFxml.load();
			exportStage.setScene(new Scene(root));
			exportStage.setTitle("导出VCF");
			exportStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 导出CSV
	@FXML
	public void exportCSV() {
		String filepath = this.fileName.getText();
		File folder = new File("./export/");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		try {
			FileWriter fw = new FileWriter("./export/" + filepath + ".csv");
			String header = "姓名,手机,QQ,微信,email,微博,博客,github,地址,分组,flag\r\n";
			fw.write(header);
			StringBuilder strs = new StringBuilder();
			Iterator iterator = ContactPerson.getInstance().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				ContactPerson contactPerson = (ContactPerson) entry.getValue();
				strs = new StringBuilder();

				String str = contactPerson.getName().trim() + ",";
				strs.append(str);
				str = contactPerson.getTelephone().trim() + ",";
				strs.append(str);
				str = contactPerson.getQq().trim() + ",";
				strs.append(str);
				str = contactPerson.getWechat().trim() + ",";
				strs.append(str);
				str = contactPerson.getEmail().trim() + ",";
				strs.append(str);
				str = contactPerson.getWeibo().trim() + ",";
				strs.append(str);
				str = contactPerson.getBlog().trim() + ",";
				strs.append(str);
				str = contactPerson.getGithub().trim() + ",";
				strs.append(str);
				str = contactPerson.getAddress().trim() + ",";
				strs.append(str);
				str = ""+",";
				strs.append(str);
				str = "fin\r\n";
				strs.append(str);
				fw.write(strs.toString());
				fw.flush();
			}
			fw.close();
			try {
				AlertController alertController = new AlertController("导出CSV成功！");
				exportStage.close();
			} catch (Exception e) {
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
		
		
	}

	@FXML
	public void shutdownExport() {
		this.exportStage.close();
	}
	
	// 导入CSV
	public void importCSV() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
		try {
			String FilePath = file.getAbsolutePath().replaceAll(".csv", "") + ".csv";

			File file1 = new File(FilePath);
			System.out.println(FilePath);

			try (Scanner input = new Scanner(file1, "UTF-8")) {
				String st = input.nextLine();// 读表头

				while (input.hasNext()) {
					String stx = input.nextLine();
					String[] strs = stx.split(",");

					ContactPerson newPerson = new ContactPerson();

					String insertId = String.valueOf((Integer.valueOf(ContactPerson.getLastId()) + 1));
					newPerson.setId(insertId);
					newPerson.setUserId(ContactUser.getLoginUserId());
					newPerson.setIsCollected("0");
					newPerson.setGroupIds("");

					int i = 0;
					System.out.println(strs[i]);
					if (strs[i] != "") {
						newPerson.setName(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setTelephone(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setQq(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setWechat(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setEmail(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setWeibo(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setBlog(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setGithub(strs[i]);
					}
					i++;
					if (strs[i] != "") {
						newPerson.setAddress(strs[i]);
					}

					ContactPerson.getInstance().put(insertId, newPerson);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
	}

	// 导出VCF
	@FXML
	public void exportVCF() {

		String filepath = this.fileName1.getText();
		File folder = new File("./export/");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File("./export/" + filepath + ".vcf");
		try (PrintWriter output = new PrintWriter(file, "UTF-8")) {
			Iterator iterator = ContactPerson.getInstance().entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				ContactPerson contactPerson = (ContactPerson) entry.getValue();
				String str = "BEGIN:VCARD";
				output.println(str);
				str = "VERSION:3.0";
				output.println(str);
				if (contactPerson.getName().length() > 0) {
					str = "FN:" + contactPerson.getName();
					output.println(str);
				}
				if (contactPerson.getTelephone().length() > 0) {
					str = "TEL;TYPE=CELL:" + contactPerson.getTelephone();
					output.println(str);
				}
				if (contactPerson.getEmail().length() > 0) {
					str = "EMAIL;TYPE=HOME:" + contactPerson.getEmail();
					output.println(str);
				}
				if (contactPerson.getAddress().length() > 0) {
					str = "ADR;TYPE=HOME;CHARSET=UTF-8:;;" + contactPerson.getAddress() + ";;;;";
					output.println(str);
				}
				str = "END:VCARD";
				output.println(str);
			}
			try {
				AlertController alertController = new AlertController("导出VCF成功！");
				exportStage.close();
			} catch (Exception e) {
			}
		} catch (Exception ex) {
		}

	}

	// 导入VCF
	public void importVCF() throws FileNotFoundException {

		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
		try {
			String FilePath = file.getAbsolutePath().replaceAll(".vcf", "") + ".vcf";

			File file1 = new File(FilePath);
			InputStream in = new FileInputStream(FilePath);
			ContactPerson newPerson = new ContactPerson();
			String name = "";
			try (Scanner input = new Scanner(in, "UTF-8")) {
				while (input.hasNextLine()) {
					String s = input.nextLine();
					if (s.toUpperCase().contains("BEGIN:VCARD")) {
						boolean sign = false;
						String phones = "";
						boolean isTwoPhone = false;
						while (input.hasNext()) {

							s = input.nextLine();

							if (s.toUpperCase().contains("FN:")) {
								newPerson = new ContactPerson();
								name = extract(s);
								newPerson.setName(name);
							}
							if (s.toUpperCase().contains("TEL;TYPE=CELL:")) {
								if (sign) {
									phones = s;
									isTwoPhone = true;
								} else {
									newPerson.setTelephone(extract(s));
									sign = true;
								}
							}

							if (s.toUpperCase().contains("EMAIL;TYPE=WORK") || s.toUpperCase().contains("EMAIL;TYPE=HOME")
									|| s.toUpperCase().contains("EMAIL;TYPE=PREF")) {
								newPerson.setEmail(extract(s));
							}
							if (s.toUpperCase().contains("ADR;TYPE=HOME;CHARSET=UTF-8")
									|| s.toUpperCase().contains("ADR;TYPE=HOME;CHARSET=UTF-8")) {
								newPerson.setAddress(extract(s).replace(";", ""));
							}
							if (s.toUpperCase().contains("END:VCARD")) {
								String insertId = String.valueOf((Integer.valueOf(ContactPerson.getLastId()) + 1));
								newPerson.setId(insertId);
								newPerson.setUserId(ContactUser.getLoginUserId());
								newPerson.setIsCollected("0");
								newPerson.setGroupIds("");
								ContactPerson.getInstance().put(insertId, newPerson);
								break;
							}
						} // while
					} // if
				} // begin
			} // try
		} catch (Exception e) {
		}
		
	}// importvcf


	// 为VCF服务
	public String extract(String str) {
		String[] s = str.split(":");
		String string = s[1];
		return string;
	}

}