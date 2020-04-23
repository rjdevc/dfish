package com.rongji.dfish.ui.plugin.ueditor;

import com.rongji.dfish.ui.widget.Html;

/**
 * UEditor专用的Html组件，能够更好地展示编辑器的内容
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class UEditorHtml extends Html {

    private static final long serialVersionUID = -3872917571270143970L;

    /**
     * 构造函数
     *
     * @param text html内容。文本支持 &lt;d:wg&gt; 标签。
     */
    public UEditorHtml(String text) {
        super(text);
    }

}
