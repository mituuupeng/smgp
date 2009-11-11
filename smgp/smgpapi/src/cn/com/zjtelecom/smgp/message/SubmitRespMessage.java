package cn.com.zjtelecom.smgp.message;

import cn.com.zjtelecom.util.Hex;
import cn.com.zjtelecom.util.TypeConvert;

public class SubmitRespMessage extends Message {
	private String MsgID = "";
	private int Status = 0;

	public SubmitRespMessage(byte[] buffer) {
		
		if (buffer.length != 18) {
			System.out.println("SubmitResp Package resolv Error:"+Hex.rhex(buffer));
			
			this.buf = new byte[buffer.length];
			System.arraycopy(buffer, 0, this.buf, 0, this.buf.length);
			return;
		}
		
		//System.out.println(Hex.rhex(buffer));
		/*if (buffer.length != 18) {
			throw new IllegalArgumentException(
					"SubmitResp Package resolv Error");
		}*/
		this.buf = new byte[buffer.length];
		System.arraycopy(buffer, 0, this.buf, 0, this.buf.length);
		byte[] msgid = new byte[10];
		this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
		System.arraycopy(this.buf, 4, msgid, 0, msgid.length);
		this.MsgID = Hex.rhex(msgid);
		//byte[] status =new byte[4];
		//System.arraycopy(this.buf, 8, msgid, 0, status.length);
		this.Status = TypeConvert.byte2int(this.buf, 14);
	}


	public String getMsgID() {
		return MsgID;
	}

	public void setMsgID(String msgID) {
		MsgID = msgID;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
