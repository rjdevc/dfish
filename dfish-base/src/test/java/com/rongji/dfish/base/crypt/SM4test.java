package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.util.CryptoUtil;

import java.io.IOException;

public class SM4test {
    public static void main(String[] args) throws IOException {
        Cryptor c=CryptoUtil.prepareCryptor(CryptoUtil.ALGORITHM_SM4,"RJ002474RJ002474")
                .gzip(false).encoding("UTF-8").present(CryptoUtil.PRESENT_BASE64_URLSAFE).build();
        String src=
                "君不见，黄河之水天上来⑵，奔流到海不复回。\n" +
                "君不见，高堂明镜悲白发，朝如青丝暮成雪⑶。\n" +
                "人生得意须尽欢⑷，莫使金樽空对月。\n" +
                "天生我材必有用，千金散尽还复来。\n" +
                "烹羊宰牛且为乐，会须一饮三百杯⑸。\n" +
                "岑夫子，丹丘生⑹，将进酒，杯莫停⑺。\n" +
                "与君歌一曲⑻，请君为我倾耳听⑼。\n" +
                "钟鼓馔玉不足贵⑽，但愿长醉不复醒⑾。\n" +
                "古来圣贤皆寂寞，惟有饮者留其名。\n" +
                "陈王昔时宴平乐，斗酒十千恣欢谑⑿。\n" +
                "主人何为言少钱⒀，径须沽取对君酌⒁。\n" +
                "五花马⒂，千金裘，呼儿将出换美酒，与尔同销万古愁⒃。 ";
//		src="dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
//        src=src+src+src+src+src+src+src+src;
//        src=src+src+src+src+src+src+src+src;
//        src=src+src;

        String en=c.encrypt(src);
        System.out.println(en);

        String de=c.decrypt(en);
        System.out.println(de);
//        byte[] srcByte=src.getBytes("UTF-8");
//        System.out.println( ByteArrayUtil.toHexString(srcByte));
//        ByteArrayOutputStream baos=new ByteArrayOutputStream();
//        SM4ECBOutputStream sm4out=new SM4ECBOutputStream(baos,"RJ002474RJ002474".getBytes());
//        sm4out.write(srcByte);
//        sm4out.close();
//        byte [] enByte=baos.toByteArray();
//        System.out.println( ByteArrayUtil.toHexString(enByte));
//
//        ByteArrayInputStream bais=new ByteArrayInputStream(enByte);
//        baos=new ByteArrayOutputStream();
//        SM4ECBInputStream sm4in=new SM4ECBInputStream(bais,"RJ002474RJ002474".getBytes());
//        byte[] buff=new byte[8192];
//        int r=-1;
//        while( (r=sm4in.read(buff))>=0){
//            baos.write(buff,0,r);
//        }
//        byte[] deBytes=baos.toByteArray();
//        System.out.println( ByteArrayUtil.toHexString(deBytes));



    }
}
