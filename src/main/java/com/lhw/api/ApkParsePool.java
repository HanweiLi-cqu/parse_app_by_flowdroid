package com.lhw.api;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ApkParsePool {
    private final BlockingQueue<ApkParse> pool;
    private final int size;

    public ApkParsePool(int size,String androidPlatformPath) {
        this.size = size;
        this.pool = new LinkedBlockingQueue<>(size);

        // 初始化池
        for (int i = 0; i < size; i++) {
            pool.offer(new ApkParse(androidPlatformPath));
        }
    }

    public ApkParse borrow() throws InterruptedException {
        // 从池中借用一个 ApkParse 实例
        return pool.take();
    }

    public void giveBack(ApkParse apkParse) {
        // 将 ApkParse 实例归还到池中
        pool.offer(apkParse);
    }
}
