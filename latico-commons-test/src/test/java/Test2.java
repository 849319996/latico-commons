import org.junit.Test;
import sun.misc.VM;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2020-01-04 21:48
 * @Version: 1.0
 */
public class Test2 {
    /**
     *
     */
    @Test
    public void test(){
        Integer i1 = 199;
        Integer i2 = new Integer(199);
        Integer i3 = Integer.valueOf(199);
        System.out.println(i1 == i2);
        System.out.println(i1 == i3);

        System.out.println(sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high"));

    }

    /**
     *
     */
    @Test
    public void test1(){
        double l1 = -0.0;
        double l2 = 0.0;
        System.out.println(l1 == l2);
        System.out.println(System.currentTimeMillis());
        System.out.println(Long.MAX_VALUE);
    }
}
