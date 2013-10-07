package test;

import core.UserProfileCrawler;
import authentication.Authen;

public class tester {
	
	private int i=0;
    public static void main(String[] args) throws Exception
    {
    	Authen.init();
    	UserProfileCrawler.init("1914443333");
    	UserProfileCrawler crawler = new UserProfileCrawler();
    	crawler.crawl();
    }
}
