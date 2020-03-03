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