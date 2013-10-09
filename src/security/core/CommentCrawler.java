package security.core;

import java.util.List;

import security.authentication.Authen;
import security.dal.CommentDAL;
import weibo4j.Comments;
import weibo4j.model.Comment;


public class CommentCrawler {
	public int crawl(){
		Comments cm = new Comments();
    	try {
	    	cm.setToken(Authen.getToken());
	    	String mid = "3631681900630653";
	    	List<Comment> comments= cm.getCommentById(mid).getComments();
	    	CommentDAL cDal = new CommentDAL();
	    	cDal.addAll2Comment(comments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
