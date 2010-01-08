package cn.com.zjtelecom.smgp.server.result;

import cn.com.zjtelecom.util.DateUtil;

public class SubmitResult {
	private int sequence_Id;
	private int Status;
	private String msgID;
	
	public int getSequence_Id() {
		return sequence_Id;
	}
	public void setSequence_Id(int sequenceId) {
		sequence_Id = sequenceId;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getMsgID() {
		return msgID;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public SubmitResult(int status,int emsnum){
		this.Status = status;
		this.msgID = GenerateMsgID(emsnum);
		
	}
	private static String GenerateMsgID(int emsnum){
		return emsnum+DateUtil.GetTimeString() +  getFourNum(); 
	}
	 
	private static String getFourNum(){
		String numstr = String.valueOf(DateUtil.getTimeStampL());
		return numstr.substring(numstr.length()-4);
	}
}
