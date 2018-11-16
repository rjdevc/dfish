package com.rongji.dfish.ui.layout.grid;

import java.beans.Transient;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.AbstractJsonObject;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Highlight;
import com.rongji.dfish.ui.JSFunction;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.Checkbox;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.Triplebox;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.json.RawJson;


/**
 * <p>配置某列的属性</p>
 * <p>属性介绍如下</p>
 * <ul>
 * <li><strong>beanProp 与 dataColumnIndex</strong>:<br>
 * 说明这个数据从data中获取的方式。如果data的格式为List&lt;Object[]&gt;则用dataColumnIndex指定取那个列的数据。
 * 否则，则用beanProp表示，取得哪个属性</li>
 * <li><strong>field</strong>:<br>
 * 数据在传输前台客户端的时候，转为JSON，这个为JSON的属性名，相当于是前台客户端里面的数据列的名字。注意不要有重复。</li>
 * <li><strong>label</strong>:<br>
 * 如果这个列是可以显示的，那么他应该有一个标题</li>
 * <li><strong>width</strong>:<br>
 * 如果这个列是可以显示的，那么他应该有一个宽度。宽度可以是数字(单位像素)，也可以是百分数，或者是<strong>*</strong>。
 * 如果有多个列为*,将平摊宽度</li>
 * <li><strong>align 和 valign</strong>:<br>
 * 这个列的对齐方式，默认为左对齐，高度居中</li>
 * <li><strong>sortfld 与 sortType</strong>:<br>
 * 如果这个列在前端可以有排序效果，那么需要指定它按哪个列(取field)排序的。而排序方式是文本还是数字可以由sortType指定。
 * 默认为文本。</li>
 * <li><strong>sortIcon 与 sortClick</strong>:<br>
 * 如果这个列在JAVA端可以有排序效果，那么需要指定它当前的排序方式用什么图标表示。并指定如果这个图标被点击时会触发什么动作。</li>
 * <li><strong>format</strong>:<br>
 * 如果这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量，
 * 典型的是做成一个链接。这个字段显示成一个链接，而其中只有标题和编号是变化的数据。那么可以使用format。
 * 并把标题和编号放在grid的数据中。format中以$id 和$tt 来动态加载数据(id和tt以产生的JSON为准)<br>
 * format以javascript:开头的将运行一系列java script代码</li>
 * <li><strong>dataFormat</strong>:<br>
 * 这个字段显示的时候的格式，这个不会输出到JSON中去，它在把时间/数字格式的字段输出时，就提供了格式化。如果字段是时间型
 * 格式请参看java.text.SimpleDateFormat的表达式，如果字段是字符型的，请参照java.text.NumberFormat的表达式<br>
 * format以javascript:开头的将运行一系列java script代码</li>
 * <li><strong>checkBoxName</strong>:<br>
 * 默认grid里面的checkbox提交的时候名字为selectItem 如果需要指定这个名字，(如需要同时提交多个grid的时候)，
 * 可以指定这个值</li>
 * <li><strong>synchronizedFocus</strong>:<br>
 * 当为 Radio/checkBox 是否让radio和当前焦点状态同步。
 * </li>
 * </ul>
 * 可以使用 text / checkbox / hidden / radio方法快捷构建一个GridColumn
 */

public class GridColumn extends AbstractJsonObject implements Alignable<GridColumn>,Valignable<GridColumn>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3246628575622594917L;

	public static final String COLUMN_FIELD_UNKNOWN = "UNKNOWN";
	
	String beanProp;
	int dataColumnIndex = -1;
	String field;
	String label;
	String width;
	Object format;
//	String sortsrc;
//	String sort;
	String style;
	String cls;
	String align;
	String valign;
	String dataFormat;
	Object tip;
	Object sort;
	Integer minwidth;
	Integer maxwidth;
	Highlight highlight;
	RawJson rawFormat;
	
	public RawJson rawFormat() {
		return rawFormat;
	}
	
//	AbstractBox<?> box;
	
	/**
	 * 排序-默认 {@link Sort#STATUS_DEFAULT}
	 */
	@Deprecated
	public static final String SORT_DEFAULT = Sort.STATUS_DEFAULT;
	/**
	 * 排序-正序 {@link Sort#STATUS_ASC}
	 */
	@Deprecated
	public static final String SORT_ASC = Sort.STATUS_ASC;
	/**
	 * 排序-倒序 {@link Sort#STATUS_DESC}
	 */
	@Deprecated
	public static final String SORT_DESC = Sort.STATUS_DESC;
	
	public static final String BOX_NAME = "selectItem";
	
	/**
	 * 默认构造函数。
	 */
	public GridColumn (){}
	/**
	 * <p>构造函数</p>
	 * <p>这个构造函数适用与数据是List&lt;Object[]&gt;的时候.</p>
	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @param label String 在表头显示的标题
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 */
	public GridColumn (int dataColumnIndex,String field,String label,String width){
		this.dataColumnIndex=dataColumnIndex;
		this.field=field;
		this.label=label;
		this.width=width;
	}
	/**
	 * <p>构造函数</p>
	 * <p>这个构造函数适用与数据是List&lt;Object[]&gt;的时候.</p>
	 * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @param label String 在表头显示的标题
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 */
	public GridColumn (String beanProp,String field,String label,String width){
		this.beanProp=beanProp;
		this.field=field;
		this.label=label;
		this.width=width;
	}
	/**
	 * 构建一个TEXT类型的GridColumn
	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @param label String 在表头显示的标题
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn text(int dataColumnIndex,String field,String label,String width){
		return new GridColumn(dataColumnIndex, field, label, width);
	}
	/**
	 * 构建一个TEXT类型的GridColumn
	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param label String 在表头显示的标题
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn text(int dataColumnIndex, String label,String width){
		return new GridColumn(dataColumnIndex, null, label, width);
	}
	/**
	 * 构建一个TEXT类型的GridColumn
	 * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @param label String 在表头显示的标题
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn text(String beanProp,String field,String label,String width){
		return new GridColumn( beanProp, field, label, width);
	}
	/**
	 * 构建一个TEXT类型的GridColumn 在GridLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
	 * 这个一般在GridLayout原型的时候使用。
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn text(String field,String width){
		return new GridColumn( null,field, null, width);
	}
	/**
	 * 构建一个TEXT类型的GridColumn 在GridLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @param format 文本格式转化
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn text(String field,String width,String format){
		return new GridColumn( null,field, null, width).setFormat(format);
	}
	
	/**
	 * 构建一个HIDDEN类型的GridColumn
	 * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn hidden(String beanProp, String field){
		return new GridColumn(beanProp, field,null,null);
	}
	/**
	 * 构建一个HIDDEN类型的GridColumn
	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param field String 输出时显示的JSON属性名字。注意不要有重复
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn hidden(int dataColumnIndex, String field){
		return new GridColumn(dataColumnIndex, field, null, null);
	}

	/**
	 * 构建一个序号列
	 * @param label 标签
	 * @param width 宽度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn rownum(String label, String width) {
		return rownum(label, null, width);
	}
	
	/**
	 * 构建一个序号列
	 * @param label 标签
	 * @param start 开始编号
	 * @param width 宽度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn rownum(String label, Integer start, String width) {
		return new GridColumn(null, null, label, width).setFormat("javascript:return " + new GridRownum(start)).setAlign(GridColumn.ALIGN_CENTER);
	}
	
	/**
	 * 构建一个CHECKBOX类型的GridColumn
	 * @param checkedField String 所选中值指定的列名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @see #gridTriplebox(String, String)
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public static GridColumn checkbox(String checkedField, String width){
//		return new GridColumn( null, field, null, width).setBox(new Checkbox(CHECK_FIELD_NAME, null, null, null,null).setField(new BoxField().setValue(field))).setAlign(GridColumn.ALIGN_CENTER);
		// FIXME 刚开始若未指定field,这个效果就有问题
//		return new GridColumn( null, field, null, width).setModel(new GridTriplebox(CHECK_FIELD_NAME, null, null, null, null)).addModelfield("value", field).setAlign(GridColumn.ALIGN_CENTER);
//		return checkbox(null, FIELD_TRIPLEBOX, width, BOX_NAME, checkedField, null, null);
		return gridTriplebox(checkedField, width);
	}
	
	/**
	 * 构建一个CHECKBOX类型的GridColumn
	 * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
	 * @param field String 返回前端的属性名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @param boxName box提交时的名字
	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
	 * @param required 是否必填提交时校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public static GridColumn checkbox(String beanProp, String field, String width, String boxName, String checkedField, Boolean required){
//		return new GridColumn( beanProp, field, null, width).setBox(new Checkbox(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)).setField(new BoxField().setValue(field).setChecked(checkedField))).setAlign(GridColumn.ALIGN_CENTER);
//		return new GridColumn(beanProp, field, null, width).setGridTriplebox(boxName, checkedField, required, sync);
		return gridTriplebox(beanProp, field, width).setGridTriplebox(boxName, checkedField, required, null);
	}
	
	/**
	 * 构建一个CHECKBOX类型的GridColumn
	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param field String 返回前端的属性名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @param boxName box提交时的名字
	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
	 * @param required 是否必填提交时校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public static GridColumn checkbox(int dataColumnIndex, String field, String width, String boxName, String checkedField, Boolean required){
		return gridTriplebox(dataColumnIndex, field, width).setGridTriplebox(boxName, checkedField, required, null);
	}
	
	/**
	 * 构建一个GridTriplebox类型的GridColumn
	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn gridTriplebox(String checkedField, String width) {
		return new GridColumn(null, null, null, width).setGridTriplebox(BOX_NAME, checkedField, null, null);
	}
	
	/**
	 * 构建一个GridTriplebox类型的GridColumn
	 * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
	 * @param field String 返回前端的属性名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @param
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn gridTriplebox(String beanProp, String field, String width) {
		return new GridColumn(beanProp, field, null, width).setGridTriplebox(BOX_NAME, field, null, null);
	}
	
	/**
	 * 构建一个GridTriplebox类型的GridColumn
	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param field String 返回前端的属性名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @param
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn gridTriplebox(int dataColumnIndex, String field, String width) {
		return new GridColumn(dataColumnIndex, field, null, width).setGridTriplebox(BOX_NAME, field, null, null);
	}
	
//	/**
//	 * 构建一个Triplebox类型的GridColumn
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn triplebox(String checkedField, String width) {
//		return triplebox(null, FIELD_TRIPLEBOX, width, BOX_NAME, checkedField, null);
//	}
//	
//	/**
//	 * 构建一个Triplebox类型的GridColumn
//	 * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn triplebox(String beanProp, String field, String width, String boxName, String checkedField, Boolean required){
//		return new GridColumn(beanProp, field, null, width).setTriplebox(boxName, checkedField, required);
//	}
//	
//	
//	/**
//	 * 构建一个Triplebox类型的GridColumn
//	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn triplebox(int dataColumnIndex, String field, String width, String boxName, String checkedField, Boolean required){
//		return new GridColumn(dataColumnIndex, field, null, width).setTriplebox(boxName, checkedField, required);
//	}
	
	/**
	 * 将此列设置为复选框
	 * @param sync String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_CLICK}
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridTriplebox(String sync) {
		return setGridTriplebox(BOX_NAME, null, null, sync);
	}
	
	/**
	 * 将此列设置为表格复选框
	 * @param boxName String 复选框名称
	 * @param checkedField String 所选值指向的列名
	 * @param required Boolean 是否必填
	 * @param sync String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_FOCUS}
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridTriplebox(String boxName, String checkedField, Boolean required, String sync) {
		GridTriplebox triplebox = new GridTriplebox(boxName, null, null, null, null);
		triplebox.setSync(sync).addValidate(new Validate().setRequired(required));
		return setGridTriplebox(triplebox, checkedField);
	}
	
	/**
	 * 设置公共的复选框
	 * @param triplebox 复选框
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridTriplebox(GridTriplebox triplebox) {
		return setGridTriplebox(triplebox, null);
	}
	/**
	 * 设置公共的复选框
	 * @param triplebox 复选框
	 * @param checkedField String 所选值指向的列名
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridTriplebox(GridTriplebox triplebox, String checkedField) {
		if (triplebox == null) {
			throw new IllegalArgumentException("The triplebox can not be null.");
		}
		String boxField = Utils.notEmpty(checkedField) ?  checkedField : this.field;
		if (Utils.isEmpty(boxField)) {
			throw new IllegalArgumentException("The checkedField can not be null.");
		}
		if (Utils.isEmpty(triplebox.getName())) {
			triplebox.setName(BOX_NAME);
		}
		// 标题全选框
		this.rawFormat = new RawJson(triplebox.setCheckall(true).toString());
		// 置空,省流量
		triplebox.setCheckall(null);
		triplebox.setValue(new RawJson("$" + boxField));
		return this.setFormat("javascript:return " + triplebox).setAlign(GridColumn.ALIGN_CENTER);
	}
	
//	
//	/**
//	 * 将此列设置为复选框
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setTriplebox(Boolean required) {
//		return setTriplebox(BOX_NAME, required);
//	}
//	
//	/**
//	 * 将此列设置为表格复选框
//	 * @param boxName String 复选框名称
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setTriplebox(String boxName, Boolean required) {
//		return setTriplebox(boxName, null, required);
//	}
//	
//	/**
//	 * 将此列设置为表格复选框
//	 * @param boxName String 复选框名称
//	 * @param checkedField String 所选值指向的列名
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setTriplebox(String boxName, String checkedField, Boolean required) {
//		return setCommonTriplebox(boxName, checkedField, required, new Triplebox(null, null, null, null, null));
//	}
	
	/**
	 * 将此列设置为单选框
	 * @param sync String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_CLICK}
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridRadio(String sync) {
		return setGridRadio(BOX_NAME, null, null, sync);
	}
	
//	/**
//	 * 将此列设置为单选框
//	 * @param boxName String 单选框名称
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setGridRadio(String boxName, Boolean required) {
//		return setGridRadio(boxName, null, required, null);
//	}
	
	/**
	 * 将此列设置为单选框
	 * @param boxName String 单选框名称
	 * @param checkedField String 所选值指向的列名
	 * @param required Boolean 是否必填
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridRadio(String boxName, String checkedField, Boolean required, String sync) {
		GridRadio gridRadio = new GridRadio(boxName, null, null, null, null);
		gridRadio.setSync(sync).addCls(sync).addValidate(new Validate().setRequired(required));
		return setGridRadio(gridRadio, checkedField);
	}
	
	/**
	 * 将此列设置为单选框
	 * @param gridRadio GridRadio 单选框
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridRadio(GridRadio gridRadio) {
		return setGridRadio(gridRadio, null);
	}
	/**
	 * 将此列设置为单选框
	 * @param gridRadio GridRadio 单选框
	 * @param checkedField String 所选值指向的列名
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setGridRadio(GridRadio gridRadio, String checkedField) {
		if (gridRadio == null) {
			throw new IllegalArgumentException("The gridRadio can not be null.");
		}
		String boxField = Utils.notEmpty(checkedField) ?  checkedField : this.field;
		if (Utils.isEmpty(boxField)) {
			throw new IllegalArgumentException("The checkedField can not be null.");
		}
		if (Utils.isEmpty(gridRadio.getName())) {
			gridRadio.setName(BOX_NAME);
		}
		return this.setFormat("javascript:return " + gridRadio.setValue(new RawJson("$" + boxField))).setAlign(GridColumn.ALIGN_CENTER);
	}
	
//	/**
//	 * 将此列设置为单选框
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setRadio(Boolean required) {
//		return setRadio(BOX_NAME, required);
//	}
//	
//	/**
//	 * 将此列设置为单选框
//	 * @param boxName String 单选框名称
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setRadio(String boxName, Boolean required) {
//		return setRadio(boxName, null, required, null);
//	}
//	
//	/**
//	 * 将此列设置为单选框
//	 * @param boxName String 单选框名称
//	 * @param checkedField String 所选值指向的列名
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setRadio(String boxName, String checkedField, Boolean required, String sync) {
//		return setCommonRadio(boxName, checkedField, required, new Radio(null, null, null, null, null).setSync(sync));
//	}
	
//	/**
//	 * 构建一个CHECKBOX类型的GridColumn
//	 * @param checkedField String 所选中值指定的列名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn radio(String checkedField, String width){
////		return new GridColumn(null, field, null, width).setModel(new Radio(CHECK_FIELD_NAME, null, null, null,null), "value").setAlign(GridColumn.ALIGN_CENTER);
//		return radio(null, FIELD_RADIO, width, BOX_NAME, checkedField, null);
//	}
//	
//	/**
//	 * 构建一个RADIO类型的GridColumn
//	 * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn radio(String beanProp,String field, String width,String boxName,String checkedField,Boolean required){
////		return new GridColumn(beanProp, field, null, width).setModel(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)), "value").setAlign(GridColumn.ALIGN_CENTER);
//		return new GridColumn(beanProp, field, null, width).setRadio(boxName, checkedField, required);
//	}
//	/**
//	 * 构建一个RADIO类型的GridColumn
//	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn radio(int dataColumnIndex,String field, String width,String boxName,String checkedField,Boolean required){
////		return new GridColumn( dataColumnIndex, field, null, width).setBox(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)).setField(new BoxField().setValue(field).setChecked(checkedField))).setAlign(GridColumn.ALIGN_CENTER);
//		return new GridColumn(dataColumnIndex, field, null, width).setRadio(boxName, checkedField, required);
//	}
	
	/**
	 * 构建一个CHECKBOX类型的GridColumn
	 * @param checkedField String 所选中值指定的列名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn gridRadio(String checkedField, String width){
//		return new GridColumn(null, field, null, width).setModel(new Radio(CHECK_FIELD_NAME, null, null, null,null), "value").setAlign(GridColumn.ALIGN_CENTER);
		return gridRadio(checkedField, width, null);
	}
	
	/**
	 * 构建一个CHECKBOX类型的GridColumn
	 * @param checkedField String 所选中值指定的列名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn gridRadio(String checkedField, String width, String sync){
		return gridRadio(null, null, width, BOX_NAME, checkedField, null, sync);
	}
	
	/**
	 * 构建一个RADIO类型的GridColumn
	 * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
	 * @param field String 返回前端的属性名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @param boxName box提交时的名字
	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
	 * @param required 是否必填提交时校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn gridRadio(String beanProp,String field, String width,String boxName,String checkedField,Boolean required, String sync){
//		return new GridColumn(beanProp, field, null, width).setModel(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)), "value").setAlign(GridColumn.ALIGN_CENTER);
		return new GridColumn(beanProp, field, null, width).setGridRadio(boxName, checkedField, required, sync);
	}
	/**
	 * 构建一个RADIO类型的GridColumn
	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param field String 返回前端的属性名
	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
	 * @param boxName box提交时的名字
	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
	 * @param required 是否必填提交时校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static GridColumn gridRadio(int dataColumnIndex,String field, String width,String boxName,String checkedField,Boolean required, String sync){
//		return new GridColumn( dataColumnIndex, field, null, width).setBox(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)).setField(new BoxField().setValue(field).setChecked(checkedField))).setAlign(GridColumn.ALIGN_CENTER);
		return new GridColumn(dataColumnIndex, field, null, width).setGridRadio(boxName, checkedField, required, sync);
	}

	/**
	 * 如果数据是List&lt;JavaBean&gt;则这里表示这个JavaBean的属性名。该列取这个属性的值
	 * @return the beanProp
	 */
	@Transient
	public String getBeanProp() {
		return beanProp;
	}

	/**
	 * <p>设置属性名</p>
	 * <p>如果数据是List&lt;JavaBean&gt;则这里表示这个JavaBean的属性名。该列取这个属性的值</p>
	 * @param beanProp 属性名
	 * @return  this
	 */
	public GridColumn setBeanProp(String beanProp) {
		this.beanProp = beanProp;
		return this;
	}


	/**
	 * 取得 该列在JSON中的属性名
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * 设置 该列在JSON中的属性名
	 * @param field 该列在JSON中的属性名
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 一般来说grid名称指定以后，相关的操作如tr里面会按这个名字放置内容，所以，一般不会在运行期改变这个值。
	 */
	public GridColumn setField(String field) {
		this.field = field;
		return this;
	}


	/**
	 * 标题(一般设置在表头上)
	 * @return the label
	 */
	@Transient
	public String getLabel() {
		return label;
	}

	/**
	 * 标题(一般设置在表头上)
	 * @param label the label to set
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setLabel(String label) {
		this.label = label;
		return this;
	}
	/**
	 * 标题(一般设置在表头上)
	 * @param label the label to set
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated setLabel
	 * @see #setLabel(String)
	 */
	@Deprecated
	public GridColumn setTitle(String label) {
		this.label = label;
		return this;
	}

	/**
	 * 宽度 支持 数字(单位：像素) 百分比或*
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * 宽度 支持 数字(单位：像素) 百分比或*
	 * @param width the width to set
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setWidth(String width) {
		this.width = width;
		return this;
	}


	/**
	 * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
	 * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
	 * 典型的有两种写法
	 * <pre>
	 * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
	 * </pre>或<pre>
	 * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
	 * </pre>
	 * 
	 * @return String
	 */
	public Object getFormat() {
		return format;
	}

	/**
	 * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
	 * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
	 * 典型的有两种写法
	 * <pre>
	 * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
	 * </pre>或<pre>
	 * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
	 * </pre>
	 * @param format String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setFormat(String format) {
		this.format = format;
		return this;
	}
	
	/**
	 * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
	 * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
	 * 典型的有两种写法
	 * <pre>
	 * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
	 * </pre>或<pre>
	 * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
	 * </pre>
	 * @param format String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setFormat(JSFunction format) {
		this.format = format;
		return this;
	}



	/**
	 * 排序状态。设置此参数表示当前列可点击排序。可选值为三个: {@link #SORT_DEFAULT}, {@link #SORT_ASC}, {@link #SORT_DESC}
	 * @return Object
	 */
	public Object getSort() {
		return sort;
	}

	/**
	 * 设置列排序
	 * @param sort Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setSort(Boolean sort) {
		this.sort = sort;
		return this;
	}
	
	/**
	 * 设置列排序
	 * @param sort Sort
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setSort(Sort sort) {
		this.sort = sort;
		return this;
	}

	/**
	 * int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @return the dataColumnIndex
	 */
	@Transient
	public int getDataColumnIndex() {
		return dataColumnIndex;
	}
	/**
	 * int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
	 * @param dataColumnIndex int
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setDataColumnIndex(int dataColumnIndex) {
		this.dataColumnIndex = dataColumnIndex;
		return this;
	}

	public String getType() {
		return null;
	}

	/**
	 * CSS样式
	 * @return String
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * CSS样式
	 * @param style String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setStyle(String style) {
		this.style = style;
		return this;
	}
	/**
	 * CSS样式类名
	 * @return String
	 */
	public String getCls() {
		return cls;
	}
	/**
	 * CSS样式类名
	 * @param cls String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setCls(String cls) {
		this.cls = cls;
		return this;
	}
	/**
	 * 水平居中。可用值: left,right,center
	 * @return align
	 * @see #ALIGN_LEFT
	 * @see #ALIGN_RIGHT
	 * @see #ALIGN_CENTER
	 */
	public String getAlign() {
		return align;
	}
	/**
	 * 水平居中。可用值: left,right,center
	 * @param align String 
	 * @return 本身，这样可以继续设置其他属性
	 * @see #ALIGN_LEFT
	 * @see #ALIGN_RIGHT
	 * @see #ALIGN_CENTER
	 */
	public GridColumn setAlign(String align) {
		this.align = align;
		return this;
	}
	/**
	 * 垂直对齐。可选值: top,middle,bottom
	 * @return valign String
	 * @see #VALIGN_TOP
	 * @see #VALIGN_MIDDLE
	 * @see #VALIGN_BOTTOM
	 * 
	 */
	public String getValign() {
		return valign;
	}
	/**
	 * 垂直对齐。可选值: top,middle,bottom
	 * @param valign String
	 * @return 本身，这样可以继续设置其他属性
	 * @see #VALIGN_TOP
	 * @see #VALIGN_MIDDLE
	 * @see #VALIGN_BOTTOM
	 */
	public GridColumn setValign(String valign) {
		this.valign = valign;
		return this;
	}
	/**
	 * 格式化内容
	 * @return dataFormat
	 */
	@Transient
	public String getDataFormat() {
		return dataFormat;
	}
	/**
	 * 格式化内容
	 * @param dataFormat
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
		return this;
	}
	/**
	 * 是否可见的。如果是false表示当前GridColumn是隐藏字段，不输出给javascript前端。仅仅用于JAVA后端用于方便获取属性用，起到方便开发的作用
	 * @return boolean
	 */
	@Transient
	public boolean isVisable() {
		return this.width!=null&&!this.width.equals("");
	}
	/**
	 * 选项表单，类型是 checkbox 或 radio。
	 * @return box
	 */
	@Deprecated
	public AbstractBox<?> getBox() {
		throw new UnsupportedOperationException("");
	}
	/**
	 * 选项表单，类型是 checkbox 或 radio。
	 * @param box AbstractBox
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setBox(AbstractBox<?> box) {
		if (box == null) {
			this.setFormat("");
			return this;
		}
		Boolean required = null;
		if (box.getValidate() != null) {
			required = box.getValidate().getRequired();
		}
//		if (box instanceof Triplebox) {
//			setTriplebox(box.getName(), required);
//		} else if (box instanceof Radio) {
//			setRadio(box.getName(), required);
//		} else
		if (box instanceof Triplebox || box instanceof Checkbox) {
			setGridTriplebox(box.getName(), null, required, box.getSync());
		} else if (box instanceof Radio) {
			setGridRadio(box.getName(), null, required, box.getSync());
		} else {
			throw new IllegalArgumentException("The box must be Triplebox or Radio");
		}
		return this;
	}
	
	/**
	 * 排序结果的 url
	 * @return String
	 */
	@Deprecated
	public String getSortsrc() {
		if (sort != null && sort instanceof Sort) {
			return ((Sort) sort).getSrc();
		}
		return null;
	}
	
	/**
	 * 排序结果的 url
	 * @param sortsrc
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public GridColumn setSortsrc(String sortsrc) {
		this.sort = new Sort().setSrc(sortsrc);
		return this;
	}
	
	/**
	 * 提示的字段
	 * @return String
	 */
	@Deprecated
	public String getTipfield() {
		if (tip != null && tip instanceof Tip) {
			return ((Tip) tip).getField();
		}
		return null;
	}
	
	/**
	 * 提示的字段
	 * @param tipfield
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public GridColumn setTipfield(String tipfield) {
		return setTip(new Tip().setField(tipfield));
	}
	
	/**
	 * 提示的字段
	 * @param tip Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setTip(Boolean tip) {
		this.tip = tip;
		return this;
	}
	
	/**
	 * 提示的字段
	 * @param tip Tip
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setTip(Tip tip) {
		this.tip = tip;
		return this;
	}
	
	/**
	 * 提示的字段
	 * @return Object
	 */
	public Object getTip() {
		return tip;
	}
	
	/**
	 * 列的最小宽度
	 * @return Integer
	 */
	public Integer getMinwidth() {
		return minwidth;
	}
	
	/**
	 * 列的最小宽度
	 * @param minwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setMinwidth(Integer minwidth) {
		this.minwidth = minwidth;
		return this;
	}
	
	/**
	 * 列的最大宽度
	 * @return Integer
	 */
	public Integer getMaxwidth() {
		return maxwidth;
	}
	
	/**
	 * 列的最大宽度
	 * @param maxwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setMaxwidth(Integer maxwidth) {
		this.maxwidth = maxwidth;
		return this;
	}
	/**
	 * 设置高亮效果
	 * @return Highlight
	 */
	public Highlight getHighlight() {
		return highlight;
	}
	
	/**
	 * 设置高亮效果
	 * @param highlight 高亮效果
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridColumn setHighlight(Highlight highlight) {
		this.highlight = highlight;
		return this;
	}

}
