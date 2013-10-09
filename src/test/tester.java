package test;

import java.util.HashSet;

import weibo4j.Timeline;

import core.TimelineCrawler;
import dal.TimelineDAL;
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
