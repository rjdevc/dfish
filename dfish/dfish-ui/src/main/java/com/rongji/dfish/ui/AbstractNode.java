package com.rongji.dfish.ui;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.json.JsonWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public abstract class AbstractNode<T extends AbstractNode<T>> implements HasId<T>,
        DataContainer<T>,TemplateSupport<T> ,JsonObject{

    /**
     *
     */
    private static final long serialVersionUID = 3228228457257982847L;
    protected static final Log LOG=LogFactory.getLog(AbstractNode.class);
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

    protected void clearNodes() {
        List<Widget<?>> nodes = findNodes();
        if (nodes != null) {
            nodes.clear();
        }
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
            if (cast.getHiddens() != null) {
                for (Hidden hidden : cast.getHiddens()) {
                    if (name.equals(hidden.getName())) {
                        result.add(hidden);
                    }
                }
            }
        }
        List<Widget<?>> nodes = findNodes();
        if (nodes != null) {
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
    /**
     * 默认构造函数，尝试去读取默认值，默认值放置在com.rongji.dfish.ui.default_prop.properties中
     */
    public AbstractNode(){
        super();
        // 如果是封装类,绑定属性需要原型类构成后方可调用此方法
        if (!(this instanceof JsonWrapper)) {
            // FIXME 目前还不清楚这种做法是否合理,所有JsonWrapper必须等原型赋值后再进行调用该方法
            bundleProperties();
        }
    }
    static{
        String version="unspecified";
        try {
            Class<?> clz = AbstractNode.class;
            URL url=clz.getClassLoader().getResource(clz.getName().replace(".", "/") + ".class");
            if (url != null) {
                Matcher m=Pattern.compile("dfish-ui-\\S+.jar").matcher(url.toString());
                if(m.find()){
                    String jarName=m.group();
                    version=jarName.substring(9,jarName.length()-4);
                }
            }
        } catch (Throwable e) {
            LOG.error("", e);
        }
        LOG.info("dfish-ui version : "+version);
    }
    /**
     * 绑定默认属性
     *
     * @author DFish Team - YuLM
     */
    protected void bundleProperties() {
        ObjectTemplate.get(getClass()).bundleProperties(this);
    }

    private Map<String,String> atProps;
    @SuppressWarnings("unchecked")
    public T at(String prop,String expr){
        if(atProps==null){
            atProps=new LinkedHashMap<String,String>();
        }
        atProps.put(prop,expr);
        return(T)this;
    }
    public T setFor(String expr){
        return setFor(expr,null,null);
    }


    public T setFor(String dataExpr,String itemName,String indexName){
        itemName = Utils.isEmpty(itemName) ? "item" : itemName;
//		if(indexName==null||indexName.equals("")){
//			at("w-for","$"+itemName+" in ("+dataExpr+")");
//		}else{
//		}
        at("w-for","$" + itemName + (Utils.notEmpty(indexName) ? ",$" + indexName : "") + " in ("+dataExpr+")");
        return (T)this;
    }
    public Map<String,String> ats(){
        return atProps;
    }
    public void ats(Map<String,String> ats){
        this.atProps=ats;
//		return atProps;
    }

    public String asJson() {
        return toString();
    }

    @Override
    public String toString(){
        Object o=this;
        while(o instanceof JsonWrapper<?>){
            Object prototype=((JsonWrapper<?>)o).getPrototype();
            if(prototype==o){
                break;
            }
            o=prototype;
        }
        return J.toJson(o);
    }

    /**
     *  转化成带换行和缩进的JSON 格式
     * @return String
     */
    public String formatString() {
        return J.formatJson(this.toString());
    }

    /**
     * HTML编码字符
     * @param src String
     * @return String
     */
    public static String toHtml(String src) {
        StringBuilder sb = new StringBuilder();
        if (src != null) {
            char[] c = src.toCharArray();
            for (int i = 0; i < c.length; i++) {
                switch (c[i]) {
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    case '\"':
                        sb.append("&quot;");
                        break;
                    case '\'':
                        sb.append("&#39;");
                        break;
                    case '\r': { //把换行替换成<br/>
                        sb.append("<br/>");
                        if (i + 1 < c.length && c[i + 1] == '\n') {
                            i++; //如果紧跟着的那个是\n那么忽略掉.这样对macos linux window 都支持
                        }
                        break;
                    }
                    case ' ': {
                        if (i + 1 < c.length && c[i + 1] == ' ') {
                            sb.append("&nbsp;"); //根据HTML规范如果两个以上的空格,除了最后一个是空格外,其他要打&nbsp;
                        } else {
                            sb.append(' ');
                        }
                        break;
                    }
                    case '\n': //把换行替换成<br/>
                        sb.append("<br/>");
                        break;
                    default:
                        if (c[i] < 0||c[i] > 31) {
                            sb.append(c[i]);
                        }
                }
            }
        }
        return sb.toString();
    }
}
