package com.rongji.dfish.ui.layout;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.auxiliary.TD;
import com.rongji.dfish.ui.auxiliary.TR;
import com.rongji.dfish.ui.auxiliary.TableRowNum;
import com.rongji.dfish.ui.widget.Toggle;

import java.beans.Transient;
import java.util.*;

/**
 * TableFactory 用于快速生成一个Table
 * TablePanel 在DFish2.x的版本是一个很实用的工具。
 * 他为 表格模型封装类。
 * <p>为了延续DFish2.x 易用的特点点，保留TablePanel类。这个类不是DFish3.x系类的原型类，它仅仅提供是一个看起来是表格布局的控件。
 * 他能够简单快速的产生一个表格，极大的减少编码的数量。</p>
 * <pre style='border:1px black solid;border-left:0px;border-right:0px;background-color:#CCC'>
 * TablePanel grid=new TablePanel("f_grid");
 * Object[][] data={
 *   {"USD","美元"},
 *   {"CNY","人民币"},
 *   {"HKD","港币"},
 * };
 * grid.setData(Arrays.asList(data));
 * grid.addColumn(TableColumn.checkbox("id", "40").setAlign(Align.center));
 * grid.addColumn(TableColumn.text(0,"id","编号","120"));
 * grid.addColumn(TableColumn.text(1,"n","名称","*"));
 * </pre>
 * <p>但3.0 作为封装类，如果原型被改变，将会变的很难理解。</p>
 * <p>所以，5.0 独立出来，自行调用build 方法。获取Table。grid除了id和cls属性可以设置外，其他都是build以后设置。
 * 注意每次build都是一个独立实例，并无关联。
 * 如果有模板的话，可以使用cls结合前端模板实现统一风格的目的。
 * 否则，需要些一个方法设置grid 关于风格的参数。</p>
 *
 * @author DFish Team
 * @since dfish 2.0
 */
public class TableFactory {
    public static DefaultTableFactory newTable(String id) {
        return new DefaultTableFactory(id);
    }

    public static GroupedTableFactory newGroupedTable(String id) {
        return new GroupedTableFactory(id);
    }

    protected static abstract class AbstractTableFactory<T extends AbstractTableFactory<T>> {
        protected String id;
        protected String cls;
        protected boolean hasTableHead;
        protected List<Column> columns = new ArrayList<>();

        protected static final int MODE_ARRAY = 2;
        protected static final int MODE_BEAN = 1;
        protected static final int MODE_UNDEFINED = 0;
        protected int dataMode = MODE_UNDEFINED;

        public AbstractTableFactory(String id) {
            this.id = id;
            this.hasTableHead = true;
        }

        public String getId() {
            return id;
        }

        public T setId(String id) {
            this.id = id;
            return (T) this;
        }

        public String getCls() {
            return cls;
        }

        public T setCls(String cls) {
            this.cls = cls;
            return (T) this;
        }

        /**
         * 是否有表头配置
         *
         * @param hasTableHead boolean 是否有表头
         * @return 本身，这样可以继续设置其他属性
         */
        public T setHasTableHead(boolean hasTableHead) {
            this.hasTableHead = hasTableHead;
            return (T) this;
        }

        public boolean isHasTableHead(boolean hasTableHead) {
            return hasTableHead;
        }

        public List<Column> getColumns() {
            return columns;
        }

        public T addColumn(Column column) {
            if (column == null) {
                return (T) this;
            }
            checkMode(column);
            columns.add(column);
            return (T) this;
        }

        public abstract Table build();

        /**
         * 获取属性信息，支持bean中类反射获取，或者是从Map中获取(主要支持JdbcTemplate的List&lt;Map&lt;String,Object&gt;&gt;)
         *
         * @param item Object
         * @param prop String 支持点号(.)分隔的多级属性
         * @return Object
         */
        private static Object getProperty(Object item, String prop) {
            if (item == null || prop == null || "".equals(prop)) {
                return null;
            }
            try {
                return BeanUtil.getProperty(item, prop);
            } catch (Exception e) {
                LogUtil.error(TableFactory.class, null, e);
                return null;
            }
        }

        protected Object getColumnValue(Object data, Column gc) {
            Object value = null;
            if (data instanceof Object[] && gc.getDataColumnIndex() >= 0) {
                int length = ((Object[]) data).length;
                if (length <= gc.getDataColumnIndex()) {
                    throw new ArrayIndexOutOfBoundsException(gc.getDataColumnIndex() + "\r\n in TableColumn " + gc);
                } else {
                    value = ((Object[]) data)[gc.getDataColumnIndex()];
                }
            } else if (gc.getBeanProp() != null) {
                value = getProperty(data, gc.getBeanProp());
            }
            String dataFormat = gc.getDataFormat();
            if (Utils.notEmpty(dataFormat)) {
                value = StringUtil.format(value, dataFormat);
            }
            return value;
        }

        protected void checkMode(Column column) {
            int currMode = MODE_UNDEFINED;
            if (column.getDataColumnIndex() >= 0) { // 数组模式
                currMode = MODE_ARRAY;
            } else if (Utils.notEmpty(column.getBeanProp())) {//Bean模式
                currMode = MODE_BEAN;
                //部分是用format写的特效，可以没有这种判断
            }
            if (currMode != MODE_UNDEFINED) {
                if (dataMode == MODE_UNDEFINED) {
                    dataMode = currMode;
                } else if (dataMode != currMode) {
                    throw new UnsupportedOperationException("data mode should be same");
                }
            }
            //FIXME 如果field重复则报异常
        }

    }

    public static class DefaultTableFactory extends AbstractTableFactory<DefaultTableFactory> {
        private Collection<?> bodyData;

        public DefaultTableFactory(String id) {
            super(id);
        }

        /**
         * 设置表格数据
         *
         * @param bodyData Collection
         * @return 本身，这样可以继续设置其他属性
         */
        public DefaultTableFactory setBodyData(Collection<?> bodyData) {
            this.bodyData = bodyData;
            return this;
        }

        /**
         * 表格数据
         *
         * @return Collection
         */
        public Collection<?> getBodyData() {
            return bodyData;
        }

        @Override
        public Table build() {
            Table prototype = new Table(super.getId());
            TR headRow = null;
            if (hasTableHead) {
                headRow = new TR();
                prototype.getTHead().add(headRow);
            }
            for (Column column : columns) {
                if (column.getWidth() != null) {//隐藏的字段不显示
                    prototype.addColumn(column);
                }
                if (hasTableHead) {
                    // 需要填充thead
                    Object columnData = null;
                    if (column.rawFormat() != null) {
                        columnData = column.rawFormat();
                    } else {
                        columnData = column.getLabel();
                    }
                    headRow.putData(column.getField(), columnData);
                }
            }

            if (bodyData != null) {
                // 假定这个集合所有对象的类型是一致的
//                int index = 0;
                for (Object data : bodyData) {
                    TR dataRow = new TR();
                    prototype.add(dataRow);
                    if (data == null) {
                        continue;
                    }
                    for (Column gc : columns) {
                        dataRow.putData(gc.getField(), getColumnValue(data, gc));
                    }
//                    index++;
                }
            }
            return prototype;
        }


    }

    public static class GroupedTableFactory extends AbstractTableFactory<GroupedTableFactory> {
        private LinkedHashMap<String, Collection<?>> col = new LinkedHashMap<>();

        public GroupedTableFactory(String id) {
            super(id);
        }

        /**
         * 设置表格数据
         *
         * @param label 标签
         * @param col   具体数据
         * @return 本身，这样可以继续设置其他属性
         */
        public GroupedTableFactory addTableData(String label, Collection<?> col) {
            this.col.put(label, col);
            return this;
        }

        @Override
        public Table build() {
            Table prototype = new Table(id);
            // 将原型中body下的所有数据结点清空
            //  需要填充thead
            int visableColumnCount = 0;//可见的布局行数
            String firstColumnField = null;
            for (Column column : columns) {
                if (column.getWidth() != null) {//隐藏的字段不显示
                    prototype.addColumn(column);
                }
                if (hasTableHead) {
                    TR headRow = new TR();
                    prototype.getTHead().add(headRow);
                    headRow.putData(column.getField(), column.getLabel());
                }
                if (column.isVisible()) {
                    if (firstColumnField == null) {
                        firstColumnField = column.getField();
                    }
                    visableColumnCount++;
                }
            }

            if (Utils.isEmpty(col)) {
                return prototype;
            }
            // 假定这个集合所有对象的类型是一致的
            for (Map.Entry<String, Collection<?>> entry : col.entrySet()) {
                //添加可折叠的标题栏
                TR tr = new TR();
                prototype.add(tr);
                //FIXME 这里应该不是0而是第0列的propName
                tr.putData(firstColumnField, new TD().setColSpan(visableColumnCount).setNode(new Toggle().setText(entry.getKey()).setHr(true).setExpanded(true)));

                for (Object data : entry.getValue()) {
                    TR dataRow = new TR();
                    prototype.add(dataRow);
                    if (data == null) {
                        continue;
                    }
                    for (Column gc : columns) {
                        dataRow.putData(gc.getField(), getColumnValue(data, gc));
                    }
                }
            }
            return prototype;
        }
    }

    /**
     * 扩展的Column 包含如何取数据，如何快速设置表头。
     * 数据格式等。这些将不输出。
     */
    public static class Column extends com.rongji.dfish.ui.auxiliary.Column {

        /**
         * 如果数据是List&lt;JavaBean&gt;则这里表示这个JavaBean的属性名。该列取这个属性的值
         */
        private String beanProp;
        private int dataColumnIndex = -1;
        private String dataFormat;
        private String label;

        /**
         * 如果数据是List&lt;JavaBean&gt;则这里表示这个JavaBean的属性名。该列取这个属性的值
         *
         * @return
         */
        @Transient
        public String getBeanProp() {
            return beanProp;
        }

        /**
         * 如果数据是List&lt;JavaBean&gt;则这里表示这个JavaBean的属性名。该列取这个属性的值
         *
         * @param beanProp 属性名
         * @return this
         */
        public Column setBeanProp(String beanProp) {
            this.beanProp = beanProp;
            return this;
        }

        /**
         * int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         *
         * @return
         */
        @Transient
        public int getDataColumnIndex() {
            return dataColumnIndex;
        }

        /**
         * int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         *
         * @param dataColumnIndex int
         * @return this
         */
        public Column setDataColumnIndex(int dataColumnIndex) {
            this.dataColumnIndex = dataColumnIndex;
            return this;
        }


        /**
         * 格式化内容 时间则是 类似于yyyy-MM-dd HH:mm 数字则类似于#.00
         * 详见SimpleDateFormat与NumberFormat
         *
         * @return String
         * @see java.text.SimpleDateFormat
         * @see java.text.NumberFormat
         */
        @Transient
        public String getDataFormat() {
            return dataFormat;
        }

        /**
         * 格式化内容 时间则是 类似于yyyy-MM-dd HH:mm 数字则类似于#.00
         * 详见SimpleDateFormat与NumberFormat
         *
         * @param dataFormat String
         * @return this
         * @see java.text.SimpleDateFormat
         * @see java.text.NumberFormat
         */
        public Column setDataFormat(String dataFormat) {
            this.dataFormat = dataFormat;
            return this;
        }

        /**
         * 标题(一般设置在表头上)
         *
         * @return
         */
        @Transient
        public String getLabel() {
            return label;
        }

        /**
         * 标题(一般设置在表头上)
         *
         * @param label the label to set
         * @return this
         */
        public Column setLabel(String label) {
            this.label = label;
            return this;
        }

        /**
         * <p>构造函数</p>
         * <p>这个构造函数适用与数据是List&lt;Object[]&gt;的时候.</p>
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 输出时显示的JSON属性名字。注意不要有重复
         * @param label           String 在表头显示的标题
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         */
        public Column(int dataColumnIndex, String field, String label, String width) {
            this.dataColumnIndex = dataColumnIndex;
            this.label = label;
            this.setField(field);
            this.setWidth(width);
        }

        /**
         * <p>构造函数</p>
         * <p>这个构造函数适用与数据是List&lt;Object[]&gt;的时候.</p>
         *
         * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 输出时显示的JSON属性名字。注意不要有重复
         * @param label    String 在表头显示的标题
         * @param width    String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         */
        public Column(String beanProp, String field, String label, String width) {
            this.beanProp = beanProp;
            this.label = label;
            this.setField(field);
            this.setWidth(width);
        }

        /**
         * 构建一个TEXT类型的TableColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 输出时显示的JSON属性名字。注意不要有重复
         * @param label           String 在表头显示的标题
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(int dataColumnIndex, String field, String label, String width) {
            return new Column(dataColumnIndex, field, label, width);
        }

        /**
         * 构建一个TEXT类型的TableColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param label           String 在表头显示的标题
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(int dataColumnIndex, String label, String width) {
            return new Column(dataColumnIndex, null, label, width);
        }

        /**
         * 构建一个TEXT类型的TableColumn
         *
         * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 输出时显示的JSON属性名字。注意不要有重复
         * @param label    String 在表头显示的标题
         * @param width    String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(String beanProp, String field, String label, String width) {
            return new Column(beanProp, field, label, width);
        }

        /**
         * 构建一个TEXT类型的TableColumn 在TableLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
         * 这个一般在TableLayout原型的时候使用。
         *
         * @param field String 输出时显示的JSON属性名字。注意不要有重复
         * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(String field, String width) {
            return new Column(null, field, null, width);
        }

        /**
         * 构建一个TEXT类型的TableColumn 在TableLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
         *
         * @param field  String 输出时显示的JSON属性名字。注意不要有重复
         * @param width  String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param format 文本格式转化
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(String field, String width, String format) {
            return (Column) new Column(null, field, null, width).setFormat(format);
        }

        /**
         * 构建一个HIDDEN类型的TableColumn
         *
         * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 输出时显示的JSON属性名字。注意不要有重复
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column hidden(String beanProp, String field) {
            return new Column(beanProp, field, null, null);
        }

        /**
         * 构建一个HIDDEN类型的TableColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 输出时显示的JSON属性名字。注意不要有重复
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column hidden(int dataColumnIndex, String field) {
            return new Column(dataColumnIndex, field, null, null);
        }

        /**
         * 构建一个序号列
         *
         * @param label 标签
         * @param width 宽度
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column rowNum(String label, String width) {
            return rowNum(label, null, width);
        }

        /**
         * 构建一个序号列
         *
         * @param label 标签
         * @param start 开始编号
         * @param width 宽度
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column rowNum(String label, Integer start, String width) {
            return (Column) new Column(null, null, label, width).setFormat("javascript:return " + new TableRowNum().setStart(start)).setAlign(com.rongji.dfish.ui.auxiliary.Column.ALIGN_CENTER);
        }


        /**
         * 构建一个TableTriplebox类型的TableColumn
         *
         * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column tripleBox(String checkedField, String width) {
            return (Column) new Column(null, null, null, width).setTripleBox(BOX_NAME, checkedField, null, null);
        }

        /**
         * 构建一个TripleBox类型的TableColumn
         *
         * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 返回前端的属性名
         * @param width    String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column tripleBox(String beanProp, String field, String width) {
            return (Column) new Column(beanProp, field, null, width).setTripleBox(BOX_NAME, field, null, null);
        }

        /**
         * 构建一个TripleBox类型的TableColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 返回前端的属性名
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column tripleBox(int dataColumnIndex, String field, String width) {
            return (Column) new Column(dataColumnIndex, field, null, width).setTripleBox(BOX_NAME, field, null, null);
        }


        /**
         * 构建一个CHECKBOX类型的TableColumn
         *
         * @param checkedField String 所选中值指定的列名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column radio(String checkedField, String width) {
//		return new TableColumn(null, field, null, width).setModel(new Radio(CHECK_FIELD_NAME, null, null, null,null), "value").setAlign(TableColumn.ALIGN_CENTER);
            return radio(checkedField, width, null);
        }

        /**
         * 构建一个CHECKBOX类型的TableColumn
         *
         * @param checkedField String 所选中值指定的列名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param sync         是否与行点击动作同步
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column radio(String checkedField, String width, String sync) {
            return radio(null, null, width, BOX_NAME, checkedField, null, sync);
        }

        /**
         * 构建一个RADIO类型的TableColumn
         *
         * @param beanProp     String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field        String 返回前端的属性名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param boxName      box提交时的名字
         * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param required     是否必填提交时校验
         * @param sync         是否与行点击动作同步
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column radio(String beanProp, String field, String width, String boxName, String checkedField, Boolean required, String sync) {
            return (Column) new Column(beanProp, field, null, width).setRadio(boxName, checkedField, required, sync);
        }

        /**
         * 构建一个RADIO类型的TableColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 返回前端的属性名
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param boxName         box提交时的名字
         * @param checkedField    如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param required        是否必填提交时校验
         * @param sync            是否与行点击动作同步
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column radio(int dataColumnIndex, String field, String width, String boxName, String checkedField, Boolean required, String sync) {
            return (Column) new Column(dataColumnIndex, field, null, width).setRadio(boxName, checkedField, required, sync);
        }

    }
}
