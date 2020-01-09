package com.rongji.dfish.ui.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.helper.JsonTd;

/**
 * 抽象的GridLayout 它有两个实例，一个是底层的JsonGridLayout 仅作吻合前端JSON要求的最低的封装，必要的getter和setter
 * 另一个是常用的GridLayout 可以指定某个位置直接增加内容。
 * @author DFish team
 *
 */
public class GridPart extends AbstractLayout<GridPart, Tr> implements GridOper<GridPart> {
/**
	 * 
	 */
	private static final long serialVersionUID = -8651836374729282552L;

	//	protected UnlimitedGridHelper ugh;
//	private Boolean escape;
	/**
	 * 构造函数
	 * @param id String
	 */
	public GridPart(String id) {
		super(id);
	}
	/**
	 * 默认构造函数
	 */
	public GridPart() {
		super(null);
	}
	
	protected Grid owner;

	public Grid owner() {
		return owner;
	}

	public GridPart owner(Grid owner) {
		this.owner = owner;
		return  this;
	}

	/**
	 * 取得行
	 * @return List
	 */
	public List<Tr> getRows(){
		return nodes;
	}

	
	@Override
	public GridPart add(Tr w) {
		w.owner(owner);
		return super.add(w);
	}

	@Override
    public GridPart add(int row, int column, Object o){
		put(row,column,o,false);
		return this;
	}
	
	private void put(int row,int column,Object o,boolean overwrite){
		if(o!=null&&o instanceof AbstractTd){
			int toColumn=column;
			int toRow=row;
			//防止加了如一个大小大于大于Grid 
			AbstractTd<?> cast=(AbstractTd<?>)o;
			if(cast.getColspan()!=null&&cast.getColspan()>1){
				toColumn +=cast.getColspan()-1;
			}
			if(cast.getRowspan()!=null&&cast.getRowspan()>1){
				toRow +=cast.getRowspan()-1;
			}
			put(row, column,toRow, toColumn, o,overwrite);
		}else{
			put(row, column,row, column, o,overwrite);
		}
	}
	
	private void put(int fromRow, int fromColumn, int toRow, int toColumn, Object o, boolean overwrite) {
		if(owner==null){
			throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in GridLayout.");
		}
		if (fromRow < 0 || fromColumn < 0 || toRow < 0 || toColumn < 0) { // 行列小于0,抛异常
			throw new IndexOutOfBoundsException("Poisition: ("+Math.min(fromRow,toRow)+","+Math.min(fromColumn,toColumn)+")");
		}
		if (fromRow > toRow) {
	    	int temp = fromRow;
	    	fromRow = toRow;
	    	toRow = temp;
	    }
	    if (fromColumn > toColumn) {
	    	int temp = fromColumn;
	    	fromColumn = toColumn;
	    	toColumn = temp;
	    }
	    if (o == null) {
	    	removeNode(fromRow, fromColumn, toRow, toColumn);
	    	return;
	    }
	    if(overwrite){
	    	removeNode(fromRow, fromColumn, toRow, toColumn);
	    }else{
	    	// 判定是否有重复，如果有抛出异常
	    	if(containsNode(fromRow, fromColumn, toRow, toColumn)){
	    		throw new UnsupportedOperationException("The position is occupied:["+fromRow+","+fromColumn+","+toRow+","+ toColumn+"]"  );
	    	}
	    }
	    while(toRow>=nodes.size()){
	    	nodes.add(new Tr().owner(owner));
	    }
	    Map<Integer,String> columnMap=new HashMap<Integer,String>();
		int column=0;
		List<GridColumn> columns = owner.getColumns(); 
		// 获取当前已设定的列
		if (columns != null) {
			for(GridColumn c:columns){
				if(c.isVisable()){
					columnMap.put( column++,c.getField());
				}
			}
		}

	    while(toColumn>=columnMap.size()){
	    	GridColumn gridColumn = GridColumn.text(null, WIDTH_REMAIN);
	    	owner.addColumn(gridColumn);
	    	columnMap.put(columnMap.size(), gridColumn.getField());
	    }

		int rowspan=toRow-fromRow+1;
		int colspan=toColumn-fromColumn+1;
	    if(o instanceof AbstractTd){
			//如果是entry是GridCell就直接使用GridCell但，GridCell的rowspan和
			//colspan 会重新计算。GridCell本身是什么模式就是什么模式
	    	AbstractTd<?> cell=(AbstractTd<?>)o;
			if(rowspan>1){
				cell.setRowspan(rowspan);
			}
			if(colspan>1){
				cell.setColspan(colspan);
			}
			nodes.get(fromRow).setData(columnMap.get(fromColumn), cell);
		}else if(o instanceof Widget){
			//如果entry 是 Widget 那么将会包装在一个GridCell里面
			if(rowspan>1||colspan>1){
				AbstractTd<?> cell=new Td();
				cell.setNode((Widget<?>)o);
				if(rowspan>1){
					cell.setRowspan(rowspan);
				}
				if(colspan>1){
					cell.setColspan(colspan);
				}
				nodes.get(fromRow).setData(columnMap.get(fromColumn), cell);
			}else{
				nodes.get(fromRow).setData(columnMap.get(fromColumn), o);
			}
	    }else{
	    	if (o != null && (rowspan>1||colspan>1)) {
	    		JsonTd td = new JsonTd();
	    		td.setText(o.toString());
				if(rowspan>1){
					td.setRowspan(rowspan);
				}
				if(colspan>1){
					td.setColspan(colspan);
				}
				nodes.get(fromRow).setData(columnMap.get(fromColumn), td);
	    	} else {
	    		nodes.get(fromRow).setData(columnMap.get(fromColumn), o);
	    	}
	    }
	    // 如果 columns 不够则，自动增加columns 
		// 如果 tr不够，则自动增加tr的数量
	    owner.notifyChange();  
	}
	
	@Override
    public GridPart add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value){
		if(toRow==null){
			toRow=fromRow;
		}
		if(toColumn==null){
			toColumn=fromColumn;
		}
		this.put(fromRow, fromColumn, toRow, toColumn, value, false);
		return this;
	}
	
	@Override
    public GridPart put(int row, int column, Object o){
		put(row,column,o,true);
		return this;
	}
	
	@Override
    public GridPart put(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value){
		if(toRow==null){
			toRow=fromRow;
		}
		if(toColumn==null){
			toColumn=fromColumn;
		}
		this.put(fromRow, fromColumn, toRow, toColumn, value, true);
		return this;
	}
	
	@Override
    public GridPart removeNode(int row, int column){
		removeNode(row,column ,row,column);
		return this;
	}
	@Override
    public boolean containsNode(int fromRow, int fromColumn, int toRow, int toColumn){
		if(owner==null){
			throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in GridLayout.");
		}
		if(owner.prototypeBuilding()){
			return false;//假定prototype的构建过程是稳定的，不会构建错内容，所以不检查格子是否被占用。
		}
		Map<String,Integer> columnMap=owner.getVisableColumnNumMap();
		int targetFromRow=0;
		for(Tr tr: nodes){
			if(tr.getData()==null){
				targetFromRow++;
				continue;
			}
			for(Map.Entry<String,Object>entry:tr.getData().entrySet()){
				String key=entry.getKey();
				if(!columnMap.containsKey(key)){
					continue;//防止hidden字段进入查询
				}
				int targetFromColumn=columnMap.get(key);
				if (entry.getValue() instanceof Td) {
					Td td=(Td) entry.getValue();
					int targetToRow=targetFromRow+(td.getRowspan()==null?1:td.getRowspan())-1;
					int targetToColumn=targetFromColumn+(td.getColspan()==null?1:td.getColspan())-1;
					if(targetFromRow<=toRow&&targetToRow>=fromRow
							&&targetFromColumn<=toColumn&&targetToColumn>=fromColumn){
						return true;
					}
				}else{
					if(targetFromRow<=toRow&&targetFromRow>=fromRow
							&&targetFromColumn<=toColumn&&targetFromColumn>=fromColumn){
						return true;
					}
				}
			}
			targetFromRow++;
		}
		return false;
	}
	@Override
    public GridPart removeNode(int fromRow, int fromColumn, int toRow, int toColumn){
		if(owner==null){
			throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in GridLayout.");
		}
		// 如果有一个格子不能被移除则报错，不能被移除的可能，是指这个区域里面的某个格子被合并过单元，并且这个合并的单元格部分区域在这个区域之外。
		Map<String,Integer> columnMap=owner.getVisableColumnNumMap();
		List<Object[]> toRemove=new ArrayList<Object[]>();
		int row=0;
		for(Tr tr: nodes){
			if(tr.getData()!=null){
				for(Map.Entry<String,Object>entry:tr.getData().entrySet()){
					String key=entry.getKey();
					if(!columnMap.containsKey(key)){
						continue;//防止hidden字段进入查询
					}
					
					int targetFromRow=row;
					int targetFromColumn=columnMap.get(key);
					int targetToRow=targetFromRow;
					int targetToColumn=targetFromColumn;
					if (entry.getValue() instanceof Td) {
						Td td=(Td) entry.getValue();
						targetToRow=targetFromRow+(td.getRowspan()==null?1:td.getRowspan())-1;
						targetToColumn=targetFromColumn+(td.getColspan()==null?1:td.getColspan())-1;
					}
					if(targetFromRow<=toRow&&targetToRow>=fromRow
							&&targetFromColumn<=toColumn&&targetToColumn>=fromColumn){
						//有交叠
						if(targetFromRow<fromRow||targetToRow>toRow
								||targetFromColumn<fromColumn||targetToColumn>toColumn){
							throw new UnsupportedOperationException("The position is occupied by the entry:["+row+","+targetFromColumn+"] \r\n" + entry.getValue());
						}
						toRemove.add(new Object[]{row,key});
					}
				}
			}
			row++;
		}
		row=0;
		if(toRemove!=null&&toRemove.size()>0){
			for(Object[] o:toRemove){
				int row_=(Integer) o[0];
				String key=(String)o[1];
				nodes.get(row_).removeData(key);
			}
			if(owner!=null){
				owner.notifyChange();
			}
		}
		
		return this;
	}

//	@Override
//	public boolean onReplace(Widget<?> oldWidget, Widget<?> newWidget) {
//		return super.onReplace(oldWidget, newWidget);
//	}
//
//	@Override
//	public void markRebuild() {
//		super.markRebuild();
//	}
//
//	/**
//	 * 重建该列表
//	 */
//	public void rebuild(){
//		this.ugh.rebuild();
//	}
    public GridPart setScroll(Boolean scroll) {
    	this.scroll = scroll;
    	return this;
    }

    public Boolean getScroll() {
	    return scroll;
    }

    public GridPart setScrollClass(String scrollClass) {
    	this.scrollClass = scrollClass;
    	return this;
    }

    public String getScrollClass() {
	    return scrollClass;
    }
	private Boolean scroll;
	private String scrollClass;


	@Override
	public String getType() {
		return null;
	}

}
