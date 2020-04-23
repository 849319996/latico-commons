/**
 * <PRE>
 *
 入站事件一般由I/O线程触发，以下事件为入站事件：
 ChannelRegistered() // Channel注册到EventLoop
 ChannelActive()     // Channel激活
 ChannelRead(Object) // Channel读取到数据
 ChannelReadComplete()   // Channel读取数据完毕
 ExceptionCaught(Throwable)  // 捕获到异常
 UserEventTriggered(Object)  // 用户自定义事件
 ChannelWritabilityChanged() // Channnel可写性改变，由写高低水位控制
 ChannelInactive()   // Channel不再激活
 ChannelUnregistered()   // Channel从EventLoop中注销

 出站事件一般由用户触发，以下事件为出站事件：
 bind(SocketAddress, ChannelPromise) // 绑定到本地地址
 connect(SocketAddress, SocketAddress, ChannelPromise)   // 连接一个远端机器
 write(Object, ChannelPromise)   // 写数据，实际只加到Netty出站缓冲区
 flush() // flush数据，实际执行底层写
 read()  // 读数据，实际设置关心OP_READ事件，当数据到来时触发ChannelRead入站事件
 disconnect(ChannelPromise)  // 断开连接，NIO Server和Client不支持，实际调用close
 close(ChannelPromise)   // 关闭Channel
 deregister(ChannelPromise)  // 从EventLoop注销Channel


 Netty提供了三种解码器解决粘包拆包问题：LineBasedFrameDecoder、DelimiterBasedFrameDecoder、LengthFieldBasedFrameDecoder。

 序列化protobuf在netty中的使用

 // FixedLengthFrameDecoder:是固定长度解码器，它能按照指定的长度对消息进行自动解码，开发者不需要考虑TCP的粘包等问题。
 // 利用FixedLengthFrameDecoder解码，无论一次性接收到多少的数据，他都会按照构造函数中设置的长度进行解码；
 // 如果是半包消息，FixedLengthFrameDecoder会缓存半包消息并等待下一个包，到达后进行拼包，直到读取完整的包。
 // arg0.pipeline().addLast(new FixedLengthFrameDecoder(20));

 //
 // 消息用 _#_ 作为分隔符,加入到DelimiterBasedFrameDecoder中，第一个参数表示单个消息的最大长度，当达到该
 // 长度后仍然没有查到分隔符，就抛出TooLongFrameException异常，防止由于异常码流缺失分隔符导致的内存溢出
 ByteBuf delimiter = Unpooled.copiedBuffer("_#_".getBytes());
 arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));

 arg0.pipeline().addLast(new StringDecoder());
 arg0.pipeline().addLast(new TimeServerHandler());

 * </PRE>
 *
 * @author: latico
 * @date: 2019-03-26 16:46
 * @version: 1.0
 */
package com.latico.commons.netty;