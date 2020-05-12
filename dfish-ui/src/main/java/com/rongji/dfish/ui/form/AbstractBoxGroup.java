package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.AbstractPubNodeContainer;
import com.rongji.dfish.ui.Directional;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.Option;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 单选框或多选框表单组
 * @param <T> 当前对象类型
 * @param <N> 子节点对象类型
 * @author DFish Team
 *
 * @since DFish1.0
 */
public abstract class AbstractBoxGroup<T extends AbstractBoxGroup<T, N>, N extends AbstractBox<N>>
        extends AbstractPubNodeContainer<T, N,N> implements FormElement<T, String>, LabelRow<T>, Directional<T> {

    private static final long serialVersionUID = 3733166777271763891L;

    protected String dir;
    protected String name;
    protected String value;
    protected Object label;
    protected Boolean noLabel;

    //    protected Integer space;
//    protected List<Widget<?>> targets = new ArrayList<>();
//    protected List<N> nodes = new ArrayList<>();
    //    protected Boolean escape;
//    private AbstractMultiNodeContainer<?> bridgedObject = new AbstractMultiNodeContainer(null) {
//        @Override
//        public List<N> findNodes() {
//            return AbstractBoxGroup.this.nodes;
//        }
//    };

    /**
     * 构造函数
     *
     * @param name         表单名
     * @param label        标题
     * @param checkedValue 已经选中的值
     * @param options      候选项
     */
    public AbstractBoxGroup(String name, String label, Object checkedValue, List<?> options) {
        super(null);
        setName(name);
        setLabel(label);
        setValue(checkedValue);
        setOptions(options);
    }
    /**
     * 构造函数
     *
     * @param name         表单名
     * @param label        标题
     * @param checkedValue 已经选中的值
     * @param options      候选项
     */
    public AbstractBoxGroup(String name, Label label, Object checkedValue, List<?> options) {
        super(null);
        setName(name);
        setLabel(label);
        setValue(checkedValue);
        setOptions(options);
    }

    public T setOptions(List<?> options) {
        nodes = parseNodes(options);
        if(nodes==null){
            nodes=new ArrayList<>();
        }
        return (T) this;
    }

    protected List<Node> parseNodes(List<?> options) {
        List<Node> realNodes = null;
        if (options != null) {
            realNodes = new ArrayList<>( options.size());
            for (Object item : options) {
                if (item == null) {
                    continue;
                }
                N node;
                if (item instanceof AbstractBox<?>) {
                    node = (N) item;
                } else if (item instanceof Option) {
                    node = newPub();
                    Option option = (Option) item;
                    node.setValue(option.getValue());
                    node.setText(Utils.isEmpty(option.getText()) ? String.valueOf(option.getValue()) : option.getText());
                    node.setName(getName());

                    if (option.getChecked() != null) {
                        node.setChecked(option.getChecked());
                    } else if (value != null) {
                        boolean checked = value.equals(option.getValue());
                        if (checked) {
                            node.setChecked(true);
                        }
                    }
                } else {
                    String text = null;
                    Object value;
                    if (item instanceof Object[] || item instanceof String[]) {
                        Object[] castItem = (Object[]) item;
                        value = castItem[0];
                        if (castItem.length > 1) {
                            text = String.valueOf(castItem[1]);
                        }
                    } else if (item instanceof String || item instanceof Number || item instanceof java.util.Date) {
                        value = item;
                    } else {
                        LogUtil.error(getClass(), "invalid option [" + item + "], should be AbstractBox", null);
                        break;
                    }
                    node = newPub();
                    node.setValue(value);
                    node.setName(getName());
                    node.setText(text);
                }
                realNodes.add(node);
            }
        }
        return realNodes;
    }

    @Override
    public String getDir() {
        return dir;
    }

    @Override
    public T setDir(String dir) {
        this.dir = dir;
        return (T) this;
    }

    @Override
    @Transient
    public String getName() {
        return name;
    }

    @Override
    public T setName(String name) {
        this.name = name;
        return (T) this;
    }

    @Override
    public String getValue() {
        return value;
    }

    public T setValue(String value) {
        this.value = value;
        return (T) this;
    }

    @Override
    public T setValue(Object value) {
        if (value != null) {
            if (value instanceof String) {
                this.value = (String) value;
            } else if (value.getClass().isArray()) {
                StringBuilder valueStr = new StringBuilder();
                boolean isFirst = true;
                for (Object item : (Object[]) value) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        valueStr.append(',');
                    }
                    valueStr.append(item);
                }
                this.value = valueStr.toString();
            } else {
                this.value = String.valueOf(value);
            }
        } else {
            this.value = null;
        }
        return (T) this;
    }


    @Override
    public Object getLabel() {
        return label;
    }

    @Override
    public T setLabel(Label label) {
        this.label = label;
        return (T) this;
    }
    @Override
    public T setLabel(String label) {
        this.label = label;
        return (T) this;
    }

    @Override
    public Boolean getNoLabel() {
        return noLabel;
    }

    @Override
    public T setNoLabel(Boolean noLabel) {
        this.noLabel = noLabel;
        return (T) this;
    }

    //    /**
//     * 添加子面板
//     *
//     * @param w N
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public T add(N w) {
//        return add(-1, w);
//    }
//
//    /**
//     * 在指定的位置添加子面板
//     *
//     * @param index 位置
//     * @param w     N
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public T add(int index, N w) {
//        if (w == null) {
//            return (T) this;
//        }
//        if (index < 0) {
//            nodes.add(w);
//        } else {
//            nodes.add(index, w);
//        }
//        return (T) this;
//    }
//
//    /**
//     * 增加选项元素
//     *
//     * @param w 元素option对象
//     * @return add(w)s
//     */
//    public T addOption(N w) {
//        return add(w);
//    }
//
//    public T add(Option option, Widget<?> target) {
//        option = option == null ? new Option(null, null) : option;
//        N w = buildOption(option);
//        while (targets.size() < nodes.size()) {
//            targets.add(EMPTY_TARGET);
//        }
//        nodes.add(w);
//        if (target == null) {
//            target = EMPTY_TARGET;
//        }
//        targets.add(target);
//        return (T) this;
//    }
//
//    /**
//     * 增加选项，同时带一个target
//     *
//     * @param value  值
//     * @param text   文本
//     * @param target 附带的元素
//     * @return
//     */
//    public T add(Object value, String text, Widget<?> target) {
//        Option option = new Option(value, text);
//        return add(option, target);
//    }
//
//
//    @Override
//    public List<Widget<?>> findNodes() {
//        List<Widget<?>> result = new ArrayList<Widget<?>>();
//        if (nodes != null) {
//            result.addAll(nodes);
//        }
//        if (targets != null) {
//            result.addAll(targets);
//        }
//        return result;
//    }
//
//
//    @Override
//    public T removeNodeById(String id) {
//        List<Widget<?>> nodes = findNodes();
//        if (id == null || nodes == null) {
//            return (T) this;
//        }
//        for (int i = 0; i < nodes.size(); i++) {
//            Widget<?> item = nodes.get(i);
//            if (id.equals(item.getId())) {
//                nodes.remove(i);
//                if (targets.size() > i + 1) {
//                    targets.remove(i);
//                }
//                i--;
//            } else if (targets.size() > i && id.equals(targets.get(i).getId())) {
//                nodes.remove(i);
//                targets.remove(i);
//                i--;
//            } else if (item instanceof Layout) {
//                Layout<?> cast = (Layout<?>) item;
//                cast.removeNodeById(id);
//            } else if (targets.size() > i && targets.get(i) instanceof Layout) {
//                Layout<?> cast = (Layout<?>) targets.get(i);
//                cast.removeNodeById(id);
//            }
//        }
//
//        return (T) this;
//    }
//
//    @Override
//    public boolean replaceNodeById(Node w) {
//        List<Widget<?>> nodes = findNodes();
//        if (w == null || w.getId() == null || nodes == null) {
//            return false;
//        }
//        String id = w.getId();
//        for (int i = 0; i < nodes.size(); i++) {
//            Widget<?> item = nodes.get(i);
//            Widget<?> t = targets.size() > i ? targets.get(i) : null;
//            if (id.equals(item.getId())) {
//                // 替换该元素
//                if (onReplace(item, (Widget) w)) {
//                    nodes.set(i, (Widget) w);
//                    return true;
//                } else {
//                    return false;
//                }
//            } else if (t != null && id.equals(t.getId())) {
//                // 替换该元素
//                if (onReplace(t, (Widget) w)) {
//                    targets.set(i, (Widget) w);
//                    return true;
//                } else {
//                    return false;
//                }
//            } else if (t != null && t instanceof Layout) {
//                Layout<?> cast = (Layout<?>) t;
//                boolean replaced = cast.replaceNodeById(w);
//                if (replaced) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    protected boolean onReplace(Widget<?> oldItem, Widget<?> newItem) {
//        return true;
//    }
//
//    @Override
//    public void clearNodes() {
//        nodes.clear();
//        targets.clear();
//    }
//
//    protected abstract N newPub();
//
//    public N pub() {
//        if (pub == null) {
//            pub = newPub();
//        }
//        return pub;
//    }
//
//    /**
//     * 公共参数
//     *
//     * @return N pub
//     */
//    public N getPub() {
//        return pub;
//    }
//
//    /**
//     * boxgroup的默认参数
//     *
//     * @param pub 默认元素对象
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public T setPub(N pub) {
//        this.pub = pub;
//        return (T) this;
//    }
//
//    /**
//     * 当设置了 targets，再设置 space 可调整行间距。
//     *
//     * @return space
//     */
//    public Integer getSpace() {
//        return space;
//    }
//
//    /**
//     * 当设置了 targets，再设置 space 可调整行间距。
//     *
//     * @param space 间距
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public T setSpace(Integer space) {
//        this.space = space;
//        return (T) this;
//    }
//
//    /**
//     * 和boxgroup 一一对应的节点数组。勾选复选框将改变 target 节点的 disabled 状态。
//     *
//     * @return targets
//     */
//    public List<Widget<?>> getTargets() {
//        return targets;
//    }
//
//    /**
//     * 和boxgroup 一一对应的节点数组。勾选复选框将改变 target 节点的 disabled 状态。
//     *
//     * @param w 对象
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public T addTarget(Widget<?> w) {
//        targets.add(w);
//        return (T) this;
//    }
//
//    @Override
//    public Boolean getEscape() {
//        return this.escape;
//    }
//
//    @Override
//    public T setEscape(Boolean escape) {
//        this.escape = escape;
//        return (T) this;
//    }

//    @Override
//    public Node<?> findNodeById(String id) {
//        return bridgedObject.findNodeById(id);
//    }


//    public List<FormElement<?, ?>> findFormElementsByName(String name) {
//        return bridgedObject.findFormElementsByName(name);
//    }

}
