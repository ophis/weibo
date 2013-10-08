package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class UserDAL extends AbstractDAL{
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
