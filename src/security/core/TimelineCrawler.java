package security.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.swing.internal.plaf.metal.resources.metal;

import security.authentication.Authen;
import security.dal.TimelineDAL;
import security.dal.UserDAL;
import sun.misc.Regexp;
import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class TimelineCrawler {
	public int crawlPublic() throws Exception {
		Timeline tm = new Timeline();
		boolean firstround=true;
		try {
			tm.setToken(Authen.getAuthen().getToken());
			while (true) {
				StatusWapper sw = tm.getPublicTimeline(200, 0);
				List<Status> status = sw.getStatuses();
				TimelineDAL td = new TimelineDAL();
				td.addAll2Timeline(status);
				td.closeConnection();
				CommentCrawler cm = new CommentCrawler();
				for (Status s : status) {
					if (s.getCommentsCount() > 0)
						cm.crawl(s.getMid());
				}
				firstround = false;
			}
		} catch (Exception e) {
			if (e instanceof WeiboException) {
				WeiboException weiboException = (WeiboException) e;
				int errorcode = weiboException.getErrorCode();
				if (errorcode == 10023 || errorcode == 10022) {
					if (firstround) {
						return crawlPublic();
					} else {
						return -1;
					}
				}
			}
			throw e;
		}
	}
	
	public void crawlById(String mid){
		Timeline tm = new Timeline();
		try {
			tm.setToken(Authen.getAuthen().getToken());
			Status s = tm.showStatus(mid);
			ArrayList<Status> slist = new ArrayList<Status>();
			slist.add(s);
			TimelineDAL tDal = new TimelineDAL();
			tDal.addAll2Timeline(slist);
			CommentCrawler cm = new CommentCrawler();
			if (s.getCommentsCount() > 0)
				try {
					cm.crawl(s.getMid());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void crawFromDB(){
		UserDAL udDal = new UserDAL();
		try {
			ArrayList<String> ulist = udDal.getUser();
			for (String uid : ulist) {
				crawl(uid);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void crawl(String uid){
		String url = "http://www.weibo.com/p/100505"+uid+"/weibo";
		String cookie = "USRHAWB=usrmdins31284; USRUG=usrmdins41446; _s_tentry="
    			+ "product.pconline.com.cn; Apache=84543444681.91266.1381520978344; "
    			+ "SINAGLOBAL=84543444681.91266.1381520978344; ULV=1381520978375:1:1:"
    			+ "1:84543444681.91266.1381520978344:; wvr=5; UOR=product.pconline.com.cn,"
    			+ "widget.weibo.com,login.sina.com.cn; SUE=es%3D5c27bd4889481c572388cb478884947c"
    			+ "%26ev%3Dv1%26es2%3D0939ab1863ff3e8b77fae7efae601649%26rs0%3DEmYBYPZicdesAxxxH"
    			+ "Wpl6OcnLHBH82dzYJtpppxcNyP1C1o7JznV%252FmpdTqFmNeEYUZUgfuGmRblaO28GK1M7dIXwEx"
    			+ "FqAS5X0tM2T0R2gpZPKaq%252FmuGFxQtsGDVfWuIj1IaGYsG5NyFVzI5qriDZ43k1j%252Fa0%25"
    			+ "2B2LneKx83id6wbg%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1381551457%26et%3D13816378"
    			+ "57%26d%3Dc909%26i%3Df868%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3"
    			+ "D3833741273%26name%3Ds.andptestweiboalaleilalei%2540gmail.com%26nick%3Dsandpte"
    			+ "stwei1%26fmp%3D%26lcp%3D2013-10-05%252003%253A21%253A44; SUS=SID-3833741273-138"
    			+ "1551457-JA-31opi-ce1670b106bec7988478c850da5ebf13; ALF=1384143457; SSOLoginState"
    			+ "=1381551457; un=s.andptestweiboalaleilalei@gmail.com; SinaRot_wb_r_topic=65";
		MimicLogin mLogin = new MimicLogin();
		try {
			String htmlString = mLogin.getUrlBody(url, cookie);
			HashSet<String> midList = new HashSet<String>();
			Pattern pattern = Pattern.compile("(\\s|&|;)mid=(\\d{16})");
			Matcher m = pattern.matcher(htmlString);
			while(m.find()){
				midList.add(m.group(2));
			}
			Pattern pattern2 = Pattern.compile("(\\s|&|;)mid=\\\\\"(\\d{16})\\\\\"");
			Matcher m2 = pattern2.matcher(htmlString);
			while(m2.find()){
				midList.add(m2.group(2));
			}
			for (String mid : midList) {
				crawlById(mid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
