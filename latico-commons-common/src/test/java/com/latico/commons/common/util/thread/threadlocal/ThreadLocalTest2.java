package com.latico.commons.common.util.thread.threadlocal;

public class ThreadLocalTest2 {
    private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

    public static class MyRunnable implements Runnable {


        public MyRunnable() {
            threadLocal.set((int) (Math.random() * 100D));
            System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
        }
    }


    public static void main(String[] args) {
        Thread t1 = new Thread(new MyRunnable(), "A");
        Thread t2 = new Thread(new MyRunnable(), "B");
        t1.start();
        t2.start();
    }
}