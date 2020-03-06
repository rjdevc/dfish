package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.Option;

import java.util.*;

/**
 * AbstractOptionsHolder 抽象的含有options的部件。
 * dfish3中 不建议使用Select 并且RadioGroup和CheckboxGroup重新实现了以后，实际上该类也只提供给XBox使用。
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @date 2018-08-03 before
 * @since 1.0
 */
public abstract class AbstractOptionContainer<T extends AbstractOptionContainer<T>> extends AbstractFormElement<T, Object> implements HtmlContentHolder<T> {

    private static final long serialVersionUID = -1184424744014200791L;

    /**
     * @param name    表单名
     * @param label   标签
     * @param value   选中的值
     * @param options 候选项
     */
    public AbstractOptionContainer(String name, String label, Object value, List<?> options) {
        this.setName(name);
        this.setLabel(label);
        this.doSetValue(value);
        this.setOptions(options);
    }
    /**
     * @param name    表单名
     * @param label   标签
     * @param value   选中的值
     * @param options 候选项
     */
    public AbstractOptionContainer(String name, Label label, Object value, List<?> options) {
        this.setName(name);
        this.setLabel(label);
        this.doSetValue(value);
        this.setOptions(options);
    }

    protected Boolean escape;
    protected List<Option> nodes;

    /**
     * 设置候选项
     *
     * @param options List
     * @return 本身，这样可以继续设置其他属性
     */
    public T setOptions(List<?> options) {
        List<Option> realOptions = parseOptions(options);
        this.nodes = realOptions;
        return (T) this;
    }

    protected List<Option> parseOptions(List<?> options) {
        if (options != null) {
            List<Option> result = new ArrayList<>(options == null ? 0 : options.size());
            for (Object item : options) {
                if (item == null) {
                    continue;
                }
                Option node;
                if (item instanceof Option) {
                    node = (Option) item;
                } else {
                    String text = null;
                    Object value;
                    String ic = null;
                    if (item instanceof Object[] || item instanceof String[]) {
                        Object[] castItem = (Object[]) item;
                        value = castItem[0];
                        if (castItem.length > 2) {
                            ic = String.valueOf(castItem[2]);
                        }
                        if (castItem.length > 1) {
                            text = String.valueOf(castItem[1]);
                        } else {
                            text = String.valueOf(castItem[0]);
                        }
                    } else if (item instanceof String || item instanceof Number ||
                            item instanceof java.util.Date) {
                        value = item;
                        text = String.valueOf(item);
                    } else {
                        LogUtil.error(getClass(), "invalid option [" + item + "], should be Option", null);
                        break;
                    }
                    node = new Option(value, text);
                    node.setIcon(ic);
                }
                if (node != null) {
                    result.add(node);
                }
            }
            return result;
        }
        return null;
    }


    /**
     * 设置 已选中的值(单项)
     *
     * @param obj Object[]
     */
    @Override
    public T setValue(Object obj) {
        doSetValue(obj);
        return (T) this;
    }

    protected void doSetValue(Object obj) {
        if (obj instanceof Object[] || obj instanceof String[]) {
            this.value = obj;
        } else if (obj instanceof Collection<?>) {
            Collection<?> cast = (Collection<?>) obj;
            this.value = cast.toArray();
        } else {
            this.value = obj;
        }
    }

    public List<Option> getNodes() {
        return nodes;
    }

    public T setNodes(List<Option> nodes) {
        this.nodes = nodes;
        return (T) this;
    }

    @Override
    public Boolean getEscape() {
        return this.escape;
    }

    @Override
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }
}
