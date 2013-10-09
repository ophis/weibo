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
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import authentication.Authen;

public class UserProfileCrawler {
	public static void init(String _rootUid){
		UserProfileCrawler.unamePool = new ArrayBlockingQueue<String>(poolCapacity);
		File file = new File("config/unameList");
		try {
			if(!file.exists()) return;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempString;
			while((tempString=reader.readLine())!=null){
				unamePool.add(tempString);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		rootUname = unamePool.poll();
	}
	
	public static void storeUidList(){
		File file = new File("config/unameList");
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			for (String uameString : unamePool) {
				bWriter.write(uameString+"\n");
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
		String token;
		boolean firstround = true;
		try {
			token = Authen.getToken();
			fs.setToken(token);
			String seedName;
			synchronized (unamePool) {
				if(0==unamePool.size()){
					seedName = rootUname;
					unamePool.add(seedName);
				}
			}
			UserDAL uDal = new UserDAL();
			while((seedName = unamePool.poll())!=null && crawl)
			{
				List<User> userlist = fs.getFriendsByScreenName(seedName).getUsers();
				uDal.addAll2Timeline(userlist);
				for (User user : userlist) {
					String uameString = user.getScreenName();
					//uDal.add2User(user.getId(), user.getScreenName(), user.getLocation(), user.getGender(),  user.getUrl(), new java.sql.Date(user.getCreatedAt().getTime()));
					if(unamePool.size()<poolCapacity && !unamePool.contains(uameString))
					{
						unamePool.add(uameString);
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
	private static ArrayBlockingQueue<String> unamePool;
	private static String rootUname;
}
