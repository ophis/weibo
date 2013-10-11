package security.drivers;

import security.authentication.Authen;
import security.core.TimelineCrawler;
import security.core.UserProfileCrawler;
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
									uc.crawl();
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
					UserProfileCrawler.storeUidList();
				}
			}
		});
		userThread.start();
//		Thread timeThread = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while(true){
//					Thread[] threads2 = new Thread[2];
//					for (int i = 0; i < 2; i++) {
//						
//						threads2[i] = new Thread(new Runnable() {
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								TimelineCrawler tc = new TimelineCrawler();
//								try {
//									tc.crawlPublic();
//								} catch (Exception e) {
//									if (e instanceof WeiboException) {
//										WeiboException weiboException = (WeiboException) e;
//										int errorcode = weiboException.getErrorCode();
//										if (errorcode == 10023 || errorcode == 10022) {
//										}
//										else {
//											e.printStackTrace();
//										}
//									}
//								}
//							}
//						});
//						threads2[i].start();
//					}
//					try {
//						threads2[0].join();
//						threads2[1].join();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		timeThread.start();
	}
}
