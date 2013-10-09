package security.test;

import security.authentication.Authen;
import security.core.TimelineCrawler;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
    	Authen.init();
    	TimelineCrawler tc = new TimelineCrawler();
    	tc.crawlPublic();
//    	TimelineDAL tDal  = new TimelineDAL();
//    	tDal.select();
    }
}
