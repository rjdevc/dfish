<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%
String systemVersion = "20170315";
%>
<link rel="shortcut icon" href="./dfish/ui/g/favicon.ico">
<script src="./dfish/dfish.js?ver=<%=systemVersion%>"></script>
<script>
dfish.config( {
	path: '<%=request.getContextPath()%>/', //工程目录。以下定义的目录如果不是以"/"开头，那么都基于本目录
	lib:  'dfish/',  //dfish包目录
	alias: { //自定义widget
		'echarts': 'pl/echarts/echarts.dfish.js',
		'ueditor': 'pl/ueditor/ueditor.dfish.js'
	},
	//表单验证效果 可选值: red,tip,alert
	validate_effect: 'red,tip',
	// 模板ID
	template: 'std',
	// alert和confirm的提示框模板ID
	template_alert: 'alert',
	template_src: 'demo/template?tempId=$0',
	// 皮肤
	skin: { // 皮肤
		dir: 'css/',
		theme: 'classic',
		color: 'blue'
	},
	// 每个 widget 类都可以定义默认属性，以 widget type 作为 key
	default_option: {
		alert : { btncls : 'btn' },
		confirm : { btncls : 'btn' },
		button : { cls : 'btn' }
	},
	view: { // 如果配置了此参数，将生成全屏的view
		id: 'index'
	},
	// 一个汉字算3个字节
	cn_bytes: 3,
	ver: '<%=systemVersion%>',
	debug: true
} );
</script>
