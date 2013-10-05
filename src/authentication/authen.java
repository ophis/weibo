package authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.util.WeiboConfig;

public class authen {
	public authen()
	{
		
	}
	
	public static void init()
	{
		
	}
	
	public static  AccessToken getToken(String username, String password) throws HttpException, IOException
    {
        String clientId = WeiboConfig.getValue("client_ID") ;
        String redirectURI = WeiboConfig.getValue("redirect_URI") ;
        String url = WeiboConfig.getValue("authorizeURL");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addParameter("client_id",clientId);
        postMethod.addParameter("redirect_uri",redirectURI);
        
        //Mimic login params
        postMethod.addParameter("userId", username);
        postMethod.addParameter("passwd", password);
        postMethod.addParameter("isLoginSina", "0");
        postMethod.addParameter("action", "submit");
        postMethod.addParameter("response_type","code");
        HttpMethodParams param = postMethod.getParams();
        param.setContentCharset("UTF-8");
        //Add header info
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Referer", "https://api.weibo.com/oauth2/authorize?client_id="+clientId+"&redirect_uri="+redirectURI+"&from=sina&response_type=code"));
        headers.add(new Header("Host", "api.weibo.com"));
        headers.add(new Header("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0"));
        HttpClient client = new HttpClient();
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        client.executeMethod(postMethod);
        int status = postMethod.getStatusCode();
        System.out.println(status);
        if (status != 302)
        {
            System.out.println("token refresh failed!");
            return null;
        }
        //Get token
        Header location = postMethod.getResponseHeader("Location");
        if (location != null)
        {
            String retUrl = location.getValue();
            int begin = retUrl.indexOf("code=");
            if (begin != -1) {
                int end = retUrl.indexOf("&", begin);
                if (end == -1)
                    end = retUrl.length();
                String code = retUrl.substring(begin + 5, end);
                if (code != null) {
                    Oauth oauth = new Oauth();
                    try{
                        AccessToken token = oauth.getAccessTokenByCode(code);
                        return token;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
	private static ArrayList<String> accoutList = new ArrayList<String>();
	private static ArrayList<String> keyList = new ArrayList<String>();
	
	public String getToken() throws Exception {	
		String token = getToken("se1curityprivacyincomputating@gmail.com","security&privacy").getAccessToken();
		return token;
	}
}
