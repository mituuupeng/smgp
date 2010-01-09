package cn.com.zjtelecom.smgp.server.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.com.zjtelecom.smgp.bean.Deliver;
import cn.com.zjtelecom.smgp.server.inf.ServerEventInterface;

public class ServerConsole extends Thread {
	private ServerEventInterface serverEventInterface;

	public void run() {
		do {
			String command = readComand();
			if (command.equals("")) {
				continue;
			} else if (command.equalsIgnoreCase("help")
					|| command.equalsIgnoreCase("h")) {
				Help();
				continue;
			} else if (command.indexOf("D") == 0 || command.indexOf("d") == 0) {
				String[] para = command.split(" ");
				if (para.length < 4) {
					System.out.println("Error Command");
					continue;
				} else {
					
					if (checkNum(para[1]) == false || checkNum(para[2]) == false ){
						System.out.println("SrcNum or DestNum arguments Must be Number!");
						continue;
					}
					System.out.println("SrcNum:" + para[1]);
					System.out.println("DestNum:" + para[2]);
					System.out.println("Content:" + para[3]);
					Deliver deliver = new Deliver();
					deliver.IsReport = 0;
					deliver.MsgFormat = 15;
					deliver.SrcTermID = para[1];
					deliver.DestTermID = para[2];
					deliver.MsgContent = para[3].getBytes();
					deliver.MsgLength = deliver.MsgContent.length;
					

					this.serverEventInterface.SendDeliver(deliver);

					continue;
				}
			}
			System.out.println("Unknow Command!");
		} while (true);
	}

	public ServerConsole(ServerEventInterface serverinf) {
		this.serverEventInterface = serverinf;
	}

	private void Help() {
		System.out.println("----------------help----------------");
		System.out.println("Help��H[elp]");
		System.out.println("Deliver��D SrcNum DestNum Content");
		System.out.println("------------------------------------");
	}

	private boolean  checkNum(String checkstring) {
		for (int i = 0; i < checkstring.length(); i++) {
		  if (!Character.isDigit(checkstring.charAt(i))){
			  return false;
		  }
		}
		return true;
	}

	private String readComand() {
		String com = "";

		try {
			BufferedReader lineOfText = new BufferedReader(
					new InputStreamReader(System.in));
			com = lineOfText.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return com;
	}
}