package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Option;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.HtmlContentHolder;

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
        this.doSetOptions(options);
        fixChecked();
    }

    protected Boolean escape;
    protected List<Option> options;

    /**
     * 设置候选项
     *
     * @param options List
     * @return 本身，这样可以继续设置其他属性
     */
    public T setOptions(List<?> options) {
        doSetOptions(options);
        fixChecked();
        return (T) this;
    }

    protected void doSetOptions(List<?> options) {
        List<Option> result = new ArrayList<>(options == null ? 0 : options.size());
//        Set<Object> theValue = null;
//        if (value == null) {
//            theValue = new HashSet<>(0);
//        } else if (value.getClass().isArray()) {
//            Object[] cast = (Object[]) value;
//            theValue = new HashSet<>(Arrays.asList(cast));
//        } else if (value instanceof Collection) {
//            Collection<?> cast = (Collection<?>) value;
//            theValue = new HashSet<>(cast);
//        } else {
//            theValue = new HashSet<>(0);
//            theValue.add(value);
//        }
        if (options != null) {
            for (Object item : options) {
                if (item == null) {
                    continue;
                }
                Option node = null;
                if (item instanceof Option) {
                    node = (Option) item;
//                } else if (item instanceof AbstractBox<?>) {
//                    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
//                    // 获取第一个类型参数的真实类型
//                    Class nodeClass = (Class<T>) pt.getActualTypeArguments()[1];
//                    if (nodeClass.isAssignableFrom(item.getClass())) {
//                        node = (N) item;
//                    }
                } else {

                    String label = null;
                    Object value = null;
                    String ic = null;
                    if (item instanceof Object[] || item instanceof String[]) {
                        Object[] castItem = (Object[]) item;
                        value = castItem[0];
                        if (castItem.length > 2) {
                            ic = String.valueOf(castItem[2]);
                        } else if (castItem.length > 1) {
                            label = String.valueOf(castItem[1]);
                        } else {
                            label = String.valueOf(castItem[0]);
                        }
                    } else if (item instanceof String || item instanceof Number ||
                            item instanceof java.util.Date) {
                        value = item;
                        label = String.valueOf(item);
                    } else {
                        LogUtil.error(getClass(), "invalid option [" + item + "], should be Option", null);
                        break;
                    }
                    node = new Option(value, label);
                    node.setIcon(ic);
//                    node.setChecked(theValue.contains(value) ? true : null);
                }
                if (node != null) {
                    result.add(node);
                }
            }
        }
        this.options = result;
    }

    protected Option buildOption(Object value, String text, Boolean checked) {
        return new Option(value, text).setChecked(checked);
    }


    protected void fixChecked() {
//        if (this.value == null) {
//            //如果value没有值，则不进行判断以option自己的checked为准
//            return;
//        }
//        Set<String> theValue = null;
//        if (value == null) {
//            theValue = new HashSet<>();
//        } else if (value instanceof int[]) {
//            int[] cast = (int[]) value;
//            theValue = new HashSet<>();
//            for (int o : cast) {
//                theValue.add(String.valueOf(o));
//            }
//        } else if (value instanceof char[]) {
//            char[] cast = (char[]) value;
//            theValue = new HashSet<>();
//            for (char o : cast) {
//                theValue.add(String.valueOf(o));
//            }
//        } else if (value instanceof long[]) {
//            long[] cast = (long[]) value;
//            theValue = new HashSet<>();
//            for (long o : cast) {
//                theValue.add(String.valueOf(o));
//            }
//        } else if (value.getClass().isArray()) {
//            Object[] cast = (Object[]) value;
//            theValue = new HashSet<>();
//            for (Object o : cast) {
//                theValue.add(o == null ? null : o.toString());
//            }
//        } else if (value instanceof Collection) {
//            Collection<?> cast = (Collection<?>) value;
//            theValue = new HashSet<>();
//            for (Object o : cast) {
//                theValue.add(o == null ? null : o.toString());
//            }
//        } else {
//            theValue = new HashSet<>();
//            theValue.add(value == null ? null : value.toString());
//        }
//        if (theValue.size() == 0) {
//            theValue.add(null);
//            theValue.add("");
//        }
////		nodes.clear();
//        if (options != null) {
//            for (Object item : options) {
//                if (item == null) {
//                    continue;
//                } else if (item instanceof Option) {
//                    Option o = (Option) item;
//                    boolean checked = theValue.contains(o.getValue() == null ? null : o.getValue().toString());
//                    o.setChecked(checked ? true : null);
//
//                } else if (item instanceof AbstractBox<?>) {
//                    AbstractBox<?> o = (AbstractBox<?>) item;
//                    boolean checked = theValue.contains(o.getValue() == null ? null : o.getValue().toString());
//                    o.setChecked(checked ? true : null);
//                }
//            }
//        }
    }

    /**
     * 设置 已选中的值(单项)
     *
     * @param obj Object[]
     */
    @Override
    public T setValue(Object obj) {
        doSetValue(obj);
        fixChecked();
        return (T) this;
    }

    protected void doSetValue(Object obj) {
        if (obj == null) {
            //do nothing
        } else if (obj instanceof Object[] || obj instanceof String[]) {
            this.value = obj;
        } else if (obj instanceof Collection<?>) {
            Collection<?> cast = (Collection<?>) obj;
            this.value = cast.toArray();
        } else {
            value = new Object[]{obj};
        }
    }

    /**
     * @return the options
     */
    public List<?> getOptions() {
        return options;
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
