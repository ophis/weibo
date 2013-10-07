package core;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import dal.UserDAL;

import weibo4j.Friendships;
import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import authentication.Authen;

public class UserProfileCrawler {
	public static void init(String _rootUid){
		UserProfileCrawler.uidPool = new ArrayBlockingQueue<String>(poolCapacity);
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
		String token;
		boolean firstround = true;
		try {
			token = Authen.getToken();
			fs.setToken(token);
			um.setToken(token);
			String seedId;
			if(0==uidPool.size()){
				seedId = rootuid;
				uidPool.add(seedId);
			}
			UserDAL uDal = new UserDAL();
			while(uidPool.size()>0)
			{
				seedId = uidPool.poll();
				String screen_name = um.showUserById(seedId).getScreenName();
				List<User> userlist = fs.getFriendsByScreenName(screen_name).getUsers();
				for (User user : userlist) {
					String uidString = user.getId();
					if(uidPool.size()<poolCapacity && !uidPool.contains(uidString))
					{
						uidPool.add(uidString);
						uDal.add2user(uidString, user.getScreenName());
					}
					firstround = false;
				}
			}
		} catch (Exception e) {
			if(e instanceof WeiboException){
				WeiboException weiboException = (WeiboException)e;
				if(weiboException.getErrorCode()==10023){
					if(firstround){ 
						crawl();
					}
					else {
						System.out.println(uidPool.size());
						return;
					}
				}
			}
		}
	}
	
	private static boolean crawl = true;
	private static int poolCapacity=1500;
	private static ArrayBlockingQueue<String> uidPool;
	private static String rootuid;
}
