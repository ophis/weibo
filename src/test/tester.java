package test;

import core.userProfileDispatcher;
import authentication.authen;

public class tester {
	
	private int i=0;
    public static void main(String[] args) throws Exception
    {
    	authen.init();
    	userProfileDispatcher dispatcher = new userProfileDispatcher(" ");
    	dispatcher.createUidPool();
    }
}
