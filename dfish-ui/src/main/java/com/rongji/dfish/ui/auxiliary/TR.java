package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.layout.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tr 表示 表格的行
 * <p>表格的行有三种工作模式</p>
 * <p>常见的是里面包行单元格(Td)。
 * 每个单元格是一个文本或独立的widget，有widget的功能和属性，只是有的时候可能并不会给每个单元格设置ID。</p>
 * <p>为了能让表格的json尽可能小。允许data类型为 文本 widget 或TableCell。
 * 并用{@link Column#getField()} 来说明这个内容显示在哪里。</p>
 * <p>当一行里面包含可折叠的子集内容的时候，它将包含rows属性。rows里面是一个有子集TableRow构成的List。
 * 而会有一个TableTreeItem字段用于做折叠操作的视觉效果</p>
 *
 * @author DFish Team
 * @see AbstractTD {@link Column} {@link Leaf}
 * @since DFish 3.0
 */
public class TR extends AbstractTR<TR> implements JsonWrapper<Object> {
    private static final long serialVersionUID = -1895404892414786019L;

    /**
     * 默认构造函数
     */
    public TR() {
        super();
    }

    /**
     * 拷贝构造函数，相当于clone
     *
     * @param tr another tr
     */
    public TR(AbstractTR<?> tr) {
        super();
        copyProperties(this, tr);
    }


    @Override
    public Object getPrototype() {
        if (hasTrProp(this)) {
            JsonTR p = new JsonTR();
            copyProperties(p, this);
            return p;
        } else {
            return this.getData();
        }
    }

    private static boolean hasTrProp(AbstractTR<?> tr) {
        if (tr == null) {
            return false;
        }
        return tr.getId() != null || tr.getFocus() != null || tr.getFocusable() != null ||
                tr.getHeight() != null || tr.getSrc() != null ||
                (tr.getData() != null && tr.getData().size() > 0) ||
                tr.getCls() != null || tr.getStyle() != null ||//常用的属性排在前面
                tr.getBeforeContent() != null || tr.getPrependContent() != null ||
                tr.getAppendContent() != null || tr.getAfterContent() != null ||
                tr.getGid() != null || tr.getHeightMinus() != null ||
                tr.getMaxHeight() != null || tr.getMaxWidth() != null ||
                tr.getMinHeight() != null || tr.getMinWidth() != null ||
                (tr.getOn() != null && tr.getOn().size() > 0) ||
                tr.getWidth() != null || tr.getWidthMinus() != null;
    }


}

/**
 * 和javascript端是对应的TR模型。
 * json中如果tr没有cls等额外属性，可能会简化显示它的data部分
 * 所以Tr默认不能显示按封装类格式。这时候json中的原型将有可能还是这个JsonTR的格式
 * 也有可能是Map格式。
 *
 * @author DFish team
 */
class JsonTR extends AbstractTR<JsonTR> {
    private static final long serialVersionUID = -1034767067781605568L;

}

/**
 * Table.Tr 表示 表格的行
 * <p>表格的行有三种工作模式</p>
 * <p>常见的是里面包行单元格(Td)。
 * 每个单元格是一个文本或独立的widget，有widget的功能和属性，只是有的时候可能并不会给每个单元格设置ID。</p>
 * <p>为了能让表格的json尽可能小。允许data类型为 文本 widget 或TableCell。
 * 并用{@link Table.Column#getField} 来说明这个内容显示在哪里。</p>
 * <p>当一行里面包含可折叠的子集内容的时候，它将包含rows属性。rows里面是一个有子集TableRow构成的List。
 * 而会有一个TableTreeItem字段用于做折叠操作的视觉效果</p>
 *
 * @param <T> 当前类型
 * @author DFish Team
 * @see AbstractTD {@link Table.Column} {@link Leaf}
 * @since DFish 3.0
 */
abstract class AbstractTR<T extends AbstractTR<T>> extends AbstractWidget<T> implements MultiNodeContainer<T, TR> {

    private static final long serialVersionUID = 4300223953187136245L;
    protected List<TR> nodes;
    @Override
    public List<TR>getNodes(){
        return nodes;
    }
    @Override
    public T setNodes(List<TR> nodes){
        this.nodes=nodes;
        return (T)this;
    }


    /**
     * 默认构造函数
     */
    public AbstractTR() {}

    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            Map<Integer,String> posMap;
            @Override
            protected  List<Node> nodes() {
                List<Node> result=new ArrayList();
                posMap=new HashMap<>();
                if(data !=null) {
                    int index = 0;
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        if (entry.getValue() instanceof Node) {
                            result.add((Node) entry.getValue());
                            posMap.put(index++, entry.getKey());
                        }
                    }
                }
                if(nodes!=null){
                    result.addAll(nodes);
                }
                return result;
            }

            @Override
            protected void setNode(int i, Node node) {
                if(posMap==null){
                    return; //本不该发生
                }else {
                    if(i < posMap.size()){
                        //在data中寻找
                        if (node == null) {
                            data.remove(posMap.get(i));
                        } else {
                            data.put(posMap.get(i), node);
                        }
                    }else {
                        //在nodes中寻找
                        if (node == null) {
                            nodes.remove(i-posMap.size());
                        }else{
                            nodes.set(i-posMap.size(), (TR)node);
                        }
                    }
                }
            }
        };
    }

    @Override
    public Node findNode(Filter filter) {
        return getNodeContainerDecorator().findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        return getNodeContainerDecorator().findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceNode(filter,node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceAllNodes(filter,node);
    }


    protected Boolean focus;
    protected Boolean focusable;
    protected String src;

    @Override
    public String getType() {
        return null;
    }

    /**
     * 当前行是不是聚焦状态
     *
     * @return Boolean
     */
    public Boolean getFocus() {
        return focus;
    }

    /**
     * 当前行是不是聚焦状态
     *
     * @param focus Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFocus(Boolean focus) {
        this.focus = focus;
        return (T) this;
    }

    /**
     * 是否可聚焦
     *
     * @return Boolean
     */
    public Boolean getFocusable() {
        return focusable;
    }

    /**
     * 设置是否可聚焦
     *
     * @param focusable Boolean
     * @return this
     */
    public T setFocusable(Boolean focusable) {
        this.focusable = focusable;
        return (T) this;
    }


    /**
     * 排序src
     *
     * @return String
     */
    public String getSrc() {
        return src;
    }

    /**
     * 排序src
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setSrc(String src) {
        this.src = src;
        return (T) this;
    }

    /**
     * 拷贝属性
     *
     * @param to   AbstractTr
     * @param from AbstractTr
     */
    protected void copyProperties(AbstractTR<?> to, AbstractTR<?> from) {
        super.copyProperties(to, from);
        //data
        to.focus = from.focus;
        to.src = from.src;
        to.focusable = from.focusable;
    }


}
