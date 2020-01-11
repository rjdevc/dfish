package com.rongji.dfish.ui.tool;

import com.rongji.dfish.ui.JsonObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class ClassExtendsAnalysis extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        File f=new File("a.txt");
        System.out.println(f.getAbsolutePath());
        List<Class> clzs=findClasses("./dfish-ui/target/classes");
        TreeView<String> rootLayout=new TreeView<>();
        TreeItem<String> rootItem=new TreeItem<>();
        rootItem.setValue("所有Class");
        rootItem.setExpanded(true);
        rootLayout.setRoot(rootItem);
        fillClassToTree(clzs,rootItem);
        primaryStage.setScene(new Scene(rootLayout, 800, 600));
        primaryStage.show();
    }

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
