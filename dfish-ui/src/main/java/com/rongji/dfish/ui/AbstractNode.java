package com.rongji.dfish.ui;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.json.JsonFormat;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * AbstractNode 为抽象Node类，为方便widget/dialog/command构建而创立
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class AbstractNode<T extends AbstractNode<T>> implements Node<T> {

    private static final long serialVersionUID = 3228228457257982847L;
    protected String id;
    protected String template;
    protected Map<String, Object> data;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getTemplate() {
        return template;
    }

    public T setTemplate(String template) {
        this.template = template;
        return (T) this;
    }

    @Override
    public Object getData(String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.get(key);
    }

    @Override
    public Object removeData(String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.remove(key);
    }

    @Override
    public T putData(String key, Object value) {
        if(value==null){
            return (T) this;
        }
        if (data == null) {
            data = new LinkedHashMap<>();
        }
        data.put(key, value);
        return (T) this;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    protected String toString(Object value) {
        return value == null ? null : value.toString();
    }

    protected Number toNumber(Object value) {
        if (value == null || "".equals(value)) {
            return null;
        }
        if (value instanceof Number) {
            return (Number) value;
        }
        String str = value.toString();
        if (str.indexOf('.') > 0) {
            return new Double(str);
        } else {
            return new Integer(str);
        }
    }

    /**
     * 默认构造函数，尝试去读取默认值，默认值放置在com.rongji.dfish.ui.default_prop.properties中
     */
    public AbstractNode() {
        super();
        // 如果是封装类,绑定属性需要原型类构成后方可调用此方法
        if (!(this instanceof JsonWrapper)) {
            // FIXME 目前还不清楚这种做法是否合理,所有JsonWrapper必须等原型赋值后再进行调用该方法
            bundleProperties();
        }
    }

    static {
        String version = "unspecified";
        try {
            Class<?> clz = AbstractNode.class;
            URL url = clz.getClassLoader().getResource(clz.getName().replace(".", "/") + ".class");
            if (url != null) {
                Matcher m = Pattern.compile("dfish-ui-\\S+.jar").matcher(url.toString());
                if (m.find()) {
                    String jarName = m.group();
                    version = jarName.substring(9, jarName.length() - 4);
                }
            }
        } catch (Throwable e) {
            LogUtil.error(AbstractNode.class, "", e);
        }
        LogUtil.info("dfish-ui version : " + version);
    }

    /**
     * 绑定默认属性
     *
     * @author DFish Team - lamontYu
     */
    protected void bundleProperties() {
        ObjectTemplate.get(getClass()).bundleProperties(this);
    }

    @Override
    public String toString() {
        Object o = this;
        while (o instanceof JsonWrapper<?>) {
            Object prototype = ((JsonWrapper<?>) o).getPrototype();
            if (prototype == o) {
                break;
            }
            o = prototype;
        }
        return JsonFormat.toJson(o);
    }

    /**
     * 转化成带换行和缩进的JSON 格式
     *
     * @return String
     */
    public String formatString() {
        return JsonFormat.formatJson(this.toString());
    }

}
