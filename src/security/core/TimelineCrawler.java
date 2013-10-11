package security.core;

import java.util.List;

import security.authentication.Authen;
import security.dal.TimelineDAL;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class TimelineCrawler {
	public int crawlPublic() throws Exception {
		Timeline tm = new Timeline();
		boolean firstround=true;
		try {
			tm.setToken(Authen.getAuthen().getToken());
			while (true) {
				StatusWapper sw = tm.getPublicTimeline(200, 0);
				List<Status> status = sw.getStatuses();
				TimelineDAL td = new TimelineDAL();
				td.addAll2Timeline(status);
				CommentCrawler cm = new CommentCrawler();
				for (Status s : status) {
					if (s.getCommentsCount() > 0)
						cm.crawl(s.getMid());
				}
				firstround = false;
			}
		} catch (Exception e) {
			if (e instanceof WeiboException) {
				WeiboException weiboException = (WeiboException) e;
				int errorcode = weiboException.getErrorCode();
				if (errorcode == 10023 || errorcode == 10022) {
					if (firstround) {
						return crawlPublic();
					} else {
						return -1;
					}
				}
			}
			throw e;
		}
	}
}
