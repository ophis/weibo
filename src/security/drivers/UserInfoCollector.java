package security.drivers;

import security.authentication.Authen;
import security.core.UserProfileCrawler;

public class UserInfoCollector {
	public static void main(String[] args) throws InterruptedException {
		int threadCount = 8;
		Authen.init();
		UserProfileCrawler.init(null);
		Thread[] threads = new Thread[threadCount];
		for(int i=0;i<threadCount;i++){
			threads[i]=new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserProfileCrawler uc = new UserProfileCrawler();
					try {
						uc.crawl();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});	
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		UserProfileCrawler.storeUidList();
	}
}
