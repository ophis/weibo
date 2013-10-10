package security.authentication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import weibo4j.http.*;
import weibo4j.model.PostParameter;
import weibo4j.util.WeiboConfig;

public class Authen {
	public static void init() {
		accoutList = getFromText("config/accounts");
		keyList = getFromText("config/appKeys");
		token = getFromText("config/tokenList");
	}

	public static void refreshToken() {
		File file = new File("config/tokenList");
		token.removeAll(token);
		try {
			int threadCount = 16;
			Thread[] threads = new Thread[threadCount];
			for (int i = 0; i < threadCount; i++) {
				threads[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						String temp;
						try {
							while ((temp = constructToken()) != null) {
								token.add(new String(temp));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				threads[i].start();
			}
			for (int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
			// while((temp=constructToken())!=null){
			// token.add(new String(temp));
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashSet<String> unique = new HashSet<String>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			for (String t : token) {
				bWriter.write(t + "\n");
				unique.add(t);
			}
			bWriter.close();
			fWriter.close();
			System.out.println(unique.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Authen getAuthen() {
		return authen;
	}

	public String getToken() {
		int tc;
		synchronized (tokenCounter) {
			tc = tokenCounter;
			tokenCounter = (++tokenCounter) % token.size();
		}
		return token.get(tc);
	}

	public static String constructToken() throws Exception {
		int ac;
		int kc;
		synchronized (accountCounter) {
			ac = accountCounter;
			kc = keyCounter;
			if (kc >= keyList.size())
				return null;
			accountCounter = (++accountCounter) % accoutList.size();
			if (accountCounter == 0) {
				synchronized (keyCounter) {
					keyCounter++;
				}
			}
		}
		String username = accoutList.get(ac);
		String password = "security&privacy";

		String[] keyStrings = keyList.get(kc).split(";");
		String key = keyStrings[0];
		String secret = keyStrings[1];

		String clientId = WeiboConfig.getValue("client_ID");

		String redirectURI = WeiboConfig.getValue("redirect_URI");
		String url = WeiboConfig.getValue("authorizeURL");
		PostMethod postMethod = new PostMethod(url);
		postMethod.addParameter("client_id", clientId);
		postMethod.addParameter("redirect_uri", redirectURI);

		// Mimic login params
		postMethod.addParameter("userId", username);
		postMethod.addParameter("passwd", password);
		postMethod.addParameter("isLoginSina", "0");
		postMethod.addParameter("action", "submit");
		postMethod.addParameter("response_type", "code");
		HttpMethodParams param = postMethod.getParams();
		param.setContentCharset("UTF-8");
		// Add header info
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Referer",
				"https://api.weibo.com/oauth2/authorize?client_id=" + clientId
						+ "&redirect_uri=" + redirectURI
						+ "&from=sina&response_type=code"));
		headers.add(new Header("Host", "api.weibo.com"));
		headers.add(new Header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0"));
		HttpClient client = new HttpClient();
		client.getHostConfiguration().getParams()
				.setParameter("http.default-headers", headers);
		client.executeMethod(postMethod);
		int status = postMethod.getStatusCode();
		if (status != 302) {
			System.out.println("token refresh failed!");
			return null;
		}
		// Get token
		Header location = postMethod.getResponseHeader("Location");
		if (location != null) {
			String retUrl = location.getValue();
			int begin = retUrl.indexOf("code=");
			if (begin != -1) {
				int end = retUrl.indexOf("&", begin);
				if (end == -1)
					end = retUrl.length();
				String code = retUrl.substring(begin + 5, end);
				if (code != null) {
					try {
						weibo4j.http.HttpClient client2 = new weibo4j.http.HttpClient();
						AccessToken token = new AccessToken(
								client2.post(
										WeiboConfig.getValue("accessTokenURL"),
										new PostParameter[] {
												new PostParameter("client_id",
														key),
												new PostParameter(
														"client_secret", secret),
												new PostParameter("grant_type",
														"authorization_code"),
												new PostParameter("code", code),
												new PostParameter(
														"redirect_uri",
														WeiboConfig
																.getValue("redirect_URI")) },
										false));
						;
						return token.getAccessToken();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public String getTokenSingle() throws Exception {
		int ac;
		int kc;
		synchronized (accountCounter) {
			ac = accountCounter;
			kc = keyCounter;
			accountCounter = (++accountCounter) % accoutList.size();
			if (accountCounter == 0) {
				synchronized (keyCounter) {
					keyCounter = (++keyCounter) % keyList.size();
				}
			}
		}
		String username = accoutList.get(ac);
		String password = "security&privacy";

		String[] keyStrings = keyList.get(kc).split(";");
		String key = keyStrings[0];
		String secret = keyStrings[1];

		String clientId = WeiboConfig.getValue("client_ID");

		String redirectURI = WeiboConfig.getValue("redirect_URI");
		String url = WeiboConfig.getValue("authorizeURL");
		PostMethod postMethod = new PostMethod(url);
		postMethod.addParameter("client_id", clientId);
		postMethod.addParameter("redirect_uri", redirectURI);

		// Mimic login params
		postMethod.addParameter("userId", username);
		postMethod.addParameter("passwd", password);
		postMethod.addParameter("isLoginSina", "0");
		postMethod.addParameter("action", "submit");
		postMethod.addParameter("response_type", "code");
		HttpMethodParams param = postMethod.getParams();
		param.setContentCharset("UTF-8");
		// Add header info
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Referer",
				"https://api.weibo.com/oauth2/authorize?client_id=" + clientId
						+ "&redirect_uri=" + redirectURI
						+ "&from=sina&response_type=code"));
		headers.add(new Header("Host", "api.weibo.com"));
		headers.add(new Header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0"));
		HttpClient client = new HttpClient();
		client.getHostConfiguration().getParams()
				.setParameter("http.default-headers", headers);
		client.executeMethod(postMethod);
		int status = postMethod.getStatusCode();
		if (status != 302) {
			System.out.println("token refresh failed!");
			return null;
		}
		// Get token
		Header location = postMethod.getResponseHeader("Location");
		if (location != null) {
			String retUrl = location.getValue();
			int begin = retUrl.indexOf("code=");
			if (begin != -1) {
				int end = retUrl.indexOf("&", begin);
				if (end == -1)
					end = retUrl.length();
				String code = retUrl.substring(begin + 5, end);
				if (code != null) {
					try {
						weibo4j.http.HttpClient client2 = new weibo4j.http.HttpClient();
						AccessToken token = new AccessToken(
								client2.post(
										WeiboConfig.getValue("accessTokenURL"),
										new PostParameter[] {
												new PostParameter("client_id",
														key),
												new PostParameter(
														"client_secret", secret),
												new PostParameter("grant_type",
														"authorization_code"),
												new PostParameter("code", code),
												new PostParameter(
														"redirect_uri",
														WeiboConfig
																.getValue("redirect_URI")) },
										false));
						;
						return token.getAccessToken();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private static ArrayList<String> getFromText(String _fileName) {
		File file = new File(_fileName);
		BufferedReader reader = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				list.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	final private static Authen authen = new Authen();
	private static ArrayList<String> token = new ArrayList<String>();
	private static Integer tokenCounter = 0;
	private static Integer accountCounter = 0;
	private static Integer keyCounter = 0;
	private static ArrayList<String> accoutList;
	private static ArrayList<String> keyList;
}
