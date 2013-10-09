package drivers;

import core.UserProfileCrawler;
import authentication.Authen;

public class UserInfoCollector {
	public static void main(String[] args) {
		int threadCount = 5;
		Authen.init();
		UserProfileCrawler.init(null);
		Thread[] threads = new Thread[threadCount];
		for(int i=0;i<threadCount;i++){
			threads[i]=new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserProfileCrawler uc = new UserProfileCrawler();
					uc.crawl();
				}
			});	
			threads[i].start();
		}
		//UserProfileCrawler.storeUidList();
	}
}
