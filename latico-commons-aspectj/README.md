教程：
https://blog.csdn.net/lhy_601/article/details/52413131
https://blog.51cto.com/7317859/2109140

下载AspectJ:可以从Eclipse的官网下载到AspectJ的.jar文件和源代码文件。
AspectJ官网地址:https://eclipse.org/aspectj

下载后得到的是一个打包好的.jar文件，不能直接通过解压的方式安装，需要用如下命令安装
java -jar aspectj-1.1.0.jar

安装之后需要再添加如下两个环境变量

把/lib/aspectjrt.jar 添加到CLASSPATH环境变量
把/bin添加到PATH环境变量 
Linux用户可以在~/.bashrc文件下添加以下代码：

export PATH=$PATH:/home/latico/devTool/aspectj1.8/bin
export CLASSPATH=/home/latico/devTool/aspectj1.8/lib/aspectjrt.jar:$CLASSPATH:

验证安装是否成功请用一下命令： ajc

配置IDEA
根据IDEA官网的步骤是要做以下几步的步骤的，不过根据本人的经验（因为我就是根据官网的步骤去做，完了之后发现起始很多步骤并不用处理大家姑且看下吧）

Enabling AspectJ Support Plugins 

编写业务示例代码

