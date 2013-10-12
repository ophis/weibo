package security.test;

import security.authentication.Authen;
import security.drivers.RefreshToken;
import sun.reflect.generics.tree.Tree;

public class tester {
	
    public static void main(String[] args) throws Exception
    {
    	Authen.init();
    	Thread thread1=new Thread(new Runnable() {
			public void run() {
				try {
					String string;
					while((string=Authen.getAuthen().getToken())!=null)System.out.println("1:"+string);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
//    	Thread thread2=new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					String string;
//					while((string=Authen.getAuthen().getToken())!=null)System.out.println("2:"+string);
//				} 
//				catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});	
//    	String aString;
//		while ((aString = Authen.getAuthen().getToken())!=null) {
//			tokensHashSet.add(new String(aString));
//		}
		thread1.start();
//		thread2.start();
		Thread.sleep(1000);
		thread1.interrupt();
//		thread2.join();
    	System.out.println();
    }
}
