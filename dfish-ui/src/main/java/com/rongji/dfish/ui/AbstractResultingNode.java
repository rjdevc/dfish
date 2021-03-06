package com.rongji.dfish.ui;

import java.util.Map;

/**
 * 前后端需要交互且结果解析的组件
 * @author lamontYu
 * @since DFish5.0
 */
public abstract class AbstractResultingNode<T extends AbstractResultingNode<T>> extends AbstractNode<T> {

    protected String src;
    protected Map<String, Object> result;

    /**
     * 请求地址
     *
     * @return String
     */
    public String getSrc() {
        return src;
    }

    /**
     * 请求地址
     *
     * @param src String
     * @return this
     */
    public T setSrc(String src) {
        this.src = src;
        return (T) this;
    }

    /**
     * 交互结果展示(不推荐修改,详细参数属性请参考JS API)
     *
     * @return Map&lt;String, Object&gt;
     */
    public Map<String, Object> getResult() {
        return result;
    }

    /**
     * 交互结果展示(不推荐修改,详细参数属性请参考JS API)
     *
     * @param result Map&lt;String, Object&gt;
     * @return this
     */
    public T setResult(Map<String, Object> result) {
        this.result = result;
        return (T) this;
    }

}
