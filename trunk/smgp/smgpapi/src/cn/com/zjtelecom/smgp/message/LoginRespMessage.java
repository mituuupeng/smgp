package cn.com.zjtelecom.smgp.message;

import cn.com.zjtelecom.util.TypeConvert;

public class LoginRespMessage extends Message {
	private int Status = 0;
	private byte[] AuthenticatorServer =null;
	private String Version = "";

	public LoginRespMessage(byte[] buffer) {
		if (buffer.length != 25) {
			throw new IllegalArgumentException("LoginResp Package resolv Error");
		} else {
			this.buf = new byte[25];
			System.arraycopy(buffer, 0, this.buf, 0, buffer.length);
			this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
			this.Status = TypeConvert.byte2int(this.buf, 4);
			AuthenticatorServer = new byte[16];
			System.arraycopy(this.buf, 8, AuthenticatorServer, 0, AuthenticatorServer.length);
			//this.AuthenticatorServer = new String(tmp);
			this.Version = (int)(this.buf[24]/16)+"."+((int)(this.buf[24])%16);
		}
	}
	public String getServerVersion(){
		return this.Version;
	}
	
	public int getSequenceId(){
		return this.sequence_Id;
	}
	
	public int getStatus(){
		return this.Status;
	}
	
	public byte[] getAuthenticatorServer(){
	   return this.AuthenticatorServer;	
	}
}
