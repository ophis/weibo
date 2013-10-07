package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import weibo4j.Friendships;
import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import authentication.authen;

public class userProfileCrawler {
	public static void init(String _rootUid){
		rootuid = _rootUid;
	}
	
	public static void start() {
		crawl = true;
	}
	
	public static void stop() {
		crawl = false;
	}
	
	public void crawl(){
		Friendships fs = new Friendships();
		Users um = new Users();
		try {
			String token = authen.getToken();
			fs.setToken(token);
			um.setToken(token);
			String seedId;
			if(0==uidPool.size()){
				seedId = rootuid;
				uidPool.add(seedId);
			}
			else {
				seedId = uidPool.poll();
			}
			
			String screen_name = um.showUserById(seedId).getScreenName();
			List<User> userlist = fs.getFriendsByScreenName(screen_name).getUsers();
			for (User user : userlist) {
					uidPool.add(user.getId());
					//TODO store user data
					
			}
			
		} catch (Exception e) {
			if(e instanceof WeiboException){
				WeiboException weiboException = (WeiboException)e;
				if(weiboException.getErrorCode()==10023){
					this.crawl();
				}
			}
		}
	}
	
	private static boolean crawl = true;
	private static int poolCapacity=1500;
	private static Queue<String> uidPool;
	private static String rootuid;
}
