package com.rongji.dfish.ui.widget;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Alignable;

import java.util.List;

/**
 * 翻页工具条
 *
 * @author DFish Team
 */
public class PageBar extends AbstractWidget<PageBar> implements Alignable<PageBar> {

    private static final long serialVersionUID = -7425582244642151536L;

    /**
     * 小按钮风格的翻页工具条。
     */
    public static final String FACE_MINI = "mini";
    /**
     * 文本风格的翻页工具条。
     */
    public static final String FACE_TEXT = "text";
    /**
     * 组合按钮风格的翻页工具条。
     */
    public static final String FACE_BUTTON_GROUP = "buttonGroup";
    /**
     * 无边框风格的翻页工具条。
     */
    public static final String FACE_BUTTON_NONE = "none";

    /**
     * 显示皮肤
     */
    private String face;
    /**
     * 如果设置了name，将生成一个隐藏项，值为当前页数
     */
    private String name;
    /**
     * 水平居中。可用值: left,right,center
     */
    private String align;
    /**
     * 按钮样式。
     */
    private String buttonCls;
    /**
     * 中间有几个显示页数的按钮。
     */
    private Integer buttonCount;
    /**
     * 显示一个可填写页数的表单。
     */
    private Boolean jump;
    /**
     * 是否支持按键翻页。设置为true时，可按“←→”进行翻页
     */
    private Boolean keyJump;
    /**
     * 不显示"首页"和"尾页"两个按钮。
     */
    private Boolean noFirstLast;
    /**
     * 点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。
     */
    private String src;
    /**
     * 绑定一个支持前端翻页的widget(例如grid)。
     */
    private String target;
    /**
     * 设置为true，可去除边框背景等预设样式。
     */
    private Boolean transparent;
    private String firstText;
    private String lastText;
    private String nextText;
    private String prevText;
    private String info;
    private List<Button> setting;
    private String dropAlign;
    private Boolean buttonSumPage;
    private Integer offset;
    private Integer size;
    private Integer limit;

    /**
     * 构造函数
     *
     * @param id String
     * @see #FACE_BUTTON_GROUP
     * @see #FACE_MINI
     * @see #FACE_TEXT
     */
    public PageBar(String id) {
        setId(id);
    }

    /**
     * 构造函数
     */
    public PageBar() {
    }

    public String getFace() {
        return face;
    }

    public PageBar setFace(String face) {
        this.face = face;
        return this;
    }

    /**
     * 当前页数。(起始值为1)
     *
     * @return Integer
     * @see #getOffset()
     */
    @Deprecated
    public Integer getCurrentPage() {
        if (offset == null || limit == null) {
            return null;
        }
        return offset / limit + 1;
    }

    /**
     * 当前页数。(起始值为1)
     *
     * @param currentPage Integer
     * @return 本身，这样可以继续设置其他属性
     * @see #setOffset(Integer)
     */
    @Deprecated
    public PageBar setCurrentPage(Integer currentPage) {
        if (currentPage != null && limit != null) {
            setOffset(limit * (currentPage - 1));
        }
        return this;
    }

    /**
     * 分页大小，每页显示多少条
     *
     * @return Integer
     * @see #getLimit()
     */
    @Deprecated
    public Integer getPageSize() {
        return getLimit();
    }

    /**
     * 分页大小，每页显示多少条
     *
     * @param pageSize Integer
     * @return 本身，这样可以继续设置其他属性
     * @see #setLimit(Integer)
     */
    @Deprecated
    public PageBar setPageSize(Integer pageSize) {
        return setLimit(pageSize);
    }

    /**
     * 总页数。(起始值为1)
     *
     * @return Integer
     * @see #getSize()
     */
    @Deprecated
    public Integer getSumPage() {
        if (size == null || limit == null) {
            return null;
        }
        return (size - 1) / limit + 1;
    }

    /**
     * 总页数。(起始值为1)
     *
     * @param sumPage Integer
     * @return 本身，这样可以继续设置其他属性
     * @see #setSize(Integer)
     */
    @Deprecated
    public PageBar setSumPage(Integer sumPage) {
        return this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public PageBar setAlign(String align) {
        this.align = align;
        return this;
    }

    /**
     * 按钮样式。
     *
     * @return btncls
     */
    public String getButtonCls() {
        return buttonCls;
    }

    /**
     * 按钮样式。
     *
     * @param buttonCls String
     * @return this
     */
    public PageBar setButtonCls(String buttonCls) {
        this.buttonCls = buttonCls;
        return this;
    }

    /**
     * 中间是否有显示页数的按钮。值为0或1。
     *
     * @return btncount
     */
    public Integer getButtonCount() {
        return buttonCount;
    }

    /**
     * 中间是否有显示页数的按钮。值为0或1。
     *
     * @param buttonCount Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setButtonCount(Integer buttonCount) {
        this.buttonCount = buttonCount;
        return this;
    }

    /**
     * 显示一个可直接填写页数的输入框，回车后直接跳往这个页号
     *
     * @return jump Boolean
     */
    public Boolean getJump() {
        return jump;
    }

    /**
     * 显示一个可直接填写页数的输入框，回车后直接跳往这个页号。
     *
     * @param jump Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setJump(Boolean jump) {
        this.jump = jump;
        return this;
    }

    /**
     * 是否支持按键翻页。设置为true时，可按“←→”进行翻页
     *
     * @return Boolean
     */
    public Boolean getKeyJump() {
        return keyJump;
    }

    /**
     * 是否支持按键翻页。设置为true时，可按“←→”进行翻页
     *
     * @param keyJump Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setKeyJump(Boolean keyJump) {
        this.keyJump = keyJump;
        return this;
    }

    /**
     * 不显示"首页"和"尾页"两个按钮。
     *
     * @return nofirstlast
     */
    public Boolean getNoFirstLast() {
        return noFirstLast;
    }

    /**
     * 不显示"首页"和"尾页"两个按钮。
     *
     * @param noFirstLast Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setNoFirstLast(Boolean noFirstLast) {
        this.noFirstLast = noFirstLast;
        return this;
    }

    /**
     * 点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。
     *
     * @return src  String
     */
    public String getSrc() {
        return src;
    }

    /**
     * 点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 绑定一个支持前端翻页的widget(例如grid)。
     *
     * @return target
     */
    public String getTarget() {
        return target;
    }

    /**
     * 绑定一个支持前端翻页的widget(例如grid)。
     *
     * @param target String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 设置为true，可去除边框背景等预设样式。
     *
     * @return transparent Boolean
     */
    public Boolean getTransparent() {
        return transparent;
    }

    /**
     * 设置为true，可去除边框背景等预设样式。
     *
     * @param transparent Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setTransparent(Boolean transparent) {
        this.transparent = transparent;
        return this;
    }

    /**
     * 如果设置了name，将生成一个隐藏项，值为当前页数
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 如果设置了name，将生成一个隐藏项，值为当前页数
     *
     * @param name String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 首页标签名
     *
     * @return String
     */
    public String getFirstText() {
        return firstText;
    }

    /**
     * 设置首页标签名
     *
     * @param firstText String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setFirstText(String firstText) {
        this.firstText = firstText;
        return this;
    }

    /**
     * 尾页标签名
     *
     * @return String
     */
    public String getLastText() {
        return lastText;
    }

    /**
     * 设置尾页标签名
     *
     * @param lastText String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setLastText(String lastText) {
        this.lastText = lastText;
        return this;
    }

    /**
     * 下页标签名
     *
     * @return String
     */
    public String getNextText() {
        return nextText;
    }

    /**
     * 设置下页标签名
     *
     * @param nextText String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setNextText(String nextText) {
        this.nextText = nextText;
        return this;
    }

    /**
     * 上页标签名
     *
     * @return String
     */
    public String getPrevText() {
        return prevText;
    }

    /**
     * 设置上页标签名
     *
     * @param prevText String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setPrevText(String prevText) {
        this.prevText = prevText;
        return this;
    }

    /**
     * 显示总条数和总页数等信息。
     *
     * @return String
     */
    public String getInfo() {
        return info;
    }

    /**
     * 显示总条数和总页数等信息。
     *
     * @param info String
     * @return this
     */
    public PageBar setInfo(String info) {
        this.info = info;
        return this;
    }

    /**
     * button数组。生成一个配置按钮和下拉菜单。
     *
     * @return List
     */
    public List<Button> getSetting() {
        return setting;
    }

    /**
     * button数组。生成一个配置按钮和下拉菜单。
     *
     * @param setting List
     * @return this
     */
    public PageBar setSetting(List<Button> setting) {
        this.setting = setting;
        return this;
    }

    /**
     * 下拉按钮的位置
     *
     * @return String
     */
    public String getDropAlign() {
        return dropAlign;
    }

    /**
     * 下拉按钮的位置
     *
     * @param dropAlign String
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setDropAlign(String dropAlign) {
        this.dropAlign = dropAlign;
        return this;
    }

    /**
     * 显示总页数按钮
     *
     * @return Boolean
     */
    public Boolean getButtonSumPage() {
        return buttonSumPage;
    }

    /**
     * 显示总页数按钮
     *
     * @param buttonSumPage Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setButtonSumPage(Boolean buttonSumPage) {
        this.buttonSumPage = buttonSumPage;
        return this;
    }

    /**
     * 当前开始记录数(跳过记录条数)
     *
     * @return Integer
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * 当前开始记录数(跳过记录条数)
     *
     * @param offset Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 总记录数
     *
     * @return Integer
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 总记录数
     *
     * @param size Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setSize(Integer size) {
        this.size = size;
        return this;
    }

    /**
     * 一页限制的最大记录数
     *
     * @return Integer
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 一页限制的最大记录数
     *
     * @param limit Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 设置分页信息
     *
     * @param page Page
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setPage(Page page) {
        if (page == null) {
            return this;
        }
//        if (page.getCurrentPage() < 1) {
//            page.setCurrentPage(1);
//        }
//        if (page.getPageSize() < 1) {
//            page.setPageSize(10);
//        }
//        this.setCurrentPage(page.getCurrentPage());
//        int pageCount = page.getPageCount();
//        if (pageCount < 1) {
//            pageCount = 1;
//        }
//        this.setSumPage(pageCount);
//
//        this.putData("pageSize", page.getPageSize());
//        this.putData("rowCount", page.getRowCount());
//        this.putData("currentCount", page.getCurrentCount());
//        return this;
        return setPagination(Pagination.fromPage(page));
    }

    /**
     * 设置分页信息
     *
     * @param pagination Pagination
     * @return 本身，这样可以继续设置其他属性
     */
    public PageBar setPagination(Pagination pagination) {
        if (pagination == null) {
            return this;
        }
        return setOffset(pagination.getOffset()).setSize(pagination.getSize()).setLimit(pagination.getLimit());
    }

}
