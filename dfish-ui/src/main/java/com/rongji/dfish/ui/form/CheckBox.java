package com.rongji.dfish.ui.form;

/**
 * 复选框 这个是老牌的HTML元素
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 1.0
 */
public class CheckBox extends AbstractBox<CheckBox> {

    private static final long serialVersionUID = 7716080336465806097L;

    /**
     * 构造函数
     *
     * @param name    表单名
     * @param label   标题
     * @param value   如果选中的提交的值
     * @param text    显示的内容标签
     */
    public CheckBox(String name, String label, Object value, String text) {
        super(name, label, value, text);
    }

}
