package com.rongji.dfish.misc.clzany;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CyclicDependenciesFinder {
    public static void main(String[] args) throws IOException {
        File folder =new File("D:\\IdeaProjects\\dfish\\dfish-ui\\target\\classes");
//        File folder =new File("D:\\IdeaProjects\\zhdj\\spip\\pac\\target\\classes");
        CyclicDependenciesFinder f=new CyclicDependenciesFinder();
        List<ClassDependency> dependenciesList=f.findCyclicDependencies(folder,false);
        Set<ClassDependency> dependencies=new HashSet<>(dependenciesList);
//        for(ClassDependency dep:dependencies){
//            System.out.println(dep.from+" --> "+dep.to);
//        }
        //根据初度和入度
        Map<String,ClassStat> stats=new HashMap<>();
        for(ClassDependency ci:dependencies){
            ClassStat cs=stats.computeIfAbsent(ci.from,c->new ClassStat());
            cs.clz=ci.from;
            cs.out++;
            ClassStat cs2=stats.computeIfAbsent(ci.to,c->new ClassStat());
            cs2.clz=ci.to;
            cs2.in++;
        }
        ArrayList<ClassStat> statList=new ArrayList<>(stats.values());
        statList.sort(Comparator.comparingInt(item->item.in-item.out));
        //根据调用关系优先从calss中取出调用关系，形成树//深度优先遍历。
        Set<String> find=new HashSet<>();
        //第一步先找出 头
        for(ClassStat stat:statList){
            String clz=stat.clz;
            //不能出现 clz 调用已有 finditem 才能增加
            boolean fail=false;
            for(String added:find){
                ClassDependency cd=new ClassDependency();
                cd.from=clz;
                cd.to=added;
                if(dependencies.contains(cd)){
                    fail=true;
                }
            }
            if(!fail){
                find.add(clz);
            }
        }
        System.out.println("第0轮最常被调用别人的类("+find.size()+")"+find);
        Set<String> curLevel=new HashSet<>(find);
        int  loop=0;
        while(true) {
            Set<String> nextLevel = new HashSet<>();
            for(Iterator<ClassDependency> iter=dependencies.iterator(); iter.hasNext();){
                ClassDependency cd=iter.next();
                if(curLevel.contains(cd.from) && !find.contains(cd.to)){
                    iter.remove();
//                    System.out.println("移除正常调用关系 "+cd.from+" --> "+cd.to) ;
                    nextLevel.add(cd.to);
                }
            }

            if(nextLevel.isEmpty()){
                break;
            }
            System.out.println("第"+(++loop)+"轮最常调用别人的类("+nextLevel.size()+")"+nextLevel);
            find.addAll(nextLevel);
            curLevel=nextLevel;
        }
        loop=0;
        int size=dependencies.size();
        while (true){
            loop++;
            System.out.println("第"+loop+"轮删除出度和入度为0的数据");
            Set<String> inClass=new HashSet<>();
            Set<String> outClass=new HashSet<>();
            dependencies.forEach(item->{outClass.add(item.from);inClass.add(item.to);});
            Set<String> both=new HashSet<>();
            for(String in:inClass){
                if(outClass.contains(in)){
                    both.add(in);
                }
            }
            inClass.removeAll(both);
            outClass.removeAll(both);
            dependencies.removeIf(item->outClass.contains(item.from)||inClass.contains(item.to));

            System.out.println("剩余关系"+dependencies.size()+"个");
            if(dependencies.size()==size){
                break;
            }
            size=dependencies.size();
        }

        System.out.println("以下调用关系可能不正常") ;
        for(ClassDependency cd:dependencies){
            System.out.println(cd .from+" --> "+cd.to);
        }

    }
    public static class ClassStat{
        String clz;
        int in;
        int out;
    }


    public List<ClassDependency> findCyclicDependencies(File folder ,boolean ignorInnerClass) throws IOException {


        List<ClassDependency> invokes=new ArrayList<>();
        checkCascade(folder,invokes,ignorInnerClass);
//        System.out.println("扫描得调用关系"+invokes.size()+"个");
        Set<String> selfClass=new HashSet<>();
        invokes.forEach(s->selfClass.add(s.from));
//        System.out.println("去除所有非内部调用，以及自身调用");
        invokes.removeIf(item->!selfClass.contains(item.to)||item.to.equals(item.from));
        //去除重复
//        invokes=new ArrayList<>(new HashSet<>(invokes));
//        System.out.println("剩余关系"+invokes.size()+"个");
//        int loop=0;
//        int size=invokes.size();
//        while (true){
//            loop++;
//            System.out.println("第"+loop+"轮删除出度和入度为0的数据");
//            Set<String> inClass=new HashSet<>();
//            Set<String> outClass=new HashSet<>();
//            invokes.forEach(item->{outClass.add(item.from);inClass.add(item.to);});
//            Set<String> both=new HashSet<>();
//            for(String in:inClass){
//                if(outClass.contains(in)){
//                    both.add(in);
//                }
//            }
//            inClass.removeAll(both);
//            outClass.removeAll(both);
//            invokes.removeIf(item->outClass.contains(item.from)||inClass.contains(item.to));
//
//            System.out.println("剩余关系"+invokes.size()+"个");
//            if(invokes.size()==size){
//                break;
//            }
//            size=invokes.size();
//        }
        return invokes;
        //找出入度比抄高的点。
    }

    private static void checkCascade(File folder, List<ClassDependency> depends,boolean ignorInnerClass)throws IOException {
        for(File file:folder.listFiles()){
            if(file.isDirectory()){
                checkCascade(file,depends,ignorInnerClass);
            }else if(file.isFile()&&file.getName().endsWith(".class")){
                try {
                    JavaClassConstReader jccr = new JavaClassConstReader(file);
                    String clz=((JavaClassConstReader.ClassConst)jccr.getThisClass()).getValue();
                    for(JavaClassConstReader.Const con:jccr.getConsts()){
                        if(con instanceof JavaClassConstReader.ClassConst){
                            JavaClassConstReader.ClassConst ccon=(JavaClassConstReader.ClassConst)con;
                            if(ignorInnerClass){
                                if(clz.contains("$")||ccon.getValue().contains("$")){
                                    continue;
                                }
                            }
                            ClassDependency depend=new ClassDependency();
                            depend.from=clz;
                            depend.to=ccon.getValue();
                            depends.add(depend);
                        }
                    }
                }catch (IllegalArgumentException ex){
                    System.err.println("read "+file.getName()+" fail,"+ex.getMessage());
                }
            }
        }
    }
    public static class ClassDependency{
        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        private String from;
        private String to;
        @Override
        public boolean equals(Object o){
            if(o==null)return false;
            if(o==this)return true;
            if(o instanceof ClassDependency){
                ClassDependency cast=(ClassDependency)o;
                return cast.to.equals(to)&&cast.from.equals(from);
            }
            return false;
        }
        @Override
        public int hashCode(){
            return to.hashCode()^from.hashCode();
        }
    }
}
