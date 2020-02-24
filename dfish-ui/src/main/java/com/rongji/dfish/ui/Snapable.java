package com.rongji.dfish.ui;

///**
// * Snapable 为可吸附的对象。一般是个弹出窗口吸附在触发该事件的按钮/链接上。
// * @author DFish Team
// *
// * @param <T>  当前对象类型
// */
//public interface Snapable<T extends Snapable<T>> {
//	/**
//	 * 吸附的对象。可以是 html 元素或 widget id。
//	 * @return snap
//	 */
//	String getSnap();
//	/**
//	 * 吸附的对象。可以是 html 元素或 widget id。
//	 * @param snap 吸附的对象
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	T setSnap(String snap);
////	/**
////	 * 默认false。如果设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。
////	 * @return pophide
////	 */
////	Boolean getPophide();
////	/**
////	 * 默认false。如果设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。
////	 * @param pophide
////	 * @return 本身，这样可以继续设置其他属性
////	 */
////	T setPophide(Boolean pophide);
//
//	/**
//	 * 指定 snap 的方式。
//	 * 可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。
//	 * 其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。
//	 * 例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。
//	 * 范例: 对话框吸附到 mydiv 元素，吸附方式指定为 "41,32,14,23"。
//	 * 系统将先尝试 "41"，如果对话框没有超出浏览器可视范围就直接显示。
//	 * 如果超出了，则继续尝试 "32", 依此类推。
//	 * <style>
//	 * .x-table TD{width:50px;height:18px;}
//	 * </style>
//	 * <div>
//	 * 单个对象的标记含义
//	 * <table class="x-table" style="border:1px solid gray;">
//	 * <tr><td>1</td><td></td><td align="center">t</td><td></td><td align="right">2</td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td>l</td><td></td><td></td><td></td><td align="right">r</td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td>4</td><td></td><td align="center">b</td><td></td><td align="right">3</td></tr>
//	 * </table>
//	 * </div>
//	 * 两个对象粘滞的情况 红色为弹出窗口，绿色为需要粘滞的对象
//	 * 情况41
//	 * <div style="width:300px;">
//	 * <table class="x-table" style="border:1px solid green;">
//	 * <tr><td></td><td></td><td></td></tr>
//	 * <tr><td>4</td><td></td><td></td></tr>
//	 * </table>
//	 * </div>
//	 * <div style="width:300px;">
//	 * <table class="x-table" style="border:1px solid red;">
//	 * <tr><td>1</td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * </table>
//	 * </div>
//	 *  情况32
//	 * <div style="width:300px;">
//	 * <table align="right" class="x-table" style="border:1px solid green;">
//	 * <tr><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td align="right">3</td></tr>
//	 * </table>
//	 * </div>
//	 * <div style="width:300px;">
//	 * <table align="right" class="x-table" style="border:1px solid red;">
//	 * <tr><td></td><td></td><td></td><td></td><td align="right">2</td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * <tr><td></td><td></td><td></td><td></td><td></td></tr>
//	 * </table>
//	 * </div>
//	 * 如此类推。
//
//	 * @return String
//	 */
//	String getSnapType();
//	/**
//	 * 指定 snap 的方式。
//	 * 可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。
//	 * 其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。
//	 * 例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。
//	 * 范例: 对话框吸附到 mydiv 元素，吸附方式指定为 "41,32,14,23"。
//	 * 系统将先尝试 "41"，如果对话框没有超出浏览器可视范围就直接显示。
//	 * 如果超出了，则继续尝试 "32", 依此类推。
//	 * @param snapType String
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	T setSnapType(String snapType);
////	/**
////	 * 例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。
////	 */
////	static final String SNAPTYPE_BELOW_ALIGN_LEFT="41";
//
//	/**
//	 * 系统将先尝试 "41"，如果对话框没有超出浏览器可视范围就直接显示。
//	 * 如果该控件位置比较靠右或靠下，有可能会继续尝试 32 14 与23的方式，
//	 * 使得该弹出窗口尽可能在控件边上，并且左右有一边对齐，上下有一边是粘再起一起的。
//	 */
//	static final String SNAPTYPE_COMMON="41,32,14,23";
//
//}
