package com.lhw.api;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        if(args.length!=4){
            System.out.println("please provide below config:\n" +
                    "- androidPlatformPath: the dir of android sdk platforms\n" +
                    "- resStoreDir: the dir of output\n" +
                    "- redisHost: ip of redis\n" +
                    "- redisPort: port of redis\n" +
                    "Note: redis no passwd");
        }
        String androidPlatformPath = args[0];
        String resStoreDir = args[1];
        Config.REDIS_HOST = args[2];
        Config.REDIS_PORT = Integer.valueOf(args[3]);
        ApkParse apkParse = new ApkParse(androidPlatformPath);
        while(true){
            String apkPath = RedisUtil.popApkPath();
            if(apkPath==null){
                break;
            }
            Path apk = Paths.get(apkPath);
            Path resStore = Paths.get(resStoreDir);
            String resStorePath = String.valueOf(resStore.resolve(apk.getFileName()))+".txt";
            apkParse.setAppPath(apkPath);
            apkParse.getApi();
            apkParse.storeResult(resStorePath);
        }
    }
}
