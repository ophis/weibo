package security.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weibo4j.model.User;

public class UserDAL extends AbstractDAL{
	
	public void addAll2Timeline(List<User> _userList){
		if(null==_userList || _userList.size()==0) return;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT uid FROM User WHERE uid IN(");
		int size = _userList.size();
		for(int i=0; i<size;i++){
			if(i == size-1){
				sql.append("?)");
			}
			else {
				sql.append("?,");
			}
		}
		try {			
			HashMap<String, Integer> uid2pos = new HashMap<String, Integer>();
			PreparedStatement selectStatement = conn.prepareStatement(sql.toString());
			for(int i=0;i<size;i++){
				String uid = _userList.get(i).getId();
				selectStatement.setString(i+1, uid);
				uid2pos.put(uid, i);
			}

			ResultSet results = selectStatement.executeQuery();
			Object [] user = _userList.toArray();
			while(results.next()){
				String uid = results.getString("uid");
				user[uid2pos.get(uid)]=null;
				//uid2user.remove(uid);
			}
			selectStatement.close();
			
			ArrayList<User> users = new ArrayList<User>();
			for (Object object : user) {
				if(object!=null){
					users.add((User)object);
				}
			}
			//User[] users=(User[])uid2user.values().toArray();
			
			if(users.size()>0){
				StringBuffer insertSql = new StringBuffer("INSERT INTO User(uid,screen_name,location,gender,url,created_at) VALUES");
				for(int i=0;i<users.size();i++)
				{
					if(i==users.size()-1)
						insertSql.append("(?,?,?,?,?,?)");
					else {
						insertSql.append("(?,?,?,?,?,?),");
					}
				}
				PreparedStatement insertStatement = conn.prepareStatement(insertSql.toString());
				for (int i=0;i<users.size();i++) {
				//construct sql statement
					User s = users.get(i);
					String uid = s.getId();
					String screen_name = s.getScreenName();
					String location = s.getLocation();
					String gender = s.getGender();
					String url = s.getUrl();
					Date date = new java.sql.Date(s.getCreatedAt().getTime());
					insertStatement.setString(6*i+1, uid);
					insertStatement.setString(6*i+2, screen_name);
					insertStatement.setString(6*i+3, location);
					insertStatement.setString(6*i+4, gender);
					insertStatement.setString(6*i+5, url);
					insertStatement.setDate(6*i+6, date);
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
}
