package security.test;

import security.authentication.Authen;
import security.core.UserProfileCrawler;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
//    	UserD/.out.println(ud.getUser("1771918757"));
    	Authen.init();
		UserProfileCrawler.init(null);
		UserProfileCrawler uCrawler = new UserProfileCrawler();
		uCrawler.crawl();
		UserProfileCrawler.storeUidList();
    }
}
