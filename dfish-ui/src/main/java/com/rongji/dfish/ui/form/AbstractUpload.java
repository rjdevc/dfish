package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Directional;
import com.rongji.dfish.ui.VAlignable;
import com.rongji.dfish.ui.command.Ajax;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractUpload为抽象的上传组件，上传组件在dfish3中默认有两种，提供文件上传和图片上传
 * 图片上传同时还有预览功能。
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @version 3.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 1.0
 */
public abstract class AbstractUpload<T extends AbstractUpload<T>> extends AbstractFormElement<T, List<UploadItem>> implements Directional<T>, Alignable<T>, VAlignable<T> {

    private static final long serialVersionUID = 4779985030349561966L;

    protected UploadPost post;
    protected String download;
    protected Ajax preview;
    protected String remove;
    protected String fileTypes;
    protected String minFileSize;
    protected String maxFileSize;
    protected Integer uploadLimit;
    protected List<ValueButton> valueButton;
    protected List<UploadButton> uploadButton;
    protected String dir;
    protected String align;
    protected String vAlign;
    protected UploadItem pub;

    /**
     * 上传地址
     *
     * @return String
     */
    public UploadPost getPost() {
        return this.post;
    }

    private UploadPost post() {
        if (this.post == null) {
            this.post = new UploadPost();
        }
        return this.post;
    }

    /**
     * 上传地址
     *
     * @param post UploadPost
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPost(UploadPost post) {
        this.post = post;
        return (T) this;
    }

    /**
     * 下载地址
     *
     * @return String
     */
    public String getDownload() {
        return this.download;
    }

    /**
     * 下载地址
     *
     * @param download String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setDownload(String download) {
        this.download = download;
        return (T) this;
    }

    /**
     * 预览地址
     *
     * @return String
     */
    public Ajax getPreview() {
        return this.preview;
    }

    protected Ajax preview() {
        if (this.preview == null) {
            this.preview = new Ajax(null);
        }
        return this.preview;
    }

    /**
     * 预览地址
     *
     * @param preview Ajax
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPreview(Ajax preview) {
        this.preview = preview;
        return (T) this;
    }

    /**
     * 移除地址
     *
     * @return String
     */
    public String getRemove() {
        return this.remove;
    }

    /**
     * 移除地址,默认无需设置该属性
     *
     * @param remove String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setRemove(String remove) {
        this.remove = remove;
        return (T) this;
    }

    /**
     * 上传地址
     *
     * @return String
     * @see #getPost()
     */
    @Deprecated
    public String getUploadsrc() {
        return this.post != null ? this.post.getSrc() : null;
    }

    /**
     * 上传地址
     *
     * @param uploadsrc String
     * @return 本身，这样可以继续设置其他属性
     * @see #setPost(UploadPost)
     */
    @Deprecated
    public T setUploadsrc(String uploadsrc) {
        post().setSrc(uploadsrc);
        return (T) this;
    }

    /**
     * 下载地址
     *
     * @return String
     * @see #getDownload()
     */
    @Deprecated
    public String getDownloadsrc() {
        return getDownload();
    }

    /**
     * 下载地址
     *
     * @param downloadsrc String
     * @return 本身，这样可以继续设置其他属性
     * @see #setDownload(String)
     */
    @Deprecated
    public T setDownloadsrc(String downloadsrc) {
        return setDownload(downloadsrc);
    }

    /**
     * 预览地址
     *
     * @return String
     * @see #getPreview()
     */
    @Deprecated
    public String getPreviewsrc() {
        return this.preview != null ? this.preview.getSrc() : null;
    }

    /**
     * 预览地址
     *
     * @param previewsrc String
     * @return 本身，这样可以继续设置其他属性
     * @see #setPreview(Ajax)
     */
    @Deprecated
    public T setPreviewsrc(String previewsrc) {
        preview().setSrc(previewsrc);
        return (T) this;
    }

    /**
     * 移除地址
     *
     * @return String
     * @see #getRemove()
     */
    @Deprecated
    public String getRemovesrc() {
        return getRemove();
    }

    /**
     * 移除地址,默认无需设置该属性
     *
     * @param removesrc String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setRemovesrc(String removesrc) {
        return setRemove(removesrc);
    }

    /**
     * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
     *
     * @return String
     */
    public String getFileTypes() {
        return fileTypes;
    }

    /**
     * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
     *
     * @param fileTypes String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFileTypes(String fileTypes) {
        this.fileTypes = fileTypes;
        return (T) this;
    }

    /**
     * 单个附件文件最小大小。如 "1K"。
     *
     * @return String
     */
    public String getMinFileSize() {
        return minFileSize;
    }

    /**
     * 单个附件文件最小大小。如 "1K"。
     *
     * @param minFileSize String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setMinFileSize(String minFileSize) {
        this.minFileSize = minFileSize;
        return (T) this;
    }

    /**
     * 单个附件文件最大大小。如 "50M"。
     *
     * @return String
     */
    public String getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * 单个附件文件最大大小。如 "50M"。
     *
     * @param maxFileSize String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
        return (T) this;
    }

    /**
     * 最大允许上传文件数
     *
     * @return Integer
     */
    public Integer getUploadLimit() {
        return uploadLimit;
    }

    /**
     * 最大允许上传文件数
     *
     * @param uploadLimit 最大允许上传文件数
     * @return 本身，这样可以继续设置其他属性
     */
    public T setUploadLimit(Integer uploadLimit) {
        this.uploadLimit = uploadLimit;
        return (T) this;
    }

    /**
     * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
     *
     * @return List&lt;{@link ValueButton}&gt;
     */
    public List<ValueButton> getValueButton() {
        return valueButton;
    }

    /**
     * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
     *
     * @param valueButton "更多"选项 button 数组
     * @return 本身，这样可以继续设置其他属性
     */
    public T setValueButton(List<ValueButton> valueButton) {
        this.valueButton = valueButton;
        return (T) this;
    }

    /**
     * 添加附件项的"更多"选项按钮。点击附件项的"更多"生成一个 menu。
     *
     * @param button "更多"选项按钮
     * @return 本身，这样可以继续设置其他属性
     */
    public T addValuebutton(ValueButton button) {
        if (this.valueButton == null) {
            this.valueButton = new ArrayList<>();
        }
        valueButton.add(button);
        return (T) this;
    }

    /**
     * 上传按钮
     *
     * @return List&lt;{@link UploadButton}&gt;
     */
    public List<UploadButton> getUploadButton() {
        return uploadButton;
    }

    /**
     * 上传按钮
     *
     * @param uploadButton 上传按钮
     * @return 本身，这样可以继续设置其他属性
     */
    public T setUploadButton(List<UploadButton> uploadButton) {
        this.uploadButton = uploadButton;
        return (T) this;
    }

    /**
     * 添加上传按钮
     *
     * @param button 上传按钮
     * @return 本身，这样可以继续设置其他属性
     */
    public T addUploadbutton(UploadButton button) {
        if (this.uploadButton == null) {
            this.uploadButton = new ArrayList<>();
        }
        this.uploadButton.add(button);
        return (T) this;
    }

    /**
     * 上传地址。
     *
     * @return String
     * @see #getUploadsrc()
     */
    @Deprecated
    public String getUpload_url() {
        return getUploadsrc();
    }

    /**
     * 上传地址。
     *
     * @param upload_url 上传地址
     * @return 本身，这样可以继续设置其他属性
     * @see #setUploadsrc(String)
     */
    @Deprecated
    public T setUpload_url(String upload_url) {
        return setUploadsrc(upload_url);
    }

    /**
     * 下载地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
     *
     * @return down_url
     * @see #getDownloadsrc()
     */
    @Deprecated
    public String getDown_url() {
        return getDownloadsrc();
    }

    /**
     * 下载地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
     *
     * @param down_url 下载地址
     * @return 本身，这样可以继续设置其他属性
     * @see #setDownloadsrc(String)
     */
    @Deprecated
    public T setDown_url(String down_url) {
        return setDownloadsrc(down_url);
    }

    /**
     * 文件预览地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
     *
     * @return 文件预览地址
     * @see #getPreviewsrc()
     */
    @Deprecated
    public String getPreview_url() {
        return getPreviewsrc();
    }

    /**
     * 文件预览地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
     *
     * @param preview_url 文件预览地址
     * @return 本身，这样可以继续设置其他属性
     * @see #setPreviewsrc(String)
     */
    @Deprecated
    public T setPreview_url(String preview_url) {
        return setPreviewsrc(preview_url);
    }

    /**
     * 单个附件最大体积。如 "50M"。
     *
     * @return file_size_limit
     * @see #getMaxFileSize()
     */
    @Deprecated
    public String getFile_size_limit() {
        return getMaxFileSize();
    }

    /**
     * 单个附件最大体积。如 "50M"。
     *
     * @param file_size_limit 单个附件最大体积。如 "50M"。
     * @return 本身，这样可以继续设置其他属性
     * @see #setMaxFileSize(String)
     */
    @Deprecated
    public T setFile_size_limit(String file_size_limit) {
        return setMaxFileSize(file_size_limit);
    }

    /**
     * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
     *
     * @return file_types
     * @see #getFileTypes()
     */
    @Deprecated
    public String getFile_types() {
        return getFileTypes();
    }

    /**
     * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
     *
     * @param file_types 文件类型
     * @return 本身，这样可以继续设置其他属性
     * @see #setFileTypes(String)
     */
    @Deprecated
    public T setFile_types(String file_types) {
        return setFileTypes(file_types);
    }

    /**
     * 最多可上传数量。
     *
     * @return file_upload_limit
     * @see #getUploadLimit()
     */
    @Deprecated
    public Integer getFile_upload_limit() {
        return getUploadLimit();
    }

    /**
     * 最大允许上传文件数
     *
     * @param file_upload_limit 最大允许上传文件数
     * @return 本身，这样可以继续设置其他属性
     * @see #setUploadLimit(Integer)
     */
    @Deprecated
    public T setFile_upload_limit(Integer file_upload_limit) {
        return setUploadLimit(file_upload_limit);
    }

    /**
     * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
     *
     * @return value_button
     * @see #getValueButton()
     */
    @Deprecated
    public List<ValueButton> getValue_button() {
        return getValueButton();
    }

    /**
     * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
     *
     * @param value_button "更多"选项 button 数组
     * @return 本身，这样可以继续设置其他属性
     * @see #addValuebutton(ValueButton)
     */
    @Deprecated
    public T addValue_button(ValueButton value_button) {
        return addValuebutton(value_button);
    }

    /**
     * 上传按钮的数组。
     *
     * @return upload_button
     * @see #getUploadButton()
     */
    @Deprecated
    public List<UploadButton> getUpload_button() {
        return getUploadButton();
    }

    /**
     * 增加上传按钮
     *
     * @param upload_button 上传按钮
     * @return 本身，这样可以继续设置其他属性
     * @see #addUploadbutton(UploadButton)
     */
    @Deprecated
    public T addUpload_button(UploadButton upload_button) {
        return addUploadbutton(upload_button);
    }

    /**
     * 取得默认值
     *
     * @return UploadItem
     */
    public UploadItem getPub() {
        if (pub == null) {
            pub = new UploadItem();
        }
        return pub;
    }

    /**
     * 设置默认值
     *
     * @param pub UploadItem
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPub(UploadItem pub) {
        this.pub = pub;
        return (T) this;
    }

    /**
     * 附件排列方向。可选值: h(横向,默认), v(纵向)
     *
     * @return String
     */
    @Override
    public String getDir() {
        return dir;
    }

    /**
     * 附件排列方向。可选值: h(横向,默认), v(纵向)
     *
     * @param dir String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setDir(String dir) {
        this.dir = dir;
        return (T) this;
    }

    @Deprecated
    @Override
    public T setTip(Boolean tip) {
        return (T) this;
    }

    @Deprecated
    @Override
    public T setTip(String tip) {
        return (T) this;
    }

    /**
     * 附件上传方案
     *
     * @return String
     */
    public String getScheme() {
        return (String) this.getData("scheme");
    }

    /**
     * 附件上传方案
     *
     * @param scheme String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setScheme(String scheme) {
        this.setData("scheme", scheme);
        return (T) this;
    }

    @Override
    public T setValue(Object value) {
        if (value == null) {
            this.value = null;
            return (T) this;
        }
        if (!(value instanceof List)) {
            // 具体项如果不是UploadItem如何判断不清楚怎么写
            throw new IllegalArgumentException("The value must be List<UploadItem>.");
        }
        return setValue((List<UploadItem>) value);
    }

    /**
     * 设置表单的元素的值
     * <p>在HTML协议中提交表单元素的时候，以键值对的方式提交。
     * 一般情况下。其中值就是value. 某些多选项的元素可能这些键值对会以复数形式出现。
     * 比如select 多行形式时。</p>
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     *
     * @param value {@link List}&lt;{@link UploadItem}&gt;
     * @return 本身，这样可以继续设置其他属性
     */
    public T setValue(List<UploadItem> value) {
        this.value = value;
        return (T) this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public T setAlign(String align) {
        this.align = align;
        return (T) this;
    }

    @Override
    public String getVAlign() {
        return vAlign;
    }

    @Override
    public T setVAlign(String vAlign) {
        this.vAlign = vAlign;
        return (T) this;
    }

}
