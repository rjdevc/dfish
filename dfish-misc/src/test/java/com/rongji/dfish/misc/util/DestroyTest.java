package com.rongji.dfish.misc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DestroyTest {
    public static void  main(String[] args) throws InterruptedException {
        log("main thread start");
        MyClass my=new MyClass("1");
        Thread.sleep(5000);
        log("main thread 5 sec");
        System.gc();
        my=new MyClass("2");
        Thread.sleep(5000);
        System.gc();
        log("main thread 10 sec");
        Thread.sleep(5000);
        System.gc();
        log("main thread 15 sec");
        Thread.sleep(5000);
        log("main thread end");
    }

    private static class MyClass{
        ExecutorService executor= Executors.newSingleThreadExecutor();
        private String name;
        public MyClass(String name){
            this.name=name;
            log("class "+name+" create");
            executor.submit(()->{
                try {
                    Thread.sleep(4000);
                    log("class "+name+" first job done at 4 sec");
                    Thread.sleep(8000);
                    log("class "+name+" second job done at 4 sec");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        protected void finalize(){
            executor.shutdown();
            log("class "+name+" destroy");
        }
    }
    static final SimpleDateFormat SDF=new SimpleDateFormat("HH:mm:ss.SSS");
    private static void log(String str){
        System.out.println(SDF.format(new Date())+" : "+str);
    }
}
