package com.rongji.dfish.base.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 默认切词器。
 * 亚洲字体(含汉字)每个字一个词语。
 * 全角当做亚洲字体
 * 英文、法文、德文。碰到空格和或符号切词。
 * 空格和符号当做 停止词。
 *
 *
 */
public class SimpleWordSplitter implements WordSplitter{

    private static SimpleWordSplitter instance=new SimpleWordSplitter();
    List<CharInfo> charinfos=new ArrayList<>();
    private SimpleWordSplitter(){
        init();
    }

    private void init() {
        charinfos.add(new CharInfo(ASIA,"基本汉字",(char)0x4E00,(char)0x9FA5));
        charinfos.add(new CharInfo(ASIA,"基本汉字补充",(char)0x9FA6,(char)0x9FCB));
        charinfos.add(new CharInfo(ASIA,"扩展A",(char)0x3400,(char)0x4DB5));
        //JDK 8现在支持不了这些汉字，加入可能会报错。0x20000当做0
        // charinfos.add(new CharInfo(ASIA,"扩展B",(char)0x20000,(char)0x2A6D6));
        // charinfos.add(new CharInfo(ASIA,"扩展C",(char)0x2A700,(char)0x2B734));
        // charinfos.add(new CharInfo(ASIA,"扩展D",(char)0x2B740,(char)0x2B81D));
        charinfos.add(new CharInfo(ASIA,"康熙部首",(char)0x2F00,(char)0x2FD5));
        charinfos.add(new CharInfo(ASIA,"部首扩展",(char)0x2E80,(char)0x2EF3));
        charinfos.add(new CharInfo(ASIA,"兼容汉字",(char)0xF900,(char)0xFAD9));
        // charinfos.add(new CharInfo(ASIA,"兼容扩展",(char)0x2F800,(char)0x2FA1D));
        charinfos.add(new CharInfo(ASIA,"PUA(GBK)部件",(char)0xE815,(char)0xE86F));
        charinfos.add(new CharInfo(ASIA,"部件扩展",(char)0xE400,(char)0xE5E8));
        charinfos.add(new CharInfo(ASIA,"PUA增补",(char)0xE600,(char)0xE6CF));
        charinfos.add(new CharInfo(ASIA,"汉字笔画",(char)0x31C0,(char)0x31E3));
        charinfos.add(new CharInfo(ASIA,"汉字结构",(char)0x2FF0,(char)0x2FFB));
        charinfos.add(new CharInfo(ASIA,"汉语注音",(char)0x3105,(char)0x3120));
        charinfos.add(new CharInfo(ASIA,"注音扩展",(char)0x31A0,(char)0x31BA));
        charinfos.add(new CharInfo(ASIA,"〇",(char)0x3007,(char)0x3007));
        charinfos.add(new CharInfo(NUMBER,"数字",'0','9'));
        charinfos.add(new CharInfo(WORD,"基本拉丁文(英文大写)",'A','Z'));
        charinfos.add(new CharInfo(WORD,"基本拉丁文(英文小写)",'a','z'));
        charinfos.add(new CharInfo(WORD,"拉丁文补充-1-1",(char)0xC0,(char)0xD6));
        charinfos.add(new CharInfo(WORD,"拉丁文补充-1-2",(char)0xD8,(char)0xF6));
        charinfos.add(new CharInfo(WORD,"拉丁文补充-1-3",(char)0xF8,(char)0xFF));
        charinfos.add(new CharInfo(WORD,"拉丁文扩展-A",(char)0x100,(char)0x17F));
        charinfos.add(new CharInfo(WORD,"拉丁文扩展-B",(char)0x180,(char)0x24F));
        charinfos.add(new CharInfo(WORD,"拉丁文扩充附加",(char)0x1E00,(char)0x1EFF));
        charinfos.add(new CharInfo(WORD,"拉丁文扩展-C",(char)0x2C60,(char)0x2C7F));
        charinfos.add(new CharInfo(WORD,"拉丁文扩展-D-1",(char)0xA720,(char)0xA7B7));
        charinfos.add(new CharInfo(WORD,"拉丁文扩展-D-2",(char)0xA7F7,(char)0xA7FF));

        Collections.sort(charinfos, new Comparator<CharInfo>() {
            @Override
            public int compare(CharInfo o1, CharInfo o2) {
                return o1.begin-o2.begin;
            }
        });
    }

    /**
     * 获得切词器实例
     * @return
     */
    public static SimpleWordSplitter getInstance(){
        return instance;
    }
    private CharInfo binarySearch(char c){
        int low = 0;
        int high = charinfos.size()-1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            CharInfo midVal = charinfos.get(mid);
            if (c>midVal.end) {
                low = mid + 1;
            }else if (c<midVal.begin) {
                high = mid - 1;
            }else {
                return charinfos.get(mid);
            }
        }
        return null;
    }
    private int type(char c){
        CharInfo ci=binarySearch(c);
        if(ci!=null){
            return ci.type;
        }
        return STOP;
    }

    private static final int ASIA=9;
    private static final int STOP=0;
    private static final int WORD=1;
    private static final int NUMBER=2;

    @Override
    public List<String> split(String text) {
        ArrayList<String> ret=new ArrayList<>();
        int lastType=STOP;
        int lastPos=0;
        char[] ca=text.toCharArray();
        for(int pos=0;pos<ca.length;pos++){
            char c=ca[pos];
            int type=type(c);
            if(lastType!=type){//只有变化的时候才进行运算
                switch (lastType){
                    case NUMBER:
                    case WORD:{
                        ret.add(text.substring(lastPos,pos));
                        break;
                    }
                    case ASIA:{
                        for(int i=lastPos;i<pos;i++){
                            ret.add(text.substring(i,i+1));
                        }
                        break;
                    }
                    default:
                }
                lastPos=pos;
                lastType=type;
            }
        }
        if(lastType!=STOP){
            if(lastType==ASIA){
                for(int i=lastPos;i<text.length();i++){
                    ret.add(text.substring(i,i+1));
                }
            }else {
                ret.add(text.substring(lastPos));
            }
        }
        return ret;
    }
    static class CharInfo{
        int type;
        String name;
        char begin;
        char end;
        CharInfo(int type,String name,char begin,char end){
            this.type=type;
            this.name=name;
            this.begin=begin;
            this.end=end;
        }
    }

}
