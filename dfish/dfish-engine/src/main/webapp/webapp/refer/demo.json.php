<? 
header( 'Content-Type: text/html; charset=UTF-8' );
$act = $_GET['act'];

if ( $act == 'panel-html' ) { ?>

{
	"type": "view",
	"commands" : {
		"ajax-menu" : { "type" : "ajax", "src" : "webapp/demo.json.php?act=ajax-menu&id=$0" }
	},
	"node": {
	  "type": "vert",
	  "nodes": [
		  {
			"id":"d_tree",
			"type":"tree",
			"height": "*",
			"scroll": true,
			"style1": "background:pink",
			"pub":{
				"text":"",
				"on":{"contextmenu":"this.cmd('ajax-menu',$id)"}
			},
			"nodes":[
				{
					"text":"福建省",
					"id":"350000",
					"nodes":[
						{
							"text":"福州市",
							"id":"350100",
							"nodes":[
								{"text":"鼓楼区","id":"350102"},
								{"text":"台江区","id":"350103"},
								{"text":"晋安区","id":"350103"},
								{"text":"仓山区","id":"350103"},
								{"text":"台江区","id":"350103"},
								{"text":"马尾区","id":"350103"}
							]
						},
						{
							"text":"厦门市",
							"id":"350200",
							"nodes":[
								{"text":"湖里区","id":"350202"}
							]
						}
					]
				},
				{"text":"台湾省<?=$_REQUEST[id]?>","id":"390000"}
			]
		},
		{ "type": "buttonbar","height": 50, "cls": "dlg-foot", "align": "right", "space": 8, "style": "padding-right:20px", "nodes": [
		  { "type" : "submitbutton", "cls": "f-button", "text" : "   确定   " },
		  { "type" : "button", "cls": "f-button", "text" : "   取消   " }
		]}
	]}
}

<? } else if ( $act == 'treeitem' ) {  ?>

[
  {
  	"id"   : "tree-0-1-0",
  	"text" : "那个那个tree item 0-1-0"
  },
  {
  	"id"   : "tree-0-1-1",
  	"text" : "那个那个tree item 0-1-1",
  	"src"  : "webapp/demo.json.php?act=treeitem&t=<?=$_REQUEST['t']?>"
  }
]

<? } else if ( $act == 'opento' ) { ?>

{ "type": "tree", "nodes": [
  { "id": "tree-0-1", "text": "那个那个tree item 2-1", "open": true, "nodes": [
    { "id": "opentome", "text": "opentome", "focus": true }
  ] }
] }
				        	  	
<? } else if ( $act == 'grid-tree' ) { ?>

[
  {"C0":"大伯-0","C1":{ "node": { "type": "leaf", "text": "堂兄" } },"C2":"大伯-2","C3":"大伯-3"},
  { "type": "tr", "data": {"C0":"大伯-0","C1":{ "node": { "type": "leaf", "text": "堂妹" } },"C2":"大伯-2","C3":"大伯-3"},
    "rows": [
      {"C0":"大伯-0","C1":{ "node": { "type": "leaf", "text": "堂孙子" } },"C2":"大伯-2","C3":"大伯-3"}
    ]
  }
]

<? } else if ( $act == 'ajax-js' ) {  ?>
{ "type": "cmd", "nodes": [
  { "type": "replace", "target": "fs_view", "node": { "type": "view", "id": "fs_view", "src": "webapp/demo.json.php?act=panel-html" } },
  { "type": "replace", "target": "ejzc", "node": { "type" : "button", "icon" : "core/ui/g/folder.png", "text" : "新鲜紫菜", "id": "ejzc", "on": { "click": "alert(this.x.text)" } } }
] }
<? } else if ( $act == 'calendar' ) { ?>

	{ "type": "calendar/date", "date": "<?=$_REQUEST[date]?>", "height": -1, "focusdate": "<?=$_REQUEST[date]?>", "src": "webapp/demo.json.php?act=calendar&date=$0", "css": { "value": "NNNNNNNNNYNNNNNNNNYYYYYYYNNNNNN", "Y" : "color:green;" }, "pub": { "on": { "click": "alert(this.val())" } } }

<? } else if ( $act == 'ajax-menu' ) { ?>

{
	"type":"menu",
	"nodes":[
		{
			"type":"button",
			"text":"新增节点",
			"more":{
				"type":"menu",
				"nodes":[
					{
						"type":"button",
						"text":"view",
						"more":{
							"type":"menu",
							"nodes":[
								{
									"type":"button",
									"text":"View(View)",
									"on":{
										"click":"this.cmd({'type':'ajax','src':'webapp/demo.json.php?act=ajax-js'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性6",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性7",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性8",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性9",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性10",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性11",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性12",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性13",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性14",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性15",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性16",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性17",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性18",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性19",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性20",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性21",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性22",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性e",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性d",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性c",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性b",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								},
								{
									"type":"button",
									"text":"编辑属性a",
									"on":{
										"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
									}
								}
								
								
							],
							"record":false
						}
					}
				],
				"record":false
			}
		},
		{
			"type":"button",
			"text":"位置编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5编辑属性5",
			"more":{
				"type":"menu",
				"nodes":[
					{"type":"button", "text":"上移"},
					{"type":"button", "text":"下移"}
				],
				"record":false
			}
		},
		{
			"type":"button",
			"text":"删除节点",
			"on":{
				"click":"if(confirm('确认删除节点 吗?')){ VM(this).cmd({'type':'ajax','src':'dfishEditor/deleteNode/ROOT0000000000000004'});}"
			}
		},
		{
			"type":"button",
			"text":"编辑属性4",
			"on":{
				"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
			}
		},
		{
			"type":"button",
			"text":"编辑属性5",
			"on":{
				"click":"VM(this).cmd({'type':'ajax','src':'dfishEditor/editNode/ROOT0000000000000004'});"
			}
		}
	],
	"record":false
}
<? } else if ( $act == 'submit' ) {  ?>

	{ "type" : "alert", "text": function(){ /*<? print_r($_REQUEST) ?>*/ } }

<? } else if ( $act == 'sortasc' ) { sleep(1); ?>

	{ "type" : "alert", "text": function(){ /*<? print_r($_REQUEST) ?>*/ } }

<? } else if ( $act == 'combo' ) { ?>

{
  "type" : "view",
  "cls": "f-bg-white",
  "node": {
    "type": "vert",
    "nodes": [{
     "id":"f_grid",
      "type":"grid",
      "face":"line", /* none,line,cell */
      "scroll":true, /* true或.开头样式名 */
      "height":"*",
      "hoverable": true,
      "focusable": true,
      "combofield": { "value": "id", "text": "name", "remark": "remark", "search": "py", "forbid": "x" },
      "limit": 5,
      "pub": {
        "height" : 36,
        "on":{
          "click": "$.dialog(this).parentNode.complete(this);$.dialog(this).close()"
        }
      },
      "columns":[
        {"field":"name", "width":"*","nobr":true},
        {"field":"dept", "width":"100", "align": "right", "format":"<a href=dept/$id>$dept</a>"}
      ],
      "rows": [
      <? if ( $_REQUEST[ v ] ) { ?>
        {"name":"aa","dept":"音乐部","remark":"<a@b.com>","id":"0001","py":"zhoujiegun"},
        {"name":"abheyman","dept":"武术部","id":"0002","py":"shuangjielun"},
        {"name":"ac哎呦不错","dept":"嘻哈部","id":"0003","py":"aiyobucuo","x":true}
      <? } else if ( $_REQUEST[ t ] ) { ?>
        {"name":"全能","dept":"刀塔部","id":"0004","py":"quanneng"},
        {"name":"熊猫","dept":"刀塔部","id":"0005","py":"xiongmao"},
        {"name":"先知","dept":"刀塔部","id":"0006","py":"xianzhi"},
        {"name":"冰魂","dept":"刀塔部","id":"0007","py":"binghun"},
        {"name":"炸弹","dept":"刀塔部","id":"0008","py":"zhadan"},
        {"name":"这个名字有点长长长长长长长长长长长长长长长长长长长长啊","dept":"刀塔部","id":"0009","py":"chang"}
      <? } ?>
      ]
    },
	{ "type": "page/mini", "height": "22", "btncount": 5, "align": "right", "target": "f_grid" }
  ]}
}

<? } else if ( $act == 'combo-tree' ) { ?>

{
  "type" : "view",
  "cls": "f-bg-white",
  "node": {
 	"type" : "tree",
	"id"   : "f_tree",
	"height": "200",
	"src"  : "demo.json.php?act=panel-tree-root2",
	"scroll" : true,
    "combofield": { "value": "id", "text": "text", "search": "_py", "forbid": "x" },
    "pub": {
      "on": { "click": "$.dialog(this).commander.complete(this);$.dialog(this).close()" }
   },
	"nodes" : [
		{
			"id"   : "0001",
			"text" : "rooooooooooot",
			"open" : true,
	   		"nodes" : [
    			{
    				"id"   : "tree-1-0",
    				"text" : "aaaaaaaabbbbbbbb",
    				"_py": ""
    			},
    			{
    				"id"   : "tree-1-1",
    				"text" : "bbbbbbbccccccccc",
    				"data": {"_uid":"0002"}
    			}
    		]
		},
		{
			"id"   : "tree-0-1",
			"text" : "dddddddddddddddd",
			"src"  : "webapp/demo.json.php?act=treeitem&t=<?=$_REQUEST['t']?>"
		},
		{
			"id"   : "tree-0-2",
			"text" : "eeeeeeeeeeeeeee",
			"icon" : "",
			"nodes": [
				{
					"id"   : "tree-0-2-0",
					"text" : "bbbbbbbbeeeeeeee",
					"icon" : ""
				}
			]
		}
	]
  }
}

<? } else if ( $act == 'suggest' ) { ?>

{
  "type" : "view",
  "cls": "f-bg-white",
  "node": {
    "type": "vert",
    "nodes": [{
      "id":"f_grid",
      "type":"grid",
      "face":"line", /* none,line,cell */
      "scroll":true, /* true或.开头样式名 */
      "height":"*",
      "hoverable": true,
      "focusable": true,
      "combofield": { "value": "id", "text": "name", "remark": "remark", "search": "py", "forbid": "x" },
      "limit": 5,
      "page": 1,
      "pub": {
        "height" : 36,
        "on":{
          "click": "$.dialog(this).parentNode.complete(this);$.dialog(this).close()"
        }
      },
      "columns": [
        {"field":"name", "width":"*", "nobr":true},
        {"field":"dept", "width":"100", "align": "right", "format":"<a href=dept/$id>$dept</a>"}
      ],
      "rows": [
        {"name":"陈敏","dept":"研发中心","remark":"(研发中心)","id":"1004","py":"chenmin"},
        {"name":"军团","dept":"刀塔部","id":"0010","py":"juntuan"},
        {"name":"小娜迦","dept":"刀塔部","id":"0011","py":"xiaonajia"},
        {"name":"月骑","dept":"刀塔部","id":"0012","py":"yueqi"},
        {"name":"双头龙","dept":"刀塔部","id":"0013","py":"shuangtoulong"},
        {"name":"骷髅王","dept":"刀塔部","id":"0014","py":"kulouwang"},
        {"name":"猴子","dept":"刀塔部","id":"0015","py":"houzi"},
        {"name":"火女","dept":"刀塔部","id":"0016","py":"huonv"},
        {"name":"直升机","dept":"<?=$_REQUEST[t]?>","id":"0017","py":"zhishenji"}
      ]
    },
	{ "type": "page/mini", "height": "22", "btncount": 5, "align": "right", "target": "f_grid" }
    ]
  }
}

<? } else if ( $act == 'online' ) { ?>

{
	"type" : "view",
	"cls": "f-bg-white",
	"node": {
		"id":"f_grid",	"type":"grid", "test": true,
		"face":"line", /* none,line,cell */
		"scroll":true, /* true或.开头样式名 */
		"height":"*",
		"hoverable": true,
		"focusable": true,
		"combofield": { "value": "name" },
		"pub": {
		  "height" : 36,
		  "on":{
		    "click": "$.dialog(this).commander.complete(this)"
		  },
		},
		"columns": [
		  {"field":"name", "width":"*"},
		  {"field":"dept", "width":"100", "align": "right", "format":"<a href=dept/$id>$dept</a>"}
		],
		"rows": [
		  {"name":"<?=$_REQUEST['q']?>_a","dept":"音乐部","id":"0001","py":"zhoujiegun"},
		  {"name":"<?=$_REQUEST['q']?>_b","dept":"武术部","id":"0002","py":"shuangjielun"},
		  {"name":"<?=$_REQUEST['q']?>_c","dept":"嘻哈部","id":"0003","py":"aiyobucuo","x":true}
		]
	}
}

<? } else if ( $act == 'pick' ) { ?>

{
  "type": "view",
  "cls": "f-bg-white",
  "node": {
    "type":"vert",
    "nodes": [
      { "type": "textarea", "name": "rr", "height": "*", "value": "dfish.dialog(this).parentNode.val('0001','嘿嘿')" },
      { "type": "buttonbar", "height": 40, "valign": "middle", "nodes":[ { "type": "button", "text": "确定", "cls": "f-button", "on": { "click": "eval(VM(this).fv('rr'))" } } ] }
    ]
  }
}

<? } else if ( $act == 'test' ) { ?>

{
  "type": "view",
  "node": {
    "type": "fieldset", "id": "fieldset", "legend": "dd", "box": { "name": "fs_box", "value": "", "text": "什么情况" }, "nodes": [
      { "type": "html", "id": "fs_view" }
    ] }
}
		    		        		    
<? } else { ?>

{
	"type" : "view",
	"commands" : {
		"js" : { "type" : "confirm", "cover": true, "yes": "alert(this.x.text)", "text" : "在油锅中将葱姜爆香，再加入辣豆瓣酱，然后放入牛肉块翻炒并加入酱油，糖胡椒 粉，酒，味精及八角，最后加水浸过牛肉，用小火慢慢煮至汁稠，肉酥香即可。在油锅中将葱姜爆香，再加入辣豆瓣酱，然后放入牛肉块翻炒并加入酱油，糖胡椒 粉，酒，味精及八角，最后加水浸过牛肉，用小火慢慢煮至汁稠，肉酥香即可。" },
		"ajax-js" : { "type" : "ajax", "src" : "webapp/demo.json.php?act=ajax-js" },
		"ajax-menu" : { "type" : "ajax", "src" : "webapp/demo.json.php?act=ajax-menu&id=$0" },
		"submit" : { "type" : "submit", "src" : "webapp/demo.json.php?act=submit" },
		"cmd-dialog" : { "type" : "dialog", "width": 500, "height": 360, "title": "新建", "template": "std", "id": "new", "src" : "webapp/demo.json.php?act=panel-html&id=$0" },
		"before": { "type": "before", "target": "mj", nodes: [ { "type": "html", "text": "insert1", "height": "*" },{ "type": "html", "text": "insert2", "height": "*" } ] },
		"replace": { "type": "replace", "target": "2nd", nodes: [ { "type": "html", "id": "2nd", "text": "update1", "height": "500" } ] },
		"remove": { "type": "remove", "target": "c_grid" },
		"insertRow": { "type": "prepend", "target": "c_grid", nodes: [ {"C0":"4-0","C1":"4-1","C2":"4-2","C3":"4-3"}, {"C0":"5-0","C1":"5-1","C2":"5-2","C3":"5-3"} ] },
		"alert": {
			"type": "alert",
			"icon": ".w-alert-warn",
			"text": "提示内容",
			"button": [
				{ "type": "button", "text": "确定" }
			]
		},
		"group" : {
			"type" : "cmd",
			"nodes" : [
				{ "type" : "js", "text" : "alert('cmd')" }
			]
		}
	},
	"templates": {
		"std": {
		  "cls": "dlg-std", "wmin": 2, "hmin": 2, "node": {
			"type": "vert",	"nodes": [
			  { "type": "horz", "height": 40, "cls": "dlg-std-head", "nodes": [
				{ "type": "template/title", "cls": "dlg-std-title", "width": "*", wmin: 10 },
				{ "type": "html", "text": "<i onclick=$.close(this) class=dlg-close>&times;</i>", "align": "center", "valign": "middle", "width": "40" }
			  ] },
			  { "type": "template/view", "height": "*" }
			]
		  }
		},
		"prong": {
		  "cls": "dlg-prong",
		  "indent": -10,
		  "node": {
			"type": "vert",
			"aftercontent": "<div class=dlg-prong-arrow-out></div><div class=dlg-prong-arrow-in></div>",
			"nodes": [
				{
					"type": "horz",
					"height": 36,
					"cls": "dlg-prong-head",
					"nodes": [
						{ "type": "template/title", "cls": "dlg-prong-title", "width": "*" },
						{ "type": "html", "text": "<i onclick=$.close(this) class='dlg-close'>&times;</i><i class=f-vi></i>", "align": "center", "valign": "middle", "width": "40" }
					]
				},
				{ "type": "template/view", "height": "*" }
			]
		  }
		}
	
	},
	"node" : {
		"type": "horz",
		"id": "layout",
		"width":  "*",
		"height": "100%",
		"nodes" : [
			{ "type": "hidden", "name": "hidden", "value": "1" },
			{
			  "type": "deck",
			  "id": "f_left",
			  "width": 260,
			  "buttonheight": 40,
			  "nodes": [
			    { "type": "button", "icon": "", "text": "按钮1" },
                { "type" : "vert", "nodes": [
			        { "type" : "buttonbar",  "height": "*", "cls": "f-tab-bar z-vert-1", "pub":{ "name": "verttab_0", "cls": "f-tab" }, "nodes" : [
		    		  { "type" : "button", "text" : "小塔布" },
		    		  { "type" : "button", "text" : "塔小布", "focus": true },
		    		  { "type" : "button", "text" : "塔布小" }
		    		] }
		        ] },
			    { "type": "button", "icon": "", "text": "按钮2" },
                { "type": "vert", "nodes": [
                  { "type": "calendar/date", "height": -1, "date": "2015-01-01", "focusdate": "2015-01-15", "src": "webapp/demo.json.php?act=calendar&date=$0", "css": { "value": "NNNNNNNNNYNNNNNNNNYYYYYYYNNNNNN", "Y" : "color:green;font-weight:bold" }, "pub": { "on": { "click": "alert(this.val())" } } },
                    { "type": "calendar/week", "height": -1, "date": "2015", "focusdate": "2015-03", "src1": "webapp/demo.json.php?act=calendar&date=$0", "css": { "value": "NNNNNNNNNYNNNNNNNNYYYYYYYNNNNNN", "Y" : "color:green;font-weight:bold" }, "pub": { "on": { "click": "alert(this.val())" } } },
                    { "type": "calendar/month", "height": -1, "date": "2015", "focusdate": "2015-03", "src1": "webapp/demo.json.php?act=calendar&date=$0", "css": { "value": "NNNNNNNNNYNNNNNNNNYYYYYYYNNNNNN", "Y" : "color:green;font-weight:bold" }, "pub": { "on": { "click": "alert(this.val())" } } },
                    { "type": "calendar/year", "height": -1, "date": "2015", "focusdate": "2015-03", "src1": "webapp/demo.json.php?act=calendar&date=$0", "css": { "value": "NNNNNNNNNYNNNNNNNNYYYYYYYNNNNNN", "Y" : "color:green;font-weight:bold" }, "pub": { "on": { "click": "alert(this.val())" } } }
                ] },
                { "type": "button", "id": "deckbutton-3", "icon": "", "text": "按钮3" },
                { "type": "vert", "height": "*", "nodes": [
		    		{ "type" : "buttonbar", "id": "dkbar", "height": "*", "dir": "v", "valign": "middle", "align": "center", "space": "3", "pub": { "cls": "f-button" }, "nodes" : [
		    			{ "type" : "button", "text" : "insertRow", "on" : { "click" : "this.cmd('insertRow')" } },
		    			{ "type" : "button", "text" : "updateRow", "on" : { "click" : "this.cmd('updateRow')" } },
		    			{ "type" : "button", "id": "dkb2", "text" : "moveRow", "on" : { "click" : "VM('/index').find('c_grid').row({C0:'5-0'}).move('+=1');" } },
		    			{ "type" : "button", "id": "dkb3", "text" : "deleteRow", "on" : { "click" : "alert(this.x.text)" }, "more" : {
		    			  "type": "menu",
		    			  "nodes" : [ { "icon" : "core/ui/g/folder.png", "text" : "子选项" } ] }
		    			}
		    		] }
                ] },
                { "type": "button", "id": "deckbutton-3", "icon": "", "text": "按钮4", "focus": true },
				{ "id": "c_img", "type": "album", "scroll": true, "height":"*", "space": 5, "hoverable": true, "focusable": true, "focusmultiple": !true,
				  "pub": {
				    "width": 100,
				    "height": 100,
				    "box": { "type": "checkbox", "name": "imgbox" },
				    "nobr": true,
				    "on1":{ "click": "alert('$1')" }
				  },
				  "format": "$name",
				  "nodes": [
				    { "type" : "img", "src": "http://tp1.sinaimg.cn/2625738240/180/40000242199/1", "box": { "value": "001" }, "data": { "name": "aaaaa.jpg" } },
				    { "type" : "img", "src": "http://tp2.sinaimg.cn/2214257545/180/5634974717/0",  "text" : "bbbbb.jpg", "box": { "value": "002" } },
				    { "type" : "img", "src": "http://tp3.sinaimg.cn/2622821682/180/5662155841/1",  "text" : "ccccccccccccccccccccccccccccccccc.jpg", "box": { "value": "003" } },
				    { "type" : "img", "src": "http://tp4.sinaimg.cn/2495996991/180/5638239663/1",  "text" : "ddddd.jpg", "box": { "value": "004" } },
				    { "type" : "img", "src": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "text" : "eeeee.jpg", "box": { "value": "005" }, "focus": true },
				    { "type" : "img", "src": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0",  "text" : "fffffffffffffffffffffffffffffffff.jpg", "box": { "value": "006" } }
				] }
			  ]
			},
			{
				"type" : "vert",
				"width": "*",
				"id": "f_center",
				"nodes" : [
				  {
				    "id":"c_grid",
				    "type":"grid",
				    "face":"cell", /* none,line,cell */
				    "scroll":true, /* true或.开头样式名 */
				    "height":"240",
				    "escape": true,
				    "hoverable": true, /* 是否有hover效果 */
				    "focusable": true, /* 是否有focus效果 */
				    "focusmultiple": true, /* 是否有多选的focus效果 */
				    "resizable": true,
				    "pub": {
				      "height" : 40,
				      "on1":{
				        "click": "alert('$1')"
				      }
				    },
				    "columns":[
				      {"field":"C0", "width":100, "align":"center"},
				      {"field":"C1", "width":"*", "sort":"desc", "sortsrc": "webapp/demo.json.php?act=sortasc"},
				      {"field":"C2", "width":"100"},
				      {"field":"C3", "width":"100", "format":"<a href=javascript: onclick=alert($.w(this).x.data.C3)>$C3</a>"}
				    ],
				    "thead": {
				      "rows": [
				        { "height": 40, "cls": "bg-form", "data": {"C0":"名称","C1":"修改日期","C2":"类型","C3":"format" } }
				      ]
				    },
				    "rows": [
				      {"C0":{ "rowspan": 1, "text": "r-0" },"C1":"0-1","C2":"<u>0-2</u>","C3":"<u>0-3</u>"},
				      {"C0":"1-0","C1":{ "rowspan": 2, "text": "1-1" },"C2":"1-2","C3":"1-3"},
				      {"C0":"x-0","C1":"x-1","C2":"x-2","C3":"x-3"},
				      {
				        "C0": { "colspan": 1, "align": "left", "text": "colspan2", "style": "background:pink" },
				        "C1": "2-1",
				        "C2": { "node": { "type" : "text", "value" : "red", "name": "background" } },
				        "C3": "2-3"
				      }
				    ]
				  },
				  { "id": "f_flow", "type": "vert", "height": "*", "scroll": true, "on": { "scroll1": "alert()" }, "nodes": [
				      {
				    	"id"     : "c_treegrid",
				    	"type"   : "grid",
				    	"face"   : "dot", /* none,line,cell,dot */
				    	"scroll1": true,
				        "hoverable": true,
				        "focusable": true,
				    	"pub": {
				    	  "height": 40,
				    	  "on1":{
				    	    "click": "alert('$1')"
				    	  }
				    	},
				    	"columns":[
				    	  { "field":"C0", "width":100, "align":"center", "box": {"type":"checkbox", "name":"mySelectItem"} },
				    	  { "field":"C1", "width":"*", "minwidth": 300 },
				    	  { "field":"C2", "width":"*", "minwidth": 300 },
				    	  { "field":"C3", "width":"500", "format":"<a href=javascript: onclick=alert($.w(this).x.data.C0)>$C3</a>"}
				    	],
				    	"thead": {
				    	  "fix": true,
				    	  "rows": [
				    	    { "type": "tr", "height": 40, "data": {"C0":"博克斯","C1":"树","C2":"打酱油","C3":"佛马特"} }
				    	  ]
				    	},
				    	"rows": [
				    	  {
				    	    "data": {"C0":{"value":"0-0"},"C1": { "type": "toggle", "text": "更早", "open": !true },"C2":"0-2","C3":"0-3"},
				    	    "rows": [
				    	      {"C0":{"value":"0-0-1"},"C1": "西瓜大抢购","C2":"0-0-2","C3":"0-0-30-0-30-0-30-0-30-0-30-0-3"},
				    	      {"C0":{"value":"0-0-2"},"C1": "萝卜大甩卖","C2":"0-0-2","C3":"0-0-30-0-30-0-30-0-30-0-30-0-3"}
				    	    ]
				    	  },
				    	  {
				    	    "type": "tr",
				    	    "src": "webapp/demo.json.php?act=panel-html",
				    	    "data": {"C0":{"value":"1-0"},"C1":{ "colspan": 1, "node": { "type" : "html", "text" : "blue", "style": "background:blue;color:white;position:relative" } },"C2":"1-2","C3":"1-3"}
				    	  },
				    	  {"C0":{"value":"2-0"},"C1":{ "node": { "type": "leaf", "text": "大伯", src: "webapp/demo.json.php?act=grid-tree" } },"C2":"2-2","C3":"0-3"}
				    	]
				      },
				      {	"type": "page/mini", "height": "22", "currentpage": 4, "pagecount": 5, "sumpage": 10, "src": "webapp/demo.json.php?act=page&page=$0", "align": "right" },
				      {	"type": "page/text", "height": "30", "currentpage": 4, "pagecount": 5, "sumpage": 10, "jump": true, "info": "11/220 条", "nofirstlast1": true, "src": "webapp/demo.json.php?act=page&page=$0", "setting": [
				        { "text": "显示设置" }, { "text": "每页显示" }
				      ] },
				      { "type": "page/buttongroup", "height": "30", "pagecount": 1, "currentpage": 1, "sumpage": 55, "src": "webapp/demo.json.php?act=page&page=$0", "setting": [
				        { "text": "显示设置" }, { "text": "每页显示" }
				      ] }
				    ] }, 
				    {
				      "type": "toggle", "height": "30", "text": "我有一条小尾巴", "hr": true, "open": true, "style1": "background:lightblue", "target": "flowtree"
				    },
				    { "type": "vert", "id": "flowtree",
				      //"scroll": true,
				      "height": "*",
				      "nodes": [
				        { "type": "horz",
				          "height": "*",
				          "nodes": [
				            { "type" : "tree",
				        	  "id"   : "f_tree",
				        	  "src"  : "demo.json.php?act=panel-tree-root",
				        	  "scroll" : true,
				        	  "width": "300",
				        	  /*"style": "background:pink",*/
				        	  "pub": {
					          	"icon" : "%img%/folder.png",
					          	"openicon" : "%img%/folderopen.png",
					          	"box": { "type": "triplebox", "name": "xxx" },
				        	  	"on" : {
				        	  		"click" : "",
				        	  		"contextmenu": "this.cmd('ajax-menu',$id)"
				        	  	}
				        	  },
				        	  "nodes" : [
				        	  	{
				        	  		"id"   : "tree-0-0",
				        	  		"text" : "tree item 0-0",
				        	  		"open" : true, "focus": true,
				        	  		"box": { "name": "treebox0", "value": "2", "on": { "change": "alert(this.type)" } },
				        	  		"icon": ".f-icon .w-upload-icon-local",
					          		"nodes" : [
					          			{
					          				"id"   : "tree-1-0",
					          				"text" : "这个这个tree item 0-1",
					          				"box": { "name": "treebox0-1", "value": "1", "on1": { "change": "alert(this.val())" } },
					          				"data": {"_uid":"0001"}
					          			},
					          			{
					          				"id"   : "tree-1-1",
					          				"text" : "那个那个tree item 1-1",
					          				"box": { "name": "treebox1-1", "value": "0-1" },
					          				"data": {"_uid":"0002"},
					          				"nodes": [
					          				  { "id": "tree-0-1-0", "text": "那个那个tree item 0-1-0", "box": { "name": "treebox1-1-0", "value": "0-1-0" } },
					          				  { "id": "tree-0-1-1", "text": "那个那个tree item 0-1-0 枸杞炖银耳 枸杞炖银耳 枸杞炖银耳 枸杞炖银耳 枸杞炖银耳", "box": { "name": "treebox1-1-0", "value": "0-1-0" } }
					          				]
					          			}
					          		]
				        	  	},
				        	  	{
				        	  		"id"   : "tree-0-1",
				        	  		"text" : "那个那个tree item 2-1",
				        	  		"box": { "type": "checkbox", "name": "treebox0", "value": "2" },
				        	  		"icon1" : ".f-i-close",
				        	  		"src"  : "webapp/demo.json.php?act=treeitem",
				        	  		"on": {
				        	  			"click" : ""
				        	  		}
				        	  	},
				        	  	{
				        	  		"id"   : "tree-0-2",
				        	  		"text" : "一品豆腐tree item 0-2",
				        	  		"icon" : "",
				        	  		"on": {
				        	  			"click" : "VM(this).find('fs_view').reload('webapp/demo.json.php?act=panel-html')"
				        	  		}
				        	  	}
				        	  ]
				            },
					        { "type": "split", "width": 1, "style": "background:red", "icon": ".f-i-config", "openicon": ".f-i-close", "range": "0,200,300", "target": "prev" },
				            {
				        	  "type": "html",
				        	  "width": "*",
				        	  "id": "label-0-0",
				        	  "style": "background:#f2f5f5",
				        	  "on":{"click":"alert()"},
				        	  "text": "label-0-0<a href=javascript: onclick=VM(this).find('tree-0-0').append({box:{name:'treebox1-1-0',value:'0-1-0'},text:index.test(this)}).focus()>新增树节点</a>\
				        	  	<a href=javascript: onclick=VM(this).find('mj').after([{type:'text',id:'nt',value:'nt'},{type:'text',id:'nt2',value:'nt2'}])>新增文本框</a>\
				        	  	<d:wg>{type:'text',id:'mj',name:'mj',value:'mj',height:24,placeholder:'ddd'}</d:wg><div id=htmltest></div>"
					        }  ]
					    }  ]
					},
				    {
				    	"type": "iframe",
				    	"id": "label-0-1",
				    	"width": "*",
				    	"height": "100",
				    	"src": "",
				    	"text": "i am iframe"
					},
				    {
				    	"type": "html",
				    	"id": "label-0-2",
				    	"text": "label-0-2",
				    	"width": "*",
				    	"height": "100",
				    	"style": "background:blue;color:#fff"
					}				
				]
			},
			{ "type": "split", "range": "100,100", "width": 1, "style": "background:#ddd" },
		    {
		    	"type": "vert",
				"id": "f_right",
				"width": "50%",
		    	"nodes": [
		    		{ "type": "grid",
		    		  "id": "right_table",
				      "columns": [
				        {"field":"C0", "width":90, "align":"right"},
				        {"field":"C1", "width":"*"},
				        {"field":"C2", "width":90, "align":"right"},
				        {"field":"C3", "width":"*"}
				      ],
				      "rows": [
				        {
				          "C0": "text:",
				          "C1": {
				            "type" : "text", "name" : "usr", "title": "姓名", "value" : "", "placeholder" : "钢铁侠钢铁侠钢铁侠钢铁侠钢铁侠钢铁侠钢铁侠钢铁侠钢铁侠", "readonly": false, "disabled" : false, "pick" : "VM(this).exec('shortcut',null,{target:this})",
			    	  	  "on": { "change": "console.log(this.val())" },
			    	  	  "validate": { "required": true, "maxlength": 5, "pattern": "/^[^<>]+$/", "patterntext": "姓名包含非法字符" }
			    	      },
			    	      "C2": "password:",
			    	      "C3": {
			    	        "type" : "password", "name" : "pwd", "title": "密码", "value" : "", "placeholder" : "password", "disabled" : false,
			    	        "validate": { "required": true, "maxlength": 5, "pattern": "/^[^<>]+$/", "patterntext": "姓名包含非法字符" },
			    	        "validategroup": {
			    	          "dft": { "required": true, "maxlength": 5, "pattern": "/^[^<>]+$/", "patterntext": "姓名包含非法字符" }
			    	        }
			    	      }
			    	    },
				        {
				          "C0": "checkbox:",
				          "C1": {
				            "type": "checkboxgroup", "name": "checkbox", "pub": { "width": "50%" }, "readonly": true,
			    	        "on": { "change" : "alert(this.val())" },
			    	        "options": [
			    	      	  { "text": "音乐", "value": "music" },
			    	      	  { "text": "体育", "value": "sport" },
			    	      	  { "text": "军事", "value": "army" },
			    	      	  { "text": "科技", "value": "tech" }
			    	        ],
			    	        "validate": { "required": true }
			    	      },
			    	      "C2": "radio:",
			    	      "C3": {
			    	        "type" : "radiogroup", "name" : "radio", "readonly": !true,
			    	        "on1" : { "change" : "alert(this.val())" },
			    	        "options": [
			    	      	{ "text" : "music", "value" : "music", "checked": true, "target": "combobox,1st" },
			    	      	{ "text" : "sport", "value" : "sport" }
			    	        ],
			    	        "validate": { "required": true }
			    	      }
			    	    },
				        {
				          "C0": "file:",
				          "C1": {
				            "type": "upload/file", "name": "file", "file_upload_limit": 2, "upload_url": "http://localhost/outlook/webapp/swfupload/upload.php",
				            "readonly1": true, "down_url": "down.php?id=$id", "value_width": 240,
				            "on": {
				              "fileselect1": "alert(this.getNewLoaders().length)",
				              "change1": "alert(event)"
				            },
			    	      	"upload_button": [
			    	      	  { "type": "upload/button", "text": "本地上传(最大2M)", "icon": ".w-upload-icon-local", "cls": "linkbtn" },
			    	      	  { "type": "button", "text": "从文件库选择", "icon": ".w-upload-icon-net", "cls": "linkbtn" }
			    	      	],
			    	      	"value_button": [
			    	      	  { "text": "预览", "on": { "click": "alert($name)" } }, { "text": "下载" }
			    	      	],
			    	      	"value": [
			    	      	  { "id": "001", "name": "00111111111111111111111111111111111111111111111.jpg" }
			    	      	]
			    	      },
			    	      "C2": "image:",
			    	      "C3": {
				            "type" : "upload/image", "name" : "image", "upload_url": "http://localhost/outlook/webapp/swfupload/upload.php", "file_upload_limit": 0,
				            "down_url": "$thumbnailUrl",
				            "on": {
				              "fileselect1": "alert(this.getNewLoaders().length)",
				              "change1": "alert(event)"
				            },
			    	      	"upload_button": [
			    	      	  { "type": "upload/button", "text": "  上传照片  ", "cls": "btn" }
			    	      	],
			    	      	"value_button": [
			    	      	  { "text": "预览", "on": { "click": "alert($id)" } }, { "text": "下载" }
			    	      	],
			    	      	"value": [
			    	      	  { "id": "001", "name": "001.jpg", "thumbnailUrl": "http://tva2.sinaimg.cn/crop.0.0.180.180.180/7534962ejw1e8qgp5bmzyj2050050aa8.jpg" }
			    	      	]
			    	      }
			    	    },
				        {
				          "C0": "select:",
				          "C1": {
				            "type" : "select", "name" : "select", "value": "", "width": "100%",
			    	        "on" : { "change" : "" },
			    	        "options": [
			    	          { "text" : "请选择", "value" : "" },
			    	          { "text" : "music", "value" : "music" },
			    	          { "text" : "sport", "value" : "sport" }
			    	        ],
			    	        "validate": { "required": true }
			    	      },
			    	      "C2": "date:",
			    	      "C3": { "type": "date", "name": "date", "width1": "100%", "value": "", "format": "yyyy-mm-dd hh:ii", "placeholder": "" }
			    	    },
				        {
			    	      "C0": "xbox:",
			    	      "C1": {
			    	        "type": "xbox", "name": "xbox", "id": "xbox", "value": "sport", "readonly": !true, 
			    	        "options": [
			    	          { "text" : "请选择", "value" : "" },
			    	          { "text" : "music", "value" : "music" },
			    	          { "text" : "sport", "value" : "sport" },
			    	          { "text" : "game", "value" : "game" },
			    	          { "text" : "food", "value" : "food" }
			    	        ],
			    	        "validate": { "required": true }
			    	      },
				          "C2": "spinner:",
				          "C3": {
				            "type": "horz", "id": "spinner_wrap", "height": -1,
				            "nodes": [
			    	          { "type": "spinner", "name": "spinner", "id": "spinner", "value": "100", "title": "数量",
			    	          "validate": { "minvalue": 10, "maxvalue": 99 },
			    	          "on": {
			    	              "change": "this.valid()",
			    	              "valid": "var r = this.getValidError();this.warn(!!r);this.next().text(r ? '请填入一个有效数字' : '')"
			    	          } },
			    	          { "type": "html", text: "" }
			    	        ]
			    	      }
			    	    },
				        {
			    	      "C0": "combobox:",
			    	      "C1": { "type": "combobox", "name": "combobox", "id": "combobox", "value": "0001,aa@rj.com", "text": "", "readonly1": true, "multiple": true, "placeholder": "124567893", "strict1": true,
			    	        "src": "webapp/demo.json.php?act=combo&v=$value&t=$text",
		    		  	    "suggestsrc": "webapp/demo.json.php?act=suggest&t=$text",
		    		  	    "dropsrc": "webapp/demo.json.php?act=combo-tree&t=1",
			    	        "picker": { "type":"dialog", "template": "std", "title": "这个那个", "src": "webapp/demo.json.php?act=pick", "width": 600, "height": 400 },
		    		  	    "on": { "click1": "alert('clk')" }, "pub": { "width": 110, "on": { "click": "alert(this.x.value)" } } },
				          "C2": "onlinebox:",
				          "C3": { "type": "onlinebox", "name": "online", "id": "online", "multiple": true, "value": "1", "title": "OL",
		    		  	    "src": "webapp/demo.json.php?act=online&q=$text" }
			    	    },
				        {
			    	      "C0": "textarea:",
			    	      "C1": { "type": "textarea", "name": "textarea", "id": "textarea", "value": "VM('/index').find('f_tree').openTo('webapp/demo.json.php?act=opento')", "height": "86", "placeholder": "哥德式" },
				          "C2": "boxgroup:",
				          "C3": {
				            "type": "checkboxgroup", "name": "checkboxgroup",
			    	        "options": [
			    	      	  { "text": "音乐人", "value": "music" },
			    	      	  { "text": "体育", "value": "sport" },
			    	      	  { "text": "军事", "value": "army", "checked": true }
			    	        ],
			    	        "targets": [
			    	          { "type": "text", "name": "grp_tag_1", "value": "1" },
			    	          { "type": "text", "name": "grp_tag_2", "value": "2" },
			    	          { "type": "text", "name": "grp_tag_3", "value": "3", "validate": { "required": true } }
			    	        ],
			    	        "validate": { "required": true }
			    	      }
			    	    },
				        {
			    	      "C0": "pickbox:",
			    	      "C1": { "type": "pickbox", "name": "pickbox", "value": "write something...",
			    	      		"picker": { "type":"dialog", "template": "std", "title": "这个那个", "src": "webapp/demo.json.php?act=pick&value=$value", "width": 600, "height": 400 } },
				          "C2": "period:",
				          "C3": { "type": "period", "id": "period", "title": "有效期限",
				            "begin": { "type": "date", "name": "pr_start", "value": "", "format": "yyyy-mm", "validate": { "required": true } },
				            "end": { "type": "date", "name": "pr_end", "value": "", "format": "yyyy-mm" }
				          }
			    	    },
				        {
			    	      "C0": "ueditor:",
			    	      "C1": { "colspan": 3, "node": { "type": "text", "name": "ueditor", "value": "write something...全能 军团", "initialFrameHeight": 120, "maximumWords": 0 } }
			    	    }
			    	  ]
					},
		    		{ "type" : "vert",
		    		  "height": "80",
		    		  "nodes" : [
		    		    { "type": "fieldset", "id": "fieldset", "legend": "", "box": { "name": "fs_box", "value": "", "text": "什么情况" }, "nodes": [
		    		      { "type": "view", "id": "fs_view", "node": { "type": "text", "name": "fs_text", "value": "xx" } }
		    		    ] }
			    	  ]
			    	},
			    	{ "type" : "horz", "height": 30, "nodes": [
		    		  { "type" : "nav", "width": "*", "pub": { "cls": "nav-button", "height": 28 }, "nodes" : [
		    			{ "type" : "button", "text" : "path1",
		    			  "on": { "click": "alert(this.x.text)" }
		    			},
		    			{ "type" : "button", "text" : "path2", 
		    			  "on": { "click": "alert(this.x.text)" }
		    			},
		    			{ "type" : "button", "text" : "path3", 
		    			  "on": { "click": "alert(this.x.text)" }
		    			}
		    		  ] },
		    		  { "type" : "buttonbar", "width": "*", "pub": { "cls": "f-button-path", "height": 28 }, "nodes" : [
		    			{ "type" : "button", "text" : "path1",
		    			  "on": { "click": "alert(this.x.text)" },
		    			  "nodes": [ { "type": "button", "text": "小按钮1" } ]
		    			},
		    			{ "type" : "button", "text" : "path2", 
		    			  "on": { "click": "alert(this.x.text)" },
		    			  "nodes": [ { "type" : "button", "text" : "小按钮2" } ]
		    			},
		    			{ "type" : "button", "text" : "path3", 
		    			  "on": { "click": "alert(this.x.text)" }
		    			}
		    		  ] },
		    		  { "type" : "buttonbar", "width": "*", "nodes" : [
		    			{ "type" : "button", "text" : "红果果" },
		    			{ "type" : "split" },
		    			{ "type" : "button", "text" : "小按钮" },
		    			{ "type" : "split" },
		    			{ "type" : "button", "text" : "按钮小", "on" : { "click" : "alert(this.x.text)" }, "nodes": [ { "icon" : "core/ui/g/folder.png", "text" : "紫菜" } ] }
		    		  ] }
		    		] },
		    		{ "type" : "buttonbar", "id": "main-btnbar", "height": 40, "space": 8, "align": "center", "style": "background:#f5f5f5", "pub": { "cls": "f-button" }, "nodes" : [
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "  js  ",     "on" : { "click" : "this.cmd('js')" } },
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "ajax",   "on" : { "click" : "VM(this).cmd('ajax-js')" } },
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "dialog", "on" : { "click" : "VM(this).exec('cmd-dialog',[5],{snap:this,snaptype:'tb'})" } },
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "before", "on" : { "click" : "VM(this).cmd('before')" } },
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "replace","on" : { "click" : "VM(this).cmd('replace')" } },
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "group",  "on" : { "click" : "VM(this).cmd('group')" } },
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "小按钮", "nodes": [
		    			  { "type" : "button", "icon" : "core/ui/g/folder.png", "text" : "选项一" },
		    			  { "type" : "split" },
		    			  { "type" : "button", "icon1" : "core/ui/g/folder.png", "text" : "选项二", "nodes": [
		    			    { "type" : "button", "icon" : "core/ui/g/folder.png", "text" : "二级紫菜", "id": "ejzc", "on": { "click": "this.attr('text','新鲜紫菜')" } },
		    		        { "type" : "button", "icon" : "core/ui/g/folder.png", "text" : "二级青菜", "on": { "click": "this.cmd('ajax-js')" } }
		    			  ] }
		    			] },
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "按小钮", "on" : { "click" : "alert(this.x.text)" }, "more" : {
		    			    "type": "dialog",
		    				"template": "prong",
		    			    "width": "500",
		    			    "pophide": true,
		    			    "id": "bravesound",
		    			    "src": "webapp/demo.json.php?act=panel-html",
		    			    "on1": { "load": "alert('dialog load: '+this.contentView.path)" },
		    			    "node1" : { "type" : "html", "text" : "对话框的话很多" }
		    			  }
		    			},
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "弹出对话框", "on" : {
		    				"click" : "this.cmd('submit')"
		    			  }
		    			},
		    			{ "type" : "button", "cls": "f-button", wmin:10, "text" : "taeyeon", "on" : { "click" : "eval(VM(this).f('textarea').val())" } },
		    			{ "type" : "submitbutton", "cls": "f-button", wmin:2,  "text" : "确定", "icon": "%img%/file.png", "on" : { "click" : "this.cmd('submit')" }, "more": {
		    			  "type": "menu",
		    			  "nodes" : [ { "type" : "button", "icon" : "core/ui/g/folder.png", "text" : "子选项" } ] }
		    			}
		    		]},
		    		{ "type" : "buttonbar", "id": "main-grpbar", "height": 50, "align": "center", "cls": "groupbar", "focusable": true, pub: { "cls": "f-button" }, "nodes" : [
		    			{ "type" : "button", "icon1" : "core/ui/g/folder.png", wmin:1, "text" : "insertRow", "on" : { "click" : "this.cmd('insertRow')" } },
		    			{ "type" : "button", "icon1" : "core/ui/g/folder.png", wmin:1, "text" : "updateRow", "on" : { "click" : "this.cmd('updateRow')" } },
		    			{ "type" : "button", "icon1" : "core/ui/g/folder.png", wmin:1, "text" : "moveRow",   "on" : { "click" : "VM('/index').find('c_grid').row({C0:'5-0'}).move('+=1');" } },
		    			{ "type" : "button", "icon1" : "core/ui/g/folder.png", wmin:1, "text" : "deleteRow", "on" : { "click" : "alert(this.x.text)" }, "more" : {
		    			  "type": "menu",
		    			  "nodes" : [ { "icon" : "core/ui/g/folder.png", "text" : "子选项" } ] }
		    			}
		    		]},
		    		{ "type" : "buttonbar",  "height": 50, "cls": "f-tab-bar z-horz-2", "focusable": true, pub: { "cls": "f-tab" }, "nodes" : [
		    			{ "type" : "button", "icon" : "core/ui/g/folder.png", "text" : "小塔布",  "width" : 100, "wmin": 1, "target": "1st", "focus": true },
		    			{ "type" : "button", "icon1" : "core/ui/g/folder.png", "text" : "塔小布", "width" : 100, "wmin": 1, "target": "2nd" },
		    			{ "type" : "button", "icon1" : "core/ui/g/folder.png", "text" : "塔布小", "width" : 100, "wmin": 1, "target": "3rd", "on" : { "click" : "void(0)" }, "nodes" : [ { "type" : "button", "icon" : "core/ui/g/folder.png", "text" : "子选项" } ] }
		    		]},
		    		{ "type" : "film", "dft": "1st", "animate": "scrollX", "height": 100, "nodes" : [
		    			{ "type" : "html", "id": "1st", "src": "webapp/demo.json.php?act=panel-html", "style": "background:LightCyan", "text" : "1st label <var style='width:100px;height:22px;border:1px solid red;' contenteditable></var>" },
		    			{ "type" : "vert", "id": "2nd", "scroll": true, "nodes": [
		    			  { "type" : "html", "style": "background:pink", "text" : "<div>2nd label</div>" },
		    			  { "type": "date", "name": "date2", "value": "2015-05-03", "format": "yyyy-mm-dd" }
		    			] },
		    			{ "type" : "view", "id": "3rd", "src": "webapp/demo.json.php?act=panel-html", "style": "background:LightGoldenrodYellow", "text" : "3rd label " }
		    		]}
		    	]
		    }
			
		]
	}
}

<? } ?>