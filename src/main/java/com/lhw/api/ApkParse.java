package com.lhw.api;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;

import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;

public class ApkParse {
    private Map<String, Boolean> visited;
    private StringBuilder result = new StringBuilder();
    private String appPath;

    public String getAppPath() {
        return appPath;
    }

    private String androidPlatformPath;
    private int mode;
    ApkParse(String appPath,String androidPlatformPath,int mode){
        this.appPath = appPath;
        this.androidPlatformPath = androidPlatformPath;
        this.mode = mode;
        this.visited = new HashMap<>();
        this.result = new StringBuilder();
    }

    public ApkParse(String appPath, String androidPlatformPath) {
        this.appPath = appPath;
        this.androidPlatformPath = androidPlatformPath;
        this.mode = 0;
        this.visited = new HashMap<>();
        this.result = new StringBuilder();
    }

    public ApkParse(String androidPlatformPath) {
        this.visited = new HashMap<>();
        this.result = new StringBuilder();
        this.androidPlatformPath = androidPlatformPath;
        this.mode = 0;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public void getApi(){
        soot.G.reset();
        final InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.getAnalysisFileConfig().setTargetAPKFile(appPath);
        config.getAnalysisFileConfig().setAndroidPlatformDir(androidPlatformPath);
        config.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
        config.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        SetupApplication app = new SetupApplication(config);
        app.constructCallgraph();
        SootMethod entryPoint = app.getDummyMainMethod();
        CallGraph cg = Scene.v().getCallGraph();

        if(result.length()!=0){
            result = new StringBuilder();
        }
        if(!visited.isEmpty()){
            visited.clear();
        }
        
        switch (mode) {
            case 0:
                visitGraph(cg, entryPoint);
                break;
        
            default:
                break;
        }
        
    }

    public void visitGraph(CallGraph cg,SootMethod entry){
        Queue<SootMethod> queue = new LinkedList<>();
        String entry_sig = entry.getSignature();
        visited.put(entry_sig, true);
        queue.add(entry);
        while(!queue.isEmpty()){
            SootMethod cur = queue.poll();
            Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(cur));
            while(targets.hasNext()){
                SootMethod target = (SootMethod)targets.next();
                String target_sig = target.getSignature();
                result.append((cur.getSignature()+"--->"+target_sig+"\n"));
                if(!visited.containsKey(target_sig)){
                    visited.put(target_sig, true);
                    queue.add(target);
                }
            }
        }

    }

    public void storeResult(String path){

        String res = result.toString();
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))){
            bufferedWriter.write(res);
        }catch (Exception e){

        }
    }

    public static void main(String[] args) {
        // 得到第一个参数
        String appPath = "F:\\androidAPISeqExtract\\demo\\test.apk";
        ApkParse apkParse = new ApkParse(appPath,"F:\\AndroidSdk\\platforms");
        String output_filename = new File(appPath).getName().replace(".apk", ".txt");
        String output_path = "F:\\Java\\parse_app_by_flowdroid\\output" + "/" + output_filename;
        apkParse.getApi();
        apkParse.storeResult(output_path);

    }
}
