package com.lhw.api;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.List;

public class RedisUtil {
    private static JedisPool jedisPool = null;
    private static final String APK_PATHS_KEY = "apkPaths";

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50); // 最大连接数
        poolConfig.setMaxIdle(5); // 最大空闲连接数
        poolConfig.setMinIdle(1); // 最小空闲连接数
        poolConfig.setTestOnBorrow(true); // 在获取连接的时候检查有效性
        poolConfig.setTestOnReturn(true); // 在return给pool时，是否提前进行validate操作
        jedisPool = new JedisPool(poolConfig, Config.REDIS_HOST, Config.REDIS_PORT);
    }
    public static void pushApkPath(String path) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(APK_PATHS_KEY, path);
        }
    }
    public static void pushApkList(List<String> paths){
        try(Jedis jedis = jedisPool.getResource()){
            jedis.lpush(APK_PATHS_KEY, paths.toArray(new String[0]));
        }
    }

    public static String popApkPath() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> res = jedis.brpop(5, APK_PATHS_KEY);
            if(res==null){
                return null;
            }
            return res.get(1);
        }
    }

    public static void main(String[] args) {
        List<String> apks = Utils.findFilesWithSuffix("F:\\androidAPISeqExtract\\demo","apk");
        pushApkList(apks);
        System.out.println("ok");
    }
}
