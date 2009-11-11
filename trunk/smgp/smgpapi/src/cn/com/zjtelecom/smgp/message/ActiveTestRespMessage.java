package cn.com.zjtelecom.smgp.message;

import cn.com.zjtelecom.smgp.protocol.RequestId;
import cn.com.zjtelecom.util.TypeConvert;

public class ActiveTestRespMessage extends Message{
    public ActiveTestRespMessage(){
        int len = 12;
        buf = new byte[len];
        TypeConvert.int2byte(len, buf, 0);
        TypeConvert.int2byte(RequestId.ActiveTest_Resp, buf, 4);
    } 
}
