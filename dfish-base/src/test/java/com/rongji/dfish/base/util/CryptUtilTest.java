package com.rongji.dfish.base.util;

import java.io.*;
import java.util.ArrayList;

import com.rongji.dfish.base.crypt.AbstractCryptor;
import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.Cryptor;
import com.rongji.dfish.base.crypt.StringCryptor;
import org.junit.Test;

public class CryptUtilTest {
	
	public void testJCECryptTool() throws Exception {
		
		CryptUtil2.JCECryptTool tool=new CryptUtil2.JCECryptTool(CryptUtil.ALGORITHM_BLOWFISH,"我就是密码1".getBytes("UTF-8"));
		FileInputStream fis1=new FileInputStream("D:/4_行政/培训/2018/加解密基础知识普及.pptx");
		FileOutputStream fos1=new FileOutputStream("D:/4_行政/培训/2018/加解密基础知识普及.myfile");
		tool.setIn(fis1);
		tool.setOut(fos1);
		tool.encrypt();
		fis1.close();
		fos1.close();
		
		
		FileInputStream fis2=new FileInputStream("D:/4_行政/培训/2018/加解密基础知识普及.myfile");
		FileOutputStream fos2=new FileOutputStream("D:/4_行政/培训/2018/加解密基础知识普及_解密版.pptx");
		tool.setIn(fis2);
		tool.setOut(fos2);
		tool.decrypt();
		fis2.close();
		fos2.close();
	}
	@Test
	public void testGzipAndCryptTool() throws Exception {
		
		CryptUtil2.GzipAndCryptTool tool=new CryptUtil2.GzipAndCryptTool(CryptUtil.ALGORITHM_BLOWFISH,"我就是密码1".getBytes("UTF-8"));
		FileInputStream fis1=new FileInputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext.xml");
		FileOutputStream fos1=new FileOutputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext.myfile");
		tool.setIn(fis1);
		tool.setOut(fos1);
		tool.encrypt();
		fis1.close();
		fos1.close();
		
		
		FileInputStream fis2=new FileInputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext.myfile");
		FileOutputStream fos2=new FileOutputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext(解密).xml");
		tool.setIn(fis2);
		tool.setOut(fos2);
		tool.decrypt();
		fis2.close();
		fos2.close();
	}

	public static void main (String[] args) throws IOException {
		Cryptor c=CryptUtil.prepareCryptor("RSA","THIS_IS_".getBytes())
				.gzip(true).encoding("UTF-8").present(CryptUtil.PRESENT_BASE64).build();
		String src="君不见，黄河之水天上来⑵，奔流到海不复回。\n" +
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
				"五花马⒂，千金裘，呼儿将出换美酒，与尔同销万古愁⒃。 [1] ";
//		src="dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
		src=src+src+src+src+src+src+src+src;
		src=src+src+src+src+src+src+src+src;
		src=src+src;
//		src="123456789";
//		//凑到64K。
		StringCryptor sc=CryptFactory.getStringCryptor(null,CryptFactory.ENCODING_UTF8_GZIP,CryptFactory.PRESENT_STYLE_BASE64,null);
		String oldValue=sc.encrypt(src);
		String en=c.encrypt(src);

		System.out.println(" OLD equals="+en.equals(oldValue));
//		if(!en.equals(oldValue)){
//			System.out.println("oldVaue.length="+oldValue.length()+" value.length"+en.length());
//			for(int i=0;i<oldValue.length();i++){
//				if(oldValue.charAt(i)!=en.charAt(i)){
//					System.out.print("第"+i+"个字符不同 ");
//				}
//			}
//		}
		System.out.println(en);
		System.out.println("长度由"+src.getBytes("UTF8").length+"变为"+en.length());
		String de=c.decrypt(en);
////		System.out.println(de);
		System.out.println("equals="+src.equals(de));

//		String base32Value="3Y5GG00000000000XQ94PNQ38RA0DR3SAV8NQJ3TEAS1E6YCPDWNH3K9BSV96D560E0GCDTFVDG7PBVAAQ4HMS8B56CQ6WHJSZT9QTA8ENTYZZFB1FPX6RNMQFWB7YFQVD7FBPMAYW7WBY333GZ8DGAN67D8XZCB7HXMN7Y28MVKWDE3WRR3XFD4YDGV06TE7ZZPHB7W2KZ5PZVX2HZ5Z2A7FFAM2BXEVJBWFMXGKYJZFCEQ1XFNFAKYMWCFBRY9DQE6RNX7FZJXJWTXBX62CSFQ5K60WRVBQZXE6D8CMZRYCJWYQPJB5HEYEMRSTNRVB6Y9S5JQJDR3F3NXSNJWZGANNA4SPPFCPZ1QRBFPPV5EWS9XWKQ6MZFTW0YFTS9H7X5479QSXE174XAWSTVC7VDGVH5D6XMBT6J1HDTK87RQD4KT2THMCXBHBE6BDJQ3B7PDZSESD3CY8FGBGFNZ0VBSDRT2WHJ7N5KSX3FB7V1ZBHFWPHDEQN5VYQQX5751V75V7JBJKZ8NX2H4FS5KB3BRFAFPFBZN4SVJZJWW4SMYDM4R5MD0Z8BXJDKPYXA1TMWTQQKYNEZ7DJXXNMXCKSW7RQHN77JDXEVD7XERK9BGFWVAFQNKVX3AQP9PFCZ5F3XR7D2WX2EJY7G7WQ4QNE2V15VSZTVSAV7N47K9KX2P7B9W7076QKX1QPXYRWXWB7Q7V327VNWFG73E3Z1ZHR9XGFWEJC3D29V9PADXV0Y4ZT9FGSCSXKEZJXWMVXTK9G7X5T4BSD31GSJNWXXXWTEM3765MADTF2FTXY2KQACV8QNNPT9QF6QFEZD5TV3ZNYQ6XWPESAZXFCHR7YN9ZCXQT2DAE063RRJEWZH9CXXYYCEPP46VQ8DXADTHQHB8MFFAXNEC7ENYJXRZFVWZKNRF059MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MRCE52H9MSZRFTDZG0W905WGR0MR100";
//		byte[] value=Base32.decode(base32Value.getBytes());
//
//		ByteArrayOutputStream baos2=new ByteArrayOutputStream();
//		AbstractCryptor.Base32InputStream is=new AbstractCryptor.Base32InputStream(new ByteArrayInputStream(base32Value.getBytes()));
//		byte[] buff=new byte[8192];
//		int r=0;
//		while((r=is.read(buff))>=0){
//			baos2.write(buff,0,r);
//		}
//		byte[] newValue=baos2.toByteArray();
//
//		System.out.println("byte size="+value.length+" new byte size="+newValue.length);
//		for(int i=0;i<value.length;i++){
//			if(value[i]!=newValue[i]){
//				System.out.println("第 "+i+"字节不一样"+Integer.toHexString(value[i]&0xff)+" "+Integer.toHexString(newValue[i]&0xff));
//			}
//		}

//		System.out.println();
//		System.out.print(new String( baos2.toByteArray(),"UTF-8"));

		//
//		String de2=sc.decrypt(en);
////		System.out.println(de2);
//		System.out.println("equals de2="+src.equals(de2));
////
//		Digester d=CryptUtil.prepareDigester("SHA-1")
//				.encoding("UTF-8").present(3).build();
//		String di=d.digest(src);
//		System.out.println(di);

		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		AbstractCryptor.Base64UrlsafeOutputStream os=new AbstractCryptor.Base64UrlsafeOutputStream(baos);
		os.write("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZa".getBytes("UTF-8"));
		os.close();
		String result=new String(baos.toByteArray());
		System.out.println(result);
//
		ByteArrayOutputStream baos2=new ByteArrayOutputStream();
		AbstractCryptor.Base64InputStream is=new AbstractCryptor.Base64InputStream(new ByteArrayInputStream(result.getBytes()));
		byte[] buff=new byte[8192];
		int r=0;
		int i=is.read();
		i=is.read();
//		i=is.read();
//		i=is.read();
//		i=is.read();
		while((r=is.read(buff))>=0){
			baos2.write(buff,0,r);
		}
//		System.out.println();
		System.out.println(new String( baos2.toByteArray(),"UTF-8"));




//		}
//		baos1.reset();

//		ByteArrayOutputStream baos1=new ByteArrayOutputStream();
//		AbstractCryptor.HexOutputStream hos =new AbstractCryptor.HexOutputStream(baos1);
//		hos.write("haha".getBytes());
//		String hex=new String(baos1.toByteArray());
//		System.out.println(hex);
//		baos1.reset();
//
//		ByteArrayInputStream bais=new ByteArrayInputStream(hex.getBytes());
//		AbstractCryptor.HexInputStream his =new AbstractCryptor.HexInputStream(bais);
//		byte[] buff=new byte[8192];
//		int r=his.read(buff);
//		String res=new String(buff,0,r,"UTF-8");
//		System.out.println(res);


	}

	private static void checkEuqals(byte[] bytes, byte[] buff) {
		for(int i=0;i<bytes.length;i++){
			if(bytes[i]!=buff[i]){
				out(bytes,bytes.length);
				System.out.println("第"+i+"个字节不等");
				out(buff,bytes.length);
				System.out.println();
				break;
			}
		}
	}

	private static void out(byte[] bytes, int length) {
		ArrayList list=new ArrayList(length);
		for(int i=0;i<bytes.length&&i<length;i++){
			list.add(bytes[i]);
		}
		System.out.print(list);
	}
}
