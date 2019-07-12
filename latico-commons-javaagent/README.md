# 环境
  JDK/lib/tools.jar这个jar需要添加进来。

# JVM代理功能组件.
基于 JVM TI (JVM Tool Interface) 实现的 Java ClassFile 的增强.

可以在类第一次加载之前修改，加载之后修改需要重新创建类加载器。或者在自定义的类加载器种修改，但这种方式比较耦合。
javaagent 的主要功能如下：

可以在加载 class 文件之前做拦截，对字节码做修改
可以在运行期对已加载类的字节码做变更，但是这种情况下会有很多的限制，后面会详细说
还有其他一些小众的功能
获取所有已经加载过的类
获取所有已经初始化过的类（执行过 clinit 方法，是上面的一个子集）
获取某个对象的大小
将某个 jar 加入到 bootstrap classpath 里作为高优先级被 bootstrapClassloader 加载
将某个 jar 加入到 classpath 里供 AppClassloard 去加载
设置某些 native 方法的前缀，主要在查找 native 方法的时候做规则匹配

https://www.jianshu.com/p/0bbd79661080
https://www.jianshu.com/p/6096bfe19e41
https://www.infoq.cn/article/javaagent-illustrated/

# java代理加载有两种方式
  1、在启动时加载 instrument agent
  2、在运行时加载 instrument agent

## 方式1：在启动时加载 instrument agent的实现
 注意：
 需要先把com.latico.commons.javaagent.JavaAgentExample这个类打包成一个jar，例如打包成jvm-agent.jar，请使用IDEA或者eclipse生成jar的方式，
 里面必须包含：com.latico.commons.javaagent.JavaAgentExample和META-INF/MANIFEST.MF，也可以使用maven打包jar后，
 手工修改jar包内容，替换META-INF/MANIFEST.MF文件。
 这里已经打包好了在：lib/jvm-agent.jar
 
 IDEA打包jar：
 (1) file -> project structure 打开project structure窗口
 (2) artifacts -> 点击+号 -> jar -> From module with dependencies
 (3) build -> build artifacts... 会出现以下选择框，选择需要打包的包名 -> build
 (4) project名称/out/artifacts/jar包名称/jar包名称.jar
 
 eclipse打包jar：
 直接右键export jar

build/assembly/javapath.cfg已经配置好了，使用maven install后，可以利用启动脚本start.bat启动查看结果。

## 方式2：在运行时加载 instrument agent的实现
  在运行时加载的方式，大致按照下面的方式来操作：
  VirtualMachine vm = VirtualMachine.attach(pid); 
  vm.loadAgent(agentPath, agentArgs); 
  
  注意VirtualMachine类在JDK的tools.jar
  
  同样需要打包用到代理jvm-agent.jar，启动 AccountMain 后，再启动JVMTIMain 类，或者把JVMTIThread类放到AccountMain中启动
  
  监视jar变化的时候可以通过监听文件夹的jar文件变化，然后调用loadAgent通知目标虚拟机重新load，例如Apache有文件夹监听器。
  
# 使用开源项目：使用 spring-loaded 实现 jar 包热部署
  在项目开发中我们可以把一些重要但又可能会变更的逻辑封装到某个 logic.jar 中，当我们需要随时更新实现逻辑的时候，可以在不重启服务的情况下让修改后的 logic.jar 被重新加载生效。
  
  spring-loaded是一个开源项目，项目地址:https://github.com/spring-projects/spring-loaded
  
  使用方法：
  
  在启动主程序之前指定参数
  -javaagent:C:/springloaded-1.2.5.RELEASE.jar -noverify
  
  如果你想让 Tomat 下面的应用自动热部署，只需要在 catalina.sh 中添加：
  set JAVA_OPTS=-javaagent:springloaded-1.2.5.RELEASE.jar -noverify
  
  这样就完成了 spring-loaded 的安装，它能够自动检测Tomcat 下部署的webapps ，在不重启Tomcat的情况下，实现应用的热部署。
  
  通过使用 -noverify 参数，关闭 Java 字节码的校验功能。 
  使用参数 -Dspringloaded=verbose;explain;watchJars=tools.jar 指定监视的jar （verbose;explain; 非必须），多个jar用“冒号”分隔，如 watchJars=tools.jar:utils.jar:commons.jar
  
  
