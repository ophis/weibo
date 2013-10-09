package security.dal;

import java.sql.PreparedStatement;
import java.util.List;

import weibo4j.model.Status;

public class TimelineDAL extends AbstractDAL {
	public void addAll2Timeline(List<Status> _statusList) {
		if (null == _statusList || _statusList.size() == 0)
			return;
		try {
			StringBuffer insertSql = new StringBuffer(
					"INSERT IGNORE INTO Timeline(mid,uid,text,inReplyUid,uscreen_name,created_at) VALUES");
			for (int i = 0; i < _statusList.size(); i++) {
				if (i == _statusList.size() - 1)
					insertSql.append("(?,?,?,?,?,?)");
				else {
					insertSql.append("(?,?,?,?,?,?),");
				}
			}
			PreparedStatement insertStatement = conn.prepareStatement(insertSql
					.toString());
			int count = 6;
			for (int i = 0; i < _statusList.size(); i++) {
				Status s = _statusList.get(i);
				String uid = s.getUser().getId();
				String mid = s.getMid();
				String text = s.getText();
				long inReplyUid = s.getInReplyToUserId();
				insertStatement.setString(count * i + 1, mid);
				insertStatement.setString(count * i + 2, uid);
				insertStatement.setString(count * i + 3, text);
				insertStatement.setString(count * i + 4,
						Long.toString(inReplyUid));
				insertStatement.setString(count * i + 5, s.getUser()
						.getScreenName());
				insertStatement.setDate(count * i + 6, new java.sql.Date(s
						.getCreatedAt().getTime()));
			}
			insertStatement.execute();
			insertStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
