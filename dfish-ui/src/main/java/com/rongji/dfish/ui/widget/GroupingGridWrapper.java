package com.rongji.dfish.ui.widget;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.layout.grid.Tr;

/**
 * 表格模型封装类
 * @author DFish Team
 *
 */
public class GroupingGridWrapper extends AbstractGridWrapper<GroupingGridWrapper> {
	private static final long serialVersionUID = -1072678745892724553L;

	/**
	 * 表格模型构造方法,表格模型编号
	 * @param id 表格编号
	 */
	public GroupingGridWrapper(String id) {
		super(id);
		setFace(FACE_LINE);
	}
	
	private LinkedHashMap<String,Collection<?>>  col = new LinkedHashMap<String,Collection<?>>();
	
    /**
     * 设置表格数据
     * @param label 标签
     * @param col 具体数据
     * @return 本身，这样可以继续设置其他属性
     */ 
    public GroupingGridWrapper addGridData(String label, Collection<?> col) {
    	this.col.put(label, col);
    	this.checkConcurrentModify();
    	return this;
    }

	/**
     * 初始化原型结点
     */
	@Override
    protected void buildPrototype() {
       	// 将原型中body下的所有数据结点清空
       	//  需要填充thead
    	int visableColumnCount=0;//可见的布局行数
    	String firstColumnField=null;
    	for(GridColumn gridColumn:columns){
    		if(gridColumn.getWidth()!=null){//隐藏的字段不显示
    			prototype.addColumn(gridColumn);
    		}
    		if(hasTableHead){
				Tr headRow = new Tr();
				prototype.getThead().add(headRow);
				headRow.setData(gridColumn.getField(), gridColumn.getLabel());
			}
    		if(gridColumn.isVisable()){
    			if(firstColumnField==null){
    				firstColumnField=gridColumn.getField();
    			}
    			visableColumnCount++;
    		}
    	}
    	
    	if (Utils.isEmpty(col)) {
    		return;
    	}
    	// 假定这个集合所有对象的类型是一致的
      	for(Map.Entry<String, Collection<?>>entry:col.entrySet()){
    		//添加可折叠的标题栏
    		Tr tr = new Tr();
    		prototype.add(tr);
    		//FIXME 这里应该不是0而是第0列的propName
    		tr.setData(firstColumnField, new Td().setColspan(visableColumnCount).setNode(new Toggle().setText(entry.getKey()).setHr(true).setOpen(true)));
     
	    	for (Object data : entry.getValue()) {
				Tr dataRow = new Tr();
				prototype.add(dataRow);
				if (data == null) {
					continue;
				}
				for(GridColumn gc:columns){
					Object value=null;
					if(data instanceof Object[] &&gc.getDataColumnIndex()>=0){
						value=((Object[])data)[gc.getDataColumnIndex()];
					}else if(gc.getBeanProp()!=null){
						value= getProperty(data, gc.getBeanProp());
					}
					String dataFormat = gc.getDataFormat();
					if (Utils.notEmpty(dataFormat)) {
						value = StringUtil.format(value, dataFormat);
					}
					dataRow.setData(gc.getField(), value);
				}
    		}
    	}
    }


	
}
