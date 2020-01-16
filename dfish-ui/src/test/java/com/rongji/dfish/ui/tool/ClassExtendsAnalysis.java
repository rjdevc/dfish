package com.rongji.dfish.ui.tool;

import com.rongji.dfish.base.util.CharUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ClassExtendsAnalysis extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Class> clzs=findClasses("./dfish-ui/target/classes");
        TabPane rootLayout=new TabPane();

        TreeView<String> tree=new TreeView<>();
        rootLayout.getTabs().add(new Tab("类结构分析",tree));

        TreeItem<String> rootItem=new TreeItem<>();
        rootItem.setValue("所有Class");
        rootItem.setExpanded(true);
        tree.setRoot(rootItem);

        fillClassToTree(clzs,rootItem);

        TreeView<String> props=new TreeView<>();
        rootLayout.getTabs().add(new Tab("属性名分析",props));
        TreeItem<String> propsItem=new TreeItem<>();
        propsItem.setValue("所有方法");
        propsItem.setExpanded(true);
        props.setRoot(propsItem);


        fillPropNamesToPane(clzs,propsItem);

        primaryStage.setScene(new Scene(rootLayout, 800, 600));
        primaryStage.show();
    }

    private void fillPropNamesToPane(List<Class> clzs, TreeItem<String> shell) {
        TreeMap<String,Set<String>> caller=new TreeMap<>();
        TreeMap<String,String> groupRef=new TreeMap<>();
        TreeMap<String,List<String>> group=new TreeMap<>();
        for(Class c:clzs){
            for(Method m:c.getDeclaredMethods()){
                String methodName=m.getName();
                String refName=getRefName(methodName,group,groupRef);
                Set<String> call=caller.get(refName);
                if(call==null){
                    call=new TreeSet<>();
                    caller.put(refName,call);
                }
                call.add(c.getName());
            }
        }
        TreeItem<String> maybeWrong=new TreeItem<>("可能有问题的");
        maybeWrong.setExpanded(true);
        TreeItem<String> ok=new TreeItem<>("已经确认的");
        shell.getChildren().add(maybeWrong);
        shell.getChildren().add(ok);
        for(Map.Entry<String,Set<String>> entry:caller.entrySet()){
            String text=entry.getKey();
            TreeItem target=isNameOk(text)?ok:maybeWrong;
            if(group.get(entry.getKey())!=null){
                text+=" "+group.get(entry.getKey());
            }
            TreeItem ti=new TreeItem(text);
            target.getChildren().add(ti);
            for(String cls:entry.getValue()){
                TreeItem tiCls=new TreeItem(cls);
                ti.getChildren().add(tiCls);
            }
        }
    }

    private boolean isNameOk(String text) {
        //如果有驼峰则分为多个词
        List <String > words=split(text);
        for(String w:words) {
            if (!getOkKeys().contains(w.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
    private static HashSet<String> okKeys;
    private static String[] OK_KEYS={
            "width","height","min","max",
            "id","cls","style","align","v",
            "name","label","value","text","content",
            "column","columns","col","row","cols","rows","span","leaf",
            "command","commands",
            "now","property","json",
            "sub","mode","node","nodes","option","options","data",
            "build","clear",
            "by","as","contains","from",
            "before","begin","end","compare","escape",
            "append","add","get","find","set","after","copy","check","send","do"
    };
    private static Set<String> getOkKeys() {
        if(okKeys==null){
            okKeys=new HashSet<>();
            for(String key:OK_KEYS){
                okKeys.add(key);
            }
        }
        return okKeys;
    }

    private List<String> split(String text) {
        int lastPos=0;
        int cur=0;
        List<String> ret=new ArrayList<>();
        for(char c:text.toCharArray()){
            if(c>='A'&&c<='Z'){
                if(cur>lastPos){
                    ret.add(text.substring(lastPos,cur));
                    lastPos=cur;
                }
            }
            cur++;
        }
        if(cur>lastPos){
            ret.add(text.substring(lastPos,cur));
        }
        return ret;
    }


    private static String getRefName(String methodName,TreeMap<String,List<String>> group,TreeMap<String,String> groupRef) {
        if(groupRef.get(methodName)!=null){
            return groupRef.get(methodName);
        }
        for(String prefix:PREFIXS){
            if(methodName.startsWith(prefix)&&methodName.length()>prefix.length()){
                char c=methodName.charAt(prefix.length());
                if(c<'A'||c>'Z'){
                    continue;
                }
                String s=methodName.substring(prefix.length());
                s=(char)(s.charAt(0)+32)+s.substring(1);
                groupRef.put(methodName,s);
                List<String> sameName=group.get(s);
                if(sameName==null){
                    sameName=new ArrayList<>();
                    group.put(s,sameName);
                }
                sameName.add(methodName);
                return s;
            }
        }
        return methodName;
    }
    private static final String[] PREFIXS={"is","get","set","add","remove"};

    private void fillClassToTree(List<Class> clzs, TreeItem<String> shell) {
        //去除 interface
        for (Iterator<Class> iter=clzs.iterator();iter.hasNext();){
            Class clz=iter.next();
            if(clz.isInterface()){
                iter.remove();
            }
        }
        Map<Class,Integer> parentCount=new HashMap<>();
        Map<Class,List<Class>> subs=new HashMap<>();
        for(int i=0;i<clzs.size();i++){
            Class clz1=clzs.get(i);
            for(int k=i+1;k<clzs.size();k++) {
                Class clz2 = clzs.get(k);
                if(clz1.isAssignableFrom(clz2)){
                    setSubClass(clz1,clz2,parentCount,subs);
                }else if(clz2.isAssignableFrom(clz1)){
                    setSubClass(clz2,clz1,parentCount,subs);
                }
            }
        }
        for(Class c:clzs){
            if(parentCount.get(c)==null){
                TreeItem ti=new TreeItem(c.getName());
                shell.getChildren().add(ti);
                List sub=subs.get(c);
                if(sub!=null) {
                    fillClassToTree(sub, ti);
                }
            }
        }
    }

    private void setSubClass(Class clz1, Class clz2,Map<Class,Integer> parentCount, Map<Class,List<Class>> subs) {
        List<Class> subList=subs.get(clz1);
        if(subList==null){
            subList=new ArrayList<>();
            subs.put(clz1,subList);
        }
        subList.add(clz2);
        Integer i=parentCount.get(clz2);
        if(i==null ){i=0;}
        parentCount.put(clz2,i+1);
    }

    private List<Class> findClasses(String rootPath) {
        File folder=new File(rootPath);
        List<Class>ret=new ArrayList<>();
        findClasses(folder,null,ret);
        return ret;
    }

    private void findClasses(File folder, String packName,List<Class> ret) {
        for(File file:folder.listFiles()){
            if(file.isDirectory()){
                String subPackName=packName==null?file.getName():(packName+"."+file.getName());
                findClasses(file,subPackName,ret);
            }else if(file.isFile()){
                String fileName=file.getName();
                if(fileName.endsWith(".class")) {
                    String clsName = packName + "." + fileName.substring(0,fileName.length()-6);
                    try {
                        ret.add(Class.forName(clsName));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
