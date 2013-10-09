package security.dal;

import java.sql.PreparedStatement;
import java.util.List;

import weibo4j.model.Comment;


public class CommentDAL extends AbstractDAL {
	public void addAll2Comment(List<Comment> _commentList) {
		if (null == _commentList || _commentList.size() == 0)
			return;
		try {
			StringBuffer insertSql = new StringBuffer(
					"INSERT IGNORE INTO Comment(id,mid,uid,text,uscreen_name,created_at) VALUES");
			for (int i = 0; i < _commentList.size(); i++) {
				if (i == _commentList.size() - 1)
					insertSql.append("(?,?,?,?,?,?)");
				else {
					insertSql.append("(?,?,?,?,?,?),");
				}
			}
			PreparedStatement insertStatement = conn.prepareStatement(insertSql
					.toString());
			int count = 6;
			for (int i = 0; i < _commentList.size(); i++) {
				Comment s = _commentList.get(i);
				String id = Long.toString(s.getId());
				String uid = s.getUser().getId();
				String mid = s.getStatus().getMid();
				String text = s.getText();
				insertStatement.setString(count * i + 1, id);
				insertStatement.setString(count * i + 2, mid);
				insertStatement.setString(count * i + 3, uid);
				insertStatement.setString(count * i + 4, text);
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
