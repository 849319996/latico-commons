/**
 * <PRE>
 *     类加载器
 *     双亲委派的逻辑在loadClass中
 ClassLoader类加载器，主要的作用是将class文件加载到jvm虚拟机中。jvm启动的时候，并不是一次性加载所有的类，而是根据需要动态去加载类，主要分为隐式加载和显示加载。

 　　隐式加载：程序代码中不通过调用ClassLoader来加载需要的类，而是通过JVM类自动加载需要的类到内存中。例如，当我们在类中继承或者引用某个类的时候，JVM在解析当前这个类的时，发现引用的类不在内存中，那么就会自动将这些类加载到内存中。

 　　显示加载：代码中通过Class.forName()，ClassLoader.LoadClass()，自定义类加载器中的findClass()方法等,
 ====================================================
 类的装载过程：
 1、加载；
    通过类的全限定名获取二进制字节流，将二进制字节流转换成方法区中的运行时数据结构，在内存中生成Java.lang.class对象；

 2、链接
   包括了：
     校验：检查导入类或接口的二进制数据的正确性；（文件格式验证，元数据验证，字节码验证，符号引用验证）
 　　准备：给类的静态变量分配并初始化存储空间；
 　　解析：将常量池中的符号引用转成直接引用；
 3、初始化
    激活类的静态变量的初始化Java代码和静态Java代码块，并初始化程序员设置的变量值。
 。
 类装载，Class.forName()和ClassLoader.LoadClass()的区别(ClassLoader.loadClass(className)方法不对类进行初始化操作)

 Class.forName(className)方法，内部实际调用的方法是  Class.forName0(className, true, ClassLoader.getClassLoader(caller), caller);
 第2个boolean参数表示类是否需要初始化，  Class.forName(className)默认是需要初始化。
 一旦初始化，就会触发目标对象的 static块代码执行，static参数也也会被再次初始化。
 ClassLoader.loadClass(className)方法，内部实际调用的方法是  ClassLoader.loadClass(className,false);
 第2个 boolean参数，表示目标对象是否进行链接，false表示不进行链接，由上面介绍可以，
 不进行链接意味着不进行包括初始化等一些列步骤，那么静态块和静态对象就不会得到执行

 ====================================================
 一、JVM核心类加载器的初始化，可以看{@link sun.misc.Launcher}的构造方法,

 // 创建并初始化扩展类加载器ExtClassLoader
 extclassloader = ExtClassLoader.getExtClassLoader();

 // 创建并初始化系统类加载器AppClassLoader，设置其父类加载器为ext，最后传给loader
 loader = AppClassLoader.getAppClassLoader(extclassloader);
 ====================================================
 二、类加载器介绍，每个类加载器都只能加载自己控制范围内的类，
 每个类只能被加载一次，委托机制是为了确保类存在的加载优先级（上层类加载器的类优先级比下层高，防止下层类加载器复写了上层类加载器的类），

 1、BootStrap，引导(或叫启动)类加载器,使用C/C++代码编写，封装到JVM内核，加载<Java_Home>/jre/lib下面的jar下面的核心类库或-Xbootclasspath选项指定的jar包。
 由native方法实现加载过程，程序无法直接获取到该类加载器，
 无法对其进行任何操作,是ExtClassLoader的父类加载器。

 2、ExtClassLoader，扩展（Extension）类加载器，是java类编写，加载<Java_Home>/jre/lib/ext/*.jar,
 是AppClassLoader的父类加载器，没有父类加载器,由sun.misc.Launcher.ExtClassLoader实现的。
 sun.misc.Launcher.ExtClassLoader#ExtClassLoader(java.io.File[])

 3、AppClassLoader，应用/系统（System）类加载器,由sun.misc.Launcher.AppClassLoader实现,加载System.getProperty("java.class.path")所指定的路径或jar。
 在使用Java运行程序时，也可以加上-cp来覆盖原有的Classpath设置，例如： java -cp ./lavasoft/classes HelloWorld，
 是java类编写,是ExtClassLoader的子类加载器，加载classpath中的所有类和资源。

 4、自定义类加载器(下面称为MyClassLoader，通过继承ClassLoader或者建议继承URLClassLoader)
 自定义类加载器的作用：jvm自带的三个加载器只能加载指定路径下的类字节码。
 如果某个情况下，我们需要加载应用程序之外的类文件呢？比如本地D盘下的，
 或者去加载网络上的某个类文件，这种情况就可以使用自定义加载器了。

 真实场景：典型的有动态代理。一般程序启动的时候都会把所需的全部资源(jar)都指定到classpath中，
 但是偏偏有些其他资源文件是在程序运行过程中动态生成在其他目录，或者其他程序上传到某个位置，
 让我们的程序在程序已经运行后再动态识别和加载。

 当所需要的资源不存在于BootStrap、ExtClassLoader、AppClassLoader所能加载的范围，
 那就需要使用自定义类加载器，用于加载其他资源。
 需要指定父类加载器（一般指定AppClassLoader作为父加载器），
 实例化自定义类加载器时，若不指定父类加载器（不把父ClassLoader传入构造函数）的情况下， 默认采用系统类加载器（AppClassLoader）作为父类加载器。

 5、线程上下文类加载器，与前面的四种类加载器不同，他不是一种新的类加载器，而是一种获取类加载器的机制,
 最初的目的是为了实现一种机制，让上层类加载器能使用到下层的类加载器对象，
 默认使用当前执行的代码所在应用的系统类加载器AppClassLoader。

 典型的例子：是注册数据库驱动实现类的时候:Class.forName("com.mysql.jdbc.Driver")，
 从Java1.6开始自带的jdbc4.0版本已支持SPI服务加载机制，现在不需要写这句代码了，
 读取jar包内META-INF/services下文件中的类名（文件名是spi类名，jdbc驱动类是:java.sql.Driver，文件内容是实现类路径：com.mysql.jdbc.Driver）,
 然后使用线程上下文类加载器(默认是系统类加载器)加载SPI文件中指定的实现类，这样就达到了，Bootstrap类加载器在运行时使用了系统类加载器加载类。

 Java 提供了很多服务提供者接口（Service Provider Interface，SPI），允许第三方为这些接口提供实现。常见的 SPI 有 JDBC、JCE、JNDI、JAXP 和 JBI 等。

 这些 SPI 的接口由 Java 核心库来提供，而这些 SPI 的实现代码则是作为 Java 应用所依赖的 jar 包被包含进类路径（CLASSPATH）里。SPI接口中的代码经常需要加载具体的实现类。那么问题来了，SPI的接口是Java核心库的一部分，是由**启动类加载器(Bootstrap Classloader)来加载的；SPI的实现类是由系统类加载器(System ClassLoader)**来加载的。引导类加载器是无法找到 SPI 的实现类的，因为依照双亲委派模型，BootstrapClassloader无法委派AppClassLoader来加载类。

 而线程上下文类加载器破坏了“双亲委派模型”，可以在执行线程中抛弃双亲委派加载链模式，使程序可以逆向使用类加载器。
 ====================================================
 三、
 父子并不是继承的意思，它们都是ClassLoader抽象类的实现，因此都含有一个ClassLoader parent成员变量，该变量指向其父加载器，类似单向链表。
 ====================================================
 四、
 类加载器的双亲委托机制(每个类只能加载一次，可能是为了保证优先级，
 已达到控制类的优先级的目的,那么jre/lib/rt.jar里面的优先级最高，再到jre/lib/ext/*.jar，而不会被使用者的类给覆盖)：

 当Java虚拟机要加载第一个类的时候，到底派出哪个类加载器去加载呢？

 (1). 首先当前线程的类加载器去加载线程中的第一个类(当前线程的类加载器：Thread类中有一个get/setContextClassLoader(ClassLoader cl);方法，可以获取/指定本线程中的类加载器)
 (2). 如果类A中引用了类B,Java虚拟机将使用加载类A的类加载器来加载类B
 (3). 还可以直接调用ClassLoader.loadClass(String className)方法来指定某个类加载器去加载某个类

 每个类加载器加载类时，又先委托给其上级类加载器当所有祖宗类加载器没有加载到类，
 回到发起者类加载器，还加载不了，则会抛出ClassNotFoundException,不是再去找发起者类加载器的儿子，
 因为没有getChild()方法。例如：如上图所示： MyClassLoader->AppClassLoader->ExtClassLoader->BootStrap.

 自定定义的MyClassLoader1首先会先委托给AppClassLoader,
 AppClassLoader会委托给ExtClassLoader,ExtClassLoader会委托给BootStrap，
 这时候BootStrap就去加载，如果加载成功，就结束了。如果加载失败，就交给ExtClassLoader去加载，
 如果ExtClassLoader加载成功了，就结束了，如果加载失败就交给AppClassLoader加载，
 如果加载成功，就结束了，如果加载失败，就交给自定义的MyClassLoader1类加载器加载，
 如果加载失败，就报ClassNotFoundException异常，结束。

 ====================================================
 常见的有以下两种获取资源文件的方法：

 方法一: Class.getClassLoader().getResourceAsStream(String name)

 Returns an input stream for reading the specified resource.

 The search order is described in the documentation for getResource(String).

 默认从classpath中找文件(文件放在resources目录下)，name不能带“/”，否则会抛空指针

 Class.getClassLoader().getResource(String path)

 path不能以'/'开头时，path是指类加载器的加载范围，在资源加载的过程中，使用的逐级向上委托的形式加载的，'/'表示Boot ClassLoader中的加载范围，因为这个类加载器是C++实现的，所以加载范围为null。

 方法二: Class.getResourceAsStream(String name)，该方式的 Class.getResource(String path)方法
 对资源路径进行了处理，如果是以/开头，那么就是会自动去掉/作为真正的资源路径，
 而不是以/开头的情况下，就会把当前类的包路径拼接传进来的路径作为一个路径传进去方法一的Class.getClassLoader().getResourceAsStream(String name)，
 所以方法二等于自动做了一层业务逻辑，所以用了哪种类型加载，路径写法要注意。

 查找资源通过给定名称，查询资源的规则与给定的类的class load来实现，这个方法由类的loader来执行，如果这个类由bootstrap加载，那么方法由ClassLoader.getSystemResourceAsStream代理执行。

 代理之前，绝对的资源名称通过传入的name参数以下算法进行构造：

 如果name以"/"开头，那么绝对路径是/后边跟的名字

 如果name不是以"/"开头，那么绝对路径是当前Class类的package名"."换成“/”以后再加name，例如：com.abc.App就是/com/abc/App/name 或者写作 :../../name(以class类所在路径为基准,name相对于该类的路径)

 Class.getResource(String path)
 对资源路径进行了处理
 {@link java.lang.Class# resolveName(java.lang.String)}

 path不以'/'开头时，默认是从此类所在的包下取资源；path以'/'开头时，则是从项目的ClassPath根下获取资源。在这里'/'表示ClassPath
 JDK设置这样的规则，是很好理解的，path不以'/'开头时，我们就能获取与当前类所在的路径相同的资源文件，
 而以'/'开头时可以获取ClassPath根下任意路径的资源。
 ====================================================
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-03-26 23:41
 * @Version: 1.0
 */
package com.latico.commons.common.util.system.classloader;