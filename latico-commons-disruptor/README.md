在复杂场景下，希望一个生产者生产的数据放入RingBuffer后，能够被多个消费者消费，至于消费的方式，可能各有不同，比如菱形结构、六边形结构、顺序执行。接下来我们一一介绍

# 菱形结构：
说明：P1生产的数据给C1和C2并行执行完成后，再交给C3执行。



代码：

Handler1：设置了事先名称h1

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
 
import jeff.generate1.Trade;
 
public class Handler1 implements EventHandler<Trade>,WorkHandler<Trade> {  
	  
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {  
        this.onEvent(event);  
    }  
  
    @Override  
    public void onEvent(Trade event) throws Exception {  
    	System.out.println("handler1: set name"+"----price:"+event.getPrice());
    	event.setName("h1");
    	Thread.sleep(1000);
    }  
}  


Handler2:设置了事件价格17.0

import com.lmax.disruptor.EventHandler;
 
import jeff.generate1.Trade;
 
public class Handler2 implements EventHandler<Trade> {  
	  
    @Override  
    public void onEvent(Trade event, long sequence,  boolean endOfBatch) throws Exception {  
    	System.out.println("handler2: set price"+"----price:"+event.getPrice());
    	event.setPrice(17.0);
    	Thread.sleep(1000);
    }  
      
}  
Handler3：打印了事件的名称、价格及实例

import com.lmax.disruptor.EventHandler;
 
import jeff.generate1.Trade;
 
public class Handler3 implements EventHandler<Trade> {
    @Override  
    public void onEvent(Trade event, long sequence,  boolean endOfBatch) throws Exception {  
    	System.out.println("handler3: name: " + event.getName() + " , price: " + event.getPrice() + ";  instance: " + event.toString());
    }  
}



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
 
import jeff.generate1.Trade;
 
public class Main {  
    public static void main(String[] args) throws InterruptedException {  
       
    	long beginTime=System.currentTimeMillis();  
        int bufferSize=1024;  
        ExecutorService executor=Executors.newFixedThreadPool(8);  
 
        Disruptor<Trade> disruptor = new Disruptor<Trade>(new EventFactory<Trade>() {  
            @Override  
            public Trade newInstance() {  
                return new Trade();  
            }  //指定一个生产者
        }, bufferSize, executor, ProducerType.SINGLE, new BusySpinWaitStrategy());  
        
        //菱形操作
        //handleEventsWith：使用disruptor创建消费者组C1,C2 并行执行 
        EventHandlerGroup<Trade> handlerGroup = 
        		disruptor.handleEventsWith(new Handler1(), new Handler2());
        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3 
        handlerGroup.then(new Handler3());
               
        disruptor.start();//启动  
        CountDownLatch latch=new CountDownLatch(1);  
        //生产者准备  
        executor.submit(new TradePublisher(latch, disruptor));
        
        latch.await();//等待生产者完事. 
       
        disruptor.shutdown();  
        executor.shutdown();  
        System.out.println("总耗时:"+(System.currentTimeMillis()-beginTime));  
    }  
}  
解释下上边生产者代码片段：

       //生产者准备  
        executor.submit(new TradePublisher(latch, disruptor));
在线程池中提交了生产者处理线程实现了Runnable接口：

import java.util.Random;
import java.util.concurrent.CountDownLatch;
 
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
 
import jeff.generate1.Trade;
 
public class TradePublisher implements Runnable {  
	
    Disruptor<Trade> disruptor;  
    private CountDownLatch latch;  
    
    private static int LOOP=1;
  
    public TradePublisher(CountDownLatch latch,Disruptor<Trade> disruptor) {  
        this.disruptor=disruptor;  
        this.latch=latch;  
    }  
  
    @Override  
    public void run() {  
    	TradeEventTranslator tradeTransloator = new TradeEventTranslator(); 
    	//disruptor发布1个数据并填充
        for(int i=0;i<LOOP;i++){  
            disruptor.publishEvent(tradeTransloator);  
        }  
        latch.countDown();  
    }  
      
}  
  
class TradeEventTranslator implements EventTranslator<Trade>{  
    
	private Random random=new Random();  
    
	@Override  
    public void translateTo(Trade event, long sequence) {  
        this.generateTrade(event);  
    }  
    
	private Trade generateTrade(Trade trade){  
        trade.setPrice(random.nextDouble()*9999);  
        return trade;  
    }  
	
}  
在TradePublisher的run方法中：

  disruptor.publishEvent(tradeTransloator);  
我们向discruptor中发布了一个事件初始化器TradeEventTranslator实现了EventTranslator接口，类型为Trade。在这个事件初始化器中给trade对象设置了随机价格，translateTo方法的执行会触发，Handler1和Handler2的执行，以及之后Handler3的执行。



再看下消费者菱形执行消费代码：

   EventHandlerGroup<Trade> handlerGroup = 
        		disruptor.handleEventsWith(new Handler1(), new Handler2());
        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3 
        handlerGroup.then(new Handler3());
handleEventsWith这个方法中提交了Handler1和Handler2两个实例，组成了消费者组handlerGroup，然后handlerGroup使用then方法提交了Handler3，表示在Handler1和Handler2两个实例并行执行完成后，再执行Handler3操作。

打印：

handler1: set name----price:6333.697550972724
handler2: set price----price:6333.697550972724
handler3: name: h1 , price: 17.0;  instance: jeff.generate1.Trade@71f111ba
总耗时:18376
注意理解：例子中只生产了一个数据，Handler1和Handler2包括Handler3在内，都执行的是同一个Trade对象，可以从上边的打印看出来，Handler1和Handler2的price数据就是translateTo方法中设置的那个随机数。



# 六边形结构
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();
        disruptor.handleEventsWith(h1, h2);
        disruptor.after(h1).handleEventsWith(h4);
        disruptor.after(h2).handleEventsWith(h5);
        disruptor.after(h4, h5).handleEventsWith(h3);
图解：



说明：P1生产的数据，被（h1，h4）和（h2，h5）并行执行完成后，h3再执行。对于（h1，h4）和（h2，h5），h1执行后h4再执行；h2执行后h5再执行。

Handler4：

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
 
import jeff.generate1.Trade;
 
public class Handler4 implements EventHandler<Trade>,WorkHandler<Trade> {  
	  
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {  
        this.onEvent(event);  
    }  
  
    @Override  
    public void onEvent(Trade event) throws Exception {  
    	System.out.println("handler4: get name : " + event.getName());
    	event.setName(event.getName() + "h4");
    }  
}  

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
 
import jeff.generate1.Trade;
 
public class Handler5 implements EventHandler<Trade>,WorkHandler<Trade> {  
	  
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {  
        this.onEvent(event);  
    }  
  
    @Override  
    public void onEvent(Trade event) throws Exception {  
    	System.out.println("handler5: get price : " + event.getPrice());
    	event.setPrice(event.getPrice() + 3.0);
    }  
}  
打印：

handler2: set price----price:2172.612895685139
handler1: set name----price:17.0
handler5: get price : 17.0
handler4: get name : h1
handler3: name: h1h4 , price: 20.0;  instance: jeff.generate1.Trade@363bed68
总耗时:1119
分析：handler2设置了price为17.0，那么h5得到一定是17.0；h1设置了name为h1后，h4一定得到的是h1。

最后handler3得到的name一定是h1h4，得到的price一定是20.0。它们处理的都是同一个实例trade。




# 顺序执行：
      disruptor.handleEventsWith(new Handler1()).
        	handleEventsWith(new Handler2()).
        	handleEventsWith(new Handler3());
图解：



打印：

handler1: set name----price:1405.3587126200412
handler2: set price----price:1405.3587126200412
handler3: name: h1 , price: 17.0;  instance: jeff.generate1.Trade@74ee7e66
总耗时:2056
--------------------- 
作者：Jeff.Sheng 
来源：CSDN 
原文：https://blog.csdn.net/shengqianfeng/article/details/80710471 
版权声明：本文为博主原创文章，转载请附上博文链接！