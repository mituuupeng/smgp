package cn.com.zjtelecom.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Key {
    public static byte[] GenerateShareKey(String clientid,String key,String timestamp) throws NoSuchAlgorithmException{
    	int sharekeylen=0;
    	if (key!=null){
    		sharekeylen=clientid.length()+key.length()+17;
    	}else {
    		sharekeylen=clientid.length()+key.length();
    	}
    	byte tmpbuf[] = new byte[sharekeylen];
        int tmploc = 0;
        System.arraycopy(clientid.getBytes(), 0, tmpbuf, 0, clientid.length());
        tmploc = clientid.length() + 7;
        if(key != null)
        {
            System.arraycopy(key.getBytes(), 0, tmpbuf, tmploc, key.length());
            tmploc += key.length();
        }
        System.arraycopy(timestamp.getBytes(), 0, tmpbuf, tmploc, 10);
    	return MD5(tmpbuf);
    }
    private static byte[] MD5(byte[] sourecebuf) throws NoSuchAlgorithmException{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(sourecebuf);
			return md5.digest();
    	 
}
}
