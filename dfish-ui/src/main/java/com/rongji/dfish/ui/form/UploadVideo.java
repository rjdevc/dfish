package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Valignable;

/**
 * 上传视频。
 * @author DFish Team
 * @param <T> 类型
 *
 */
@SuppressWarnings("unchecked")
public class UploadVideo<T extends UploadVideo<T>> extends AbstractUpload<T> implements Alignable<T>,Valignable<T> {

	private static final long serialVersionUID = -798266151699771677L;
	private String align;
	private String valign;

	/**
	 * @param name
	 * @param label
	 */
	public UploadVideo(String name, String label) {
		this.name=name;
		setLabel(label);
	}

	@Override
	public String getType() {
		return "upload/video";
	}

	@Override
	public String getAlign() {
		return align;
	}

	@Override
    public T setAlign(String align) {
		this.align = align;
		return (T) this;
	}

	@Override
	public String getValign() {
		return valign;
	}

	@Override
    public T setValign(String valign) {
		this.valign = valign;
		return (T) this;
	}
}
