package com.lhw.api;

import java.util.concurrent.Callable;

public class ApkParseCallable implements Callable<String> {
    private String apkData;
    private ApkParse apkParse;
    private String resStorePath;

    ApkParseCallable(ApkParse apkParse,String apkData,String resStorePath){
        this.apkData = apkData;
        this.apkParse = apkParse;
        this.resStorePath = resStorePath;
    }
    @Override
    public String call() throws Exception {
        apkParse.setAppPath(apkData);
        apkParse.getApi();
        apkParse.storeResult(resStorePath);
        return null;
    }
}
