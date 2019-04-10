package com.rongji.dfish.ui.widget;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Alignable;

/**
 * 翻页工具条
 * @author DFish Team
 *
 */
public class PageBar extends AbstractWidget<PageBar> implements Alignable<PageBar>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7425582244642151536L;
	private String name;//如果设置了name，将生成一个隐藏项，值为当前页数
	private String align ;//水平居中。可用值: left,right,center
	private String btncls ;//	按钮样式。
	private Integer btncount;//中间有几个显示页数的按钮。
	private Integer currentpage ;//	当前页数。(起始值为1)
	private Boolean jump ; //	显示一个可填写页数的表单。
	private Boolean nofirstlast ; //	不显示"首页"和"尾页"两个按钮。
	private String src;//	点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。
	private Integer sumpage;//总页数。(起始值为1)
	private String target ; //绑定一个支持前端翻页的widget(例如grid)。
	private Boolean transparent;//设置为true，可去除边框背景等预设样式。
	private String labelfirst;
	private String labellast;
	private String labelnext;
	private String labelprev;
	private String info;
	private List<Button> setting;
	private String type;
	private String dropalign;
	private Boolean btnsumpage;
	/**
	 * page/mini 小按钮风格的翻页工具条。
	 */
	public static final String TYPE_MINI ="page/mini";
	/**
	 * page/text 文本风格的翻页工具条。
	 */
	public static final String TYPE_TEXT ="page/text";
	/**
	 * page/buttongroup 组合按钮风格的翻页工具条。
	 */
	public static final String TYPE_BUTTONGROUP ="page/buttongroup";
	/**
	 * 默认构造函数
	 */
	public PageBar(){
		this.type=TYPE_BUTTONGROUP;
	}
	/**
	 * 构造函数
	 * @param id String
	 * @param type 类型
	 * @see #TYPE_BUTTONGROUP
	 * @see #TYPE_MINI
	 * @see #TYPE_TEXT
	 */
	public PageBar(String id,String type){
		this.id=id;
		this.type=type;
	}
	
	/**
	 * 当前页数。(起始值为1)
	 * @return currentpage
	 */
	public Integer getCurrentpage() {
		return currentpage;
	}
	/**
	 * 当前页数。(起始值为1)
	 * @param currentpage
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setCurrentpage(Integer currentpage) {
		this.currentpage = currentpage;
		return this;
	}
	/**
	 * 总页数。(起始值为1)
	 * @return sumpage
	 */
	public Integer getSumpage() {
		return sumpage;
	}
	/**
	 * 总页数。(起始值为1)
	 * @param sumpage
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setSumpage(Integer sumpage) {
		this.sumpage = sumpage;
		return this;
	}

	
	public String getType() {
		return type;
	}
	public String getAlign() {
		return align;
	}
	public PageBar setAlign(String align) {
		this.align = align;
		return this;
	}

	/**
	 * 按钮样式。
	 * @return btncls
	 */
	public String getBtncls() {
		return btncls;
	}
	/**
	 * 按钮样式。
	 * @param btncls
	 * @return  this
	 */
	public PageBar setBtncls(String btncls) {
		this.btncls = btncls;
		return this;
	}
	/**
	 * 中间是否有显示页数的按钮。值为0或1。
	 * @return btncount
	 */
	public Integer getBtncount() {
		return btncount;
	}
	/**
	 * 中间是否有显示页数的按钮。值为0或1。
	 * @param btncount
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setBtncount(Integer btncount) {
		this.btncount = btncount;
		return this;
	}
	/**
	 * 显示一个可填写页数的表单。
	 * @return jump
	 */
	public Boolean getJump() {
		return jump;
	}
	/**
	 * 显示一个可填写页数的表单。
	 * @param jump
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setJump(Boolean jump) {
		this.jump = jump;
		return this;
	}
	/**
	 * 不显示"首页"和"尾页"两个按钮。
	 * @return nofirstlast
	 */
	public Boolean getNofirstlast() {
		return nofirstlast;
	}
	/**
	 * 不显示"首页"和"尾页"两个按钮。
	 * @param nofirstlast
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setNofirstlast(Boolean nofirstlast) {
		this.nofirstlast = nofirstlast;
		return this;
	}
	/**
	 * 点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。
	 * @return src 
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。
	 * @param src
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setSrc(String src) {
		this.src = src;
		return this;
	}
	/**
	 * 绑定一个支持前端翻页的widget(例如grid)。
	 * @return target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * 绑定一个支持前端翻页的widget(例如grid)。
	 * @param target
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setTarget(String target) {
		this.target = target;
		return this;
	}

	/**
	 * 设置为true，可去除边框背景等预设样式。
	 * @return transparent
	 */
	public Boolean getTransparent() {
		return transparent;
	}
	/**
	 * 设置为true，可去除边框背景等预设样式。
	 * @param transparent
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setTransparent(Boolean transparent) {
		this.transparent = transparent;
		return this;
	}
	/**
	 * 如果设置了name，将生成一个隐藏项，值为当前页数
	 * @return String
	 */
	public String getName() {
		return name;
	}
	/**
	 * 如果设置了name，将生成一个隐藏项，值为当前页数
	 * @param name String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * 首页标签名
	 * @return String
	 */
	public String getLabelfirst() {
		return labelfirst;
	}
	/**
	 * 设置首页标签名
	 * @param labelfirst String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setLabelfirst(String labelfirst) {
		this.labelfirst = labelfirst;
		return this;
	}
	/**
	 * 尾页标签名
	 * @return String
	 */
	public String getLabellast() {
		return labellast;
	}
	/**
	 * 设置尾页标签名
	 * @param labellast
	 */
	public PageBar setLabellast(String labellast) {
		this.labellast = labellast;
		return this;
	}
	/**
	 * 下页标签名
	 * @return String
	 */
	public String getLabelnext() {
		return labelnext;
	}
	/**
	 * 设置下页标签名
	 * @param labelnext String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setLabelnext(String labelnext) {
		this.labelnext = labelnext;
		return this;
	}
	/**
	 * 上页标签名
	 * @return String
	 */
	public String getLabelprev() {
		return labelprev;
	}
	/**
	 * 设置上页标签名
	 * @param labelprev String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setLabelprev(String labelprev) {
		this.labelprev = labelprev;
		return this;
	}
	/**
	 * 显示总条数和总页数等信息。
	 * @return String
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * 显示总条数和总页数等信息。
	 * @param info String
	 * @return this
	 */
	public PageBar setInfo(String info) {
		this.info = info;
		return this;
	}
	/**
	 * button数组。生成一个配置按钮和下拉菜单。
	 * @return List
	 */
	public List<Button> getSetting() {
		return setting;
	}
	/**
	 * button数组。生成一个配置按钮和下拉菜单。
	 * @param setting List
	 * @return this
	 */
	public PageBar setSetting(List<Button> setting) {
		this.setting = setting;
		return this;
	}
	/**
	 * 设置分页信息
	 * @param page Page
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setPage(Page page) {
		if (page == null) {
			return this;
		}
		if (page.getCurrentPage() < 1) {
			page.setCurrentPage(1);
		}
		if (page.getPageSize() < 1) {
			page.setPageSize(10);
		}
		this.setCurrentpage(page.getCurrentPage());
		this.setSumpage(page.getPageCount());
		// FIXME 目前每页显示数量先放在data,后续有可能也是pagebar一个属性
		this.setData("pagesize", page.getPageSize());
		return this;
	}
	/**
	 * 下拉按钮的位置
	 * @return String
	 */
	public String getDropalign() {
		return dropalign;
	}
	/**
	 * 下拉按钮的位置
	 * @param dropalign String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setDropalign(String dropalign) {
		this.dropalign = dropalign;
		return this;
	}
	/**
	 * 显示总页数按钮
	 * @return Boolean
	 */
	public Boolean getBtnsumpage() {
		return btnsumpage;
	}
	
	/**
	 * 显示总页数按钮
	 * @param btnsumpage Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PageBar setBtnsumpage(Boolean btnsumpage) {
		this.btnsumpage = btnsumpage;
		return this;
	}
}
