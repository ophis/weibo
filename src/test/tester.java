package test;

import core.userProfileCrawler;
import authentication.authen;

public class tester {
	
	private int i=0;
    public static void main(String[] args) throws Exception
    {
    	authen.init();
    	userProfileCrawler.init("1914443333");
    	userProfileCrawler crawler = new userProfileCrawler();
    	crawler.crawl();
    }
}
