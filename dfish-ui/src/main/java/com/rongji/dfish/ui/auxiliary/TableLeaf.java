package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.LazyLoad;
import com.rongji.dfish.ui.layout.Table;

/**
 * TableTreeItem 是可折叠表格中的折叠项
 * <p>在TableRow中添加了下级可折叠的行的时候，TableTreeItem作为一个视觉标志出现在当前行({@link TR})上。
 * 它前方有一个+号(或-号)点击有展开或折叠效果。</p>
 * <p>多级别的TableTreeItem自动产生缩进效果</p>
 *
 * @author DFish Team
 * @see TR
 */
public class TableLeaf extends AbstractLeaf<TableLeaf> {

    private static final long serialVersionUID = -7465823398383091843L;

    /**
     * 构造函数
     * @param text
     */
    public TableLeaf(String text) {
        super(text);
    }

}
