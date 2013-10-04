package core;

import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.WeiboException;
import authentication.authen;

public class userProfileCrawler {
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		authen A = new authen();
		String token;
		try {
			token = A.getToken();
			Users um = new Users();
			um.client.setToken(token);
			Timeline tm = new Timeline();
			tm.client.setToken(token);
				String uid = "2308247765";
				long i = Long.parseLong(uid);
				long startMili=System.currentTimeMillis();
				int a=0;
				for(int j=0;j<10;j++)
				{
					uid = String.valueOf(i+j);
					try {
						tm.showStatus(uid);
						a++;
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						
					}
				}
				long endMili=System.currentTimeMillis();
				System.out.println("总耗时为："+(endMili-startMili)+"毫秒"+" hit:"+a);
		} catch (WeiboException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
