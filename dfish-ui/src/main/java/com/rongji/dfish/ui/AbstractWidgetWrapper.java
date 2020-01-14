package com.rongji.dfish.ui;

import java.util.Map;

import com.rongji.dfish.ui.json.JsonWrapper;
/**
 * 默认的warper抽象类
 * 一般继承与此类的抽象类需要在构造方法体内为
 * prototype 赋值。
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 * @param <P> 原型类型
 */
public abstract class AbstractWidgetWrapper<T extends AbstractWidgetWrapper<T, P>, P extends Widget<P>> extends AbstractWidget<T> implements JsonWrapper<P>,UiNode<T> {

	private static final long serialVersionUID = 2711923347412806092L;

	protected P prototype;
	protected boolean prototypeChanged=false;
	public void notifyChange(){
		prototypeChanged=true;
	}
	protected void checkConcurrentModify(){
		if(prototypeChanged){
			throw new java.util.ConcurrentModificationException("can NOT change wrapper when prototype is changed");
		}
	}

	@Override
    public P getPrototype() {
	    return prototype;
    }
	
	@Override
    public String getType() {
	    return prototype.getType();
    }

    @Override
    public String asJson() {
	    return prototype.asJson();
    }
    
    @Override
    public String toString() {
    	return getPrototype().toString();
    }

	@Override
    public String getStyle() {
	    return prototype.getStyle();
    }

	@Override
    public T setStyle(String style) {
		prototype.setStyle(style);
	    return (T) this;
    }

	@Override
    public String getCls() {
	    return prototype.getCls();
    }

	@Override
    public T setCls(String styleClass) {
	    prototype.setCls(styleClass);
	    return (T) this;
    }
	
	@Override
	public T addCls(String styleClass) {
		prototype.addCls(styleClass);
		return (T) this;
	}
	
	@Override
	public T removeCls(String styleClass) {
		prototype.removeCls(styleClass);
		return (T) this;
	}

	@Override
    public String getId() {
	    return prototype.getId();
    }

	@Override
    public T setId(String id) {
	    prototype.setId(id);
	    return (T) this;
    }
	
	@Override
    public String getGid() {
	    return prototype.getGid();
    }


	@Override
    public T setGid(String gid) {
	    prototype.setGid(gid);
	    return (T) this;
    }

	@Override
    public String getWidth() {
	    return prototype.getWidth();
    }

	@Override
    public T setWidth(String width) {
	    prototype.setWidth(width);
	    return (T) this;
    }

	@Override
	public T setWidth(int width) {
		prototype.setWidth(width);
		return (T) this;
	}
	
	@Override
    public String getHeight() {
	    return prototype.getHeight();
    }

	@Override
    public T setHeight(String height) {
	    prototype.setHeight(height);
	    return (T) this;
    }

	@Override
    public T setHeight(int height) {
		prototype.setHeight(height);
	    return (T) this;
    }
	@Override
    public Map<String, String> getOn() {
	    return prototype.getOn();
    }

	@Override
    public T setOn(String eventName, String script) {
	    prototype.setOn(eventName, script);
	    return (T) this;
    }

	@Override
    public Integer getWidthMinus() {
	    return prototype.getWidthMinus();
    }

	@Override
    public T setWidthMinus(Integer widthMinus) {
	    prototype.setWidthMinus(widthMinus);
	    return (T) this;
    }

	@Override
    public Integer getHeightMinus() {
	    return prototype.getHeightMinus();
    }

	@Override
    public T setHeightMinus(Integer heightMinus) {
	    prototype.setHeightMinus(heightMinus);
	    return (T) this;
    }

	@Override
	public Object getData(String key) {
        return prototype.getData(key);
    }
	
	@Override
	public Object removeData(String key) {
		return prototype.removeData(key);
	}
	
    @Override
    public T setData(String key, Object value) {
    	prototype.setData(key, value);
        return (T) this;
    }
    
    @Override
    public Map<String, Object> getData() {
    	return prototype.getData();
    }
}
