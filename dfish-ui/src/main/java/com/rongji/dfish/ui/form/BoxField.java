package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractNode;

/**
 * BoxField 当box(一般包含Checkbox和Radio)中数值需要动态绑定的时候设置该field属性，来判定如何绑定。
 *
 * @author DFish Team
 * @version 3.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 1.0
 */
@Deprecated
public class BoxField extends AbstractNode<BoxField> {

    private static final long serialVersionUID = 4188450997038991450L;

    private String value;
    private String checked;

	@Override
	public String getType() {
		return null;
	}

    /**
     * 值绑定字段
     *
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * 绑定字段
     *
     * @param value String
     * @return this
     */
    public BoxField setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 选中状态绑定字段
     *
     * @return String
     */
    public String getChecked() {
        return checked;
    }

    /**
     * 选中状态绑定字段
     *
     * @param checked String
     * @return this
     */
    public BoxField setChecked(String checked) {
        this.checked = checked;
        return this;
    }

}
