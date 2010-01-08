package cn.com.zjtelecom.smgp.server.inf;

import cn.com.zjtelecom.smgp.bean.Login;
import cn.com.zjtelecom.smgp.bean.Submit;
import cn.com.zjtelecom.smgp.server.result.LoginResult;
import cn.com.zjtelecom.smgp.server.result.SubmitResult;

public interface ServerEventInterface {
     public SubmitResult onSumit(Submit submit);
     public LoginResult  onLogin(Login login);

}
