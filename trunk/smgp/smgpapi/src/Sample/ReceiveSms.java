package Sample;

import java.io.IOException;

import cn.com.zjtelecom.smgp.Client;
import cn.com.zjtelecom.smgp.bean.Deliver;
import cn.com.zjtelecom.smgp.bean.Result;
import cn.com.zjtelecom.smgp.bean.Submit;

public class ReceiveSms {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//读取配置文件对象
		Config config = new Config();
		
		//获取配置参数
		String host = config.get("smgwip");
		String account = config.get("smgwaccount");
		String passwd = config.get("smgwpasswd");
		String spid = config.get("smgwspid");
		String spnum = config.get("smgpspnum");
		int port = Integer.parseInt(config.get("smgwport"));
		
		//目标号码：18918911891
		String destnum =config.get("destnum");
		String content =config.get("content");;
		
		//初始化client
		Client client = new Client(host, port, 2,account, passwd,spid, 0);
		
		do{
			Deliver deliver = client.OnDeliver();
			if (deliver.IsReport==1){
				System.out.println("-----------------");
				System.out.println("Got MsgReport");
				System.out.println("-----------------");
				System.out.println("MsgID       : "+deliver.MsgID);
				System.out.println("RecvTime    : "+deliver.RecvTime);
				System.out.println("MsgContent  : "+new String(deliver.MsgContent));
				System.out.println("ReportMsgID : "+deliver.ReportMsgID);
				System.out.println("-----------------");
			} else {
				String encode="ISO8859-1";
				if (deliver.MsgFormat==8) encode="iso-10646-ucs-2";
				if (deliver.MsgFormat==15) encode="GBK";
				
				System.out.println("-----------------");
				System.out.println("Got Deliver");
				System.out.println("-----------------");
				System.out.println("SrcTermID   : "+deliver.SrcTermID);
				System.out.println("DestTermID  : "+deliver.DestTermID);
				System.out.println("MsgID       : "+deliver.MsgID);
				System.out.println("RecvTime    : "+deliver.RecvTime);
				System.out.println("LinkID      : "+deliver.LinkID);
				System.out.println("MsgFormat   : "+deliver.MsgFormat);
				System.out.println("MsgContent  : "+new String(deliver.MsgContent,encode));
				System.out.println("-----------------");				
			}
			
		}while(true);
	}

}
