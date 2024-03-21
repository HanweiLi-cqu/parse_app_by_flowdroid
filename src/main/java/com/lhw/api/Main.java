package com.lhw.api;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        if(args.length!=3){
            System.out.println("please provide below config:\n" +
                    "- androidPlatformPath: the dir of android sdk platforms\n" +
                    "- resStoreDir: the dir of output\n" +
                    "- apkPaths: the dir of android apps");
        }
//        String androidPlatformPath = "F:\\AndroidSdk\\platforms";
//        String resStoreDir = "F:\\Java\\parse_app_by_flowdroid\\output";
//        String apkPaths = "F:\\androidAPISeqExtract\\demo";
        String androidPlatformPath = args[0];
        String resStoreDir = args[1];
        String apkPaths = args[2];

        int apkParsePoolSize = 1;
        int executorPoolSize = 1;
        List<String> paths = Utils.findFilesWithSuffix(apkPaths,"apk");
        RedisUtil.pushApkList(paths);
        ApkParsePool apkParsePool = new ApkParsePool(apkParsePoolSize,androidPlatformPath);
        ExecutorService executorService = Executors.newFixedThreadPool(executorPoolSize);
        try{
            int batch = 200;
            List<CompletableFuture<String>> futures = new ArrayList<>();
            while(!Thread.currentThread().isInterrupted()){
                if(batch==0){
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                    futures.clear();
                    batch = 200;
                }
                String apkPath = RedisUtil.popApkPath(); // 假设这个方法处理了阻塞和中断
                if(apkPath==null){
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                    futures.clear();
                    break;
                }
                Path apk = Paths.get(apkPath);
                Path resStore = Paths.get(resStoreDir);
                String resStorePath = String.valueOf(resStore.resolve(apk.getFileName()))+".txt";
                ApkParse apkParse = apkParsePool.borrow();
                CompletableFuture<String> cf = CompletableFuture.supplyAsync(()->{
                    try{
                        return new ApkParseCallable(apkParse, apkPath, resStorePath).call();
                    } catch (Exception e){
                        throw new RuntimeException();
                    }
                },executorService)
                        .orTimeout(600,TimeUnit.SECONDS)
                        .whenComplete((result,ex)->{
                            apkParsePool.giveBack(apkParse);
                        });
                futures.add(cf);
                batch--;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdown();
        }
    }
}
