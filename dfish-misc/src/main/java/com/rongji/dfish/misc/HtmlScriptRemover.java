package com.rongji.dfish.misc;

/**
 * 将HTML里面可能有的注入信息改变，防止脚本攻击
 *
 * @deprecated 方案不完整。也没有符合规范。毕竟注入还可能通过&amp;#的方式注入一些转义过的 绿盟的建议是过滤特殊字符。
 */
@Deprecated
public class HtmlScriptRemover {
	CaseInsensitiveTextReplacer citr=new CaseInsensitiveTextReplacer();

	/**
	 * 默认转义配置
	 */
	public void defaultConfig(){
		//IGNORE UPPER CASE
		setReplaceText("script","my_script");
		setReplaceText("javascript:","my_javascript:");
		setReplaceText(" on"," my_on");//注意有空格
		setReplaceText("iframe","my_iframe");
		setReplaceText("frameset","my_frameset");
		setReplaceText("link","my_link");
		setReplaceText("style","my_style");
	}

	/**
	 * 设置要替换的字符
	 * @param text 敏感关键字
	 * @param toText 替换成
	 */
	public void setReplaceText(String text,String toText){
		citr.addKeyWord(text, toText);
	}

	/**
	 * 构造函数
	 */
	public HtmlScriptRemover(){
		defaultConfig();
	}

	/**
	 * 格式化/ 过滤 文本
	 * @param text
	 * @return
	 */
	public String format(String text){
		if(text==null){
			return null;
		}
		StringBuilder sb=new StringBuilder();
		int begin=text.indexOf('<');
		int cur=0;
		while (begin>=0){
			int end=text.indexOf('>', begin);
			if(end<0) {
                break;
            }
			sb.append(text,cur,begin);
			//处理
			String htmlBlock=text.substring(begin,end);
			sb.append(citr.replaceText(htmlBlock));
			
			cur=end;
			begin=text.indexOf('<', end);
		}
		sb.append(text,cur,text.length());
		return sb.toString();
	}
	
}
