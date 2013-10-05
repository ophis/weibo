package test;

import authentication.authen;

public class tester {
	
	private int i=0;
    public static void main(String[] args) throws Exception
    {
    	Thread[] t = new Thread[1000];
    	for(int i=0;i<1000;i++)
    	{
    		t[i]=new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (authen.getAcc()==999) {
						System.out.println("999!");
					}
					//System.out.println(authen.getAcc());
				}
			});
    	}
    	for(int i=0;i<1000;i++) t[i].start();
    }
}
