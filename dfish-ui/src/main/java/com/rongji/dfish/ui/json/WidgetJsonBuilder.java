package com.rongji.dfish.ui.json;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.FormElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * WidgetJsonBuilder 为加速DFish3的json构建而设计
 * 因为JSON构建多为类反射构建，并且可能会严格检查每个属性的信息性能相对较差。
 * 而dfish3中大量组件为widget。widget下是属性大多数是已知的，直接获取分别判断将会加速构建。
 *
 * @author LinLW
 */
public class WidgetJsonBuilder extends ClassJsonBuilder {

    public WidgetJsonBuilder(Class<?> clz) {
        super(clz);
        for (int i = 0; i < methods.size(); i++) {
            JsonPropertyAppender jbpg = methods.get(i);
            if ("beforeContent".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("beforeContent") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getBeforeContent();
                    }
                });
            } else if ("prependContent".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("prependContent") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getPrependContent();
                    }
                });
            } else if ("appendContent".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("appendContent") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getAppendContent();
                    }
                });
            } else if ("afterContent".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("afterContent") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getAfterContent();
                    }
                });
            } else if ("cls".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("cls") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getCls();
                    }
                });
            } else if ("style".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("style") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getStyle();
                    }
                });
            } else if ("gid".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("gid") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getGid();
                    }
                });
            } else if ("id".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("id") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getId();
                    }
                });
            } else if ("height".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("height") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getHeight();
                    }
                });
            } else if ("width".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("width") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getWidth();
                    }
                });
            } else if ("maxHeight".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("maxHeight") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getMaxHeight();
                    }
                });
            } else if ("maxWidth".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("maxWidth") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getMaxWidth();
                    }
                });
            } else if ("minHeight".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("minHeight") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getMinHeight();
                    }
                });
            } else if ("minWidth".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetStringPropertyAppender("minWidth") {
                    @Override
                    protected String getValue(Object w) {
                        return ((Widget<?>) w).getMinWidth();
                    }
                });
            } else if ("heightMinus".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetIntegerPropertyAppender("heightMinus") {
                    @Override
                    protected Integer getValue(Object w) {
                        return ((Widget<?>) w).getHeightMinus();
                    }
                });
            } else if ("widthMinus".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetIntegerPropertyAppender("widthMinus") {
                    @Override
                    protected Integer getValue(Object w) {
                        return ((Widget<?>) w).getWidthMinus();
                    }
                });
            } else if ("type".equals(jbpg.getPropertyName())) {
                methods.set(i, WidgetTypeAppender.getInstance());
            } else if ("on".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetPropertyAppender("on") {
                    @Override
                    public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
                        Widget<?> w = (Widget<?>) o;
                        Map<String, String> on = w.getOn();
                        if (on != null && on.size() > 0) {
                            if (begin) {
                                begin = false;
                            } else {
                                sb.append(',');
                            }
                            sb.append("\"on\":");
                            boolean mapBegin = true;
                            sb.append('{');
                            for (Map.Entry<String, String> item : on.entrySet()) {
                                if (item.getValue() == null || "".equals(item.getValue())) {
                                    continue;
                                }
                                if (mapBegin) {
                                    mapBegin = false;
                                } else {
                                    sb.append(',');
                                }
                                sb.append('"').append(item.getKey()).append('"').append(':').append('"');
                                escapeJson(item.getValue(), sb);
                                sb.append('"');
                            }
                            sb.append('}');
                        }
                        return begin;
                    }
                });
            } else if ("data".equals(jbpg.getPropertyName())) {
                methods.set(i, new WidgetPropertyAppender("data") {
                    @Override
                    public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
                        Widget<?> w = (Widget<?>) o;
                        Map<String, Object> data = w.getData();
                        if (data != null && data.size() > 0) {
                            if (begin) {
                                begin = false;
                            } else {
                                sb.append(',');
                            }
                            sb.append("\"data\":");
                            path.push(new PathInfo("data", data));
                            JsonFormat.buildJson(data, sb, path);
                            path.pop();
                        }
                        return begin;
                    }
                });
            } else if ("name".equals(jbpg.getPropertyName())) {
                if (FormElement.class.isAssignableFrom(clz)) {
                    methods.set(i, new WidgetStringPropertyAppender("name") {
                        @Override
                        protected String getValue(Object w) {
                            return ((FormElement<?, ?>) w).getName();
                        }
                    });
                }
                //value因为没有限定类型，不适合强制转化
            } else if ("align".equals(jbpg.getPropertyName())) {
                if (Alignable.class.isAssignableFrom(clz)) {
                    methods.set(i, new WidgetStringPropertyAppender("align") {
                        @Override
                        protected String getValue(Object w) {
                            return ((Alignable<?>) w).getAlign();
                        }
                    });
                }
            } else if ("vAlign".equals(jbpg.getPropertyName())) {
                if (VAlignable.class.isAssignableFrom(clz)) {
                    methods.set(i, new WidgetStringPropertyAppender("vAlign") {
                        @Override
                        protected String getValue(Object w) {
                            return ((VAlignable<?>) w).getVAlign();
                        }
                    });
                }
            } else if ("text".equals(jbpg.getPropertyName())) {
                if (HasText.class.isAssignableFrom(clz)) {
                    methods.set(i, new WidgetStringPropertyAppender("text") {
                        @Override
                        protected String getValue(Object w) {
                            return ((HasText<?>) w).getText();
                        }
                    });
                }
            } else if ("scroll".equals(jbpg.getPropertyName())) {
                if (Scrollable.class.isAssignableFrom(clz)) {
                    methods.set(i, new WidgetBooleanPropertyAppender("scroll") {
                        @Override
                        protected Boolean getValue(Object w) {
                            return ((Scrollable<?>) w).getScroll();
                        }
                    });
                }
            }

            //3.2 中label为复杂属性
//			if(LabelRow.class.isAssignableFrom(clz)){
//				if("label".equals(jbpg.getPropertyName())){
//					methods.set(i, new WidgetStringPropAppender("label"){
//						protected String getValue(Object w) {
//							return ((LabelRow<?>)w).getLabel();
//						}});
//				}
//			}
        }

    }

    /**
     * Widget的type属性在输出的时候，有很多种情况是可以忽略的，用于减少JSON长度。
     * 这里WidgetTypeAppender就是专门处理type属性的Appender.
     */
    public static class WidgetTypeAppender extends WidgetPropertyAppender {
        private static WidgetTypeAppender instance = new WidgetTypeAppender();

        /**
         * 因为要读取h和编译配置，所以这个Append必须是单例的，以提高性能。
         * @return WidgetTypeAppender
         */
        public static WidgetTypeAppender getInstance() {
            return instance;
        }

        /**
         * 构造函数
         */
        private WidgetTypeAppender() {
            super("type");
            //加载信息type 可以省略的信息
            init("path:pub;" +
                    "path:hiddens,*;" +
                    "path:options,*;" +
                    "type:ButtonBar,path:nodes,type:Button;" +
                    "type:Button,path:nodes,type:Button;" +
                    "type:Ablum,path:nodes,type:Img;" +
                    "type:Tree,path:nodes,type:Leaf;" +
                    "type:Leaf,path:nodes,type:Leaf;" +
                    "type:Calendar,path:nodes,type:CalendarItem;" +
                    "path:split;");
        }

        @Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
            com.rongji.dfish.ui.Node w = (com.rongji.dfish.ui.Node) o;
            String type = w.getType();
            if (type != null && !"".equals(type)) {
                if (!match(path)) {
                    if (begin) {
                        begin = false;
                    } else {
                        sb.append(',');
                    }
                    sb.append("\"type\":\"");
                    sb.append(type);
                    sb.append('"');
                }
            }
            return begin;
        }

        /**
         * 节点配置 表示任何节点
         */
        public static final String ANY = "*";
        /**
         * 节点配置 的前缀 表示路径为这个值的节点
         */
        public static final String PATH_PREFIX = "path:";
        /**
         * 节点配置的前缀 表示 type为这个值的节点
         */
        public static final String TYPE_PREFIX = "type:";
        private Node root = new AllMatchNode(ANY);

        /**
         * 初始化，根据文本的配置，编译成Node树的格式，
         * 该格式可以高性能的判断 type是否需要输出。
         * @param config String 配置
         */
        protected void init(String config) {
            if (config == null) {
                return;
            }
            //将该结构编译成用于判断的中间结构
            for (String line : config.split("[;]")) {
                //多条规则
                Node parent = root;
                String[] exprs = line.split("[,]");
                for (int index = exprs.length - 1; index >= 0; index--) {
                    String expr = exprs[index];
                    if (expr == null || "".equals(expr)) {
                        continue;
                    }
                    Node node = null;
                    boolean find = false;
                    if (parent.subs != null) {
                        for (Node n : parent.subs) {
                            if (expr.equals(n.expr)) {
                                node = n;
                                find = true;
                            }
                        }
                    }
                    if (!find) {
                        if (ANY.equals(expr)) {
                            node = new AllMatchNode(ANY);
                        } else if (expr.startsWith(PATH_PREFIX)) {
                            node = new PathMatchNode(expr);
                        } else if (expr.startsWith(TYPE_PREFIX)) {
                            node = new TypeMatchNode(expr);
                        }
                        parent.addNode(node);
                    }
                    parent = node;
                    if (!node.end) {
                        node.end = index == 0;
                    }
                }
            }
            LogUtil.lazyDebug(getClass(), () -> root.show());
        }

        /**
         * 当前路径是否符合 不需要输出type属性的规则。
         * @param path 构建过程中path 详见构建过程JsonFormat
         * @return boolean
         */
        public boolean match(Stack<PathInfo> path) {
            if (root.subs == null) {
                return false;
            }
            for (Node sub : root.subs) {
                if (path.size() > 0 && match(sub, path, path.size() - 1)) {
                    return true;
                }
            }
            return false;
        }

        private boolean match(Node node, Stack<PathInfo> path, int index) {
            PathInfo p = path.get(index);
            if (!node.match(p)) {
                return false;
            }
            if (node.end) {
                return true;
            }
            index--;
            if (index < 0) {
                return false;
            }
            for (Node sub : node.subs) {
                if (match(sub, path, index)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 用于将Type可以隐藏的规则编译成树格式
         * 所以这个Node有subs 当最满足一整串条件的时候，end值为true。这时候即可得出结论。
         */
        public static abstract class Node {
            protected String expr;
            protected List<Node> subs;
            protected boolean end;

            /**
             * 判断路径是否符合规则，如果符合规则，将不显示type属性
             *
             * @param pathInfo 路径
             * @return boolean
             */
            public abstract boolean match(PathInfo pathInfo);

            public String show() {
                StringBuilder sb = new StringBuilder();
                show("", "the [type] property should be hidden, when path like below ", sb);
                return sb.toString();
            }

            private static final char CHAR_BLANK = '\u3000';//全角空格
            private static final char CHAR_I = '\u2502';//制表符 │
            private static final char CHAR_T = '\u251C';//制表符├
            private static final char CHAR_L = '\u2514';//制表符└

            /**
             * 构建成有缩进格式的文本，以便理解
             * @param prefix 前缀
             * @param expr 当前节点表达式
             * @param sb 结果输出到该StringBuilder
             */
            private void show(String prefix, String expr, StringBuilder sb) {
                sb.append(prefix);
                sb.append(expr);
                if (end) {
                    sb.append(" => hide");
                }
                sb.append("\r\n");

                if (subs == null || subs.size() == 0) {
                    return;
                }
                String newPrefix = "";
                if (prefix.length() > 1) {
                    newPrefix = prefix.substring(0, prefix.length() - 1);
                }
                if (prefix.length() > 0) {
                    char lastChar = prefix.charAt(prefix.length() - 1);
                    if (lastChar == CHAR_L) {
                        newPrefix += CHAR_BLANK;
                    } else if (lastChar == CHAR_T) {
                        newPrefix += CHAR_I;
                    }
                }
                int i = 0;
                for (Node node : subs) {
                    if (++i < subs.size()) {
                        node.show(newPrefix + CHAR_T, node.expr, sb);
                    } else {
                        node.show(newPrefix + CHAR_L, node.expr, sb);
                    }
                }
            }

            /**
             * 构造函数
             * @param expr 当前节点表达式
             */
            public Node(String expr) {
                this.expr = expr;
            }

            /**
             * 增加子节点
             * @param sub Node
             */
            public void addNode(Node sub) {
                if (subs == null) {
                    subs = new ArrayList<Node>();
                }
                subs.add(sub);
            }

            @Override
            public String toString() {
                return expr;
            }
        }

        /**
         * 表达式 * 编译成的节点。
         * 即任何节点。都符合这段配置
         */
        public static class AllMatchNode extends Node {
            @Override
            public boolean match(PathInfo pathInfo) {
                return true;
            }

            /**
             * 构造函数
             * @param expr  表达式 这里只能是*
             */
            public AllMatchNode(String expr) {
                super(expr);
            }
        }

        /**
         * 表达式 type:xxxx 所编译成的节点
         * 即表示 type必须符合这个值才能匹配。
         */
        public static class TypeMatchNode extends Node {
            private String type;

            @Override
            public boolean match(PathInfo pathInfo) {
                if (pathInfo.getPropertyValue() instanceof com.rongji.dfish.ui.Node) {
                    String pType = ((com.rongji.dfish.ui.Node) pathInfo.getPropertyValue()).getType();
                    return type.equals(pType);
                }
                return false;
            }

            /**
             * 构造函数
             * @param expr 表达式
             */
            public TypeMatchNode(String expr) {
                super(expr);
                this.type = expr.substring(5);
            }
        }

        /**
         * 表达式 path:xxx 所编译成的节点
         * 即表示 path必须符合这个值才能匹配
         */
        public static class PathMatchNode extends Node {
            private String path;

            @Override
            public boolean match(PathInfo pathInfo) {
                String pPath = pathInfo.getPropertyName();
                return path.equals(pPath);
            }

            /**
             * 构造函数
             * @param expr 表达式
             */
            public PathMatchNode(String expr) {
                super(expr);
                this.path = expr.substring(5);
            }
        }
    }

    /**
     * WidgetPropertyAppender 用于构建类的属性。
     * 与默认的JsonPropertyAppender不同
     * 因为它只处理符合Widget接口的对象。部分属性是直接调用。而非类反射。
     * 从而提高性能。
     */
    public static abstract class WidgetPropertyAppender implements JsonPropertyAppender {
        /**
         * 构造函数
         * @param propertyName 属性名
         */
        public WidgetPropertyAppender(String propertyName) {
            this.propertyName = propertyName;
        }

        String propertyName;

        @Override
        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

    }

    /**
     * 属性添加器，它用于添加Widget属性中类型是String的属性。
     */
    public static abstract class WidgetStringPropertyAppender extends WidgetPropertyAppender {
        /**
         * 构造函数
         * @param propertyName 属性名
         */
        public WidgetStringPropertyAppender(String propertyName) {
            super(propertyName);
        }

        @Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
            String v = getValue(o);
            if (v != null && !"".equals(v)) {
                if (begin) {
                    begin = false;
                } else {
                    sb.append(',');
                }
                sb.append('"');
                sb.append(propertyName);
                sb.append("\":\"");
                escapeJson(v, sb);//可能有转义字符
                sb.append('"');
            }
            return begin;
        }

        /**
         * 取得用于显示的文本
         *
         * @param w Object
         * @return String
         */
        protected abstract String getValue(Object w);
    }

    /**
     * 属性添加器，它用于添加Widget属性中类型是Integer的属性。
     */
    public static abstract class WidgetIntegerPropertyAppender extends WidgetPropertyAppender {
        /**
         * 构造函数
         * @param propertyName 属性名
         */
        public WidgetIntegerPropertyAppender(String propertyName) {
            super(propertyName);
        }

        @Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
            Integer v = getValue(o);
            if (v != null) {
                if (begin) {
                    begin = false;
                } else {
                    sb.append(',');
                }
                sb.append('"');
                sb.append(propertyName);
                sb.append("\":");
                sb.append(v);
            }
            return begin;
        }

        /**
         * 取得用于显示的属性。
         *
         * @param w Object
         * @return Integer
         */
        protected abstract Integer getValue(Object w);
    }

    /**
     * 属性添加器，它用于添加Widget属性中类型是Boolean的属性。
     */
    public static abstract class WidgetBooleanPropertyAppender extends WidgetPropertyAppender {
        /**
         * 构造函数
         * @param propertyName 属性名
         */
        public WidgetBooleanPropertyAppender(String propertyName) {
            super(propertyName);
        }

        @Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
            Boolean v = getValue(o);
            if (v != null) {
                if (begin) {
                    begin = false;
                } else {
                    sb.append(',');
                }
                sb.append('"');
                sb.append(propertyName);
                sb.append("\":");
                sb.append(v);
            }
            return begin;
        }

        /**
         * 取得用于显示的属性。
         *
         * @param w Object
         * @return Boolean
         */
        protected abstract Boolean getValue(Object w);
    }
}