package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.LazyLoad;

/**
 * 该命令用于打开一个对话框(Dialog)
 *
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 2.0
 */
public class Dialog extends AbstractDialog<Dialog> implements Command<Dialog>, LazyLoad<Dialog> {

    private static final long serialVersionUID = -3055223672741088528L;

    private String template;
    private String preload;
    private String src;
    private Boolean sync;
    private String success;
    private String error;
    private String complete;
    private String filter;
    private Boolean prong;

    @Override
    public String getType() {
        return "Dialog";
    }

    /**
     * 打开对话框命令
     *
     * @param id      String 编号
     * @param preload 预加载模板
     * @param title   String 标题栏内容
     * @param width   String 窗口宽度
     * @param height  String 窗口高度
     * @param pos     DialogPosition 窗口在屏幕位置
     * @param src     String 窗口数据的URL
     */
    @Deprecated
    public Dialog(String id, String preload, String title,
                  int width, int height, String pos,
                  String src) {
        this.id = id;
        this.preload = preload;
        this.title = title;
        this.setWidth(width);
        this.setHeight(height);
        this.position = pos;
        this.src = src;
    }

    /**
     * 打开对话框命令
     *
     * @param id      String 编号
     * @param preload 预加载模板
     * @param title   String 标题栏内容
     * @param width   String 窗口宽度
     * @param height  String 窗口高度
     * @param pos     DialogPosition 窗口在屏幕位置
     * @param src     String 窗口数据的URL
     */
    public Dialog(String id, String preload, String title,
                  String width, String height, String pos,
                  String src) {
        this.id = id;
        this.preload = preload;
        this.title = title;
        this.setWidth(width);
        this.setHeight(height);
        this.position = pos;
        this.src = src;
    }

    /**
     * 打开对话框命令
     *
     * @param id    String 编号
     * @param title String 标题栏内容
     * @param src   String 窗口数据的URL
     */
    public Dialog(String id, String title, String src) {
        this.id = id;
        this.title = title;
        this.src = src;
    }

    /**
     * 打开对话框命令
     *
     * @param src String 窗口数据的URL
     */
    public Dialog(String src) {
        this.src = src;
    }

    /**
     * 打开对话框命令
     */
    public Dialog() {

    }

    /**
     * 设置窗口数据的URL
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Dialog setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 设置窗口使用的模板编号
     *
     * @param template 模板
     * @return this
     */
    @Override
    public Dialog setTemplate(String template) {
        this.template = template;
        return this;
    }

    /**
     * 加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。
     *
     * @return src
     */
    @Override
    public String getSrc() {
        return src;
    }

    /**
     * 模板ID。
     *
     * @return template
     */
    @Override
    public String getTemplate() {
        return template;
    }

    /**
     * 如果设置为true，显示一个箭头，指向snap 参数对象
     *
     * @return Boolean
     */
    public Boolean getProng() {
        return prong;
    }

    /**
     * 如果设置为true，显示一个箭头，指向snap 参数对象
     *
     * @param prong Boolean
     * @return this
     */
    public Dialog setProng(Boolean prong) {
        this.prong = prong;
        return this;
    }

    @Override
    public String getPreload() {
        return preload;
    }

    @Override
    public Dialog setPreload(String preload) {
        this.preload = preload;
        return this;
    }

    @Override
    public String getSuccess() {
        return success;
    }

    @Override
    public Dialog setSuccess(String success) {
        this.success = success;
        return this;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public Dialog setError(String error) {
        this.error = error;
        return this;
    }

    @Override
    public String getComplete() {
        return complete;
    }

    @Override
    public Dialog setComplete(String complete) {
        this.complete = complete;
        return this;
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public Dialog setFilter(String filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public Boolean getSync() {
        return sync;
    }

    @Override
    public Dialog setSync(Boolean sync) {
        this.sync = sync;
        return this;
    }

}
