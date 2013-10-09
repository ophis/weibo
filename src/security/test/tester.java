package security.test;

import security.authentication.Authen;
import security.core.CommentCrawler;
import security.core.TimelineCrawler;
import security.dal.TimelineDAL;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
    	Authen.init();
    	CommentCrawler ccCommentCrawler = new CommentCrawler();
    	ccCommentCrawler.crawl();
    }
}
