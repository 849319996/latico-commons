import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2020-01-01 22:58
 * @version: 1.0
 */
public class TestDemo {

    /**
     *
     */
    @Test
    public void test(){
        List<Integer> src = new ArrayList<>();
        src.add(1);
        src.add(2);
        src.add(3);
        ConcurrentLinkedQueue<Object> result = new ConcurrentLinkedQueue<>();

        src.parallelStream().forEach(i -> {
            System.out.println(Thread.currentThread().getName() + " 线程执行:" + i);
            result.add(i + 2);
        });


        System.out.println(result);
    }


    /**
     *
     */
    @Test
    public void test2(){
        Integer i1 = 99;
        Integer i2 = new Integer(99);
        Integer i3 = Integer.valueOf(99);
        System.out.println(i1 == i2);
        System.out.println(i1 == i3);

    }
}
