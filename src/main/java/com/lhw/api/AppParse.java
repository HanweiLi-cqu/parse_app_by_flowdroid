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
public class AppParse {
    private static Map<String, Boolean> visited = new HashMap<>();
    private static StringBuilder result = new StringBuilder();
    public static void get_api(String app_path,String android_platform_path,int mode){
        SetupApplication app = new SetupApplication(android_platform_path, app_path);
        soot.G.reset();
        app.constructCallgraph();
        SootMethod entryPoint = app.getDummyMainMethod();
        CallGraph cg = Scene.v().getCallGraph();
        
        switch (mode) {
            case 0:
                visit_graph(cg, entryPoint);
                break;
        
            default:
                break;
        }
        
    }

    public static void visit_graph(CallGraph cg,SootMethod entry){
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

    public static String get_result(){
        return result.toString();
    }
    public static void main(String[] args) {
        // 得到第一个参数
        String app_path = args[0];
        String android_platform_path = args[1];
        String output_dir = args[2];
        AppParse.get_api(app_path,android_platform_path, 0);
        String res = AppParse.get_result();
        String output_filename = new File(app_path).getName().replace(".apk", ".txt");
        String output_path = output_dir + "/" + output_filename;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(output_path));){
            bufferedWriter.write(res);
            bufferedWriter.close();
        } catch (Exception e) {}
        
    }
}
