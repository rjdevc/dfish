//package com.rongji.dfish.ui.form;
//
//import com.rongji.dfish.ui.AbstractNode;
//
///**
// * Combo 用于设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。 其中field
// * 是用来表达和Combobox整合的方式；keepshow是下拉弹出框是否需要显示。
// * 主要应用场景是，当前对象为Table或Tree的时候，可以作为Combobox值的候选项。
// * 这时候就需要设定他们如何和Combobox绑定。field设定各个绑定字段名，其中search多个字段可以用逗号隔开。
// *
// * @author DFish Team
// * @version 1.0.20160819
// * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
// *
// * @since DFish1.0
// */
//public class Combo extends AbstractNode<Combo> {
//
//    private static final long serialVersionUID = -1238918488428976174L;
//
//    private Field field;
//    private Boolean keepShow;
//    private Boolean fullPath;
//
//    /**
//     * 绑定字段
//     *
//     * @return Field
//     */
//    public Field getField() {
//        return field;
//    }
//
//    /**
//     * 绑定字段
//     *
//     * @param field Field
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Combo setField(Field field) {
//        this.field = field;
//        return this;
//    }
//
//    /**
//     * 是否一直显示
//     *
//     * @return Boolean
//     */
//    public Boolean getKeepShow() {
//        return keepShow;
//    }
//
//    /**
//     * 是否一直显示
//     *
//     * @param keepShow Boolean
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Combo setKeepShow(Boolean keepShow) {
//        this.keepShow = keepShow;
//        return this;
//    }
//
//    /**
//     * 是否显示选项值完整路径
//     *
//     * @return Boolean
//     */
//    public Boolean getFullPath() {
//        return fullPath;
//    }
//
//    /**
//     * 是否显示选项值完整路径
//     *
//     * @param fullPath Boolean
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Combo setFullPath(Boolean fullPath) {
//        this.fullPath = fullPath;
//        return this;
//    }
//
//    /**
//     * Combo要使用的Field
//     *
//     * @author DFish Team
//     */
//    public static class Field {
//        private String value;// 值字段名
//        private String text;// 文本字段名
//        private String search;// 搜索字段名
//        private String remark;// 备注字段名
//        private String forbid;// 禁用字段名
//
//        /**
//         * 构造函数
//         *
//         * @param value 值字段名
//         * @param text  文本字段名
//         */
//        public Field(String value, String text) {
//            this.value = value;
//            this.text = text;
//        }
//
//        /**
//         * 值字段名
//         *
//         * @return String
//         */
//        public String getValue() {
//            return value;
//        }
//
//        /**
//         * 值字段名
//         *
//         * @param value String
//         * @return 本身，这样可以继续设置其他属性
//         */
//        public Field setValue(String value) {
//            this.value = value;
//            return this;
//        }
//
//        /**
//         * 文本字段名
//         *
//         * @return String
//         */
//        public String getText() {
//            return text;
//        }
//
//        /**
//         * 文本字段名
//         *
//         * @param text String
//         * @return 本身，这样可以继续设置其他属性
//         */
//        public Field setText(String text) {
//            this.text = text;
//            return this;
//        }
//
//        /**
//         * 搜索字段名
//         *
//         * @return String
//         */
//        public String getSearch() {
//            return search;
//        }
//
//        /**
//         * 搜索字段名
//         *
//         * @param search String
//         * @return 本身，这样可以继续设置其他属性
//         */
//        public Field setSearch(String search) {
//            this.search = search;
//            return this;
//        }
//
//        /**
//         * 备注字段名
//         *
//         * @return String
//         */
//        public String getRemark() {
//            return remark;
//        }
//
//        /**
//         * 备注字段名
//         *
//         * @param remark String
//         * @return 本身，这样可以继续设置其他属性
//         */
//        public Field setRemark(String remark) {
//            this.remark = remark;
//            return this;
//        }
//
//        /**
//         * 禁用字段名
//         *
//         * @return String
//         */
//        public String getForbid() {
//            return forbid;
//        }
//
//        /**
//         * 禁用字段名
//         *
//         * @param forbid String
//         * @return 本身，这样可以继续设置其他属性
//         */
//        public Field setForbid(String forbid) {
//            this.forbid = forbid;
//            return this;
//        }
//    }
//}
