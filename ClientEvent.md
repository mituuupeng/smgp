#类：cn.com.zjtelecom.smgp.ClientEvent

# 事件触发方式的类 #

由于cn.com.zjtelecom.smgp.Client在提交Submit消息时，等待Submit\_Resp，导致发送速度较慢。ClientEvent类修改为发送Submit消息时，只返回Submit包的SeqID,而Submit\_Resp和Deliver通过调用程序实现接口ClientEventInterface的方法，ClientEvent通过onDeliver和OnSubmitResp来回调该方法，通知程序收到Submit\_resp和上行消息


# 例子 #


```
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cn.com.zjtelecom.smgp.ClientEvent;
import cn.com.zjtelecom.smgp.Exception.SubmitException;
import cn.com.zjtelecom.smgp.bean.Deliver;
import cn.com.zjtelecom.smgp.bean.Result;
import cn.com.zjtelecom.smgp.bean.Submit;
import cn.com.zjtelecom.smgp.bean.SubmitResp;
import cn.com.zjtelecom.smgp.inf.ClientEventInterface;
import cn.com.zjtelecom.stock.Stock;

public class StockTest {
	/*
	 * Config类为获取配置文件类，系统缺省读取当前目录下的smgpc.ini
	 * 配置文件应包含以下参数
	 * smgwip=短信网关ip地址
	 * smgwport=短信网关端口
	 * smgwaccount=账号
	 * smgwpasswd=密码
	 * smgwspid=企业代码
	 * 
	 * */
	private static class Config {
		 private static String configFile="smgpc.ini";
		 private  HashMap <String,String> configproperty=new HashMap<String,String>();
		 
	   public Config() throws IOException {
	  	 this.ReadProperty(configFile);
	   }
		 public  Config(String configFile2) throws IOException {
			// TODO Auto-generated method stub
			 this.ReadProperty(configFile2);
		}
		private  void ReadProperty(String cfile) throws IOException{
				String line = "";
				FileInputStream fileinputstream = new FileInputStream(cfile);
				BufferedReader bufferedreader = new BufferedReader(
						new InputStreamReader(fileinputstream));
				do {
					if ((line = bufferedreader.readLine()) == null)
						break;
					if (line.indexOf("#") == 0 || line.indexOf("=") < 0)
						continue;
					String key=line.substring(0,line.indexOf("="));
					String value=line.substring(line.indexOf("=")+1);
					
					//System.out.println("key:"+key);
					//System.out.println("value:"+value);
					configproperty.put(key,value);
					
				} while(true);
		 }
		public  String get(String key){
			return configproperty.get(key);
		}
	}
	
	/*
	 * SmgpClient实现接口ClientEventInterface的方法,ClentEvnet会将
	 * OnSubmitResp，onDeliver回调给该类
	 * 
	 * */
	private static class SmgpClient implements ClientEventInterface {
		public ClientEvent client = null;

		public SmgpClient(String host, int port, int loginmode,
				String clientid, String clientpasswd, String spid,
				int displaymode) {
			this.client = new ClientEvent(this, host, port, loginmode,
					clientid, clientpasswd, spid, displaymode);
		}

		public void OnSubmitResp(SubmitResp submitResp) {
			System.out.println("-----------------GetSubmitResp-----------------");
			System.out.println("ResultCode:" + submitResp.getResultCode());
			System.out.println("MsgID:" + submitResp.getMsgID());
			System.out.println("SequenceID:" + submitResp.getSequenceID());
			System.out.println("DestNum:"
					+ submitResp.getSubmit().getDestTermid());

		}

		public void onDeliver(Deliver deliver) {
			System.out.println("-----------------GetDeliver-----------------");
			System.out.println("IsReport:" + deliver.IsReport);
			System.out.println("DestTermID:" + deliver.DestTermID);
			System.out.println("SrcTermID:" + deliver.SrcTermID);
			System.out.println("LinkID:" + deliver.LinkID);
			System.out.println("ReportMsgID:" + deliver.ReportMsgID);
			System.out.println("MsgContent:" + new String(deliver.MsgContent));

			if (deliver.IsReport == 0) {
				try {
					if (new String(deliver.MsgContent, "ISO8859-1").substring(
							0, 1).equalsIgnoreCase("s")) { // 如果是s开头的指令，表示用户是点播的股票业务
						String stockstr = Stock.getStock(new String(
								deliver.MsgContent, "ISO8859-1"));//该方法为获取股票数据，需要自行实现
						Submit submit = new Submit("10620068",
								deliver.SrcTermID, stockstr.getBytes("iso-10646-ucs-2"),
								"112000000000000001032");
						submit.setFeetype("01");
						submit.setFeeCode("0");
						submit.setMsgFormat(8);
						submit.setLinkID(deliver.LinkID);
						submit.setNeedReport(1);
						submit.setChargeTermid(deliver.SrcTermID);
						Integer seq[] = client.SendLong(submit);
						for (int i=0;i<seq.length;i++){
						System.out.println("SubmitSeq:"+seq[i]);
						}
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (SubmitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) throws IOException {
		Config configproperty=new Config();
		SmgpClient smgpclient = new SmgpClient(configproperty.get("smgwip"),
				Integer.parseInt(configproperty.get("smgwport")), 2,
				configproperty.get("smgwaccount"), configproperty
						.get("smgwpasswd"), configproperty
						.get("smgwspid"), 0);
		smgpclient.client.setSpeed(10); // 设置发送速度，单位为 条/秒
		Result loginresult= smgpclient.client.Login();
		System.out.println("LoginStatus:"+loginresult.ErrorCode);
		System.out.println("LoginDescription:"+loginresult.ErrorDescription);
		
		if (loginresult.ErrorCode!=0) {
			System.out.println("登录失败");
			smgpclient.client.Close(); //登录失败关闭client的链接，以便程序正常退出。
		} else {
			System.out.println("登录成功");
		}

	}

}

```