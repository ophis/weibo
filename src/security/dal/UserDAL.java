package security.dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import weibo4j.model.User;

public class UserDAL extends AbstractDAL{
	
	public void addAll2Timeline(List<User> _userList){
		if(null==_userList || _userList.size()==0) return;

		try {			
			if(_userList.size()>0){
				StringBuffer insertSql = new StringBuffer("INSERT IGNORE INTO User(uid,screen_name,location,gender,url,created_at,name,verified,allow_all_act_msg,allow_all_comment) VALUES");
				for(int i=0;i<_userList.size();i++)
				{
					if(i==_userList.size()-1)
						insertSql.append("(?,?,?,?,?,?,?,?,?,?)");
					else {
						insertSql.append("(?,?,?,?,?,?,?,?,?,?),");
					}
				}
				PreparedStatement insertStatement = conn.prepareStatement(insertSql.toString());
				int count=10;
				for (int i=0;i<_userList.size();i++) {
				//construct sql statement
					User s = _userList.get(i);
					String uid = s.getId();
					String name = s.getName();
					String screen_name = s.getScreenName();
					String location = s.getLocation();
					String gender = s.getGender();
					String url = s.getUrl();
					Date date = new java.sql.Date(s.getCreatedAt().getTime());
					insertStatement.setString(count*i+1, uid);
					insertStatement.setString(count*i+2, screen_name);
					insertStatement.setString(count*i+3, location);
					insertStatement.setString(count*i+4, gender);
					insertStatement.setString(count*i+5, url);
					insertStatement.setDate(count*i+6, date);
					insertStatement.setString(count*i+7,name);
					insertStatement.setBoolean(count*i+8, s.isVerified());
					insertStatement.setBoolean(count*i+9, s.isallowAllActMsg());
					insertStatement.setBoolean(count*i+10, s.isAllowAllComment());
				}
				insertStatement.execute();
				insertStatement.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private int insert2User(String _uid, String _screenName, String _location, String _gender, /*String _description, */String _url, Date _createdAt) {
		try {
			PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO User(uid,screen_name,location,gender,url,created_at) VALUES(?,?,?,?,?,?)");
			insertStatement.setString(1, _uid);
			insertStatement.setString(2, _screenName);
			insertStatement.setString(3, _location);
			insertStatement.setString(4, _gender);
//			insertStatement.setString(5, _description);
			insertStatement.setString(5, _url);
			insertStatement.setDate(6, _createdAt);
			insertStatement.execute();
			insertStatement.close();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}	
	}
	
	public void add2User(String _uid, String _screenName, String _location, String _gender, /*String _description, */String _url, Date _createdAt){
		try {
			PreparedStatement selectStatement = conn.prepareStatement("SELECT * FROM User WHERE uid=?");
			selectStatement.setString(1, _uid);
			ResultSet result = selectStatement.executeQuery();
			if(result.next()){
				return;
			}
			else{
				insert2User(_uid,_screenName,_location,_gender, /*_description,*/ _url, _createdAt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addConnection(String _uscreen_name, String _fscreen_name){
		try {
			PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO Connection(uscreen_name,fscreen_name) VALUES(?,?) ");
			insertStatement.setString(1, _uscreen_name);
			insertStatement.setString(2, _fscreen_name);
			insertStatement.execute();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getUser() throws IOException{
		try {
			ArrayList<String> uidArrayList = new ArrayList<String>();
			PreparedStatement selectStatement = conn.prepareStatement("SELECT id,uid FROM User WHERE id>? LIMIT 200");
			selectStatement.setString(1, id.toString());
			ResultSet result = selectStatement.executeQuery();
			if(result.next()){
				uidArrayList.add(result.getString("uid"));
			}
			synchronized (id) {
				id+=200;
			}
			return uidArrayList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Integer id=0;
	public static void iniId()
	{
		try{
			File file = new File("config/id");
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp;
			if((temp=reader.readLine())!=null) id=Integer.parseInt(temp);
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void writeId()
	{
		try {
			File file = new File("config/id");
			FileWriter fWriter = new FileWriter(file);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			id+=200;
			bWriter.write(id.toString());
			bWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
