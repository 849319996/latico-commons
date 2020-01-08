import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTest {
    public static void main(String[] args) {
        // 分配内存大小为10的缓存区
        IntBuffer buffer = IntBuffer.allocate(10);

        System.out.println("capacity:" + buffer.capacity());
        long start = System.currentTimeMillis();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 5; ++i) {
            int randomNumber = secureRandom.nextInt(20);
            buffer.put(randomNumber);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("before flip position:" + buffer.position());
        System.out.println("before flip limit:" + buffer.limit());

        buffer.flip();

        System.out.println("after flip position:" + buffer.position());
        System.out.println("after flip limit:" + buffer.limit());

        System.out.println("enter while loop");

        while (buffer.hasRemaining()) {
            System.out.println("=================");
            System.out.println("position:" + buffer.position());
            System.out.println("limit:" + buffer.limit());
            System.out.println("capacity:" + buffer.capacity());
            System.out.println("元素:" + buffer.get());
        }
        System.out.println("最后flip=================");
        buffer.flip();
        System.out.println("before flip position:" + buffer.position());
        System.out.println("before flip limit:" + buffer.limit());
    }
}