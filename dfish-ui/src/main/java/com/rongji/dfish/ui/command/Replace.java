package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.UiNode;
import com.rongji.dfish.ui.SingleContainer;
import com.rongji.dfish.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * 替换命令。替换某个 widget。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class Replace extends NodeControlCommand<Replace>
        implements SingleContainer<Replace,UiNode> {

    private static final long serialVersionUID = -5997424469287615043L;

    /**
     * 构造函数
     *
     * @param node 需要替换的内容
     */
    public Replace(UiNode<?> node) {
        setNode(node);
    }
    public Replace(UiNode<?> node, String target) {
        setNode(node);
        setTarget(target);
    }

    private UiNode<?> node;

    @Override
    public UiNode<?> getNode() {
        return node;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UiNode<?>> findNodes() {
        List<UiNode<?>> result = new ArrayList<>(1);
        result.add(node);
        return result;
    }

    @Override
    public Replace setNode(UiNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node == null");
        }
        this.node = node;
        if (target == null && node.getId() != null) {
            target = node.getId();
        }
        if (node instanceof Command<?>) {
            super.setSection(SECTION_COMMAND);
        }
        return this;
    }


    @Override
    public UiNode<?> findNodeById(String id) {
        return null;
    }

    @Override
    public Replace removeNodeById(String id) {
        return null;
    }

    @Override
    public boolean replaceNodeById(Widget<?> w) {
        return false;
    }

    @Override
    public void clearNodes() {

    }
}
