package com.rongji.dfish.ui.helper;

import java.util.Map;

import com.rongji.dfish.ui.command.JSCommand;

public class JSCmdLib {
	private static String find(String viewPath, String panelId) {
		return (viewPath == null || viewPath.equals("") ? "this" : "VM('"
				+ viewPath + "')")
				+ ".find('" + panelId + "')";
	}


	/**
	 * <p>
	 * 刷新当前窗口
	 * <p>
	 * 调用范例：JSCommand jc = JSCmdLib.reload( null, "abc.do" ) );
	 * <p>
	 * 范例打印内容：<js>this.reload( "abc.do" );</js>
	 * @param url 窗口刷新到什么URL。如果为空则按原有地址刷新
	 * @return JSCommand
	 */
	static public JSCommand reload(String url) {
		return new JSCommand( "VM(this).reload("
				+ (url == null || url.equals("") ? "" : "\"" + url + "\"")
				+ ");");
	}

	/**
	 * <p>
	 * 刷新来源窗口。
	 * <p>
	 * 调用范例：JSCommand jc = JSCmdLib.openerReload( "abc.do" ) );
	 * <p>
	 * 范例打印内容：<js>DFish.g_dialog(this).fromView.reload( "abc.do" );</js>
	 * @param url String 窗口刷新到什么URL。如果为空则按原有地址刷新
	 * @return JSCommand
	 */
	static public JSCommand openerReload( String url) {
		return new JSCommand( "$.dialog(this).ownerView.reload("
				+ (url == null || url.equals("") ? "" : "\"" + url + "\"")
				+ ");");
	}



	/**
	 * <p>
	 * 关闭当前对话框
	 * <p>
	 * 调用范例：JSCommand jc = JSCmdLib.dialogClose(  );
	 * @return JSCommand
	 */
	static public JSCommand dialogClose() {
		return new JSCommand("$.close(this);");//DFish.g_dialog(this).close();
	}

	/**
	 * <p>
	 * 刷新树的子节点
	 * <p>
	 * 调用范例：JSCommand jc = JSCmdLib.treeReload(  "/",  "3500" );
	 * <p>
	 * @param viewPath String 视图路径。如果设为null则为当前视图
	 * @param treeNodePkId String 要作用的树节点的PKID值
	 * @return JSCommand
	 */
	static public JSCommand treeReload( String viewPath,
			String treeNodePkId) {
		return new JSCommand( find(viewPath, treeNodePkId)
				+ ".reload()");
	}
//	/**
//	 * <p>
//	 * 刷新树的子节点
//	 * <p>
//	 * 调用范例：JSCommand jc = JSCmdLib.treeReload( null, "/", "f_tree", "3500" );
//	 * <p>
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param treePanelId String TreePanel的ID
//	 * @param treeNodePkId String 要作用的树节点的PKID值
//	 * @param synchronize boolean 是否同步执行
//	 * @return JSCommand
//	 */
//	static public JSCommand treeReload(String viewPath,
//			String treePanelId, String treeNodePkId,boolean synchronize) {
//		return new JSCommand( find(viewPath, treePanelId)
//				+ ".reload('" + treeNodePkId + "',"+synchronize+")");
//	}

	/**
	 * <p>
	 * 刷出树的新子节点
	 * @param viewPath String 视图路径。如果设为null则为当前视图
	 * @param treeNodePkId String 要作用的树节点的PKID值
	 * @return JSCommand
	 */
	static public JSCommand treeAdd( String viewPath,
			 String treeNodePkId) {
		return new JSCommand( find(viewPath, treeNodePkId) + ".reloadForAdd()");
	}
	
	/**
	 * <p>
	 * 刷出树的新子节点
	 * @param viewPath String 视图路径。如果设为null则为当前视图
	 * @param treeNodePkId String 要作用的树节点的PKID值
	 * @param synchronize boolean 是否同步执行
	 * @return JSCommand
	 */
	static public JSCommand treeAdd( String viewPath,String treeNodePkId,boolean synchronize) {
		return new JSCommand(find(viewPath, treeNodePkId) + ".reloadForAdd("+synchronize+")");
	}

//	/**
//	 * <p>
//	 * 更新树节点
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param treePanelId String TreePanel的ID
//	 * @param treeNodePkId String 要作用的树节点的PKID值
//	 * @return JSCommand
//	 */
//	static public JSCommand treeModify(String commandId, String viewPath,
//			String treePanelId, String treeNodePkId) {
//		return new JSCommand(commandId, find(viewPath, treePanelId)
//				+ ".modify('" + treeNodePkId + "')");
//	}
//	/**
//	 * <p>
//	 * 更新树节点
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param treePanelId String TreePanel的ID
//	 * @param treeNodePkId String 要作用的树节点的PKID值
//	 * @param synchronize boolean 是否同步执行
//	 * @return JSCommand
//	 */
//	static public JSCommand treeModify(String commandId, String viewPath,
//			String treePanelId, String treeNodePkId,boolean synchronize) {
//		return new JSCommand(commandId, find(viewPath, treePanelId)
//				+ ".modify('" + treeNodePkId + "',"+synchronize+")");
//	}

	/**
	 * <p>
	 * 删除树节点
	 * @param viewPath String 视图路径。如果设为null则为当前视图
	 * @param treeNodePkId String 要作用的树节点的PKID值
	 * @return JSCommand
	 */
	static public JSCommand treeRemove(String viewPath,
			 String treeNodePkId) {
		return new JSCommand(find(viewPath, treeNodePkId)
				+ ".remove()");
	}
	/**
	 * <p>
	 * 删除树节点
	 * @param viewPath String 视图路径。如果设为null则为当前视图
	 * @param treeNodePkId String 要作用的树节点的PKID值
	 * @param synchronize String 是否同步执行
	 * @return JSCommand
	 */
	static public JSCommand treeRemove( String viewPath,
			String treeNodePkId,boolean synchronize) {
		return new JSCommand( find(viewPath, treeNodePkId)
				+ ".remove("+synchronize+")");
	}

	/**
	 * <p>
	 * 高亮树节点
	 * @param viewPath String 视图路径。如果设为null则为当前视图
	 * @param treeNodePkId String 要作用的树节点的PKID值
	 * @return JSCommand
	 */
	static public JSCommand treeFocus( String viewPath,
			 String treeNodePkId) {
		return new JSCommand( find(viewPath, treeNodePkId)
				+ ".focus()");
	}

//	/**
//	 * <p>
//	 * 点击树节点
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param treePanelId String TreePanel的ID
//	 * @param treeNodePkId String 要作用的树节点的PKID值
//	 * @return JSCommand
//	 */
//	static public JSCommand treeClick(String commandId, String viewPath,
//			String treePanelId, String treeNodePkId) {
//		return new JSCommand(commandId, find(viewPath, treePanelId)
//				+ ".click('" + treeNodePkId + "')");
//	}

	/**
	 * <p>
	 * 深度展开树节点
	 * @param viewPath String 视图路径。如果设为null则为当前视图
	 * @param treePanelId String TreePanel的ID
	 * @param url String 要作用的树节点的PKID值
	 * @return JSCommand
	 */
	static public JSCommand treeOpen( String viewPath,
			String treePanelId, String url) {
		return new JSCommand( find(viewPath, treePanelId) + ".openTo('"
				+ url + "')");
	}

//	/**
//	 * <p>
//	 * 更新gridPanel的一行数据
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param data Map 数据内容
//	 * @param index Integer 更新的是第几行。基数是0，如果设为null，则作用于当前正在编辑的那一行
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	static public JSCommand gridUpdateRow(String commandId, String viewPath,
//			String gridPanelId, Map data, Integer index) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".updateRow(" + jsonEscape(data)
//				+ (index == null ? "" : ("," + index)) + ")");
//	}
//	/**
//	 * <p>
//	 * 更新gridPanel的一行数据
//	 * </p>
//	 * <p>不指定更新哪一行，则更新正在编辑的行</p>
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param data Map 数据内容
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	static public JSCommand gridUpdateRow(String commandId, String viewPath,
//			String gridPanelId, Map data) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".updateRow(" + jsonEscape(data) + ")");
//	}
//	/**
//	 * <p>
//	 * 更新gridPanel的一行数据
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param data Map 数据内容
//	 * @param contition Map 更新的是哪一行。如果设为null，则作用于当前正在编辑的那一行，<br/>
//	 * 与delete不同.如果如果符合条件的Condition好多个，他只update最前面那个。
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	static public JSCommand gridUpdateRow(String commandId, String viewPath,
//			String gridPanelId, Map data, Map contition) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".updateRow(" + jsonEscape(data)
//				+ (contition == null ? "" : ("," + jsonEscape(contition))) + ")");
//	}
//	/**
//	 * <p>
//	 * 插入gridPanel新的一行
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param data Map 数据内容
//	 * @param index Integer 插入的位置序号。基数是0，如果设为null，则添加到当前表格的末尾
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	public static JSCommand gridInsertRow(String commandId, String viewPath,
//			String gridPanelId, Map data, Integer index) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".insertRow(" + jsonEscape(data)
//				+ (index == null ? "" : ("," + index)) + ")");
//	}
//	/**
//	 * <p>
//	 * gridPanel的最末端插入新的一行
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param data Map 数据内容
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	public static JSCommand gridInsertRow(String commandId, String viewPath,
//			String gridPanelId, Map data) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".insertRow(" + jsonEscape(data) + ")");
//	}
//	/**
//	 * <p>
//	 * 插入gridPanel新的一行
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param data Map 数据内容
//	 * @param condition Map 插入的位置条件。如果设为null，则添加到当前表格的末尾
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	public static JSCommand gridInsertRow(String commandId, String viewPath,
//			String gridPanelId, Map data, Map condition) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".insertRow(" + jsonEscape(data)
//				+ (condition == null ? "" : ("," + jsonEscape(condition))) + ")" );
//	}
//	/**
//	 * <p>
//	 * 删除gridPanel的某一行
//	 * </p>
//	 * <p>
//	 * <pre>
//	 * Map&lt;String, String&gt; condition = new HashMap&lt;String, String&gt;();
//	 * condition.put(&quot;id&quot;, &quot;001&quot;);
//	 * JSCommand del = JSCmdLib.gridDeleteRow(&quot;del&quot;, &quot;/main&quot;, &quot;f_grid&quot;, condition);
//	 * </pre>
//	 * 这个即删除ID为001的哪行数据
//	 * </p>
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param condition Map 数据内容 <p>这里data里面可以只有一个键值对。如果根据这个键值，找到匹配的项进行删除</p>
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	public static JSCommand gridDeleteRow(String commandId, String viewPath,
//			String gridPanelId, Map condition) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".deleteRow(" + jsonEscape(condition) + ")");
//	}
//	/**
//	 * 删除行，
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param index 删除的位置序号。基数是0
//	 * @return JSCommand
//	 */
//	public static JSCommand gridDeleteRow(String commandId, String viewPath,
//			String gridPanelId, Integer index) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".deleteRow(" + index + ")");
//	}
//	
//	/**
//	 * 高亮某行
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param condition 条件。注意，只高亮符合条件的第一条。
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	public static JSCommand gridFocus(String commandId, String viewPath,
//			String gridPanelId, Map condition) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".focus(" + jsonEscape(condition) + ")");
//	}
//	
//	/**
//	 * 滚动滚动条到一定位置，保证某一行能够被看到
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param condition 条件。注意，只显示符合条件的第一条。
//	 * @return JSCommand
//	 */
//	@SuppressWarnings("unchecked")
//	public static JSCommand gridScrollIntoView(String commandId, String viewPath,
//			String gridPanelId, Map condition) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".scrollIntoView(" + jsonEscape(condition) + ")");
//	}
//	
//	/**
//	 * 高亮某行
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param index 行号，基数为0
//	 * @return JSCommand
//	 */
//	public static JSCommand gridFocus(String commandId, String viewPath,
//			String gridPanelId, Integer index) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".focus(" + index + ")");
//	}
//	/**
//	 * 滚动滚动条到一定位置，保证某一行能够被看到
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param gridPanelId String GridPanel的ID
//	 * @param index 行号，基数为0
//	 * @return JSCommand
//	 */
//	public static JSCommand gridScrollIntoView(String commandId, String viewPath,
//			String gridPanelId, Integer index) {
//		return new JSCommand(commandId, find(viewPath, gridPanelId)
//				+ ".scrollIntoView(" + index + ")");
//	}
	

	/**
	 * 既通过 JS的转义又通过XML转移
	 * @param sb StringBuilder
	 * @param src String
	 */
    public static final void escapeWithJSAndXML(StringBuilder sb, String src) {
        if (src == null) {
            return;
        }
        char[] ca = src.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            switch (ca[i]) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '\"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            default:
                if (ca[i] < 32 ) {
                    sb.append("&#").append((int) ca[i]).append(';');
                } else {
                    sb.append(ca[i]);
                }
            }
        }
    }

	/**
	 * 既通过 JS的转义又通过XML转移
	 * @param sb StringBuilder
	 * @param src String
	 */
    public static final void escapeWithJS(StringBuilder sb, String src) {
        if (src == null) {
            return;
        }
        char[] ca = src.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            switch (ca[i]) {
            case '&':
                sb.append("&amp;");
                break;
            case '\"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            default:
                if (ca[i] < 32 || (ca[i] > 127 && ca[i] < 256)) {
//                    sb.append("&#").append((int) ca[i]).append(';');
                } else {
                    sb.append(ca[i]);
                }
            }
        }
    }

//	/**
//	 * <p>
//	 * 使按钮有效
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param buttonId String 按钮的ID
//	 * @param disabled boolean true为失效,false为有效
//	 * @return JSCommand
//	 */
//	static public JSCommand operButtonDisable(String commandId, String viewPath,
//			String buttonId, boolean disabled ) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Fn.Oper.disable(")
//		  .append( viewPath == null || viewPath.equals("") ? "this" : "VM('" + viewPath + "')" )
//		  .append(",'").append(buttonId).append("',").append(disabled).append(')');
//		return new JSCommand( commandId, sb.toString() );
//	}
//
//	/**
//	 * <p>
//	 * 使按钮高亮
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param buttonId String 按钮的ID
//	 * @return JSCommand
//	 */
//	static public JSCommand operButtonFocus(String commandId, String viewPath,
//			String buttonId ) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Fn.Oper.focus(")
//		  .append( viewPath == null || viewPath.equals("") ? "this" : "VM('" + viewPath + "')" )
//		  .append(",'").append(buttonId).append("')");
//		return new JSCommand( commandId, sb.toString() );
//	}
//
//	/**
//	 * <p>
//	 * 使按钮执行点击动作
//	 * @param commandId String 命令ID
//	 * @param viewPath String 视图路径。如果设为null则为当前视图
//	 * @param buttonId String 按钮的ID
//	 * @return JSCommand
//	 */
//	static public JSCommand operButtonClick(String commandId, String viewPath,
//			String buttonId ) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Fn.Oper.click(")
//		  .append( viewPath == null || viewPath.equals("") ? "this" : "VM('" + viewPath + "')" )
//		  .append(",'").append(buttonId).append("')");
//		return new JSCommand( commandId, sb.toString() );
//	}
//
//	/**
//	 * <p>
//	 * 清空并重载combox的缓存
//	 * @param commandId String 命令ID
//	 * @param src String 缓存URL地址
//	 * @return JSCommand
//	 */
//	static public JSCommand comboxReloadCache(String commandId,
//			String src ) {
//		return new JSCommand( commandId, "Fn.Combox.load('" + src + "')" );
//	}
	/**
	 * json编码
	 * @param sb StringBuilder
	 * @param data 数据
	 */
	public static final void jsonEscape(StringBuilder sb,Map<?, ?> data){
		sb.append('{');
		if(data!=null&&data.size()>0){
		for (Object d : data.entrySet()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) d;
			sb.append(entry.getKey()).append(':');
			Object value=entry.getKey();
			if(value==null){
				sb.append("null");
			}else if(value instanceof Number){
				sb.append(value);
			}else if(value instanceof Map){
				jsonEscape(sb,(Map<?, ?>)value);
			}else{
				sb.append('"');
				String v=String.valueOf(entry.getValue());
				sb.append(v.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\\r").replace("\n", "\\\n"));
				sb.append("\",");
			}
		}
		sb.setCharAt(sb.length()-1,'}');
		}else{
			sb.append('}');
		}
	}
	 /**
     * 把一个map按JSON方式编码
     * @param data 数据
     * @return String
     */
	public static final String jsonEscape(Map<?, ?> data){
		StringBuilder sb = new StringBuilder();
		jsonEscape(sb,data);
		return sb.toString();
	}
}
