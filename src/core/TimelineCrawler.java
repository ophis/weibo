package core;

import java.util.List;

import authentication.Authen;
import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

public class TimelineCrawler {
	public int crawlPublic(){
		Timeline tm = new Timeline();
    	try {
			tm.setToken(Authen.getToken());
	    	StatusWapper sw = tm.getPublicTimeline();
	    	List<Status> status = sw.getStatuses();
	    	Status status2 = status.get(0);
	    	System.out.println(status2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
