package com.rongji.dfish.framework.plugin.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rongji.dfish.base.util.LogUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *校验码生成器。
 * 新建的时候默认参数为
 * <pre>
 * setWidth(80);
 * this.setHeight(40);
 * this.setCodeLength(4);
 * setInterfering(true);
 * setFont(new Font(Font.SERIF,Font.BOLD,16));
 * setRotateDegree(20);
 * setColorGroup(new Color[][]{
 *     {new Color(0x255A84),Color.LIGHT_GRAY},
 *     {new Color(0xC33E00),Color.LIGHT_GRAY},
 *     {new Color(0x5A7712),Color.LIGHT_GRAY}
 * });
 * </pre>
 */
public class CheckCodeGenerator {
	/**
	 * 验证码的Key
	 */
	public static final String KEY_CHECKCODE = "com.rongji.dfish.CHECKCODE";

	private String alias;
	private String imgType = "png";
	private Color[][] colorGroup;
	private boolean interfering;
	private int rotateDegree;
	private Font font;
	private int height;
	private int width;
	private int codeLength;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getCodeLength() {
		return codeLength;
	}
	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}
	public boolean isInterfering() {
		return interfering;
	}
	public void setInterfering(boolean interfering) {
		this.interfering = interfering;
	}
	public int getRotateDegree() {
		return rotateDegree;
	}
	public void setRotateDegree(int rotateDegree) {
		this.rotateDegree = rotateDegree;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Color[][] getColorGroup() {
		return colorGroup;
	}
	public void setColorGroup(Color[][] colorGroup) {
		this.colorGroup = colorGroup;
	}
	/**
	 * 新建一个校验码生成器。
	 * 新建的时候默认参数为
	 * <pre>
	 * setWidth(80);
	 * this.setHeight(40);
	 * this.setCodeLength(4);
	 * setInterfering(true);
	 * setFont(new Font(Font.SERIF,Font.BOLD,16));
	 * setRotateDegree(20);
	 * setColorGroup(new Color[][]{
	 *     {new Color(0x255A84),Color.LIGHT_GRAY},
	 *     {new Color(0xC33E00),Color.LIGHT_GRAY},
	 *     {new Color(0x5A7712),Color.LIGHT_GRAY}
	 * });
	 * </pre>
	 */
	public CheckCodeGenerator(){
		this.setWidth(80);
		this.setHeight(40);
		this.setCodeLength(4);
		this.setInterfering(true);
		this.setFont(new Font(Font.SERIF,Font.BOLD,16));
		this.setRotateDegree(20);
		setColorGroup(new Color[][]{
				{new Color(0x255A84),Color.LIGHT_GRAY},
				{new Color(0xC33E00),Color.LIGHT_GRAY},
				{new Color(0x5A7712),Color.LIGHT_GRAY}
		});
		setSetting(SETTING_READABLE);
	}
//	private static final char[] CHARS={'0','1','2','3','4','5','6','7','8','9',
//		'A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z'};//因为I O容易混淆
	private static final String SETTING_MUMBER="[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]";
	private static final String SETTING_UPPER_CASE=
			"[\"Aa\",\"Bb\",\"Cc\",\"Dd\",\"Ee\"," +
			"\"Ff\",\"Gg\",\"Hh\",\"Ii\",\"Jj\"," +
			"\"Kk\",\"Ll\",\"Mm\",\"Nn\",\"Oo\"," +
			"\"Pp\",\"Qq\",\"Rr\",\"Ss\",\"Tt\"," +
			"\"Uu\",\"Vv\",\"Ww\",\"Xx\",\"Yy\",\"Zz\"]";
	private static final String SETTING_NUMBER_AND_UPPER_CASE=
			"[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"," +
			"\"Aa\",\"Bb\",\"Cc\",\"Dd\",\"Ee\"," +
			"\"Ff\",\"Gg\",\"Hh\",\"Ii\",\"Jj\"," +
			"\"Kk\",\"Ll\",\"Mm\",\"Nn\",\"Oo\"," +
			"\"Pp\",\"Qq\",\"Rr\",\"Ss\",\"Tt\"," +
			"\"Uu\",\"Vv\",\"Ww\",\"Xx\",\"Yy\",\"Zz\"]";
	private static final String SETTING_READABLE=
			"[\"0Oo\",\"1IiLl\",\"2Zz\",\"3\",\"4\",\"5Ss\",\"6\",\"7\",\"8\",\"9\"," +
			"\"Aa\",\"Bb\",\"Cc\",\"Dd\",\"Ee\",\"Ff\",\"Gg\",\"Hh\",\"Jj\"," +
			"\"Kk\",\"Mm\",\"Nn\",\"Pp\",\"Qq\",\"Rr\",\"Tt\"," +
			"\"Uu\",\"Vv\",\"Ww\",\"Xx\",\"Yy\"]";
	private char[] chars;
	private Map<Character,Integer> charsMap;
	private static final Random RANDOM = new Random();
	public void setSettingScheme(int scheme){
		switch (scheme){
			case 1:
				setSetting(SETTING_MUMBER);
				break;
			case 2:
				setSetting(SETTING_UPPER_CASE);
				break;
			case 3:
				setSetting(SETTING_NUMBER_AND_UPPER_CASE);
				break;
			default:
				setSetting(SETTING_READABLE);
		}
	}

	public void setSetting(String setting) {
		try{
			JSONArray arr= (JSONArray)JSON.parse(setting);
			int i=0;
			chars=new char[arr.size()];
			charsMap=new HashMap<Character,Integer>();
			for(Object o:arr){
				String s=(String)o;
				char[] cs=s.toCharArray();
				chars[i]=cs[0];
				for(char c:cs){
					if(charsMap.put(c,i)!=null){
						LogUtil.warn("REPEAT CHAR : "+c);
						if(setting!=SETTING_READABLE) {
							setSetting(SETTING_READABLE);
						}
						return;
					}
				}
				i++;
			}
		}catch (Exception ex){
			LogUtil.warn("UNKOWN SETTING : "+setting);
			if(setting!=SETTING_READABLE) {
				setSetting(SETTING_READABLE);
			}
		}
	}
	public boolean checkCodeEquals(String str1,String str2){
		if(str1==null||str2==null||str1.length()!=str2.length()){
			return false;
		}
		char[] cs1=str1.toCharArray();
		char[] cs2=str2.toCharArray();
		for(int i=0;i<cs1.length;i++){
			Integer i1=charsMap.get(cs1[i]);
			Integer i2=charsMap.get(cs2[i]);
			if(i1==null||i2==null||i1.intValue()!=i2.intValue()){
				return false;
			}
		}

		return true;
	}

	/**
	 * 生成length长度的校验码，去除一些容易混淆的字符，如I(1混淆) O(0混淆) 
	 * @return
	 */
	public String getRandomCode(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<this.codeLength;i++){
			sb.append(chars[RANDOM.nextInt(chars.length)]);
		}
		return sb.toString();
	}

	/**
	 * 生成校验码图形
	 * @param code 校验码文本。可以使用getRandomString(int)方法预先获得。
	 * @return BufferedImage
	 */
	public BufferedImage generate(String code){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Color[] colors=getColorGroup()[RANDOM.nextInt(getColorGroup().length)];
		Color fontColor=colors[0];
		Color bgColor=colors[1];
		fillBG(g,bgColor,width,height);
		//分配校验码图形的空间。
		//边距默认各占5%，并且不小于2像素
		
		boolean horizontal=width>=height;
		//宽度比高度高的时候，从左向右排列，否则从上向下排列
		int codeLength=code.length();
		CharInfo[] cis=new CharInfo[codeLength];
		int twidth,theight;
		if(horizontal){
			twidth=width;
			theight=height;
		}else{
			//X Y翻转计算
			twidth=height;
			theight=width;
		}
		int leftPadding=twidth/20;
		if(leftPadding<2){
			leftPadding=2;
		}
		int topPadding=theight/20;
		if(topPadding<2){
			topPadding=2;
		}
		int useableWidth=twidth-leftPadding*2;
		int useableHeight=theight-topPadding*2;
		int hMaxFontSize=useableWidth*4/codeLength/3; //我们允许校验码最大的比最小的字体大一倍，所以，这里最大fontSize是平均的4/3
		int vMaxFontSize=useableHeight; 
		int maxFontSize=Math.min(hMaxFontSize, vMaxFontSize);
		int charUsableWidth=3*codeLength*maxFontSize/4;
		double[] rates=new double[codeLength];
		for(int i=0;i<rates.length;i++){
			rates[i]=1.0+RANDOM.nextDouble();
		}
		double allRate=0.0;
		for(double rate:rates){
			allRate+=rate;
		}
		int[] sizes=new int[codeLength];
		//计算每个size
		for(int i=0;i<rates.length;i++){
			sizes[i]=(int)(charUsableWidth*rates[i]/allRate+0.5);
		}
		//需要计算间距
		int leftWidthSpace=useableWidth;
		for(int size:sizes){
			leftWidthSpace-=size;
		}
		//如果这个图形比较狭长，可能还要个没两个字之间增加space
		int wSpace=0;
		if(codeLength>1&&leftWidthSpace>0){
			wSpace=leftWidthSpace/(codeLength-1);
		}
		//计算每个字该有的位置
		int curX=leftPadding;
		for(int i=0;i<codeLength;i++){
			int size=sizes[i];
			CharInfo ci=new CharInfo();
			cis[i]=ci;
			if(horizontal){
				ci.x=curX;
				if(useableHeight-size>=0){
					ci.y=topPadding+RANDOM.nextInt(useableHeight-size+1);
				}else{
					ci.y=topPadding;
				}
			}else{
				ci.y=curX;
				if(useableHeight-size>=0){
					ci.x=topPadding+RANDOM.nextInt(useableHeight-size+1);
				}else{
					ci.x=topPadding;
				}
			}
			ci.size=size;
			ci.code=new String(new char[]{code.charAt(i)});
			curX+=wSpace+size;
		}
		if(isInterfering()){
			drawInterfering(g,fontColor,bgColor,width,height,horizontal);
		}
		drawChars(g,fontColor, cis);
		g.dispose();
		return image;
	}
	private void drawChars(Graphics g, Color fontColor, CharInfo[] cis) {
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D) g;
			// LinLW 2010-11-18 抗锯齿
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
					RenderingHints.VALUE_STROKE_NORMALIZE);
		}
		g.setColor(fontColor);
		//绘制辅助线确定位置。
//		for(CharInfo c:cis){
//			g.drawRect(c.x, c.y, c.size, c.size);
//		}
		
		for(CharInfo c:cis){
			double rotate=0;
			if (g instanceof Graphics2D) {
				//获取随机旋转角度
				Graphics2D g2d = (Graphics2D) g;
				rotate = (RANDOM.nextInt(this.getRotateDegree()*2) - this.getRotateDegree()) * Math.PI / 180;
				g2d.rotate(rotate );
				g.setFont(new Font(getFont().getFamily(), getFont().getStyle(), c.size));
				double middle=c.size/2.0;
				int x1 = (int) (Math.cos(-rotate)*(c.x+middle)-Math.sin(-rotate)*(c.y+middle)-middle);
				int y1 = (int) (Math.cos(-rotate)*(c.y+middle)+Math.sin(-rotate)*(c.x+middle)+middle);
				//这是因为绘制文字的时候，原点必须往下移动字体的高度，所以从-middle变成+middle
				g.drawString(c.code, x1, y1);
				g2d.rotate(-rotate );
			}
		}
	}
	private static class CharInfo{
		int x;
		int y;
		int size;
		String code;
	}
	private void drawInterfering(Graphics g, Color fontColor,Color bgColor, int width,
			int height,boolean horizontal) {
		if(!(g instanceof Graphics2D))return;
		Color interferingColor=new Color((fontColor.getRed()+bgColor.getRed()*3)/4,(fontColor.getGreen()+bgColor.getGreen()*3)/4,(fontColor.getBlue()+bgColor.getBlue()*3)/4);
		g.setColor(interferingColor);
		if(!horizontal){
			int temp=width;
			width=height;
			height=temp;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
//		Location center = new Location(width, height,ys);
		int radius = 0;
		if(width<7) return;//宽度不能小于7,否则返回
		while(radius<width/2){
			radius = RANDOM.nextInt(width-3);
		}
		int modulus = 0;
		while(modulus<3){
			modulus = RANDOM.nextInt(height/2);
		}
		int offset=RANDOM.nextInt(height-modulus*2)-height/2+modulus;
		// 确定每个点的坐标
		int[] x = new int[width/3+1];
		int[] y = new int[width/3+1];
		for (int i = 0; i < width; i+=3) {
			x[i/3] = i;
			double y1 = Math.sin(((double) (-radius + i) / radius) * 4
					* Math.PI);// 这个很重要，sin()里面必须为double值
			int y2 = (int) (y1 * modulus);
			y[i/3] = height/2 - y2+offset;
		}
		if(horizontal){
			g2d.drawPolyline(x, y, width/3);
		}else{
			g2d.drawPolyline(y, x, width/3);	
		}
	}

	private void fillBG(Graphics g,Color bgColor,int width,int height) {
		g.setColor(bgColor);//设置背景色
		g.fillRect(0, 0, width, height);
	}


	public String drawImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/" + imgType);
		String randomCode = getRandomCode();
		// 同个session理论上不会同时出现多个验证码,所以这里名称以定死方式
		request.getSession().setAttribute(CheckCodeGenerator.KEY_CHECKCODE, randomCode);
		drawImage(response.getOutputStream(), randomCode);
		return randomCode;
	}

	public void drawImage(OutputStream output, String randomCode) throws IOException {
		try {

			BufferedImage image = generate(randomCode);
			ImageIO.write(image, imgType, output);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

}
