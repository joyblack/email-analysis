package com.sunrun.emailanalysis.tool.uuid;

import java.util.Random;

/**
 * 通过该类设置全局唯一ID
 */
public final class JoyUUID {
    private JoyUUID(){}

    private static class MyBuilder {
        private static final SnowflakeIdWorker snowflakeIdWorker;
        static{
            Random random = new Random();
            snowflakeIdWorker = new SnowflakeIdWorker((long)random.nextInt(31),(long)random.nextInt(31));
        }
    }

    public static final SnowflakeIdWorker getInstance(){
        return MyBuilder.snowflakeIdWorker;
    }

    public static synchronized Long getUUId(){
        return getInstance().nextId();
    }



}
