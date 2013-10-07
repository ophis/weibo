package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
		File file = new File("config/uidList");
		try {
			if(!file.exists()) return;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempString;
			while((tempString=reader.readLine())!=null){
				uidPool.add(tempString);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		rootuid = uidPool.poll();
	}
	
	public static void storeUidList(){
		File file = new File("config/uidList");
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			for (String uidString : uidPool) {
				bWriter.write(uidString+"\n");
			}
			bWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
	
	public static void start() {
		crawl = true;
	}
	
	public static void stop() {
		crawl = false;
	}
	
	public int crawl(){
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
				List<User> userlist = fs.getFollowersByName(screen_name).getUsers();
				for (User user : userlist) {
					String uidString = user.getId();
					uDal.add2user(uidString, user.getScreenName(), user.getLocation(), user.getGender(),  user.getUrl(), new java.sql.Date(user.getCreatedAt().getTime()));
					if(uidPool.size()<poolCapacity && !uidPool.contains(uidString))
					{
						uidPool.add(uidString);
					}
					firstround = false;
				}
			}
		} catch (Exception e) {
			if(e instanceof WeiboException){
				WeiboException weiboException = (WeiboException)e;
				if(weiboException.getErrorCode()==10023){
					if(firstround){ 
						return crawl();
					}
					else {
						return -1;
					}
				}
			}
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	private static boolean crawl = true;
	private static int poolCapacity=1500;
	private static ArrayBlockingQueue<String> uidPool;
	private static String rootuid;
}
