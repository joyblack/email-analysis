package com.sunrun.emailanalysis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 总线程数 = 排队线程数 + 活动线程数 + 执行完成的线程数。
public class ThreadMonitorTest {

    public static ExecutorService es = new ThreadPoolExecutor(50,100,0L, TimeUnit.HOURS,
            new LinkedBlockingDeque<Runnable>(100000));

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            es.execute(() -> {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        ThreadPoolExecutor tpe = (ThreadPoolExecutor) es;
        while(true){
            System.out.println("*****************");

            System.out.println("当前排队线程数: " + tpe.getQueue().size());

            System.out.println("执行完成线程数: " + tpe.getCompletedTaskCount());

            System.out.println("活动线程数:" + tpe.getActiveCount());

            System.out.println("总线程数: " + tpe.getTaskCount());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
