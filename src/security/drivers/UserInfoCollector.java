package security.drivers;

import security.authentication.Authen;
import security.core.TimelineCrawler;
import security.core.UserProfileCrawler;
import security.dal.UserDAL;
import weibo4j.model.WeiboException;

public class UserInfoCollector {
	public static void main(String[] args) throws InterruptedException {
		Thread userThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					int threadCount = 8;
					Authen.init();
					UserProfileCrawler.init(null);
					Thread[] threads = new Thread[threadCount];
					for (int i = 0; i < threadCount; i++) {
						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								UserProfileCrawler uc = new UserProfileCrawler();
								try {
									if(UserProfileCrawler.crawl)
										uc.crawl();
									else{
										Thread.sleep(5000);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
					for (int i = 0; i < threadCount; i++) {
						threads[i].start();
						try {
							threads[i].join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		});
		userThread.start();
		Thread timeThread = new Thread(new Runnable() {		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					int count = 1;
					Thread[] threads2 = new Thread[count];
					for (int i = 0; i < count; i++) {
						
						threads2[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								TimelineCrawler tc = new TimelineCrawler();
								try {
									if(UserProfileCrawler.crawl){
										tc.crawlPublic();
										Thread.sleep(2000);
									}
									else{
										Thread.sleep(5000);
									}
								} catch (Exception e) {
									if (e instanceof WeiboException) {
										WeiboException weiboException = (WeiboException) e;
										int errorcode = weiboException.getErrorCode();
										if (errorcode == 10023 || errorcode == 10022) {
										}
										else {
											e.printStackTrace();
										}
									}
								}
							}
						});
						threads2[i].start();
					}
					try {
						for(int i=0;i<threads2.length;i++) threads2[i].join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		timeThread.start();
		Thread timeThread2 = new Thread(new Runnable() {		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					UserDAL.iniId();
					int count=8;
					Thread[] threads2 = new Thread[count];
					for (int i = 0; i < count; i++) {
						
						threads2[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								TimelineCrawler tc = new TimelineCrawler();
								try {
									if(UserProfileCrawler.crawl){
										tc.crawFromDB();
									}
									else{
										Thread.sleep(5000);
									}
								} catch (Exception e) {
									if (e instanceof WeiboException) {
										WeiboException weiboException = (WeiboException) e;
										int errorcode = weiboException.getErrorCode();
										if (errorcode == 10023 || errorcode == 10022) {
										}
										else {
											e.printStackTrace();
										}
									}
								}
							}
						});
						threads2[i].start();
					}
					try {
						for(int i=0;i<threads2.length;i++) threads2[i].join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					UserDAL.writeId();
				}
			}
		});
		timeThread2.start();
		//timing
		while(true)
		{
			Thread.sleep(24*3600*1000);
			UserProfileCrawler.crawl=false;
			Authen.refreshToken();
			UserProfileCrawler.crawl=true;
		}
	}
}
