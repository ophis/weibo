package test;

import core.TimelineCrawler;
import authentication.Authen;

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
