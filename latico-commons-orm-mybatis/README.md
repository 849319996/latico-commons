功能列表：
1、mybatis；
2、mybatis-plus；
3、mybatis-generator-maven-plugin；
4、自定义插件；


 自定义插件拦截器
 1、使用Intercepts注解；
 2、实现Interceptor接口；
 3、在配置文件配置指定插件；

 1：intercept 拦截器，它将直接覆盖掉你真实拦截对象的方法。
 2：plugin方法它是一个生成动态代理对象的方法
 3：setProperties它是允许你在使用插件的时候设置参数值。