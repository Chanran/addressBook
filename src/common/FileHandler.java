package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import model.ContactGroup;
import model.ContactPerson;
import model.ContactUser;
import tools.CreateMD5;

/*文件读写*/
public class FileHandler {
	
	/*未加密的缓存文件(以后放再完善,放在服务器端)*/
	private static String personsPath = "data/persons/";		//联系人缓存文件目录
	private static String groupsPath = "data/groups/";			//联系组缓存文件目录
	private static File CacheDir = new File("data");
	private static File CachePersonsDir = new File("data/persons");
	private static File CacheGroupsDir = new File("data/groups");
	private static File personsCache = new File(personsPath);
	private static File groupsCache = new File(groupsPath);
	
	//防止创建实例
	private FileHandler(){
		
	}

	/*读取联系人缓存文件*/
	public static void readPersonsCache(){
		try{
			if (!CacheDir.exists()){
				CacheDir.mkdirs();
			}
			if (!CachePersonsDir.exists()){
				CachePersonsDir.mkdirs();
			}
			if (!personsCache.exists()){
				personsCache.createNewFile();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			String fileName = CreateMD5.getMd5(ContactUser.getLoginUserId()+ContactUser.getLoginUserName(), 16);
			try{
				CsvReader reader = new CsvReader(personsPath+fileName+".csv",',',Charset.forName("UTF-8"));
				reader.readHeaders();
				while(reader.readRecord()){
					ContactPerson insertPerson = new ContactPerson();
					insertPerson.setId(reader.get("id"));
					insertPerson.setUserId(reader.get("userId"));
					insertPerson.setGroupIds(reader.get("groupIds"));
					insertPerson.setName(reader.get("name"));
					insertPerson.setTelephone(reader.get("telephone"));
					insertPerson.setQq(reader.get("qq"));
					insertPerson.setWechat(reader.get("wechat"));
					insertPerson.setWeibo(reader.get("weibo"));
					insertPerson.setBlog(reader.get("blog"));
					insertPerson.setGithub(reader.get("github"));
					insertPerson.setEmail(reader.get("email"));
					insertPerson.setAddress(reader.get("address"));
					insertPerson.setIsCollected(reader.get("isCollected"));
					ContactPerson.getInstance().put(reader.get("id"), insertPerson);
				}
				reader.close();
			}catch(FileNotFoundException e){
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void readPersonsCache(String groupId){
		try{
			if (!CacheDir.exists()){
				CacheDir.mkdirs();
			}
			if (!CachePersonsDir.exists()){
				CachePersonsDir.mkdirs();
			}
			if (!personsCache.exists()){
				personsCache.createNewFile();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			String fileName = CreateMD5.getMd5(ContactUser.getLoginUserId()+ContactUser.getLoginUserName(), 16);
			try{
				CsvReader reader = new CsvReader(personsPath+fileName+".csv",',',Charset.forName("UTF-8"));
				reader.readHeaders();
				while(reader.readRecord()){
					ContactPerson insertPerson = new ContactPerson();
					insertPerson.setId(reader.get("id"));
					insertPerson.setUserId(reader.get("userId"));
					insertPerson.setGroupIds(reader.get("groupIds"));
					insertPerson.setName(reader.get("name"));
					insertPerson.setTelephone(reader.get("telephone"));
					insertPerson.setQq(reader.get("qq"));
					insertPerson.setWechat(reader.get("wechat"));
					insertPerson.setWeibo(reader.get("weibo"));
					insertPerson.setBlog(reader.get("blog"));
					insertPerson.setGithub(reader.get("github"));
					insertPerson.setEmail(reader.get("email"));
					insertPerson.setAddress(reader.get("address"));
					insertPerson.setIsCollected(reader.get("isCollected"));
					
					String[]  groupIdArr = reader.get("groupIds").split("_");
					for (int i = 0; i < groupIdArr.length; i++){
						if(groupId.equals(groupIdArr[i])){
							ContactPerson.getInstance().put(reader.get("id"), insertPerson);
							break;
						}
					}
				}
				reader.close();
			}catch(FileNotFoundException e){
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/*写入联系人缓存文件*/
	public static void writePersonsCache(){
		try{
			if (!CacheDir.exists()){
				CacheDir.mkdirs();
			}
			if (!CachePersonsDir.exists()){
				CachePersonsDir.mkdirs();
			}
			if (!personsCache.exists()){
				personsCache.createNewFile();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			String fileName = CreateMD5.getMd5(ContactUser.getLoginUserId()+ContactUser.getLoginUserName(), 16);
			
			CsvWriter writer = new CsvWriter(personsPath+fileName+".csv",',',Charset.forName("UTF-8"));
			writer.write("id");
			writer.write("userId");
			writer.write("groupIds");
			writer.write("name");
			writer.write("telephone");
			writer.write("qq");
			writer.write("wechat");
			writer.write("weibo");
			writer.write("blog");
			writer.write("github");
			writer.write("email");
			writer.write("address");
			writer.write("isCollected");
			writer.endRecord();
			
			//使用iterator迭代器
			Iterator personIterator =  ContactPerson.getInstance().entrySet().iterator();
			while(personIterator.hasNext()){
				Map.Entry<String,ContactPerson> entry = (Entry<String, ContactPerson>) personIterator.next();
				ContactPerson person = entry.getValue();
				writer.write(person.getId());
				writer.write(person.getUserId());
				writer.write(person.getGroupIds());
				writer.write(person.getName());
				writer.write(person.getTelephone());
				writer.write(person.getQq());
				writer.write(person.getWechat());
				writer.write(person.getWeibo());
				writer.write(person.getBlog());
				writer.write(person.getGithub());
				writer.write(person.getEmail());
				writer.write(person.getAddress());
				writer.write(person.getIsCollected());
				writer.endRecord();
			}
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*读取联系组缓存*/
	public static void readGroupsCache(){
		try{
			if (!CacheDir.exists()){
				CacheDir.mkdirs();
			}
			if (!CacheGroupsDir.exists()){
				CacheGroupsDir.mkdirs();
			}
			if (!groupsCache.exists()){
				groupsCache.createNewFile();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			String fileName = CreateMD5.getMd5(ContactUser.getLoginUserId()+ContactUser.getLoginUserName(), 16);
			try{
				CsvReader reader = new CsvReader(groupsPath+fileName+".csv",',',Charset.forName("UTF-8"));
				reader.readHeaders();
				//String lastId = ContactPerson.getLastId();
				while(reader.readRecord()){
					ContactGroup insertGroup = new ContactGroup();
					insertGroup.setId(reader.get("id"));
					insertGroup.setUserId(reader.get("userId"));
					insertGroup.setName(reader.get("name"));
					ContactGroup.getInstance().put(reader.get("id"), insertGroup);
				}
				reader.close();
			}catch(FileNotFoundException e){
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*写入联系组缓存文件*/
	public static void writeGroupsCache(){
		try{
			if (!CacheDir.exists()){
				CacheDir.mkdirs();
			}
			if (!CacheGroupsDir.exists()){
				CacheGroupsDir.mkdirs();
			}
			if (!groupsCache.exists()){
				groupsCache.createNewFile();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String fileName = CreateMD5.getMd5(ContactUser.getLoginUserId()+ContactUser.getLoginUserName(), 16);
			
			CsvWriter writer = new CsvWriter(groupsPath+fileName+".csv",',',Charset.forName("UTF-8"));
			writer.write("id");
			writer.write("userId");
			writer.write("name");
			writer.endRecord();
			
			//使用iterator迭代器
			Iterator groupIterator =  ContactGroup.getInstance().entrySet().iterator();
			while(groupIterator.hasNext()){
				Map.Entry<String,ContactGroup> entry = (Entry<String, ContactGroup>) groupIterator.next();
				ContactGroup group = entry.getValue();
				writer.write(group.getId());
				writer.write(group.getUserId());
				writer.write(group.getName());
				writer.endRecord();
			}
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
