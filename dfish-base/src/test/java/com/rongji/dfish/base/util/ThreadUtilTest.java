package com.rongji.dfish.base.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadUtilTest {

    @Test
    public void testDetCharChinese(){
        log("main thread start");
        ThreadUtil.execute(()->{
            log("job 1 start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("job 1 done");
        });
        log("main thread after job1 exec");
        ThreadUtil.execute(()->{
            log("job 2 start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("job 2 done");
        },2000);
        log("main thread after job2 exec");

        ThreadUtil.invoke(()->{
            log("job 3 start");
            try {
                Thread.sleep(5000);
                log("job 3 after sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("job 3 done");
        },2000);
        log("main thread after job3 exec");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log("main thread end");
    }

    static final SimpleDateFormat SDF=new SimpleDateFormat("HH:mm:ss.SSS");
    private static void log(String str){
        System.out.println(SDF.format(new Date())+" : "+str);
    }
}
