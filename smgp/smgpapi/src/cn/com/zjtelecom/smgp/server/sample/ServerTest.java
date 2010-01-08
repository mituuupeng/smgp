package cn.com.zjtelecom.smgp.server.sample;

import java.io.IOException;
import cn.com.zjtelecom.smgp.bean.Login;
import cn.com.zjtelecom.smgp.bean.Submit;
import cn.com.zjtelecom.smgp.server.ServerSimulate;
import cn.com.zjtelecom.smgp.server.inf.ServerEventInterface;
import cn.com.zjtelecom.smgp.server.result.LoginResult;
import cn.com.zjtelecom.smgp.server.result.SubmitResult;
import cn.com.zjtelecom.smgp.server.sample.config.ServerAccountConfFromFile;
import cn.com.zjtelecom.smgp.server.sample.config.ServerAccountConfig;
import cn.com.zjtelecom.util.Key;

public class ServerTest {

	private static ServerAccountConfig accountconfig;

	private static class server implements ServerEventInterface {
		
		public server(int port) {
			ServerSimulate serverSimulate = new ServerSimulate(this, port);
			serverSimulate.start();
		}

		public LoginResult onLogin(Login login) {
			// TODO Auto-generated method stub
			System.out.println("Account:" + login.Account);
			System.out.println("sequence_Id:" + login.sequence_Id);
			System.out.println("Version:" + login.Version);
			System.out.println("LoginMode:" + login.LoginMode);

			if (accountconfig.getPassword(login.Account) == null) {
				return (new LoginResult(52, "", "3.0", ""));
			} else if (Key.checkAuth(login.AuthenticatorClient, login.Account,
					accountconfig.getPassword(login.Account), String
							.valueOf(login.timestamp)) == false) {
				return (new LoginResult(21, "", "3.0", ""));
			}else if (!accountconfig.getIpAddress(login.Account).equals(login.ipaddress)){
				return (new LoginResult(20, "", "3.0", ""));
			}

			return (new LoginResult(0, "lulin", "3.0", "10620068"));
		}

		public SubmitResult onSumit(Submit submit) {
			System.out.println("MsgType:" + submit.getMsgType());
			System.out.println("MsgFormat:" + submit.getMsgFormat());
			System.out.println("SrcTermid:" + submit.getSrcTermid());
			return new SubmitResult(0, 571063);
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
