package com.latico.commons.objenesis;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 * <PRE>
 Java已经支持通过Class.newInstance()动态实例化Java类，但是这需要Java类有个适当的构造器。很多时候一个Java类无法通过这种途径创建，例如：

 构造器需要参数
 构造器有副作用
 构造器会抛出异常
 Objenesis可以绕过上述限制。它一般用于：

 序列化、远程处理和持久化：无需调用代码即可将Java类实例化并存储特定状态。
 代理、AOP库和Mock对象：可以创建特定Java类的子类而无需考虑super()构造器。
 容器框架：可以用非标准方式动态实例化Java类。例如Spring引入Objenesis后，Bean不再必须提供无参构造器了。

 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-22 15:38
 * @Version: 1.0
 */
public class ObjenesisUtils {
    /**
     * 对象创建助手
     */
    private static final Objenesis objenesis = createObjenesisStd(true);

    /**
     * 创建一个标准的对象助手,默认使用缓存
     * @return
     */
    public static Objenesis createObjenesisStd() {
        Objenesis objenesis = new ObjenesisStd(true);
        return objenesis;
    }

    /**
     * 创建一个标准的对象助手
     * @param useCache
     * @return
     */
    public static Objenesis createObjenesisStd(boolean useCache) {
        Objenesis objenesis = new ObjenesisStd(useCache);
        return objenesis;
    }

    public static <T> ObjectInstantiator getObjectInstantiator(Objenesis objenesis, Class<T> cls) {
        return objenesis.getInstantiatorOf(cls);
    }

    /**
     * 通过对象助手，创建一个对象
     * @param objenesis
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T createObject(Objenesis objenesis, Class<T> cls) {
        return objenesis.newInstance(cls);
    }

    /**
     * 通过默认的对象助手，创建一个对象
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T createObject(Class<T> cls) {
        return objenesis.newInstance(cls);
    }
}
