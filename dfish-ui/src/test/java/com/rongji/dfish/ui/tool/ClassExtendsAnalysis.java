package com.rongji.dfish.ui.tool;

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

        FlowPane props=new FlowPane();
        ScrollPane scp=new ScrollPane();
        scp.setContent(props);
        rootLayout.getTabs().add(new Tab("属性名分析",scp));
        fillPropNamesToPane(clzs,props);

        primaryStage.setScene(new Scene(rootLayout, 800, 600));
        primaryStage.show();
    }

    private void fillPropNamesToPane(List<Class> clzs, FlowPane props) {
        TreeMap<String ,Object[]> propNames=new TreeMap<>();

        for(Class c:clzs){
            for(Method m:c.getMethods()){
                if(m.getName().startsWith("get")&& Modifier.isPublic(m.getReturnType().getModifiers())&&
                        (m.getParameterTypes()==null||m.getParameterTypes().length==0)&&
                        m.getAnnotation(Deprecated.class)==null){
                    if(m.getDeclaringClass().getName().indexOf(".json.")>0){
                        continue;
                    }
                    String propName=m.getName().substring(3);
                    propName=((char)(propName.charAt(0)+32))+propName.substring(1);
                    Object[] o=propNames.get(propName);
                    if(o==null){
                        propNames.put(propName,new Object[]{1,m.getDeclaringClass().getSimpleName()});
                    }else{
                        propNames.put(propName,new Object[]{(Integer)o[0]+1,m.getDeclaringClass().getSimpleName()});
                    }
                }
            }
        }
        List<Map.Entry<String,Object[]>> entries =new ArrayList<>(propNames.entrySet());
        Collections.sort(entries,(entry1,entry2)->{
            Object[] o1=entry1.getValue();
            Object[] o2=entry2.getValue();
            return ((Integer)o2[0])- ((Integer)o1[0]);
        });

        for(Map.Entry<String,Object[]>entry:entries) {
            Object[] o=entry.getValue();
//            System.out.println(entry.getKey()+"\t"+o[0]+"\t"+o[1]);
            Label lb=new Label(entry.getKey()+"("+entry.getValue()[0]+")");
            lb.setPadding(new Insets(5));
            props.getChildren().add(lb);
        }

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
