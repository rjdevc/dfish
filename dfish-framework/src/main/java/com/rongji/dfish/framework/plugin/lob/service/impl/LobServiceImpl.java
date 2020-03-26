package com.rongji.dfish.framework.plugin.lob.service.impl;

import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import com.rongji.dfish.framework.plugin.lob.service.LobService;
import com.rongji.dfish.framework.service.impl.AbstractFrameworkService4Simple;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * lob数据服务层默认实现
 *
 * @author lamontYu
 * @date 2019-09-23
 * @since 5.0
 */
public class LobServiceImpl extends AbstractFrameworkService4Simple<PubLob, String> implements LobService {
    public static final String ARCHIVE_YES="1";
    public static final String ARCHIVE_NO="1";
    public static final String ENCODING="UTF-8";
    private static final int ZIP_THRESHOLD= 1024;
    private static final byte[] TYPE_PERFIX_TEXT=new byte[]{-1,'T'};
    private static final byte[] TYPE_PERFIX_ZIPPED=new byte[]{-1,'Z'};


    private byte[] format(String text){
        if(text==null){
            return null;
        }
        if(text.length()>ZIP_THRESHOLD){
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            try {
                baos.write(TYPE_PERFIX_ZIPPED);
                GZIPOutputStream out = new GZIPOutputStream(baos);
                byte[] textBytes = text.getBytes(ENCODING);
                out.write(textBytes);
                out.close();
                return baos.toByteArray();
            }catch (IOException ioe){
                LogUtil.error(null,ioe);
            }
            return null;
        }else{
            byte[] textBytes;
            try {
                textBytes=text.getBytes(ENCODING);
            } catch (UnsupportedEncodingException e) {
                textBytes=text.getBytes();
            }
            byte[] ret=new byte[textBytes.length+2];
            System.arraycopy(TYPE_PERFIX_TEXT,0,ret,0,2);
            System.arraycopy(textBytes,0,ret,2,textBytes.length);
            return ret;
        }
    }
    private String parse(byte[] data){
        if(data==null){
            return null;
        }
        if(data.length<2){
            try {
                return new String(data,ENCODING);
            } catch (UnsupportedEncodingException e) {
                return new String(data);
            }
        }
        if(data[0]==TYPE_PERFIX_TEXT[0]&& data[1]==TYPE_PERFIX_TEXT[1]){
            try {
                return new String(data,2,data.length-2,ENCODING);
            } catch (UnsupportedEncodingException e) {
                return new String(data,2,data.length-2);
            }
        }else  if(data[0]==TYPE_PERFIX_ZIPPED[0]&& data[1]==TYPE_PERFIX_ZIPPED[1]){
            try {
                ByteArrayInputStream bais=new ByteArrayInputStream(data,2,data.length-2);
                GZIPInputStream in = new GZIPInputStream(bais);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                byte[] buff=new byte[8192];
                int read=0;
                while((read=in.read(buff))>=0){
                    baos.write(buff,0,read);
                }
                return new String(baos.toByteArray(),ENCODING);
            } catch (IOException e) {
                return null;
            }
        }else{
            try {
                return new String(data,ENCODING);
            } catch (UnsupportedEncodingException e) {
                return new String(data);
            }
        }
    }
    private byte[] read(InputStream lobData){
        try {
            byte[] buffer = new byte[lobData.available()];
            lobData.read(buffer);
            return buffer;
        }catch (IOException e) {
            LogUtil.error(null,e);
        }finally{
            if(lobData!=null) {
                try {
                    lobData.close();
                } catch (IOException e) {
                    LogUtil.error(null,e);
                }
            }
        }
        return null;
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String src=
//                "君不见，黄河之水天上来⑵，奔流到海不复回。\n" +
//                        "君不见，高堂明镜悲白发，朝如青丝暮成雪⑶。\n" +
//                        "人生得意须尽欢⑷，莫使金樽空对月。\n" +
//                        "天生我材必有用，千金散尽还复来。\n" +
//                        "烹羊宰牛且为乐，会须一饮三百杯⑸。\n" +
//                        "岑夫子，丹丘生⑹，将进酒，杯莫停⑺。\n" +
//                        "与君歌一曲⑻，请君为我倾耳听⑼。\n" +
//                        "钟鼓馔玉不足贵⑽，但愿长醉不复醒⑾。\n" +
//                        "古来圣贤皆寂寞，惟有饮者留其名。\n" +
//                        "陈王昔时宴平乐，斗酒十千恣欢谑⑿。\n" +
//                        "主人何为言少钱⒀，径须沽取对君酌⒁。\n" +
//                        "五花马⒂，千金裘，呼儿将出换美酒，与尔同销万古愁⒃。 ";
//        testOneRound(src);
//        src=src+src+src+src+src;
//        testOneRound(src);
//        src=src+src+src+src+src;
//        testOneRound(src);
//
//    }
//
//    private static void testOneRound(String src) throws UnsupportedEncodingException {
//        LobServiceImpl lsi=new LobServiceImpl();
//        byte[] formatted=lsi.format(src);
//        String parsed=lsi.parse(formatted);
//        System.out.println("原始文本有"+src.length()+"个字符，共"+src.getBytes(ENCODING).length+"个字节。" +
//                "转化后有"+formatted.length+"个字节。 解析后 equals="+(src.equals(parsed)));
//    }


    @Resource(name = "lobDao")
    private LobDao dao;

    @Override
    public LobDao getDao() {
        return dao;
    }

    public void setDao(LobDao dao) {
        this.dao = dao;
    }

    @Override
    public String saveContent(String text) throws Exception {
        if (Utils.isEmpty(text)) {
            throw new MarkedException("文本内容不能为空");
        }
        return saveLobData(format(text));
    }


    @Override
    public String saveLobData(InputStream lobData) throws Exception {
        if (lobData == null) {
            throw new MarkedException("存储内容不能为空");
        }
        return saveLobData(read(lobData));
    }
    @Override
    public String saveLobData(byte[] lobData) throws Exception {
        if (lobData==null) {
            throw new MarkedException("存储内容不能为空");
        }
        String lobId = getNewId();
        PubLob pubLob = new PubLob();
        pubLob.setLobId(lobId);
        pubLob.setOperTime(new Date());
        pubLob.setArchiveFlag(ARCHIVE_NO);
        pubLob.setLobData(lobData);
        getDao().save(pubLob);
        return lobId;
    }

    @Override
    public int updateContent(String lobId, String text) {
        return updateLobData(lobId,format(text));
    }

    @Override
    public int updateLobData(String lobId, byte[] lobData) {
        return getDao().updateLobData(lobId, lobData, new Date());
    }

    @Override
    public int updateLobData(String lobId, InputStream lobData) {
        return updateLobData(lobId,read(lobData));
    }



    @Override
    public int archive(String lobId) {
        return getDao().archive(lobId, ARCHIVE_YES, new Date());
    }


    @Override
    public byte[] getLobData(String lobId) {
        Map<String, byte[]> lobMap = getLobDatas(Arrays.asList(lobId));
        return lobMap.get(lobId);
    }

    @Override
    public Map<String, byte[]> getLobDatas(String... lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        return getLobDatas(Arrays.asList(lobIds));
    }

    @Override
    public Map<String, byte[]> getLobDatas(Collection<String> lobIds) {
        return getDao().getLobDatas(lobIds);
    }


    @Override
    public String getContent(String lobId) {
        Map<String, String> lobMap = getContents(lobId);
        return lobMap.get(lobId);
    }

    @Override
    public Map<String, String> getContents(String... lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        return getContents(Arrays.asList(lobIds));
    }

    @Override
    public Map<String, String> getContents(Collection<String> lobIds) {
        Map<String, byte[]> lobDatas = getDao().getLobDatas(lobIds);
        Map<String, String> contents = new HashMap<>(lobDatas.size());
        for (Map.Entry<String, byte[]> entry : lobDatas.entrySet()) {
            contents.put(entry.getKey(), parse(entry.getValue()));
        }
        return contents;
    }

}
