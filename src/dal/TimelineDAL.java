package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import weibo4j.model.Status;

public class TimelineDAL extends AbstractDAL{
	public void select() {
		String sqlString = ("SELECT * FROM Timeline");
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			ResultSet results = statement.executeQuery();
			while(results.next()){
				String mid = results.getString("text");
				System.out.println(mid);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void addAll2Timeline(List<Status> _statusList){
		if(null==_statusList || _statusList.size()==0) return;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT mid FROM Timeline WHERE mid IN(");
		int size = _statusList.size();
		for(int i=0; i<size;i++){
			if(i == size-1){
				sql.append("?)");
			}
			else {
				sql.append("?,");
			}
		}
		try {
			PreparedStatement selectStatement = conn.prepareStatement(sql.toString());
			HashMap<String, Integer> mid2pos = new HashMap<String, Integer>();
			for(int i=0;i<size;i++){
				String mid = _statusList.get(i).getMid();
				selectStatement.setString(i+1, mid);
				mid2pos.put(mid,i);
			}
			
			ResultSet results = selectStatement.executeQuery();
			while(results.next()){
				String mid = results.getString("mid");
				_statusList.remove(mid2pos.get(mid));
			}
			selectStatement.close();
			if(_statusList.size()>0){
				StringBuffer insertSql = new StringBuffer("INSERT INTO Timeline(mid,uid,text,inReplyUid) VALUES");
				for(int i=0;i<_statusList.size();i++)
				{
					if(i==_statusList.size()-1)
						insertSql.append("(?,?,?,?)");
					else {
						insertSql.append("(?,?,?,?),");
					}
				}
				PreparedStatement insertStatement = conn.prepareStatement(insertSql.toString());
				for (int i=0;i<_statusList.size();i++) {
				//TODO construct sql statement
					Status s = _statusList.get(i);
					String uid = s.getUser().getId();
					String mid = s.getMid();
					String text = s.getText();
					long inReplyUid = s.getInReplyToUserId();
					insertStatement.setString(4*i+1, mid);
					insertStatement.setString(4*i+2, uid);
					insertStatement.setString(4*i+3, text);
					insertStatement.setString(4*i+4, Long.toString(inReplyUid));
				}
				insertStatement.execute();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
