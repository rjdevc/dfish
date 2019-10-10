package com.rongji.dfish.ui;

import java.util.Map;

/**
 * 可以被设置动作的对象。
 * @author DFish Team
 * @param <T> 当前对象类型
 *
 */
public interface EventTarget<T extends EventTarget<T>> {
	//http://www.w3school.com.cn/tags/html_ref_eventattributes.asp
    /**
     * 设置事件触发动作
     * <p>设置这个对象的某个事件，将触发脚本</p>
     * @param eventName 事件的名称，如 click等
     * @param script 触发的动作脚本，当这个值为空的时候，表示取消该动作
     * @return T this
     */
    T setOn(String eventName,String script);
    /**
     * 获取事件的名称和动作。
     * 如果为空则这个部件上没有绑定事件
     * @return 如果为空则这个部件上没有绑定事件
     */
    Map<String,String> getOn();
    
 
    /**
     * DFISH事件-当widget在页面上生成HTML dom对象后触发
     */
    public static final String EVENT_READY= "ready";
    /**
     * DFISH事件-当widget的子节点有增删改其中一种变化时触发
     */
    public static final String EVENT_NODECHANGE= "nodechange";
    /**
     * DFISH事件-当 leaf tr 等widget 展开时触发
     */
    public static final String EVENT_EXPAND= "expand";
    /**
     * DFISH事件-当 leaf tr 等widget收起时触发
     */
    public static final String EVENT_COLLAPSE= "collapse";
    /**
     * DFISH事件-当 dialog menu 关闭时触发
     */
    public static final String EVENT_CLOSE= "close";
    /**
     *  DFISH事件-当 widget移除时触发
     */
    public static final String EVENT_REMOVE= "remove";
    /*  以下是WINDOW事件  */
    /**
     *  WINDOW事件-文档打印之后运行的脚本
     */
    public static final String EVENT_AFTERPRINT= "afterprint";
    /**
     *  WINDOW事件-文档打印之前运行的脚本
     */
    public static final String EVENT_BEFOREPRINT= "beforeprint";
    /**
     *  WINDOW事件-文档卸载之前运行的脚本
     */
    public static final String EVENT_BEFOREUNLOAD= "beforeunload";
    /**
     *  WINDOW事件-在错误发生时运行的脚本
     */
    public static final String EVENT_ERROR= "error";
    /**
     * WINDOW事件-当文档已改变时运行的脚本
     */
    public static final String EVENT_HASCHANGE= "haschange";
    /**
     * WINDOW事件-页面结束加载之后触发
     */
    public static final String EVENT_LOAD= "load";
    /**
     * WINDOW事件-在消息被触发时运行的脚本
     */
    public static final String EVENT_MESSAGE= "message";
    /**
     * WINDOW事件-当文档离线时运行的脚本
     */
    public static final String EVENT_OFFLINE= "offline";
    /**
     * WINDOW事件-当文档上线时运行的脚本
     */
    public static final String EVENT_ONLINE= "online";
    /**
     * WINDOW事件-当窗口隐藏时运行的脚本
     */
    public static final String EVENT_PAGEHIDE= "pagehide";
    /**
     * WINDOW事件-当窗口成为可见时运行的脚本
     */
    public static final String EVENT_PAGESHOW= "pageshow";
    /**
     * WINDOW事件-当窗口历史记录改变时运行的脚本
     */
    public static final String EVENT_POPSTATE= "popstate";
    /**
     * WINDOW事件-当文档执行撤销（redo）时运行的脚本
     */
    public static final String EVENT_REDO= "redo";
    /**
     * WINDOW事件-当浏览器窗口被调整大小时触发
     */
    public static final String EVENT_RESIZE= "resize";
    /**
     * WINDOW事件-在 Web Storage 区域更新后运行的脚本
     */
    public static final String EVENT_STORAGE= "storage";
    /**
     * WINDOW事件-在文档执行 undo 时运行的脚本
     */
    public static final String EVENT_UNDO= "undo";
    /**
     * WINDOW事件-一旦页面已下载时触发（或者浏览器窗口已被关闭）
     */
    public static final String EVENT_UNLOAD= "unload";
    
     /* 以下是FORM 事件 */
    /**
     * FORM事件-元素失去焦点时运行的脚本
     */
    public static final String EVENT_BLUR= "blur";
    
    /**
     * FORM事件-在元素值被改变前运行的脚本
     */
    public static final String EVENT_BEFORECHANGE= "beforechange";
    /**
     * FORM事件-在元素值被改变时运行的脚本
     */
    public static final String EVENT_CHANGE= "change";
    /**
     * FORM事件-当上下文菜单被触发时运行的脚本
     */
    public static final String EVENT_CONTEXTMENU= "contextmenu";
    /**
     * FORM事件-当元素聚焦时运行的脚本
     */
    public static final String EVENT_FOCUS= "focus";
    /**
     * FORM事件-在表单改变时运行的脚本
     */
    public static final String EVENT_FORMCHANGE= "formchange";
    /**
     * FORM事件-当表单获得用户输入时运行的脚本
     */
    public static final String EVENT_FORMINPUT= "forminput";
    /**
     * FORM事件-当元素获得用户输入时运行的脚本
     */
    public static final String EVENT_INPUT= "input";
    /**
     * FORM事件-当元素无效时运行的脚本
     */
    public static final String EVENT_INVALID= "invalid";
    /**
     *  FORM事件-当表单中的重置按钮被点击时触发HTML5 中不支持
     */
    public static final String EVENT_RESET= "reset";
    /**
     *  FORM事件-在元素中文本被选中后触发
     */
    public static final String EVENT_SELECT= "select";
    /**
     *  FORM事件-在提交表单时触发
     */
    public static final String EVENT_SUBMIT= "submit";

    /**
     * FORM事件-在表单控件认证完成(暂时仅有滑块组件使用)
     */
    public static final String EVENT_AUTH = "auth";
    
    /* 以下是KEYBOARD 事件 */
    /**
     *  KEYBOARD事件-在用户按下按键时触发。
     */
    public static final String EVENT_KEYDOWN= "keydown";
    /**
     *  KEYBOARD事件-在用户敲击按钮时触发。
     */
    public static final String EVENT_KEYPRESS= "keypress";
    /**
     *  KEYBOARD事件-当用户释放按键时触发。
     */
    public static final String EVENT_KEYUP= "keyup";

    /* 以下是MOUSE事件 */
    /**
     * MOUSE事件-元素上发生鼠标点击时触发。
     */
    public static final String EVENT_CLICK= "click";
    /**
     * MOUSE事件-元素上发生鼠标双击时触发。
     */
    public static final String EVENT_DBLCLICK= "dblclick";
    /**
     * MOUSE事件-元素被拖动时运行的脚本。
     */
    public static final String EVENT_DRAG= "drag";
    /**
     * MOUSE事件-在拖动操作末端运行的脚本。
     */
    public static final String EVENT_DRAGEND= "dragend";
    /**
     * MOUSE事件-当元素元素已被拖动到有效拖放区域时运行的脚本。
     */
    public static final String EVENT_DRAGENTER= "dragenter";
    /**
     * MOUSE事件-当元素离开有效拖放目标时运行的脚本。
     */
    public static final String EVENT_DRAGLEAVE= "dragleave";
    /**
     * MOUSE事件-当元素在有效拖放目标上正在被拖动时运行的脚本。
     */
    public static final String EVENT_DRAGOVER= "dragover";
    /**
     * MOUSE事件-在拖动操作开端运行的脚本。
     */
    public static final String EVENT_DRAGSTART= "dragstart";
    /**
     * MOUSE事件-当被拖元素正在被拖放时运行的脚本。
     */
    public static final String EVENT_DROP= "drop";
    /**
     *  MOUSE事件-当元素上按下鼠标按钮时触发。
     */
    public static final String EVENT_MOUSEDOWN= "mousedown";
    /**
     * MOUSE事件-当鼠标指针移动到元素上时触发。
     */
    public static final String EVENT_MOUSEMOVE= "mousemove";
    /**
     * MOUSE事件-当鼠标指针移出元素时触发。
     */
    public static final String EVENT_MOUSEOUT= "mouseout";
    /**
     * MOUSE事件-当鼠标指针移动到元素上时触发。
     */
    public static final String EVENT_MOUSEOVER= "mouseover";
    /**
     *  MOUSE事件-当在元素上释放鼠标按钮时触发。
     */
    public static final String EVENT_MOUSEUP= "mouseup";
    /**
     * MOUSE事件-当鼠标滚轮正在被滚动时运行的脚本。
     */
    public static final String EVENT_MOUSEWHEEL= "mousewheel";
    /**
     *  MOUSE事件-当元素滚动条被滚动时运行的脚本。
     */
    public static final String EVENT_SCROLL= "scroll";
    /* 以下是MEDIA 事件 */
    /**
     *  MEDIA事件-在退出时运行的脚本。
     */
    public static final String EVENT_ABORT= "abort";
    /**
     *  MEDIA事件-当文件就绪可以开始播放时运行的脚本（缓冲已足够开始时）。
     */
    public static final String EVENT_CANPLAY= "canplay";
    /**
     *  MEDIA事件-当媒介能够无需因缓冲而停止即可播放至结尾时运行的脚本。
     */
    public static final String EVENT_CANPLAYTHROUGH= "canplaythrough";
    /**
     * MEDIA事件-当媒介长度改变时运行的脚本。
     */
    public static final String EVENT_DURATIONCHANGE= "durationchange";
    /**
     *  MEDIA事件-当发生故障并且文件突然不可用时运行的脚本（比如连接意外断开时）。
     */
    public static final String EVENT_EMPTIED= "emptied";
    /**
     *  MEDIA事件-当媒介已到达结尾时运行的脚本（可发送类似“感谢观看”之类的消息）。
     */
    public static final String EVENT_ENDED= "ended";
//    /**
//     *  MEDIA事件-当在文件加载期间发生错误时运行的脚本。 与window事件重复
//     */
//    public static final String EVENT_ERROR= "error";
    /**
     * MEDIA事件-当元数据（比如分辨率和时长）被加载时运行的脚本。
     */
    public static final String EVENT_LOADEDMETADATA= "loadedmetadata";
    /**
     *  MEDIA事件-在文件开始加载且未实际加载任何数据前运行的脚本。
     */
    public static final String EVENT_LOADSTART= "loadstart";
    /**
     *  MEDIA事件-当媒介被用户或程序暂停时运行的脚本。
     */
    public static final String EVENT_PAUSE= "pause";
    /**
     *  MEDIA事件-当媒介已就绪可以开始播放时运行的脚本。
     */
    public static final String EVENT_PLAY= "play";
    /**
     * MEDIA事件-当媒介已开始播放时运行的脚本。
     */
    public static final String EVENT_PLAYING= "playing";
    /**
     *  MEDIA事件-当浏览器正在获取媒介数据时运行的脚本。
     */
    public static final String EVENT_PROGRESS= "progress";
    /**
     *  MEDIA事件-每当回放速率改变时运行的脚本（比如当用户切换到慢动作或快进模式）。
     */
    public static final String EVENT_RATECHANGE= "ratechange";
    /**
     *  MEDIA事件-每当就绪状态改变时运行的脚本（就绪状态监测媒介数据的状态）。
     */
    public static final String EVENT_READYSTATECHANGE= "readystatechange";
    /**
     * MEDIA事件-当 seeking 属性设置为 false（指示定位已结束）时运行的脚本。
     */
    public static final String EVENT_SEEKED= "seeked";
    /**
     * MEDIA事件-当 seeking 属性设置为 true（指示定位是活动的）时运行的脚本。
     */
    public static final String EVENT_SEEKING= "seeking";
    /**
     *  MEDIA事件-在浏览器不论何种原因未能取回媒介数据时运行的脚本。
     */
    public static final String EVENT_STALLED= "stalled";
    /**
     *  MEDIA事件-在媒介数据完全加载之前不论何种原因终止取回媒介数据时运行的脚本。
     */
    public static final String EVENT_SUSPEND= "suspend";
    /**
     * MEDIA事件-当播放位置改变时（比如当用户快进到媒介中一个不同的位置时）运行的脚本。
     */
    public static final String EVENT_TIMEUPDATE= "timeupdate";
    
    /**
     *  MEDIA事件-每当音量改变时（包括将音量设置为静音）时运行的脚本。
     */
    public static final String EVENT_VOLUMECHANGE= "volumechange";
    /**
     * MEDIA事件-当媒介已停止播放但打算继续播放时（比如当媒介暂停已缓冲更多数据）运行脚本
     */
    public static final String EVENT_WAITING= "waiting";

}
