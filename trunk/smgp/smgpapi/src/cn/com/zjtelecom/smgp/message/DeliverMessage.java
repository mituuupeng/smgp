package cn.com.zjtelecom.smgp.message;

import java.util.Vector;

import cn.com.zjtelecom.smgp.bean.Deliver;
import cn.com.zjtelecom.smgp.protocol.RequestId;
import cn.com.zjtelecom.smgp.protocol.Tlv;
import cn.com.zjtelecom.smgp.protocol.TlvId;
import cn.com.zjtelecom.util.Hex;
import cn.com.zjtelecom.util.TypeConvert;

public class DeliverMessage extends Message {
	public byte[] MsgID_BCD;
	public String MsgID;
	public int IsReport;
	public int MsgFormat;
	public String RecvTime;
	public String SrcTermID;
	public String DestTermID;
	public int MsgLength;
	public byte[] MsgContent;
	public byte[] Reserve;
	public String LinkID;
	public int TP_udhi;
	public String ReportMsgID;
	public Tlv[] OtherTlv;
	
	public DeliverMessage (Deliver deliver){
		//12是header,77是固定长度，还需包含tlv和msgconent
		int len = 12+77+deliver.MsgLength;
		buf = new byte[len];
		// System.out.println("len:"+len);
		TypeConvert.int2byte(len, buf, 0); // PacketLength
		TypeConvert.int2byte(RequestId.Submit, buf, 4); // RequestID
		TypeConvert.int2byte(this.sequence_Id, buf, 8); // sequence_Id
	}

	public DeliverMessage(byte[] buffer) {

		this.sequence_Id = TypeConvert.byte2int(buffer, 0);
		this.MsgID_BCD = new byte[10];
		System.arraycopy(buffer, 4, this.MsgID_BCD, 0, this.MsgID_BCD.length);
		this.MsgID = TypeConvert.getHexString(buffer, 4, 0, 10);
		this.IsReport = buffer[14];
		this.MsgFormat = buffer[15];
		this.RecvTime = TypeConvert.getString(buffer, 16, 0, 14);
		this.SrcTermID = TypeConvert.getString(buffer, 30, 0, 21);
		this.DestTermID = TypeConvert.getString(buffer, 51, 0, 21);
		this.MsgLength = buffer[72] & 0xFF;
		// System.out.println("len:"+this.MsgLength);
		this.MsgContent = new byte[this.MsgLength];
		System.arraycopy(buffer, 73, this.MsgContent, 0, this.MsgLength);

		if (this.IsReport == 1) {
			byte[] tmpmsgid = new byte[10];
			System.arraycopy(this.MsgContent, 3, tmpmsgid, 0, 10);
			this.ReportMsgID = Hex.rhex(tmpmsgid);
			// System.out.println("DeliverID:"+this.ReportMsgID);
		}

		this.Reserve = new byte[8];
		System.arraycopy(buffer, 73 + this.MsgLength, this.Reserve, 0,
				this.Reserve.length);

		byte[] tlv = new byte[buffer.length - 73 - this.MsgLength - 8];
		System.arraycopy(buffer, this.MsgLength + 73 + 8, tlv, 0, tlv.length);
		Vector tmptlv = new Vector();

		//System.out.println("tlv:" + Hex.rhex(tlv));
		for (int loc = 0; loc < tlv.length;) {
			int tlv_Tag = TypeConvert.byte2short(tlv, loc + 0);
			int tlv_Length = TypeConvert.byte2short(tlv, loc + 2);
			String tlv_Value = "";
			if (tlv_Tag == TlvId.Mserviceid || tlv_Tag == TlvId.SrcTermPseudo
					|| tlv_Tag == TlvId.DestTermPseudo
					|| tlv_Tag == TlvId.ChargeTermPseudo
					|| tlv_Tag == TlvId.LinkID) {
				tlv_Value = TypeConvert.getString(tlv, loc + 4, 0, tlv_Length);

				loc = loc + 4 +tlv_Length;
			} else {
				tlv_Value = String.valueOf(TypeConvert.byte2tinyint(tlv, loc + 4));
				loc = tlv_Length + 4 +1;
			}
			if (tlv_Tag == TlvId.TP_udhi) {
				this.TP_udhi=Integer.parseInt(tlv_Value);
			}
			else if (tlv_Tag == TlvId.LinkID) {
				this.LinkID = tlv_Value;
				// System.out.println("tlv_Tag:"+tlv_Tag);
				// System.out.println("tlv_Length:"+tlv_Length);
				// System.out.println("tlv_Value:"+tlv_Value);
				// System.out.println(Hex.rhex(tlv));
			} else {
				tmptlv.add(new Tlv(tlv_Tag, tlv_Value));
			}
			if (tmptlv.size() > 0) {
				this.OtherTlv = new Tlv[tmptlv.size()];
				for (int i = 0; i < tmptlv.size(); i++) {
					this.OtherTlv[i] = (Tlv) tmptlv.get(i);
				}
			}
		}
		// System.out.println("tlv:" + Hex.rhex(tlv));

	}
}
