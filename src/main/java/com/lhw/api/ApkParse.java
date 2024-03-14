package com.lhw.api;
import soot.jimple.infoflow.android.SetupApplication;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
public class ApkParse {
    private Map<String, Boolean> visited;
    private StringBuilder result = new StringBuilder();
    private String appPath;
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
        SetupApplication app = new SetupApplication(androidPlatformPath, appPath);
        soot.G.reset();
        app.constructCallgraph();
        SootMethod entryPoint = app.getDummyMainMethod();
        CallGraph cg = Scene.v().getCallGraph();
        
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

//    public static void main(String[] args) {
//        // 得到第一个参数
//        String app_path = args[0];
//        String android_platform_path = args[1];
//        String output_dir = args[2];
//        ApkParse.get_api(app_path,android_platform_path, 0);
//        String res = ApkParse.get_result();
//        String output_filename = new File(app_path).getName().replace(".apk", ".txt");
//        String output_path = output_dir + "/" + output_filename;
//        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(output_path));){
//            bufferedWriter.write(res);
//            bufferedWriter.close();
//        } catch (Exception e) {}
//
//    }
}
