package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.DataContainer;
import com.rongji.dfish.ui.HasSrc;

/**
 * CommunicateCommand 是需要和后台交互的命令，包括 AJAX和Submit
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public abstract class CommunicateCommand<T extends CommunicateCommand<T>> extends AbstractCommand<T> implements DataContainer<T>, HasSrc<T> {

    private static final long serialVersionUID = -2296464991066275984L;
    /**
     * http 格式的路径。
     */
    protected String src;
    /**
     * http 格式的路径。
     */
    protected String template;
    /**
     * 是否同步。
     */
    protected Boolean sync;
    /**
     * 是否启用下载模式
     */
    protected Boolean download;
    /**
     * js语句，在发送请求前调用
     */
    protected String beforeSend;
    /**
     * js语句，在获取服务器的响应数据后调用(不论成功失败都会执行)。
     */
    protected String complete;
    /**
     * js语句，在获取服务器的响应数据失败后调用。
     */
    protected String error;
    /**
     * js语句，在成功获取服务器的响应数据后调用。
     */
    protected String success;
    /**
     * js语句，执行该语句而不是执行命令
     */
    protected String filter;
    protected Object loading;

    /**
     * 最简构造函数
     *
     * @param src String
     */
    public CommunicateCommand(String src) {
        this.src = src;
    }

    /**
     * http 格式的路径。
     *
     * @return src
     */
    @Override
    public String getSrc() {
        return src;
    }

    /**
     * 通信地址 http 格式的路径。
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setSrc(String src) {
        this.src = src;
        return (T) this;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    /**
     * 通信地址 http 格式的路径。
     *
     * @param template String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setTemplate(String template) {
        this.template = template;
        return (T) this;
    }

    /**
     * 是否同步
     *
     * @return Boolean
     */
    @Override
    public Boolean getSync() {
        return sync;
    }

    /**
     * 是否同步
     *
     * @param sync Boolean
     * @return this
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setSync(Boolean sync) {
        this.sync = sync;
        return (T) this;
    }

    /**
     * js语句，在发送请求前调用。
     *
     * @return String
     */
    public String getBeforeSend() {
        return beforeSend;
    }

    /**
     * js语句，在发送请求前调用。
     *
     * @param beforeSend String
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setBeforeSend(String beforeSend) {
        this.beforeSend = beforeSend;
        return (T) this;
    }

    /**
     * js语句，在获取服务器的响应数据后调用(不论成功失败都会执行)。
     *
     * @return String
     */
    @Override
    public String getComplete() {
        return complete;
    }

    /**
     * js语句，在获取服务器的响应数据后调用(不论成功失败都会执行)。
     *
     * @param complete String
     * @return this
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setComplete(String complete) {
        this.complete = complete;
        return (T) this;
    }

    /**
     * js语句，在获取服务器的响应数据失败后调用。
     *
     * @return String
     */
    @Override
    public String getError() {
        return error;
    }

    /**
     * js语句，在获取服务器的响应数据失败后调用。
     *
     * @param error String
     * @return this
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setError(String error) {
        this.error = error;
        return (T) this;
    }

    /**
     * js语句，在成功获取服务器的响应数据后调用。
     *
     * @return String
     */
    @Override
    public String getSuccess() {
        return success;
    }

    /**
     * js语句，在成功获取服务器的响应数据后调用。
     *
     * @param success String
     * @return this
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setSuccess(String success) {
        this.success = success;
        return (T) this;
    }

    /**
     * 启用download模式，download模式不在需要返回一个json
     *
     * @return download Boolean
     */
    public Boolean getDownload() {
        return download;
    }

    /**
     * 启用download模式，download模式不在需要返回一个json
     *
     * @param download Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setDownload(Boolean download) {
        this.download = download;
        return (T) this;
    }

    /**
     * 加载时文本提示
     *
     * @return LoadingCommand
     */
    public Object getLoading() {
        return loading;
    }

    /**
     * 加载时文本提示
     *
     * @param loading LoadingCommand
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setLoading(Loading loading) {
        this.loading = loading;
        return (T) this;
    }

    /**
     * 加载时提示
     *
     * @param loading Boolean
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setLoading(Boolean loading) {
        this.loading = loading;
        return (T) this;
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFilter(String filter) {
        this.filter = filter;
        return (T) this;
    }


}
