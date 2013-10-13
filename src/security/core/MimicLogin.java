package security.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import org.apache.commons.httpclient.params.HttpMethodParams;

public class MimicLogin {
	public String getUrlBody(String url,String cookie) throws Exception {
		GetMethod postMethod = new GetMethod(url);
//		postMethod.addParameter("userId", "﻿s.andptestweiboalaleilalei@gmail.com");
//		postMethod.addParameter("passwd", "security&privacy");
		HttpMethodParams param = postMethod.getParams();
		param.setContentCharset("UTF-8");
		// Add header info
		List<Header> headers = new ArrayList<Header>();
//		headers.add(new Header("Referer",
//				"http://www.weibo.com/u/3833741273/home?wvr=5"));
		headers.add(new Header("Host", "www.weibo.com"));
		headers.add(new Header("Cookie",cookie));
		headers.add(new Header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36"));
		HttpClient client = new HttpClient();
		client.getHostConfiguration().getParams()
				.setParameter("http.default-headers", headers);
		client.executeMethod(postMethod);
		InputStream inputStream = postMethod.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
}
