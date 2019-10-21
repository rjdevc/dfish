<? 
header( 'Content-Type: text/html; charset=UTF-8' );
$act = $_GET['act'];

if ( $act == 'version' ) { ?>

{ "type": "view", "node": {
  "type": "vert", "nodes": [
    { "type": "html", "height": "*", "text": "一些内容" },
    { "type": "buttonbar","height": 50, "space": 8, "cls": "dlg-foot", "align": "right", "style": "padding-right:20px", "wmin": 20, "hmin": 1, "nodes": [
      { "type" : "submitbutton", "cls": "btn btn-submit", "text" : "   确定   ", "on": { "click": "VM(this).cmd('ok')" } },
      { "type" : "button", "cls": "btn", "text" : "   取消   ", "on": { "click": "$.close(this)" } }
    ] }
  ] }
}
<? } else if ( $act == 'view' ) { ?>

{ "type": "view",
  "commands" : {
  	"version" : { "type" : "dialog", "id": "dg","width": 500, "height": 360, "title": "最新版本", "template": "std", "src" : "webapp/serv.php?act=version" },
  	"rep": { "type": "replace", "target":"main", "node": {
  	  "type": "vert", "width": "*", "id": "main", "nodes": [
  	    { "type": "html", "height": "*", "text": "text", "valign": "middle", "style": "font-size:16px;background:red" }
  	  ]
  	} }
  },
  "node": {
    "type": "horz", "id": "root", "width": "*", "height": "*", "nodes": [
      { "type": "tree", "width": 240, "id": "menu", "cls": "bg-menu", "pub": { "icon": ".ico-file", "openicon": ".ico-folder-open" },
      "hiddens": [ { "type": "hidden", "name": "hd1", "id": "hd1", "value": "1" }, { "type": "hidden", "name": "hd2", "value": "2" } ],
      "nodes": [
        { "text": "布局组件", "icon": ".ico-folder", "open": true, "nodes": [
          { "text": "FieldsetLayout" },
          { "text": "FieldsetLayout" },
          { "text": "FilmLayout" },
          { "text": "GridLayout" },
          { "text": "HorizontalLayout" }
        ] },
        { "text": "功能组件", "icon": ".ico-folder" },
        { "text": "基础表单", "icon": ".ico-folder", "open": true, "nodes": [
          { "text": "text" },
          { "text": "password" },
          { "text": "textarea" }
        ] }
      ] },
      { "type": "vert", "width": "*", "id": "main", "style": "padding:0 20px 10px 20px;", "nodes": [
        { "type": "horz", "height": 50, "nodes": [
          { "type": "html", "text": "text", "valign": "middle", "style": "float:left" },
          { "type": "horz", "style": "float:right", "nodes": [
            { "type": "buttonbar", "space": 8, "align": "right", "nodes": [
              { "type": "button", "text": "导出", "cls": "btn btn-main", "on": {"click": "this.cmd({type:'submit',src:'webapp/serv.php?act=submit'})"} },
              { "type": "button", "text": "导入", "cls": "btn", "on": {"click": "VM(this).find('gd').insertRow( { 'O': '操作', 'K': { type : 'text', name : 'usr', title: '姓名', height: 40, 'value' : '', transparent: true }, 'V': { type : 'text', name : 'pwd', title: '密码', 'value' : '', height: 40, transparent: true, readonly: true } } );"} },
              { "type": "button", "text": "版本", "cls": "btn", "nodes": [
                 { "type": "button", "text": "查看" },
                 { "type": "button", "text": "保存" }
              ] }
            ] },
            { "type": "buttonbar", "space": 1, "align": "right", "cls": "groupbar", "style": "margin-left:8px", "focusable": true, "nodes": [
              { "type": "button", "text": " 日 ", "cls": "btn" },
              { "type": "button", "text": " 周 ", "cls": "btn" },
              { "type": "button", "text": " 月 ", "cls": "btn", "focus": true },
              { "type": "button", "text": "全部", "cls": "btn", "on": {"click": "this.cmd('version')"}, "nodes": [
                 { "type": "button", "text": "查看" },
                 { "type": "button", "text": "保存" }
              ] }
            ] }
          ] }
        ] },
        { "type": "horz", "height": "*", "nodes": [
          { "type": "vert", "width": "*", "cls": "bd-mod", "wmin": 2, "hmin": 2, "nodes": [
            { "type": "buttonbar", "height": "40", "cls": "modtabbar f-14", "focusable": true, "nodes": [
              { "type": "button", "text": "    预览    ", "cls": "modtab", "target": "c_prev", "focus": true },
              { "type": "button", "text": "    编辑    ", "cls": "modtab", "target": "c_edit" },
              { "type": "button", "text": "    JAVA    ", "cls": "modtab", "target": "c_java" },
              { "type": "button", "text": "    JSON    ", "cls": "modtab", "target": "c_json" }
            ] },
            { "type": "horz", "height": "*", "nodes": [
            { "type": "film", "width": "*", "dft": "c_prev", "nodes": [
              { "type": "vert", "id": "c_prev", "nodes": [
                { "type": "grid", "id": "gd", "height": "150", "face": "cell", "scroll": true, "cellpadding": 0, "columns": [
					{"func":"text","width":"35%","field":"K"},
					{"func":"text","width":"*","field":"V"},
					{"func":"text","width":"40","field":"O"}
                  ]
                }
              ] },
              { "type": "html", "text": "编辑", "id": "c_edit" },
              { "type": "html", "text": "JAVA", "id": "c_java" },
              { "type": "html", "text": "JSON", "id": "c_json" }
            ] },
            { "type": "tree", "width": "240", "cls": "bg-low bd-mod bd-onlyleft", "wmin": 1, "nodes": [
              { "text": "组件结构", "open": true, "nodes": [
                { "text": "view", "open": true, "nodes": [
                  { "text": "VerticalLayout", "open": true, "nodes": [
                    { "text": "FormPanel" }
                  ] }
                ] }
              ] }
            ] }            
          ] }
        ] }
      ] }
    ] }
  ] }
}

	    		        		    
<? } else { ?>

{ "type": "view", "commands" : {
  	"js": { "type" : "confirm", "cover": true, "yes": "alert(this.x.text)", "text" : "" }
  },
  "node": {
    "type": "vert", "id": "root", "width": "*", "height": "*", "nodes": [
      { "type": "horz", "height": 50, "id": "top", "cls": "bg-top", "nodes": [
        { "type": "html", "text": "", "width": 240, "cls": "bg-logo", "valign": "middle", "text": "&nbsp; <i class=ico-logo></i>" },
        { "type": "buttonbar", "id": "wstab", "width": "*", "focusable": true, "nodes": [
          { "type": "button", "text": "视图", "cls": "wstab", "target": "m_view", "focus": true },
          { "type": "button", "text": "模板", "cls": "wstab", "target": "m_tmpl" },
          { "type": "button", "text": "简介", "cls": "wstab", "target": "m_intr" }
        ] }
      ] },
      { "type": "film", "height": "*", "id": "main", "dft": "m_view", "nodes": [
        { "type": "view", "src": "webapp/serv.php?act=view", "id": "m_view" },
        { "type": "view", "src": "webapp/serv.php?act=version", "id": "m_tmpl" },
        { "type": "view", "src": "webapp/serv.php?act=version", "id": "m_intr" }
      ] }
  ] },
  "templates": {
    "std": {
      "cls": "dlg-std", "node": {
    	"type": "vert",	"nodes": [
    	  { "type": "horz", "height": 40, "cls": "dlg-std-head", "nodes": [
    		{ "type": "template/title", "cls": "dlg-std-title", "width": "*", wmin: 10 },
    		{ "type": "html", "cls": "dlg-max", "align": "center", "valign": "middle", "text": "<i class=ico-dlg-max></i>", "width": "40", "on": { "click": "$.dialog(this).max(this)" } },
    		{ "type": "html", "cls": "dlg-close", "align": "center", "valign": "middle", "text": "<i class=ico-dlg-close></i>", "width": "40", "on": { "click": "$.dialog(this).close()" } }
    	  ] },
    	  { "type": "template/view", "height": "*" }
    	]
      }
    },
    "alert": {
      "cls": "dlg-std", "node": {
    	"type": "vert",	"nodes": [
    	  { "type": "horz", "height": 40, "cls": "dlg-std-head", "nodes": [
    		{ "type": "template/title", "cls": "dlg-std-title", "width": "*", wmin: 10 },
    		{ "type": "html", "cls": "dlg-close", "align": "center", "valign": "middle", "text": "<i class=ico-dlg-close></i>", "width": "40", "on": { "click": "$.dialog(this).close()" } }
    	  ] },
    	  { "type": "template/view", "height": "*" }
    	]
      }
    },
    "prong": {
      "cls": "dlg-prong", "indent": -10, "node": {
    	"type": "vert", "aftercontent": "<div class=dlg-prong-arrow-out></div><div class=dlg-prong-arrow-in></div>", "nodes": [
    	  { "type": "horz", "height": 36, "cls": "dlg-prong-head", "nodes": [
    	    { "type": "template/title", "cls": "dlg-prong-title", "width": "*" },
    		{ "type": "html", "text": "<i onclick=$.close(this) class='dlg-close'>&times;</i><i class=f-vi></i>", "align": "center", "valign": "middle", "width": "40" }
    	  ] },
    	  { "type": "template/view", "height": "*" }
    	]
      }
    }
  }
}

<? } ?>

