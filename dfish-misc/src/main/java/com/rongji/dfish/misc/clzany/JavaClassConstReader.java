package com.rongji.dfish.misc.clzany;

import java.io.*;

public class JavaClassConstReader {
    byte[] source;
    int offset;
    int classVersion;
    Const[] consts;
    int accessFlag;
    int thisClz;
    int superClz;

    public Const[] getConsts() {
        return consts;
    }
    public int getClassVersion(){
        return classVersion;
    }
    public int getAccessFlag(){
        return accessFlag;
    }
    public Const getThisClass(){
        return consts[thisClz];
    }
    public Const getSuperClass(){
        return consts[superClz];
    }

    public JavaClassConstReader(byte[] source){
        this.source=source;
        readConsts();
        accessFlag=readShort();
        thisClz=readShort();
        superClz=readShort();
    }
    public JavaClassConstReader(File file) throws IOException {
        this(readClassAsByteArray(file));
    }
    private static byte[] readClassAsByteArray(File file) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        FileInputStream fis=new FileInputStream(file);
        byte[] buff=new byte[8192];
        int read=0;
        while((read=fis.read(buff))>=0){
            baos.write(buff,0,read);
        }
        return baos.toByteArray();
    }

    private void readConsts() {
        offset=6;//4 for magic CAFE, 2 for minVersion
        classVersion=readShort();
        int constLenth=readShort();
        consts= new Const[constLenth];
        for(int i=1;i<constLenth;i++){
            Const con=readConst();
            consts[i]=con;
            //Long 和 double;
            if(con.getTag()==Const.TAG_DOUBLE||con.getTag()==Const.TAG_LONG){
                i++;
            }
        }
    }
    private Const readConst() {
        int tag=readByte();
        Const con;
        switch (tag){
            case Const.TAG_UTF8:
                con=readUtf8();
                break;
            case Const.TAG_INTEGER:
            case Const.TAG_FLOAT:
                con=readConst(tag,4);
                break;
            case Const.TAG_LONG:
            case Const.TAG_DOUBLE:
                con=readConst(tag,8);
                break;
            case Const.TAG_STRING:
            case Const.TAG_CLASS:
            case Const.TAG_METHOD_TYPE:
                con=readReferConst(tag);
                break;
            case Const.TAG_FIELDREF:
            case Const.TAG_METHODREF:
            case Const.TAG_INTERFACE_METHODREF:
            case Const.TAG_NAME_AND_TYPE:
            case Const.TAG_INVOKE_DYNAMIC:
                con=readBioConst(tag);
                break;
            case Const.TAG_METHOD_HANDLE:
                con=readMethodHandleConst(tag);
                break;

            default:
                throw new IllegalArgumentException("tag="+tag+",pos="+offset);
        }
        return con;
    }

    private Const readMethodHandleConst(int tag) {
        BioConst con=new BioConst(tag,consts);
        con.refer1=readByte();
        con.refer2=readShort();
        return con;
    }

    private Const readBioConst(int tag) {
        BioConst con=new BioConst(tag,consts);
        con.refer1=readShort();
        con.refer2=readShort();
        return con;
    }

    private Const readReferConst(int tag) {
        ReferConst con=null;
        switch (tag){
            case Const.TAG_STRING:
                con=new StringConst(consts);
                break;
            case Const.TAG_CLASS:
                con=new ClassConst(consts);
                break;
            case Const.TAG_METHOD_TYPE:
                con=new MethodTypeConst(consts);
                break;
        }
        con.refer=readShort();
        return con;
    }


    private Const readUtf8() {
        Utf8Const con=new Utf8Const();
        int length= readShort();
        byte[] bytes=new byte[length];
        System.arraycopy(source,offset,bytes,0,length);
        try {
            con.value=new String(source,offset,length,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            con.value=new String(source,offset,length);
        }
        offset+=length;
        return con;
    }
    private Const readConst(int tag,int size) {
        Const cp=new Const();
        cp.tag=tag;
        offset+=size;
        return cp;
    }


    private int readShort() {
        int ret= ((source[offset]&0xFF)<<8)|(source[offset+1]&0xFF);
        offset+=2;
        return ret;
    }
    private int readByte() {
        return source[ offset++]&0xFF;
    }

    public static class Const{
        public static final int TAG_UTF8=1;
        public static final int TAG_INTEGER=3;
        public static final int TAG_FLOAT=4;
        public static final int TAG_LONG=5;
        public static final int TAG_DOUBLE=6;
        public static final int TAG_CLASS=7;
        public static final int TAG_STRING=8;
        public static final int TAG_FIELDREF=9;
        public static final int TAG_METHODREF=10;
        public static final int TAG_INTERFACE_METHODREF=11;
        public static final int TAG_NAME_AND_TYPE=12;
        public static final int TAG_METHOD_HANDLE=15;
        public static final int TAG_METHOD_TYPE=16;
        public static final int TAG_INVOKE_DYNAMIC=18;

        int tag;
        public int getTag(){
            return tag;
        }
        private static final String[] NAMES=
                {"UNKNOWN",
                        "Utf8",
                        "UNKNOWN",
                        "Integer",//boolean byte short都是integer
                        "Float",
                        "Long",
                        "Double",
                        "Class",
                        "String",
                        "Fieldref",
                        "Methodref",
                        "InterfaceMethodref",//
                        "NameAndType",
                        "UNKNOWN","UNKNOWN",
                        "MethodHandle",
                        "MethodType",
                        "UNKNOWN",
                        "InvokeDynamic",//18
                };
        public String getTagName(){
            if(tag<1||tag>NAMES.length){
                return "UNKNOWN";
            }
            return NAMES[tag];
        }
        public String toString(){
            return getTagName();
        }
    }
    public static class Utf8Const extends Const{
        public Utf8Const(){
            tag=TAG_UTF8;
        }
        String value;
        public String getValue(){
            return value;
        }
        public String toString(){
            return "Utf8 "+value;
        }
    }
    public static class StringConst extends ReferConst{
        public StringConst(Const[] consts){
            super(consts);
            tag=TAG_STRING;
        }
    }
    protected static class ReferConst extends Const{
        public ReferConst(Const[] consts){
            this.consts=consts;
        }
        int refer;
        Const[] consts;
        public String getValue(){
            Utf8Const cast=(Utf8Const)consts[refer];
            return cast.getValue();
        }
        public String toString(){
            return getTagName()+" "+getValue();
        }
    }
    public static class ClassConst extends ReferConst{
        public ClassConst(Const[] consts){
            super(consts);
            tag=TAG_CLASS;
        }
    }
    public static class MethodTypeConst extends ReferConst{
        public MethodTypeConst(Const[] consts){
            super(consts);
            tag=TAG_METHOD_TYPE;
        }
    }
    public static class BioConst extends Const{
        public BioConst(int tag,Const[] consts){
            this.tag=tag;
            this.consts=consts;
        }
        int refer1;
        int refer2;
        Const[] consts;
        public int getRefer1(){
            return refer1;
        }
        public int getRefer2(){
            return refer2;
        }
        public Const getRefer1Const(){
            return consts[refer1];
        }
        public Const getRefer2Const(){
            return consts[refer2];
        }
        public String toString(){
            //第一个通常是class 第二个 可能是NameAndType
            return getTagName()+" "+getRefer1Const()+"#"+getRefer2Const();
        }
    }

    public static void main(String[] args) throws IOException {
        File file =new File("D:\\IdeaProjects\\dfish-project\\fzwp_maven\\target\\classes/com/rongji/fzwp/cmp/manage/service/CallDelService.class");
        JavaClassConstReader reader=new JavaClassConstReader(file);
        Const[] consts=reader.getConsts();
        for (int i=0;i<consts.length;i++){
            Const con=consts[i];
            System.out.println("#"+(i)+": "+con);
        }
    }
}
