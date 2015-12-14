## 概述 ##

> SMGP协议是中国电信为短信增值业务指定的接口协议，协议在在SMPP协议基础上进行扩展，扩展了相关计费和鉴权信息。目前SMGP协议有1.0，2.0，3.0三个版本的协议。1.0协议目前基本上停用，而2.0目前在部分省小灵通接入仍然在使用。3.0协议时目前最新的协议，相比2.0协议主要增加了tlv可选字段。

> 该项目主要针对SMGP 3.0协议进行开发。

> 程序使用JAVA开发。

> 代码可以通过svn工具获取：**svn checkout http://smgp.googlecode.com/svn/trunk/ smgp-read-only**
> Downloads下打包下载的为出现重大更新才会提供

> 测试登录：
```
//Client("服务器ip地址","服务器端口号",LoginMode,"账号","密码","企业代码",显示模式)
Client client = new Client("189.189.189.189", 8890, 2, "account","password", "12100001", 0);
Result result = client.Login();
System.out.println("ErrorCode:"+result.ErrorCode);
System.out.println("ErrorDescription:"+result.ErrorDescription);
```


> 发送一条消息
```
//Client("服务器ip地址","服务器端口号",LoginMode,"账号","密码","企业代码",显示模式)
Client client = new Client("189.189.189.189", 8890, 2, "account","password", "12100001", 0);
//submit("主叫号码","被叫号码","内容","产品编号")
Submit submit = new Submit("10620068", "18967441118", "你好！","112000000000061090527"); 
Result result = client.Send(submit);
System.out.println("ErrorCode:"+result.ErrorCode);
System.out.println("MsgID:"+result.ErrorDescription);
```

## 更新情况 ##
> 2010年1月13日，由于部分网关发送的ActiveTest频率较低，导致程序在缺省的60秒内出现超时，修正程序在出现60秒没有数据过来的时候，自动往服务器上发送一个ActiveTest

> 2010年1月9日 增加SMGP服务器模拟器，可以在[Downloads](Downloads.md)下载SmgpServerSimulate.zip ，运行以后会在当前ip地址在端口8890启动一个模拟器，具体查看zip文件中的readme

> 2010年1月8日 修正LoginMessage未送version错误；修正SubmitMessage中tlv长度计算错误（导致发送纯英文的长短信，导致封包错误，服务器关闭连接）；修正异常断开后，由于有SubmitResp需要接收时，api死锁问题。

> 2009年12月24日 增加一个cn.com.zjtelecom.smgp.ClientEvent类，调整了Submit\_resp和Deliver的处理机制，这个类需要传入一个实现接口cn.com.zjtelecom.smgp.inf.ClientEventInterface接口的类，当出现Submit\_resp和Deliver时候，ClientEvent会回调相应接口 特别注意该类未充分测试

> 2009年11月20日 修正cn.com.zjtelecom.smgp.connect.PConnect 方法LogOut出现处理问题，导致无法立即断开连接。导致在调用Client.Close()时候需要等待60秒后才退出。

## 适配情况 ##

浙江华为CDMA网关测试通过

重庆中兴CDMA网关测试通过


