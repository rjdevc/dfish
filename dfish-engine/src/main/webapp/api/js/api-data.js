/* data.js */

define( {
  "$": {
  	title: 'dfish ($)',
  	remark: 'dfish是全局静态变量，"$"是别名。',
    Properties: [
      /*{ name: '$.br', remark: '浏览器信息', common: true, param:[
        { name: '$.br.ie', type: 'Boolean', remark: '是否ie浏览器(包含ie10及以下，不包括ie11及以上。)', common: true },
        { name: '$.br.ms', type: 'Boolean', remark: '是否微软的浏览器(包含ie5到ie11，edge)', common: true },
        { name: '$.br.css3', type: 'Boolean', remark: '浏览器是否支持css3(ie8及以下浏览器不支持css3；ie9+、chrome等支持css3)', common: true }
      ] },*/
      { name: '$.globals', id: '$.globals', remark: '设置了 gid 参数的 widget 实例都存放在这里。可以通过 $.globals[ gid ] 来获取该实例。', common: true },
      { name: '$.x', id: '$.x', remark: '经 $.config() 方法设置的参数对象。', common: true, example: [
          function() {
            dfish.config( { path: '/myPath/', lib: 'dfish/' } );
            alert( $.x.path ); // 显示 "myPath"
          }
      ] }
    ],
    Methods: [
      { name: '$.ajax(url, [onsuccess], [context], [sync], [data], [onerror], [dataType])', remark: '发送ajax请求。返回的数据类型是字符串。', common: true, param: [
        { name: 'url', type: 'String', remark: '发送请求的URL字符串' },
        { name: 'onsuccess(data)', type: 'Function', remark: '请求成功后的回调函数' },
        { name: 'context', type: 'Object', remark: '用于设置Ajax相关回调函数的上下文' },
        { name: 'sync', type: 'Boolean', remark: '(默认:false)是否同步' },
        { name: 'data', type: 'Object | String', remark: '发送到服务器的数据' },
        { name: 'onerror(XMLHttpRequest)',type: 'Function', remark: 'ajax请求发生错误时调用的函数' },
        { name: 'dataType', type: 'String', remark: '预期服务器返回的数据类型。可选值: <b>text</b>, <b>xml</b>, <b>json</b>' }
      ], example: [
          function() {
            $.ajaxJSON( "data.sp?act=text", function( data ) {
              alert( typeof data ); // 显示"string"
            } );
          }
      ] },
      { name: '$.ajaxJSON(url, [onsuccess], [context], [sync], [data], [onerror])', remark: '$.ajax()的衍生方法，返回的数据类型是JSON对象', common: true, example: [
          function() {
            $.ajaxJSON( "data.sp?act=json", function( data ) {
              alert( typeof data ); // 显示"object"
            } );
          }
      ] },
      { name: '$.ajaxXML(url, [onsuccess], [context], [sync], [data], [onerror])', remark: '$.ajax()的衍生方法，返回的数据是XML节点。', common: true, example: [
          function() {
            $.ajaxJSON( "data.sp?act=xml", function( data ) {
              alert( data.xml ); // 显示xml的内容
            } );
          }
      ] },
      { name: '$.append(elem, content)', remark: '在元素内部追加内容', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'content', type: 'String', remark: 'html内容' }
      ], example: [
          function() {
            $.append( $( 'myDiv' ), '<a href=#>新增链接</a>' );
          }
      ] },
      { name: '$.after(elem, content)', remark: '在元素之后插入内容', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'content', type: 'String', remark: 'html内容' }
      ] },
      { name: '$.alert(text, [position], [timeout], [id])', remark: '弹出一个信息窗口。', common: true, param: [
        { name: 'text', type: 'String', remark: '信息内容。' },
        { name: 'position', type: 'String', remark: '弹出位置。可选值: <b>tl</b> <b>tr</b> <b>rt</b> <b>rb</b> <b>br</b> <b>bl</b> <b>lb</b> <b>lt</b> <b>t</b> <b>r</b> <b>b</b> <b>l</b> <b>c</b>' +
           '<br>备注：t:top, r:right, b:bottom, l:left, c:center'
       	},
        /*{ name: 'position', type: 'String', remark: '弹出位置。可选值: <b>topleft</b> <b>topright</b> <b>righttop</b> <b>rightbottom</b> <b>bottomright</b> <b>bottomleft</b> <b>leftbottom</b> <b>lefttop</b> <b>top</b> <b>right</b> <b>bottom</b> <b>left</b> <b>center</b>' +
           '<br>备注：以上参数可以分别简写为 <b>tl</b> <b>tr</b> <b>rt</b> <b>rb</b> <b>br</b> <b>bl</b> <b>lb</b> <b>lt</b> <b>t</b> <b>r</b> <b>b</b> <b>l</b> <b>c</b>'
       	},*/
        { name: 'timeout', type: 'Number', remark: '定时关闭，单位:毫秒。' },
        { name: 'id', type: 'String', remark: '弹窗的ID。' }
      ] },
      { name: '$.confirm(text, yes, [no])', remark: '弹出一个确认提示窗口。', common: true, param: [
        { name: 'text', type: 'String', remark: '信息内容。' },
        { name: 'yes', type: 'Function', remark: '点击"确定"执行的回调函数。' },
        { name: 'no', type: 'Function', remark: '点击"取消"执行的回调函数。', optional: true }
      ] },
      { name: '$.bcr(elem)', id: '$.bcr', remark: '获取元素在当前视窗内的相对位置。返回的对象包含6个整型属性：top、left、right、bottom、width、height，以像素计。此方法只对可见元素有效。', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' }
      ], example: [
          function() {
            var r = $.bcr( $( 'myDiv' ) );
            alert( 'top:' + r.top + '\nleft:' + r.left );
          }
      ] },
      { name: '$.before(elem, content)', remark: '在元素之前插入内容', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'content', type: 'String', remark: 'html内容' }
      ] },
      { name: '$.classAdd(elem, class)', id: '$.classAdd', remark: '给元素添加指定的类名', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'class', type: 'String', remark: 'CSS类名。多个用空格隔开' }
      ], example: [
          function() {
            $.classAdd( $( 'myDiv' ), 'bg-red' );
          }
      ] },
      { name: '$.classAny(elem, class)', remark: '元素是否包含指定的类名', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'class', type: 'String', remark: 'CSS类名。多个用空格隔开' }
      ], example: [
          function() {
            if ( $.classAny( $( 'myDiv' ), 'bg-red' ) ) {
              alert( '包含样式' );
            }
          }
      ] },
      { name: '$.classRemove(elem, class)', remark: '删除元素指定的类名', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'class', type: 'String', remark: 'CSS类名。多个用空格隔开' }
      ] },
      { name: '$.config(settings)', remark: '设置环境参数。设置好的配置参数可以从 $.x 获取。', common: true, param: [
        { name: 'settings', type: 'Object', remark: '配置参数', param: [
          { name: 'alias', type: 'Object', remark: '新增的模块可在此注册别名' },
          { name: 'ajaxData', type: 'Object', remark: '以POST方式发送到服务器的数据。格式为 key:value。' },
          { name: 'ajaxError', type: 'Function | Boolean', remark: '如果设为false，不提示任何ajax信息。<br>如果设为function，则作为处理错误信息的方法。该方法接收一个参数，ajax实例。' },
          { name: 'ajaxFilter', type: 'Function', remark: '对命令或widget的src返回的数据进行处理，并返回处理后的数据。该方法接收两个参数，第一个是返回数据，第二个是ajax实例。' },
          { name: 'autoPlaceholder', type: 'Boolean', remark: '如果设为true，表单将自动填充placeholder。' },
          { name: 'cnBytes', type: 'Number', remark: '一个汉字算几个字节。默认值为2。' },
          { name: 'debug', type: 'Boolean', remark: '开启调试模式。调试模式下按"Ctrl+鼠标右键"可查看view的信息' },
          { name: 'defaultOptions', type: 'Object', remark: '每个 widget 类都可以定义默认样式，以 widget type 作为 key' },
          { name: 'inputDetect', type: 'Object', remark: '设置表单在键入文本时是否即时检测。', param: [
            { name: 'maxLength', type: 'Boolean', remark: '设置为true，键入文本时将会即时检测是否超出最大长度。' }
          ] },
          { name: 'lib', type: 'String', remark: 'dfish包的路径，必选项。' },
          { name: 'lang', type: 'String', remark: '语言。可选项:zh_CN,zh_TW,en' },
          { name: 'noConflict', type: 'Boolean', remark: '设置为true，将变量$的控制权让渡给第一个实现它的那个库。' },
          { name: 'path', type: 'String', remark: '工程项目的路径。必选项。' },
          { name: 'preloadDir', type: 'String', remark: '预装载模板目录。' },
          //{ name: 'server', type: 'String', remark: '服务器地址的绝对路径。当执行 ajax 命令等交互操作时会使用此路径。<br>注：如果 ajax 命令的 src 参数以 ./ 开头，将不会使用 server 参数，而是访问本地地址。', mobile: true },
          { name: 'skin', type: 'Object', remark: '配置皮肤样式。', param: [
            { name: 'dir', type: 'String', remark: '皮肤目录' },
            { name: 'theme', type: 'String', remark: '主题名。在皮肤目录下应有一个和主题名相同的目录，该目录里面有一个 "主题名.css"' },
            { name: 'color', type: 'String', remark: '颜色名。在主题目录下应有一个和颜色名相同的目录，该目录里面有一个 "颜色名.css"' }
          ] },
          { name: 'supportUrl', type: 'String', remark: '显示支持与帮助的页面URL。如果配置此参数，系统所需的软件下载等都将指向这个地址。' },
          { name: 'templateDir', type: 'String', remark: '模板目录。' },
          { name: 'validate', type: 'Object', remark: '表单验证选项。', param: [
            { name: 'effect', type: 'String', remark: '表单验证效果。可选项: "red"(表单边框变成红色)；"alert"(弹出提示框)；"red,alert"(边框变红并弹出提示)' },
            { name: 'method', type: 'Function', remark: '表单验证的回调函数。函数有一个参数，接收一个验证信息的数组。' }
          ] },
          { name: 'ver', type: 'String', remark: '版本号。这个参数将会附加在js和css的路径上，以避免更新后的浏览器缓存问题。' },
          { name: 'view', type: 'Object', remark: 'view的配置项。如果配置了此参数，将生成一个全屏view' },
          { name: 'viewResources', type: 'Array', remark: '设置view的依赖JS或CSS。以 view path 作为 key。当页面上生成这个 path 的 view 时，就会加载对应的JS或CSS。' }
        ] }
      ], example: [
          function() {
            dfish.config( {
              debug: true, // 开启调试模式
              lang: 'zh_CN',  // 语言包
              alias: { //自定义模块
              	'Ueditor': 'pl/ueditor1_4_3/ueditor.dfish.js' //百度编辑器
              },
              ajaxError: function( req, url ) { // 处理ajax错误信息的方法
              	alert( req.status );
              },
              // 给 alert 和 confirm 设置 btncls 的默认值。
              defaultOptions: {
              	'Alert': { buttonCls: 'x-btn' },
              	'Confirm': { buttonCls: 'x-btn' }
              },
              inputDetect: { // 表单即时检测
              	maxLength: true
              },
              skin: { // 皮肤样式设置
              	dir: 'css/',
              	theme: 'classic',
              	color: 'blue'
              },
              view: { // 生成一个全屏的view
              	id: 'index', // view的ID。可通过 VM( '/index' ) 来访问
              	src: 'm/index/index.json' // view的src
              },
              viewResources: { // view的依赖JS模块
              	'/index' : [ './m/pub.js', './m/index.js' ],
              	'/index/task' : './m/task/task.js'
              }
            } );
          }
      ] },
      { name: '$.cookie(name, [value], [expire], [path])', remark: '读/写cookie', common: true, param: [
        { name: 'name', type: 'String', remark: 'cookie名' },
        { name: 'value', type: 'String', remark: 'cookie名对应的值' },
        { name: 'expire', type: 'Number', remark: 'cookie的有效时间(单位:天)' },
        { name: 'path', type: 'String', remark: '指定cookie的域' }
      ], example: [
          function() {
          	$.cookie( 'name', 'John' ); // 写入cookie
          	$.cookie( 'name', 'John', 15 ); // 写入cookie，有效时间15天
          	var name = $.cookie( 'name' ); // 读取cookie
          	$.cookie( 'name', null ); // 删除cookie
          }
      ] },
      { name: '$.css(elem, name, value)', remark: '设置元素的样式', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'name', type: 'String | Object', remark: '样式属性名，或者是样式属性的对象。数字型的值可用 += 和 -= 来做额外附加。' },
        { name: 'value', type: 'String', remark: '样式属性值。数字型的值可用 += 和 -= 来做额外附加。' }
      ], example: [
          function() {
            //
          	$.css( $( 'oDiv' ), { 'width': 100, 'background': 'red' } ); // 设置div的宽度为100px，背景色为红色
          	$.css( $( 'oDiv' ), { 'height': '+=100' } ); // div的高度增加100px
          	$.css( $( 'oDiv' ), 'background-color', 'red' ); // 设置背景色
          }
      ] },
      { name: '$.data(key, [value])', remark: '读/写自定义的数据。', common: true, param: [
        { name: 'key', type: 'String', remark: '属性名。' },
        { name: 'value', type: 'String', remark: '属性值。', optional: true }
      ], example: [
          function() {
            $.data( 'myname', '123' ); // 写入数据
            var v = $.data( 'myname' );  // 读取数据
          }
      ] },
      { name: '$.dateAdd(date, type, value)', remark: '日期增减', common: true, param: [
        { name: 'date', type: 'Date', remark: '日期对象' },
        { name: 'type', type: 'String', remark: '要增加的日期类型，可选值：<b>y</b>(年) <b>m</b>(月) <b>d</b>(日) <b>h</b>(时) <b>i</b>(分) <b>s</b>(秒)' }
      ], example: [
          function() {
            var tomorrow = $.dateAdd( new Date(), 'd', 1 ); // 给当下的时间加一天
          }
      ] },
      { name: '$.dateFormat(date, format)', remark: '把日期对象格式化成字串', common: true, param: [
        { name: 'date', type: 'Date', remark: '时间对象' },
        { name: 'format', type: 'String', remark: '时间格式，可选值：<b>y</b>(年) <b>m</b>(月) <b>d</b>(日) <b>h</b>(时) <b>i</b>(分) <b>s</b>(秒)' }
      ], example: [
          function() {
            var f = $.dateFormat( new Date(), 'yyyy-mm-dd hh:ii:ss' );
          }
      ] },
      { name: '$.dateParse(date)', id: '$.dateFormat', remark: '把字符串格式的日期转为日期对象', common: true, param: [
        { name: 'date', type: 'String', remark: '时间' }
      ], example: [
          function() {
            var date = $.dateParse( '2015-11-12' );
          }
      ] },
      { name: '$.dialog(obj)', remark: '获取dialog对象', common: true, param: [
        { name: 'obj', type: 'String | HTMLElement | widget', remark: '发送请求的URL字符串' }
      ], example: [
          function() {
          	// 根据id获取dialog
            var d = VM().cmd( { type: "dialog", width: 500, height: 400, id: "myDialog" } );
            var e = $.dialog( "myDialog" );
            alert( d === e ); //显示true
          },
          function() {
          	// 点击按钮，关闭当前窗口
            return~
            { type: "button", text: "关闭", on: { click: "$.dialog(this).close()" } }
          }
      ] },
      { name: '$.download(url, [data])', remark: '下载文件。', common: true, param: [
        { name: 'url', type: 'String', remark: '下载的文件地址。' },
        { name: 'data', type: 'Object', remark: 'post方式发送到后台的数据。', optional: true }
      ] },
      { name: '$.draggable(target, [option])', remark: '设置对象为可拖拽。', param: [
      	{ name: 'target', type: 'Widget', remark: 'widget元素。' },
        { name: 'option', type: 'Object', remark: '拖拽参数。', optional: true, param: [
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true }
        ] }
      ] },
      { name: '$.droppable(target, [option])', remark: '设置对象为可放置。拖拽过程中，放置节点有状态样式 z-droppable。', param: [
      	{ name: 'target', type: 'Widget', remark: 'widget元素。' },
        { name: 'option', type: 'Object', remark: '放置参数。', optional: true, param: [
          { name: 'drop', type: 'Function(event, ui)', remark: '当一个可接受的 draggable 被放置在 droppable 上时触发。', optional: true, param: [
          	{ name: 'event', type: 'Event', remark: '放置事件。' },
          	{ name: 'ui', type: 'Object', remark: '辅助参数。', param: [
          		{ name: 'draggable', type: 'Widget', remark: '拖拽节点。' },
          		{ name: 'droppable', type: 'Widget', remark: '放置节点。' },
          		{ name: 'type', type: 'String', remark: '放置方式。可能的值："append","before","after"。' }
          	] }
          ] },
          { name: 'highlight', type: 'Boolean', remark: '设置为true时，当前放置节点高亮，周围被半透明蒙版遮住。当前节点增加状态样式 z-droppable-highlight。', optional: true },
          { name: 'isDisabled', type: 'Function(event, ui)', remark: '当函数返回true时，当前放置节点为禁用状态。', optional: true, param: [
          	{ name: 'event', type: 'Event', remark: '拖动事件。' },
          	{ name: 'ui', type: 'Object', remark: '辅助参数。', param: [
          		{ name: 'draggable', type: 'Widget', remark: '拖拽节点。' },
          		{ name: 'droppable', type: 'Widget', remark: '放置节点。' },
          		{ name: 'type', type: 'String', remark: '放置方式。可能的值："append"。' }
          	] }
          ] },
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true },
          { name: 'sort', type: 'Boolean', remark: '是否可排序。', optional: true }
       ] }
      ], example: [
          function() {
            // 设置树可拖拽也可放置
            $.draggable( vm.find( 'f_tree' ) );
            $.droppable( vm.find( 'f_tree' ), {
              drop: function( ev, ui ) {
                var u = 'move.sp?act=move&from=' + ui.draggable.x.id + '&to=' + ui.droppable.x.id + '&type=' + ui.type;
                alert(u);
              }
            } );
          }
      ] },
      { name: '$.each(arr, fn)', remark: '对数组进行循环处理。', common: true, param: [
        { name: 'arr', type: 'Array', remark: '发送请求的URL字符串' },
        { name: 'fn([item], [index], [arr])', type: 'Function | String', remark: '调用方法。如果传入的参数是String，那么有三个默认的变量名可用: v(值) i(索引) r(数组本身)' }
      ], example: [
          function() {
          	// 让数组每个元素的值+1
            var arr = [ 1, 2 ];
            $.each( arr, function( v, i, r ) {
              r[ i ] ++;
            } );
          },
          function() {
          	// 字符串方式的简便写法，效果与上例相同
            var arr = [ 1, 2 ];
            $.each( arr, 'r[i]++' );
          }
      ] },
      { name: '$.embedWindow([window])', remark: '获取窗口所属的 embedwindow widget。', common: true, param: [
      	{ name: 'window', type: 'Window', remark: '窗口对象', optional: true }
      ] },
      { name: '$.extend(target, obj1, [objN])', remark: '用一个或多个其他对象来扩展一个对象，返回被扩展的对象。如果对象本身已存在某个属性，那么这个属性的值将会保留，不会覆盖', common: true, param: [
        { name: 'target', type: 'Map', remark: '需要扩展的对象' },
        { name: 'obj1', type: 'Map', remark: '待合并到第一个对象的对象' },
        { name: 'objN', type: 'Map', remark: '待合并到第一个对象的对象' }
      ], example: [
          function() {
            var obj1 = $.extend( { name: 'John' }, { sex: 'male' } ); // 返回结果为 { name: 'John', sex: 'male' }
            var obj2 = $.extend( { name: 'John' }, { name: 'Bob', sex: 'male' } ); // 返回结果同上
          }
      ] },
      { name: '$.height()', remark: '获取浏览器可用的高度。', common: true, example: [
          function() {
            alert( 'document height:' + $.height() );
          }
      ] },
      { name: '$.inArray(arr, obj)', remark: '数组中是否存在某个对象。返回true/false', common: true, param: [
        { name: 'arr', type: 'Array', remark: '数组' },
        { name: 'obj', type: 'All', remark: '待查的对象' }
      ], example: [
          function() {
            if ( $.inArray( [ 'John', 'Bob' ], 'Bob' ) ) {
              alert( 'Bob已存在' );
            }
          }
      ] },
      { name: '$.init([settings])', remark: '初始化应用环境。', common: true, param: [
        { name: 'settings', type: 'Object', remark: '环境配置参数。详细参数请参考 $.config 方法。', optional: true }
      ] },
      { name: '$.isArray(arr, obj)', remark: '判断某个对象是否数组类型。返回true/false', common: true, param: [
        { name: 'obj', type: 'Map', remark: '待查的对象' }
      ], example: [
          function() {
            if ( $.isArray( [ 0, 1 ] ) ) {
              alert( '是数组' );
            }
          }
      ] },
      { name: '$.jsonClone(obj)', remark: '复制一个json对象。', common: true, param: [
        { name: 'obj', type: 'json', remark: '待复制的对象' }
      ], example: [
          function() {
            var obj = $.jsonClone( { name: 'John' } ); //复制一个json
          }
      ] },
      { name: '$.jsonParse(text)', remark: '把json格式的字符串解析为一个json对象。', common: true, param: [
        { name: 'text', type: 'String', remark: 'json格式的字符串' }
      ], example: [
          function() {
            var obj = $.jsonParse( '{"name":"John"}' ); //返回一个json对象
          }
      ] },
      { name: '$.jsonString(value, [replacer], [space])', remark: '把一个json对象转为字符串。', common: true, param: [
        { name: 'value', type: 'Object', remark: 'json对象。' },
        { name: 'replacer', type: 'Function | Array', remark: '用于转换结果的函数或数组。<br>如果 replacer 为函数，则 $.jsonString 将调用该函数，并传入每个成员的键和值。使用返回值而不是原始值。如果此函数返回 undefined，则排除成员。根对象的键是一个空字符串：""。<br>如果 replacer 是一个数组，则仅转换该数组中具有键值的成员。成员的转换顺序与键在数组中的顺序一样。当 value 参数也为数组时，将忽略 replacer 数组。', optional: true },
        { name: 'space', type: 'Number | String', remark: '文本添加缩进、空格和换行符，如果 space 是一个数字，则返回值文本在每个级别缩进指定数目的空格，如果 space 大于 10，则文本缩进 10 个空格。space 也可以使用非数字，如：\\t。', optional: true }
      ], example: [
          function() {
          	// 把JSON对象转为字符串
            var str = $.jsonString( { name: 'John' } );
          }
      ] },
      { name: '$.loadCss(url)', remark: '装载CSS文件。', common: true, param: [
        { name: 'url', type: 'String', remark: 'CSS文件路径。' }
      ] },
      { name: '$.map(arr, fn)', remark: '将一个数组中的元素转换到另一个数组中。返回一个新的数组，原有的数组不会改变。', common: true, param: [
        { name: 'arr', type: 'Array', remark: '数组' },
        { name: 'fn([item], [index], [arr])', type: 'Function | String', remark: '转换函数。如果此参数是String，那么有三个默认的变量名可用: v(值) i(索引) r(数组本身)' }
      ], example: [
          function() {
          	//让每个元素的值+1后返回给新的数组
            var arr1 = $.map( [ 1, 2 ], function( v, i, r ) {
              return v ++;
            } );
            var arr2 = $.map( [ 1, 2 ], 'v++' ); //arr1和arr2的内容相同
         }
      ] },
      { name: '$.noConflict()', remark: '如果调用了本方法，将取消 $ VM Q 等全局变量，只留一个全局变量"dfish"。以便和其他框架共存。', common: true },
      { name: '$.numAdd(n1, n2)', remark: '获取两个浮点数字相加的和。', common: true, param: [
        { name: 'n1', type: 'Number', remark: '数字' },
        { name: 'n2', type: 'Number', remark: '数字' }
      ] },
      { name: '$.numFormat(number, [length], [separator], [rightward])', remark: '格式化数字。', common: true, param: [
        { name: 'number', type: 'Number', remark: '数字' },
        { name: 'length', type: 'Number', optional: true, remark: '分隔长度。默认值为 3' },
        { name: 'separator', type: 'String', optional: true, remark: '分隔符。默认值为 ","' },
        { name: 'rightward', type: 'Boolean', optional: true, remark: '从左向右的方向来分隔。默认值为 false' }
      ], example: [
          function() {
            var n = $.numFormat( 1234 ); // 返回 "1,234"
          }
      ] },
      { name: '$.prepend(elem, content)', id: '$.prepend', remark: '在元素内部前置内容。', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'content', type: 'String', remark: 'html内容' }
      ] },
      { name: '$.previewImage(imgsrc, [originalsrc])', id: '$.previewImage', remark: '预览图片。', common: true, param: [
        { name: 'imgsrc', type: 'String', remark: '图片地址。' },
        { name: 'originalsrc', type: 'String', remark: '原图地址。如果配置了此参数，将在预览窗口上生成一个"查看原图"的链接。' }
      ] },
      { name: '$.print(target, [opt])', id: '$.print', remark: '打印目标对象的内容。', common: true, param: [
        { name: 'target', type: 'Widget | HTMLElement', remark: 'widget对象，或者HTML元素对象。' },
        { name: 'opt', type: 'Boolean | Object', remark: '设置为true，立即执行打印。', optional: true, param: [
          { name: 'print', type: 'Boolean', remark: '是否立即打印。' },
          { name: 'head', type: 'String', remark: 'head标签内容。' },
          { name: 'input2text', type: 'Boolean', remark: '是否把表单转为文本显示。' }
        ] }
      ], example: [
          function() {
          	//打印id=content的widget内容，设置打印字体为36px，表单转为文本显示
            $.print( VM(this).find('content'), { print: true, head: "<style>body{font-size:36px}</style>", input2text: true } );
          }
      ] },
      { name: '$.proxy(context, fn)', remark: '返回一个新函数，并且为这个函数指定一个特定的作用域对象。', common: true, param: [
        { name: 'context', type: 'HTMLElement', remark: '一个object，函数的作用域会被设置到这个object上来' },
        { name: 'fn', type: 'Function | String', remark: '将要被改变作用域的函数。如果是字符串，那么它应该是前一个参数 "context" 对象的方法名' }
      ], example: [
          function() {
            var product = { type: 'cloth' };
            var f1 = function() { alert( this.type ) };
            var f2 = $.proxy( product, f1 );
            f1(); // 显示 "undefined"
            f2(); // 显示 "cloth"
          }
      ] },
      { name: '$.query(selector, [context])', remark: 'jQuery的别名。当调用了 dfish.noConflict() 而不能使用 Q 方法时，仍可通过 dfish.query 来使用 jQuery。', common: true, param: [
        { name: 'selector', type: 'String', remark: 'CSS选择器' },
        { name: 'context', type: 'String', remark: '作为待查找的 DOM 元素集、文档或 jQuery 对象' }
      ], example: [
          function() {
            $.query( 'p' ).css( 'color', 'red' ); //设置所有p标签的字体色为红色
            Q( 'p' ).css( 'color', 'red' ); //和上一条语句效果相同
          }
      ] },
      { name: '$.ready(fn)', remark: '当页面DOM载入就绪可以查询及操纵时绑定一个要执行的函数。一般用这个方法来取代 window.onload 事件。', common: true, param: [
        { name: 'fn', type: 'Function', remark: '在DOM就绪时执行的函数' }
      ], example: [
          function() {
            $.ready( function() {
              // 在这里写你的代码...
            } );
          }
      ] },
      { name: '$.remove(elem)', remark: '删除一个元素。', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: '要删除的元素' }
      ], example: [
          function() {
            $.remove( $( 'myDiv' ) );
          }
      ] },
      { name: '$.replace(elem, content)', remark: '把元素替换成指定的HTML或DOM元素。', common: true, param: [
        { name: 'elem', type: 'String | HTMLElement', remark: '将要替换成的内容' }
      ], example: [
          function() {
            $.replace( $( 'myDiv' ), '<p>新内容</p>' ); // 把一个DOM元素替换成标签
          }
      ] },
      { name: '$.script(src)', remark: '在全局环境内装载JS。', common: true, param: [
        { name: 'src', type: 'String', remark: 'JS文件的路径。' },
      ] },
      { name: '$.scrollIntoView(elem, [top], [left])', remark: '元素滚动到可视范围。', common: true, param: [
        { name: 'elem', type: 'HTMLElement | Widget', remark: 'HTML 元素或 widget 对象。' },
        { name: 'top', type: 'String', remark: '纵向位置。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>', optional: true },
        { name: 'left', type: 'String', remark: '横向位置。可选值: <b>left</b>, <b>center</b>, <b>right</b>', optional: true }
      ], example: [
          function() {
            $.replace( $( 'myDiv' ), '<p>新内容</p>' ); // 把一个DOM元素替换成标签
          }
      ] },
      { name: '$.skin(option)', remark: '更换皮肤。使用此方法要使用符合dfish皮肤标准的css文件结构。', common: true, param: [
        { name: 'option', type: 'Map', remark: '皮肤属性的json对象', param: [
          { name: 'dir', type: 'String', remark: '皮肤目录' },
          { name: 'theme', type: 'String', remark: '主题名。在皮肤目录下应有一个和主题名相同的目录，该目录里面有一个 "主题名.css"' },
          { name: 'color', type: 'String', remark: '颜色名。在主题目录下应有一个和颜色名相同的目录，该目录里面有一个 "颜色名.css"' }
        ] }
      ], example: [
          function() {
          	// 更换皮肤css。执行此方法后，系统将调用 "css/classic/classic.css" 和 "css/classic/blue/blue.css"
            $.skin( {
			  dir: 'css/',
			  theme: 'classic',
			  color: 'blue'
			} );
          }
      ] },
      { name: '$.splice(arr, item)', remark: '从数组中删除某一项。', common: true, param: [
        { name: 'arr', type: 'Array', remark: '数组' },
        { name: 'item', type: 'Object', remark: '数组中的某一项' }
      ], example: [
          function() {
            var ar = [ 'a', 'b' ];
            $.splice( ar, 'b' ); // 删除 'b' 这一项
            alert( ar.length ); // 显示 "1"
          }
      ] },
      { name: '$.stop(event)', remark: '阻止事件并取消事件冒泡。', common: true, param: [
        { name: 'event', type: 'Event', remark: '事件对象' }
      ], example: [
          function() {
          	// 一个点击无效的链接
            $( 'myDiv' ).innerHTML = '<a href="www.baidu.com" onclick="$.stop(event)"></a>';
          }
      ] },
      { name: '$.strEscape(html)', remark: '对html格式的字符串进行编码。&转为&amp;amp; <转为&amp;lt; >转为&amp;gt;', common: true, param: [
        { name: 'html', type: 'String', remark: 'html格式的字符串。' }
      ], example: [
          function() {
            var s = $.strEscape( '<a href=#>链接</a>' );
          }
      ] },
      { name: '$.strFrom(str, from, [last])', remark: '截取从某个/些字符开始的字符串。如果没有匹配到则返回空字符串。', common: true, param: [
        { name: 'str', type: 'String', remark: '要截取的字符串。' },
        { name: 'from', type: 'String', remark: '开始的字符。' },
        { name: 'last', type: 'Boolean', remark: '从最后一个匹配的字符开始。', optional: true }
      ], example: [
          function() {
            var s1 = $.strFrom( 'm/pub/test.js', '/' ); // 返回 "pub/test.js"
            var s2 = $.strFrom( 'm/pub/test.js', '/', true ); // 返回 "test.js"
          }
      ] },
      { name: '$.strHighlight(str, key, [matchLength], [keyCls])', remark: '给字串中的关键词加上高亮的样式标签。', common: true, param: [
        { name: 'str', type: 'String', remark: '字符串。' },
        { name: 'key', type: 'String', remark: '关键词。' },
        { name: 'matchLength', type: 'Number', optional: true, remark: '切词长度。' },
        { name: 'keyCls', type: 'String', optional: true, remark: '高亮的样式名。默认值为"f-keyword"。' }
      ] },
      { name: '$.strLen(str, [cnbyte])', remark: '获取字符串的字节长度。中文字符的字节数读取自 dfish 全局配置的 cnBytes 参数。如果没有设置此参数，默认算两个字符。', common: true, param: [
        { name: 'str', type: 'String', remark: '字符串。' },
        { name: 'cnbyte', type: 'Number', remark: '中文字符的字节数。默认值为2', optional: true }
      ] },
      { name: '$.strSlice(str, len, [ext])', remark: '把字符串按照字节数截取。中文字符的字节数读取自 dfish 全局配置的 cnBytes 参数。如果没有设置此参数，默认算两个字符。', common: true, param: [
        { name: 'str', type: 'String', remark: '要截取的字符串。' },
        { name: 'len', type: 'Number', remark: '要截取的长度。' },
        { name: 'ext', type: 'String', remark: '当字串超出长度时补充到最后的文本。', optional: true }
      ] },
      { name: '$.strTo(str, to, [last])', remark: '截取从开始到某个/些字符为止的字符串。如果没有匹配到则返回空字符串。', common: true, param: [
        { name: 'str', type: 'String', remark: '要截取的字符串。' },
        { name: 'to', type: 'String', remark: '到此截止的字符。' },
        { name: 'last', type: 'Boolean', remark: '是否截止最后一个匹配的字符。', optional: true }
      ], example: [
          function() {
            var s1 = $.strTo( 'm/pub/test.js', '/' ); // 返回 "m"
            var s2 = $.strTo( 'm/pub/test.js', '/', true ); // 返回 "m/pub"
          }
      ] },
      { name: '$.strTrim(str)', remark: '删除字符串前后空格。', common: true, param: [
        { name: 'str', type: 'String', remark: '要处理的字符串' }
      ], example: [
          function() {
            var s1 = $.strTrim( ' abc ' ); // 返回 "abc"
          }
      ] },
      { name: '$.strUnescape(str)', remark: '对经过 $.strEscape 编码的字符串进行解码。', common: true, param: [
        { name: 'str', type: 'String', remark: '要接吗的字符串' }
      ], example: [
          function() {
            var s = $.strUnescape( '&lt;a href=#&gt;链接&lt;/a&gt;' );
          }
      ] },
      { name: '$.template(id, content)', remark: '定义模板。', common: true, param: [
        { name: 'id', type: 'String', remark: '设置模板的ID。' },
        { name: 'content', type: 'Object', remark: '模板内容。' }
      ], example: [
          function() {
            $.template( 'index_view', {
            	type: 'view',
            	node: { type: 'html', text: 'hello world' }
            } );
          }
      ] },
      { name: '$.thumbnail(range, width, [opt])', remark: '把某个范围内的图片变成缩略图。', common: true, param: [
        { name: 'range', type: 'HtmlElement | Widget', remark: 'HTML元素或widget对象。' },
        { name: 'width', type: 'Number', remark: '图片最大宽度。' },
        { name: 'opt', type: 'String | Function | Boolean', remark: 'String: 弹出新窗的URL(支持$0,$1变量。$0是图片地址,$1是图片标题); Function: 回调函数; Boolean: 设为false，取消点击预览功能。' }
      ], example: [
          function() {
            $.thumbnail( vm.find('img').$(), 500 );
          }
      ] },
      { name: '$.urlDecode(url)', remark: '解码经过UTF-8编码的url。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串' }
      ], example: [
          function() {
            var url = $.urlDecode( '%26' ); // 返回 "&"
          }
      ] },
      { name: '$.urlEncode(url)', remark: '对url进行UTF-8编码。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串' }
      ], example: [
          function() {
            var url = $.urlEncode( '&' ); // 返回 "%26"
          }
      ] },
      { name: '$.urlFormat(url, data)', remark: '对url中的 $0, $xxx 等变量进行替换。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串。' },
        { name: 'data', type: 'Array | Object', remark: '用来替换的数据。如果 url 中的变量是 $0, $1...$n，那么本参数应该是 Array；如果url 中的变量是 $str，那么本参数应该是 Object。' }
      ], example: [
          function() {
            var url = $.urlFormat( 'a.sp?id=$0&v=$1', [ 'a', 'b' ] ); // 返回 "a.sp?id=a&v=b"
            var url = $.urlFormat( 'a.sp?id=$a&v=$b', { a: 1, b: 2 } ); // 返回 "a.sp?id=1&v=2"
          }
      ] },
      { name: '$.urlParam(url, [opt])', remark: '读/写 url 中的变量。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串。' },
        { name: 'opt', type: 'String | Object', remark: '如果是 String 类型，读取以 data 作为 key 的值。如果是 Object 类型，则写入 URL 变量。' }
      ], example: [
          function() {
          	// 获取url中某一项的值
            var id = $.urlParam( 'a.sp?id=1', 'id' ); // 返回 "1"
          },
          function() {
          	// 获取url中 # 号后面的部分
            var hash = $.urlParam( 'a.sp?id=1#cms', '#' ); // 返回 "cms"
          },
          function() {
          	// 获取url所有参数
            var para = $.urlParam( 'a.sp?id=1&page=2' ); // 返回JSON对象 { id: 1, page: 2 }
          },
          function() {
          	// 设置url参数
            var url = $.urlParam( 'a.sp?id=1', { id: 2, page: 3 } ); // 返回 "a.sp?id=2&page=3"
          }
      ] },
      { name: '$.use(url)', remark: '装载模块。模块js是在CMD模块规范的封闭环境内运行，运行环境说明参见"<a href=javascript:>模块加载</a>"', common: true, param: [
        { name: 'url', type: 'String | Array', remark: '模块名称/地址' }
      ], example: [
          function() {
          	// 范例1：完成配置后，装载业务模块
            dfish.config( { path: '/itask7/', lib: 'dfish/' } );
            dfish.use( './m/app.js' );
          }
      ] },
      { name: '$.vm(obj)', remark: '获取某个对象所属的view。和 VM 方法相同。', common: true, param: [
        { name: 'obj', type: 'htmlElement | widget | path', remark: 'html元素对象、widget对象、或路径字符串' }
      ], example: [
          function() {
            // 以下两条语句结果相同
            var vm1 = $.vm( '/index' );
            var vm2 = VM( '/index' );
          }
      ] },
      { name: '$.widget(elem)', remark: '获取某个元素对象所属的widget。"', common: true, param: [
        { name: 'elem', type: 'htmlElement', remark: 'html元素对象' }
      ], example: [
          function() {
          	// 点击这个 html widget 里的链接，更换内容为123
          	var opt = { type: 'html', text: '<a href="javascript:" onclick="$.widget(this).text(123)">click</a>' }
          }
      ] },
      { name: '$.width()', remark: '获取浏览器可用的宽度。', common: true, example: [
          function() {
            alert( 'document width:' + $.width() );
          }
      ] },
      { name: '$.xmlParse(content)', remark: '把字符串转为xml对象', common: true, param: [
        { name: 'content', type: 'String', remark: '字符串形式的xml' }
      ], example: [
          function() {
            var x = $.xmlParse( '<doc>123</doc>' ); // 返回一个xml节点对象
          }
      ] },
      { name: '$.xmlQuery(url)', remark: '根据查询语句获取匹配到的第一个节点。返回一个xml节点对象。', common: true, param: [
        { name: 'expr', type: 'String', remark: '符合xpath语法的查询语句' }
      ], example: [
          function() {
            var x = $.xmlQueryAll( xml, 'name' ); // 获取第一个标签名为name的子节点
          }
      ] },
      { name: '$.xmlQueryAll(expr)', remark: '根据查询语句获取匹配到的所有节点。返回一个数组。', common: true, param: [
        { name: 'expr', type: 'String', remark: '符合xpath语法的查询语句' }
      ], example: [
          function() {
            var xs = $.xmlQueryAll( xml, 'name' ); // 获取标签名为name的子节点
          }
      ] }
    ]
  },
  "Q": {
  	title: 'Q',
  	remark: 'Q 是一个全局方法，即 jQuery。本方法 和 $.query 方法等价。详细用法请参考 <a href=http://hemin.cn/jq/index.html target=_blank>jQuery文档</a>',
    Methods: [
      { name: 'Q(expr, [context])', remark: '获取一个jQuery对象。', param: [
        { name: 'expr', type: 'String | element', remark: '用于查找的表达式。' }
      ], example: [
          function() {
          	// 获取样式包含 abc 的所有元素
            var $q = Q( '.abc' );
          }
	  ] }
	] },
  "VM": {
  	title: 'VM',
  	remark: 'VM 是一个全局方法，获取对象所属的view。点击查看: <a href=javascript:; onclick=api.open("view");>view的方法与属性</a>。',
    Methods: [
      { name: 'VM(obj)', remark: '获取对象所属的view。', param: [
        { name: 'obj', type: 'String | widget | element', remark: '以 / 开头的路径，或是 widget 对象，或是 html element 元素。' }
      ], example: [
          function() {
          	// 根据路径获取 view
            var vm = VM( '/index' );
          }
	  ] }
	] },
  "CommonModuleDefinition": { //http://javascript.ruanyifeng.com/nodejs/module.html
  	title: '模块加载',
  	remark: 'DFISH3中的JS引入了CommonJS规范。CommonJS模块规范源于Node.js。根据这个规范，每个文件就是一个模块，有自己的作用域。在一个文件里面定义的变量、函数、类，都是私有的，对其他文件不可见。',
    Properties: [
      { name: 'exports', remark: '为了方便，CommonJS中为每个模块提供一个exports变量，指向module.exports。注意，不能直接将exports变量指向一个值。一般情况建议使用module.exports，不使用exports。', example: [
          function() {
            // 对外提供 foo 属性
            exports.foo = 'bar';
            // 对外提供 doSomething 方法
            exports.doSomething = function() {};
          }
      ] },
      { name: 'module', remark: 'CommonJS规范规定，每个模块内部，module变量代表当前模块。这个变量是一个对象，它的exports属性（即module.exports）是对外的接口。加载某个模块，其实是加载该模块的module.exports属性。', param: [
        { name: 'id', type: 'String', remark: '模块id。是当前模块文件的绝对路径' },
        { name: 'path', type: 'String', remark: '模块所在的目录路径' },
        { name: 'exports', type: 'Object', remark: '当前模块对外提供的接口' }
      ], example: [
          function() {
          	// 下面代码通过module.exports输出变量x和函数addX。
            var x = 5;
            var addX = function (value) {
              return value + x;
            };
            module.exports.x = x;
            module.exports.addX = addX;
          },
          function() {
          	// 下面代码通过module.exports输出变量x
            var x = { a: 1 };
            module.exports = x;
          }
      ] }
    ],
    Methods: [
      { name: 'define([id], [deps], factory)', remark: '定义一个模块。', param: [
        { name: 'id', type: 'String', remark: '模块id。如果不写此参数，那么模块id默认为当前js文件的路径' },
        { name: 'deps', type: 'Array', remark: '模块依赖' },
        { name: 'factory', type: 'Object', remark: '定义当前模块。可以是一个函数，也可以是一个对象或字符串。' }
      ], example: [
          function() {
          	// factory 为对象、字符串时，表示模块的接口就是该对象、字符串。比如可以如下定义一个 JSON 数据模块:
            define( { "foo": "bar" } );
          },
          function() {
          	// factory 为函数时，表示是模块的构造方法。执行该构造方法，可以得到模块向外提供的接口。factory 方法在执行时，默认会传入三个参数：require、exports 和 module
            define( function(require, exports, module) {
              return { "foo": "bar" };
            } ); // 此例和上例结果相同
          },
          function() {
          	// 定义一个名为 hello 的模块
            define( 'hello', function(require, exports, module) {
              var foo = {};
              return foo;
            } );
          }
      ] },
      { name: 'define.widget([id], factory)', remark: 'widget是在dfish3里经过特别封装的模块类。使用此方法定义的widget，会默认继承Widget基础类的所有属性和方法。', param: [
        { name: 'id', type: 'String', remark: '模块id。如果不写此参数，那么模块id默认为当前js文件的路径' },
        { name: 'factory', type: 'Object', remark: 'widget类的参数', param: [
       	  { name: 'Const(settings, [parentNode])', type: 'Function', remark: '构造函数。第一个参数是widget的配置json对象，第二个参数是父节点对象' },
       	  { name: 'Extend', type: 'String | Array', remark: '要继承的widget类' },
       	  { name: 'Helper', type: 'Object', remark: '静态方法/属性' },
       	  { name: 'Listener', type: 'Object', remark: '事件监听' },
       	  { name: 'Prototype', type: 'Object', remark: '方法/属性' }
       	] }
      ], example: [
          function() {
          	// 定义一个circle类，它在页面上生成一个圆形，点击它时背景变成红色。内容如下
            var W = require( 'widget' );
            define.widget( 'circle', {
              Const: function( settings, parentNode ) {
                W.apply( this, arguments );
              },
              Listener: {
                click: function() {
                  this.$().style.background = 'red';
                }
              },
              Prototype: {
                // 覆盖 widget 基础类的输出接口
                html: function() {
                  return '<div ' + this.html_prop() + ' style="background:#000;border-radius:' + this.x.radius + 'px"></div>';
                }
              }
            } );
          }
      ] },
      { name: 'require(id, [fn])', remark: '加载模块。读入并执行一个JavaScript文件，然后返回该模块的exports对象。', param: [
        { name: 'id', type: 'String | Array', remark: '模块id或url。<br>如果是内置模块，如 require("view")，则返回该模块。<br>如果在全局配置项的alias里有设置模块名，则根据这个配置装载模块。<br>如果以 "./" 或者 "../" 或者 "/" 开头，则根据路径来装载模块。' },
        { name: 'fn', type: 'Function', remark: '模块装载完毕后的回调函数。当设置了此参数时，模块以异步方式装载。' }
      ], example: [
          function() {
          	// 在同一目录下有 a.js 和 b.js两个文件。a.js中定义了两个模块，在 b.js 中调用这两个模块。
            // a.js
            module.exports = { name: 'aa' };
          },
          function() {
            // b.js
            var a = require( './a' ); // 调用同一目录下的 a.js
            alert( a.name ); // 显示 "aa"
          }
      ] },
      { name: 'require.css(url)', remark: '加载CSS样式文件。', param: [
        { name: 'url', type: 'String', remark: 'CSS文件路径。如果是调用同一目录下的CSS，应使用 "./" 作为开头；如果是上级目录可使用"../"' }
      ], example: [
          function() {
          	// 在同一目录下有 a.js 和 b.js两个文件。a.js中定义了两个模块，在 b.js 中调用这两个模块。
            // a.js
            define( function() {
              return { name: "aa" }
            } );
            define( 'c', { name: "cc" } ); // 额外再定义一个模块，"c" 是模块别名
          },
          function() {
            // b.js
            var a = require( './a' ); // 调用同一目录下的 a.js
            alert( a.name ); // 显示 "aa"
            var c = require( 'c' );
            alert( c.name ); // 显示 "cc"
          }
      ] }
    ]
  },
  "app": {
  	title: 'JS/CSS规范',
  	remark: "<dl class=upgrade><dt>业务模块JS/CSS创建流程</dt><dd><ol type=1 class=remark-ul><li>新建一个业务JS的总目录(例如叫 \"m\")，所有业务JS都放在其中。" +
  		"<li>在 m 中创建一个定义全局模块变量的JS文件，例如 m/app.js，并在HTML页面的&lt;script&gt;中写上：" +
  		"<pre class=example-content><code>	&lt;script><br>	dfish.use(\"./m/app.js\");<br>	&lt;/script></code></pre>"+
  		"<li>一个模块创建一个目录。例如为首页模块创建一个 m/index/ 目录，并创建一个和目录名相同的JS文件，例如 m/index/index.js，这个JS中定义一个和文件名相同的变量:" +
  		"<pre class=example-content><code>	var index = {<br>	  ...<br>	};<br>	module.exports = index;</code></pre>"+
  		"<li>如果模块JS里需要定义子模块，例如首页模块，那么就在 m/index/ 里再新建子目录，和步骤3相同，然后" +
  		"<pre class=example-content><code>	var index = {<br>	  xxx: require(\"./xxx/xxx.js\"),<br>	  ...<br>	};<br>	module.exports = index;</code></pre>"+
  		"<li>如果需要定义模块CSS，那么创建 m/index/index.css。样式命名以 \".模块名-\" 开头，例如 <code class=note>.index-top{...}</code>。<br>引入模块CSS的方式:" +
  		"<pre class=example-content><code>	require.css(\"./index.css\");<br>	var index = {<br>	  ...<br>	};<br>	module.exports = index;</code></pre>"+
  		"<li>在 m/app.js 中引入 index 模块并注册全局变量，至此完成一个模块JS的定义和引用: " +
  		"<pre class=example-content><code>	window.index = require(\"./index/index.js\");</code></pre>"+
  		"</ol></dd></dl>" +
  		"<dl class=upgrade><dt>皮肤CSS</dt><dd><ul class=remark-ul><li><b>皮肤创建</b>：<br>创建一个主题目录，下设颜色子目录。" +
  		"<li><b>主题CSS文件命名</b>：<br>与主题目录名相同，放在主题根目录下。主题CSS文件内定义全局CSS样式。" +
  		"<li><b>颜色CSS文件命名</b>：<br>与颜色目录名相同，放在颜色子目录下。颜色CSS文件内定义颜色相关的CSS样式。" +
  		"</ul></dd></dl>"
  },
  "@": {
  	title: '模板',
  	sort: false,
  	remark: 'widget的template参数代表模板。<p>支持模板的widget优先实现顺序，以 view 为范例: <ol>' +
  		'<li>如果有node，就直接展示node。' + 
	 	'<li>有src，没有template。这个src应当返回有node(s)节点的JSON。(兼容3.1)' +
	 	'<li>有src，也有template，那么src应当返回JSON数据，用于template的内容填充。</ol></p>',
    Properties: [
      { name: '@propName', remark: '模板中的 widget 属性名称前面加 @ 符号，表示这是一个动态属性，对应的值是一个JS表达式，可以从数据源获取数据。', example: [
          function() {
          	/// 一个简单的模板使用范例，在页面上显示hello world。可以把本范例另存为html文件进行测试。
          	return''
            '<!doctype html>'
            '<html>'
            '<head>'
            '<meta charset=utf-8>'
            '<title>DFish3.2</title>'
            '<script src="dfish/dfish.js"></script>'
            '<script>'
            'dfish.init( {'
            '  view: {'
            '    id: "index",'
            '    src: {'
            '      data: { content: "hello world!" }'
            '    },'
            '    template: {'
            '      node: { type: "Html", "@text": "$data.content" }'
            '    }'
            '  }'
            '} );'
            '</script>'
            '</head>'
            '<body style="margin:0;overflow:hidden;" scroll="no"></body>'
            '</html>'
          },
          function() {
          	/// 把上述范例中的 src 和 template 参数改为字符串格式。使用 $.template() 方法定义模块。
          	return''
            '<script>'
            'dfish.init( {'
            '  view: {'
            '    id: "index",'
            '    src: "index.sp?act=index",'
            '    template: "index_view"'
            '  }'
            '} );'
            '$.template( "index_view", {'
            '    node: { type: "Html", "@text": "$data.content" }'
            '} );'
            '</script>'
          }
	  ] },
	  { name: '@w-for', remark: '循环输出一组节点。expr 语句使用 in 语法，如 <b>$item in $data</b>，或 <b>$item,$index in $data</b>。$data 可以是 Array 或 Object 类型。', example: [
          function() {
          	/// 使用模板的树
          	return~
            { type: 'tree', src: 'tree.sp', template: 'tmpl_tree' }
          },
          function() {
          	/// tree.sp 返回数据
          	return~
            { data: [
            	{ name: "张三" },
            	{ name: "李四" }
            ] }
          },
          function() {
            /// tmpl_tree 定义
          	return~
            define.template( 'tmpl_tree', {
              type: 'leaf', nodes: [
                { type: 'leaf', '@w-for': '$item in $data', '@text': '$item.name' }
              ]
            } );
          },
          function() {
            /// 上述模板解析结果为：
          	return~
            { type: 'leaf', nodes: [
              { type: 'leaf', text: '张三' },
              { type: 'leaf', text: '李四' }
            ] }
          }
	  ] },
	  { name: '@w-if', remark: '条件表达式。', example: [
          function() {
          	/// 数据源
          	return~
            { data: { flag: 1, name: "张三" } }
          },
          function() {
          	/// 范例1: 兄弟节点判断输出
          	return~
            {
              "type": "ButtonBar", "nodes": [
                { "type": "button", "@w-if": "$data.flag==1", "@text": "$data.name" },
                { "type": "button", "@w-else": "", "text": "else" },
              ]
            }
          },
          function() {
          	/// 范例1: 上述数据源+模板输出结果为：
          	return~
            {
              "type": "ButtonBar", "nodes": [
                { "type": "button", "@text": "张三" }
              ]
            }
          },
          function() {
          	/// 范例2: 单个节点判断输出
          	return~
            {
              "@w-if($data.flag==1)": { "type": "text", "name": "name", "@value": "$data.name" },
              "@w-else": { "type": "html", "text": "welcome" }
            }
          },
          function() {
          	/// 范例2: 上述数据源+模板输出结果为：
          	return~
            { "type": "text", "name": "name", "value": "张三" }
          }          
	  ] },
	  { name: '@w-elseif', remark: '条件表达式。必须搭配 @w-if 使用。' },
	  { name: '@w-else', remark: '条件表达式。必须搭配 @w-if 使用。' },
	  { name: '@w-include', remark: '引用模板。值是模板ID。', example: [
          function() {
          	/// 引用 id 为 tmpl_tree 的模板
          	return~
            { "@w-include": "tmpl_tree" }
          } 
      ] },
	  { name: '$dataKey', remark: 'dataKey是数据源第一层子节点的属性名，前面再加上$，作为表达式中可用的变量。', example: [
          function() {
          	/// 这个数据源可以获取 $data 和 $head 变量
          	return~
            {
              data: { name: "张三" },
              head: { limit: 15 }
            }
          }
      ] },
	  { name: '$this', remark: '特殊的数据变量名，代指数据源根节点。', example: [
          function() {
          	/// 数据源
          	return~
            { data: { content: 'hello world' } }
          },
          function() {
          	/// 在表达式中使用 $this
          	return~
            { type: "Html", "@text": "$this.data.content" }
          }
      ] }
  ] },
  "Widget": {
  	title: 'Widget基础类',
  	remark: '所有Widget都继承此类。',
    Config: [
      { name: 'afterContent', type: 'String', remark: '附加到之前的内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget的json对象)。', common: true },
      { name: 'beforeContent', type: 'String', remark: '附加到之后的内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget的json对象)。', common: true },
      { name: 'prependContent', type: 'String', remark: '附加到开头的内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget的json对象)。', common: true },
      { name: 'appendContent', type: 'String', remark: '附加到末尾的内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget的json对象)。', common: true },
      { name: 'cls', type: 'String', remark: '样式类名。', common: true },
      { name: 'data', type: 'Object', remark: '扩展数据。key:value键值对。在当前widget及子孙节点范围内的事件可以用变量 $key 的来获取值。', common: true },
      { name: 'display', type: 'Boolean', remark: '是否显示。', common: true },
      { name: 'gid', type: 'String', remark: '自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。', common: true },
      { name: 'height', type: 'Number | String', remark: '高度。可以用数字, *, 百分比。如果设置为 -1, 就是自适应高度。', common: true },
      { name: 'heightMinus', type: 'Number', remark: '如果设置了 cls 参数，并且 cls 里定义了 padding border margin 这三种样式中的至少一种 ，那么就需要手工设置 heightMinus 以减去因这些样式额外增加的高度。<br>注: 如果在 style 参数里设置了这三种样式，系统会自动分析，一般不需要额外设置 heightMinus。', common: true },
      { name: 'id', type: 'String', remark: '自定义的ID。可通过 view.find( id ) 方法来获取 widget。', common: true },
      { name: 'maxHeight', type: 'Number', remark: '最大高度。当 height 设置为 * 时可以使用本参数。', common: true },
      { name: 'maxWidth', type: 'Number', remark: '最大宽度。当 width 设置为 * 时可以使用本参数。', common: true },
      { name: 'minHeight', type: 'Number', remark: '最小高度。当 height 设置为 * 时可以使用本参数。', common: true },
      { name: 'minWidth', type: 'Number', remark: '最小宽度。当 width 设置为 * 时可以使用本参数。', common: true },
      { name: 'on', type: 'Object', remark: '事件。', common: true },
      { name: 'template', type: 'String', remark: '模板地址。', common: true },
      { name: 'style', type: 'String', remark: '样式。', common: true },
      { name: 'type', type: 'String', remark: '类型名称。', common: true },
      { name: 'width', type: 'Number | String', remark: '宽度。可以用数字, *, 百分比。如果设置为 -1, 就是自适应宽度。', common: true },
      { name: 'widthMinus', type: 'Number', remark: '如果设置了 cls 参数，并且 cls 里定义了 padding border margin 这三种样式中的至少一种 ，那么就需要手工设置 widthMinus 以减去因这些样式额外增加的宽度。<br>注: 如果在 style 参数里设置了这三种样式，系统会自动分析，一般不需要额外设置 widthMinus。', common: true }
    ],
    Properties: [
      { name: '0,1,...,n', type: 'Number', remark: '子节点序号。', common: true, example: [
          function() {
          	// 获取所有子节点
            var wg = vm.find( 'aaa' );
            for ( var i = 0; i < wg.length; i++ ) {
            	alert( wg[ i ].x.type );
            }
          }
      ] },
      { name: 'id', type: 'String', remark: 'widget对象的ID。这个ID由引擎自动生成。', common: true },
      { name: 'isWidget', type: 'Boolean', remark: '是否是一个widget对象。所有widget的这个属性都为 true。可用来简单区分widget对象和JSON对象。', common: true },
      { name: 'ownerView', type: 'View', remark: 'widget 所属的 view 对象。', common: true, example: [
          function() {
            wg.ownerView.reload(); // 让 widget 所在的 view 刷新
          }
      ] },
      { name: 'parentNode', type: 'Widget', remark: '父节点。', common: true },
      { name: 'nodeIndex', type: 'Number', remark: '节点序号。标识在兄弟节点中的排序位置。', common: true },
      { name: 'length', type: 'Number', remark: '子节点的个数。', common: true },	
      { name: 'x', type: 'Object', remark: 'widget的初始配置项对象。', common: true, example: [
          function() {
            var wg = vm.find( 'aaa' );
            alert( wg.x.type );
          }
      ] }
    ],
    Event: [
      { name: 'nodeChange', remark: '子节点有增、删、改时触发。', common: true },
      { name: 'ready', remark: '生成DOM对象后触发。', common: true },
      { name: 'resize', remark: '调整大小时触发。', common: true },
      { name: 'click', remark: '当定点设备的按钮（通常是鼠标左键）在一个元素上被按下和放开时，click事件就会被触发。', common: true },
      { name: 'contextMenu', remark: '在用户尝试打开上下文菜单时被触发。', common: true },
      { name: 'dragStart', remark: '当用户开始拖动一个元素或者一个选择文本的时候 dragStart 事件就会触发。', common: true },
      { name: 'drag', remark: '当元素或者选择的文本被拖动时触发 drag 事件。', common: true },
      { name: 'dragEnd', remark: '拖放事件在拖放操作结束时触发(通过释放鼠标按钮或单击escape键)。', common: true },
      { name: 'dragEnter', remark: '当拖动的元素或被选择的文本进入有效的放置目标时, dragEnter 事件被触发。', common: true },
      { name: 'dragExit', remark: '当一个元素不再是拖动操作的直接选择目标时，将会触发dragExit 事件。', common: true },
      { name: 'dragLeave', remark: '当一个被拖动的元素或者被选择的文本离开一个有效的拖放目标时，将会触发dragLeave 事件。', common: true },
      { name: 'dragOver', remark: '当元素或者选择的文本被拖拽到一个有效的放置目标上时，触发 dragOver 事件(每几百毫秒触发一次)。', common: true },
      { name: 'drop', remark: '当一个元素或是选中的文字被拖拽释放到一个有效的释放目标位置时，drop 事件被抛出。', common: true },
      { name: 'keyDown', remark: '按下键盘按键时触发。', common: true },
      { name: 'keyPress', remark: '按键持续被按住时触发。', common: true },
      { name: 'keyUp', remark: '当一个按键被释放时，keyup事件被触发。', common: true },
      { name: 'copy', remark: '当用户启用复制操作时触发。', common: true },
      { name: 'cut', remark: '当用户启用剪切操作时触发。', common: true },
      { name: 'paste', remark: '当用户启用粘贴操作时触发。', common: true },
      { name: 'scroll', remark: '滚动时触发。', common: true },
      { name: 'select', remark: '选择某些文本时会触发事件。', common: true },
      { name: 'selectStart', remark: '在用户开始一个新的选择时候触发。', common: true },
      { name: 'touchStart', remark: '当触点与触控设备表面接触时触发touchStart 事件。', common: true },
      { name: 'touchMove', remark: '当触点在触控平面上移动时触发touchMove事件。', common: true },
      { name: 'touchEnd', remark: '当触点离开触控平面时触发touchEnd事件。', common: true },
      { name: 'touchCancel', remark: '当触控点被特定的实现方式打乱时触发 touchCancel 事件（例如， 创建了太多的触控点）。', common: true },
      { name: 'tap', remark: '当触点在触控平面上接触和离开时，tap事件就会被触发。', common: true },
      { name: 'mouseOver', remark: '当指针设备移动到存在监听器的元素或其子元素的时候，mouseOver事件就会被触发。', common: true },
      { name: 'mouseOut', remark: '事件在当指针设备（通常是鼠标）移出了附加侦听器的元素或关闭了它的一个子元素时触发。', common: true },
      { name: 'mouseDown', remark: '事件在指针设备按钮按下时触发。', common: true },
      { name: 'mouseUp', remark: '事件在指针设备按钮抬起时触发。', common: true },
      { name: 'mouseMove', remark: '当指针设备( 通常指鼠标 )在元素上移动时, mouseMove 事件被触发。', common: true },
      { name: 'mouseWheel', remark: '当滚动鼠标滚轮或操作其它类似输入设备时会触发滚轮事件。', common: true },
      { name: 'mouseEnter', remark: '当定点设备（通常指鼠标）移动到元素上时就会触发 mouseEnter 事件。', common: true },
      { name: 'mouseLeave', remark: '指点设备（通常是鼠标）的指针移出某个元素时，会触发mouseLeave事件。', common: true },
      { name: 'dblClick', remark: '在单个元素上单击两次鼠标的指针设备按钮时, 将触发 dblClick 事件。', common: true },
      { name: 'focus', remark: '在元素获取焦点时触发。', common: true },
      { name: 'blur', remark: '在元素失去焦点时触发。', common: true },
      { name: 'change', remark: '当表单元素的 value 被修改时，会触发 change 事件。', common: true }
    ],
    Methods: [
      { name: '$([suffix])', remark: '获取 widget 对象所对应的 html 元素。', common: true, param: [
        { name: 'suffix', type: 'String', remark: '后缀。', optional: true }
      ], example: [
          function() {
          	// 给 widget 设置背景色
            widget.$().style.background = 'red';
          }
      ] },
      { name: 'addClass(cls)', remark: '增加样式。', common: true, param: [
        { name: 'cls', type: 'String', remark: '样式名。多个样式用空格隔开。' }
      ] },
      { name: 'addEvent(name, fn, [context])', remark: '绑定事件。', common: true, param: [
        { name: 'name', type: 'String', remark: '事件名称' },
        { name: 'fn', type: 'Function', remark: '绑定函数' },
        { name: 'context', type: 'Object', remark: '函数的作用域，即 this 对象。默认是调用的当前 widget。', optional: true }
      ], example: [
          function() {
          	//  绑定事件，然后触发
            wg.addEvent( 'click', function() { alert( this.id ) } );
            wg.trigger( 'click' );
          }
      ] },
      { name: 'after(opt)', remark: '在自身之后新增兄弟节点。', common: true, param: [
        { name: 'opt', type: 'object | widget | Array', remark: 'widget 配置参数或对象。如果要新增多个，可以使用数组。' }
      ], example: [
          function() {
            wg.after( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'append(opt)', remark: '在内部的末尾处插入子节点。', common: true, param: [
        { name: 'opt', type: 'object | widget | Array', remark: 'widget 配置参数或对象。如果要新增多个，可以使用数组。' }
      ], example: [
          function() {
            wg.append( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'attr(name, [value])', remark: '读/写属性。', common: true, param: [
        { name: 'name', type: 'String', remark: '属性名。' },
        { name: 'value', type: 'String', remark: '属性值。', optional: true }
      ], example: [
          function() {
          	// 读取属性
            var wg = $.create( { type: 'html', text: '123' } );
            alert( wg.attr( 'text' ) ); // 显示"123"
          },
          function() {
          	// 写入属性
            wg.attr( 'text', '456' );
          }
      ] },
      { name: 'before(opt)', remark: '在自身之前新增兄弟节点。', common: true, param: [
        { name: 'opt', type: 'object | widget | Array', remark: 'widget 配置参数或对象。如果要新增多个，可以使用数组。' }
      ], example: [
          function() {
            wg.before( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'closest(type)', remark: '获取符合条件的祖先节点。从当前节点开始，逐级向上级匹配，并返回最先匹配的节点。', common: true, param: [
        { name: 'type', type: 'String | Object | Function', remark: '如果是字符串，则按照 widget type 查找。如果是 Object, 返回满足所有条件的节点。' }
      ], example: [
          function() {
          	// 以下三条语句等效
            var p1 = wg.closest( 'vert' );
            var p2 = wg.closest( { type: 'vert' } );
            var p3 = wg.closest( function() { return this.type == 'vert' } );
          }
      ] },
      { name: 'cmd(cmdID, [arg1, arg2...argN])', remark: '执行命令。', common: true, param: [
        { name: 'cmdID', type: 'String | Object', remark: '命令ID，或命令参数对象' },
        { name: 'argN', type: 'String', remark: '调用 ajax 或 submit 命令时，会替换 src 中的 $0...$N', optional: true }
      ], example: [
          function() {
            wg.cmd( { type: 'ajax', src: 'abc.sp' } );
          }
      ] },
      { name: 'css(name, value)', remark: '设置 widget 的 style。', common: true, param: [
        { name: 'name', type: 'String | Object', remark: '样式属性名，或者是样式属性的对象。数字型的值可用 += 和 -= 来做额外附加。' },
        { name: 'value', type: 'String', remark: '样式属性值。数字型的值可用 += 和 -= 来做额外附加。' }
      ], example: [
          function() {
            vm.find( 'wg' ).css( { 'width': 100, 'background': 'red' } ); // 设置宽度为100px，背景色为红色
            vm.find( 'wg' ).css( 'height', '+=100' ); // 高度增加100px
          }
      ] },
      { name: 'data([name], [value])', remark: '读/写自定义的数据。如果不设置任何参数，则返回整个data对象。', common: true, param: [
        { name: 'name', type: 'String', remark: '属性名。', optional: true },
        { name: 'value', type: 'String', remark: '属性值。', optional: true }
      ], example: [
          function() {
            wg.data( 'mydata', '123' );
            alert( wg.data( 'mydata' ) ); // 显示"123"
          }
      ] },
      { name: 'display([show], [valid])', remark: '显示或隐藏。如果设为隐藏，当前widget内的表单不做验证。如果仍然需要验证，可以设置第二个参数。', common: true, param: [
        { name: 'show', type: 'Boolean', remark: 'true:显示; false:隐藏。', optional: true },
        { name: 'valid', type: 'Boolean', remark: 'true:验证隐藏状态下的表单。', optional: true }
      ] },
      { name: 'isDisplay()', remark: '是否在显示状态。', common: true },
      { name: 'exec(cmdID, [args], [opt])', remark: '执行命令。和 .cmd() 方法作用一样，只是参数不同。', common: true, param: [
        { name: 'cmdID', type: 'String | Object', remark: '命令ID，或命令参数对象' },
        { name: 'args', type: 'Array', remark: '调用 ajax 或 submit 命令时，会替换 src 中的 $0...$N', optional: true },
        { name: 'opt', type: 'String', remark: '为 cmdID 命令提供额外的参数。', optional: true }
      ], example: [
          function() {
          	//假设 view 中定义了命令: "new_ca": { "type": "dialog", src: 'abc.sp?id=$0', width: 500, height: 400 }，使用 exec 调用此命令：
            wg.exec( 'new_ca', [ 5 ], { target: this, pophide: true } ); // 把 src 中的 $0 替换为5, 并增加 target 和 pophide 参数
          }
      ] },
      { name: 'empty()', remark: '删除所有子节点。', common: true },
      { name: 'fireEvent(name, [args])', remark: '触发由 addEvent 绑定的事件方法。', common: true, param: [
        { name: 'name', type: 'String', remark: '事件名称。' },
        { name: 'args', type: 'Array', remark: '给绑定方法传入的参数。', optional: true }
      ], example: [
          function() {
          	// 绑定事件，然后触发
            wg.addEvent( 'click', function() { alert( this.id ) } );
            wg.fireEvent( 'click' );
          }
      ] },
      { name: 'descendants()', remark: '获取所有子孙节点，返回一个数组。', common: true },
      { name: 'descendant(type)', remark: '获取某个子孙节点。', common: true, param: [
        { name: 'type', type: 'String', remark: 'widget type。' }
      ] },
      { name: 'hasClass(cls)', remark: '是否包含某些样式。', common: true, param: [
        { name: 'cls', type: 'String', remark: '样式名。多个样式用空格隔开。' }
      ] },
      { name: 'height([num])', remark: '获取或设置高度。', common: true, param: [
        { name: 'num', type: 'String | Number', remark: '高度值。可以是数字，*, 百分比。', optional: true, common: true }
      ], example: [
          function() {
            var h = wg.height(); // 获取高度
            wg.height( 100 ); //设置高度
          }
      ] },
      { name: 'innerHeight()', remark: '获取或设置高度。即去除 padding border margin 后的高度。', common: true, example: [
          function() {
            var h = wg.innerHeight();
          }
      ] },
      { name: 'innerWidth()', remark: '获取可用宽度。即去除 padding border margin 后的宽度。', common: true, example: [
          function() {
            var w = wg.innerWidth();
          }
      ] },
      { name: 'next()', remark: '获取下一个兄弟节点。', common: true, example: [
          function() {
            var n = wg.next();
          }
      ] },
      { name: 'prepend(opt)', remark: '在内部的开始处插入子节点。', common: true, param: [
        { name: 'opt', type: 'object | widget | Array', remark: 'widget 配置参数或对象。如果要新增多个，可以使用数组。' }
      ], example: [
          function() {
            wg.prepend( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'prev()', remark: '获取上一个兄弟节点。', common: true, example: [
          function() {
            var n = wg.prev();
          }
      ] },
      { name: 'removeClass(cls)', remark: '移除样式。', common: true, param: [
        { name: 'cls', type: 'String', remark: '样式名。多个样式用空格隔开。' }
      ] },
       { name: 'removeEvent(name, [fn], [context])', remark: '移除由 addEvent 绑定的事件方法。', common: true, param: [
        { name: 'name', type: 'String', remark: '事件名称。' },
        { name: 'fn', type: 'Function', remark: '绑定函数。', optional: true },
        { name: 'context', type: 'Object', remark: '函数的作用域，即 this 对象。默认是调用的当前 widget。', optional: true }
      ], example: [
          function() {
          	// 移除事件
            wg.addEvent( 'click', function() { alert( this.id ) } );
            wg.removeEvent( 'click' );
          }
      ] },
      { name: 'render([elem], [method])', remark: '生成 html 元素。', common: true, param: [
        { name: 'elem', type: 'htmlElement', remark: '目标位置。可选参数。', optional: true },
        { name: 'method', type: 'String', remark: '生成方式。可选值: <b>append</b>, <b>prepend</b>, <b>before</b>, <b>after</b>, <b>replace</b>', optional: true }
      ] },
      { name: 'repaint([deep])', remark: '重绘当前节点。', common: true, param: [
        { name: 'deep', type: 'Boolean', remark: '设置为true，子节点也全部重绘。', optional: true }
      ] },
      { name: 'replace(opt)', remark: '把自身替换为另一个 widget。', common: true, param: [
        { name: 'opt', type: 'object | widget', remark: 'widget 配置参数或对象。' }
      ], example: [
          function() {
            wg.replace( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'remove()', remark: '移除。', common: true },
      { name: 'trigger(event, [args])', remark: '触发事件。这个方法会触发 addEvent 绑定的方法、引擎内部方法、以及配置参数里 on 设置的方法。', common: true, param: [
        { name: 'event', type: 'Event | String', remark: '事件对象或名称。' },
        { name: 'args', type: 'Array', remark: '给绑定方法传入的参数。', optional: true }
      ], example: [
          function() {
            wg.trigger( 'click' );
          }
      ] },
      { name: 'setOn(event, fn)', common: true, param: [
        { name: 'event', type: 'String', remark: '事件名称。' },
        { name: 'fn', type: 'Function | String', remark: '事件函数。' }
      ], remark: '设置事件。', example: [
          function() {
            wg.setOn( 'click', 'alert(this.type)' );
          }
      ] },
      { name: 'srcData()', common: true, remark: '获取当前widget所在的数据源祖先节点的数据源JSON对象。' },
      { name: 'srcParent()', common: true, remark: '获取当前widget所在的数据源祖先节点。' },      
      { name: 'triggerAll(event)', common: true, param: [
        { name: 'event', type: 'Event | String', remark: '事件对象或名称。' }
      ], remark: '触发自身和所有子孙节点的事件。', example: [
          function() {
            wg.triggerAll( 'click' );
          }
      ] },
      { name: 'triggerHandler(event, [args])', common: true, param: [
        { name: 'event', type: 'Event | String', remark: '事件对象或名称。' },
        { name: 'args', type: 'Array', remark: '给绑定方法传入的参数。', optional: true }
      ], remark: '触发绑定事件的函数。不会执行引擎的默认动作。', example: [
          function() {
          	// 如果 wg 是一个树节点，那么以下语句只会触发 "on": { "click": "xxx()" } 这里绑的函数 xxx()，而树节点不会聚焦(点击树节点聚焦是引擎的默认动作)
            wg.triggerHandler( 'click' );
          }
      ] },
      { name: 'width([num])', remark: '获取或设置宽度。', common: true, param: [
        { name: 'num', type: 'String | Number', remark: '宽度值。可以是数字, *, 百分比。', optional: true }
      ], example: [
          function() {
            var w = wg.width(); // 获取宽度
            wg.width( 100 ); // 设置宽度
          }
      ] }
    ]
  },
  "Button": {
  	remark: '按钮类。',
  	extend: 'Widget',
  	//deprecate: 'prepend,append',
    Config: [
      { name: 'badge', type: 'Boolean | String | Badge', remark: '显示徽标。' },
      { name: 'closable', type: 'Boolean', remark: '是否有关闭图标。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'focus', type: 'Boolean', remark: '是否焦点模式。' },
      { name: 'focusable', type: 'Boolean', remark: '设置为 true，按钮点击后转为焦点状态(按钮增加焦点样式 .z-on )' },
      { name: 'hoverDrop', type: 'Boolean', remark: '是否当鼠标 hover 时展开下拉菜单。' },
      { name: 'icon', type: 'String', remark: '图标的url。支持以 "." 开头的样式名。' },
      { name: 'more', type: 'Menu | Dialog', remark: '点击按钮时展示一个下拉元素。类型为Menu或Dialog。' },
      { name: 'name', type: 'String', remark: '在一个 view 中设置了相同 name 的 button 将成为一组，focus 只会作用于其中一个。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。点击下拉显示右键菜单。nodes 和 more 不应同时使用。' },
      { name: 'status', type: 'String', remark: '按钮状态。可选值：<b>normal</b>, <b>disabled</b>。' },
      { name: 'target', type: 'Widget', remark: '标签对应的内容widget。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'tip', type: 'String', remark: '浮动显示的提示文本。' }
    ],
    Methods: [
      { name: 'badge([value])', remark: '设置徽标。', param: [
        { name: 'value', type: 'Boolean | String | Number', remark: '读取/设置徽标。', optional: true }
      ], example: [
          function() {
          	// 设置徽标
            this.badge( 5 );
            this.badge( '99+' );
            // 取消徽标
            this.badge( false );
          }
      ] },
      { name: 'click()', remark: '模拟一次点击。' },
      { name: 'disable([bDisabled])', remark: '设置按钮状态为可用/禁用。', param: [
        { name: 'bDisabled', type: 'Boolean', remark: '是否禁用。', optional: true }
      ] },
      { name: 'focus([bFocus])', remark: '设置焦点状态。', param: [
        { name: 'bFocus', type: 'Boolean', remark: '默认为 true', optional: true }
      ], example: [
          function() {
            btn.focus(); // 聚焦
            btn.focus( false ); // 失去焦点
          }
      ] },
      { name: 'drop()', remark: '如果有设置 more 参数，执行此方法可以展示下拉菜单。'},
      { name: 'icon(src)', remark: '更换图标。', param: [
        { name: 'src', type: 'String', remark: '图标地址。可以是 url 地址或以"."开头的图标样式名。' }
      ] },
      { name: 'isFocus()', remark: '是否焦点状态。' },
      { name: 'isLocked()', remark: '是否锁定状态。' },
      { name: 'lock([locked])', remark: '锁定/解锁按钮。', param: [
        { name: 'locked', type: 'Boolean', remark: 'true: 锁定状态; false: 解除锁定。', optional: true }
      ] },
      { name: 'status([status])', remark: '获取或设置状态。', param: [
        { name: 'status', type: 'String', remark: '传入此参数是设置状态。不传此参数是获取状态。可选值: <b>normal</b><s>(默认)</s>, <b>disabled</b><s>(禁用)</s>。', optional: true }
      ] },
      { name: 'text(str)', remark: '更换文本。', param: [
        { name: 'str', type: 'String', remark: '文本内容。' }
      ] }
    ],
    Classes: [
      { name: '.w-button', remark: '基础样式。' },
      { name: '.z-ds', remark: '设置了 disabled:true 时的样式。' },
      { name: '.z-combo', remark: '同时设置了 click 事件和 nodes 时的样式。(有下拉箭头，和按钮文本分开。)' },
      { name: '.z-normal', remark: '没有 nodes 时的样式。(无下拉箭头)' },
      { name: '.z-more', remark: '设置了 nodes 时的样式。(有下拉箭头)' },
      { name: '.z-lock', remark: '按钮处于锁定状态的样式。当按钮执行submit或ajax命令时会被锁定，避免重复点击。' },
      { name: '.z-hv', remark: '鼠标移到按钮上的样式。如果按钮设置了 disabled:true 则无此样式。' },
      { name: '.z-dn', remark: '鼠标按下时的样式。如果按钮设置了 disabled:true 则无此样式。' },
      { name: '.z-first', remark: '如果按钮的父节点是ButtonBar，且当前按钮为ButtonBar的第一个子节点时的样式。' },
      { name: '.z-last', remark: '如果按钮的父节点是ButtonBar，且当前按钮为ButtonBar的最后一个子节点时的样式。' },
      { name: '.z-on', remark: '高亮的样式。如果按钮设置了 disabled:true 则无此样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	// 图标使用图片路径
          	return~
            { type: 'Button', icon: 'img/abc.gif' }
          },
          function() {
          	// 图标使用样式名
          	return~
            { type: 'Button', icon: '.i-edit', text: '编辑' }
          },
          function() {
          	// 有下拉选项的按钮
          	return~
            {
                type: 'Button',
                text: '更多',
                nodes: [
                    { text: '新建' },
                    { text: '编辑' }
                ]
            }
          }
      ] }
    ]
  },
  "OverflowButton": {
  	remark: 'ButtonBar溢出时显示的按钮。',
  	extend: 'Button',
  	deprecate: 'closable,focusable,focus,name,target,.z-on',
  	Config: [
      { name: 'effect', type: 'String', remark: '效果。可选值：<b>normal</b><s>(默认)</s>, <b>swap</b><s>(点击下拉菜单按钮，和可见按钮交换位置。)</s>', optional: true },
    ],
    Classes: [
      { name: '.w-overflowbutton', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//例2: 当按钮过多，超出按钮栏宽度时，设置overflowButton参数，显示一个"更多"的可下拉按钮。
          	return~
            {
                type: 'ButtonBar',
                space: 10,
                height: 40,
                nodes: [
                    { text: 'Button 1' },
                    { text: 'Button 2' },
                    { text: 'Button 3' }
                ],
                overflowButton: { type: 'OverflowButton', text: '更多', effect: 'swap' } //type参数可以省略
            }
          }
      ] }
    ]
  },
  "SubmitButton": {
  	remark: '默认提交按钮。<ul><li>在 text 等表单上按回车，将触发此按钮的点击事件。<li>执行 submit 命令时，默认带 lock: true 的效果。</ul>',
  	extend: 'Button',
    Classes: [
      { name: '.w-submit', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	return~
            { type: 'SubmitButton', text: '提交' }
          }
      ] }
    ]
  },
  "MenuButton": {
  	remark: 'Menu菜单中的按钮。',
  	extend: 'Button',
    Methods: [
      { name: 'getCommander()', remark: '获取最上层菜单的 commander 对象。' }
    ],
	Examples: [
	  { example: [
          function() {
          	return~
            {
                type: 'Menu',
                nodes: [
                    { type: 'MenuButton', text: 'Menu1' }, // type属性可以省略
                    { type: 'MenuButton', text: 'Menu2' }
                ]
            }
          }
      ] }
    ]
  },
  "ButtonBar": {
  	remark: 'Button 的父类。',
  	extend: 'Widget',
    Config: [
      { name: 'align', type: 'String', remark: '水平居中。可选值: <b>left</b>, <b>right</b>, <b>center</b>' },
      { name: 'dir', type: 'String', remark: '按钮排列方向。可选值: <b>h</b><s>(横向,默认)</s>, <b>v</b><s>(纵向)</s>' },
      { name: 'focusMultiple', type: 'Boolean', remark: '是否有多个按钮可同时设为焦点状态。' },
      { name: 'br', type: 'Boolean', remark: '是否换行。默认为false。' },
      { name: 'overflowButton', type: 'OverflowButton', remark: '按钮溢出可见范围时，显示一个有下拉菜单的"更多"按钮。' },
      { name: 'pub', type: 'Object', remark: '按钮的默认属性。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'space', type: 'Number', remark: '按钮之间的间隔。' },
      { name: 'split', type: 'Object', remark: '在按钮之间插入一个split widget。' },
      { name: 'vAlign', type: 'String', remark: '垂直居中。可选值: <b>top</b>, <b>bottom</b>, <b>middle</b>' }
    ],
    Methods: [
      { name: 'getFocus([name])', remark: '获取焦点状态的按钮。', param: [
        { name: 'name', type: 'String', remark: '获取相同name的焦点按钮。', optional: true }
      ] },
      { name: 'getLocked()', remark: '获取锁定状态的子节点。' }
    ],
    Classes: [
      { name: '.w-ButtonBar', remark: '基础样式。' },
      { name: '.z-dirv', remark: '设置了 dir:"v"(按钮垂直排列) 时的样式。' },
      { name: '.z-dirh', remark: '设置了 dir:"v"(按钮水平排列) 时的样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//例1: 用于对话框的按钮栏
          	return~
            {
                type: 'ButtonBar',
                space: 10,
                height: 40,
                align: 'right',
                nodes: [
                    { type: 'SubmitButton', text: '确定' },
                    { type: 'Button', text: '取消', on: { click: '$.close(this)' } }
                ]
            }
          },
          function() {
          	//例2: 当按钮过多，超出按钮栏宽度时，设置overflowButton参数，显示一个"更多"的可下拉按钮。
          	return~
            {
                type: 'ButtonBar',
                space: 10,
                height: 40,
                nodes: [
                    { text: 'Button 1' },
                    { text: 'Button 2' },
                    { text: 'Button 3' }
                ],
                overflowButton: { type: 'OverflowButton', text: '更多' }
            }
          }
      ] }
    ]
  },
  "Tabs": {
  	remark: '可切换标签容器。<br>子节点宽度默认为*，高度默认为*。',
  	extend: 'ButtonBar',
  	deprecate: 'dir,focusMultiple,br,scroll,.w-ButtonBar,.z-dirh,.z-dirv',
	Examples: [
	  { example: [
          function() {
          	// 有两个tab标签和对应内容的范例
            return~
            {
              type: "Tabs",
              pub: { cls: "f-tab", height: 30 }, // 设置标签按钮的默认参数
              nodes: [
                { type: "Tab", text: "首页", target: { type: "Html", text: "内容1" } }, // 这里的 type 属性定义可以省略
                { type: "Tab", text: "文档", target: { type: "Html", text: "内容2" } }
              ]
            }
          }
      ] }
    ],
    Config: [
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'position', type: 'String', remark: 'tab位置。可选值: <b>top</b> <b>right</b> <b>bottom</b> <b>left</b>' }
    ],
    Properties: [
      { name: 'buttonBar', type: 'ButtonBar', remark: '按钮栏对象。' },
      { name: 'frame', type: 'Frame', remark: 'Frame对象。' }
    ],
    Classes: [
      { name: '.w-tabs', remark: '基础样式。' }
    ]
  },
  "Tab": {
  	remark: '标签按钮。',
  	extend: 'Button',
  	deprecate: 'focusable,name',
    Classes: [
      { name: '.w-tab', remark: '基础样式。' }
    ]
  },
  "Collapse": {
  	remark: '可切换标签容器。',
  	extend: 'Widget',
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'Collapse',
              nodes: [
                  { type: 'CollapseButton', text: '标题 1', target: { type: 'Html', text: '内容1' } }, // 这里的 type 属性定义可以省略
                  { type: 'CollapseButton', text: '标题 2', target: { type: 'Html', text: '内容2' } }
              ]
            }
          }
      ] }
    ],
    Config: [
      { name: 'focusMultiple', type: 'Boolean', remark: '设置为true，允许多个按钮聚焦，以及子面板展开。默认值为false。' },
      { name: 'pub', type: 'Object', remark: '子节点的默认配置项。' }
    ],
    Classes: [
      { name: '.w-collapse', remark: '基础样式。' }
    ]
  },  
  "CollapseButton": {
  	remark: '标签按钮。',
  	extend: 'Button',
  	deprecate: 'focusable,name',
    Classes: [
      { name: '.w-tab', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'Collapse',
              nodes: [
                  { type: 'CollapseButton', text: '标题 1', target: { type: 'Html', text: '内容1' } }, // 这里的 type 属性定义可以省略
                  { type: 'CollapseButton', text: '标题 2', target: { type: 'Html', text: '内容2' } }
              ]
            }
          }
      ] }
    ]
  },
  "FieldSet": {
  	remark: 'FieldSet模式布局。',
  	extend: 'Widget',
    Config: [
      { name: 'legend', type: 'String', remark: '标题文本。' },
      { name: 'box', type: 'Object', remark: '选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。', param: [
        { name: 'type',    type: 'String',  remark: '类型。可选值: <b>checkbox</b>, <b>radio</b>' },
        { name: 'name',    type: 'String',  remark: '表单名。' },
        { name: 'value',   type: 'String',  remark: '表单值。' },
        { name: 'text',    type: 'String',  remark: '显示文本。', optional: true },
        { name: 'checked', type: 'Booelan', remark: '是否默认选中。', optional: true },
        { name: 'target',  type: 'String | Widget', remark: '绑定 widget 或 widgetID，同步 disabled 属性。', optional: true }
      ] },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' }
    ],
    Classes: [
      { name: '.w-fieldset', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
                type: 'FieldSet',
                legend: '标题', 
                nodes: [
                    {
                        type: 'Html',
                        text: '内容'
                    }
                ]
            };
          }
      ] }
    ]
  },
  "Frame": {
  	remark: '帧模式布局。只显示一个子元素，其他子元素都隐藏。<br>子节点宽度默认为*，高度默认为*。',
  	extend: 'Widget',
    Config: [
      { name: 'dft', type: 'String', remark: '默认显示 widget 的 ID。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' }
    ],
    Methods: [
      { name: 'getFocus()', remark: '获取当前显示的 widget。' },
      { name: 'focus(id)', remark: '显示某个子元素。', param: [
        { name: 'id', type: 'String | widget', remark: 'widget ID 或对象。' }
      ] }
    ],
    Classes: [
      { name: '.w-frame', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//用 ButtonBar + Frame 来实现 Tab 效果
            return~
            {
                type: 'Vertial',
                height: '*',
                nodes: [
                    {
                    	type: 'ButtonBar',
                    	height: 30,
                    	pub: {
                    	    focusable: true
                    	},
                    	nodes: [
                    	    { type: 'Button', text: '标题1', target: 'html_1', focus: true },
                    	    { type: 'Button', text: '标题2', target: 'html_2' }
                    	]
                    },
                    {
                    	type: 'Frame',
                    	height: '*',
                    	nodes: [
                    	    { type: 'Html', id: 'html_1', text: '内容1' },
                    	    { type: 'Html', id: 'html_2', text: '内容2' }
                    	]
                    }
                ]
            }
          }
      ] }
    ]
  },
  "Column": {
  	remark: 'table的列配置。',
    Config: [
        { name: 'align', type: 'String', remark: '水平对齐方式。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
        { name: 'cls', type: 'String', remark: '样式名。' },
        { name: 'field', type: 'String', remark: '字段名。' },
        { name: 'fixed', type: 'String', remark: '固定列。可选值：<b>left</b>, <b>right</b>' },
        { name: 'format', type: 'String', remark: '格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget格式的json对象)。' },
        { name: 'highlight', type: 'Object', remark: '高亮关键词的配置。', param: [
          { name: 'key', type: 'String', remark: '关键词。' },
          { name: 'keyCls', type: 'String', remark: '关键词样式名。' },
          { name: 'matchLength', type: 'Number', remark: '切词长度。' }
        ] },
        { name: 'labelWidth', type: 'Number', remark: '表单标题宽度。' },
        { name: 'minWidth', type: 'Number', remark: '列的最小宽度。只能用整数。' },
        { name: 'maxWidth', type: 'Number', remark: '列的最大宽度。只能用整数。' },
        { name: 'style', type: 'String', remark: '样式。' },
        { name: 'sort', type: 'Boolean | Object', remark: '设置当前列为可点击排序。如果设为true，则以当前列的值为排序依据。', param: [
          { name: 'field', type: 'String', remark: '排序字段名。' },
          { name: 'number', type: 'Boolean', remark: '是否按数字方式排序。' },
          { name: 'status', type: 'String', remark: '当前排序状态。可选值: <b>desc</b>, <b>asc</b>。' },
          { name: 'src', type: 'Boolean', remark: '后端排序URL。点击标题将访问此地址，支持变量$0(可用值:<b>desc</b>, <b>asc</b>)，$1(当前字段名)。<br>支持"javascript:"开头的JS语句。可通过 <b>this.x</b> 来获取当前列的参数。' }
        ] },
        { name: 'tip', type: 'Boolean | Object', remark: '浮动提示的字段名。如果设为true，使用当前字段值作为提示内容。', param: [
          { name: 'field', type: 'String', remark: '提示字段名。' }
        ] },
        { name: 'vAlign', type: 'String', remark: '垂直对齐方式。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
        { name: 'width', type: 'String | Percent | Number', remark: '列的宽度。可选值: *, 百分比, 整数。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center' },
              	{ field: 'B', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { A: '001', B: 'title 1' },
                  { A: '002', B: 'title 2' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "THead": {
  	remark: 'Table的表头。',
  	extend: 'Widget',
    Config: [
        { name: 'nodes', type: 'Array', remark: '表头的行数组集合。每条数据都是一个 TR。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center' },
              	{ field: 'B', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { A: '001', B: 'title 1' },
                  { A: '002', B: 'title 2' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "TBody": {
  	remark: 'Table的内容。',
  	extend: 'Widget',
    Config: [
        { name: 'nodes', type: 'Array', remark: '表格内容的行数组集合。每条数据都是一个 TR。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center' },
              	{ field: 'B', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { A: '001', B: 'title 1' },
                  { A: '002', B: 'title 2' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "TFoot": {
  	remark: 'Table的表尾。',
  	extend: 'Widget',
    Config: [
        { name: 'nodes', type: 'Array', remark: '表尾的行数组集合。每条数据都是一个 TR。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center' },
              	{ field: 'B', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { A: '001', B: 'title 1' },
                  { A: '002', B: 'title 2' }
                ]
              },
              tFoot: {
              	nodes: [
                  { A: '统计', B: '总计100' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "TableLeaf": {
  	remark: '用于table的树节点。',
  	extend: 'Leaf',
  	deprecate: 'nodes',
    Methods: [
      { name: 'tr()', remark: '获取leaf所在的tr行对象。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: 40, align: 'center' },
              	{ field: 'B', width: 150 },
              	{ field: 'C', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '树节点', C: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  {
                    data: {
                      A: '001',
                      B: { type: 'TableLeaf', text: 'leaf 1' },
                      C: 'title 1'
                    },
                    nodes: [
                      {
                        data: {
                          A: '002',
                          B: { type: 'TableLeaf', text: 'leaf 2' },
                          C: 'title 2'
                        }
                      },
                      {
                        data: {
                          A: '003',
                          B: { type: 'TableLeaf', text: 'leaf 3' },
                          C: 'title 3'
                        }
                      }
                    ]
                  }
                ]
              }
            }
          }
      ] }
     ]
  },
  "TableRowNum": {
  	remark: '用于table的自增数字字段。',
  	extend: 'Widget',
    Config: [
      { name: 'start', type: 'Number', remark: '初始值。默认值为1' }
    ],
    Classes: [
      { name: '.w-tablerownum', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
	  	    // 带自增数字的表格
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center', format: 'javascript:return {type:"TableRowNum"}' },
              	{ field: 'B', width: '50', align: 'center' },
              	{ field: 'C', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: '序号', B: 'ID', C: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { B: '001', C: 'title 1' },
                  { B: '002', C: 'title 2' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "TableRadio": {
  	remark: 'Table 内部专用的 Radio。选中状态与 TR 的 focus 效果同步。',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.w-input,.z-trans,.z-on',
  	extend: 'Radio',
	Examples: [
	  { example: [
          function() {
	  	    // 带单选框的表格
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center', format: 'javascript:return {type:"TableRadio",name:"selectItem",value:$B}' },
              	{ field: 'B', width: '50', align: 'center' },
              	{ field: 'C', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { B: 'ID', C: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { B: '001', C: 'title 1' },
                  { B: '002', C: 'title 2' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "TableTripleBox": {
  	remark: 'Table 内部专用的 TripleBox。选中状态与 TR 的 focus 效果同步。',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.w-input,.z-trans,.z-on',
  	extend: 'TripleBox',
	Examples: [
	  { example: [
          function() {
	  	    // 带选择框的表格
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center', format: 'javascript:return {type:"TableTripleBox",name:"selectItem",value:$B}' },
              	{ field: 'B', width: '50', align: 'center' },
              	{ field: 'C', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: { type: 'TableTripleBox', name: 'selectItem', checkAll: true }, B: 'ID', C: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { B: '001', C: 'title 1' },
                  { B: '002', C: 'title 2' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "TR": {
  	remark: '表格行。',
  	extend: 'Widget',
    Config: [
      { name: 'data', type: 'Object', remark: '行数据。' },
      { name: 'focus', type: 'Boolean', remark: '是否高亮。' },
      { name: 'src', type: 'String', remark: '可展开内容的地址。这个 src 应当返回一个 view' },
      { name: 'nodes', type: 'Array', remark: '子节点数组。这些子节点也应该是 tr 类型。' }
    ],
    Event: [
      { name: 'collapse', remark: '收起时触发。' },
      { name: 'expand', remark: '展开时触发。' },
      { name: 'load', remark: '经 src 加载子节点完毕时触发。' }
    ],
    Properties: [
      { name: 'rootNode', type: 'Table', remark: 'tr所属的table。' }
    ],
    Methods: [
      { name: 'checkBox([checked])', remark: '设置 tr 的 checkbox / radio 为选中状态。', param: [
        { name: 'checked', type: 'Boolean', optional: true, remark: '是否选中。' }
      ] },
      { name: 'focus([bFocus])', remark: '设置 tr 为焦点状态。', param: [
        { name: 'bFocus', type: 'Boolean', optional: true, remark: '是否选中。' }
      ] },
      { name: 'isFocus()', remark: '获取当前行是否处于焦点状态。' },
      { name: 'isBoxChecked()', remark: '获取 checkbox / radio 是否为选中状态。' },
      { name: 'move(index)', remark: '上移或下移。', param: [
        { name: 'index', type: 'Number | String', remark: '移动到指定行的序列号。支持 "+=数字", "-=数字" 格式的用法。' }
      ], example: [
          function() {
          	// 移动到第一行
            tr.move( 0 );
          },
          function() {
          	// 往上移一行
            tr.move( "-=1" );
          }
      ] },
      { name: 'toggle([expand])', remark: '展开或收拢。', param: [
        { name: 'expand', type: 'Boolean', optional: true, remark: '是否展开。' }
      ] }
    ],
    Classes: [
      { name: '.w-tr', remark: '基础样式。' },
      { name: '.z-0', remark: '当前行为偶数行时的样式。' },
      { name: '.z-1', remark: '当前行为奇数行时的样式。' },
      { name: '.z-hv', remark: '鼠标移到行上的样式。' },
      { name: '.z-on', remark: '高亮的样式。需要设置属性 focusable:true' }
    ],
	Examples: [
	  { example: [
          function() {
	  	    // TR格式的表格
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center' },
              	{ field: 'B', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { type: 'TR', data: { A: '001', B: 'title 1' } }, //type属性可以省略
                  { type: 'TR', data: { A: '002', B: 'title 2' } }
                ]
              }
            }
          }
      ] }
    ]
  },
  "TD": {
  	remark: '表格单元格。',
  	extend: 'Widget',
    Config: [
      { name: 'colSpan', type: 'Number', remark: '跨行数。' },
      { name: 'rowSpan', type: 'Number', remark: '跨列数。' },
      { name: 'align', type: 'String', remark: '水平对齐。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
      { name: 'vAlign', type: 'String', remark: '垂直对齐。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
      { name: 'node', type: 'Object', remark: '子节点。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'labelWidth', type: 'Number', remark: '表单标题宽度。' }
    ],
	Examples: [
	  { example: [
          function() {
	  	    // 用TD实现跨列
            return~
            {
              type: 'Table',
              face: 'cell',
              columns: [
              	{ field: 'A', width: '100', align: 'center' },
              	{ field: 'B', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { A: '001', B: 'title 1' },
                  { A: '002', B: 'title 2' },
                  {
                    A: {
                      type: 'TD', //type属性可以省略
                      colSpan: 2,
                      node: { type: 'Textarea', value: '跨列' }
                    }
                  }
                ]
              }
            }
          }
      ] }
    ]
  },
  "Table": {
  	remark: '表格。',
  	extend: 'Widget',
    Config: [
      { name: 'br', type: 'Boolean', remark: '内容是否换行。默认值为true。' },
      { name: 'cellPadding', type: 'Number', remark: '设置单元边沿与其内容之间的空白。' },
      { name: 'columns', type: 'Array', remark: '列参数的数组集合。<br>单个列的参数参见 Column 类。' },
      { name: 'escape', type: 'Boolean', remark: 'html内容转义。' },
      { name: 'face', type: 'String', remark: '表格行的样式。可选值: <b>line</b>(默认值，横线), <b>dot</b>(虚线), <b>cell</b>(横线和竖线), <b>none</b>(无样式)。' },
      { name: 'focusMultiple', type: 'Boolean', remark: '是否有多选的点击高亮效果。' },
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'pub', type: 'Object', remark: '为每一行设置默认属性' },
      { name: 'resizable', type: 'Boolean', remark: '是否可以拖动表头调整列宽。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'tHead', type: 'THead', remark: '表尾。' },
      { name: 'tBody', type: 'TBody', remark: '表格内容。' },
      { name: 'tFoot', type: 'TFoot', remark: '表头。' }
    ],
    Methods: [
      { name: 'filter([data])', remark: '只显示符合条件的行。', param: [
        { name: 'data', type: 'Object | Number | Function', remark: '用来过滤的字段对象，或行的序列号，或函数。', optional: true }
      ], example: [
          function() {
            // 显示所有行
            vm.find( 'myTable' ).filter();
          },
          function() {
            // 显示所有字段 C0 值为 "1" 的行
            vm.find( 'myTable' ).filter( { C0: '1' } );
          },
          function() {
            // 显示所有字段 C0 值为 "1" 或 "2" 的行
            vm.find( 'myTable' ).filter( function( data ) {
            	return data.C0 == '1' || data.C0 == '2';
            } );
          }
      ] },
      { name: 'row(data)', remark: '获取符合条件的某一行。', param: [
        { name: 'data', type: 'Object | Number | Function', remark: '用来查询的字段对象，或行的序列号，或函数。' }
      ] },
      { name: 'rows([data])', remark: '获取符合条件的所有行。返回一个由若干 tr 实例组成的数组集合。', param: [
        { name: 'data', type: 'Object | Number | Function', remark: '用来查询的字段对象，或行的序列号，或函数。', optional: true }
      ], example: [
          function() {
            // 获取所有行
            var r = vm.find( 'myTable' ).rows();
          },
          function() {
            // 获取所有字段 C0 值为 "1" 的行
            var r = vm.find( 'myTable' ).rows( { C0: '1' } );
          },
          function() {
            // 获取所有字段 C0 值为 "1" 或 "2" 的行
            var r = vm.find( 'myTable' ).rows( function( data ) {
            	return data.C0 == '1' || data.C0 == '2';
            } );
          }
      ] },
      { name: 'rowsData([data])', remark: '获取符合条件的所有行的 data json 的数组。', param: [
        { name: 'data', type: 'Object | Number | Function', remark: '用来查询的字段对象。', optional: true }
      ] },
      { name: 'getCheckedAll()', remark: '获取所有选中行，返回一个数组。' },
      { name: 'getEchoRows()', remark: '获取所有可显示的行，返回一个数组。' },
      { name: 'getFocus()', remark: '获取焦点行。' },
      { name: 'getFocusAll()', remark: '获取所有焦点行，返回一个数组。' },
      { name: 'focusRow(target)', remark: '设置焦点行。', param: [
        { name: 'target', type: 'Object | Number | Function', remark: '查询目标行的字段对象，或目标行的序列号，或函数。' }
      ] },
      { name: 'insertRow(data, [target])', remark: '新增行。', param: [
        { name: 'data', type: 'Object | Array', remark: '新增行的JSON数据。同时新增多行可以用数组。' },
        { name: 'target', type: 'Object | Number | Function', remark: '查询目标行的字段对象，或目标行的序列号，或函数。新增行的位置将在目标行之前。如果不设置此参数，新增位置为末尾。', optional: true }
      ], example: [
          function() {
            // { C1: '000' } 是新增行的数据，把它新增到 { C1: '001' } 之前。本例和下例效果相同。
            vm.find( 'myTable' ).insertRow( { C1: '000' }, { C1: '001' } );
          },
          function() {
            // { C1: '000' } 是新增行的数据，把它新增到 { C1: '001' } 之前。本例和上例效果相同。
            var r = vm.find( 'myTable' ).row( { C1: '001' } );
            r.before( { C1: '000' } );
          }
      ] },
      { name: 'updateRow(data, target)', remark: '更新行。', param: [
        { name: 'data', type: 'Object', remark: '更新行的JSON数据。' },
        { name: 'target', type: 'Object | Number | Function', remark: '查询目标行的字段对象，或目标行的序列号，或函数。' }
      ] },
      { name: 'deleteRow(target)', remark: '删除行。', param: [
        { name: 'target', type: 'Object | Number | Function', remark: '查询目标行的字段对象，或目标行的序列号，或函数。' }
      ] },
      { name: 'deleteAllRows()', remark: '删除所有行。', param: [
      ] },
      { name: 'moveRow(target, index)', remark: '移动行。', param: [
        { name: 'target', type: 'Object | Number | Function', remark: '查询目标行的字段对象，或目标行的序列号，或函数。' },
        { name: 'index', type: 'Number | String', remark: '移动到指定行的序列号。支持 "+=数字", "-=数字" 格式的用法。' }
      ] },
      { name: 'checkRow(target, [checked])', remark: '更新行的 checkbox/radio 状态为已选或未选。', param: [
        { name: 'target', type: 'Object', remark: '查询目标行的字段对象，或目标行的序列号。' },
        { name: 'checked', type: 'Boolean', remark: '是否选中。', optional: true }
      ] },
      { name: 'checkAllRows([checked])', remark: '更新所有行的 checkbox/radio 状态为已选或未选。', param: [
        { name: 'checked', type: 'Boolean', remark: '是否选中。', optional: true }
      ] },
      { name: 'insertColumn(data, [index])', remark: '插入一列。', param: [
        { name: 'data', type: 'Object', remark: '包含一列的数据，格式为 table json。' },
        { name: 'index', type: 'Number | String', remark: '如果是数字，表示插入到序列号为 index 的那一列之前；如果是字符串，表示插入到 column.field == index 的那一列之前。如果不传此参数，表示插入到最后。', optional: true }
      ], example: [
          function() {
            // 插入到最后一列
            vm.find( 'myTable' ).insertColumn( {
            	columns: [
            	  { field: 'C3', width: 100 }
            	],
            	tHead: {
            	  nodes: [ { C3: 'C3-title' } ]
            	},
            	nodes: [
            	  { C3: 'C3-content0' },
            	  { C3: 'C3-content1' }
            	]
            } );
          }
      ] },
      { name: 'updateColumn(data, index)', remark: '更新一列。', param: [
        { name: 'data', type: 'Object', remark: '包含一列的数据，格式为 table json。' },
        { name: 'index', type: 'Number | String', remark: '如果是数字，表示更新序列号为 index 的那一列；如果是字符串，表示更新 column.field == index 的那一列。' }
      ], example: [
          function() {
            // 更新 C3 字段
            vm.find( 'myTable' ).updateColumn( {
            	columns: [
            	  { field: 'C3', width: 100 }
            	],
            	tHead: {
            	  nodes: [ { C3: 'C3-title' } ]
            	},
            	nodes: [
            	  { C3: 'C3-content0' },
            	  { C3: 'C3-content1' }
            	]
            }, 'C3' );
          }
      ] },
      { name: 'deleteColumn(index)', remark: '删除一列。', param: [
        { name: 'index', type: 'Number | String', remark: '如果是数字，表示删除序列号为 index 的那一列；如果是字符串，表示删除 column.field == index 的那一列。' }
      ], example: [
          function() {
            // 删除第一列
            vm.find( 'myTable' ).deleteColumn( 0 );
          }
      ] },
      { name: 'page(index)', remark: '翻页。', param: [
        { name: 'index', type: 'Number', remark: '页数。从 0 开始计数。' }
      ] },
      { name: 'isScrollBottom()', remark: '滚动条是否滚动到了底部。' }
    ],
    Classes: [
      { name: '.w-table', remark: '基础样式。' },
      { name: '.z-empty', remark: '没有子节点时的样式。' }
    ],
	Examples: [
	  { example: [
          function() {
	  	    // 跨列范例
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center' },
              	{ field: 'B', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: 'ID', B: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { A: '001', B: 'title 1' },
                  { A: '002', B: 'title 2' },
                  {
                    A: {
                      colSpan: 2,
                      node: { type: 'Html', text: '跨列' }
                    }
                  }
                ]
              }
            }
          },
          function() {
	  	    // 带选择框的表格
            return~
            {
              type: 'Table',
              columns: [
              	{ field: 'A', width: '40', align: 'center', format: 'javascript:return {type:"TableTripleBox",name:"selectItem",value:$B}' },
              	{ field: 'B', width: '50', align: 'center' },
              	{ field: 'C', width: '*' }
              ],
              tHead: {
              	nodes: [
                  { A: { type: 'TableTripleBox', name: 'selectItem', checkAll: true }, B: 'ID', C: '标题' }
                ]
              },
              tBody: {
              	nodes: [
                  { B: '001', C: 'title 1' },
                  { B: '002', C: 'title 2' }
                ]
              }
            }
          }
      ] }
    ]
  },
  "Form": {
  	remark: '12列栅栏表单布局。',
  	extend: 'Widget',
    Config: [
      { name: 'escape', type: 'Boolean', remark: 'html内容转义。' },
      { name: 'face', type: 'String', remark: '表格行的样式。可选值: <b>line</b>(默认值，横线), <b>dot</b>(虚线), <b>cell</b>(横线和竖线), <b>none</b>(无样式)。' },
      { name: 'br', type: 'Boolean', remark: '内容是否换行。默认值为true' },
      { name: 'pub', type: 'Object', remark: '为每一个单元格设置默认属性' },
      { name: 'cols', type: 'Number', remark: '列数。默认值为12。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' }
    ],
    Methods: [
      { name: 'isScrollBottom()', remark: '滚动条是否滚动到了底部。' }
    ],
	Examples: [
	  { example: [
          function() {
          	// 简单范例
            return~
            {
                type: 'Form',
                nodes: [
                    { type: 'Text', label: { text: '标题' } },
                    { type: 'Textarea', label: { text: '内容' } }
                ]
            }
          },
          function() {
          	// 跨列范例
            return~
            {
                type: 'Form',
                cols: 12,
                nodes: [
                    { colSpan: 6, node: { type: 'Text', label: { text: '姓名' } } },
                    { colSpan: 6, node: { type: 'Text', label: { text: '住址' } } },
                    { type: 'Textarea', label: { text: '说明' } }
                ]
            }
          }
      ] }
    ]
  },
  "Vertical": {
  	remark: '子节点按垂直方向排列的布局widget。子节点的默认宽度为*，高度默认为-1。高度可以设置数字,百分比,*。如果高度设为-1，表示自适应高度。',
  	extend: 'Widget',
    Config: [
      { name: 'align', type: 'String', remark: '水平对齐方式。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'vAlign', type: 'String', remark: '垂直对齐方式。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。<br>一般情况下，如果希望纵向滚动，内部子节点高度应该设为-1；如果希望横向滚动，子节点宽度应该设为-1。' },
      { name: 'swipeDown', type: 'String', remark: '下拉刷新的URL地址。', mobile: true }
    ],
    Event: [
      { name: 'scroll', remark: '滚动时触发。' }
    ],
    Properties: [
    ],
    Methods: [
      { name: 'scrollTo(elem, [y], [x], [speed])', remark: '滚动到指定元素的位置。', param: [
        { name: 'elem',  type: 'HTMLElement | Widget', remark: '要滚动到的html元素或widget。' },
        { name: 'y',     type: 'String | Number', remark: '元素滚动到可见区域的的垂直位置。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>，或数字。', optional: true },
        { name: 'x',     type: 'String', remark: '元素滚动到可见区域的的水平位置。可选值: <b>left</b>, <b>center</b>, <b>right</b>，或数字', optional: true },
        { name: 'speed', type: 'String | Number', remark: '平滑滚动效果参数。可选值: <b>fast</b>, <b>normal</b>, <b>slow</b>, 或毫秒', optional: true }
      ] },
      { name: 'scrollTop([y], [speed])', remark: '获取或设置滚动垂直位置。不设置任何参数时返回滚动垂直位置。', param: [
        { name: 'y',     type: 'String | Number', remark: '元素滚动到可见区域的的垂直位置。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>，或数字。', optional: true },
        { name: 'speed', type: 'String | Number', remark: '平滑滚动效果参数。可选值: <b>fast</b>, <b>normal</b>, <b>slow</b>, 或毫秒', optional: true }
      ] },
      { name: 'scrollLeft([x], [speed])', remark: '获取或设置滚动水平位置。不设置任何参数时返回滚动水平位置。', param: [
        { name: 'x',     type: 'String', remark: '元素滚动到可见区域的的水平位置。可选值: <b>left</b>, <b>center</b>, <b>right</b>，或数字', optional: true },
        { name: 'speed', type: 'String | Number', remark: '平滑滚动效果参数。可选值: <b>fast</b>, <b>normal</b>, <b>slow</b>, 或毫秒', optional: true }
      ] },
      { name: 'isScrollable()', remark: '是否有滚动条。' },
      { name: 'isScrollBottom()', remark: '滚动条是否滚动到了底部。' }
    ],
    Classes: [
      { name: '.w-Vertical', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//两个Html纵向排列
            return~
            {
                type: 'Vertical',
                height: '*',
                nodes: [
                    {
                    	type: 'Html',
                    	height: 300,
                    	text: '内容1'
                    },
                    {
                    	type: 'Html',
                    	height: '*',
                    	text: '内容2'
                    }
                ]
            }
          }
      ] }
    ]
  },
  "Horizontal": {
   	remark: '子节点按水平方向排列的布局widget。子节点的默认高度为*，默认宽度为-1。宽度可以设置数字,百分比,*。如果宽度设为-1，表示自适应宽度。',
  	extend: 'Vertical',
  	deprecate: '.w-Vertical',
    Config: [
      { name: 'br', type: 'Boolean', remark: '是否换行。默认值为 false。' },
	],
    Classes: [
      { name: '.w-horizontal', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//两个Html横向排列
            return~
            {
                type: 'Horizontal',
                nodes: [
                    { type: 'Html', width: 300, text: '内容1' },
                    { type: 'Html', width: '*', text: '内容2' }
                ]
            }
          }
      ] }
    ]
  },
  "FormGroup": {
  	remark: '表单容器。默认横向排列。',
  	extend: 'Horizontal',
  	deprecate: '.w-horz,hiddens,align,vAlign,br,scroll,swipedown',
    Config: [
      { name: 'label', type: 'String | LabelWidget', optional: true, remark: '表单标签。<br><font color=red>*</font> 当设为 labelWidget 并有宽度时，将在表单左边显示标签内容。' }
	],
    Classes: [
      { name: '.w-formgroup', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在Form中显示一个FormGroup
            return~
            {
                type: 'Form',
                nodes: [
                    {
                        type: 'FormGroup',
                        label: { text: 'FormGroup', width: 90 },
                        nodes: [
                            { type: 'Html', width: 100, text: '内容1' },
                            { type: 'Html', width: '*', text: '内容2' }
                        ]
                    }
                ]
            }
          }
      ] }
    ]
  },
  "FormLabel": {
  	remark: '文本表单。',
  	extend: 'Html',
  	deprecate: '.w-html,scroll,thumbWidth',
    Classes: [
      { name: '.w-formlabel', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在Form中显示一个FormLabel
            return~
            {
                type: 'Form',
                nodes: [
                    {
                        type: 'FormLabel',
                        label: { text: 'FormLabel', width: 90 },
                        text: '内容'
                    }
                ]
            }
          }
      ] }
    ]
  },
  "docview": {
  	title: 'docview',
  	remark: 'docview是一个特殊的view。页面引入dfish后就会自动生成唯一一个docview的实例，它是所有widget的顶层元素(类似html里的document对象)。通过 VM() 或 $.vm() 方法可以获取到它。',
    Methods: [
      { name: 'find(id)', remark: '根据 id 查找 widget。', param: [
        { name: 'id', type: 'String', remark: 'widget ID。' }
      ] },
      { name: 'f(name, [range])', remark: '查找表单widget。', param: [
        { name: 'name', type: 'String', remark: '表单的name属性值。' },
        { name: 'range', type: 'HTMLElement | widget | String', remark: '指定获取表单的范围，可以是html元素或widget对象或widgetID。', optional: true }
      ], example: [
          function() {
          	// 获取一个表单的值
            var v = vm.f( 'usr' ).val();
          },
          function() {
          	// 获取一个面板内所有的表单
            var arr = vm.f( vm.find( 'root' ) );
          }
      ] },
      { name: 'fAll(name, [range])', remark: '查找相同 name 的所有表单widget，返回一个数组。', param: [
        { name: 'name', type: 'String', remark: '表单的name属性值。可以用特殊值 * 来获取所有类型的表单widget。' },
        { name: 'range', type: 'HTMLElement | widget | String', remark: '指定获取表单的范围，可以是html元素或widget对象或widgetID。', optional: true }
      ], example: [
          function() {
          	// 获取一个面板内所有的表单
            var arr = vm.fAll( '*', vm.find( 'root' ) );
          }
      ] },
      { name: 'fv(name, [value])', remark: '获取或设置表单 widget 的值。', param: [
        { name: 'name', type: 'String', remark: 'widget 的 name。' },
        { name: 'value', type: 'String', remark: '设置此参数将给表单赋值。', optional: true }
      ] },
      { name: 'getPostData([range], [json])', remark: '获取提交数据，返回URL编码的字串: "name1=value1&name2=value2..."', param: [
        { name: 'range', type: 'String', remark: '某个 widget 的 ID。设置此参数，将只获取这个 widget 内的表单。多个用逗号隔开。如果以感叹号开头，则表示排除指定的widget表单数据。', optional: true },
        { name: 'json', type: 'Boolean', remark: '设置为true，返回的数据是一个JSON对象: { name1: "value1", name2: "value2", ... }', optional: true }
      ] },
      { name: 'getValidError([group], [range])', remark: '获取表单验证结果。如果验证过程中发现错误，将返回一个包含错误信息的数组；否则返回空。', param: [
        { name: 'group', type: 'String', remark: '验证组名。', optional: true },
        { name: 'range', type: 'String', remark: '验证范围(某个 widget 的 ID)。多个ID用逗号隔开。如果以 "!" 开头，则表示排除。', optional: true }
      ] },
      { name: 'isModified([range], [original])', remark: '检测表单是否有修改，对照的值为当前表单值。如果有修改则返回被修改的表单widget。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true },
        { name: 'original', type: 'Boolean', remark: '设置为true，检测表单是否有修改，对照的值为初始值。', optional: true }
      ] },
      { name: 'saveModified([range], [original])', remark: '把表单当前的值设置为默认值。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true },
        { name: 'original', type: 'Boolean', remark: '设置为true，把表单当前值设置为初始值。', optional: true }
      ] },
      { name: 'valid([group], [range])', remark: '表单验证。验证通过返回true；验证出错将提示错误信息，并返回false。', param: [
        { name: 'group', type: 'String', remark: '验证组名。默认值为 "default"。', optional: true },
        { name: 'range', type: 'String', remark: '验证范围，某个 widget 的 ID。', optional: true }
      ] }
    ]
  },
  "Section": {
  	remark: '用来组合模板的容器。<p>' +
  		'实现顺序: <ol>' +
  		'<li>如果有node，就直接展示node。' + 
	 	'<li>有src，没有template。这个src应当返回有node(s)节点的JSON。(兼容3.1)' +
	 	'<li>有src，也有template，那么src应当返回JSON数据，用于template的内容填充。</ol></p>',
  	extend: 'Widget',
    Config: [
      { name: 'src', type: 'String | Object', remark: '数据源的URL地址或者JSON对象。' },
      { name: 'preload', type: 'String | Object', remark: '预装载模板地址，或预装载模板内容。' },
      { name: 'node', type: 'Object', remark: '直接展示的内容节点。' },
      { name: 'complete', type: 'String | Function', remark: '在得到服务器的响应后调用的函数(不论成功失败都会执行)。支持两个变量，<b>$response</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'error', type: 'String | Function', remark: '在获取服务器的响应数据失败后调用的函数。支持一个变量，<b>$ajax</b>(Ajax实例)' },
      { name: 'filter', type: 'String | Function', remark: '在获取服务器的响应数据后调用的函数。本语句应当 return 一个命令JSON。支持两个变量，<b>$response</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'success', type: 'String | Function', remark: '在成功获取服务器的响应数据并执行返回的命令之后调用的函数。如果设置了本参数，引擎将不会执行后台返回的命令，由业务自行处理。支持两个变量，<b>$response</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'sync', type: 'Boolean', remark: '加载src是否同步模式。' }
    ],
    Methods: [
      { name: 'reload([src], [template], [target], [fn])', remark: '重新装载。', param: [
        { name: 'src', type: 'String | Object', remark: '数据源的URL地址或者JSON对象。', optional: true },
        { name: 'template', type: 'String | Object', remark: '模板地址，或模板内容。', optional: true },
        { name: 'target', type: 'String', remark: 'widget ID。重新装载数据后，只更新指定的节点。多个ID以逗号隔开。', optional: true },
        { name: 'fn', type: 'String', remark: '重载后执行的回调函数。', optional: true }
      ] },
      { name: 'template(template)', remark: '重新装载模板。', param: [
        { name: 'template', type: 'String | Object', remark: '模板地址，或模板内容。' }
      ] }
	],
    Event: [
      { name: 'filter', remark: '数据下载后触发。支持 $response 变量作为返回数据对象。需返回(return)操作。', example: [
          function() {
          	// view加载完毕后显示path
            return~
            { type: 'view', id: 'myview', src: 'abc.sp', on: { filter: "return $response;" } };
          }
      ] },
      { name: 'load', remark: '数据加载完毕并展示后触发。', example: [
          function() {
          	// view加载完毕后显示path
            return~
            { type: 'view', id: 'myview', src: 'abc.sp', on: { load: "alert(this.path)" } };
          }
      ] }
    ],
	Examples: [
	  { example: [
          function() {
          	// 用 Section 加载显示
            return~
            {
                type: 'Section',
                height: '*',
                src: 'getSec.sp'
            }
          },
          function() {
          	// 上例 getSec.sp 返回的内容
            return~
            {
                type: 'Section',
                node: {
                    type: 'Html',
                    text: '内容'
                }
            }
          }
      ] }
    ]
  },
  "View": {
  	remark: '视图对象。<br>子节点宽度默认为*，高度默认为*。',
  	extend: 'Section',
  	deprecate: 'ownerView',
    Config: [
      { name: 'base', type: 'String', remark: '给当前view里的所有ajax请求指定一个默认地址。' },
      { name: 'id', type: 'String', remark: 'View 设置 id 后将产生一个 path。并可通过 VM( path ) 方法获取view。' },
      { name: 'commands', type: 'Object', remark: '命令集。' }
    ],
    Properties: [
      { name: 'path', type: 'String', remark: '路径。' },
      { name: 'parent', type: 'String', remark: '父级视图对象。' },
      { name: 'loaded', type: 'Boolean', remark: '是否装载完毕。' }
    ],
    Methods: [
      { name: 'find(id)', remark: '根据 id 查找 widget。', param: [
        { name: 'id', type: 'String', remark: 'widget ID。' }
      ] },
      { name: 'f(name, [range])', remark: '查找表单widget。', param: [
        { name: 'name', type: 'String', remark: '表单的name属性值。' },
        { name: 'range', type: 'HTMLElement | Widget', remark: '指定获取表单的范围，可以是html元素或widget对象。', optional: true }
      ], example: [
          function() {
          	// 获取一个表单的值
            var v = vm.f( 'usr' ).val();
          },
          function() {
          	// 获取一个面板内所有的表单
            var arr = vm.f( vm.find( 'root' ) );
          }
      ] },
      { name: 'fAll(name, [range])', remark: '查找相同 name 的所有表单widget，返回一个数组。', param: [
        { name: 'name', type: 'String', remark: '表单的name属性值。可以用特殊值 * 来获取所有类型的表单widget。' },
        { name: 'range', type: 'HTMLElement | widget', remark: '指定获取表单的范围，可以是html元素或widget对象。', optional: true }
      ], example: [
          function() {
          	// 获取一个面板内所有的表单
            var arr = vm.fAll( '*', vm.find( 'root' ) );
          }
      ] },
      { name: 'fv(name, [value])', remark: '获取或设置表单 widget 的值。', param: [
        { name: 'name', type: 'String', remark: 'widget 的 name。' },
        { name: 'value', type: 'String', remark: '设置此参数将给表单赋值。', optional: true }
      ] },
      { name: 'getPostData([range], [json])', remark: '获取提交数据，返回URL编码的字串: "name1=value1&name2=value2..."', param: [
        { name: 'range', type: 'String', remark: '某个 widget 的 ID。设置此参数，将只获取这个 widget 内的表单。多个用逗号隔开。如果以感叹号开头，则表示排除指定的widget表单数据。', optional: true },
        { name: 'json', type: 'Boolean', remark: '设置为true，返回的数据是一个JSON对象: { name1: "value1", name2: "value2", ... }', optional: true }
      ] },
      { name: 'getValidError([group], [range])', remark: '获取表单验证结果。如果验证过程中发现错误，将返回一个包含错误信息的数组；否则返回空。', param: [
        { name: 'group', type: 'String', remark: '验证组名。默认值为 "default"。', optional: true },
        { name: 'range', type: 'String', remark: '验证范围，某个 widget 的 ID。', optional: true }
      ] },
      { name: 'resetForm([range], [empty])', remark: '重置表单。', param: [
        { name: 'range', type: 'String', remark: 'widget ID，多个用逗号隔开。指定表单的范围。', optional: true },
        { name: 'empty', type: 'Boolean', remark: '设置为true，强制清空值。', optional: true }
      ] },
      { name: 'isModified([range], [original])', remark: '检测表单是否有修改，对照的值为默认值。如果有修改则返回 true。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true },
        { name: 'original', type: 'Boolean', remark: '设置为true，检测表单是否有修改，对照的值为初始值。', optional: true }
      ] },
      { name: 'saveModified([range], [original])', remark: '把表单当前的值设置为默认值。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true },
        { name: 'original', type: 'Boolean', remark: '设置为true，把表单当前值设置为初始值。', optional: true }
      ] },
      { name: 'valid([group], [range])', remark: '表单验证。验证通过返回true；验证出错将提示错误信息，并返回false。', param: [
        { name: 'group', type: 'String', remark: '验证组名。默认值为 "default"。', optional: true },
        { name: 'range', type: 'String', remark: '验证范围，某个 widget 的 ID。', optional: true }
      ] }
    ],
    Classes: [
      { name: '.w-view', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	// 用 View 加载显示
            return~
            {
                type: 'View',
                height: '*',
                src: 'getView.sp'
            }
          },
          function() {
          	// 上例 getView.sp 返回的内容
            return~
            {
                type: 'View',
                node: {
                    type: 'Html',
                    text: '内容'
                }
            }
          }
      ] }
    ]
  },
  "Album": {
  	remark: '图片集。',
  	extend: 'Widget',
    Config: [
      { name: 'focusMultiple', type: 'Boolean', remark: '是否可多选。' },
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。Album的子节点类型为"Img"' },
      { name: 'pub', type: 'Object', remark: '子节点的默认配置项。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'space', type: 'Number', remark: '图片之间的间隔。' }
    ],
    Methods: [
      { name: 'focusAll([bFocus])', remark: '使子节点全选/全不选。', param: [
        { name: 'bFocus', type: 'Boolean', optional: true, remark: '是否全选。' }
      ] },
      { name: 'getFocus()', remark: '获取焦点图widget。' },
      { name: 'getFocusAll()', remark: '获取所有焦点图widget，返回一个数组。' },
      { name: 'isScrollBottom()', remark: '滚动条是否滚动到了底部。' }
    ],
    Classes: [
      { name: '.w-album', remark: '基础样式。' },
      { name: '.z-face-straight', remark: '当设置参数 face:"straight" 时的样式。' },
      { name: '.z-empty', remark: '没有子节点时的样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	return~
            {
                type: 'Album',
                pub: { width: 50, height: 50 },
                nodes: [
                    { src: 'images/001.jpg', text: '图片1' },
                    { src: 'images/002.jpg', text: '图片2' }
                ]
            }
          },
          function() {
          	// 带有选择框的Album
          	return~
            {
                type: 'Album',
                focusMultiple: true,
                space: 10,
                pub: { width: 50, height: 50, focusable: true },
                nodes: [
                    { src: 'images/001.jpg', text: '图片1', box: { type: 'CheckBox', name: 'selectItem', value: '001' } },
                    { src: 'images/002.jpg', text: '图片2', box: { type: 'CheckBox', name: 'selectItem', value: '002' } }
                ]
            }
          }
      ] }
    ]
  },
  "Img": {
  	remark: '图片。',
  	extend: 'Widget',
    Config: [
      { name: 'badge', type: 'Boolean | String | Badge', remark: '显示徽标。' },
      { name: 'box', type: 'Object', remark: '选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。', param: [
        { name: 'type',    type: 'String',  remark: '类型。可选值: <b>checkbox</b>, <b>radio</b>' },
        { name: 'name',    type: 'String',  remark: '表单名。' },
        { name: 'value',   type: 'String',  remark: '表单值。' },
        { name: 'text',    type: 'String',  remark: '显示文本。', optional: true },
        { name: 'checked', type: 'Booelan', remark: '是否默认选中。', optional: true },
        { name: 'target',  type: 'String | Widget', remark: '绑定 widget 或 widgetID，同步 disabled 属性。', optional: true }
      ] },
      { name: 'description', type: 'String', remark: '图片说明。当 album face="straight" 时会显示说明。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'face', type: 'String', remark: '图片展现方式。可选值: <b>none</b>, <b>straight</b>。' },
      { name: 'focusable', type: 'Boolean', remark: '是否可选中。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'imgWidth', type: 'Number | String', remark: '图片宽度。' },
      { name: 'imgHeight', type: 'Number | String', remark: '图片高度。' },
      { name: 'textWidth', type: 'Number | String', remark: '文本宽度。' },
      { name: 'br', type: 'Boolean', remark: '文本是否换行。默认值为false。' },
      { name: 'src', type: 'String', remark: '图片地址。支持以 "." 开头的样式名。支持以 "javascript:" 开头的JS语句。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'tip', type: 'Boolean | string', remark: '图片的文本提示信息。' },
      { name: 'focus', type: 'Boolean', remark: '是否焦点状态。' }
    ],
    Methods: [
      { name: 'isFocus()', remark: '获取是否焦点状态。' },
      { name: 'focus(bFocus)', remark: '设置图片为焦点状态。', param: [
        { name: 'bFocus', type: 'Boolean', optional: true, remark: '是否焦点。' }
      ] }
    ],
    Properties: [
      { name: 'box', type: 'Widget', remark: 'widget对象的选项widget。可能是一个radio或者checkbox。' }
    ],
    Classes: [
      { name: '.w-img', remark: '基础样式。' },
      { name: '.z-hv', remark: '鼠标hover样式。' },
      { name: '.z-on', remark: '焦点高亮样式。需要设置属性 focusable:true' },
      { name: '.z-err', remark: '图片加载失败时的样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//用图片URL地址展示图片
          	return~
            { type: 'Img', width: 100, height: 100, src: 'images/001.jpg' }
          },
          function() {
          	//用样式名展示图片
          	return~
            { type: 'Img', width: 16, height: 16, src: '.f-i-config' }
          }
      ] }
    ]
  },
  "Tree": {
  	remark: '树。',
  	extend: 'Widget',
    Config: [
      { name: 'ellipsis', type: 'Boolean', remark: '设置为true，树节点文本超出可视范围部分以省略号显示。' },
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'highlight', type: 'Object', remark: '高亮关键词的配置。', param: [
        { name: 'key', type: 'String', remark: '关键词。' },
        { name: 'keyCls', type: 'String', remark: '关键词样式名。' },
        { name: 'matchLength', type: 'Number', remark: '切词长度。' }
      ] },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' },
      { name: 'pub', type: 'Object', remark: '子节点的默认配置项。' },
      { name: 'rootInvisible', type: 'Boolean', remark: '设为true，隐藏根节点，根节点的子节点缩进一层。' },
      { name: 'src', type: 'String | Object', remark: '获取子节点的 URL 地址。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'template', type: 'String', remark: '模板地址。' }
    ],
    Methods: [
      { name: 'getFocus()', remark: '获取焦点状态的 leaf。' },
      { name: 'expandTo(src, [sync], [fn])', remark: '通过src请求获取一个 json，并按照这个 json 的格式显示树。每个 leaf 节点都必须设置 id。', param: [
        { name: 'src',  type: 'String',  remark: '获取 json 的地址。' },
        { name: 'sync', type: 'Boolean',  remark: '是否同步。', optional: true },
        { name: 'fn', type: 'Function',  remark: '请求结束后执行的回调函数。', optional: true }
      ] },
      { name: 'reload()', remark: '重新装载子节点。' },
      { name: 'reloadForAdd([sync], [fn])', remark: '重新读取当前节点的 src 获取子节点数据，如果有新的子节点，将会显示这些新节点。', param: [
        { name: 'sync', type: 'Booelan', optional: true, remark: '是否同步。true: 同步; false: 异步。' },
        { name: 'fn', type: 'Function', optional: true, remark: '节点更新完毕后执行的回调函数。' }
      ], example: [
          function() {
            vm.find( 'myLeaf' ).reloadForAdd();
          }
      ] },
      { name: 'isScrollBottom()', remark: '滚动条是否滚动到了底部。' }
    ],
    Classes: [
      { name: '.w-tree', remark: '基础样式。' },
      { name: '.z-empty', remark: '没有子节点时的样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            return~
            {
                type: 'Tree',
                nodes: [
                    {
                        text: '我的收藏', 
                        expanded: true, 
                        nodes: [
                            { text: '收藏1' },
                            { text: '收藏2' }
                        ]
                    }
                ]
            }
          }
      ] }
    ]
  },
  "Leaf": {
  	remark: '树节点。',
  	extend: 'Widget',
    Config: [
      { name: 'badge', type: 'Boolean | String | Badge', remark: '显示徽标。' },
      { name: 'box', type: 'Object', remark: '选项表单，类型是 checkbox 或 radio。取消或勾选这个 box，将同步 fieldset 内部所有表单的状态。', param: [
        { name: 'type',    type: 'String',  remark: '类型。可选值: <b>checkbox</b>, <b>radio</b>, <b>triplebox</b>' },
        { name: 'name',    type: 'String',  remark: '表单名。' },
        { name: 'value',   type: 'String',  remark: '表单值。' },
        { name: 'text',    type: 'String',  remark: '显示文本。', optional: true },
        { name: 'checked', type: 'Booelan', remark: '是否默认选中。', optional: true },
        { name: 'target',  type: 'String | Widget', remark: '绑定 widget 或 widgetID，同步 disabled 属性。', optional: true }
      ] },
      { name: 'focus', type: 'Boolean', remark: '是否焦点状态。' },
      { name: 'noToggle', type: 'Boolean', remark: '是否隐藏 toggle 图标。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'focusable', type: 'Boolean', remark: '是否可选中。默认值为true。' },
      { name: 'folder', type: 'Boolean', remark: '是否有子节点。' },
      { name: 'format', type: 'String', remark: '格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'icon', type: 'String', remark: '图标。可使用图片url地址，或以 "." 开头的样式名。' },
      { name: 'line', type: 'Boolean', remark: '是否有连线效果。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' },
      { name: 'expanded', type: 'Boolean', remark: '是否展开状态。' },
      { name: 'src', type: 'String', remark: '获取子节点的 URL 地址。' },
      { name: 'status', type: 'String', remark: '节点状态。可选值：<b>normal</b>, <b>disabled</b>。' },
      { name: 'template', type: 'String', remark: '模板地址。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'tip', type: 'Boolean | String', remark: '提示信息。设为true，提示信息将使用 text 参数的值。' }
    ],
    Event: [
      { name: 'collapse', remark: '收起时触发。' },
      { name: 'expand', remark: '展开时触发。' },
      { name: 'load', remark: '经 src 加载子节点完毕时触发。' }
    ],
    Properties: [
      { name: 'rootNode', type: 'Tree', remark: 'leaf所属的tree。' }
    ],
    Methods: [
      { name: 'disable([bDisabled])', remark: '设置按钮状态为可用/禁用。', param: [
        { name: 'bDisabled', type: 'Boolean', remark: '是否禁用。', optional: true }
      ] },
      { name: 'toggle([expand], [sync], [fn])', remark: '展开或收拢。', param: [
        { name: 'expand', type: 'Booelan', optional: true, remark: '是否展开。true: 展开; false: 收拢。' },
        { name: 'sync', type: 'Booelan', optional: true, remark: '是否同步。true: 同步; false: 异步。' },
        { name: 'fn', type: 'Function', optional: true, remark: '节点展开或收拢后执行的回调函数。' }
      ] },
      { name: 'toggleOne([expand], [sync], [fn])', remark: '展开或收拢。当前节点为展开状态时，其他兄弟节点全部收拢。', param: [
        { name: 'expand', type: 'Booelan', optional: true, remark: '是否展开。true: 展开; false: 收拢。' },
        { name: 'bSync', type: 'Booelan', optional: true, remark: '是否同步。true: 同步; false: 异步。' },
        { name: 'fn', type: 'Function', optional: true, remark: '节点展开或收拢后执行的回调函数。' }
      ] },
      { name: 'focus([bFocus])', remark: '设为焦点状态。', param: [
        { name: 'bFocus', type: 'Booelan', optional: true, remark: '是否焦点状态。' }
      ] },
      { name: 'isFocus()', remark: '获取树节点是否为焦点状态。' },
      { name: 'reload()', remark: '重新装载子节点。' },
      { name: 'reloadForAdd([sync], [fn])', remark: '重新读取当前节点的 src 获取子节点数据，如果有新的子节点，将会显示这些新节点。', param: [
        { name: 'sync', type: 'Booelan', optional: true, remark: '是否同步。true: 同步; false: 异步。' },
        { name: 'fn', type: 'Function', optional: true, remark: '节点更新完毕后执行的回调函数。' }
      ], example: [
          function() {
            vm.find( 'myLeaf' ).reloadForAdd();
          }
      ] },
      { name: 'reloadForModify([sync], [fn])', remark: '重新读取父节点的 src 获取当前节点数据，并更新当前节点。', param: [
        { name: 'sync', type: 'Booelan', optional: true, remark: '是否同步。true: 同步; false: 异步。' },
        { name: 'fn', type: 'Function', optional: true, remark: '节点更新完毕后执行的回调函数。' }
      ] },
      { name: 'checkBox([checked])', remark: '设置 checkbox / radio为选中/未选中状态。', param: [
        { name: 'checked', type: 'Booelan', optional: true, remark: '是否选中。' }
      ] },
      { name: 'isBoxChecked()', remark: '获取 checkbox / radio 是否为选中状态。' },
      { name: 'scrollIntoView()', remark: '当前节点滚动到可视范围。' }
    ],
    Classes: [
      { name: '.w-leaf', remark: '基础样式。' },
      { name: '.z-hv', remark: '鼠标hover样式。' },
      { name: '.z-on', remark: '焦点高亮样式。' },
      { name: '.z-loading', remark: '当树节点正在装载数据时的样式。' },
      { name: '.z-folder', remark: '有子节点时的样式。' },
      { name: '.z-expanded', remark: '展开时的样式。' },
      { name: '.z-first', remark: '在兄弟节点中排行第一时的样式。' },
      { name: '.z-last', remark: '在兄弟节点中排行最后时的样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            return~
            {
                type: 'Tree',
                nodes: [
                    {
                        type: 'Leaf', // type属性可以省略
                        text: '我的收藏', 
                        expanded: true, 
                        nodes: [
                            { type: 'Leaf', text: '收藏1' },
                            { type: 'Leaf', text: '收藏2' }
                        ]
                    }
                ]
            }
          }
      ] }
    ]
  },
  "Html": {
  	remark: '展示html内容。',
  	extend: 'Widget',
    Config: [
      { name: 'align', type: 'String', remark: '水平对齐。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化内容。支持"javascript:"开头的JS语句(需return返回值)。' },
      { name: 'vAlign', type: 'String', remark: '垂直对齐。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'text', type: 'String', remark: 'html内容。支持 &lt;d:wg&gt; 标签。' },
      { name: 'thumbWidth', type: 'Number | String', remark: '设置内容区域所有图片的最大宽度。点击图片可以预览大图。' }
    ],
    Methods: [
      { name: 'text(content)', remark: '更新内容。', param: [
        { name: 'content', type: 'String', remark: 'HTML内容。支持 &lt;d:wg&gt; 标签。' }
      ] },
      { name: 'isScrollBottom()', remark: '滚动条是否滚动到了底部。' }
    ],
    Classes: [
      { name: '.w-html', remark: '基础样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            return~
            { type: 'Html', text: '显示内容' }
          }
      ] }
    ]
  },
  "Label": {
  	remark: '表单标签。',
  	extend: 'Widget',
    Config: [
      { name: 'align', type: 'String', remark: '水平居中。可选值: <b>left</b>, <b>right</b>, <b>center</b>' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'suffix', type: 'String', remark: '后缀。' },
      { name: 'text', type: 'String', remark: '内容。' }
    ],
    Classes: [
      { name: '.w-label', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
                type: 'Form',
                nodes: [
                    { type: 'Text', label: { text: '标题', width: 100 } },
                    { type: 'Textarea', label: { text: '内容', width: 100 } }
                ]
            }
          }
      ] }
    ]
  },
  "Blank": {
  	remark: '空白面板。',
  	extend: 'Widget',
    Classes: [
      { name: '.w-Blank', remark: '基础样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            return~
            { type: 'Blank' }
          }
      ] }
    ]
  },
  "Badge": {
  	remark: '徽标。',
  	extend: 'Widget',
    Config: [
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'text', type: 'String', remark: '徽标内容。如果不设置本参数，将显示一个圆点。' }
    ],
    Classes: [
      { name: '.w-widget', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在按钮上显示圆点徽标
          	return~
            {
                type: 'Button',
                badge: true
            }
          },
          function() {
          	//在按钮上显示有数字的徽标
          	return~
            {
                type: 'Button',
                badge: {
                    text: '99'
                }
            }
          }
      ] }
    ]
  },
  "Progress": {
  	remark: '进度条集合。',
  	extend: 'Widget',
    Config: [
      { name: 'delay', type: 'Number', remark: '延迟访问 src 。单位:毫秒。' },
      { name: 'pub', type: 'Object', remark: '子节点的默认配置项。' },
      { name: 'src', type: 'String', remark: '访问这个地址返回一个命令json。如果返回的是 progress json，当前实例将被替换。' },
      { name: 'guide', type: 'String', remark: '首次访问的地址。比src优先，且只访问一次。' },
      { name: 'nodes', type: 'Array', remark: '子节点数组。' }
    ],
    Methods: [
      { name: 'stop()', remark: '停止由 delay 参数引起的延迟。' },
      { name: 'start()', remark: '继续 delay 参数的延迟执行。' }
    ],
    Classes: [
      { name: '.w-progress', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	return~
            {
                type: 'Progress',
                width: 200,
                nodes: [
                    { percent: 30 }
                ]
            }
          }
      ] }
    ]
  },
  "ProgressItem": {
  	title: 'ProgressItem',
  	remark: '进度条。',
  	extend: 'Widget',
    Config: [
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'percent', type: 'Number', remark: '进度值。范围从 0 到 100。' },
      { name: 'range', type: 'String', remark: '划分进度阶段的数值，用逗号隔开。每个数字都会生成该阶段的样式 "z-数值"，数值范围从 0 到 100。<br>例如设置 range: "60,100"，那么进度在 (>=60 && <100) 范围内会存在样式 "z-60"，进度在 100 时会存在样式 "z-100"。' },
      { name: 'text', type: 'String', remark: '显示文本。' }
    ],
    Methods: [
      { name: 'stop()', remark: '停止由 delay 参数引起的延迟。' },
      { name: 'start()', remark: '继续 delay 参数的延迟执行。' }
    ],
    Classes: [
      { name: '.w-progress', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	return~
            {
                type: 'Progress',
                width: 200,
                nodes: [
                    { type: 'ProgressItem', percent: 30 } // type属性可以省略
                ]
            }
          }
      ] }
    ]
  },
  "Timeline": {
  	remark: '时间轴。',
  	extend: 'Widget',
    Config: [
      { name: 'align', type: 'String', optional: true, remark: '水平对齐方式。可选值: <b>left</b>, <b>right</b>, <b>center</b>' },
      { name: 'nodes', type: 'Array', remark: '子节点数组。' },
      { name: 'pub', type: 'Object', remark: '子节点的默认配置项。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' }
    ],
    Classes: [
      { name: '.w-timeline', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
	  	    // 靠左
            return~
            {
                type: "Timeline",
                nodes: [
                    { text: "商品已经下单 2019-09-01" },
                    { text: "卖家已发货 2019-09-01" },
                    { text: "包裹正在等待揽收 2019-09-01", icon: ".f-i-search" },
                    { text: "正在为您派件 2019-09-01" }
                ]
            }
          },
          function() {
	  	    // 靠右
            return~
            {
                type: 'Timeline', 
                align: 'right', 
                nodes: [
                    { text: "商品已经下单" },
                    { text: "卖家已发货" },
                    { text: "包裹正在等待揽收" },
                    { text: "正在为您派件" }
                ]
            }
          },
          function() {
	  	    // 居中
            return~
            {
                type: 'Timeline',
                align: 'center', 
                nodes: [
                    { text: "商品已经下单" },
                    { text: "卖家已发货", align: 'left' },
                    { text: "包裹正在等待揽收" },
                    { text: "正在为您派件", align: 'left' }
               ]
            }
          }
      ] }
    ]
  },
  "TimelineItem": {
  	remark: '时间轴条目。',
  	extend: 'Widget',
    Config: [
      { name: 'align', type: 'String', optional: true, remark: '水平对齐方式。仅当父节点设置align:"center"时本参数有效。可选值: <b>left</b>, <b>right</b>' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'icon', type: 'String', optional: true, remark: '图标。可用 "." 开头的样式名，或图片路径。' },
      { name: 'text', type: 'String', remark: '显示文本。' }
    ],
    Classes: [
      { name: '.w-timeline-item', remark: '基础样式。' },
      { name: '.z-first', remark: '当前节点是兄弟节点中的首个节点时的样式。' },
      { name: '.z-last', remark: '当前节点是兄弟节点中的末尾节点时的样式。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
                type: 'Timeline',
                nodes: [
                    { type: 'TimelineItem', text: '商品已经下单 2019-09-01' }, // type属性可以省略
                    { type: 'TimelineItem', text: '卖家已发货 2019-09-01' },
                    { type: 'TimelineItem', text: '包裹正在等待揽收 2019-09-01' },
                    { type: 'TimelineItem', text: '正在为您派件 2019-09-01' }
                ]
            }
          }
      ] }
    ]
  },
  "EmbedWindow": {
  	remark: '生成一个内嵌窗口。作用与HTML的&lt;iframe&gt;标签相同。',
  	extend: 'Widget',
    Config: [
      { name: 'scroll', type: 'Boolean', remark: '是否显示滚动条。' },
      { name: 'src', type: 'String', remark: '页面地址。' },
      { name: 'text', type: 'String', remark: 'HTML内容。text 属性和 src 属性不应同时使用。' }
    ],
    Methods: [
      { name: 'getContentWindow()', remark: '获取内嵌窗口的 window 对象。' },
      { name: 'reload([src])', remark: '窗口刷新。', param: [
        { name: 'src', type: 'String', optional: true, remark: '窗口地址。' }
      ] },
      { name: 'text(content)', remark: '写入文本内容。', param: [
        { name: 'content', type: 'String', remark: '文本内容。' }
      ] }
    ],
    Examples: [
	  { example: [
          function() {
            return~
            { type: 'EmbedWindow', src: 'http://www.baidu.com' }
          }
      ] }
    ]
  },
  "Toggle": {
  	remark: '展开收拢的工具条。',
  	extend: 'Widget',
    Config: [
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'icon', type: 'String', optional: true, remark: '收拢时的图标。可用 . 开头的样式名，或图片路径。' },
      { name: 'expandedIcon', type: 'String', optional: true, remark: '展开时的图标。可用 . 开头的样式名，或图片路径。' },
      { name: 'hr', type: 'Boolean', optional: true, remark: '显示一条水平线。' },
      { name: 'expanded', type: 'Boolean', optional: true, remark: '设置初始状态为展开或收拢。如果设置了此参数，会产生一个toggle图标' },
      { name: 'target', type: 'String', remark: '绑定要展开收拢的 widget ID。多个用逗号隔开。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'tip', type: 'Boolean | String', remark: '提示信息。设为true，提示信息将使用 text 参数的值。' }
    ],
    Event: [
      { name: 'collapse', remark: '收起时触发。' },
      { name: 'expand', remark: '展开时触发。' }
    ],
    Methods: [
      { name: 'text(content, [method])', remark: '更新内容。', param: [
        { name: 'content', type: 'String', remark: 'HTML内容。' },
        { name: 'method', type: 'String', optional: true, remark: '写入方式。可选值: <b>replace</b>, <b>prepend</b>, <b>append</b>' }
      ] },
      { name: 'toggle([expand])', remark: '展开或收拢。', param: [
        { name: 'expand', type: 'Boolean', optional: true, remark: '是否展开。' }
      ] }
    ],
    Classes: [
      { name: '.w-toggle', remark: '基础样式。' },
      { name: '.z-expanded', remark: '展开时的样式。' },
      { name: '.z-hr', remark: '有横线时的样式。' }
    ],
    Examples: [
	  { example: [
          function() {
          	//点击Toggle，展开或收起后面的兄弟节点
            return~
            {
                type: 'Vertical',
                nodes: [
                    { type: 'Toggle', text: '展开折叠', expanded: true },
                    { type: 'Html', text: '内容1' },
                    { type: 'Html', text: '内容2' }
                ]
            }
          }
      ] }
    ]
  },
  "Split": {
  	remark: '分割线。',
  	extend: 'Widget',
    Config: [
      { name: 'range', type: 'String', optional: true, remark: '设置拖动调整大小的前后范围。只在父节点为 vert, horz 时可用。此参数由2-3个数字组成，以逗号隔开。第一个数字表示前一个节点的最小size，第二个数字表示后一个节点的最小size，第三个数字可选，表示 toggle 节点的初始size。', example: [
          function() {
            // 横向布局的3个widget, 拖动中间的分割线调整大小
            return~
            { type: 'horz', nodes:[
                { type: 'html',  width: '*', text: 'aaa' },
                { type: 'split', width: 1, style: 'background:blue', range: '100,100' },
                { type: 'html',  width: '*', text: 'bbb' }
            ] }
          }
      ] },
      { name: 'icon',  type: 'String', optional: true, remark: '收拢图标。图片地址url，或是以点 "." 开头的样式名。' },
      { name: 'expandedIcon',  type: 'String', optional: true, remark: '展开图标。图片地址url，或是以点 "." 开头的样式名。' },
      { name: 'hide',  type: 'String', optional: true, remark: '指定展开收拢的节点位置。可选值: <b>prev</b><s>(默认,前节点)</s>, <b>next</b><s>(后节点)</s>。本参数配合 icon expandedIcon 参数一起使用。' },
      { name: 'movable',  type: 'Boolean', optional: true, remark: '设置为true，可以用鼠标拖动调整大小。' },
      { name: 'text',  type: 'String', optional: true, remark: '显示文本。' }
    ],
    Methods: [
      { name: 'toggle([expand])', remark: '展开或收拢。', param: [
        { name: 'expand', type: 'Boolean', optional: true, remark: '是否展开。' }
      ] }
    ],    
    Classes: [
      { name: '.w-split', remark: '基础样式。' },
      { name: '.z-expanded', remark: '展开时的样式。' },
      { name: '.z-hv', remark: '鼠标经过时的样式。' }
    ],
    Examples: [
	  { example: [
          function() {
          	//点击Toggle，展开或收起后面的兄弟节点
            return~
            {
                type: 'Vertical',
                nodes: [
                    { type: 'Toggle', text: '展开折叠', expanded: true },
                    { type: 'Html', text: '内容1' },
                    { type: 'Html', text: '内容2' }
                ]
            }
          }
      ] }
    ]
  },
  "PageBar": {
  	remark: '小按钮风格的翻页工具条。',
  	extend: 'Widget',
    Config: [
      { name: 'align', type: 'String', optional: true, remark: '水平对齐方式。可选值: <b>left</b>, <b>right</b>, <b>center</b>' },
      { name: 'buttonCls', type: 'String', optional: true, remark: '按钮样式。' },
      { name: 'buttonCount', type: 'Number', optional: true, remark: '数字页数按钮的数量。' },
      { name: 'buttonSumPage', type: 'Boolean', optional: true, remark: '显示总页数按钮。' },
      { name: 'currentPage', type: 'Number', remark: '当前页数。(起始值为1)' },
      { name: 'dropAlign', type: 'String', optional: true, remark: '下拉按钮的位置，仅当 face:"simple" 时本参数有效。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
      { name: 'face', type: 'String', optional: true, remark: '样式。可选值: <b>normal</b>, <b>mini</b>, <b>simple</b>, <b>none</b>' },
      { name: 'jump', type: 'Boolean', optional: true, remark: '显示一个可填写页数的表单。' },
      { name: 'keyJump', type: 'Boolean', optional: true, remark: '设置为true，按下"← →"键时执行翻页。' },
      { name: 'name', type: 'String', optional: true, remark: '如果设置了name，将生成一个隐藏表单项，值为当前页数。' },
      { name: 'noFirstLast', type: 'Boolean', optional: true, remark: '不显示"首页"和"尾页"两个按钮。' },
      { name: 'firstText', type: 'String', optional: true, remark: '"首页"的文本。' },
      { name: 'lastText', type: 'String', optional: true, remark: '"尾页"的文本。' },
      { name: 'nextText', type: 'String', optional: true, remark: '"下一页"的文本。' },
      { name: 'pageSize', type: 'Number', remark: '每页显示多少条。配合target参数使用。' },
      { name: 'prevText', type: 'String', optional: true, remark: '"上一页"的文本。' },
      { name: 'src', type: 'String', optional: true, remark: '点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。支持以 "javascript:" 开头的JS语句。' },
      { name: 'sumPage', type: 'Number', remark: '总页数。(起始值为1)' },
      { name: 'setting', type: 'Array', remark: 'button数组。生成一个配置按钮和下拉菜单。' },
      { name: 'target', type: 'Boolean', optional: true, remark: '绑定一个支持前端翻页的widget(例如table)。' },
      { name: 'transparent', type: 'Boolean', optional: true, remark: '设置为true，可去除边框背景等预设样式。' }
    ],
    Methods: [
      { name: 'val([page])', remark: '设置/获取当前页数。', param: [
        { name: 'page', type: 'Number', remark: '当前页数。如果设置了此参数，则跳转到这一页；如果不设置此参数，则返回当前页数值。' }
      ] }
    ],
    Classes: [
      { name: '.w-page', remark: '基础样式。' },
      { name: '.z-face-normal', remark: 'normal样式。' },
      { name: '.z-face-mini', remark: 'mini样式。' },
      { name: '.z-face-simple', remark: 'simple样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            return~
			{ type: 'PageBar', face: 'mini', height: 22, currentPage: 1, buttonCount: 5, sumPage: 10, src: 'getData.sp?page=$0' }
          }
      ] }
    ]
  },
  "Calendar": {
  	remark: '日历。',
  	extend: 'Widget',
    Config: [
      { name: 'nodes', type: 'Array', optional: true, remark: 'CalendarItem的数组集合。' },
      //{ name: 'cg', type: 'Number', optional: true, remark: '一周的重心是星期几。可选值从1到7。仅当 face 值为 "week" 时本参数有效。' },
      { name: 'date', type: 'String', optional: true, remark: '以此日期为基准显示日历。' },
      { name: 'face', type: 'String', optional: true, remark: '日历类型。可选值：<b>date</b>, <b>week</b>, <b>month</b>, <b>year</b>。默认值为"date"。' },
      { name: 'focusDate', type: 'String', optional: true, remark: '高亮显示的某一日期。' },
      { name: 'src', type: 'String', optional: true, remark: '点击日期将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表日期。' },
      //{ name: 'start', type: 'Number', optional: true, remark: '一周的第一天是星期几。可选值从1到7。仅当 face 为week时本参数有效。' },
      { name: 'fillBlank', type: 'Boolean', optional: true, remark: '设置为true，填补空白的日期。当日历不满6行时填补空白行。' },
      { name: 'pub', type: 'Object', optional: true, remark: '日期按钮的公共设置。' }
    ],
    Methods: [
      { name: 'click(date)', remark: '执行某个日期的点击事件。', param: [
        { name: 'date', type: 'String', remark: '指定日期。' }
      ] },
      { name: 'focus(date)', remark: '设置某个日期为焦点状态。', param: [
        { name: 'date', type: 'String', remark: '指定日期。' }
      ] },
      { name: 'go(date, [fn])', remark: '跳转到某个日期。', param: [
        { name: 'date', type: 'String', remark: '指定日期。' },
        { name: 'fn', type: 'Function', remark: '跳转成功后执行的回调函数。', optional: true }
      ] },
      { name: 'val()', remark: '获取处于焦点状态的日期值。' }
    ],
    Classes: [
      { name: '.w-calendar', remark: '基础样式。' },
      { name: '.w-calendar-date', remark: '基础样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            // 简单范例
            return~
            { type: 'Canlendar', face: 'date' }
          },
          function() {
            // 给1日和7日设置样式和内容
            return~
            {
              type: 'Canlendar',
              face: 'date',
              date: '2019-05-05',
              pub: {
                  on: { click: 'alert(this.val())' }
              },
              nodes: [
                  { type: 'CalendarItem', value: '2019-05-01', cls: 'x-cal-yes', text: '第一个日程' }, // 可以省略 type 定义 
                  { type: 'CalendarItem', value: '2019-05-07', cls: 'x-cal-yes', text: '第二个日程' }
              ]
            }
          }
      ] }
    ]
  },
  "CalendarItem": {
  	remark: '日历单元格。',
  	extend: 'Widget',
    Config: [
      { name: 'focus', type: 'Boolean', remark: '是否焦点模式。' },
      { name: 'focusable', type: 'Boolean', remark: '设置为 true，点击后转为焦点状态(按钮增加焦点样式 .z-on )' },
      { name: 'value', type: 'String', remark: '日期。' },
      { name: 'text', type: 'String', optional: true, remark: '显示内容。' }
    ],
    Classes: [
      { name: '._td', remark: '基础样式。' },
      { name: '.z-pad', remark: '填充空白的状态样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            // 给1日和7日设置样式和内容
            return~
            {
              type: 'Canlendar',
              face: 'date',
              date: '2019-05-05',
              pub: {
                  on: { click: 'alert(this.val())' }
              },
              nodes: [
                  { type: 'CalendarItem', value: '2019-05-01', cls: 'x-cal-yes', text: '第一个日程' }, // 可以省略 type 定义 
                  { type: 'CalendarItem', value: '2019-05-07', cls: 'x-cal-yes', text: '第二个日程' }
              ]
            }
          }
      ] }
    ]
  },
  "Text": {
  	remark: '单行文本输入框。',
  	extend: 'Widget',
    Config: [
      { name: 'label', type: 'String | Label', optional: true, remark: '表单标签。当设为 labelWidget 并有宽度时，将在表单左边显示标签内容。',
      	 example: [
          function() {
            // 在css中设置标签背景色
            return''
            '.w-label ._bg{background:#ddd}'
          }
      ] },
      { name: 'name', type: 'String', remark: '表单名。' },
      { name: 'placeholder', type: 'String', optional: true, remark: '当表单没有值时显示的提示文本。' },
      { name: 'status', type: 'String', optional: true, remark: '表单状态。可选值: <b>normal</b><s>(默认)</s>, <b>readonly</b><s>(只读，不验证数据)</s>, <b>validonly</b><s>(只读，验证数据)</s>, <b>disabled</b><s>(禁用)</s>。' },
      { name: 'tip', type: 'Boolean | string', remark: '提示信息。如果设置为true，提示内容为当前的文本。' },
      { name: 'transparent', type: 'Boolean', optional: true, remark: '设置为true，表单将成为无边框无背景的状态。' },
      { name: 'value', type: 'String', remark: '表单值。' },
      { name: 'validate', type: 'Object', optional: true, remark: '表单校验选项。', param: [
        { name: 'required', type: 'Boolean | Object', remark: '必填。', param: [
          { name: 'value', type: 'Boolean', remark: '是否必填。默认值为true。' },
          { name: 'text', type: 'String', remark: '必填提示文本。' }
        ] },
        { name: 'pattern', type: 'String | Object', remark: '正则表达式。', param: [
          { name: 'value', type: 'String', remark: '正则表达式。' },
          { name: 'text', type: 'String', remark: '正则提示文本。' }
        ] },
        { name: 'compare', type: 'Object', remark: '和另一个表单做比较。', param: [
          { name: 'target', type: 'String', remark: '做比较的表单的name。' },
          { name: 'mode', type: 'String', remark: '比较符号，可选值: <b>></b> <b>>=</b> <b><</b> <b><=</b> <b>==</b>。' },
          { name: 'text', type: 'String', remark: '比较提示文本。' }
        ] },
        { name: 'minLength', type: 'Number | Object', remark: '最小字节数。', param: [
          { name: 'value', type: 'Number', remark: '最小字节数。' },
          { name: 'text', type: 'String', remark: '最小字节数提示文本。' }
        ] },
        { name: 'maxLength', type: 'Number | Object', remark: '最大字节数。用于 Text Textarea Password', param: [
          { name: 'value', type: 'Number', remark: '最大字节数。' },
          { name: 'text', type: 'String', remark: '最大字节数提示文本。' }
        ] },
        { name: 'minValue', type: 'String | Object', remark: '最小值。用于 Spinner DatePicker', param: [
          { name: 'value', type: 'Number', remark: '最小值。' },
          { name: 'text', type: 'String', remark: '最小值提示文本。' }
        ] },
        { name: 'maxValue', type: 'String | Object', remark: '最大值。用于 Spinner DatePicker', param: [
          { name: 'value', type: 'Number', remark: '最大值。' },
          { name: 'text', type: 'String', remark: '最大值提示文本。' }
        ] },
        { name: 'minSize', type: 'Number | Object', remark: '最少选择几项。用于 CheckBox', param: [
          { name: 'value', type: 'Number', remark: '最少选择几项。' },
          { name: 'text', type: 'String', remark: '最少选择几项提示文本。' }
        ] },
        { name: 'maxSize', type: 'Number | Object', remark: '最多选择几项。用于 CheckBox', param: [
          { name: 'value', type: 'Number', remark: '最多选择几项。' },
          { name: 'text', type: 'String', remark: '最多选择几项提示文本。' }
        ] },
        { name: 'beforeNow', type: 'Boolean | Object', remark: '不能大于当前时间。用于 DatePicker', param: [
          { name: 'value', type: 'Number', remark: '不能大于当前时间。默认值为true。' },
          { name: 'text', type: 'String', remark: '不能大于当前时间的显示文本。' }
        ] },
        { name: 'afterNow', type: 'Boolean | Object', remark: '不能小于当前时间。用于 DatePicker', param: [
          { name: 'value', type: 'Number', remark: '不能小于当前时间。默认值为true。' },
          { name: 'text', type: 'String', remark: '不能小于当前时间的显示文本。' }
        ] },
        { name: 'method', type: 'String', remark: 'JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。' }
      ] },
      { name: 'validateGroup', type: 'Object', optional: true, remark: '表单校验选项组。', param: [
          { name: 'name', type: 'String', remark: '校验组名。' },
          { name: 'validate', type: 'Object', remark: '校验选项。' }
        ] }
    ],
    Event: [
      { name: 'change', remark: '值发生改变时触发。' },
      { name: 'valid', remark: '表单校验时触发。' }
    ],
    Methods: [
      { name: 'focus([bFocus])', remark: '设置表单为焦点状态。', param: [
        { name: 'bFocus', type: 'Boolean', optional: true, remark: '是否聚焦。' }
      ] },
      { name: 'focusEnd()', remark: '设置表单为焦点状态，并把光标移动到末尾。' },
      { name: 'valid([item], [group])', remark: '执行表单验证。验证成功返回true，否则返回false。', param: [
        { name: 'item', type: 'String', remark: '验证参数项目名。', optional: true },
        { name: 'group', type: 'String', remark: '验证组名。', optional: true }
      ] },
      { name: 'setValidate(opt, [group])', remark: '设置表单的验证参数。', param: [
        { name: 'opt', type: 'Object', remark: '验证参数对象。', optional: true },
        { name: 'group', type: 'String', remark: '验证组名。', optional: true }
      ], example: [
          function() {
            // 设置表单为必填
            vm.f( 'myText' ).setValidate( {required: true} );
          }
      ] },
      { name: 'getValidError([vld])', remark: '获取校验结果。如果校验成功则返回空，校验有错则返回一个JSON格式的错误信息，格式为 { name: "表单名", code: "错误代号", text: "错误描述" }。', param: [
        { name: 'vld', type: 'String', remark: '检验组名。', optional: true }
      ], example: [
          function() {
            // 显示校验信息
            var e = vm.f( 'myText' ).getValidError( 'default' );
            alert( $.jsonString( e ) );
          }
      ] },
      { name: 'normal()', remark: '设置表单状态为正常状态。' },
      { name: 'disable([bDisabled])', remark: '设置表单状态为可用/禁用。', param: [
        { name: 'bDisabled', type: 'Boolean', remark: '是否禁用。', optional: true }
      ] },
      { name: 'readonly([bReadonly])', remark: '设置表单状态为只读。', param: [
        { name: 'bReadonly', type: 'Boolean', remark: '是否只读。', optional: true }
      ] },
      { name: 'validonly([bValidonly])', remark: '设置表单状态为只读并可验证数据。', param: [
        { name: 'bReadonly', type: 'Boolean', remark: '是否只读并可验证数据。', optional: true }
      ] },
      { name: 'isNormal()', remark: '获取表单状态是否为正常状态。' },
      { name: 'isDisabled()', remark: '获取表单状态是否为禁用。' },
      { name: 'isReadonly()', remark: '获取表单状态是否为只读。' },
      { name: 'isValidonly()', remark: '获取表单状态是否为只读并可验证数据。' },
      { name: 'status([status])', remark: '获取或设置表单状态。', param: [
        { name: 'status', type: 'String', remark: '传入此参数是设置表单状态。不传此参数是获取表单状态。可选值: <b>normal</b><s>(默认)</s>, <b>readonly</b><s>(只读，不验证数据)</s>, <b>validonly</b><s>(只读，验证数据)</s>, <b>disabled</b><s>(禁用)</s>。', optional: true }
      ] },
      { name: 'isModified([original])', remark: '检测表单是否有修改。', param: [
        { name: 'original', type: 'Boolean', remark: '设为true，检测表单是否有修改，对照参考的值为初始值。', optional: true }
      ] },
      { name: 'saveModified([original])', remark: '把当前的表单值设置默认值。', param: [
        { name: 'original', type: 'Boolean', remark: '设为true，检测表单是否有修改，对照参考的值为初始值。', optional: true }
      ] },
      { name: 'reset()', remark: '把表单的值重置为初始值。' },
      { name: 'val([value])', remark: '获取或设置值。', param: [
        { name: 'value', type: 'String', remark: '传入此参数是设置值。不传此参数是获取值。', optional: true }
      ], example: [
          function() {
            // 获取表单值
            var e = vm.f( 'myText' ).val();
          },
          function() {
            // 设置表单值
            vm.f( 'myText' ).val( 'new value' );
          }
      ] },
      { name: 'warn([cls])', remark: '给表单设置警告效果（默认效果是表单框变红色）。点击表单后警告样式自动消失。', param: [
        { name: 'cls', type: 'String | Boolean', remark: '字符串格式为样式名。如果设为true，显示默认的警告效果。如果设为false，取消默认的警告效果。', optional: true }
      ] }
    ],
    Classes: [
      { name: '.w-text', remark: '基础样式。' },
      { name: '.z-on', remark: '处于焦点状态时的样式。' },
      { name: '.z-required', remark: '必填状态的样式。' },
      { name: '.z-trans', remark: '设置 transparent:true 时的样式。' },
      { name: '.z-ds', remark: '设置 status 为 readonly,validonly,disabled 时的样式。' },
      { name: '.z-err', remark: '表单验证出错时的样式。' }
    ],
    Examples: [
	  { example: [
          function() {
          	//设置输入框为必填项
            return~
            {
                type: 'Form',
                nodes: [
                    {
                        type: 'Text',
                        name: 'usr',
                        label: { text: '用户名', width: 100 },
                        validate: { required: true }
                    },
                    {
                        type: 'Password',
                        name: 'pwd',
                        label: { text: '密码', width: 100 },
                        validate: { required: true }
                    },
                ]
            }
          }
      ] }
    ]
  },
  "Textarea": {
  	remark: '多行文本输入框。',
  	extend: 'Text',
  	deprecate: '.w-text',
    Classes: [
      { name: '.w-textarea', remark: '基础样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            return~
            {
                type: 'Form',
                nodes: [
                    {
                        type: 'Text',
                        name: 'title',
                        label: { text: '标题', width: 100 }
                    },
                    {
                        type: 'Textarea',
                        name: 'content',
                        label: { text: '内容', width: 100 }
                    },
                ]
            }
          }
      ] }
    ]
  },
  "Password": {
  	remark: '密码输入框。',
  	extend: 'Text',
  	deprecate: '.w-text',
    Config: [
      { name: 'autocomplete', type: 'Boolean', optional: true, remark: '是否允许自动填充保存的密码。默认值为false。' }
    ],
    Classes: [
      { name: '.w-password', remark: '基础样式。' }
    ],
    Examples: [
	  { example: [
          function() {
            return~
            {
                type: 'Form',
                nodes: [
                    {
                        type: 'Text',
                        name: 'usr',
                        label: { text: '用户名', width: 100 },
                        validate: { required: true }
                    },
                    {
                        type: 'Password',
                        name: 'pwd',
                        label: { text: '密码', width: 100 },
                        validate: { required: true }
                    },
                ]
            }
          }
      ] }
    ]
  },
  "CheckBoxGroup": {
  	title: 'CheckBoxGroup',
  	remark: '复选表单组。',
  	extend: 'Text',
  	deprecate: 'placeholder,tip,transparent,name,value,focus,focusEnd,.z-trans,.w-text,.w-input,status',
    Config: [
      { name: 'dir', type: 'String', remark: '排列方向。可选值: <b>h</b><s>(横向,默认)</s>,<b>v</b><s>(纵向)</s>' },
      { name: 'nodes', type: 'Array', remark: 'CheckBox 节点数组。' },
      { name: 'pub', type: 'Object', optional: true, remark: 'CheckBox 的默认参数。' },
      { name: 'value', type: 'String', optional: true, remark: '选中的值，用逗号隔开。' }
    ],
    Methods: [
      { name: 'checkAll([checked])', remark: '设置全选/不选。', param: [
        { name: 'checked', type: 'Boolean', remark: '是否可用。', optional: true }
      ] }
    ],
    Classes: [
      { name: '.w-checkboxgroup', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            // 设置 CheckBox 宽度为 50%, 可以形成整齐的两列排列
            return~
            {
                type: 'CheckBoxGroup',
                pub: { name: 'box', width: '50%' },
                nodes: [
                    { text: '选项1' },
                    { text: '选项2' },
                    { text: '选项3' },
                    { text: '选项4' }
                ]
            }
          },
          function() {
            // CheckBox 和其他表单的组合
            return~
            {
                type: 'CheckBoxGroup',
                nodes: [
                    { text: '选项1', target: { type: 'Date' } },
                    { text: '选项2', target: { type: 'Spinner' } },
                    { text: '选项3', target: { type: 'Text' } }
                ]
            }
          }
        ]
      }
    ]
  },
  "CheckBox": {
  	remark: '复选项。',
  	extend: 'Text',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.w-input,.z-trans,.z-on',
    Config: [
      { name: 'bubble', type: 'Boolean', remark: '点击事件是否冒泡。用于 leaf 或 tr 的选项box。', optional: true },
      { name: 'checked', type: 'Boolean', remark: '是否选中。', optional: true },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'br', type: 'Boolean', remark: '是否换行。默认值为false。', optional: true },
      { name: 'sync', type: 'String', remark: '选中状态跟父节点同步，用于 leaf 或 tr 的选项box。可选值: <b>click</b><s>(点击父节点，box也触发点击)</s>, <b>focus</b><s>(父节点聚焦则box则选中，父节点失去焦点则box未选中)</s>', optional: true },
      { name: 'target', type: 'String | Widget', remark: 'widget 或 widget ID。使这个 widget 和当前 option 的 disabled 状态同步。', optional: true },
      { name: 'text', type: 'String', remark: '文本。', optional: true },
      { name: 'value', type: 'String', remark: '值。' }
    ],
    Methods: [
      { name: 'check([checked])', remark: '设置为选中/不选中。', param: [
        { name: 'checked', type: 'Boolean', remark: '是否可用。', optional: true }
      ] },
      { name: 'isChecked()', remark: '获取是否选中状态。返回true/false。' },
      { name: 'checkAll([checked])', remark: '设置全选/不选。', param: [
        { name: 'checked', type: 'Boolean', remark: '是否可用。', optional: true }
      ] },
      { name: 'getSiblings([checked])', remark: '获取所有相同name的兄弟节点，返回一个数组。', param: [
        { name: 'checked', type: 'Boolean', remark: '设为 true，获取所有选中的同名节点；设为 false，获取所有未选的同名节点。不设此参数，获取所有同名节点。', optional: true }
      ] }
    ],
    Classes: [
      { name: '.w-checkbox', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'CheckBoxGroup',
                nodes: [
                    { type: 'CheckBox', value: '1', text: '选项1', checked: true }, //type属性可以省略
                    { type: 'CheckBox', value: '2', text: '选项2' },
                    { type: 'CheckBox', value: '3', text: '选项3' }
                ]
            }
          }
        ]
      }
    ]
  },
  "TripleBox": {
   	remark: '有三种状态的复选项。',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.z-trans',
  	extend: 'CheckBox',
    Config: [
      { name: 'checkAll', type: 'Boolean', remark: '设为 true 时，点击它可以全选/全不选其他相同name的triplebox。一组同name的TripleBox中只能有一个设置checkAll参数。' },
      { name: 'checked', type: 'Boolean | String', remark: '选中状态。可选值: <b>true</b><s>(选中)</s>, <b>false</b><s>(未选)</s>, <b>checked</b><s>(选中)</s>, <b>unchecked</b><s>(未选)</s>, <b>partial</b><s>(半选)</s>。默认值为false' },
      { name: 'partialsubmit', type: 'Boolean', remark: '设为 true 时，半选状态也会提交数据。' }
    ],
    Methods: [
      { name: 'check([checked])', remark: '设置选中状态。', param: [
        { name: 'checked', type: 'Boolean | String', remark: '选中状态。可选值: <b>true</b><s>(选中)</s>, <b>false</b><s>(未选)</s>, <b>checked</b><s>(选中)</s>, <b>unchecked</b><s>(未选)</s>, <b>partial</b><s>(半选)</s>', optional: true }
      ] },
      { name: 'isPartial()', remark: '是否半选中状态。' }
    ],
    Examples: [
	  { example: [
          function() {
          	//带有TripleBox的树
            return~
            {
                type: 'Tree',
                nodes: [
                    {
                        text: '我的收藏', 
                        box: { type: 'TripleBox', name: 'box1', value: '1' },
                        expanded: true, 
                        nodes: [
                            { text: '收藏1', box: { type: 'TripleBox', name: 'box2', value: '2' } },
                            { text: '收藏2', box: { type: 'TripleBox', name: 'box3', value: '3' } }
                        ]
                    }
                ]
            }
          }
      ] }
    ]
  },
  "Switch": {
  	remark: '开关选项。',
  	extend: 'CheckBox',
  	deprecate: 'focus,focusEnd,placeholder,transparent,bubble,br,text,.w-text,.z-trans',
    Config: [
      { name: 'checkedText', type: 'String', remark: '选中状态时的文本。' },
      { name: 'uncheckedText', type: 'String', remark: '未选中状态时的文本。' }
    ],
    Methods: [
      { name: 'check([checked])', remark: '设置选中状态。', param: [
        { name: 'checked', type: 'Number', remark: '选中状态。可选值: <b>0</b><s>(未选)</s>，<b>1</b><s>(选中)</s>，<b>2</b><s>(半选)</s>', optional: true }
      ] }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'Switch',
                name: 'enable',
                label: { text: '启用' }
            }
          }
        ]
      }
    ]
  },
  "RadioGroup": {
  	remark: '单选表单组。',
  	extend: 'CheckBoxGroup',
  	deprecate: 'placeholder,tip,transparent,value,focus,focusEnd,.z-trans,.w-text,.w-input,status',
    Config: [
      { name: 'nodes', type: 'Array', remark: 'Radio 节点数组。' },
      { name: 'pub', type: 'Object', optional: true, remark: 'Radio 的默认参数。' },
    ],
    Classes: [
      { name: '.w-radiogroup', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'RadioGroup',
                nodes: [
                    { type: 'Radio', value: '1', text: '选项1', checked: true }, //type属性可以省略
                    { type: 'Radio', value: '2', text: '选项2' },
                    { type: 'Radio', value: '3', text: '选项3' }
                ]
            }
          }
        ]
      }
    ]
  },
  "Radio": {
  	remark: '单选项。',
  	extend: 'CheckBox',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.w-input,.z-trans,.z-on',
    Classes: [
      { name: '.w-radio', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'RadioGroup',
                nodes: [
                    { type: 'Radio', value: '1', text: '选项1', checked: true }, //type属性可以省略
                    { type: 'Radio', value: '2', text: '选项2' },
                    { type: 'Radio', value: '3', text: '选项3' }
                ]
            }
          }
        ]
      }
    ]
  },
  "Select": {
  	remark: '下拉选择表单。',
  	deprecate: 'focusEnd,placeholder,transparent,.w-text,.z-trans',
  	extend: 'Text',
    Config: [
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'nodes', type: 'Array', remark: '下拉选项数组。<br>单个选项的配置参数如下:', param: [
      	{ name: 'checked', type: 'Boolean', optional: true, remark: '是否选中。' },
      	{ name: 'text', type: 'String', remark: '文本。' },
      	{ name: 'value', type: 'String', remark: '值。' }
      ] }
	],
    Methods: [
      { name: 'getFocusOption()', remark: '获取当前的option选项对象。' },
      { name: 'getPrevOption()', remark: '获取上一个option选项对象。' },
      { name: 'getNextOption()', remark: '获取下一个option选项对象。' }
    ],
    Classes: [
      { name: '.w-select', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'Select',
                nodes: [
                    { value: '001', text: '选项1', checked: true },
                    { value: '002', text: '选项2' },
                    { value: '003', text: '选项3' }
                ]
            }
          }
        ]
      }
    ]
  },
  "DatePicker": {
  	remark: '日期选择表单。',
  	extend: 'Text',
  	deprecate: '.w-text',
    Config: [
      { name: 'format', type: 'String', remark: '日期格式: yyyy-mm-dd hh:ii:ss' },
      { name: 'multiple', type: 'Boolean', remark: '是否多选模式。' },
      { name: 'noButton', type: 'Boolean', optional: true, remark: '设置为true，不显示按钮。' }
    ],
    Classes: [
      { name: '.w-datepicker', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
          	//日期选择器
            return~
            { type: 'DatePicker', format: 'yyyy-mm-dd' }
          },
          function() {
          	//可选择小时分钟的日期选择器
            return~
            { type: 'DatePicker', format: 'yyyy-mm-dd hh:ii' }
          }
        ]
      }
    ]
  },
  "Hidden": {
  	remark: '隐藏表单。',
  	extend: 'Text',
  	deprecate: 'label,focus,focusEnd,placeholder,tip,transparent,status,validate,validateGroup,.w-text,.z-trans,.z-on,.z-required,.z-err,.z-ds',
    Classes: [
      { name: '.w-hidden', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在Form中使用Hidden
            return~
            {
                type: 'Form',
                nodes: [
                    {
                        type: 'FormLabel',
                        label: { text: 'FormLabel', width: 90 },
                        text: '内容'
                    }
                ],
                Hiddens: [
                    { type: 'Hidden', name: 'newId', value: '1' }
                ]
            }
          }
      ] }
    ]
  },
  "Rate": {
  	remark: '评分表单。',
  	extend: 'Text',
  	deprecate: 'focus,focusEnd,.w-text,.z-trans,placeholder,tip,transparent',
    Config: [
      { name: 'value', type: 'String', remark: '表单值。从0 - 10。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'Rate',
                name: 'rate',
                label: { text: '评分' }
            }
          }
        ]
      }
    ]
  },
  "Range": {
  	remark: '指定范围的表单组合。',
  	extend: 'Widget',
    Config: [
      { name: 'label', type: 'String | LabelWidget', optional: true, remark: '表单标签。<br>当设为 labelWidget 并有宽度时，将在表单左边显示标签内容。',
      	 example: [
          function() {
            // 显示标签的表单
            return~
            { type: 'Text', label: { text: '姓名', width: 100 } }
          },
          function() {
            // 在css中设置标签背景色
            return''
            '.w-label ._bg{background:#ddd}'
          }
      ] },
      { name: 'begin', type: 'Widget', remark: '开始值的表单。' },
      { name: 'end', type: 'Widget', remark: '结束值的表单。' },
      { name: 'to', type: 'Widget', remark: '中间的连接显示。一般是一个html widget。' }
    ],
    Examples: [
      { example: [
          function() {
          	// 选择开始日期和结束日期
            return~
            {
                type: 'Range',
                label: { text: '日期' },
                begin: { type: 'DatePicker', format: 'yyyy-mm-dd', label: { text: '开始日期' } },
                end: { type: 'DatePicker', format: 'yyyy-mm-dd', label: { text: '结束日期' } }
            }
          }
        ]
      }
    ]
  },
  "Slider": {
  	remark: '滑块。',
  	extend: 'Text',
  	deprecate: '.w-text,.z-trans,placeholder,transparent,focus,focusEnd,warn,change',
    Config: [
      { name: 'tip', type: 'Boolean | String', optional: true, remark: '拖动滑块时显示的tip。支持变量 <b>$value</b><s>(值)</s>。' }
    ],
    Event: [
      { name: 'dragStart', remark: '拖动开始时触发。' },
      { name: 'drag', remark: '拖动时触发。' },
      { name: 'drop', remark: '结束拖动时触发。' }
    ],
    Classes: [
      { name: '.w-slider', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'Slider',
                label: { text: '滑块' }
            }
          }
        ]
      }
    ]
  },
  "Jigsaw": {
  	remark: '滑块拼图。',
  	extend: 'Slider',
    Config: [
      { name: 'img', type: 'String | JigsawImg', remark: '获取拼图的图片地址，或拼图图片对象。如果是地址，通过Ajax访问该地址应返回一个JigsawImg对象。' },
      { name: 'auth', type: 'String | JigsawAuth', remark: '验证拼图是否正确的地址，或拼图验证对象。如果是地址，通过Ajax访问该地址应返回一个JigsawAuth对象。地址支持变量 <b>$response</b><s>(值)</s> 和 <b>$token</b><s>(jigsawImg的token)</s>。' }
    ],
    Event: [
      { name: 'auth', remark: '验证完成后触发。' },
      { name: 'drag', remark: '拖动滑块时触发。' },
      { name: 'drop', remark: '结束拖动时触发。' }
    ],
    Methods: [
      { name: 'isSuccess()', remark: '验证是否成功。' }
    ],
    Classes: [
      { name: '.w-slider', remark: '基础样式。' },
      { name: '.w-sliderjigsaw', remark: '基础样式。' },
      { name: '.z-authing', remark: '正在验证时的样式。' },
      { name: '.z-drag', remark: '拖动时的样式。' },
      { name: '.z-success', remark: '验证成功的样式。' }
    ],
    Examples: [
      { example: [
          function() {
          	//例1
            return~
            {
                type: 'Jigsaw',
                label: { text: '验证码' },
                img: { type: 'JigsawImg', src: 'jigsawimg.sp' },
                auth: { type: 'JigsawAuth', src: 'jigsawauth.sp?value=$value' }
            }
          },
          function() {
          	//例1 jigsawimg.sp 地址返回的数据格式范例
            return~
            {
                type: 'JigsawImg',
                src: 'jigsawimg.sp',
                result: {
                    big: { src: 'img/big.png', width: 400, height: 200 },
                    small: { src: 'img/small.png', width: 64, height: 200 }
                }
            }
          },
          function() {
          	//例1 jigsawauth.sp 地址返回的数据格式范例
            return~
            {
                type: 'JigsawAuth',
                src: 'jigsawauth.sp?value=$value',
                result: {
                    success: true,
                    msg: '验证通过'
                }
            }
          }
        ]
      }
    ]
  },
  "JigsawImg": {
  	remark: '拼图的图片对象。',
    Config: [
      { name: 'src', type: 'String', remark: '获取图片信息的地址。' },
      { name: 'result', type: 'Object', remark: '设置分隔格式。', param: [
        { name: 'big', type: 'Object', remark: '大图。', param: [
          { name: 'src', type: 'String', remark: '图片地址。' },
          { name: 'height', type: 'Number', remark: '图片高度。' },
          { name: 'width', type: 'Number', remark: '图片宽度。' }
        ] },
        { name: 'error', type: 'Object', remark: '错误信息。', param: [
          { name: 'msg', type: 'String', remark: '错误描述文本。' },
          { name: 'timeout', type: 'Number', remark: '锁定并倒数时间，结束后自动刷新。单位:毫秒。' }
        ] },
        { name: 'maxValue', type: 'Number', remark: '最大值。' },
        { name: 'minValue', type: 'Number', remark: '最小值。' },
        { name: 'small', type: 'Object', remark: '小图。', param: [
          { name: 'src', type: 'String', remark: '图片地址。' },
          { name: 'height', type: 'Number', remark: '图片高度。' },
          { name: 'width', type: 'Number', remark: '图片宽度。' }
        ] },
        { name: 'token', type: 'String', remark: '标识字串。' }
      ] }
    ],
	Examples: [
	  { example: [
          function() {
            // 正常返回的范例
            return~
            {
              type: 'JigsawImg',
              src: 'getimg.sp',
              result: {
      	          big: {
                      src: 'big.jpg', width: 200, height: 90
                  },
                  small: {
                      src: 'small.jpg', width: 200, height: 90
                  },
                minvalue: 0,
                maxvalue: 300,
                token: 'abc'
              }
            }
          },
          function() {
            // 发生错误时的范例
            return~
            {
              type: 'JigsawImg',
              src: 'getimg.sp',
              result: {
      	          error: {
                      msg: '次数过多，请稍候再试', timeout: 15
                  }
              }
            }
          }
      ] }
    ]
  },
  "JigsawAuth": {
  	remark: '拼图的验证对象。',
    Config: [
      { name: 'src', type: 'String', remark: '验证地址。' },
      { name: 'result', type: 'Object', remark: '设置分隔格式。', param: [
        { name: 'success', type: 'Boolean', remark: '验证是否成功。' },
        { name: 'msg', type: 'String', remark: '验证结果的描述信息。' }
      ] }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            {
              type: 'JigsawAuth',
              src: 'auth.sp?value=$value',
              result: {
      	          success: true,
                  msg: '验证通过'
              }
            }
          }
      ] }
    ]
  },
  "Spinner": {
  	remark: '数字输入框。',
  	extend: 'Text',
  	deprecate: '.w-text',
    Config: [
      { name: 'decimal', type: 'Number', optional: true, remark: '设为0时，只允许输入整数。设为正整数，则限制小数的最大位数。设为负数，则不限整数和小数。默认值为0' },
      { name: 'step', type: 'Number', optional: true, remark: '递增/递减的数值。' },
      { name: 'noButton', type: 'Boolean', optional: true, remark: '设置为true，不显示按钮。' },
      { name: 'format', type: 'Object', optional: true, remark: '设置分隔格式。',  param: [
        { name: 'length', type: 'Number', remark: '分隔长度。默认值为 3' },
        { name: 'separator', type: 'String', remark: '分隔符。默认值为 ","' },
        { name: 'rightward', type: 'Boolean', remark: '设置为true，从左向右的方向进行分隔。默认值为 false' }        
      ] }
    ],
    Classes: [
      { name: '.w-spinner', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	// 设置最大值和最小值
            return~
            {
              type: 'Spinner',
              name: 'count',
              label: { text: '数字框' },
              validate: { minValue: 0, maxValue: 100 }
            }
          },
          function() {
          	// 千分位格式化
            return~
            {
              type: 'Spinner',
              name: 'count',
              label: { text: '数字框' },
              format: { separator: ',', length: 3 }
            }
          }
      ] }
    ]
  },
  "DropBox": {
  	remark: '下拉选择表单。',
  	extend: 'Text',
  	deprecate: 'focus,.w-text',
    Config: [
      { name: 'cancelable', type: 'Boolean', remark: '设置为true，可取消当前选中的选项，并且不会默认选中第一项。该参数仅在单选模式下有效。默认值为false。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'multiple', type: 'Boolean', remark: '是否多选模式。' },
      { name: 'src', type: 'String | Object',  remark: '获取选项的 URL 地址。' },
      { name: 'template', type: 'String', remark: '模板地址。' },
      { name: 'nodes', type: 'Array', remark: '下拉选项数组。<br>单个选项的配置参数如下:', param: [
      	{ name: 'checked', type: 'Boolean', optional: true, remark: '是否选中。' },
      	{ name: 'icon', type: 'String', optional: true, remark: '图标。可以是图片地址，或以 "." 开头的样式名。' },
      	{ name: 'text', type: 'String', remark: '文本。' },
      	{ name: 'value', type: 'String', remark: '值。' }
      ] }
    ],
    Event: [
      { name: 'beforeChange', remark: '在点击选项，即将改变值之前触发。可以用 return false 来取消事件。支持变量 <b>$0</b><s>(点击选项的值)</s>。' }
    ],
    Methods: [
      { name: 'getLength()', remark: '获取选项总数。' },
      { name: 'getFocusOption()', remark: '获取当前的选项对象。', example: [
          function() {
            // 获取选中项的文本
            var text = this.getFocusOption().text;
          }
      ] },
      { name: 'getPrevOption()', remark: '获取上一个选项对象。' },
      { name: 'getNextOption()', remark: '获取下一个选项对象。' },
      { name: 'setOptions(opt, [index])', remark: '设置下拉选项。', param: [
        { name: 'opt', type: 'Array | Option', remark: '选项数组或单个选项。' },
        { name: 'index', type: 'Number', remark: '选项序号。可替换指定的选项。', optional: true }
      ], example: [
          function() {
            // 替换所有的选项
            xbox.setOptions( [ { value: 1, text: '选项1' }, { value: 2, text: '选项2' } ] );
          },
          function() {
            // 替换第一个选项
            xbox.setOptions( { value: 1, text: '选项1' }, 0 );
          }
      ] },
      { name: 'addOption(opt, [index])', remark: '追加一个选项。', param: [
        { name: 'opt', type: 'Array | Option', remark: '选项数组或单个选项。' },
        { name: 'index', type: 'Number', remark: '追加的序号。如果不设置此参数，默认追加到末尾。', optional: true }
      ], example: [
          function() {
            // 在末尾追加一个选项
            xbox.addOption( { value: 1, text: '选项1' } );
          }
      ] },
      { name: 'removeOption([index])', remark: '删除一个选项。', param: [
        { name: 'index', type: 'Number', remark: '删除选项的序号。如果不设置此参数，默认删除末尾的选项。', optional: true }
      ], example: [
          function() {
            // 删除第一个选项
            xbox.removeOption( 0 );
          }
      ] }
    ],
    Classes: [
      { name: '.w-xbox', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'DropBox',
                nodes: [
                    { value: '001', text: '选项1', checked: true },
                    { value: '002', text: '选项2' },
                    { value: '003', text: '选项3' }
                ]
            }
          }
        ]
      }
    ]
  },
  "ImgBox": {
  	remark: '图片下拉选择表单。',
  	extend: 'DropBox',
  	deprecate: 'focus,placeholder,tip,transparent,cancelable,multiple,format,src,.w-text,.z-trans',
    Config: [
      { name: 'imgWidth', type: 'Number', remark: '图标宽度。' },
      { name: 'imgHeight', type: 'Number', remark: '图标高度。' },
      { name: 'nodes', type: 'Array', remark: '下拉选项数组。', param: [
      	{ name: 'icon', type: 'String', remark: '图标。' },
      	{ name: 'text', type: 'String', remark: '文本。', optional: true },
      	{ name: 'value', type: 'String', remark: '值。' }
      ] }
    ],
    Methods: [
      { name: 'getFocusOption()', remark: '获取当前的选项对象。' },
      { name: 'getPrevOption()', remark: '获取上一个选项对象。' },
      { name: 'getNextOption()', remark: '获取下一个选项对象。' },
      { name: 'setOptions(opt)', remark: '设置下拉选项。', param: [
        { name: 'opt', type: 'Array', remark: '选项数组。' }
      ] }
    ],
    Classes: [
      { name: '.w-imgbox', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
            return~
            {
                type: 'ImgBox',
                name: 'fileType',
                imgWidth: 100,
                imgHeight: 100,
                nodes: [
                    { icon: '.i-doc', value: '001', text: 'Word', checked: true },
                    { icon: '.i-ppt', value: '002', text: 'PowerPoint' },
                    { icon: '.i-xls', value: '003', text: 'Excel' }
                ]
            }
          }
        ]
      }
    ]
  },
  "PickBox": {
  	remark: '选择框。',
  	extend: 'Text',
  	deprecate: '.w-text',
    Config: [
      { name: 'drop', type: 'Dialog', remark: '显示所有选项的下拉对话框。' },
      { name: 'text', type: 'String', remark: '显示文本。如果有设置value而text为空，将会尝试自动从drop中匹配文本。' },
      { name: 'picker', type: 'Object', remark: 'dialog 参数。其中 dialog 的 src 支持变量 <b>$value</b><s>(值)</s> 和 <b>$text</b><s>(文本)</s>。' }
    ],
    Methods: [
      { name: 'pick()', remark: '弹出对话框选择器。' },
      { name: 'val([value], [text])', remark: '设置/读取值。', param: [
        { name: 'value', type: 'String', remark: '值。', optional: true },
        { name: 'text', type: 'String', remark: '文本。', optional: true }
      ], example: [
          function() {
            // 在打开的对话框中点击"确定"按钮执行以下代码，返回值给 pickbox。
            $.dialog( this ).commander.val( '001', '选项一' );
            $.dialog( this ).close();
          }
      ] }
    ],
    Classes: [
      { name: '.w-pickbox', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
          	//树形候选项
            return~
            {
                type: 'PickBox',
                name: 'address',
                label: { text: '住址', width: 100 },
                bind: {
                    field: { value: 'code', text: 'text' }
                },
                drop: {
                    type: 'Dialog',
                    node: {
                        type: 'View',
                        node: {
                            type: 'Tree',
                            nodes: [
                                {
                                    text: '福州市',
                                    data: { code: '350000' },
                                    nodes: [
                                        { text: '鼓楼区', data: { code: '350001' } },
                                        { text: '晋安区', data: { code: '350002' } }
                                    ]
                                },
                                {
                                    text: '厦门市',
                                    data: { code: '360000' },
                                    nodes: [
                                        { text: '思明区', data: { code: '360001' } },
                                        { text: '海沧区', data: { code: '360002' } }
                                    ]
                                }
                            ]
                        }
                    }
                }
            }
          }
        ]
      }
    ]
  },
  "OnlineBox": {
  	remark: '在线匹配关键词，并显示推荐列表。',
  	extend: 'Text',
  	deprecate: '.w-text',
    Config: [
      { name: 'delay', type: 'Number', remark: '输入字符时的延迟查询时间。单位:毫秒。' },
      { name: 'drop', type: 'Dialog', remark: '显示所有选项的下拉对话框。' },
      { name: 'multiple', type: 'Boolean', remark: '多选模式。' },
      { name: 'picker', type: 'Object', remark: 'dialog 参数。其中 dialog 的 src 支持变量 <b>$value</b><s>(值)</s> 和 <b>$text</b><s>(文本)</s>。' },
      { name: 'separator', type: 'String', remark: '文本选项分隔符。默认是逗号。' },
      { name: 'suggest', type: 'Dialog', remark: '根据输入文本显示一个候选项提示框。src参数支持变量 <b>$value</b><s>(值)</s> 和 <b>$text</b><s>(文本)</s>。' }
    ],
    Methods: [
      { name: 'search(text)', remark: '根据关键词弹出对话框选择器。' },
      { name: 'complete(obj)', remark: '返回完整的结果给 onlinebox，完成正在输入的文本。用于选择器中设置了 combofield 的 table 或 tree。', param: [
        { name: 'obj', type: 'Object', remark: 'tr 或 leaf 类型的 widget。' }
      ], example: [
          function() {
            // 给 table 绑一个点击事件，返回结果
            return~
            { type: 'Table', combofield: { value: 'C0', text: 'C1' }, pub: { on: { click: '$.dialog(this).commander.complete(this)' } } };
          }
      ] }
    ],
    Classes: [
      { name: '.w-onlinebox', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
             return~
            {
                type: 'OnlineBox',
                bind: {
                    field: { value: 'id', text: 'name' }
                },
                suggest: {
                    type: 'Dialog',
                    src: 'search.sp?text=$text'
                }
            }
          }
        ]
      }
    ]
  },
  "ComboBox": {
  	remark: '可输入的有下拉选项的表单。',
  	extend: 'Text',
  	deprecate: 'tip,.w-text',
    Config: [
      { name: 'bind', type: 'Object', remark: '设置数据来源的参数。', param: [
      	{ name: 'field', type: 'String', remark: '字段参数。', param: [
          { name: 'forbid', type: 'String', remark: '禁用字段名。' },
          { name: 'remark', type: 'String', remark: '备注字段名。' },
          { name: 'search', type: 'String', remark: '搜索字段名。' },
          { name: 'text', type: 'String', remark: '文本字段名。' },
          { name: 'value', type: 'String', remark: '值字段名。' }
        ] },
        { name: 'fullPath', type: 'Boolean', remark: '设置为true，选中项的文本显示完整的路径。' },
        { name: 'keepShow', type: 'Boolean', remark: '设置为true，无论是否有匹配到内容，都始终显示搜索结果框。' },
        { name: 'target', type: 'String', remark: '数据来源Widget的ID。类型可以是Table或Tree。如果不设置此参数，将以对话框内的第一个Table或第一个Tree作为数据来源。' }
      ] },
      { name: 'delay', type: 'Number', remark: '输入字符时的延迟查询时间。单位:毫秒。' },
      { name: 'drop', type: 'Dialog', remark: '显示所有选项的下拉对话框。' },
      { name: 'face', type: 'String', remark: '设置已选项的外观效果。可选值: <b>default</b>, <b>tag</b>' },
      { name: 'multiple', type: 'Boolean', remark: '是否多选模式。' },
      { name: 'loadingText', type: 'String', remark: '加载数据时显示的文本。' },
      { name: 'suggest', type: 'Dialog', remark: '根据输入文本显示一个候选项提示框。src参数支持变量 <b>$value</b><s>(值)</s> 和 <b>$text</b><s>(文本)</s>。' },
      { name: 'br', type: 'Boolean', remark: '已选项是否换行。' },
      { name: 'picker', type: 'Object', remark: '选择器 dialog 参数。dialog 的 src 支持变量 <b>$value</b><s>(值)</s> 和 <b>$text</b><s>(文本)</s>。' },
      { name: 'pub', type: 'Object', remark: '用于 combobox/option 的默认参数。', example: [
          function() {
            // 设置每个已选项的宽度为 100，并绑定点击事件，显示选项的值
            return~
            { type: 'ComboBox', pub: { width: 100, on: { click: 'alert(this.x.value)' } } };
          }
      ] },
      { name: 'strict', type: 'Boolean', remark: '设为 true，如果存在没有匹配成功的选项，则不能通过表单验证。设为false，允许存在没有匹配成功的选项。默认值是true。' },
      { name: 'text', type: 'String', remark: '初始化时显示的文本。如果设置了此参数，就要和 value 值一一对应。一般只设置 value 就可以，仅当 src 是 tree 模式的数据岛，并且 value 在 tree 的初始数据中匹配不到时才需要定义 text。' },
      { name: 'value', type: 'String', remark: '表单值。多个用逗号隔开。' }
    ],
    Methods: [
      { name: 'suggest(text)', remark: '根据关键词弹出对话框选择器。' },
      { name: 'complete(obj)', remark: '返回完整的结果给 combobox，完成正在输入的文本。用于选择器中设置了 combofield 的 table 或 tree。', param: [
        { name: 'obj', type: 'Object', remark: 'tr 或 leaf 类型的 widget。' }
      ], example: [
          function() {
            // 给 table 绑一个点击事件，返回结果
            return~
            { type: 'Table', combofield: { value: 'C0', text: 'C1' }, pub: { on: { click: '$.dialog(this).commander.complete(this)' } } };
          }
      ] },
      { name: 'resetOptions()', remark: '重置选项。' },
      { name: 'text()', remark: '获取文本。' },
      { name: 'queryText()', remark: '获取正在输入的文本。' }
    ],
    Classes: [
      { name: '.w-combobox', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
          	//下例中输入"张"字，将出现两个候选项
            return~
            {
                type: 'ComboBox',
                bind: {
                    field: { value: 'id', text: 'name' }
                },
                suggest: {
                    type: 'Dialog',
                    node: {
                        type: 'View',
                        node: {
                            type: 'Table',
                            columns: [
                                { field: 'name', width: '*' }
                            ],
                            tBody: {
                                nodes: [
                                    { id: '001', name: '张飞' },
                                    { id: '002', name: '赵云' },
                                    { id: '003', name: '张角' }
                                ]
                            }
                        }
                    }
                }
            }
          }
        ]
      }
    ]
  },
  "ComboBoxOption": {
  	remark: 'ComboBox 的已选项。它由 ComboBox 自动生成，没有显式定义。可以通过 ComboBox 的 pub 属性来设置它的参数。',
  	extend: 'Widget',
    Config: [
      { name: 'value', type: 'String', remark: '值。' },
      { name: 'text', type: 'String', remark: '文本。' },
      { name: 'data', type: 'JSON', remark: 'tr 或 leaf 的 data。' }
    ],
    Methods: [
      { name: 'close()', remark: '删除。' },
      { name: 'val()', remark: '获取值。' },
      { name: 'text()', remark: '获取文本。' }
    ],
    Examples: [
      { example: [
          function() {
          	//给选中的项绑定点击事件
            return~
            {
                type: 'ComboBox',
                pub: {
                    on: { click: 'alert(this.val())' }
                },
                bind: {
                    field: { value: 'id', text: 'name' }
                },
                suggest: {
                    type: 'Dialog',
                    node: {
                        type: 'View',
                        node: {
                            type: 'Table',
                            columns: [
                                { field: 'name', width: '*' }
                            ],
                            tBody: {
                                nodes: [
                                    { id: '001', name: '张飞' },
                                    { id: '002', name: '赵云' },
                                    { id: '003', name: '张角' }
                                ]
                            }
                        }
                    }
                }
            }
          }
        ]
      }
    ]
  },
  "LinkBox": {
  	remark: '可输入的有下拉选项的表单。如果设置了dblclick事件，已选项的样式为带下划线的链接。',
  	extend: 'ComboBox',
  	deprecate: 'pub,face,.w-text,.w-combobox',
    Config: [
      { name: 'strict', type: 'Boolean', remark: '设为 true，如果存在没有匹配成功的选项，则不能通过表单验证。设为false，允许存在没有匹配成功的选项。默认值是false。' },
      { name: 'separator', type: 'String', remark: '文本选项分隔符。默认是逗号。' }
    ],
    Classes: [
      { name: '.w-linkbox', remark: '基础样式。' }
    ],
    Examples: [
      { example: [
          function() {
          	//下例中输入"张"字，将出现两个候选项
            return~
            {
                type: 'LinkBox',
                bind: {
                    field: { value: 'id', text: 'name' }
                },
                suggest: {
                    type: 'Dialog',
                    node: {
                        type: 'View',
                        node: {
                            type: 'Table',
                            columns: [
                                { field: 'name', width: '*' }
                            ],
                            tBody: {
                                nodes: [
                                    { id: '001', name: '张飞' },
                                    { id: '002', name: '赵云' },
                                    { id: '003', name: '张角' }
                                ]
                            }
                        }
                    }
                }
            }
          }
        ]
      }
    ]
  },
  "ImageUpload": {
  	remark: '上传图片。',
  	extend: 'Text',
  	deprecate: 'focus,focusEnd,placeholder,tip,transparent,.w-text,.z-trans,.z-on',
    Config: [
      { name: 'dir', type: 'String', remark: '附件排列方向。可选值: <b>h</b><s>(横向,默认)</s>, <b>v</b><s>(纵向)</s>' },
      { name: 'download', type: 'String', remark: '下载地址。支持 $xxx 变量(对应变量值取自 json 格式的 value)。' },
      { name: 'fileTypes', type: 'String', remark: '允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"' },
      { name: 'preview', type: 'String', remark: '预览地址。支持 $xxx 变量(对应变量值取自 json 格式的 value)。' },
      //{ name: 'removesrc', type: 'String', remark: '删除附件的地址。支持 $xxx 变量(对应变量值取自 json 格式的 value)。' },
      { name: 'maxFileSize', type: 'String', remark: '单个附件最大的文件大小。如 "50M"。' },
      { name: 'minFileSize', type: 'String', remark: '单个附件最小的文件大小。如 "1B"。' },
      { name: 'thumbnail', type: 'String', remark: '缩略图地址。支持 $xxx 变量(对应变量值取自 json 格式的 value)。' },
      { name: 'uploadButtons', type: 'Array', remark: '上传按钮的数组。' },
      { name: 'uploadLimit', type: 'Number', remark: '最多可上传数量。' },
      { name: 'post', type: 'String', remark: '上传地址。<br>上传成功返回JSON格式: { "id": "ID", "name": "名称", "size": "字节数", "url": "地址", "thumbnail": "缩略图地址" } <s>//id 和 name 必填</s><br>上传失败返回JSON格式: { "error": true, "text": "失败原因" }' },
      { name: 'valueButtons', type: 'Array', remark: '附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。' },
      { name: 'value', type: 'String | Array', remark: '值。' }
    ],
    Examples: [
      { example: [
          function() {
            // 图片上传控件
            return~
            {
                type: 'ImageUpload',
                post: { type: 'UploadPost', src: 'upload.sp' },
                pub: { width: 70, height: 70 },
                fileTypes: '*.png;*.jpg;*.gif',
                uploadButtons: [
                    { type: 'UploadButton', text: '上传照片' }
                ],
                value: [
                    { id: '001', name: '001.jpg', thumbnail: 'images/001.jpg' }
                ]
            }
          },
          function() {
          	// 上例中的 upload.sp 处理附件数据后返回的内容示例
            return~
            {
                type: 'UploadPost',
                result: {
                    id: '002', name: '002.jpg', thumbnail: 'images/002.jpg'
                }
            }
          }
        ]
      }
    ]
  },
  "FileUpload": {
  	remark: '上传附件。',
  	extend: 'ImageUpload',
  	deprecate: 'thumbnail',
    Examples: [
      { example: [
          function() {
            // 文件上传控件
            return~
            {
                type: 'FileUpload',
                post: { type: 'UploadPost', src: 'upload.sp' },
                uploadButtons: [
                    { type: 'UploadButton', text: '选择文件' }
                ],
                value: [
                    { id: '001', name: '001.doc' }
                ]
            }
          },
          function() {
          	// 上例中的 upload.sp 处理附件数据后返回的内容示例
            return~
            {
                type: 'UploadPost',
                result: {
                    id: '002', name: '002.doc'
                }
            }
          }
        ]
      }
    ]
  },
  "UploadButton": {
  	remark: '上传按钮。FileUpload 和 ImageUpload 的专用按钮。',
  	extend: 'Button',
  	deprecate: 'target',
    Examples: [
      { example: [
          function() {
            // 文件上传控件
            return~
            {
                type: 'FileUpload',
                post: { type: 'UploadPost', src: 'upload.sp' },
                uploadButtons: [
                    { type: 'UploadButton', text: '选择文件' }
                ]
            }
          }
        ]
      }
    ]
  },
  "Cmd": {
  	remark: '命令集合。',
    Config: [
      { name: 'delay', type: 'Number', remark: '延迟执行。单位:毫秒。' },
      { name: 'nodes', type: 'Array', remark: '命令集合的数组。' },
      { name: 'path', type: 'String', remark: '指定一个 view path，当前的命令集由这个 view 作为执行主体。' },
      { name: 'target', type: 'String', remark: '指定一个 widget id，当前的命令集由这个 widget 作为执行主体。path 和 target 同时指定时，相当于 VM( path ).find( target ).cmd( args ); ' }
    ],
	Examples: [
	  { example: [
          function() {
          	//执行一个命令集
            return~
            vm.cmd( {
              type: 'Cmd',
              nodes: [
                { type: 'JS', text: 'alert("one")' },
                { type: 'JS', text: 'alert("two")' }
              ]
            } );
          }
      ] }
    ]
  },
  "JS": {
  	remark: 'js命令。',
    Config: [
      { name: 'text', type: 'String', remark: 'js语句。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            vm.cmd( { type: 'JS', text: 'alert("JS Command")' } );
          }
      ] }
    ]
  },
  "Ajax": {
  	remark: '发送一个 http 请求到服务器。服务端应当返回一个命令格式JSON。',
    Config: [
      { name: 'beforeSend', type: 'String | Function', remark: '在发送请求之前调用的函数。如果返回false可以取消本次ajax请求。支持一个变量，<b>$ajax</b>(Ajax实例)' },
      { name: 'complete', type: 'String | Function', remark: '在得到服务器的响应后调用的函数(不论成功失败都会执行)。支持两个变量，<b>$response</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'data', type: 'String | Object', remark: 'post 数据。' },
      { name: 'dataType', type: 'String', remark: '指定后台返回的数据格式。可选值: <b>json</b>(默认), <b>xml</b>, <b>text</b>' },
      { name: 'download', type: 'Boolean', remark: '设置为true，转为下载模式。' },
      { name: 'error', type: 'String | Function', remark: '在获取服务器的响应数据失败后调用的函数。支持一个变量，<b>$ajax</b>(Ajax实例)' },
      { name: 'filter', type: 'String | Function', remark: '在获取服务器的响应数据后调用的函数。本语句应当 return 一个命令JSON。支持两个变量，<b>$response</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'headers', type: 'Object', remark: '一个额外的"{键:值}"对映射到请求一起发送。' },
      { name: 'src', type: 'String', remark: '路径。' },
      { name: 'template', type: 'String', remark: '模板地址。' },
      { name: 'success', type: 'String | Function', remark: '在成功获取服务器的响应数据并执行返回的命令之后调用的函数。如果设置了本参数，引擎将不会执行后台返回的命令，由业务自行处理。支持两个变量，<b>$response</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'sync', type: 'Boolean', remark: '是否同步。' }
    ],
	Examples: [
	  { example: [
          function() {
            // 数据提交过程中显示"正在加载"的提示框，完成后提示框自动关闭
            return~
            vm.cmd( {
            	type: 'Ajax',
            	src: 'get.sp',
            	loading: true
            } );
          },
          function() {
            // 用 data 发送数据
            vm.cmd( {
            	type: 'Ajax',
            	src: 'get.sp',
            	data: {
            	    id: '001',
            	    name: 'a b'
            	}
            } );
          },
          function() {
            // 打印服务器返回的数据
            vm.cmd( {
            	type: 'Ajax',
            	src: 'get.sp',
            	success: 'console.log($response)'
            } );
          }
      ] }
   ]
  },
  "Submit": {
  	remark: '提交表单数据到服务器。服务端应当返回一个命令格式JSON。',
  	extend: 'Ajax',
    Config: [
      { name: 'range', type: 'String', remark: '指定一个 widget id，只提交这个 widget 内的表单数据。多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示排除 widget 内的表单数据。' },
      { name: 'validate', type: 'Object', remark: '验证配置。', param: [
        { name: 'range', type: 'String', remark: '指定一个 widget id，只校验这个 widget 内的表单。多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示不校验 widget 内的表单。' },
        { name: 'group', type: 'String', remark: '验证组名。' },
        { name: 'effect', type: 'String', remark: '验证效果。可选值: <b>alert</b>, <b>red</b>, <b>none</b>' }
      ] }
    ],
	Examples: [
	  { example: [
          function() {
            // 数据提交过程中显示"正在加载"的提示框，完成后提示框自动关闭
            return~
            vm.cmd( {
            	type: 'Submit',
            	src: 'post.sp',
            	loading: true
            } );
          },
          function() {
            // 指定只提交 id=f_form 面板内的表单数据
            vm.cmd( {
            	type: 'Submit',
            	src: 'post.sp',
            	range: 'f_form'
            } );
          },
          function() {
            // 打印服务器返回的数据
            vm.cmd( {
            	type: 'Submit',
            	src: 'post.sp',
            	success: 'console.log($response)'
            } );
          }
      ] }
   ]
  },
  "After": {
  	remark: '插入命令。在某个 widget 之后插入一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      //{ name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在 id=f_btn 的按钮后面增加一个按钮
            return~
            vm.cmd( {
              type: 'After',
              target: 'f_btn',
              nodes: [
                { type: 'Button', text: '新增After' }
              ]
            } );
          }
      ] }
    ]
  },
  "Prepend": {
  	remark: '插入命令。在某个 widget 内部前置一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      //{ name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。node 和 nodes 不应同时使用。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在 id=f_bbr 的按钮栏里面插入一个按钮，位置在最前
            return~
            vm.cmd( {
              type: 'Prepend',
              target: 'f_bbr',
              nodes: [
                { type: 'Button', text: '新增Prepend' }
              ]
            } );
          }
      ] }
    ]
  },
  "Append": {
  	remark: '插入命令。在某个 widget 内部后置一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      //{ name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。node 和 nodes 不应同时使用。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在 id=f_bbr 的按钮栏里面插入一个按钮，位置在最后
            return~
            vm.cmd( {
              type: 'Append',
              target: 'f_bbr',
              nodes: [
                { type: 'Button', text: '新增Append' }
              ]
            } );
          }
      ] }
    ]
  },
  "Before": {
  	remark: '插入命令。在某个 widget 之前插入一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      //{ name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。node 和 nodes 不应同时使用。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//在 id=f_btn 的按钮前面增加一个按钮
            return~
            vm.cmd( {
              type: 'Before',
              target: 'f_btn',
              nodes: [
                { type: 'Button', text: '新增Before' }
              ]
            } );
          }
      ] }
    ]
  },
  "Replace": {
  	remark: '替换命令。替换某个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'node', type: 'Object', remark: '新的 widget 配置项。' },
      { name: 'target', type: 'String', remark: 'widget ID。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//替换 id="f_btn" 的按钮
            return~
            vm.cmd( {
            	type: 'Replace',
            	target: 'f_btn',
            	node: {
            	    type: 'Button',
            	    text: '新按钮'
            	}
            } );
          }
      ] }
    ]
  },
  "Remove": {
  	remark: '删除命令。删除某个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//删除 id="f_btn" 的按钮
            return~
            vm.cmd( {
            	type: 'Remove',
            	target: 'f_btn'
            } );
          }
      ] }
    ]
  },
  "Dialog": {
  	remark: '打开一个对话框。dialog 既是命令，也是 widget。',
  	extend: 'Widget',
    Config: [
      { name: 'cache', type: 'Boolean', remark: '如果设为 true, 当前窗口调用 .close() 方法关闭后，窗口处于隐藏状态并不删除，再次打开时将恢复为上次打开时的状态。' },
      { name: 'cover', type: 'Boolean', remark: '如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'id', type: 'String', remark: 'Dialog 的 id 参数有全局性。可以通过两种方式获取 dialog 的实例: <br> 1. 可通过 view.find( id ) 方法来获取 widget。<br> 2. 通过 $.dialog( id ) 获取。' },
      { name: 'loading', type: 'LoadingCommand', remark: '加载数据时显示一个等候窗口。' },
      { name: 'node', type: 'Object', remark: 'Dialog的唯一子节点。' },
      { name: 'src', type: 'String | Object', remark: '加载 view 的 url。<br>3.2版本以上，src可以是JSON对象。' },
      { name: 'independent', type: 'Boolean', remark: '设置为true，取消与父窗口的关联效果。' },
      { name: 'maxWidth', type: 'Number', remark: '最大宽度。' },
      { name: 'maxHeight', type: 'Number', remark: '最大高度。' },
      { name: 'minWidth', type: 'Number', remark: '最小宽度。' },
      { name: 'minHeight', type: 'Number', remark: '最小高度。' },
      { name: 'movable', type: 'Boolean', remark: '窗口是否可用鼠标移动位置。默认值为 true。' },
      { name: 'resizable', type: 'Boolean', remark: '窗口是否可用鼠标拖动调整大小。' },
      { name: 'fullScreen', type: 'Boolean', remark: '窗口在初始化时是否最大化。' },
      { name: 'snap', type: 'Object', remark: '吸附参数设置。', param: [
        { name: 'indent', type: 'Number', remark: '指定相对于初始位置缩进多少个像素。' },
        { name: 'inner', type: 'Boolean', remark: '是否在吸附对象的里面。默认值为false。' },
        { name: 'position', type: 'String', remark: '指定 snap 的位置。 <!--a href=javascript:; onclick="var s=this.nextSibling.style;s.display=s.display==\'none\'?\'block\':\'none\'"><b>点击查看参数说明图>></b></a--><span style="display:none"><img style="border:1px solid #ccc" src=src/img/snaptype.png></span><br>可选值：<b>tl</b> <b>tr</b> <b>rt</b> <b>rb</b> <b>br</b> <b>bl</b> <b>lb</b> <b>lt</b> <b>t</b> <b>r</b> <b>b</b> <b>l</b> <b>c</b>' +
         '<br>备注：t:top, r:right, b:bottom, l:left, c:center' },
        { name: 'target', type: 'HtmlElement | Widget', remark: '吸附的对象。可以是 html 元素或 widget ID。' }
      ] },
      { name: 'position', type: 'String', remark: '弹出位置。可选值: <b>tl</b> <b>tr</b> <b>rt</b> <b>rb</b> <b>br</b> <b>bl</b> <b>lb</b> <b>lt</b> <b>t</b> <b>r</b> <b>b</b> <b>l</b> <b>c</b>' +
         '<br>备注：t:top, r:right, b:bottom, l:left, c:center'
      },
      { name: 'autoHide', type: 'Boolean', remark: '设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。' },
      { name: 'prong', type: 'Boolean', remark: '设为 true，显示一个箭头，指向 snap 参数对象。' },
      { name: 'preload', type: 'String | Object', remark: '预装载模板地址，或预装载模板内容。' },
      { name: 'template', type: 'String | Object', remark: '模板地址，或模板内容。' },
      { name: 'timeout', type: 'Number', remark: '定时关闭，单位:毫秒。' },
      { name: 'title', type: 'String', remark: '标题。如果有设置 template, 标题将显示在 template/title 中。' }
    ],
    Event: [
      { name: 'load', remark: '对话框内的 view 加载完毕后触发。' },
      { name: 'close', remark: '关闭对话框后触发。' }
    ],
    Properties: [
      { name: 'contentView', type: 'View', remark: '对话框内部的 view 对象。' },
      { name: 'commander', type: 'Widget', remark: '执行 dilaog 命令的 widget。即以 xxx.cmd() 方式打开的 dialog, 它的 commander 就是 xxx。' },
      { name: 'ownerView', type: 'Widget', remark: 'dialog 所属的 view 对象。即 dialog 从哪个 view 打开，它的 ownerView 就是那个 view。' }
    ],
    Methods: [
      { name: 'close()', remark: '关闭，并触发 close 事件。如果设置了 cache:true，调用 close() 方法只会隐藏对话框。' },
      { name: 'draggable([target])', remark: '允许鼠标拖动窗口。', param: [
        { name: 'target', type: 'Widget | HTMLElement', optional: true, remark: '可拖动的对象。如果不设置此参数，那么整个窗口都可以拖动。' }
      ] },
      { name: 'hide()', remark: '隐藏。和 show() 方法对应。' },
      { name: 'isShow()', remark: '是否可见状态。' },
      { name: 'isMax()', remark: '是否最大化状态。' },
      { name: 'moveTo(iLeft, iTop)', remark: '移动到指定位置。', param: [
        { name: 'iLeft', type: 'Number', remark: '左边位置。' },
        { name: 'iTop', type: 'Number', remark: '顶部位置。' }
      ] },
      { name: 'max()', remark: '窗口最大化。如果窗口已经是最大化的状态，那么将恢复到初始大小。' },
      { name: 'parentDialog()', remark: '获取父窗口。' },
      { name: 'remove()', remark: '完全删除。调用本方法不会触发 close 事件。' },
      { name: 'show()', remark: '显示。和 hide() 方法对应。' }
    ],
    Classes: [
      { name: '.w-dialog', remark: '基础样式。' },
      { name: '.z-snap-{**}', remark: ' dialog 设置了 snap 参数时的样式。{**} 值根据 snap 结果类型而定。例如 snaptype 为 "41"，那么该样式名称则为: z-snap-41' },
      { name: '.z-mag-{*}', remark: ' dialog 设置了 snap 参数时的样式。{*} 值根据 snap 位置类型而定。x 的可能值有: t(top), r(right), b(bottom), l(left)' }
    ],
	Examples: [
	  { example: [
          function() {
          	//例1: Dialog用src加载显示
            return~
            {
                type: 'Dialog',
                width: 700,
                height: 500,
                src: 'getView.sp'
            }
          },
          function() {
          	//例1的 getView.sp 返回的内容
            return~
            {
                type: 'View',
                node: { type: 'Html', text: '内容' }
            }
          },
          function() {
          	//例2: Dialog用node显示
            return~
            {
                type: 'Dialog',
                width: 700,
                height: 500,
                node: {
                    type: 'View',
                    node: { type: 'Html', text: '内容' }
                }
            }
          }
      ] }
    ]
  },
  "Menu": {
  	remark: '显示一个菜单。menu 既是命令，也是 widget。',
  	extend: 'Widget',
    Config: [
      { name: 'nodes', type: 'Array', remark: '子节点集合。子节点类型是 button 或 split。' },
      { name: 'snap', type: 'Object', remark: '吸附参数设置。', param: [
        { name: 'indent', type: 'Number', remark: '指定相对于初始位置缩进多少个像素。' },
        { name: 'inner', type: 'Boolean', remark: '是否在吸附对象的里面。默认值为false。' },
        { name: 'position', type: 'String', remark: '指定 snap 的位置。 <!--a href=javascript:; onclick="var s=this.nextSibling.style;s.display=s.display==\'none\'?\'block\':\'none\'"><b>点击查看参数说明图>></b></a--><span style="display:none"><img style="border:1px solid #ccc" src=src/img/snaptype.png></span><br>可选值：<b>tl</b> <b>tr</b> <b>rt</b> <b>rb</b> <b>br</b> <b>bl</b> <b>lb</b> <b>lt</b> <b>t</b> <b>r</b> <b>b</b> <b>l</b> <b>c</b>' +
         '<br>备注：t:top, r:right, b:bottom, l:left, c:center' },
        { name: 'target', type: 'HtmlElement | Widget', remark: '吸附的对象。可以是 html 元素或 widget ID。' }
      ] },
      { name: 'prong', type: 'Boolean', remark: '设为 true，显示一个箭头，指向 snap 参数对象。' },
      { name: 'timeout', type: 'Number', remark: '定时关闭，单位:毫秒。' }
   ],
    Properties: [
      { name: 'commander', type: 'Widget', remark: '执行 menu 命令的 widget。即以 xxx.cmd() 方式打开的 menu, 它的 commander 就是 xxx。' },
      { name: 'ownerView', type: 'Widget', remark: 'menu 所属的 view 对象。即 menu 从哪个 view 打开，它的 ownerView 就是那个 view。' }
    ],
    Classes: [
      { name: '.w-menu', remark: '基础样式。' },
      { name: '.w-menu-line', remark: '用于button和menu的连接效果。如果需要这个效果，则设置: .w-menu-line{display:block}' },
      { name: '.z-snap-{**}', remark: ' dialog 设置了 snap 参数时的样式。{**} 值根据 snap 结果类型而定。例如 snaptype 为 "41"，那么该样式名称则为: z-snap-41' },
      { name: '.z-mag-{*}', remark: ' dialog 设置了 snap 参数时的样式。{*} 值根据 snap 位置类型而定。x 的可能值有: t(top), r(right), b(bottom), l(left)' }
    ],
	Examples: [
	  { example: [
          function() {
          	//树节点上点右键，显示一个Menu菜单
          	return~
            {
                type: 'Leaf',
                text: '树节点1',
                on: {
                    contextMenu: 'this.cmd( { type: "Menu", nodes: [ { text: "Menu1" }, { text: "Menu2" } ] } )'
                }
            }
          }
      ] }
    ]
  },
  "Alert": {
  	remark: '警告提示框。alert 是特殊的 dialog，既是命令，也是 widget。',
  	extend: 'Widget',
    Config: [
      { name: 'buttonCls', type: 'String', remark: '按钮样式名。' },
      { name: 'buttons', type: 'Array', remark: '自定义的一组按钮。' },
      { name: 'cover', type: 'Boolean', remark: '如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'id', type: 'String', remark: 'Dialog 的 id 参数有全局性。可以通过两种方式获取 dialog 的实例: <br> 1. 可通过 view.find( id ) 方法来获取 widget。<br> 2. 通过 $.dialog( id ) 获取。' },
      { name: 'snap', type: 'Object', remark: '吸附参数设置。', param: [
        { name: 'indent', type: 'Number', remark: '指定相对于初始位置缩进多少个像素。' },
        { name: 'inner', type: 'Boolean', remark: '是否在吸附对象的里面。默认值为false。' },
        { name: 'position', type: 'String', remark: '指定 snap 的位置。 <!--a href=javascript:; onclick="var s=this.nextSibling.style;s.display=s.display==\'none\'?\'block\':\'none\'"><b>点击查看参数说明图>></b></a--><span style="display:none"><img style="border:1px solid #ccc" src=src/img/snaptype.png></span><br>可选值：<b>tl</b> <b>tr</b> <b>rt</b> <b>rb</b> <b>br</b> <b>bl</b> <b>lb</b> <b>lt</b> <b>t</b> <b>r</b> <b>b</b> <b>l</b> <b>c</b>' +
         '<br>备注：t:top, r:right, b:bottom, l:left, c:center' },
        { name: 'target', type: 'HtmlElement | Widget', remark: '吸附的对象。可以是 html 元素或 widget ID。' }
      ] },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'icon', type: 'String', remark: '图标。' },
      { name: 'position', type: 'String', remark: '弹出位置。可选值: <b>tl</b> <b>tr</b> <b>rt</b> <b>rb</b> <b>br</b> <b>bl</b> <b>lb</b> <b>lt</b> <b>t</b> <b>r</b> <b>b</b> <b>l</b> <b>c</b>' +
         '<br>备注：t:top, r:right, b:bottom, l:left, c:center'
      },
      { name: 'timeout', type: 'Number', remark: '定时关闭，单位:毫秒。' },
      { name: 'title', type: 'String', remark: '标题。' },
      { name: 'yes', type: 'CommandJSON | Function', remark: '点击"确定"执行的命令或函数。' }
    ],
    Event: [
      { name: 'close', remark: '关闭提示框后触发。' }
    ],
    Properties: [
      { name: 'commander', type: 'Widget', remark: '执行对话框命令的 widget。' }
    ],
    Methods: [
      { name: 'close()', remark: '关闭，并触发 close 事件。' },
      { name: 'remove()', remark: '完全删除。调用本方法不会触发 close 事件。' }
    ],
    Classes: [
      { name: '.w-dialog', remark: '基础样式。' },
      { name: '.w-alert', remark: '基础样式。' }    ],
	Examples: [
	  { example: [
          function() {
            return~
            vm.cmd( { type: 'Alert', text: '提示内容' } );
          }
      ] }
    ]
  },
  "Confirm": {
  	remark: '确认对话框。confirm 是特殊的 dialog，既是命令，也是 widget。',
  	extend: 'Alert',
  	deprecate: 'position,timeout,.w-alert',
    Config: [
      { name: 'yes', type: 'CommandJSON | Function', remark: '点击"确定"执行的命令或函数。' },
      { name: 'no', type: 'CommandJSON | Function', remark: '点击"取消"执行的命令或函数。' }
    ],
    Classes: [
      { name: '.w-confirm', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
            return~
            vm.cmd( {
            	type: 'Confirm', 
            	text: '确定吗？', 
            	yes: { 
            	    type: 'JS', 
            	    text: 'alert("yes")'
            	}, 
            	no: { 
            	    type: 'JS', 
            	    text: 'alert("no")' 
            	} 
            } );
          }
      ] }
    ]
  },
  "Loading": {
  	remark: '显示一个"请稍候"的信息窗。',
  	extend: 'Widget',
    Config: [
      { name: 'cover', type: 'Boolean', remark: '如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。默认值为true。' },
      { name: 'format', type: 'String', remark: '格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'icon', type: 'String', remark: '图标。' },
      { name: 'node', type: 'Widget', remark: 'widget节点。' },
      { name: 'text', type: 'String', remark: '显示文本。' }
    ],
    Event: [
      { name: 'close', remark: '关闭提示框后触发。' }
    ],
    Properties: [
      { name: 'commander', type: 'Widget', remark: '执行对话框命令的 widget。' }
    ],
    Methods: [
      { name: 'close()', remark: '关闭，并触发 close 事件。' },
      { name: 'remove()', remark: '完全删除。调用本方法不会触发 close 事件。' }
    ],
    Classes: [
      { name: '.w-dialog', remark: '基础样式。' },
      { name: '.w-loading', remark: '基础样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	// Ajax命令在src加载进程中显示一个Loading窗口。进程结束后Loading窗口自动关闭。
            return~
            vm.cmd( {
                type: 'Ajax',
                src: 'getData.sp',
                loading: {
                    type: 'Loading',
                    text: '正在加载，请稍候..'
                }
            } );
          }
      ] }
    ]
  },
  "Tip": {
  	remark: '提示信息。',
  	extend: 'Alert',
  	deprecate: 'yes,timeout,buttonCls,buttons,icon,title,.w-alert',
    Config: [
      { name: 'closable', type: 'Boolean', remark: '是否显示关闭图标。默认值为 true。' },
      { name: 'face', type: 'String', remark: '样式效果。可选值：<b>normal</b>(默认值), <b>warn</b>(警告)' },
      { name: 'prong', type: 'Boolean', remark: '是否显示箭头。默认值为 true' }
    ],
    Event: [
      { name: 'close', remark: '关闭提示框后触发。' }
    ],
    Properties: [
      { name: 'commander', type: 'Widget', remark: '执行对话框命令的 widget。' }
    ],
    Methods: [
      { name: 'close()', remark: '关闭，并触发 close 事件。' },
      { name: 'remove()', remark: '完全删除。调用本方法不会触发 close 事件。' }
    ],
    Classes: [
      { name: '.w-dialog', remark: '基础样式。' },
      { name: '.w-tip', remark: '基础样式。' },
      { name: '.z-x', remark: '设置了 closeable:true 时的样式。' },
      { name: '.z-noprong', remark: '设置了 prong:false 时的样式。' }
    ],
	Examples: [
	  { example: [
          function() {
          	//让 id="f_btn" 的按钮显示一个Tip
            return~
            vm.find( 'f_btn' ).cmd( { type: 'Tip', text: "提示内容" } );
          }
      ] }
    ]
  }
} );
