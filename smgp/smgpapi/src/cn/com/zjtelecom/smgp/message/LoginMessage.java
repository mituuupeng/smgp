package cn.com.zjtelecom.smgp.message;

import java.security.NoSuchAlgorithmException;

import cn.com.zjtelecom.smgp.protocol.RequestId;
import cn.com.zjtelecom.util.DateUtil;
import cn.com.zjtelecom.util.Hex;
import cn.com.zjtelecom.util.Key;
import cn.com.zjtelecom.util.TypeConvert;

public class LoginMessage extends Message {
	public LoginMessage(String ClientID, String shared_Secret, int LoginMode)
			throws IllegalArgumentException, NoSuchAlgorithmException {
		// 判断数据合法性
		if (ClientID == null)
			throw new IllegalArgumentException("ClientID isNull");
		if (ClientID.length() > 8)
			throw new IllegalArgumentException(
					"ClientID is length is great then 8");
		if (ClientID.length() > 8)
			throw new IllegalArgumentException(
					"ClientID is length is great then 8");
		int len = 42;
		buf = new byte[len];
		TypeConvert.int2byte(len, buf, 0); // PacketLength
		TypeConvert.int2byte(RequestId.Login, buf, 4); // RequestID
		System.arraycopy(ClientID.getBytes(), 0, buf, 12, ClientID.length()); // ClientID

		// 生成sharekey
		String timeStamp = DateUtil.GetTimeStamp();
		byte[] sharekey = Key.GenerateShareKey(ClientID, shared_Secret,
				timeStamp);
		System.arraycopy(sharekey, 0, buf, 20, sharekey.length); //sharekey
		//System.out.println("LoginMode:"+LoginMode);
		buf[36] = (byte)LoginMode;                               //LoginMode
		TypeConvert.int2byte(Integer.parseInt(timeStamp), buf, 37);  //TimeStamp
		//System.out.println("login:"+Hex.rhex(buf));
	}
}
