package com.rongji.dfish.ui;

import java.util.Map;

/**
 * 可以被设置动作的对象。
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
public interface EventTarget<T extends EventTarget<T>> {
    //http://www.w3school.com.cn/tags/html_ref_eventattributes.asp

    /**
     * 设置事件触发动作
     * <p>设置这个对象的某个事件，将触发脚本</p>
     *
     * @param eventName 事件的名称，如 click等
     * @param script    触发的动作脚本，当这个值为空的时候，表示取消该动作
     * @return T this
     */
    T setOn(String eventName, String script);

    /**
     * 获取事件的名称和动作。
     * 如果为空则这个部件上没有绑定事件
     *
     * @return 如果为空则这个部件上没有绑定事件
     */
    Map<String, String> getOn();


    /**
     * DFISH事件-当widget在页面上生成HTML dom对象后触发
     */
    String EVENT_READY = "ready";
    /**
     * DFISH事件-当widget的子节点有增删改其中一种变化时触发
     */
    String EVENT_NODECHANGE = "nodeChange";
    /**
     * DFISH事件-当 leaf tr 等widget 展开时触发
     */
    String EVENT_EXPAND = "expand";
    /**
     * DFISH事件-当 leaf tr 等widget收起时触发
     */
    String EVENT_COLLAPSE = "collapse";
    /**
     * DFISH事件-当 dialog menu 关闭时触发
     */
    String EVENT_CLOSE = "close";
    /**
     * DFISH事件-当 widget移除时触发
     */
    String EVENT_REMOVE = "remove";
    /*  以下是WINDOW事件  */
    /**
     * WINDOW事件-文档打印之后运行的脚本
     */
    String EVENT_AFTERPRINT = "afterPrint";
    /**
     * WINDOW事件-文档打印之前运行的脚本
     */
    String EVENT_BEFOREPRINT = "beforePrint";
    /**
     * WINDOW事件-文档卸载之前运行的脚本
     */
    String EVENT_BEFOREUNLOAD = "beforeUnload";
    /**
     * WINDOW事件-在错误发生时运行的脚本
     */
    String EVENT_ERROR = "error";
    /**
     * WINDOW事件-当前 URL 的锚部分(以 '#' 号为开始) 发生改变时触发的脚本
     */
    String EVENT_HASHCHANGE = "hashChange";
    /**
     * WINDOW事件-页面结束加载之后触发
     */
    String EVENT_LOAD = "load";
    /**
     * WINDOW事件-在消息被触发时运行的脚本
     */
    String EVENT_MESSAGE = "message";
    /**
     * WINDOW事件-当文档离线时运行的脚本
     */
    String EVENT_OFFLINE = "offline";
    /**
     * WINDOW事件-当文档上线时运行的脚本
     */
    String EVENT_ONLINE = "online";
    /**
     * WINDOW事件-当窗口隐藏时运行的脚本
     */
    String EVENT_PAGEHIDE = "pageHide";
    /**
     * WINDOW事件-当窗口成为可见时运行的脚本
     */
    String EVENT_PAGESHOW = "pageShow";
    /**
     * WINDOW事件-当窗口历史记录改变时运行的脚本
     */
    String EVENT_POPSTATE = "popState";
    /**
     * WINDOW事件-当文档执行撤销（redo）时运行的脚本
     */
    String EVENT_REDO = "redo";
    /**
     * WINDOW事件-当浏览器窗口被调整大小时触发
     */
    String EVENT_RESIZE = "resize";
    /**
     * WINDOW事件-在 Web Storage 区域更新后运行的脚本
     */
    String EVENT_STORAGE = "storage";
    /**
     * WINDOW事件-在文档执行 undo 时运行的脚本
     */
    String EVENT_UNDO = "undo";
    /**
     * WINDOW事件-一旦页面已下载时触发（或者浏览器窗口已被关闭）
     */
    String EVENT_UNLOAD = "unload";

    /* 以下是FORM 事件 */
    /**
     * FORM事件-元素失去焦点时运行的脚本
     */
    String EVENT_BLUR = "blur";

    /**
     * FORM事件-在元素值被改变前运行的脚本
     */
    String EVENT_BEFORECHANGE = "beforeChange";
    /**
     * FORM事件-在元素值被改变时运行的脚本
     */
    String EVENT_CHANGE = "change";
    /**
     * FORM事件-当上下文菜单被触发时运行的脚本
     */
    String EVENT_CONTEXTMENU = "contextMenu";
    /**
     * FORM事件-当元素失去焦点时运行的脚本
     */
    String EVENT_FOCUS = "focus";
    /**
     * FORM事件-在表单改变时运行的脚本
     */
    String EVENT_FORMCHANGE = "formChange";
    /**
     * FORM事件-当表单获得用户输入时运行的脚本
     */
    String EVENT_FORMINPUT = "formInput";
    /**
     * FORM事件-当元素获得用户输入时运行的脚本
     */
    String EVENT_INPUT = "input";
    /**
     * FORM事件-当元素无效时运行的脚本
     */
    String EVENT_INVALID = "invalid";
    /**
     * FORM事件-当表单中的重置按钮被点击时触发HTML5 中不支持
     */
    String EVENT_RESET = "reset";
    /**
     * FORM事件-在元素中文本被选中后触发
     */
    String EVENT_SELECT = "select";
    /**
     * FORM事件-在提交表单时触发
     */
    String EVENT_SUBMIT = "submit";

    /**
     * FORM事件-在表单控件认证完成(暂时仅有滑块组件使用)
     */
    String EVENT_AUTH = "auth";

    /* 以下是KEYBOARD 事件 */
    /**
     * KEYBOARD事件-在用户按下按键时触发。
     */
    String EVENT_KEYDOWN = "keyDown";
    /**
     * KEYBOARD事件-在用户敲击按钮时触发。
     */
    String EVENT_KEYPRESS = "keyPress";
    /**
     * KEYBOARD事件-当用户释放按键时触发。
     */
    String EVENT_KEYUP = "keyUp";

    /* 以下是MOUSE事件 */
    /**
     * MOUSE事件-元素上发生鼠标点击时触发。
     */
    String EVENT_CLICK = "click";
    /**
     * MOUSE事件-元素上发生鼠标双击时触发。
     */
    String EVENT_DBLCLICK = "dblClick";
    /**
     * MOUSE事件-元素被拖动时运行的脚本。
     */
    String EVENT_DRAG = "drag";
    /**
     * MOUSE事件-在拖动操作末端运行的脚本。
     */
    String EVENT_DRAGEND = "dragend";
    /**
     * MOUSE事件-当元素元素已被拖动到有效拖放区域时运行的脚本。
     */
    String EVENT_DRAGENTER = "dragEnter";
    /**
     * MOUSE事件-当元素离开有效拖放目标时运行的脚本。
     */
    String EVENT_DRAGLEAVE = "dragLeave";
    /**
     * MOUSE事件-当元素在有效拖放目标上正在被拖动时运行的脚本。
     */
    String EVENT_DRAGOVER = "dragOver";
    /**
     * MOUSE事件-在拖动操作开端运行的脚本。
     */
    String EVENT_DRAGSTART = "dragStart";
    /**
     * MOUSE事件-当被拖元素正在被拖放时运行的脚本。
     */
    String EVENT_DROP = "drop";
    /**
     * MOUSE事件-当元素上按下鼠标按钮时触发。
     */
    String EVENT_MOUSEDOWN = "mouseDown";
    /**
     * MOUSE事件-当鼠标指针移动到元素上时触发。
     */
    String EVENT_MOUSEMOVE = "mouseMove";
    /**
     * MOUSE事件-当鼠标指针移出元素时触发。
     */
    String EVENT_MOUSEOUT = "mouseOut";
    /**
     * MOUSE事件-当鼠标指针移动到元素上时触发。
     */
    String EVENT_MOUSEOVER = "mouseOver";
    /**
     * MOUSE事件-当在元素上释放鼠标按钮时触发。
     */
    String EVENT_MOUSEUP = "mouseUp";
    /**
     * MOUSE事件-当鼠标滚轮正在被滚动时运行的脚本。
     */
    String EVENT_MOUSEWHEEL = "mouseWheel";
    /**
     * MOUSE事件-当元素滚动条被滚动时运行的脚本。
     */
    String EVENT_SCROLL = "scroll";
    /* 以下是MEDIA 事件 */
    /**
     * MEDIA事件-在退出时运行的脚本。
     */
    String EVENT_ABORT = "abort";
    /**
     * MEDIA事件-当文件就绪可以开始播放时运行的脚本（缓冲已足够开始时）。
     */
    String EVENT_CANPLAY = "canplay";
    /**
     * MEDIA事件-当媒介能够无需因缓冲而停止即可播放至结尾时运行的脚本。
     */
    String EVENT_CANPLAYTHROUGH = "canPlayThrough";
    /**
     * MEDIA事件-当媒介长度改变时运行的脚本。
     */
    String EVENT_DURATIONCHANGE = "durationChange";
    /**
     * MEDIA事件-当发生故障并且文件突然不可用时运行的脚本（比如连接意外断开时）。
     */
    String EVENT_EMPTIED = "emptied";
    /**
     * MEDIA事件-当媒介已到达结尾时运行的脚本（可发送类似“感谢观看”之类的消息）。
     */
    String EVENT_ENDED = "ended";
//    /**
//     *  MEDIA事件-当在文件加载期间发生错误时运行的脚本。 与window事件重复
//     */
//    String EVENT_ERROR= "error";
    /**
     * MEDIA事件-当元数据（比如分辨率和时长）被加载时运行的脚本。
     */
    String EVENT_LOADEDMETADATA = "loadedMetadata";
    /**
     * MEDIA事件-在文件开始加载且未实际加载任何数据前运行的脚本。
     */
    String EVENT_LOADSTART = "loadStart";
    /**
     * MEDIA事件-当媒介被用户或程序暂停时运行的脚本。
     */
    String EVENT_PAUSE = "pause";
    /**
     * MEDIA事件-当媒介已就绪可以开始播放时运行的脚本。
     */
    String EVENT_PLAY = "play";
    /**
     * MEDIA事件-当媒介已开始播放时运行的脚本。
     */
    String EVENT_PLAYING = "playing";
    /**
     * MEDIA事件-当浏览器正在获取媒介数据时运行的脚本。
     */
    String EVENT_PROGRESS = "progress";
    /**
     * MEDIA事件-每当回放速率改变时运行的脚本（比如当用户切换到慢动作或快进模式）。
     */
    String EVENT_RATECHANGE = "rateChange";
    /**
     * MEDIA事件-每当就绪状态改变时运行的脚本（就绪状态监测媒介数据的状态）。
     */
    String EVENT_READYSTATECHANGE = "readyStateChange";
    /**
     * MEDIA事件-当 seeking 属性设置为 false（指示定位已结束）时运行的脚本。
     */
    String EVENT_SEEKED = "seeked";
    /**
     * MEDIA事件-当 seeking 属性设置为 true（指示定位是活动的）时运行的脚本。
     */
    String EVENT_SEEKING = "seeking";
    /**
     * MEDIA事件-在浏览器不论何种原因未能取回媒介数据时运行的脚本。
     */
    String EVENT_STALLED = "stalled";
    /**
     * MEDIA事件-在媒介数据完全加载之前不论何种原因终止取回媒介数据时运行的脚本。
     */
    String EVENT_SUSPEND = "suspend";
    /**
     * MEDIA事件-当播放位置改变时（比如当用户快进到媒介中一个不同的位置时）运行的脚本。
     */
    String EVENT_TIMEUPDATE = "timeUpdate";

    /**
     * MEDIA事件-每当音量改变时（包括将音量设置为静音）时运行的脚本。
     */
    String EVENT_VOLUMECHANGE = "volumeChange";
    /**
     * MEDIA事件-当媒介已停止播放但打算继续播放时（比如当媒介暂停已缓冲更多数据）运行脚本
     */
    String EVENT_WAITING = "waiting";

}
