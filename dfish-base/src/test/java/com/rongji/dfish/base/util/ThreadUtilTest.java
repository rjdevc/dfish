package com.rongji.dfish.base.util;

import org.junit.Test;
import org.omg.CORBA.portable.Delegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class ThreadUtilTest {

    @Test
    public void testDetCharChinese() throws Exception {
        log("main thread start");
        ThreadUtil.execute(()->{
            log("job 1 start");
            run(5000,"job 1 done");
        });
        log("main thread after job1 exec");
//        ThreadUtil.execute(()->{
//            log("job 2 start");
//            run(5000,"job 2 done");
//        },2000);
        log("main thread after job2 exec");

        ThreadUtil.invoke(2000,true,new Job("JOB 3"));
        ThreadUtil.invoke(2000,true,new Job("JOB 4"));
        ThreadUtil.invoke(2000,true,new Job("JOB 5"));
        log("main thread after job3 exec");

        run(4000,"main thread end");
    }


    static ExecutorService es=ThreadUtil.newSingleThreadExecutor();



    private static class Job implements Runnable{
         String name;
         public Job(String name){
             this.name=name;
         }
         public void run(){
             log(name+" start");
             ThreadUtilTest.run(1000,name+" 1 sec");
             if(Thread.currentThread().isInterrupted()){return;}
             ThreadUtilTest.run(1000,name+" 2 sec");
             if(Thread.currentThread().isInterrupted()){return;}
             ThreadUtilTest.run(1000,name+" 3 sec");
             if(Thread.currentThread().isInterrupted()){return;}
             ThreadUtilTest.run(1000,name+" 4 sec");
             if(Thread.currentThread().isInterrupted()){return;}
             ThreadUtilTest.run(1000,name+" 5 sec");
             if(Thread.currentThread().isInterrupted()){return;}
             ThreadUtilTest.run(500,name+" done");
         }
    }
    static void run(long time,String str){
        try {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        log(str);
    }
    static final SimpleDateFormat SDF=new SimpleDateFormat("HH:mm:ss.SSS");
    private static void log(String str){
        System.out.println(SDF.format(new Date())+" : "+str);
    }
}
