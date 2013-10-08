package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import weibo4j.model.Status;

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
			while(results.next()){
				String mid = results.getString("mid");
				_statusList.remove(mid2pos.get(mid));
			}
			selectStatement.close();
			for (Status s : _statusList) {
				//TODO construct sql statement
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
