package com.rongji.dfish.ui;

import java.util.*;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.form.Hidden;


/**
 * AbstractNode 为抽象Node类，为方便widget/dialog/command构建而创立
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractNode<T extends AbstractNode<T>> extends AbstractJsonObject implements HasId<T>,
	DataContainer<T>,EventTarget<T>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3228228457257982847L;
	protected String id;
	protected String gid;
	
	protected String width;
	protected String height;
	protected String maxwidth;
	protected String maxheight;
	protected String minwidth;
	protected String minheight;
	
	protected String cls;
	protected String style;
	protected Integer wmin;
	protected Integer hmin;
	protected String beforecontent;
	protected String aftercontent;

	protected Map<String, String> events;
	protected Map<String,Object> data;
	
	protected boolean prototypeLock;
	protected boolean rebuild=true;
	/**
     * 取得该面板所用的CSS样式 多个用分号(半角)隔开
     * @return String
     */
	public String getStyle() {
		return style;
	}
	 /**
     * 设置该面板所用的CSS样式 多个用分号(半角)隔开
     * @param style String
     * @return 本身，这样可以继续设置其他属性
     */
	public T setStyle(String style) {
		this.style = style;
		return (T)this;
	}
	/**
     * 取得该面板所用的CSS类型 多个用空格隔开
     * @return String
     */
    public String getCls(){
		return cls;
	}
	/**
     * 设置该面板所用的CSS类型 多个用空格隔开
     * @param cls String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setCls(String cls){
		this.cls = cls;
		return (T)this;
	}
	/**
	 * 添加样式
	 * @param cls 样式
	 * @return this
	 */
	public T addCls(String cls) {
		// FIXME 这里性能可能有问题
		if (Utils.notEmpty(cls)) {
			cls = cls.trim();
			if (Utils.isEmpty(this.cls)) {
				this.setCls(cls);
			} else {
				Set<String> clsSet = parseClsSet(this.cls);
				if (clsSet.add(cls)) {
					this.setCls(Utils.toString(clsSet, ' '));
				}
			}
		}
		return (T)this;
	}
	/**
	 * 移除样式
	 * @param cls 样式
	 * @return this
	 */
	public T removeCls(String cls) {
		if (Utils.notEmpty(cls) && Utils.notEmpty(this.cls)) {
			cls = cls.trim();
			Set<String> clsSet = parseClsSet(this.cls);
			if (clsSet.remove(cls)) {
				this.setCls(Utils.toString(clsSet, ' '));
			}
		}
		return (T)this;
	}
	
	private Set<String> parseClsSet(String cls) {
		// FIXME 这里性能可能有问题
		Set<String> clsSet = new HashSet<String>();
		if (Utils.notEmpty(cls)) {
			String[] clsArray = cls.split(" ");
			Collections.addAll(clsSet, clsArray);
			clsSet.remove("");
		}
		return clsSet;
	}
	
	/**
	 * 同setCls 用于兼容2.x的使用习惯
	 * @param cls cssClass
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 同setCls 用于兼容2.x的使用习惯
	 */
	@Deprecated
	public T setStyleClass(String cls) {
		this.cls = cls;
		return (T)this;
	}
	/**
	 * 取得原始前的HTML内容，尽在做特效时有用处，如浮动的时候，一个箭头
	 * @return String
	 */
	public String getBeforecontent() {
		return beforecontent;
	}
	/**
	 *  原始前的HTML内容，尽在做特效时有用处，如浮动的时候，一个箭头
	 * @param beforecontent String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setBeforecontent(String beforecontent) {
		this.beforecontent = beforecontent;
		return (T)this;
	}
	/**
	 * 原始后的HTML内容，尽在做特效时有用处，
	 * @return String
	 */
	public String getAftercontent(){
		return aftercontent;
	}
	/**
	 * 原始后的HTML内容，尽在做特效时有用处，
	 * @param aftercontent String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setAftercontent(String aftercontent){
		this.aftercontent = aftercontent;
		return (T)this;
	}
	/**
	 * 设置备注
	 * @param remark String 为空时,会将aftercontent清空
	 * @see #setAftercontent(String)
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setRemark(String remark) {
		if (Utils.notEmpty(remark)) {
			return setAftercontent("<div class='f-remark'>" + Utils.escapeXMLword(remark) + "</div>");
		}
		// 为空时是将aftercontent清空
		return setAftercontent(null);
	}
	
	public String getId() {
		return id;
	}
	public T setId(String id) {
		this.id = id;
		return (T)this;
	}
	/**
     * 取得自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     * @return String
     */
	public String getGid() {
		return gid;
	}
	 /**
     * 设置自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     * @param gid String
	 * @return 本身，这样可以继续设置其他属性
     */
	public T setGid(String gid) {
		this.gid = gid;
		return (T)this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return String
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param width String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setWidth(String width) {
		this.width = width;
		return (T)this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return String
	 */
	public String getHeight() {
		return height;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param height String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setHeight(String height) {
		this.height = height;
		return (T)this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param width String
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setWidth(int width) {
    	this.width = String.valueOf(width);
	    return (T) this;
    }

    /**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param height String
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setHeight(int height) {
    	this.height = String.valueOf(height);
    	return (T) this;
    }
	
	public Map<String, String> getOn() {
		return events;
	}
	public T setOn(String eventName, String script) {
		if(eventName==null){
			return  (T)this;
		}
		if(events==null){
    		events=new TreeMap<String, String>();
    	}

        if(script==null||script.equals("")){
        	events.remove(eventName);
        }else{
        	events.put(eventName, script);
        }
		return (T)this;
	}
	/**
     * 取得竖直方向可用像素(高度)减少值
     * @return Integer
     */
	public Integer getWmin() {
		return wmin;
	}
    /**
     * 设置竖直方向可用像素(高度)减少值
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉上下各1像素，该面板的wmin=2
     * @param wmin Integer
     * @return 本身，这样可以继续设置其他属性
     */
	public T setWmin(Integer wmin) {
		this.wmin = wmin;
		return (T)this;
	}
	/**
     * 取得水平方向可用像素(高度)减少值
     * @return String
     */
    public Integer getHmin(){
		return hmin;
	}
    /**
     * 设置水平方向可用像素(宽度)减少值。
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉左右各1像素，该面板的hmin=2
     * @param hmin int
     * @return 本身，这样可以继续设置其他属性
     */
    public T setHmin(Integer hmin){
		this.hmin = hmin;
		return (T)this;
	}
	public Object getData(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        if(data == null) {
        	return null;
        }
        return data.get(key);
    }
	public Object removeData(String key) {
		if (key == null || key.equals("")) {
			return null;
		}
		if(data == null) {
			return null;
		}
		return data.remove(key);
	}
    public T setData(String key, Object value) {
    	if(data == null){
    		data = new LinkedHashMap<String, Object>();
    	}
    	data.put(key, value);
        return (T) this;
    }

    public Map<String, Object> getData() {
    	return data;
    }
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	public String getMaxwidth() {
		return maxwidth;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMaxwidth(int maxwidth) {
		this.maxwidth = String.valueOf(maxwidth);
		return (T) this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMaxwidth(String maxwidth) {
		this.maxwidth = maxwidth;
		return (T) this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	public String getMaxheight() {
		return maxheight;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMaxheight(String maxheight) {
		this.maxheight = maxheight;
		return (T) this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMaxheight(int maxheight) {
		this.maxheight = String.valueOf(maxheight);
		return (T) this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	public String getMinwidth() {
		return minwidth;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMinwidth(int minwidth) {
		this.minwidth = String.valueOf(minwidth);
		return (T) this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMinwidth(String minwidth) {
		this.minwidth = minwidth;
		return (T) this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	public String getMinheight() {
		return minheight;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMinheight(String minheight) {
		this.minheight = minheight;
		return (T) this;
	}
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMinheight(int minheight) {
		this.minheight = String.valueOf(minheight);
		return (T) this;
	}
	
	protected<N extends Widget<?>> List<N> findNodes(){
		return null;
	}
	protected Widget<?> findNodeById(String id) {
		List<Widget<?>> nodes=findNodes();
		if (id == null||nodes==null) {
			return null;
		}
		for (Iterator<Widget<?>> iter = nodes.iterator(); iter.hasNext();) {
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
		List<Widget<?>> nodes=findNodes();
		if (id == null||nodes==null) {
            return (T) this;
        }
        for (Iterator<Widget<?>> iter = nodes.iterator();
                                    iter.hasNext(); ) {
        	Widget<?> item = iter.next();
            if (id.equals(item.getId())) {
                iter.remove();
            } else if (item instanceof Layout) {
            	Layout<?,Widget<?>> cast = (Layout<?,Widget<?>>) item;
                cast.removeNodeById(id);
            }
        }
        return (T) this;
	}

	protected boolean replaceNodeById(Widget<?> w) {
		List<Widget<?>> nodes=findNodes();
		 if (w == null || w.getId() == null||nodes==null) {
			return false;
		}
		String id = w.getId();
		for (int i = 0; i < nodes.size(); i++) {
			Widget<?> item = nodes.get(i);
			if (id.equals(item.getId())) {
				// 替换该元素
				if(onReplace(item,w)){
					nodes.set(i,  w);
					return true;
				}else{
					return false;
				}
			} else if (item instanceof Layout) {
                Layout<?,Widget<?>> cast = (Layout<?,Widget<?>>) item;
				boolean replaced = cast.replaceNodeById(w);
				if (replaced) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean onReplace(Widget<?> oldWidget,Widget<?> newWidget){
		if(!Utils.isEmpty(oldWidget.getWidth())&&
				Utils.isEmpty(newWidget.getWidth())){
			newWidget.setWidth(oldWidget.getWidth());
		}
		if(!Utils.isEmpty(oldWidget.getHeight())&&
				Utils.isEmpty(newWidget.getHeight())){
			newWidget.setHeight(oldWidget.getHeight());
		}
		return true;
	}
	protected List<FormElement<?,?>> findFormElementsByName(String name) {
		List<FormElement<?,?>> result=new ArrayList<FormElement<?,?>>();
		findFormElementsByName(name,result);
		return result;
	}
	protected void findFormElementsByName(String name,List<FormElement<?,?>> result) {
		if (name == null||name.equals("")) {
			return;
		}
		if(this instanceof HiddenContainer<?>){
			HiddenContainer<?> cast=(HiddenContainer<?>)this;
			if(cast.getHiddens()!=null)
			for(Hidden hidden:cast.getHiddens()){
				if(name.equals(hidden.getName())){
					result.add(hidden);
				}
			}
		}
		List<Widget<?> > nodes=findNodes();
		if(nodes!=null)
		for (Widget<?> item :nodes) {
			if(item==null){
				continue;
			}
			if (item instanceof FormElement<?,?>) {
				FormElement<?,?> cast=(FormElement<?,?>)item;
				if(name.equals(cast.getName())){
					result.add(cast);
				}
			}else if(item instanceof Layout){
				if(item instanceof AbstractNode){
					((AbstractNode<?>) item).findFormElementsByName(name,result);
				}else{
					result.addAll(((Layout<?,?>)item).findFormElementsByName(name));
				}
			}
		}
	}

	/**
	 * 拷贝属性
	 * @param to AbstractNode
	 * @param from AbstractNode
	 */
	protected void copyProperties(AbstractNode<?>to,AbstractNode<?>from){
//		Utils.copyPropertiesExact(to, from);
		to.aftercontent=from.aftercontent;
		to.beforecontent=from.beforecontent;
		to.cls=from.cls;
		to.gid=from.gid;
		to.height=from.height;
		to.hmin=from.hmin;
		to.id=from.id;
		to.maxheight=from.maxheight;
		to.maxwidth=from.maxwidth;
		to.minheight=from.minheight;
		to.minwidth=from.minwidth;
		to.style=from.style;
		to.width=from.width;
		to.wmin=from.wmin;
		//浅拷贝
		to.data=from.data;
		to.events=from.events;
	}
	protected String toString(Object value){
		return value==null?null:value.toString();
	}
	protected Number toNumber(Object value){
		if(value==null||"".equals(value)){
			return null;
		}
		if(value instanceof Number){
			return (Number) value;
		}
		String str=value.toString();
		if(str.indexOf('.')>0){
			return new Double(str);
		}else{
			return new Integer(str);
		}
	}

}
