package com.rongji.dfish.framework.plugin.qrcode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.misc.qrcode.MatrixToImageConfig;
import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.widget.Img;

public class QRCode extends AbstractWidgetWrapper<QRCode, Img>{

	private static final long serialVersionUID = 4326337757852569803L;

	private String url = "qrCode/image";
	private String content;
	private int size;
	private String format;
	private MatrixToImageConfig config;

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

	public MatrixToImageConfig getConfig() {
		return config;
	}

	public QRCode setConfig(MatrixToImageConfig config) {
		this.config = config;
		return rebuildPrototype();
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
		this(content, size, format, null);
	}
	/**
	 * 构造方法
	 * @param content 转码内容
	 * @param size 二维码大小
	 * @param config 二维码配置
	 */
	private QRCode(String content, int size, String format, MatrixToImageConfig config) {
		// FIXME 这个方法预留后续可能进行调整二维码颜色,所以暂时先是私有方法
		this.content = content;
		this.size = size;
		this.format = format;
		this.config = config;
		
		prototype = new Img(null);
		rebuildPrototype();
	}
	
	private String getImageUrl() {
		String imageUrl = null;
		try {
			imageUrl = url + "?content=" + URLEncoder.encode(content, "UTF-8") + "&size=" + size;
			if (Utils.notEmpty(format)) {
				imageUrl += "&format=" + format;
			}
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("The content can not be encoded.");
		}
		return imageUrl;
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
