package security.test;

import security.authentication.Authen;
import security.core.TimelineCrawler;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
//    	UserD/.out.println(ud.getUser("1771918757"));
    	TimelineCrawler tcCrawler = new TimelineCrawler();
    	Authen.init();
    	tcCrawler.crawFromDB();
    }
}
