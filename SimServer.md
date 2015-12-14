#SMGP Server模拟器

# 概述 #

> 在[Downloads](Downloads.md)下载SmgpServerSimulate.zip。
> > 该模拟器需要安装jdk1.5以上才能运行。
> > server目录为服务器端模拟器目录，双击start.bat即可运行
> > client目录下为客户端测试程序，src目录下有源代码。
> > lib目录下为相关的jar文件


# 帮助 #

可以在窗口相应命令
help:帮助信息
list:列出当前连接的客户端
d:模拟发送上行消息

> d 手机号码 SP接入号 短信内容
> 例如：模拟手机号码 18905718888 给10628888发送短信hello world
> > 那么使用命令:d 18905718888 10628888 hello world