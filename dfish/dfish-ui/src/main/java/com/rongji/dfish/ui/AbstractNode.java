package com.rongji.dfish.ui;

import java.util.*;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.form.Hidden;


/**
 * AbstractNode 为抽象Node类，为方便widget/dialog/command构建而创立
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class AbstractNode<T extends AbstractNode<T>> extends AbstractJsonObject<T> implements HasId<T>,
        DataContainer<T> {

    /**
     *
     */
    private static final long serialVersionUID = 3228228457257982847L;
    protected String id;

    protected Map<String, Object> data;

    protected boolean prototypeLock;
    protected boolean rebuild = true;

    public String getId() {
        return id;
    }

    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public Object getData(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.get(key);
    }

    public Object removeData(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.remove(key);
    }

    public T setData(String key, Object value) {
        if (data == null) {
            data = new LinkedHashMap<String, Object>();
        }
        data.put(key, value);
        return (T) this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    protected <N extends Widget<?>> List<N> findNodes() {
        return null;
    }

    protected Widget<?> findNodeById(String id) {
        List<Widget<?>> nodes = findNodes();
        if (id == null || nodes == null) {
            return null;
        }
        for (Iterator<Widget<?>> iter = nodes.iterator(); iter.hasNext(); ) {
            Widget<?> item = iter.next();
            if (id.equals(item.getId())) {
                return item;
            } else if (item instanceof Layout) {
                Layout<?, ?> cast = (Layout<?, ?>) item;
                Widget<?> c = cast.findNodeById(id);
                if (c != null) {
                    return c;
                }
            }
        }
        return null;
    }

    protected T removeNodeById(String id) {
        List<Widget<?>> nodes = findNodes();
        if (id == null || nodes == null) {
            return (T) this;
        }
        for (Iterator<Widget<?>> iter = nodes.iterator();
             iter.hasNext(); ) {
            Widget<?> item = iter.next();
            if (id.equals(item.getId())) {
                iter.remove();
            } else if (item instanceof Layout) {
                Layout<?, Widget<?>> cast = (Layout<?, Widget<?>>) item;
                cast.removeNodeById(id);
            }
        }
        return (T) this;
    }

    protected boolean replaceNodeById(Widget<?> w) {
        List<Widget<?>> nodes = findNodes();
        if (w == null || w.getId() == null || nodes == null) {
            return false;
        }
        String id = w.getId();
        for (int i = 0; i < nodes.size(); i++) {
            Widget<?> item = nodes.get(i);
            if (id.equals(item.getId())) {
                // 替换该元素
                if (onReplace(item, w)) {
                    nodes.set(i, w);
                    return true;
                } else {
                    return false;
                }
            } else if (item instanceof Layout) {
                Layout<?, Widget<?>> cast = (Layout<?, Widget<?>>) item;
                boolean replaced = cast.replaceNodeById(w);
                if (replaced) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean onReplace(Widget<?> oldWidget, Widget<?> newWidget) {
        if (!Utils.isEmpty(oldWidget.getWidth()) &&
                Utils.isEmpty(newWidget.getWidth())) {
            newWidget.setWidth(oldWidget.getWidth());
        }
        if (!Utils.isEmpty(oldWidget.getHeight()) &&
                Utils.isEmpty(newWidget.getHeight())) {
            newWidget.setHeight(oldWidget.getHeight());
        }
        return true;
    }

    protected List<FormElement<?, ?>> findFormElementsByName(String name) {
        List<FormElement<?, ?>> result = new ArrayList<FormElement<?, ?>>();
        findFormElementsByName(name, result);
        return result;
    }

    protected void findFormElementsByName(String name, List<FormElement<?, ?>> result) {
        if (name == null || name.equals("")) {
            return;
        }
        if (this instanceof HiddenContainer<?>) {
            HiddenContainer<?> cast = (HiddenContainer<?>) this;
            if (cast.getHiddens() != null)
                for (Hidden hidden : cast.getHiddens()) {
                    if (name.equals(hidden.getName())) {
                        result.add(hidden);
                    }
                }
        }
        List<Widget<?>> nodes = findNodes();
        if (nodes != null)
            for (Widget<?> item : nodes) {
                if (item == null) {
                    continue;
                }
                if (item instanceof FormElement<?, ?>) {
                    FormElement<?, ?> cast = (FormElement<?, ?>) item;
                    if (name.equals(cast.getName())) {
                        result.add(cast);
                    }
                } else if (item instanceof Layout) {
                    if (item instanceof AbstractNode) {
                        ((AbstractNode<?>) item).findFormElementsByName(name, result);
                    } else {
                        result.addAll(((Layout<?, ?>) item).findFormElementsByName(name));
                    }
                }
            }
    }

    /**
     * 拷贝属性
     *
     * @param to   AbstractNode
     * @param from AbstractNode
     */
    protected void copyProperties(AbstractNode<?> to, AbstractNode<?> from) {
//		Utils.copyPropertiesExact(to, from);
//		to.appendcontent =from.appendcontent;
//		to.prependcontent =from.prependcontent;
//		to.cls=from.cls;
//		to.height=from.height;
//		to.hmin=from.hmin;
//		to.maxheight=from.maxheight;
//		to.maxwidth=from.maxwidth;
//		to.minheight=from.minheight;
//		to.minwidth=from.minwidth;
//		to.style=from.style;
//		to.width=from.width;
//		to.wmin=from.wmin;
        //浅拷贝
        to.id = from.id;
        to.data = from.data;
        to.ats(from.ats());
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

}
