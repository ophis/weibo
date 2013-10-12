package security.test;

import security.core.TimelineCrawler;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
//    	UserD/.out.println(ud.getUser("1771918757"));
    	TimelineCrawler tcCrawler = new TimelineCrawler();
    	tcCrawler.crawl("2644969471");
    }
}
