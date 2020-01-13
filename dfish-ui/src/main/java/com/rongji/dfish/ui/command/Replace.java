package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.HasId;
import com.rongji.dfish.ui.SingleContainer;

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
public class Replace extends NodeControlCommand<Replace> implements SingleContainer<Replace, HasId<?>> {

    private static final long serialVersionUID = -5997424469287615043L;

    /**
     * 默认构造函数
     */
    public Replace() {
    }

    /**
     * 构造函数
     *
     * @param node 需要替换的内容
     */
    public Replace(HasId<?> node) {
        setNode(node);
    }

    private HasId<?> node;

    @Override
    public String getType() {
        return "Replace";
    }

    @Override
    public HasId<?> getNode() {
        return node;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HasId<?>> findNodes() {
        ArrayList<HasId<?>> result = new ArrayList<>();
        result.add(node);
        return result;
    }

    @Override
    public Replace setNode(HasId<?> node) {
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


}
