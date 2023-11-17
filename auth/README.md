WT可以使用HMAC算法或使用RSA的公钥/私钥对来签名，防止被篡改
CMD命令行进入到JDK安装目录的bin目录下, 使用keytool命令生成zj.jks证书

keytool -genkey -alias sugar -keyalg RSA -keystore D:\jwt\zj.jks

-genkeypair：生成一对非对称密钥。
-alias：指定条目以及密钥对的别名，该别名是公开的。
-keyalg：指定加密算法，本例中采用通用的RSA算法。
-keystore：设定密钥库文件的存放路径以及文件名字。

执行命令后会要求输入基本信息
这里我的密码是：zzzz1111

查看test.keystore密钥库的信息，会列出所包含的条目的信息
keytool -list -v -keystore D:\jwt\zj.jks -storepass "zzzz1111"

删除JWT密钥库中的别名为“sugar”的条目
keytool -delete -alias sugar -keystore D:\jwt\zj.jks  -storepass "zzzz1111"

导出到一个安全证书sugar.crt
keytool -export -alias sugar -keystore D:\jwt\zj.jks -file D:\jwt\sugar.crt -storepass "zzzz1111"

首先我们使用keytool生成RSA证书zj.jks，复制到auth工程的resource目录下