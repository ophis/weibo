package security.drivers;

import security.authentication.Authen;

public class RefreshToken {
	
    public static void main(String[] args) throws Exception
    {
    	Authen.init();
    	Authen.refreshToken();
    }
}
