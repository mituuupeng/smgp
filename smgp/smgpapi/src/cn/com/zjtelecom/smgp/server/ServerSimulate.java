package cn.com.zjtelecom.smgp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import cn.com.zjtelecom.smgp.bean.Login;
import cn.com.zjtelecom.smgp.bean.Submit;
import cn.com.zjtelecom.smgp.server.inf.ServerEventInterface;
import cn.com.zjtelecom.smgp.server.result.LoginResult;
import cn.com.zjtelecom.smgp.server.result.SubmitResult;
import cn.com.zjtelecom.smgp.server.util.ClientStatus;

public class ServerSimulate extends Thread {
	private int serverPort = 8890;
	private ServerEventInterface serverEventInterface;
	private ServerSocket server;
	private int connectCount = 0;
	private int TimeOut = 60*15; //ȱʡ15�볬ʱ
	
	public int getTimeOut() {
		return TimeOut;
	}

	public void setTimeOut(int timeOut) {
		TimeOut = timeOut;
	}


	public HashMap<String, ClientStatus> getClientlist() {
		return clientlist;
	}

	public void setClientlist(HashMap<String, ClientStatus> clientlist) {
		this.clientlist = clientlist;
	}

	private HashMap<String, ClientStatus> clientlist = new HashMap<String, ClientStatus>();

	public ServerSimulate(ServerEventInterface serverEventInterface, int port) {
		this.serverEventInterface = serverEventInterface;
		this.serverPort = port;
	}

	public void run() {
		try {
			server = new ServerSocket(this.serverPort);
			ActiveTestThread activeTestThread = new ActiveTestThread(this);
			activeTestThread.start();
			while (true) {
				Socket clientsocket = server.accept();
				ServerHandleConnect serverHandleConnect = new ServerHandleConnect(
						this, clientsocket,this.TimeOut);
				serverHandleConnect.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void connected(String account, String ipaddress,
			ServerHandleConnect serverHandleConnect) {
		String key = account + "$" + ipaddress;
		System.out.println("IpAddress:" + ipaddress + "," + "Account:"
				+ account + " has connected!");
		if (this.clientlist.get(key) == null) {
			this.clientlist.put(key, new ClientStatus(account, ipaddress,
					serverHandleConnect));
		} else {
			this.clientlist.get(key).AddNew(serverHandleConnect);
		}
		connectCount++;
	}

	public void disconnect(String account, String ipaddress,
			ServerHandleConnect serverHandleConnect) {
		String key = account + "$" + ipaddress;
		if (this.clientlist.get(key).removeClientConnected(serverHandleConnect)) {
			System.out.println("IpAddress:" + ipaddress + "," + "Account:"
					+ account + " all has disconnected!");
			this.clientlist.remove(key);
		} else {
			System.out.println("IpAddress:" + ipaddress + "," + "Account:"
					+ account + "  has disconnected!");
		}

	}

	public void disconnect(ServerHandleConnect serverHandleConnect) {
		System.out.println("other disconnected");
		serverHandleConnect.stop();
		serverHandleConnect.destroy();

	}

	public LoginResult onLogin(Login login,
			ServerHandleConnect serverHandleConnect) {
		LoginResult loginresult = this.serverEventInterface.onLogin(login);
		if (loginresult.getStatus() == 0) {
			this.connected(login.Account, login.ipaddress, serverHandleConnect);
		}

		return loginresult;
	}

	public SubmitResult onSumit(Submit submit) {
		// TODO Auto-generated method stub
		return this.serverEventInterface.onSumit(submit);
	}

}
