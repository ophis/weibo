package test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.org.json.JSONObject;
import authentication.Authen;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
    	Timeline tm = new Timeline();
    	tm.getPublicTimelineByUid();

    }
}
