package cn.com.zjtelecom.smgp.server.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cn.com.zjtelecom.smgp.bean.Deliver;
import cn.com.zjtelecom.smgp.bean.Login;
import cn.com.zjtelecom.smgp.bean.Submit;
import cn.com.zjtelecom.smgp.protocol.Tlv;
import cn.com.zjtelecom.smgp.protocol.TlvId;
import cn.com.zjtelecom.smgp.server.Server;
import cn.com.zjtelecom.smgp.server.inf.ServerEventInterface;
import cn.com.zjtelecom.smgp.server.result.LoginResult;
import cn.com.zjtelecom.smgp.server.result.SubmitResult;
import cn.com.zjtelecom.smgp.server.sample.config.ServerAccountConfFromFile;
import cn.com.zjtelecom.smgp.server.sample.config.ServerAccountConfig;
import cn.com.zjtelecom.smgp.server.util.CheckValid;
import cn.com.zjtelecom.util.Key;

public class SampleServer {

	private static ServerAccountConfig accountconfig;
	private static String ServerVersion ="3.0";

	private static class server implements ServerEventInterface {
		private Server serverSimulate;
        private ServerConsole serverConsole;
		
        public server(int port) {
			this.serverSimulate = new Server(this, port);
			this.serverSimulate.start();
			this.serverConsole = new ServerConsole(this);
			this.serverConsole.start();

		}

		public void SendDeliver(Deliver deliver) {
			this.serverSimulate.SendDeliver(deliver);
		}

		public LoginResult onLogin(Login login) {
			// TODO Auto-generated method stub
			System.out.println("------------New Login------------");
			System.out.println("Account:" + login.Account);
			System.out
					.println("SPNum:" + accountconfig.getSPNum(login.Account));
			System.out.println("Version:" + login.Version);
			System.out.println("LoginMode:" + login.LoginMode);

			if (accountconfig.getPassword(login.Account) == null) {
				return (new LoginResult(52, "", ServerVersion, ""));
			} else if (Key.checkAuth(login.AuthenticatorClient, login.Account,
					accountconfig.getPassword(login.Account), String
							.valueOf(login.timestamp)) == false) {
				return (new LoginResult(21, "", ServerVersion, ""));
			} else if (!accountconfig.getIpAddress(login.Account).equals(
					login.ipaddress)) {
				return (new LoginResult(20, "", ServerVersion, ""));
			}

			return (new LoginResult(0,
					accountconfig.getPassword(login.Account), ServerVersion,
					accountconfig.getSPNum(login.Account)));
		}

		public SubmitResult onSumit(Submit submit,String account) {
			int checkvalue = 0; 
			System.out.println("------------Get Submit------------");
			System.out.println("MsgType:" + submit.getMsgType());
			System.out.println("MsgFormat:" + submit.getMsgFormat());
			System.out.println("SrcTermid:" + submit.getSrcTermid());
			System.out.println("DestTermid:" + submit.getDestTermid());
			System.out.println("MsgLength:" + submit.getMsgLength());
			System.out.println("ProductID:" + submit.getProductID());
			System.out.println("LinkID:" + submit.getLinkID());
			System.out.println("TP_udhi:" + submit.getTP_udhi());
				
			try {
				if (submit.getMsgFormat() == 8) {
					System.out.println("MsgContent:"
							+ new String(submit.getMsgContent(),
									"iso-10646-ucs-2"));
				} else if (submit.getMsgFormat() == 15) {
					System.out.println("MsgContent:"
							+ new String(submit.getMsgContent(), "gbk"));
				} else {
					System.out.println("MsgContent:"
							+ new String(submit.getMsgContent()));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			
			//check SPId valid
			if (accountconfig.getSPId(account)!=null){
				int findTlv =0;
				Tlv [] otherTlv = submit.getOtherTlvArray();
				for (int i=0;i<otherTlv.length;i++){
					if (otherTlv[i].Tag == TlvId.MsgSrc){
						if (otherTlv[i].Value.equals(accountconfig.getSPId(account))){
							findTlv =1;  
						}else{
							checkvalue =8200;
						}
					}
				}
				if (findTlv==0) checkvalue =8200;
				
			}
			
			//check SrcTermid valid
			if ((submit.getSrcTermid()).indexOf(accountconfig.getSPNum(account)) !=0)
				checkvalue=46;
			//check Other Valid
			if (checkvalue==0) checkvalue = CheckValid.CheckSubmit(submit);
			System.out.println("SubmitResult:"+checkvalue);
			return new SubmitResult(checkvalue);
		}

	}

	public static void main(String[] args) {
		try {
			accountconfig = new ServerAccountConfFromFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server sv = new server(8890);
	}

}
