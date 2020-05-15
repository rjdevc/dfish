package com.rongji.dfish.framework.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.info.EthNetInfo;
import com.rongji.dfish.framework.info.ServletInfo;
import com.rongji.dfish.misc.util.XMLUtil;

/**
 * ID生成器,提供id生成的方法,目前提供32位UUID和16位以时间排序的2种编号方式
 *
 * @author DFish Team
 */
public class IdGenerator {


    /**
     * 获取32位的UUID字符串,默认小写方式
     *
     * @return 32位的UUID
     */
    public static String getUUID() {
        return getUUID(false);
    }

    /**
     * 获取32位的UUID字符串
     *
     * @param toUpperCase 是否转大写
     * @return 32位的UUID
     */
    public static String getUUID(boolean toUpperCase) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (toUpperCase) {
            uuid = uuid.toUpperCase();
        }
        return uuid;
    }

    // 进制数
    private static final int RADIX = 32;
    // 存储的时间戳长度
    private static final int LENGTH_TIMESTAMP = 9;
    // 存储的系统标识码长度
    private static final int LENGTH_SYSTEM_KEY = 3;
    // 存储的计数长度
    private static final int LENGTH_INDEX = 4;
    // 生成的ID位数
    private static final int LENGTH_ID = LENGTH_TIMESTAMP + LENGTH_SYSTEM_KEY + LENGTH_INDEX;
    // 上一个时间
    private static volatile long lastTime = 0L;
    // 上一个排序号
    private static volatile AtomicLong lastIndex = new AtomicLong(0);
    // 系统标示符
    private static String systemKey = "";

    /**
     * 生成16位{@link #LENGTH_ID}以时间排序的ID编号
     *
     * @return 16位的ID编号
     */
    public static String getSortedId() {
        return getSortedId(false);
    }

    /**
     * 生成16位{@link #LENGTH_ID}以时间排序的ID编号
     *
     * @param timestamp 1970到现在为止的毫秒数
     * @return 16位的ID编号
     */
    public static String getSortedId(long timestamp) {
        return getSortedId(timestamp, false);
    }

    /**
     * 生成16位{@link #LENGTH_ID}以时间排序的ID编号
     *
     * @param toUpperCase 是否转大写
     * @return 16位的ID编号
     */
    public static String getSortedId(boolean toUpperCase) {
        // 1970到现在为止的毫秒数
        long timestamp = System.currentTimeMillis();
        return getSortedId(timestamp, toUpperCase);
    }

    /**
     * 生成16位{@link #LENGTH_ID}以时间排序的ID编号
     *
     * @param timestamp   1970到现在为止的毫秒数
     * @param toUpperCase 是否转大写
     * @return 16位的ID编号
     */
    public static String getSortedId(long timestamp, boolean toUpperCase) {
        String timestampStr = Long.toString(timestamp, RADIX);
        StringBuilder idStr = new StringBuilder(timestampStr);
        if (timestampStr.length() != LENGTH_TIMESTAMP) {
            fixString(idStr, LENGTH_TIMESTAMP);
        }

        // 系统标识符
        String storeSystemKey = getStoreSystemKey();
        idStr.append(storeSystemKey);

        // 计数
        long li;
        if (timestamp != lastTime) {
            lastTime = timestamp;
            lastIndex.set(0);
            li=0;
        } else {
            li=lastIndex.incrementAndGet();
        }
        StringBuilder storeIndex = new StringBuilder();
        storeIndex.append(Long.toString(li, RADIX));
        fixString(storeIndex, LENGTH_INDEX);
        idStr.append(storeIndex.toString());
        String id = idStr.toString();
        if (toUpperCase) { // 转大写
            id = id.toUpperCase();
        }
        return id;
    }

    /**
     * 系统标识码(3位{@link #LENGTH_SYSTEM_KEY} 32进制数{@link #RADIX})
     *
     * @return
     */
    private static String getStoreSystemKey() {
        if (Utils.isEmpty(systemKey)) {
            String xmlFilePath = "";
            try {
                // Web工程获取方式
                @SuppressWarnings("deprecation")
                String appRealPath = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath();
                xmlFilePath = appRealPath + "WEB-INF/config/";
            } catch (Exception e) {
                // 获取不到时,默认系统临时文件夹产生
                xmlFilePath = System.getenv("TMP") + "/";
            }
            // 文件直接使用dfish-config.xml这个文件
            String fileName = "dfish-config.xml";
            String keyProperty = "dfish.id.systemKey";
            XMLUtil xmlTools = null;
            try {
                xmlTools = new XMLUtil(xmlFilePath + fileName);
                systemKey = xmlTools.getProperty(keyProperty);
                if (Utils.isEmpty(systemKey)) {
                    long maxKeyValue = 1;
                    int power = (int) Math.sqrt(RADIX);
                    for (int i = 0; i < LENGTH_SYSTEM_KEY; i++) {
                        maxKeyValue = maxKeyValue << power;
                    }
                    Random random = new Random();
                    int keyValue = random.nextInt((int) maxKeyValue);
                    systemKey = Integer.toString(keyValue, RADIX);
                    StringBuilder sb = new StringBuilder();
                    sb.append(systemKey);
                    fixString(sb, LENGTH_SYSTEM_KEY);
                    systemKey = sb.toString();
                    xmlTools.setProperty(keyProperty, systemKey);
                    xmlTools.writeDoc("UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 错误时系统标示符全部补0
                StringBuilder errorSystemKey = new StringBuilder();
                fixString(errorSystemKey, LENGTH_SYSTEM_KEY);
                systemKey = errorSystemKey.toString();
            }
        }
        return systemKey;
    }

    /**
     * 自动补全字符串长度，若太长则自动截掉前面的字符，保证产生的ID位数不会超出
     *
     * @param sb        字符串
     * @param strLength 字符串长度
     */
    private static void fixString(StringBuilder sb, int strLength) {
        if (sb != null && strLength > 0) {
            String oldStr = sb.toString();
            int fixLength = strLength - sb.length();
            sb.setLength(0);
            if (fixLength < 0) {
                sb.append(oldStr.substring(-fixLength));
            } else {
                for (int i = 0; i < fixLength; i++) {
                    sb.append('0');
                }
                sb.append(oldStr);
            }
        }
    }

    /**
     * 16位时间排序编号格式转换工具
     *
     * @author DFish Team
     */
    public static class TimeSortedIdFormat {
        String id;
        long[] value = new long[3];

        public TimeSortedIdFormat(String id) {
            if (id == null || id.length() != LENGTH_ID) {
                throw new UnsupportedOperationException("The length of this id(" + id + ") must be " + LENGTH_ID + ".");
            }
            this.id = id;
            int split1 = LENGTH_TIMESTAMP;
            int split2 = LENGTH_TIMESTAMP + LENGTH_SYSTEM_KEY;
            String timestampStr = id.substring(0, split1);
            value[0] = Long.parseLong(timestampStr, RADIX);
            String systemKeyStr = id.substring(split1, split2);
            value[1] = Long.parseLong(systemKeyStr, RADIX);
            String indexStr = id.substring(split2);
            value[2] = Long.parseLong(indexStr, RADIX);
        }

        /**
         * 获取编号id
         *
         * @return 编号
         */
        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
            return id + "={\"time\"=\"" + SDF.format(getTime()) + "\",\"systemKey\"=" + value[1] + ",\"sequence\"=" + value[2] + "}";
        }

        /**
         * 时间
         *
         * @return 时间
         */
        public Date getTime() {
            return new Date(value[0]);
        }

        /**
         * 序号
         *
         * @return 序号
         */
        public long getSequence() {
            return value[2];
        }

        /**
         * 标识
         *
         * @return 标识
         */
        public long getSystemKey() {
            return value[1];
        }
    }

    /**
     * 生成32位的不重复ID。和JDK的UUID不同，它是有序的。但如果服务器之间时间没有同步过，可能会有一些偏差。
     * 32字节使用16进制，等同于128bit 划分为12(48字节)位 为时间戳 12位为机器码(MAC) 8位为序列号
     *
     * @return 32位ID编号
     */
    public static String getSortedId32() {
        StringBuilder sb = new StringBuilder(32);
        appendHex(System.currentTimeMillis(), 12, sb);
        sb.append(getMac());
        appendHex(getIndex32(), 8, sb);
        return sb.toString();

    }

    private static final char[] HEX_CHARS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
    };

    private static void appendHex(long seed, int leng, StringBuilder sb) {
        for (int i = 0; i < leng; i++) {
            sb.append(HEX_CHARS[(int) (seed >> 4 * (leng - 1 - i)) & 0xF]);
        }
    }

    private static String mac = null;

    private static String getMac() {
        if (mac == null) {
            //去掉
            try {
                String strMac = EthNetInfo.getMacAddress();//如果取得失败，采用随机数
                strMac = strMac.toUpperCase();
                //strMac 有可能为 00:09:e9:fa:97:21的格式(win)
                //也可能 00-09-e9-fa-97-21的格式(linux)
                //也可能 0-9-E9-FA-97-21的格式(UNIX)
                String[] strsplit = strMac.split("[:-]");
                assert (strsplit.length == 6);
                StringBuilder sb = new StringBuilder();
                for (String sp : strsplit) {
                    if (sp.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(sp);
                }
                mac = sb.toString();
            } catch (Throwable ex) {
                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                long l = random.nextLong();
                appendHex(l, 12, sb);
                mac = sb.toString();
            }
        }
        return mac;
    }

    private static long index32 = 0xF00000000L;

    private static synchronized long getIndex32() {
        if (index32 > 0x200000000L) {
            //产生随机数
            Random random = new Random();
            index32 = random.nextInt();
        }
        if (++index32 > 0xFFFFFFFFL) {
            index32 = 0;
        }
        return index32;
    }

}
