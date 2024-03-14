package com.lhw.api;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        String androidPlatformPath = "";
        String resStoreDir = "";
        int apkParsePoolSize = 10;
        int executorPoolSize = 10;
        ApkParsePool apkParsePool = new ApkParsePool(apkParsePoolSize,androidPlatformPath);
        ExecutorService executorService = Executors.newFixedThreadPool(executorPoolSize);
        try{
            while(!Thread.currentThread().isInterrupted()){
                String apkPath = RedisUtil.popApkPath(); // 假设这个方法处理了阻塞和中断
                Path apk = Paths.get(apkPath);
                if(apkPath==null){
                    break;
                }
                Path resStore = Paths.get(resStoreDir);
                String resStorePath = String.valueOf(resStore.resolve(apk.getFileName()));

                Future<String> future = null;
                ApkParse apkParse = apkParsePool.borrow();
                try {
                    ApkParseCallable apkParseCallable = new ApkParseCallable(apkParse, apkPath, resStorePath);
                    future = executorService.submit(apkParseCallable);
                    String result = future.get(600, TimeUnit.SECONDS);
                    // 处理结果...
                } catch (Exception e) {
                    if (future != null) {
                        future.cancel(true);
                    }
                } finally {
                    apkParsePool.giveBack(apkParse);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdown();
        }
    }
}
