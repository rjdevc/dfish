package com.rongji.dfish.misc;

public class HtmlScriptRemover {
	CaseInsensitiveTextReplacer citr=new CaseInsensitiveTextReplacer();
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
	public void setReplaceText(String text,String toText){
		citr.addKeyWord(text, toText);
	}
	
	public HtmlScriptRemover(){
		defaultConfig();
	}
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
