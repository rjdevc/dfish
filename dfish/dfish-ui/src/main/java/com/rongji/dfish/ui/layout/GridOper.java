package com.rongji.dfish.ui.layout;

/**
 * 支持表格操作
 * @author DFish team
 * @param <T> 类型
 */
public interface GridOper<T> {
	/**
	 * <p>在某行某列添加一个元素</p>
	 * <p>行和列的初始坐标为0</p>
	 * <p>如果该行和该列还不存在，将会自动增补Tr和Td，直到足够容纳该行和列。
	 * 自动增加的行可以通过pub指定高度样式等默认属性。自动增加的列，默认将按A B C D... Z AA AB ... AZ BA BB... 命名,宽度为*</p>
	 * <p>o 可以是String Widget 或者 Td。实际上都相当于一个Td里面的内容可以被简写，详见{@link com.rongji.dfish.ui.layout.grid.Td}</p>
	 * @param row 行
	 * @param column 列
	 * @param o 对象
	 * @return this
	 */
	T add(int row,int column,Object o);
	/**
	 * <p>在某个区块合并单元格，并添加一个元素</p>
	 * <p>如果fromRow与toRow相等，fromColumn与toColumn相等。则相当于{@link #add(int, int, Object)}</p>
	 * <p>行和列的初始坐标为0</p>
	 * <p>如果该区块还不存在，将会自动增补Tr和Td，直到足够容纳该区块。
	 * 自动增加的行可以通过pub指定高度样式等默认属性。自动增加的列，默认将按A B C D... Z AA AB ... AZ BA BB... 命名,宽度为*</p>
	 * <p>o 可以是String Widget 或者 Td。实际上都相当于一个Td里面的内容可以被简写，详见{@link com.rongji.dfish.ui.layout.grid.Td}</p>
	 * @param fromRow 区块起始行
	 * @param fromColumn 区块起始列
	 * @param toRow 区块结束行
	 * @param toColumn 区块结束列
	 * @param o  对象
	 * @return this
	 */
	T add(Integer fromRow,Integer fromColumn,Integer toRow, Integer toColumn,Object o);
	/**
	 * <p>在某行某列设置一个元素，与add不一样的是，如果这个位置已经有内容了，将会尝试覆盖它。</p>
	 * <p>覆盖可能是失败，比如说要覆盖的位置实际上是个合并单元格，而当前内容不足以覆盖它全部内容</p>
	 * <p>行和列的初始坐标为0</p>
	 * <p>如果该行和该列还不存在，将会自动增补Tr和Td，直到足够容纳该行和列。
	 * 自动增加的行可以通过pub指定高度样式等默认属性。自动增加的列，默认将按A B C D... Z AA AB ... AZ BA BB... 命名,宽度为*</p>
	 * <p>o 可以是String Widget 或者 Td。实际上都相当于一个Td里面的内容可以被简写，详见{@link com.rongji.dfish.ui.layout.grid.Td}</p>
	 * @param row 行
	 * @param column 列
	 * @param o 对象
	 * @return this
	 */
	T put(int row,int column,Object o);
	/**
	 * <p>在某个区块合并单元格，并设置一个元素，与add不一样的是，如果这个区块已经有内容了，将会尝试覆盖它。</p>
	 * <p>覆盖可能是失败，比如说要覆盖的区块原本可能已经有一个或多个合并单元格，而当前内容不足以覆盖它全部内容</p>
	 * <p>如果fromRow与toRow相等，fromColumn与toColumn相等。则相当于{@link #add(int, int, Object)}</p>
	 * <p>行和列的初始坐标为0</p>
	 * <p>如果该区块还不存在，将会自动增补Tr和Td，直到足够容纳该区块。
	 * 自动增加的行可以通过pub指定高度样式等默认属性。自动增加的列，默认将按A B C D... Z AA AB ... AZ BA BB... 命名,宽度为*</p>
	 * <p>o 可以是String Widget 或者 Td。实际上都相当于一个Td里面的内容可以被简写，详见{@link com.rongji.dfish.ui.layout.grid.Td}</p>
	 * @param fromRow 区块起始行
	 * @param fromColumn 区块起始列
	 * @param toRow 区块结束行
	 * @param toColumn 区块结束列
	 * @param o  对象
	 * @return this
	 */
	T put(Integer fromRow,Integer fromColumn,Integer toRow, Integer toColumn,Object o);
	/**
	 * <p>删除某个单元格内容</p>
	 * <p>行和列的初始坐标为0</p>
	 * <p>删除可能是失败，比如说要删除的位置实际上是个合并单元格，而指定位置不足以包含它全部内容</p>
	 * <p>删除节点后可能会留下空行和空列，可以用{@link GridLayout#minimize()}来删除空行和空列。或去rows和column中手动清理</p>
	 * @param row 行
	 * @param column 列
	 * @return this
	 */
	T removeNode(int row,int column);
	/**
	 * <p>删除某个区块内的所有内容</p>
	 * <p>行和列的初始坐标为0</p>
	 * <p>删除可能是失败，比如说要删除的区块原本可能已经有一个或多个合并单元格，而当前区块不足以包含它全部内容</p>
	 * <p>删除节点后可能会留下空行和空列，可以用{@link GridLayout#minimize()}来删除空行和空列。或去rows和column中手动清理</p>
	 * @param fromRow 区块起始行
	 * @param fromColumn 区块起始列
	 * @param toRow 区块结束行
	 * @param toColumn 区块结束列
	 * @return this
	 */
	T removeNode(int fromRow,int fromColumn,int toRow, int toColumn);
	/**
	 * <p>删除所有的节点 </p>
	 * <p>注意会删除掉所有的隐藏列。</p>
	 */
	void clearNodes();
	/**
	 * <p>判定某个区块内是否有内容</p>
	 * <p>行和列的初始坐标为0</p>
	 * <p>如果fromRow与toRow相等，fromColumn与toColumn相等，则相当于判定某个位置是否有内容</p>
	 * @param fromRow 区块起始行
	 * @param fromColumn 区块起始列
	 * @param toRow 区块结束行
	 * @param toColumn 区块结束列
	 * @return boolean
	 */
	boolean containsNode(int fromRow,int fromColumn,int toRow, int toColumn);
}
