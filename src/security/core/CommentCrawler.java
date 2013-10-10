package security.core;

import java.util.List;

import security.authentication.Authen;
import security.dal.CommentDAL;
import weibo4j.Comments;
import weibo4j.model.Comment;


public class CommentCrawler {
	public int crawl(String mid) throws Exception{
		Comments cm = new Comments();
    	try {
	    	cm.setToken(Authen.getAuthen().getToken());
	    	List<Comment> comments= cm.getCommentById(mid).getComments();
	    	CommentDAL cDal = new CommentDAL();
	    	cDal.addAll2Comment(comments);
		} catch (Exception e) {
			throw e;
		}
		return 0;
	}
}
