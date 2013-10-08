package test;

import java.util.HashSet;

import core.TimelineCrawler;
import authentication.Authen;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
    	Authen.init();
    	HashSet<String> set = new HashSet<String>();
    	for(int i=0;i<70;i++)
    		set.add(Authen.getToken());
    	System.out.println(set.size());
    }
}
