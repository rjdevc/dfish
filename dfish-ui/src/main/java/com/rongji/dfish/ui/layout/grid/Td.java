package com.rongji.dfish.ui.layout.grid;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.json.JsonWrapper;
import com.rongji.dfish.ui.layout.Grid;
import com.rongji.dfish.ui.widget.Html;
/**
 *  Td 表示一个Grid的单元格
 * <p>在一些复杂布局结构中，它可以占不止一行或不止一列</p>
 * <p>GridCell 有两种工作模式，他内部可以包含一个Widget或简单的包含一个文本，如果包含了widget文本模式将失效</p>
 * <p>虽然GridCell也是一个Widget，但其很可能并不会专门设置ID。</p>
 * <p>由于很多情况下，界面上出现的就是TD元素。所以，TD元素一般输出的时候会自动简写</p>
 * 其完整格式为。
 * <pre>
 * {
 *   "align":"right","node":{
 *     "type":"html","text":"杨桃","style":"background-color:gray"
 *   }
 * }</pre>
 * <p>如果没有TD本身的属性都没有设置。很可能只输出node的部分</p>
 * {"type":"html","text":"杨桃","style":"background-color:gray"}
 * <p>而如果这时候html的其他属性也没设置，很可能进一步简写为 "杨桃"</p>
 * <p>一个特殊的简写规则，如果Td设置了属性，但内部node是Html而且只设置了text属性，可以被简写为</p>
 * <p>{"text":"橙子","align":"right"}</p>
 * <p>而td本身并没有setText属性</p>
 * 
 * @author DFish Team
 * @see Tr
 */
public class Td extends AbstractTd<Td> implements JsonWrapper<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4639610865052336483L;
	/**
	 * 默认构造函数
	 */
	public Td(){
		super();
	}
	/**
	 * 拷贝构造函数 相当于clone
	 * @param td  AbstractTd
	 */
	public Td(AbstractTd<?> td){
		super();
		copyProperties(this,td);
	}
	protected Grid owner;

	public Grid owner() {
		return owner;
	}

	public Td owner(Grid owner) {
		this.owner = owner;
		return this;
	}
	private void nc(){
		if(owner!=null){
			owner.notifyChange();
		}
	}
	
	@Override
	public Object getPrototype() {
		if(hasTdllInfo(this)){
			JsonTd p=new JsonTd();
			copyProperties(p,this);
			if(isTextWidget(getNode())){
				//把文本当做cell的text
				String text=getTextValue(getNode());
				p.setText(text);
				p.setNode(null);
				return p;
			}else{
				p.setNode(getNode());
				return p;
			}
		}else{
			Widget<?> w=getNode();
			if(isTextWidget(getNode())){
				String text=getTextValue(getNode());
				return text;
			}else{
				return w;
			}
		}
	}

	private static boolean hasTdllInfo(AbstractTd<?> td){
		return td.getId()!=null||td.getHeight()!=null||
				td.getAlign()!=null||td.getValign()!=null||
				td.getColspan()!=null||td.getRowspan()!=null||
				td.getCls()!=null||td.getStyle()!=null ||//常用的属性排在前面
				td.getBeforecontent()!=null||td.getPrependcontent()!=null||
				td.getAppendcontent()!=null||td.getAftercontent()!=null||
				td.getGid()!=null||td.getHmin()!=null||
				td.getMaxheight()!=null||td.getMaxwidth()!=null||
				td.getMinheight()!=null||td.getMinwidth()!=null||
				(td.getOn()!=null&&td.getOn().size()>0)||
				td.getWidth()!=null||td.getWmin()!=null;
    }
	/**
	 * 取得html的内容。为了效率这个方法不再进行判断，所以只能跟在isTextWidget 后使用。
	 * @param node Widget
	 * @return String
	 */
    private String getTextValue(Widget<?> node) {
    	Object prototype=node;
		while (prototype instanceof JsonWrapper){
			prototype=((JsonWrapper<?>)prototype).getPrototype();
		}
    	Html cast=(Html)prototype;
		return cast.getText();
	}
    /**
     * 是不是内容里只有Text部分是有效信息，如果是的话。这里要简化输出json
     * @param node
     * @return 是否文本组件
     */
	private static boolean isTextWidget(Widget<?> node) {
		if(node==null){
			return false;
		}
		Object prototype=node;
		while (prototype instanceof JsonWrapper){
			prototype=((JsonWrapper<?>)prototype).getPrototype();
		}
		if(!(prototype instanceof Html)){
			return false;
		}
		Html html=(Html)prototype;
		return html.getId()==null&&html.getHeight()==null&&
				html.getWidth()==null&&html.getEscape()==null&&
				html.getAlign()==null&&html.getValign()==null&&
				html.getCls()==null&&html.getStyle()==null &&//常用的属性排在前面
				html.getAppendcontent()==null&&html.getPrependcontent()==null&&
				html.getGid()==null&&html.getHmin()==null&&
				html.getMaxheight()==null&&html.getMaxwidth()==null&&
				html.getMinheight()==null&&html.getMinwidth()==null&&
				(html.getOn()==null||html.getOn().size()==0)&&
				html.getScroll()==null&&html.getScrollClass()==null&&
				html.getWmin()==null;
	}
	@Override
	public Td setColspan(Integer colspan) {
		nc();
		return super.setColspan(colspan);
	}
	@Override
	public Td setRowspan(Integer rowspan) {
		nc();
		return super.setRowspan(rowspan);
	}
	@Override
	public Td setNode(Widget<?> node) {
		nc();
		return super.setNode(node);
	}
	@Override
	public Td removeNodeById(String id) {
		Td td=super.removeNodeById(id);
		if(td!=null){
			nc();
		}
		return td;
	}
	@Override
	public boolean replaceNodeById(Widget<?> w) {
		boolean success=super.replaceNodeById(w);
		if(success){nc();}
		return success;
	}
	@Override
	public Td setValign(String valign) {
		nc();
		return super.setValign(valign);
	}
	@Override
	public Td setAlign(String align) {
		nc();
		return super.setAlign(align);
	}
	@Override
	public Td setStyle(String style) {
		nc();
		return super.setStyle(style);
	}
	@Override
	public Td setCls(String cls) {
		nc();
		return super.setCls(cls);
	}
	@Override
	public Td setPrependcontent(String prependcontent) {
		nc();
		return super.setPrependcontent(prependcontent);
	}
	@Override
	public Td setAppendcontent(String appendcontent) {
		nc();
		return super.setAppendcontent(appendcontent);
	}
	@Override
	public Td setId(String id) {
		nc();
		return super.setId(id);
	}
	@Override
	public Td setGid(String gid) {
		nc();
		return super.setGid(gid);
	}
	@Override
	public Td setWidth(String width) {
		nc();
		return super.setWidth(width);
	}
	@Override
	public Td setHeight(String height) {
		nc();
		return super.setHeight(height);
	}
	@Override
	public Td setWidth(int width) {
		nc();
		return super.setWidth(width);
	}
	@Override
	public Td setHeight(int height) {
		nc();
		return super.setHeight(height);
	}
	@Override
	public Td setOn(String eventName, String script) {
		nc();
		return super.setOn(eventName, script);
	}
	@Override
	public Td setWmin(Integer wmin) {
		nc();
		return super.setWmin(wmin);
	}
	@Override
	public Td setHmin(Integer hmin) {
		nc();
		return super.setHmin(hmin);
	}
	@Override
	public Td setData(String key, Object value) {
		nc();
		return super.setData(key, value);
	}
	@Override
	public Td setMaxwidth(int maxwidth) {
		nc();
		return super.setMaxwidth(maxwidth);
	}
	@Override
	public Td setMaxwidth(String maxwidth) {
		nc();
		return super.setMaxwidth(maxwidth);
	}
	@Override
	public Td setMaxheight(String maxheight) {
		nc();
		return super.setMaxheight(maxheight);
	}
	@Override
	public Td setMaxheight(int maxheight) {
		nc();
		return super.setMaxheight(maxheight);
	}
	@Override
	public Td setMinwidth(int minwidth) {
		nc();
		return super.setMinwidth(minwidth);
	}
	@Override
	public Td setMinwidth(String minwidth) {
		nc();
		return super.setMinwidth(minwidth);
	}
	@Override
	public Td setMinheight(String minheight) {
		nc();
		return super.setMinheight(minheight);
	}
	@Override
	public Td setMinheight(int minheight) {
		nc();
		return super.setMinheight(minheight);
	}
	

}
