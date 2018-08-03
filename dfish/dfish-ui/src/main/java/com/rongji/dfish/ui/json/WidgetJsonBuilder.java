package com.rongji.dfish.ui.json;

import java.util.Map;
import java.util.Stack;

import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.FormElement;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.LabelRow;
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
			if("aftercontent".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("aftercontent"){
					protected String getValue(Object w) {
						return ((Widget<?>)w).getAftercontent();
					}});
			}else if("beforecontent".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("beforecontent"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getBeforecontent();
					}});
			}else if("cls".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("cls"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getCls();
					}});
			}else if("style".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("style"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getStyle();
					}});
			}else if("gid".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("gid"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getGid();
					}});
			}else if("id".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("id"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getId();
					}});
			}else if("height".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("height"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getHeight();
					}});
			}else if("width".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("width"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getWidth();
					}});
			}else if("maxheight".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("maxheight"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getMaxheight();
					}});
			}else if("maxwidth".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("maxwidth"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getMaxwidth();
					}});
			}else if("minheight".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("minheight"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getMinheight();
					}});
			}else if("minwidth".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("minwidth"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getMinwidth();
					}});
			}else if("minwidth".equals(jbpg.getPropName())){
				methods.set(i, new WidgetStringPropAppender("minwidth"){
					protected String getValue(Object w) {
						return  ((Widget<?>)w).getMinwidth();
					}});
			}else if("hmin".equals(jbpg.getPropName())){
				methods.set(i, new WidgetIntegerPropAppender("hmin"){
					protected Integer getValue(Object w) {
						return  ((Widget<?>)w).getHmin();
					}});
			}else if("wmin".equals(jbpg.getPropName())){
				methods.set(i, new WidgetIntegerPropAppender("wmin"){
					protected Integer getValue(Object w) {
						return ((Widget<?>)w).getWmin();
					}});
			}else if("type".equals(jbpg.getPropName())){
				methods.set(i, new WidgetPropAppender("type"){
					public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
						Widget<?>w=(Widget<?>)o;
						String type=w.getType();
						if(type!=null&&!type.equals("")){
							boolean show=true;
							if(path.size()>0){
								PathInfo parentInfo=path.peek();
								show=!"pub".equals(parentInfo.getPropName())&&
										!"options".equals(parentInfo.getPropName())&&
										!"hiddens".equals(parentInfo.getPropName());
							}
							if(show){
								if(begin){begin=false;}else{sb.append(',');}
								sb.append("\"type\":\"");
								sb.append(type);
								sb.append('"');
							}
						}
						return begin;
					}});
			}else if("on".equals(jbpg.getPropName())){
				methods.set(i, new WidgetPropAppender("on"){
					public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
						Widget<?>w=(Widget<?>)o;
						Map<String,String> on=w.getOn();
						if(on!=null&&on.size()>0){
							if(begin){begin=false;}else{sb.append(',');}
							sb.append("\"on\":");
							boolean mapBegin=true;
							sb.append('{');
							for(Map.Entry<String,String> item:on.entrySet()){
								if(item.getValue()==null||item.getValue().equals("")){
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
			}
			if(LabelRow.class.isAssignableFrom(clz)){
				if("label".equals(jbpg.getPropName())){
					methods.set(i, new WidgetStringPropAppender("label"){
						protected String getValue(Object w) {
							return ((LabelRow<?>)w).getLabel();
						}});
				}
			}
			if(FormElement.class.isAssignableFrom(clz)){
				if("name".equals(jbpg.getPropName())){
					methods.set(i, new WidgetStringPropAppender("name"){
						protected String getValue(Object w) {
							return ((FormElement<?,?>)w).getName();
						}});
				}
				//value因为没有限定类型，不适合强制转化
			}
			if(Scrollable.class.isAssignableFrom(clz)){
				if("scroll".equals(jbpg.getPropName())){
					methods.set(i, new WidgetBooleanPropAppender("scroll"){
						protected Boolean getValue(Object w) {
							return ((Scrollable<?>)w).getScroll();
						}});
				}else if("scrollClass".equals(jbpg.getPropName())){
					methods.set(i, new WidgetStringPropAppender("scrollClass"){
						protected String getValue(Object w) {
							return ((Scrollable<?>)w).getScrollClass();
						}});
				}
			}
			if(Alignable.class.isAssignableFrom(clz)){
				if("align".equals(jbpg.getPropName())){
					methods.set(i, new WidgetStringPropAppender("align"){
						protected String getValue(Object w) {
							return ((Alignable<?>)w).getAlign();
						}});
				}
			}
			if(Valignable.class.isAssignableFrom(clz)){
				if("valign".equals(jbpg.getPropName())){
					methods.set(i, new WidgetStringPropAppender("valign"){
						protected String getValue(Object w) {
							return ((Valignable<?>)w).getValign();
						}});
				}
			}
			if(HasText.class.isAssignableFrom(clz)){
				if("text".equals(jbpg.getPropName())){
					methods.set(i, new WidgetStringPropAppender("text"){
						protected String getValue(Object w) {
							return ((HasText<?>)w).getText();
						}});
				}
			}
		}

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

		@Override
		public abstract boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception ;
//		protected abstract  T getValue(Widget<?> w);
	}
	public static  abstract class WidgetStringPropAppender extends WidgetPropAppender{
		public WidgetStringPropAppender(String propName) {
			super(propName);
		}
		public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
			String v=getValue(o);
			if(v!=null&&!v.equals("")){
				if(begin){begin=false;}else{sb.append(',');}
				sb.append('"');
				sb.append(propName);
				sb.append("\":\"");
				escapeJson(v, sb);//可能有转义字符
				sb.append('"');
			}
			return begin;
		}

		protected abstract  String getValue(Object w);
	}
	public static  abstract class WidgetIntegerPropAppender extends WidgetPropAppender{
		public WidgetIntegerPropAppender(String propName) {
			super(propName);
		}
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

		protected abstract  Integer getValue(Object w);
	}
	public static  abstract class WidgetBooleanPropAppender extends WidgetPropAppender{
		public WidgetBooleanPropAppender(String propName) {
			super(propName);
		}
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

		protected abstract  Boolean getValue(Object w);
	}
}
