package cn.com.zjtelecom.smgp.message;

import cn.com.zjtelecom.smgp.protocol.RequestId;
import cn.com.zjtelecom.smgp.protocol.Tlv;
import cn.com.zjtelecom.util.Hex;
import cn.com.zjtelecom.util.TypeConvert;

public class SubmitMessage extends Message {
	public SubmitMessage(int msgtype, int needreport, int priority,
			String serviceid, String feetype, String feecode, String fixedfee,
			int msgformat, String validtime, String attime, String srctermid,
			String chargetermid, String[] desttermid, int msglength,
			byte[] msgcontent, byte[] reserve, Tlv[] tlvarray, int SequenceId) {
		this.sequence_Id=SequenceId;
		int tlvlength = 0;
		for (int i = 0; i < tlvarray.length; i++) {
			tlvlength += tlvarray[i].Length + 4;
		}

		int len = 126 + 21 * desttermid.length + msgcontent.length
				+ tlvlength;
		buf = new byte[len];
		// System.out.println("len:"+len);
		TypeConvert.int2byte(len, buf, 0); // PacketLength
		TypeConvert.int2byte(RequestId.Submit, buf, 4); // RequestID
		TypeConvert.int2byte(this.sequence_Id, buf, 8); // RequestID
		buf[12] = (byte) msgtype;
		buf[13] = (byte) needreport;
		buf[14] = (byte) priority;
		System.arraycopy(serviceid.getBytes(), 0, buf, 15, serviceid.length());
		System.arraycopy(feetype.getBytes(), 0, buf, 25, feetype.length());
		System.arraycopy(feecode.getBytes(), 0, buf, 27, feecode.length());
		System.arraycopy(fixedfee.getBytes(), 0, buf, 33, fixedfee.length());
		buf[39] = (byte) msgformat;
		if (validtime != null && validtime.length() > 0)
			System.arraycopy(validtime.getBytes(), 0, buf, 40, 16);
		if (attime != null && attime.length() > 0)
			System.arraycopy(attime.getBytes(), 0, buf, 57, 16);
		System.arraycopy(srctermid.getBytes(), 0, buf, 74, srctermid.length());
		System.arraycopy(chargetermid.getBytes(), 0, buf, 95, chargetermid
				.length());
		buf[116] = (byte) desttermid.length;
		int i = 0;
		for (i = 0; i < desttermid.length; i++)
			System.arraycopy(desttermid[i].getBytes(), 0, buf, 117 + i * 21,
					desttermid[i].length());

		int loc = 117 + i * 21;
		//System.out.println("DEbug msglen:"+msgcontent.length);
		//System.out.println("msg:"+new String(msgcontent));
		buf[loc] = (byte) msgcontent.length;
		System.arraycopy(msgcontent, 0, buf, loc + 1, msgcontent.length);
		loc = loc + msgcontent.length + 1;
		if (reserve != null)
			System.arraycopy(reserve, 0, buf, loc, reserve.length);
		loc = loc + 8;
		// System.out.println("loc:"+loc);
		//System.out.print("tlv:");
		for (i = 0; i < tlvarray.length; i++) {
            //System.out.println("Tag:"+(tlvarray[i].Tag));
            //System.out.print("Tag:"+Hex.rhex(tlvarray[i].TlvBuf));
			System.arraycopy(tlvarray[i].TlvBuf, 0, buf, loc,
					tlvarray[i].TlvBuf.length);
			loc = loc + tlvarray[i].TlvBuf.length;
			//System.out.println("loc:" + loc);
		}
		//System.out.println();

	}
}
