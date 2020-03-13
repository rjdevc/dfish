package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractMultiNodeContainer;
import com.rongji.dfish.ui.MultiNodeContainer;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.Table;
import com.rongji.dfish.ui.layout.TableOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象的Table 它有两个实例，一个是底层的JsonTable 仅作吻合前端JSON要求的最低的封装，必要的getter和setter
 * 另一个是常用的Table 可以指定某个位置直接增加内容。
 *
 * @author DFish team
 */
public abstract class TablePart extends AbstractMultiNodeContainer<TablePart, TR> implements TableOperation<TablePart>, MultiNodeContainer<TablePart, TR> {
    /**
     * 默认构造函数
     */
    public TablePart() {
        super(null);
    }

    @Override
    public String getType() {
        return null;
    }

    protected Table owner;

    public Table owner() {
        return owner;
    }

    public TablePart owner(Table owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public TablePart add(int row, int column, Object o) {
        put(row, column, o, false);
        return this;
    }

    private void put(int row, int column, Object o, boolean overwrite) {
        if (o != null && o instanceof AbstractTD) {
            int toColumn = column;
            int toRow = row;
            //防止加了如一个大小大于大于Table
            AbstractTD<?> cast = (AbstractTD<?>) o;
            if (cast.getColSpan() != null && cast.getColSpan() > 1) {
                toColumn += cast.getColSpan() - 1;
            }
            if (cast.getRowSpan() != null && cast.getRowSpan() > 1) {
                toRow += cast.getRowSpan() - 1;
            }
            put(row, column, toRow, toColumn, o, overwrite);
        } else {
            put(row, column, row, column, o, overwrite);
        }
    }

    private void put(int fromRow, int fromColumn, int toRow, int toColumn, Object o, boolean overwrite) {
        if (owner == null) {
            throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in Table.");
        }
        if (fromRow < 0 || fromColumn < 0 || toRow < 0 || toColumn < 0) { // 行列小于0,抛异常
            throw new IndexOutOfBoundsException("Position: (" + Math.min(fromRow, toRow) + "," + Math.min(fromColumn, toColumn) + ")");
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
        if (overwrite) {
            removeNode(fromRow, fromColumn, toRow, toColumn);
        } else {
            // 判定是否有重复，如果有抛出异常
            if (containsNode(fromRow, fromColumn, toRow, toColumn)) {
                throw new UnsupportedOperationException("The position is occupied:[" + fromRow + "," + fromColumn + "," + toRow + "," + toColumn + "]");
            }
        }
        while (toRow >= nodes.size()) {
            nodes.add(new TR());
        }
        Map<Integer, String> columnMap = new HashMap<>();
        int column = 0;
        List<Column> columns = owner.getColumns();
        // 获取当前已设定的列
        if (columns != null) {
            for (Column c : columns) {
                if (c.isVisible()) {
                    columnMap.put(column++, c.getField());
                }
            }
        }

        while (toColumn >= columnMap.size()) {
            Column gridColumn = Column.text(null, WIDTH_REMAIN);
            owner.addColumn(gridColumn);
            columnMap.put(columnMap.size(), gridColumn.getField());
        }

        int rowspan = toRow - fromRow + 1;
        int colspan = toColumn - fromColumn + 1;
        if (o instanceof AbstractTD) {
            //如果是entry是TableCell就直接使用TableCell但，TableCell的rowspan和
            //colspan 会重新计算。TableCell本身是什么模式就是什么模式
            AbstractTD<?> cell = (AbstractTD<?>) o;
            if (rowspan > 1) {
                cell.setRowSpan(rowspan);
            }
            if (colspan > 1) {
                cell.setColSpan(colspan);
            }

            (nodes.get(fromRow)).putData(columnMap.get(fromColumn), cell);
        } else if (o instanceof Widget) {
            //如果entry 是 Widget 那么将会包装在一个TableCell里面
            if (rowspan > 1 || colspan > 1) {
                AbstractTD<?> cell = new TD();
                cell.setNode((Widget<?>) o);
                if (rowspan > 1) {
                    cell.setRowSpan(rowspan);
                }
                if (colspan > 1) {
                    cell.setColSpan(colspan);
                }

                (nodes.get(fromRow)).putData(columnMap.get(fromColumn), cell);
            } else {
                (nodes.get(fromRow)).putData(columnMap.get(fromColumn), o);
            }
        } else {
            if (o != null && (rowspan > 1 || colspan > 1)) {
                JsonTD td = new JsonTD();
                td.setText(o.toString());
                if (rowspan > 1) {
                    td.setRowSpan(rowspan);
                }
                if (colspan > 1) {
                    td.setColSpan(colspan);
                }
                (nodes.get(fromRow)).putData(columnMap.get(fromColumn), td);
            } else {
                (nodes.get(fromRow)).putData(columnMap.get(fromColumn), o);
            }
        }
    }

    @Override
    public TablePart add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
        if (toRow == null) {
            toRow = fromRow;
        }
        if (toColumn == null) {
            toColumn = fromColumn;
        }
        this.put(fromRow, fromColumn, toRow, toColumn, value, false);
        return this;
    }

    @Override
    public TablePart put(int row, int column, Object o) {
        put(row, column, o, true);
        return this;
    }

    @Override
    public TablePart put(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
        if (toRow == null) {
            toRow = fromRow;
        }
        if (toColumn == null) {
            toColumn = fromColumn;
        }
        this.put(fromRow, fromColumn, toRow, toColumn, value, true);
        return this;
    }

    @Override
    public TablePart removeNode(int row, int column) {
        removeNode(row, column, row, column);
        return this;
    }

    @Override
    public boolean containsNode(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (owner == null) {
            throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in Table.");
        }
        Map<String, Integer> columnMap = owner.getVisibleColumnNumMap();
        int targetFromRow = 0;
        for (Object obj : nodes) {
            TR tr = (TR) obj;
            if (tr.getData() == null) {
                targetFromRow++;
                continue;
            }
            for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                String key = entry.getKey();
                if (!columnMap.containsKey(key)) {
                    continue;//防止hidden字段进入查询
                }
                int targetFromColumn = columnMap.get(key);
                if (entry.getValue() instanceof TD) {
                    TD td = (TD) entry.getValue();
                    int targetToRow = targetFromRow + (td.getRowSpan() == null ? 1 : td.getRowSpan()) - 1;
                    int targetToColumn = targetFromColumn + (td.getColSpan() == null ? 1 : td.getColSpan()) - 1;
                    if (targetFromRow <= toRow && targetToRow >= fromRow
                            && targetFromColumn <= toColumn && targetToColumn >= fromColumn) {
                        return true;
                    }
                } else {
                    if (targetFromRow <= toRow && targetFromRow >= fromRow
                            && targetFromColumn <= toColumn && targetFromColumn >= fromColumn) {
                        return true;
                    }
                }
            }
            targetFromRow++;
        }
        return false;
    }

    @Override
    public TablePart removeNode(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (owner == null) {
            throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in Table.");
        }
        // 如果有一个格子不能被移除则报错，不能被移除的可能，是指这个区域里面的某个格子被合并过单元，并且这个合并的单元格部分区域在这个区域之外。
        Map<String, Integer> columnMap = owner.getVisibleColumnNumMap();
        List<Object[]> toRemove = new ArrayList<Object[]>();
        int row = 0;
        for (Object obj : nodes) {
            TR tr = (TR) obj;
            if (tr.getData() != null) {
                for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                    String key = entry.getKey();
                    if (!columnMap.containsKey(key)) {
                        continue;//防止hidden字段进入查询
                    }

                    int targetFromRow = row;
                    int targetFromColumn = columnMap.get(key);
                    int targetToRow = targetFromRow;
                    int targetToColumn = targetFromColumn;
                    if (entry.getValue() instanceof TD) {
                        TD td = (TD) entry.getValue();
                        targetToRow = targetFromRow + (td.getRowSpan() == null ? 1 : td.getRowSpan()) - 1;
                        targetToColumn = targetFromColumn + (td.getColSpan() == null ? 1 : td.getColSpan()) - 1;
                    }
                    if (targetFromRow <= toRow && targetToRow >= fromRow
                            && targetFromColumn <= toColumn && targetToColumn >= fromColumn) {
                        //有交叠
                        if (targetFromRow < fromRow || targetToRow > toRow
                                || targetFromColumn < fromColumn || targetToColumn > toColumn) {
                            throw new UnsupportedOperationException("The position is occupied by the entry:[" + row + "," + targetFromColumn + "] \r\n" + entry.getValue());
                        }
                        toRemove.add(new Object[]{row, key});
                    }
                }
            }
            row++;
        }
        row = 0;
        if (toRemove != null && toRemove.size() > 0) {
            for (Object[] o : toRemove) {
                int row_ = (Integer) o[0];
                String key = (String) o[1];
                ((TR) nodes.get(row_)).removeData(key);
            }
        }

        return this;
    }

    /**
     * 是否有滚动条。
     * @param scroll
     * @return 本身，这样可以继续设置其他属性
     */
    public TablePart setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }

    /**
     * 是否有滚动条。
     * @return
     */
    public Boolean getScroll() {
        return scroll;
    }

    private Boolean scroll;
}
