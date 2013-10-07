package core;

import java.util.ArrayList;

import weibo4j.Users;

import authentication.authen;

public class userProfileCrawler {
	public userProfileCrawler(ArrayList<String> _uidArrayList)
	{
		this.uidList = _uidArrayList;
	}
	
	public void crawl()
	{
		try {
			String token = authen.getToken().getAccessToken();
			Users users = new Users();
			users.setToken(token);
			for(int i=0;i<this.uidList.size();i++)
			{
				users.showUserByScreenName(uidList.get(i));
				//TODO store the data
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> uidList;
}
