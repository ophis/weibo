package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAL extends AbstractDAL{
	private int insert2user(String _uid, String _screenName) {
		try {
			PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO User(uid,screen_name) VALUES(?,?)");
			insertStatement.setString(1, _uid);
			insertStatement.setString(2, _screenName);
			insertStatement.execute();
			insertStatement.close();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}	
	}
	
	public void add2user(String _uid, String _screenName, String _location, String _gender){
		try {
			PreparedStatement selectStatement = conn.prepareStatement("SELECT * FROM User WHERE uid=?");
			selectStatement.setString(1, _uid);
			ResultSet result = selectStatement.executeQuery();
			if(result.next()){
				return;
			}
			else{
				insert2user(_uid,_screenName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
