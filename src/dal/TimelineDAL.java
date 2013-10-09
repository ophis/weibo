package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weibo4j.model.Status;
import weibo4j.model.User;

public class TimelineDAL extends AbstractDAL{
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
			Object[] status=_statusList.toArray();
			while(results.next()){
				String mid = results.getString("mid");
				status[mid2pos.get(mid)]=null;
			}
			selectStatement.close();
			ArrayList<Status> statuslList = new ArrayList<Status>();
			for (Object object : status) {
				if(object!=null){
					statuslList.add((Status)object);
				}
			}
			
			if(statuslList.size()>0){
				StringBuffer insertSql = new StringBuffer("INSERT INTO Timeline(mid,uid,text,inReplyUid,uscreen_name,created_at) VALUES");
				for(int i=0;i<statuslList.size();i++)
				{
					if(i==statuslList.size()-1)
						insertSql.append("(?,?,?,?,?,?)");
					else {
						insertSql.append("(?,?,?,?,?,?),");
					}
				}
				PreparedStatement insertStatement = conn.prepareStatement(insertSql.toString());
				int count=6;
				for (int i=0;i<statuslList.size();i++) {
				//TODO construct sql statement
					Status s = statuslList.get(i);
					String uid = s.getUser().getId();
					String mid = s.getMid();
					String text = s.getText();
					long inReplyUid = s.getInReplyToUserId();
					insertStatement.setString(count*i+1, mid);
					insertStatement.setString(count*i+2, uid);
					insertStatement.setString(count*i+3, text);
					insertStatement.setString(count*i+4, Long.toString(inReplyUid));
					insertStatement.setString(count*i+5, s.getUser().getScreenName());
					insertStatement.setDate(count*i+6, new java.sql.Date(s.getCreatedAt().getTime()));
				}
				insertStatement.execute();
				insertStatement.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
