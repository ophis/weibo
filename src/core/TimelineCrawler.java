package core;

import java.util.List;

import dal.TimelineDAL;

import authentication.Authen;
import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

public class TimelineCrawler {
	public int crawlPublic(){
		Timeline tm = new Timeline();
    	try {
			tm.setToken(Authen.getToken());
	    	StatusWapper sw = tm.getPublicTimeline(200,0);
	    	List<Status> status = sw.getStatuses();
	    	TimelineDAL td = new TimelineDAL();
	    	td.addAll2Timeline(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
