package com.rongji.dfish.ui.layout.grid;

import com.rongji.dfish.ui.form.AbstractBox;

import java.beans.Transient;

/**
 * Grid专用的复选框
 *
 * @author YuLM - DFish Team
 */
public class GridTriplebox extends AbstractBox<GridTriplebox> {

    /**
     *
     */
    private static final long serialVersionUID = -4770736316914887083L;

    private Integer checkstate;

    private Boolean partialsubmit;

    private Boolean checkall;

    /**
     * 构造函数
     *
     * @param name       名称
     * @param label      标题
     * @param checkstate 状态
     * @param value      值，这里的值只有3中状态 选中,未选中,部分选中
     * @param text       显示文本
     */
    public GridTriplebox(String name, String label, Integer checkstate, Object value, String text) {
        super(name, label, null, value, text);
        this.checkstate = checkstate;
    }

    /**
     * 状态
     *
     * @return Integer
     */
    public Integer getCheckstate() {
        return checkstate;
    }

    /**
     * 状态
     *
     * @param checkstate Integer
     * @return this
     */
    public GridTriplebox setCheckstate(Integer checkstate) {
        this.checkstate = checkstate;
        return this;
    }

    @Transient
    @Override
    public Boolean getChecked() {
        return super.getChecked();
    }

    @Override
    public GridTriplebox setChecked(Boolean checked) {
        if (checked != null) {
            this.checkstate = checked ? CHECKSTATE_CHECKED : CHECKSTATE_UNCHECKED;
        }
        return this;
    }

    /**
     * 半选状态是否提交数据
     *
     * @return Boolean
     */
    public Boolean getPartialsubmit() {
        return partialsubmit;
    }

    /**
     * 半选状态是否提交数据
     *
     * @param partialsubmit Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public GridTriplebox setPartialsubmit(Boolean partialsubmit) {
        this.partialsubmit = partialsubmit;
        return this;
    }

    /**
     * 未选中状态
     */
    public static final int CHECKSTATE_UNCHECKED = 0;
    /**
     * 选中状态
     */
    public static final int CHECKSTATE_CHECKED = 1;
    /**
     * 部分选中状态
     */
    public static final int CHECKSTATE_PARTIALCHECKED = 2;

    public String getType() {
        return "grid/triplebox";
    }
//	@Override
//	public GridTriplebox setValue(Object value) {
//		this.value=value;
//		return this;
//	}

    /**
     * 选中所有
     *
     * @return Boolean
     */
    public Boolean getCheckall() {
        return checkall;
    }

    /**
     * 选中所有
     *
     * @param checkall Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public GridTriplebox setCheckall(Boolean checkall) {
        this.checkall = checkall;
        return this;
    }

}
