package com.rongji.dfish.ui.layout;

import java.util.List;

import com.rongji.dfish.ui.AbstractNodeContainer;
import com.rongji.dfish.ui.MultiNodeContainer;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.BoxHolder;

/**
 * fieldset模式布局。
 *@author DFish Team
 *
 */
public class FieldSet extends AbstractNodeContainer<FieldSet> implements MultiNodeContainer<FieldSet>, BoxHolder<FieldSet> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6633135609318102753L;
	private String legend;
	private AbstractBox box;

	/**
	 * 标题文本。
	 * @return legend
	 */
	public String getLegend() {
		return legend;
	}
	/**
	 * 标题文本。
	 * @param legend 标题文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public FieldSet setLegend(String legend) {
		this.legend = legend;
		return this;
	}
	/**
	 * 构造 函数
	 * @param legend  标题文本
	 */
	public FieldSet(String legend) {
		this(null,legend);
	}
	/**
	 * 构造 函数
	 * @param id 编号
	 * @param legend 标题文本
	 */
	public FieldSet(String id, String legend) {
		super(id);
		this.legend=legend;
	}
	/**
	 * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
	 * @return box
	 */
	@Override
	public AbstractBox getBox() {
		return box;
	}
	/**
	 * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
	 * @param box 勾选项。可能是checkbox 或 radio
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
	public FieldSet setBox(AbstractBox box) {
		this.box = box;
		return this;
	}
	@Override
	public List<Node> getNodes() {
		return nodes;
	}
	
}