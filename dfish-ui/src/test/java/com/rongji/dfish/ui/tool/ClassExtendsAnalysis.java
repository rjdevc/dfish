package com.rongji.dfish.ui.tool;

import com.rongji.dfish.base.util.CharUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
//                Set<String> call=caller.get(refName);
//                if(call==null){
//                    call=new TreeSet<>();
//                    caller.put(refName,call);
//                }
//                call.add(c.getName());
                caller.computeIfAbsent(refName,k->new TreeSet<>()).add(c.getName());
                if(m.getName().equals("getType")&&c!= Node.class){
                    System.out.println(c.getName());
                }
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

    private static String OK_KEYS=
            "add, after, align, all, another, append, as, auth, auto, " +
            "autocomplete, badge, base, bean, before, begin, blank, box, btn, " +
            "bubble, build, bundle, button, buttons, by, cache, calc, " +
            "cancelable, cell, check, checked, class, clear, closable, cls, " +
            "col, cols, column, columns, combine, combo, command, commands, " +
            "compare, complete, complex, concurrent, contains, content, copy, count, " +
            "cover, current, data, date, decimal, delay, description, dft,  " +
            "disabled, do, download, drop, effect, ellipsis, end, equals, error, " +
            "escape, expanded, face, field, file, filed, fill, filter, find, " +
            "first, fix, fixed, focus, focusable, folder, forbid, form, format, " +
            "from, full, get, getter, gid, grid, group, has, height, hidden, " +
            "hide, highlight, hover, hr, html, icon, id, img, " +
            "indent, independent, index, info, init, instance, item, json, jump, " +
            "keep, key, label, last, leaf, legend, length, limit, line, loading, " +
            "match, max, method, methods, min, minimize, minus, mode, more, " +
            "movable, multiple, name, new, next, no, node, nodes, notify, " +
            "notnull, now, num, number, on, onclick, option, options, overflow, " +
            "owner, page, parse, partial, path, pattern, percent, picker, " +
            "placeholder, position, post, preload, prepend, prev, preview, " +
            "prong, prop, properties, property, prototype, pub, put, radio, " +
            "range, raw, readonly, remark, remove, replace, required, " +
            "resizable, result, retain, rightward, root, row, rows, scheme, " +
            "scope, scroll, search, section, send, separator, set, setting, " +
            "show, size, snap, sort, space, span, split, src, start, status, " +
            "step, strict, string, style, sub, submit, success, suggest, sum, sync, " +
            "target, targets, template, text, thumbnail, timeout, tip, title, " +
            "to, transparent, tree, triple, type, unchecked, upload, url," +
            "validate, value, visible, widget, width, yes," +
            "v, t, body, foot, head";//valgin tbody
    private static Set<String> getOkKeys() {
        if(okKeys==null){
            okKeys=new HashSet<>();
            for(String key:OK_KEYS.split(",")){
                okKeys.add(key.trim());
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
//                List<String> sameName=group.get(s);
//                if(sameName==null){
//                    sameName=new ArrayList<>();
//                    group.put(s,sameName);
//                }
//                sameName.add(methodName);
                group.computeIfAbsent(s,k->new ArrayList<>()).add(methodName);
                return s;
            }
        }
        return methodName;
    }
    private static final String[] PREFIXS={"is","get","set","add","remove"};

    private void fillClassToTree(List<Class> clzs, TreeItem<String> shell) {
        //去除 interface
//        for (Iterator<Class> iter=clzs.iterator();iter.hasNext();){
//            Class clz=iter.next();
//            if(clz.isInterface()){
//                iter.remove();
//            }
//        }
        clzs.removeIf(clz->clz.isInterface());

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
//        List<Class> subList=subs.get(clz1);
//        if(subList==null){
//            subList=new ArrayList<>();
//            subs.put(clz1,subList);
//        }
//        subList.add(clz2);
        subs.computeIfAbsent(clz1,k->new ArrayList<>()).add(clz2);
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
