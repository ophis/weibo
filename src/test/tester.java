package test;

import core.UserProfileCrawler;
import authentication.Authen;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
    	Authen.init();
    	UserProfileCrawler.init("1914443333");
    	UserProfileCrawler crawler = new UserProfileCrawler();
    	crawler.crawl();
    	UserProfileCrawler.storeUidList();
    }
}
