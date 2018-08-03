package com.rongji.dfish.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rongji.dfish.ui.Directional;
import com.rongji.dfish.ui.FormElement;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.Layout;
import com.rongji.dfish.ui.Widget;

/**
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 * @param <N> 子节点对象类型
 * @param <P> value对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBoxgroup <T extends AbstractBoxgroup<T,N,P>,N extends AbstractBox<N>,P> extends AbstractOptionsHolder<T,P> implements Layout<T,N>,HtmlContentHolder<T>,Directional<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3733166777271763891L;
	protected N pub;
	protected Integer space;
	protected List<Widget<?>> targets= new ArrayList<Widget<?>>();
	protected List<N> nodes = new ArrayList<N>();
	protected Boolean escape;
	protected String dir;

    /**
     * 构造函数
     * @param name 表单名
     * @param label 标题
     * @param checkedValue 已经选中的值
     * @param options 候选项
     */
    public AbstractBoxgroup(String name, String label, Object checkedValue,
			List<?> options) {
    	super(name, label, checkedValue, options);
    	
//    	this.name=name;
//    	this.label=label;
//    	this.setValue(checkedValue);
    	checkOptions(checkedValue, options);
//    	escape=true;
	}
	
    protected abstract N buildOption(Option o);
    /**
     * 添加子面板
     * @param w  N
     * @return 本身，这样可以继续设置其他属性
     */
	public T add(N w) {
        return add(-1,w);
    }
    /**
     * 在指定的位置添加子面板
     * @param index 位置
     * @param w  N
     * @return 本身，这样可以继续设置其他属性
     */
	public T add(int index, N w) {
        if (w == null) {
            return (T) this;
        }
        if(index<0){
        	nodes.add(w);
        }else{
        	nodes.add(index,w);
        }
        return (T) this;
    }
	
	/**
	 * 增加选项元素
	 * @param w 元素option对象
	 * @return add(w)s
	 */
	public T addOption(N w) {
        return add(w);
    }

	public Widget<?> findNodeById(String id) {
		return super.findNodeById(id);
	}
	
	public List<FormElement<?,?>> findFormElementsByName(String name) {
		return super.findFormElementsByName(name);
	}

	public List<Widget<?>> findNodes() {
		List<Widget<?>> result=new ArrayList<Widget<?>>();
		if(nodes!=null){
			result.addAll(nodes);
		}
		if(targets!=null){
			result.addAll(targets);
		}
		return result;
	}

	private void checkOptions(Object checkedValue,List<?> options) {
		Set<String> theValue=null;
		if(checkedValue==null){
			theValue=new HashSet<String>();
		}else if(checkedValue instanceof int[]){
			int[] cast=(int[])checkedValue;
			theValue=new HashSet<String>();
			for(int o:cast){
				theValue.add(String.valueOf(o));
			}
		}else if(checkedValue instanceof char[]){
			char[] cast=(char[])checkedValue;
			theValue=new HashSet<String>();
			for(char o:cast){
				theValue.add(String.valueOf(o));
			}
		}else if(checkedValue instanceof long[]){
			long[] cast=(long[])checkedValue;
			theValue=new HashSet<String>();
			for(long o:cast){
				theValue.add(String.valueOf(o));
			}
		}else if(checkedValue.getClass().isArray()){
			Object[] cast=(Object[])checkedValue;
			theValue=new HashSet<String>();
			for(Object o:cast){
				theValue.add(o==null?null:o.toString());
			}
		}else if(checkedValue instanceof Collection){
			Collection<?> cast=(Collection<?>)checkedValue;
			theValue=new HashSet<String>();
			for(Object o:cast){
				theValue.add(o==null?null:o.toString());
			}
		}else{
			theValue=new HashSet<String>();
			theValue.add(checkedValue==null?null:checkedValue.toString());
		}
		if(theValue.size()==0){
			theValue.add(null);
			theValue.add("");
		}
//		nodes.clear();
		if(options!=null){
			for(Object item:options){
				Option o=null;
				if(item ==null ){
					continue;
				} else if (item instanceof Option) {
					o = (Option) item;
				} else {
					String text=null;
					Object value=null; 
					String ic=null;
					if (item instanceof Object[]|| item instanceof String[]) {
						Object[] castItem = (Object[]) item;
						value = castItem[0];
						if (castItem.length > 2) {
							ic= String.valueOf(castItem[2]);
						}
						if (castItem.length > 1) {
							text = String.valueOf(castItem[1]);
						} else {
							text = String.valueOf(castItem[0]);
						}
						
					} else if (item instanceof String || item instanceof Number ||
							item instanceof java.util.Date) {
						value = item;
						text =  String.valueOf(item);
					} else {
						LOG.error("invalide options item " + item + " ,should be Object[] ");
						break;
					}
					o = new Option(value, text);
					o.setIcon(ic);
	            }
				
				if (theValue.contains(o.getValue()==null?o.getValue():o.getValue().toString())) {
					o.setChecked(true);
				}
				N option = buildOption(o);
				option.setEscape(calcRealEscape(option.getEscape(), getEscape()));
				this.addOption(option);
			}
		}
	}


	public T removeNodeById(String id) {
		//FIXME 这个需要重写
		return super.removeNodeById(id);
	}

    public boolean replaceNodeById(Widget<?> w) {
		//FIXME 这个需要重写
		 return super.replaceNodeById(w);
	}


	/**
	 * boxgroup节点数组
	 * @return 本身，这样可以继续设置其他属性
	 */
	public List<? extends N> getOptions() {
		return nodes;
	}

	/**
	 * boxgroup的默认参数
	 * @return pub 
	 */
	public N getPub() {
		return pub;
	}
	/**
	 * boxgroup的默认参数
	 * @param pub 默认元素对象
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setPub(N pub) {
		this.pub = pub;
		return (T)this;
	}

	/**
	 * 当设置了 targets，再设置 space 可调整行间距。
	 * @return space
	 */
	public Integer getSpace() {
		return space;
	}
	/**
	 * 当设置了 targets，再设置 space 可调整行间距。
	 * @param space 间距
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setSpace(Integer space) {
		this.space = space;
		return (T)this;
	}

	/**
	 * 和boxgroup 一一对应的节点数组。勾选复选框将改变 target 节点的 disabled 状态。
	 * @return targets
	 */
	public List<Widget<?>> getTargets() {
		return targets;
	}
	/**
	 * 和boxgroup 一一对应的节点数组。勾选复选框将改变 target 节点的 disabled 状态。
	 * @param w 对象
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T addTarget(Widget<?> w) {
		targets.add(w);
		return (T)this;
	}
	public Boolean getEscape() {
		return this.escape;
	}
	
	public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T)this;
	}
	
	public String getDir() {
		return dir;
	}
	
	public T setDir(String dir) {
		this.dir = dir;
		return (T) this;
	}
	
}
