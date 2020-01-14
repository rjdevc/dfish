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
 * @author LinLW
 *
 */
public class WidgetJsonBuilder extends ClassJsonBuilder {

	public WidgetJsonBuilder(Class<?> clz) {
		super(clz);
		for(int i=0;i<methods.size();i++){
			JsonPropAppender jbpg=methods.get(i);
			if("beforecontent".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("beforecontent"){
					@Override
                    protected String getValue(Object w) {
						return ((Widget<?>)w).getBeforeContent();
					}});
			}else if("prependcontent".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("prependcontent"){
					@Override
                    protected String getValue(Object w) {
						return ((Widget<?>)w).getPrependContent();
					}});
			}else if("appendcontent".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("appendcontent"){
					@Override
                    protected String getValue(Object w) {
						return ((Widget<?>)w).getAppendContent();
					}});
			}else if("aftercontent".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("aftercontent"){
					@Override
                    protected String getValue(Object w) {
						return ((Widget<?>)w).getAfterContent();
					}});
			}else if("cls".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("cls"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getCls();
					}});
			}else if("style".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("style"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getStyle();
					}});
			}else if("gid".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("gid"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getGid();
					}});
			}else if("id".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("id"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getId();
					}});
			}else if("height".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("height"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getHeight();
					}});
			}else if("width".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("width"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getWidth();
					}});
			}else if("maxheight".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("maxheight"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getMaxHeight();
					}});
			}else if("maxwidth".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("maxwidth"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getMaxWidth();
					}});
			}else if("minheight".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("minheight"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getMinHeight();
					}});
			}else if("minwidth".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("minwidth"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getMinWidth();
					}});
			}else if("minwidth".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("minwidth"){
					@Override
                    protected String getValue(Object w) {
						return  ((Widget<?>)w).getMinWidth();
					}});
			}else if("hmin".equals(jbpg.getPropName())){
				methods.set(i, new WidgetIntegerPropAppender("hmin"){
					@Override
                    protected Integer getValue(Object w) {
						return  ((Widget<?>)w).getHeightMinus();
					}});
			}else if("wmin".equals(jbpg.getPropName())){
				methods.set(i, new WidgetIntegerPropAppender("wmin"){
					@Override
                    protected Integer getValue(Object w) {
						return ((Widget<?>)w).getWidthMinus();
					}});
			}else if("type".equals(jbpg.getPropName())){
				methods.set(i, WidgetTypeAppender.getInstance());
			}else if("on".equals(jbpg.getPropName())){
				methods.set(i, new WidgetPropAppender("on"){
					@Override
                    public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
						Widget<?>w=(Widget<?>)o;
						Map<String,String> on=w.getOn();
						if(on!=null&&on.size()>0){
							if(begin){begin=false;}else{sb.append(',');}
							sb.append("\"on\":");
							boolean mapBegin=true;
							sb.append('{');
							for(Map.Entry<String,String> item:on.entrySet()){
								if(item.getValue()==null|| "".equals(item.getValue())){
									continue;
								}
								if(mapBegin){mapBegin=false;}else{sb.append(',');}
								sb.append('"').append(item.getKey()).append('"').append(':').append('"');
								escapeJson(item.getValue(), sb);
								sb.append('"');
							}
							sb.append('}');
						}
						return begin;
					}});
			}else if("data".equals(jbpg.getPropName())){
				methods.set(i, new WidgetPropAppender("data"){
					@Override
                    public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
						Widget<?>w=(Widget<?>)o;
						Map<String,Object> data=w.getData();
						if(data!=null&&data.size()>0){
							if(begin){begin=false;}else{sb.append(',');}
							sb.append("\"data\":");
							path.push(new PathInfo("data",data));
							J.buildJson(data, sb, path);
							path.pop();
						}
						return begin;
					}});
			} else if("name".equals(jbpg.getPropName())){
			    if(FormElement.class.isAssignableFrom(clz)){
					methods.set(i, new WidgetStringPropAppender("name"){
						@Override
                        protected String getValue(Object w) {
							return ((FormElement<?,?>)w).getName();
						}});
				}
				//value因为没有限定类型，不适合强制转化
			} else if("align".equals(jbpg.getPropName())){
			    if(Alignable.class.isAssignableFrom(clz)){
					methods.set(i, new WidgetStringPropAppender("align"){
						@Override
                        protected String getValue(Object w) {
							return ((Alignable<?>)w).getAlign();
						}});
				}
			} else if("valign".equals(jbpg.getPropName())){
			    if(VAlignable.class.isAssignableFrom(clz)){
					methods.set(i, new WidgetStringPropAppender("valign"){
						@Override
                        protected String getValue(Object w) {
							return ((VAlignable<?>)w).getVAlign();
						}});
				}
			} else if("text".equals(jbpg.getPropName())){
                if(HasText.class.isAssignableFrom(clz)){
                    methods.set(i, new WidgetStringPropAppender("text"){
						@Override
                        protected String getValue(Object w) {
                        	return ((HasText<?>)w).getText();
                        }
                    });
                }
            } else if("scroll".equals(jbpg.getPropName())){
                if(Scrollable.class.isAssignableFrom(clz)){
                    methods.set(i, new WidgetBooleanPropAppender("scroll"){
                        @Override
                        protected Boolean getValue(Object w) {
                            return ((Scrollable<?>)w).getScroll();
                        }});
                }
            }

            //3.2 中label为复杂属性
//			if(LabelRow.class.isAssignableFrom(clz)){
//				if("label".equals(jbpg.getPropName())){
//					methods.set(i, new WidgetStringPropAppender("label"){
//						protected String getValue(Object w) {
//							return ((LabelRow<?>)w).getLabel();
//						}});
//				}
//			}
		}

	}
	public static class  WidgetTypeAppender extends WidgetPropAppender{
		static WidgetTypeAppender instance=new WidgetTypeAppender();
		public static WidgetTypeAppender getInstance(){
			return instance;
		}

		public WidgetTypeAppender(){
			super("type");
			//加载信息type 可以省略的信息
			init("path:pub;" +
					"path:hiddens,*;" +
					"path:options,*;" +
					"type:buttonbar,path:nodes,type:button;" +
					"type:button,path:nodes,type:button;" +
					"type:ablum,path:nodes,type:img;" +
					"type:tree,path:nodes,type:leaf;" +
					"type:leaf,path:nodes,type:leaf;" +
					"path:split;");
//			String hideTypeConfig=null;
//			try{
//				ResourceBundle rb=ResourceBundle.getBundle("com.rongji.dfish.ui.json.WidgetJsonBuilder");
//				hideTypeConfig=rb.getString("hideType");
////				J.LOG.info("hideType config = "+hideTypeConfig);
//			}catch(Exception ex){
//				J.LOG.warn("can NOT load hideType config");
//			}
//			init(hideTypeConfig);
		}

		@Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
			JsonObject w=(JsonObject)o;
			String type=w.getType();
			if(type!=null&&!"".equals(type)){
				if(!match(path)){
					if(begin){begin=false;}else{sb.append(',');}
					sb.append("\"type\":\"");
					sb.append(type);
					sb.append('"');
				}
			}
			return begin;
		}

		public  static final String ANY="*";
		public  static final String PATH_PREFIX="path:";
		public  static final String TYPE_PREFIX="type:";
		private Node root=new AllMatchNode(ANY);
		public void init(String config){
			if(config==null){
				return;
			}
			//将该结构编译成用于判断的中间结构
			for(String line:config.split("[;]")){
				//多条规则
				Node parent=root;
				String[] exprs=line.split("[,]");
				for(int index=exprs.length-1;index>=0;index--){
					String expr=exprs[index];
					if(expr==null|| "".equals(expr)){
						continue;
					}
					Node node=null;
					boolean find=false;
					if(parent.subs!=null){
						for(Node n:parent.subs){
							if(expr.equals(n.expr)){
								node=n;
								find=true;
							}
						}
					}
					if(!find){
						if(ANY.equals(expr)){
							node=new AllMatchNode(ANY);
						}else if(expr.startsWith(PATH_PREFIX)){
							node=new PathMatchNode(expr);
						}else if(expr.startsWith(TYPE_PREFIX)){
							node=new TypeMatchNode(expr);
						}
						parent.addSubNode(node);
					}
					parent=node;
					if(!node.end){
						node.end= index==0;
					}
				}
			}
			LogUtil.info(getClass(), root.show());
		}
		public boolean match(Stack<PathInfo> path){
			if(root.subs==null){
				return false;
			}
			for(Node sub:root.subs){
				if(path.size()>0&&match(sub,path,path.size()-1)){
					return true;
				}
			}
			return false;
		}

		private boolean match(Node node, Stack<PathInfo> path, int index) {
			PathInfo p=path.get(index);
			if(!node.match(p)){
				return false;
			}
			if(node.end){
				return true;
			}
			index--;
			if(index<0){
				return false;
			}
			for(Node sub:node.subs){
				if(match(sub,path,index)){
					return true;
				}
			}
			return false;
		}

		public static abstract class Node {
			protected String expr;
			protected List<Node> subs;
			protected boolean end;

			/**
			 * 判断路径是否符合规则，如果符合规则，将不显示type属性
			 * @param pathInfo 路径
			 * @return boolean
			 */
			public abstract boolean match(PathInfo pathInfo);
			public String show() {
				StringBuilder sb = new StringBuilder();
				show("","the [type] property should be hidden, when path like below ",sb);
				return sb.toString();
			}
			private static final char CHAR_BLANK = '\u3000';//全角空格
			private static final char CHAR_I = '\u2502';//制表符 │
			private static final char CHAR_T = '\u251C';//制表符├
			private static final char CHAR_L = '\u2514';//制表符└
			private void show(String prefix, String expr, StringBuilder sb) {
					sb.append(prefix);
					sb.append(expr);
					if (end) {
						sb.append(" => hide");
					}
					sb.append("\r\n");

				if (subs == null||subs.size()==0) {
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
				for (Node node:subs) {
					if (++i < subs.size()) {
						node.show(newPrefix + CHAR_T, node.expr, sb);
					} else {
						node.show(newPrefix + CHAR_L, node.expr, sb);
					}
				}
			}

			public Node(String expr){
				this.expr=expr;
			}
			public void addSubNode(Node sub){
				if (subs==null){
					subs=new ArrayList<Node>();
				}
				subs.add(sub);
			}

			@Override
            public String toString(){
				return expr;
			}
		}
		public static class AllMatchNode extends Node{
			@Override
            public boolean match(PathInfo pathInfo){
				return true;
			}
			public AllMatchNode(String expr){
				super(expr);
			}
		}
		public static class TypeMatchNode extends Node{
			private String type;
			@Override
            public boolean match(PathInfo pathInfo){
				if(pathInfo.getPropValue() instanceof JsonObject){
					String pType=((JsonObject)pathInfo.getPropValue()).getType();
					return type.equals(pType);
				}
				return false;
			}
			public TypeMatchNode(String expr){
				super(expr);
				this.type=expr.substring(5);
			}
		}
		public static class PathMatchNode extends Node{
			private String path;
			@Override
            public boolean match(PathInfo pathInfo){
				String pPath=pathInfo.getPropName();
				return path.equals(pPath);
			}
			public PathMatchNode(String expr){
				super(expr);
				this.path=expr.substring(5);
			}
		}
//		private boolean showType(Stack<PathInfo> path) {
//			if(path.size()>0){
//				PathInfo curInfo=path.peek();
//				boolean show= !"pub".equals(curInfo.getPropName());
//				if(show&&path.size()>1){
//					PathInfo theInfo=path.get(path.size()-2);
//					show=!"options".equals(theInfo.getPropName())&&
//						!"hiddens".equals(theInfo.getPropName());
//				}
//				if (show&&path.size()>2){
//					PathInfo theInfo=path.get(path.size()-3);
//					if(theInfo.getPropValue() instanceof JsonObject
//							&& curInfo.getPropValue() instanceof JsonObject){
//						String theType=((JsonObject)(theInfo.getPropValue())).getType();
//						String curType=((JsonObject)(curInfo.getPropValue())).getType();
//						show= !("buttonbar".equals(theType)&&"button".equals(curType))&&
//								!("button".equals(theType)&&"button".equals(curType))&&
//								!("album".equals(theType)&&"img".equals(curType))&&
//								!("leaf".equals(theType)&&"leaf".equals(curType))&&
//								!("tree".equals(theType)&&"leaf".equals(curType));
//					}
//				}
//				return show;
//			}
//			return true;
//		}
	}
	public static abstract class WidgetPropAppender implements JsonPropAppender{
		public WidgetPropAppender(String propName){
			this.propName=propName;
		}
		String propName;
		@Override
		public String getPropName() {
			return propName;
		}
		@Override
		public void setPropName(String propName) {
			this. propName=propName;
		}

//		protected abstract  T getValue(Widget<?> w);
	}
	public static  abstract class WidgetStringPropAppender extends WidgetPropAppender{
		public WidgetStringPropAppender(String propName) {
			super(propName);
		}
		@Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
			String v=getValue(o);
			if(v!=null&&!"".equals(v)){
				if(begin){begin=false;}else{sb.append(',');}
				sb.append('"');
				sb.append(propName);
				sb.append("\":\"");
				escapeJson(v, sb);//可能有转义字符
				sb.append('"');
			}
			return begin;
		}

		/**
		 * 取得用于显示的文本
		 * @param w Object
		 * @return String
		 */
		protected abstract  String getValue(Object w);
	}
	public static  abstract class WidgetIntegerPropAppender extends WidgetPropAppender{
		public WidgetIntegerPropAppender(String propName) {
			super(propName);
		}
		@Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
			Integer v=getValue(o);
			if(v!=null){
				if(begin){begin=false;}else{sb.append(',');}
				sb.append('"');
				sb.append(propName);
				sb.append("\":");
				sb.append(v);
			}
			return begin;
		}

		/**
		 * 取得用于显示的属性。
		 * @param w Object
		 * @return Integer
		 */
		protected abstract  Integer getValue(Object w);
	}
	public static  abstract class WidgetBooleanPropAppender extends WidgetPropAppender{
		public WidgetBooleanPropAppender(String propName) {
			super(propName);
		}
		@Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
			Boolean v=getValue(o);
			if(v!=null){
				if(begin){begin=false;}else{sb.append(',');}
				sb.append('"');
				sb.append(propName);
				sb.append("\":");
				sb.append(v);
			}
			return begin;
		}
		/**
		 * 取得用于显示的属性。
		 * @param w Object
		 * @return Boolean
		 */
		protected abstract  Boolean getValue(Object w);
	}
}
