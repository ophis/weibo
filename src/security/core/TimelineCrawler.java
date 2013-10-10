package security.core;

import java.util.List;

import security.authentication.Authen;
import security.dal.TimelineDAL;


import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

public class TimelineCrawler {
	public int crawlPublic() throws Exception{
		Timeline tm = new Timeline();
    	try {
			tm.setToken(Authen.getAuthen().getToken());
	    	StatusWapper sw = tm.getPublicTimeline(200,0);
	    	List<Status> status = sw.getStatuses();
	    	TimelineDAL td = new TimelineDAL();
	    	td.addAll2Timeline(status);
	    	CommentCrawler cm = new CommentCrawler();
	    	for (Status s : status) {
	    		if(s.getCommentsCount()>0)
	    			cm.crawl(s.getMid());
			}
		} catch (Exception e) {
			throw e;
		}
		return 0;
	}
}
