package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.NodeContainerDecorator;
import com.rongji.dfish.ui.auxiliary.BoxBind;
import com.rongji.dfish.ui.command.Dialog;

import java.util.Arrays;
import java.util.List;

/**
 * SuggestionBox 默认可以通过填写出现输入提示的输入框，主要有{@link ComboBox} {@link LinkBox}和{@link OnlineBox}
 *
 * @param <T> 当前对象类型
 * @author DFishTeam
 */
public abstract class SuggestionBox<T extends SuggestionBox<T>> extends AbstractPickerBox<T> {
    /**
     *
     */
    private static final long serialVersionUID = -3727695759981575245L;
    private Dialog suggest;
    private Boolean multiple;
    private Boolean br;
    private Long delay;
    private String separator;
    private BoxBind bind;

    /**
     * 构造函数
     *
     * @param name    表单名
     * @param label   标题
     * @param value   初始值
     * @param suggest 在线匹配关键词的 view suggest。支持 $value 和 $text 变量。
     */
    public SuggestionBox(String name, String label, String value, String suggest) {
        super(name, label, value);
        if (Utils.notEmpty(suggest)) {
            suggest().setSrc(suggest);
        }
    }

    /**
     * 构造函数
     *
     * @param name    表单名
     * @param label   标题
     * @param value   初始值
     * @param suggest 在线匹配关键词的 view suggest。支持 $value 和 $text 变量。
     */
    public SuggestionBox(String name, Label label, String value, Dialog suggest) {
        super(name, label, value);
        this.setSuggest(suggest);
    }


//	private Integer matchlength;

    /**
     * 设置是否可以多选
     *
     * @param multiple boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setMultiple(Boolean multiple) {
        this.multiple = multiple;
        return (T) this;
    }

    /**
     * 设置是否可以多选
     *
     * @return the multiple
     */
    public Boolean getMultiple() {
        return multiple;
    }

    /**
     * 设置当内容太多的时候不换行
     *
     * @return Boolean
     */
    public Boolean getBr() {
        return br;
    }

    /**
     * @param br 设置当内容太多的时候不换行
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setBr(Boolean br) {
        this.br = br;
        return (T) this;
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    public T setValue(Object value) {
//        this.value = toString(value);
//        return (T) this;
//    }

    /**
     * 在线匹配关键词的动作。支持 $value 和 $text 变量。
     *
     * @return String
     */
    public Dialog getSuggest() {
        return suggest;
    }

    /**
     * 获取在线匹配关键词的命令,当不存在时,将创建新的对话框
     *
     * @return Dialog 搜索建议下拉框
     */
    protected Dialog suggest() {
        if (this.suggest == null) {
            this.suggest = new Dialog(null,null,null);
        }
        return this.suggest;
    }

    /**
     * 在线匹配关键词的 view suggest。支持 $value 和 $text 变量。
     *
     * @param suggest 在线匹配关键词的 view suggest
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setSuggest(String suggest) {
        Dialog thisSrc = suggest().setSrc(suggest);
        thisSrc.setSrc(suggest);
        return (T) this;
    }

    /**
     * 在线匹配关键词的对话框命令
     *
     * @param suggest 在线匹配关键词的对话框命令
     * @return 本身，这样可以继续设置其他属性
     */
    public T setSuggest(Dialog suggest) {
        this.suggest = suggest;
        return (T) this;
    }

    /**
     * 输入字符延时时间,单位:毫秒
     *
     * @return Long
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * 输入字符延时时间,单位:毫秒
     *
     * @param delay Long
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setDelay(Long delay) {
        this.delay = delay;
        return (T) this;
    }

    /**
     * 文本选项分隔符，默认是逗号
     *
     * @return String
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * 文本选项分隔符，默认是逗号
     *
     * @param separator String
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setSeparator(String separator) {
        this.separator = separator;
        return (T) this;
    }

    /**
     * box的数据来源
     * @return BoxBind 数据项绑定对象
     */
    public BoxBind getBind() {
        return bind;
    }

    /**
     * box的数据来源
     * @param bind 数据项绑定对象
     * @return 本身，这样可以继续设置其他属性
     */
    public T setBind(BoxBind bind) {
        this.bind = bind;
        return (T) this;
    }

    @Override
    protected NodeContainerDecorator getNodeContainerDecorator() {
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                return Arrays.asList(picker,drop,suggest) ;
            }

            @Override
            protected void setNode(int i, Node node) {
                switch (i){
                    case 0:
                        picker=(Dialog) node;
                        break;
                    case 1:
                        drop=(Dialog) node;
                        break;
                    case 2:
                        suggest=(Dialog) node;
                        break;
                    default:
                        throw new IllegalArgumentException("expect 0-picker 1-drop 2-suggest, but get "+i);
                }
            }
        };


    }
}
