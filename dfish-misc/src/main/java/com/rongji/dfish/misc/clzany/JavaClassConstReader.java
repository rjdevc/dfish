package com.rongji.dfish.misc.clzany;

import com.rongji.dfish.base.util.ByteArrayUtil;

import java.io.File;

public class JavaClassConstReader {
    byte[] source;
    int offset;
    int classVersion;
    Const[] consts;


    public JavaClassConstReader(byte[] source){
        this.source=source;
        readConsts();
    }
    public JavaClassConstReader(File file){
        readConsts();
    }

    private void readConsts() {
        offset=6;//4 for magic CAFE, 2 for minVersion
        classVersion=readShort();
        int constLenth=readShort();
        consts= new Const[constLenth];
        for(int i=1;i<constLenth;i++){
            Const con=null;

            consts[i]=con;
            //Long 和 double;
            if(con!=null&&(con.getTag()==11||con.getTag()==12)){
                i++;
            }
        }
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
        int tag;
        public int getTag(){
            return tag;
        }
    }
    public static class Utf8Const extends Const{
        String value;
        public String getValue(){
            return value;
        }
        public String toString(){
            return "Utf8 "+value;
        }
    }
    public static class StringConst extends Const{
        int refer;
        Const[] consts;
        public String getValue(){
            Utf8Const cast=(Utf8Const)consts[refer];
            return cast.getValue();
        }
        public String toString(){
            return "String "+getValue();
        }
    }
    public static class ClassConst extends Const{
        int refer;
        Const[] consts;
        public String getClassName(){
            Utf8Const cast=(Utf8Const)consts[refer];
            return cast.getValue();
        }
        public String toString(){
            return "Class "+getClassName();
        }
    }

}
