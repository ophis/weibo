package security.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import security.authentication.Authen;
import security.dal.UserDAL;

import weibo4j.Friendships;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class UserProfileCrawler {
	public static void init(String _rootUid) {
		UserProfileCrawler.unamePool = new ArrayBlockingQueue<String>(
				poolCapacity);
		File file = new File("config/unameList");
		try {
			if (!file.exists())
				return;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				unamePool.add(tempString);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		rootUname = unamePool.poll();
	}

	public static void storeUidList() {
		File file = new File("config/unameList");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			for (String uameString : unamePool) {
				bWriter.write(uameString + "\n");
			}
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void start() {
		crawl = true;
	}

	public static void stop() {
		crawl = false;
	}

	public int crawl() throws Exception {
		Friendships fs = new Friendships();
		String token;
		boolean firstround = true;
		try {
			token = Authen.getAuthen().getToken();
			if (token == null)
				return -1;
			fs.setToken(token);
			String seedName;
			synchronized (unamePool) {
				if (0 == unamePool.size()) {
					seedName = rootUname;
					unamePool.add(seedName);
				}
			}
			
//			synchronized (unamePool) {
//				seedName = unamePool.poll();
//			}
			while ((seedName = unamePool.poll()) != null && crawl) {
				// seedName = unamePool.poll();
				UserDAL uDal = new UserDAL();
				List<User> userlist = fs.getFriendsByScreenName(seedName)
						.getUsers();
				for (User user : userlist) {
					uDal.addConnection(seedName, user.getScreenName());
				}
				List<User> fUsers = fs.getFollowersByName(seedName, 200, 0)
						.getUsers();
				for (User user : fUsers) {
					uDal.addConnection(user.getScreenName(), seedName);
				}
				userlist.addAll(fUsers);
				uDal.addAll2Timeline(userlist);
				for (User user : userlist) {
					String uameString = user.getScreenName();
					if (unamePool.size() < poolCapacity
							&& !unamePool.contains(uameString)) {
						unamePool.add(uameString);
					}
					firstround = false;
				}
				uDal.closeConnection();
				storeUidList();
			}	
		} catch (Exception e) {
			if (e instanceof WeiboException) {
				WeiboException weiboException = (WeiboException) e;
				int errorcode = weiboException.getErrorCode();
				if (errorcode == 10023 || errorcode == 10022) {
					if (firstround) {
						return crawl();
					} else {
						return -1;
					}
				}
			}
			throw e;
		}
		return 0;
	}

	public static boolean crawl = true;
	private static int poolCapacity = 2500;
	private static ArrayBlockingQueue<String> unamePool;
	private static String rootUname;
}
