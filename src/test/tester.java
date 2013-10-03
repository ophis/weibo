package test;

import authentication.authen;
import weibo4j.*;
import weibo4j.model.WeiboException;

public class tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		authen A = new authen();
		String token = A.getToken();
		Users um = new Users();
		um.client.setToken(token);
		Friendships fm = new Friendships();
		fm.client.setToken(token);
			String uid = "2308247765";
			long i = Long.parseLong(uid);
			long startMili=System.currentTimeMillis();
			int a=0;
			for(int j=0;j<2;j++)
			{
				uid = String.valueOf(i+j);
				try {
					um.showUserById(uid);
					a++;
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					
				}
			}
			long endMili=System.currentTimeMillis();
			System.out.println("总耗时为："+(endMili-startMili)+"毫秒"+" hit:"+a);
	}

}
