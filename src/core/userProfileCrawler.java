package core;


import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import authentication.authen;

public class userProfileCrawler {
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		authen.init();
		String token;
		try {
			token = authen.getToken().getAccessToken();
			Users um = new Users();
			um.client.setToken(token);
			Timeline tm = new Timeline();
			tm.client.setToken(token);
			String uid = "2308247765";
			long i = Long.parseLong(uid);
			long startMili=System.currentTimeMillis();
			uid = String.valueOf(i);
			try {
				User user;
				user = um.showUserById(uid);
				System.out.println(user.getScreenName());
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
						
			}
			long endMili=System.currentTimeMillis();
			System.out.println("总耗时为："+(endMili-startMili)+"毫秒");
		} catch (WeiboException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
