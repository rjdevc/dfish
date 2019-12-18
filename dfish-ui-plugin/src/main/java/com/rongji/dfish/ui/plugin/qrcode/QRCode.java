package com.rongji.dfish.ui.plugin.qrcode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.widget.Img;

/**
 * 二维码组件
 * @author lamontYu
 * @date 2018-08-03 before
 * @since 3.1
 * @version 调整包层 lamontYu 2019-12-05
 */
public class QRCode extends AbstractWidgetWrapper<QRCode, Img>{

	private static final long serialVersionUID = 4326337757852569803L;

	private String url = "./qrCode/image";
	private String content;
	private int size = 100;
	private String format;
	private Integer onColor;
	private Integer offColor;

	public String getContent() {
		return content;
	}

	public QRCode setContent(String content) {
		this.content = content;
		return rebuildPrototype();
	}

	public int getSize() {
		return size;
	}

	public QRCode setSize(int size) {
		this.size = size;
		return rebuildPrototype();
	}

	public String getFormat() {
		return format;
	}

	public QRCode setFormat(String format) {
		this.format = format;
		return rebuildPrototype();
	}

	public Integer getOnColor() {
		return onColor;
	}

	public QRCode setOnColor(Integer onColor) {
		this.onColor = onColor;
		return this;
	}

	public Integer getOffColor() {
		return offColor;
	}

	public QRCode setOffColor(Integer offColor) {
		this.offColor = offColor;
		return this;
	}

	private QRCode rebuildPrototype() {
		prototype.setSrc(getImageUrl()).setHeight(size).setWidth(size);
		return this;
	}

	/**
	 * 构造方法,默认构造的二维码大小是100
	 * @param content 转码内容
	 */
	public QRCode(String content) {
		this(content, 100);
	}
	
	/**
	 * 构造方法
	 * @param content 转码内容
	 * @param size 二维码大小
	 */
	public QRCode(String content, int size) {
		this(content, size, "png");
	}
	
	/**
	 * 构造方法
	 * @param content 转码内容
	 * @param size 二维码大小
	 * @param format 二维码图片格式
	 */
	public QRCode(String content, int size, String format) {
		this(content, size, format, null, null);
	}
	/**
	 * 构造方法
	 * @param content 转码内容
	 * @param size 二维码大小
	 * @param onColor 二维码颜色(黑色部分)
	 * @param offColor 二维码颜色(白色部分)
	 */
	private QRCode(String content, int size, String format, Integer onColor, Integer offColor) {
		this.content = content;
		this.size = size;
		this.format = format;
		this.setOnColor(onColor);
		this.setOffColor(offColor);
		
		prototype = new Img(null);
		rebuildPrototype();
	}
	
	private String getImageUrl() {
		try {
			String imageUrl = url + "?content=" + URLEncoder.encode(content, "UTF-8") + "&size=" + size;
			if (Utils.notEmpty(format)) {
				imageUrl += "&format=" + format;
			}
			if (onColor != null) {
				imageUrl += "&onColor=" + onColor;
			}
			if (offColor != null) {
				imageUrl += "&offColor=" + offColor;
			}
			return imageUrl;
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("The content can not be encoded.");
		}
	}

	@Override
	public QRCode setWidth(String width) {
		throw new UnsupportedOperationException("Replace with setSize");
	}

	@Override
	public QRCode setWidth(int width) {
		throw new UnsupportedOperationException("Replace with setSize");
	}

	@Override
	public QRCode setHeight(String height) {
		throw new UnsupportedOperationException("Replace with setSize");
	}

	@Override
	public QRCode setHeight(int height) {
		throw new UnsupportedOperationException("Replace with setSize");
	}
	
}
