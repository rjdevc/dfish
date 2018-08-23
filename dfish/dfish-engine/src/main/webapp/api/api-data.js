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
      { name: '$.alert(text, [pos], [time], [id])', remark: '弹出一个信息窗口。', common: true, param: [
        { name: 'text', type: 'String', remark: '信息内容。' },
        { name: 'pos', type: 'String', remark: '弹出位置。可选值: 0(默认) 1 2 3 4 5 6 7 8。其中 0 为页面中心点，1-8是页面八个角落方位。' },
        { name: 'time', type: 'Number', remark: '定时关闭，单位:秒。' },
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
            //
            $.classAdd( $( 'myDiv' ), 'bg-red' );
          }
      ] },
      { name: '$.classAny(elem, class)', remark: '元素是否包含指定的类名', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'class', type: 'String', remark: 'CSS类名。多个用空格隔开' }
      ], example: [
          function() {
            //
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
        { name: 'settings', type: 'object', remark: '配置参数', param: [
          { name: 'alias', type: 'object', remark: '新增的模块可在此注册别名' },
          { name: 'ajax_error', type: 'Function | Boolean', remark: '如果设为false，不提示任何ajax信息。<br>如果设为function，则作为处理错误信息的方法。方法接收两个参数，第一个是XMLHttpRequest实例，第二个是URL。' },
          { name: 'cn_bytes', type: 'Number', remark: '一个汉字算几个字节。默认值为2。' },
          { name: 'debug', type: 'Boolean', remark: '开启调试模式。调试模式下按"Ctrl+鼠标右键"可查看view的信息' },
          { name: 'default_option', type: 'object', remark: '每个 widget 类都可以定义默认样式，以 widget type 作为 key' },
          { name: 'input_detect', type: 'object', remark: '设置表单在键入文本时是否即时检测。', param: [
            { name: 'maxlength', type: 'Boolean', remark: '设置为true，键入文本时将会即时检测是否超出最大长度。' }
          ] },
          { name: 'lib', type: 'String', remark: 'dfish包路径，必选项。' },
          { name: 'lang', type: 'String', remark: '语言。可选项:zh_CN,zh_TW,en' },
          { name: 'no_conflict', type: 'Boolean', remark: '设置为true，将变量$的控制权让渡给第一个实现它的那个库。' },
          { name: 'path', type: 'String', remark: '工程项目的路径。必选项。' },
          { name: 'support_url', type: 'String', remark: '显示支持与帮助的页面URL。如果配置此参数，系统所需的软件下载等都将指向这个地址。' },
          { name: 'server', type: 'String', remark: '服务器地址的绝对路径。当执行 ajax 命令等交互操作时会使用此路径。<br>注：如果 ajax 命令的 src 参数以 ./ 开头，将不会使用 server 参数，而是访问本地地址。', mobile: true },
          { name: 'skin', type: 'Object', remark: '配置皮肤样式。', param: [
            { name: 'dir', type: 'String', remark: '皮肤目录' },
            { name: 'theme', type: 'String', remark: '主题名。在皮肤目录下应有一个和主题名相同的目录，该目录里面有一个 "主题名.css"' },
            { name: 'color', type: 'String', remark: '颜色名。在主题目录下应有一个和颜色名相同的目录，该目录里面有一个 "颜色名.css"' }
          ] },
          { name: 'template', type: 'String', remark: '对话框默认模板ID。' },
          { name: 'template_alert', type: 'String', remark: 'alert和confirm的默认对话框模板ID。' },
          { name: 'template_src', type: 'String', remark: '对话框模板路径' },
          { name: 'validate_effect', type: 'String', remark: '表单验证效果。可选项: "red"(表单边框变成红色)；"alert"(弹出提示框)；"red,alert"(边框变红并弹出提示)' },
          { name: 'validate_handler', type: 'Function', remark: '表单验证的回调函数。函数有一个参数，接收一个验证信息的数组。' },
          { name: 'ver', type: 'String', remark: '版本号。这个参数将会附加在js和css的路径上，以避免更新后的浏览器缓存问题。' },
          { name: 'view', type: 'Object', remark: 'view的配置项。如果配置了此参数，将生成一个全屏view' },
          { name: 'view_js', type: 'Object', remark: '设置view的依赖JS模块。以 view path 作为 key。当页面上生成这个 path 的 view 时，就会加载对应的JS。多个JS可以用数组。' }
        ] }
      ], example: [
          function() {
            //
            dfish.config( {
              path: '/itask7_2/', //工程目录
              lib:  'dfish/',  //dfish包目录
              debug: true, // 开启调试模式
              lang: 'zh_CN',  // 语言包
              alias: { //自定义模块
              	'ueditor':  'pl/ueditor1_4_3/ueditor.dfish.js', //百度编辑器
              	'upload':	'pl/upload/upload.js' //上传组件
              },
              ajax_error: function( req, url ) { // 处理ajax错误信息的方法
              	alert( req.status );
              },
              // 给 alert 和 confirm 设置 btncls 的默认值。
              default_option: {
              	'alert': { btncls: 'x-btn' },
              	'confirm': { btncls: 'x-btn' }
              },
              input_detect: { // 表单即时检测
              	maxlength: true
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
              view_js: { // view的依赖JS模块
              	'/index'      : [ './m/pub.js', './m/index.js' ],
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
            //
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
            //
            $.data( 'myname', '123' ); // 写入数据
            var v = $.data( 'myname' );  // 读取数据
          }
      ] },
      { name: '$.dateAdd(date, type, value)', remark: '日期增减', common: true, param: [
        { name: 'date', type: 'Date', remark: '日期对象' },
        { name: 'type', type: 'String', remark: '要增加的日期类型，可选值：<b>y</b>(年) <b>m</b>(月) <b>d</b>(日) <b>h</b>(时) <b>i</b>(分) <b>s</b>(秒)' }
      ], example: [
          function() {
            //
            var tomorrow = $.dateAdd( new Date(), 'd', 1 ); // 给当下的时间加一天
          }
      ] },
      { name: '$.dateFormat(date, format)', remark: '把日期对象格式化成字串', common: true, param: [
        { name: 'date', type: 'Date', remark: '时间对象' },
        { name: 'format', type: 'String', remark: '时间格式，可选值：<b>y</b>(年) <b>m</b>(月) <b>d</b>(日) <b>h</b>(时) <b>i</b>(分) <b>s</b>(秒)' }
      ], example: [
          function() {
            //
            var f = $.dateFormat( new Date(), 'yyyy-mm-dd hh:ii:ss' );
          }
      ] },
      { name: '$.dateParse(date)', id: '$.dateFormat', remark: '把字符串格式的日期转为日期对象', common: true, param: [
        { name: 'date', type: 'String', remark: '时间' }
      ], example: [
          function() {
            //
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
            var opt = { type: "button", text: "关闭", on: { click: "$.dialog(this).close()" } }
          }
      ] },
      { name: '$.download(url, [data])', remark: '下载文件。', common: true, param: [
        { name: 'url', type: 'String', remark: '下载的文件地址。' },
        { name: 'data', type: 'Object', remark: 'post方式发送到后台的数据。', optional: true }
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
          	//
            var obj1 = $.extend( { name: 'John' }, { sex: 'male' } ); // 返回结果为 { name: 'John', sex: 'male' }
            var obj2 = $.extend( { name: 'John' }, { name: 'Bob', sex: 'male' } ); // 返回结果同上
          }
      ] },
      { name: '$.height()', remark: '获取浏览器可用的高度。', common: true, example: [
          function() {
            //
            alert( 'document height:' + $.height() );
          }
      ] },
      { name: '$.inArray(arr, obj)', remark: '数组中是否存在某个对象。返回true/false', common: true, param: [
        { name: 'arr', type: 'Array', remark: '数组' },
        { name: 'obj', type: 'All', remark: '待查的对象' }
      ], example: [
          function() {
          	//
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
          	//
            if ( $.isArray( [ 0, 1 ] ) ) {
              alert( '是数组' );
            }
          }
      ] },
      { name: '$.jsonClone(obj)', remark: '复制一个json对象。', common: true, param: [
        { name: 'obj', type: 'json', remark: '待复制的对象' }
      ], example: [
          function() {
          	//
            var obj = $.jsonClone( { name: 'John' } ); //复制一个json
          }
      ] },
      { name: '$.jsonParse(text)', remark: '把json格式的字符串解析为一个json对象。', common: true, param: [
        { name: 'text', type: 'String', remark: 'json格式的字符串' }
      ], example: [
          function() {
          	//
            var obj = $.jsonParse( '{"name":"John"}' ); //返回一个json对象
          }
      ] },
      { name: '$.jsonString(obj)', remark: '把一个json对象转为字符串。', common: true, param: [
        { name: 'obj', type: 'json', remark: 'json对象' }
      ], example: [
          function() {
          	//
            var str = $.jsonString( { name: 'John' } ); //返回字符串
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
      { name: '$.prepend(elem, content)', id: '$.prepend', remark: '在元素内部前置内容。', common: true, param: [
        { name: 'elem', type: 'HTMLElement', remark: 'html元素对象' },
        { name: 'content', type: 'String', remark: 'html内容' }
      ], example: [
          function() {
            $.prepend( $( 'myDiv' ), '<a href=#>新增链接</a>' );
          }
      ] },
      { name: '$.print(target, [bPrint], [tag])', id: '$.print', remark: '打印目标对象的内容。', common: true, param: [
        { name: 'target', type: 'Widget | HTMLElement', remark: 'widget对象，或者HTML元素对象。' },
        { name: 'bPrint', type: 'Boolean', remark: '设置为true，立即执行打印。', optional: true },
        { name: 'tag', type: 'String', remark: '放在打印页面head里的标签。', optional: true }
      ], example: [
          function() {
          	//打印id=content的widget内容，并设置打印字体为36px
            $.print( VM(this).find('content'), true, "<style>body{font-size:36px}</style>" );
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
          	//
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
          	//
            $.remove( $( 'myDiv' ) );
          }
      ] },
      { name: '$.replace(elem, content)', remark: '把元素替换成指定的HTML或DOM元素。', common: true, param: [
        { name: 'elem', type: 'String | HTMLElement', remark: '将要替换成的内容' }
      ], example: [
          function() {
          	//
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
          	//
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
          	// 
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
          	//
            var s = $.strEscape( '<a href=#>链接</a>' );
          }
      ] },
      { name: '$.strFrom(str, from, [last])', remark: '截取从某个/些字符开始的字符串。如果没有匹配到则返回空字符串。', common: true, param: [
        { name: 'str', type: 'String', remark: '要截取的字符串。' },
        { name: 'from', type: 'String', remark: '开始的字符。' },
        { name: 'last', type: 'Boolean', remark: '从最后一个匹配的字符开始。', optional: true }
      ], example: [
          function() {
          	// 
            var s1 = $.strFrom( 'm/pub/test.js', '/' ); // 返回 "pub/test.js"
            var s2 = $.strFrom( 'm/pub/test.js', '/', true ); // 返回 "test.js"
          }
      ] },
      { name: '$.strHighlight(str, key, [matchlength], [keycls])', remark: '给字串中的关键词加上高亮的样式标签。', common: true, param: [
        { name: 'str', type: 'String', remark: '字符串。' },
        { name: 'key', type: 'String', remark: '关键词。' },
        { name: 'matchlength', type: 'Number', optional: true, remark: '切词长度。' },
        { name: 'keycls', type: 'String', optional: true, remark: '高亮的样式名。默认值为"f-keyword"。' }
      ] },
      { name: '$.strSlice(str, len, [ext])', remark: '把字符串按照字节数截取。中文字节数读取自 dfish 全局配置的 cn_bytes 参数。如果没有设置此参数，默认算两个字符。', common: true, param: [
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
          	// 
            var s1 = $.strTo( 'm/pub/test.js', '/' ); // 返回 "m"
            var s2 = $.strTo( 'm/pub/test.js', '/', true ); // 返回 "m/pub"
          }
      ] },
      { name: '$.strTrim(str)', remark: '删除字符串前后空格。', common: true, param: [
        { name: 'str', type: 'String', remark: '要处理的字符串' }
      ], example: [
          function() {
          	// 
            var s1 = $.strTrim( ' abc ' ); // 返回 "abc"
          }
      ] },
      { name: '$.strUnescape(str)', remark: '对经过 $.strEscape 编码的字符串进行解码。', common: true, param: [
        { name: 'html', type: 'String', remark: '要接吗的字符串' }
      ], example: [
          function() {
          	//
            var s = $.strUnescape( '&lt;a href=#&gt;链接&lt;/a&gt;' );
          }
      ] },
      { name: '$.thumbnail(range, width, [opt])', remark: '把某个范围内的图片变成缩略图。', common: true, param: [
        { name: 'range', type: 'HtmlElement | Widget', remark: 'HTML元素或widget对象。' },
        { name: 'width', type: 'Number', remark: '图片最大宽度。' },
        { name: 'opt', type: 'String | Function | Boolean', remark: 'String: 弹出新窗的URL(支持$0,$1变量。$0是图片地址,$1是图片标题); Function: 回调函数; Boolean: 设为false，取消点击预览功能。' },
      ], example: [
          function() {
          	//
            $.thumbnail( vm.find('img').$(), 500 );
          }
      ] },
      { name: '$.urlDecode(url)', remark: '解码经过UTF-8编码的url。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串' }
      ], example: [
          function() {
          	//
            var url = $.urlDecode( '%26' ); // 返回 "&"
          }
      ] },
      { name: '$.urlEncode(url)', remark: '对url进行UTF-8编码。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串' }
      ], example: [
          function() {
          	//
            var url = $.urlEncode( '&' ); // 返回 "%26"
          }
      ] },
      { name: '$.urlFormat(url, data)', remark: '对url中的 $0, $xxx 等变量进行替换。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串。' },
        { name: 'data', type: 'Array | Object', remark: '用来替换的数据。如果 url 中的变量是 $0, $1...$n，那么本参数应该是 Array；如果url 中的变量是 $str，那么本参数应该是 Object。' }
      ], example: [
          function() {
          	//
            var url = $.urlFormat( 'a.sp?id=$0&v=$1', [ 'a', 'b' ] ); // 返回 "a.sp?id=a&v=b"
            var url = $.urlFormat( 'a.sp?id=$a&v=$b', { a: 1, b: 2 } ); // 返回 "a.sp?id=1&v=2"
          }
      ] },
      { name: '$.urlParam(url, data)', remark: '读/写 url 中的变量。', common: true, param: [
        { name: 'url', type: 'String', remark: 'url字符串。' },
        { name: 'data', type: 'String | Object', remark: '如果是 String 类型，读取以 data 作为 key 的值。如果是 Object 类型，则写入 URL 变量。' }
      ], example: [
          function() {
          	//
            var id  = $.urlParam( 'a.sp?id=1', 'id' ); // 返回 "1"
            var url = $.urlParam( 'a.sp?id=1', { v: 2 } ); // 返回 "a.sp?id=1&v=2"
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
            //
            alert( 'document width:' + $.width() );
          }
      ] },
      { name: '$.xmlParse(content)', remark: '把字符串转为xml对象', common: true, param: [
        { name: 'content', type: 'String', remark: '字符串形式的xml' }
      ], example: [
          function() {
          	//
            var x = $.xmlParse( '<doc>123</doc>' ); // 返回一个xml节点对象
          }
      ] },
      { name: '$.xmlQuery(url)', remark: '根据查询语句获取匹配到的第一个节点。返回一个xml节点对象。', common: true, param: [
        { name: 'expr', type: 'String', remark: '符合xpath语法的查询语句' }
      ], example: [
          function() {
          	//
            var x = $.xmlQueryAll( xml, 'name' ); // 获取第一个标签名为name的子节点
          }
      ] },
      { name: '$.xmlQueryAll(expr)', remark: '根据查询语句获取匹配到的所有节点。返回一个数组。', common: true, param: [
        { name: 'expr', type: 'String', remark: '符合xpath语法的查询语句' }
      ], example: [
          function() {
          	//
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
          	//
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
  "widget": {
  	title: 'widget基础类',
  	remark: '所有widget都继承此类。',
    Config: [
      { name: 'aftercontent', type: 'String', remark: '附加到末尾的内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget的json对象)。', common: true },
      { name: 'beforecontent', type: 'String', remark: '附加到开头的内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget的json对象)。', common: true },
      { name: 'cls', type: 'String', remark: '样式类名。', common: true },
      { name: 'data', type: 'Object', remark: '扩展数据。key:value键值对。在当前widget及子孙节点范围内的事件可以用变量 $key 的来获取值。', common: true },
      { name: 'gid', type: 'String', remark: '自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。', common: true },
      { name: 'height', type: 'Number | String', remark: '高度。可以用数字, *, 百分比。如果设置为 -1, 就是自适应高度。', common: true },
      { name: 'hmin', type: 'Number', remark: '如果设置了 cls 参数，并且 cls 里定义了 padding border margin 这三种样式中的至少一种 ，那么就需要手工设置 hmin 以减去因这些样式额外增加的高度。<br>注: 如果在 style 参数里设置了这三种样式，系统会自动分析，一般不需要额外设置 hmin。', common: true },
      { name: 'id', type: 'String', remark: '自定义的ID。可通过 view.find( id ) 方法来获取 widget。', common: true },
      { name: 'maxheight', type: 'Number', remark: '最大高度。当 height 设置为 * 时可以使用本参数。', common: true },
      { name: 'maxwidth', type: 'Number', remark: '最大宽度。当 width 设置为 * 时可以使用本参数。', common: true },
      { name: 'minheight', type: 'Number', remark: '最小高度。当 height 设置为 * 时可以使用本参数。', common: true },
      { name: 'minwidth', type: 'Number', remark: '最小宽度。当 width 设置为 * 时可以使用本参数。', common: true },
      { name: 'on', type: 'Object', remark: '事件。', common: true },
      { name: 'style', type: 'String', remark: '样式。', common: true },
      { name: 'type', type: 'String', remark: '类型名称。', common: true },
      { name: 'width', type: 'Number | String', remark: '宽度。可以用数字, *, 百分比。如果设置为 -1, 就是自适应宽度。', common: true },
      { name: 'wmin', type: 'Number', remark: '如果设置了 cls 参数，并且 cls 里定义了 padding border margin 这三种样式中的至少一种 ，那么就需要手工设置 wmin 以减去因这些样式额外增加的宽度。<br>注: 如果在 style 参数里设置了这三种样式，系统会自动分析，一般不需要额外设置 wmin。', common: true }
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
          	// 
            wg.ownerView.reload(); // 让 widget 所在的 view 刷新
          }
      ] },
      { name: 'parentNode', type: 'Widget', remark: '父节点。', common: true },
      { name: 'nodeIndex', type: 'Number', remark: '节点序号。标识在兄弟节点中的排序位置。', common: true },
      { name: 'length', type: 'Number', remark: '子节点的个数。', common: true },	
      { name: 'x', type: 'Object', remark: 'widget的初始配置项对象。', common: true, example: [
          function() {
          	//
            var wg = vm.find( 'aaa' );
            alert( wg.x.type );
          }
      ] }
    ],
    Event: [
      { name: 'nodechange', remark: '子节点有增、删、改时触发。', common: true },
      { name: 'ready', remark: '生成DOM对象后触发。', common: true },
      { name: 'resize', remark: '调整大小时触发。', common: true }
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
          	//
            wg.after( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'append(opt)', remark: '在内部的末尾处插入子节点。', common: true, param: [
        { name: 'opt', type: 'object | widget | Array', remark: 'widget 配置参数或对象。如果要新增多个，可以使用数组。' }
      ], example: [
          function() {
          	//
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
          	//
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
      { name: 'closestData(key)', remark: '获取祖先节点的data数据。从当前节点开始，逐级向上，返回最先获取到的data值。', common: true, param: [
        { name: 'key', type: 'String', remark: '属性名。' }
      ] },
      { name: 'cmd(cmdID, [arg1, arg2...argN])', remark: '执行命令。', common: true, param: [
        { name: 'cmdID', type: 'String | Object', remark: '命令ID，或命令参数对象' },
        { name: 'argN', type: 'String', remark: '调用 ajax 或 submit 命令时，会替换 src 中的 $0...$N', optional: true }
      ], example: [
          function() {
          	//
            wg.cmd( { type: 'ajax', src: 'abc.sp' } );
          }
      ] },
      { name: 'css(name, value)', remark: '设置 widget 的 style。', common: true, param: [
        { name: 'name', type: 'String | Object', remark: '样式属性名，或者是样式属性的对象。数字型的值可用 += 和 -= 来做额外附加。' },
        { name: 'value', type: 'String', remark: '样式属性值。数字型的值可用 += 和 -= 来做额外附加。' }
      ], example: [
          function() {
            //
          	vm.find( 'wg' ).css( { 'width': 100, 'background': 'red' } ); // 设置宽度为100px，背景色为红色
          	vm.find( 'wg' ).css( 'height', '+=100' ); // 高度增加100px
          }
      ] },
      { name: 'data(name, [value])', remark: '读/写自定义的数据。', common: true, param: [
        { name: 'name', type: 'String', remark: '属性名。' },
        { name: 'value', type: 'String', remark: '属性值。', optional: true }
      ], example: [
          function() {
            wg.data( 'mydata', '123' );
            alert( wg.data( 'mydata' ) ); // 显示"123"
          }
      ] },
      { name: 'display([show])', remark: '显示或隐藏。', common: true, param: [
        { name: 'show', type: 'Boolean', remark: 'true:显示; false:隐藏。', optional: true }
      ] },
      { name: 'isDisplay()', remark: '是否在显示状态。', common: true },
      { name: 'exec(cmdID, [args], [opt])', remark: '执行命令。和 .cmd() 方法作用一样，只是参数不同。', common: true, param: [
        { name: 'cmdID', type: 'String | Object', remark: '命令ID，或命令参数对象' },
        { name: 'args', type: 'Array', remark: '调用 ajax 或 submit 命令时，会替换 src 中的 $0...$N', optional: true },
        { name: 'opt', type: 'String', remark: '为 cmdID 命令提供额外的参数。', optional: true }
      ], example: [
          function() {
          	//假设 view 中定义了命令: "new": { "type": "dialog", src: 'abc.sp?id=$0', width: 500, height: 400 }，使用 exec 调用此命令：
            wg.exec( 'new', [ 5 ], { target: this, pophide: true } ); // 把 src 中的 $0 替换为5, 并增加 target 和 pophide 参数
          }
      ] },
      { name: 'find(id)', remark: '根据ID获取当前widget内部的节点。', common: true, param: [
        { name: 'id', type: 'String', remark: 'widget ID。' }
      ] },
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
      { name: 'getAll()', remark: '获取所有子孙节点，返回一个数组。', common: true },
      { name: 'hasClass(cls)', remark: '是否包含某些样式。', common: true, param: [
        { name: 'cls', type: 'String', remark: '样式名。多个样式用空格隔开。' }
      ] },
      { name: 'height([num])', remark: '获取或设置高度。', common: true, param: [
        { name: 'num', type: 'String | Number', remark: '高度值。可以是数字，*, 百分比。', optional: true, common: true }
      ], example: [
          function() {
          	//
            var h = wg.height(); // 获取高度
            wg.height( 100 ); //设置高度
          }
      ] },
      { name: 'innerHeight()', remark: '获取或设置高度。即去除 padding border margin 后的高度。', common: true, example: [
          function() {
          	//
            var h = wg.innerHeight();
          }
      ] },
      { name: 'innerWidth()', remark: '获取可用宽度。即去除 padding border margin 后的宽度。', common: true, example: [
          function() {
          	//
            var w = wg.innerWidth();
          }
      ] },
      { name: 'next()', remark: '获取下一个兄弟节点。', common: true, example: [
          function() {
          	//
            var n = wg.next();
          }
      ] },
      { name: 'prepend(opt)', remark: '在内部的开始处插入子节点。', common: true, param: [
        { name: 'opt', type: 'object | widget | Array', remark: 'widget 配置参数或对象。如果要新增多个，可以使用数组。' }
      ], example: [
          function() {
          	//
            wg.prepend( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'prev()', remark: '获取上一个兄弟节点。', common: true, example: [
          function() {
          	//
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
      ], example: [
          function() {
          	// 创建一个 view 并生成在某个 div 里
           var vm = $.create( { type: 'view', src: 'index.sp', width: 800, height: -1 } );
           vm.render( $( 'mydiv' ) );
          }
      ] },
      { name: 'replace(opt)', remark: '把自身替换为另一个 widget。', common: true, param: [
        { name: 'opt', type: 'object | widget', remark: 'widget 配置参数或对象。' }
      ], example: [
          function() {
          	//
            wg.replace( { type: 'html', text: '123' } );
          }
      ] },
      { name: 'remove()', remark: '移除。', common: true },
      { name: 'trigger(event, [args])', remark: '触发事件。这个方法会触发 addEvent 绑定的方法、引擎内部方法、以及配置参数里 on 设置的方法。', common: true, param: [
        { name: 'event', type: 'Event | String', remark: '事件对象或名称。' },
        { name: 'args', type: 'Array', remark: '给绑定方法传入的参数。', optional: true }
      ], example: [
          function() {
          	//
            wg.trigger( 'click' );
          }
      ] },
      { name: 'setOn(event, fn)', common: true, param: [
        { name: 'event', type: 'String', remark: '事件名称。' },
        { name: 'fn', type: 'Function | String', remark: '事件函数。' }
      ], remark: '设置事件。', example: [
          function() {
          	//
            wg.setOn( 'click', 'alert(this.type)' );
          }
      ] },
      { name: 'triggerAll(event)', common: true, param: [
        { name: 'event', type: 'Event | String', remark: '事件对象或名称。' }
      ], remark: '触发自身和所有子孙节点的事件。', example: [
          function() {
          	//
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
          	//
            var w = wg.width(); // 获取宽度
            wg.width( 100 ); // 设置宽度
          }
      ] }
    ]
  },
  "button": {
  	title: 'button',
  	remark: '按钮类。',
  	extend: 'widget',
  	//deprecate: 'prepend,append',
    Config: [
      { name: 'closeable', type: 'Boolean', remark: '是否有关闭图标。' },
      { name: 'focus', type: 'Boolean', remark: '是否焦点模式。' },
      { name: 'focusable', type: 'Boolean', remark: '设置为 true，按钮点击后转为焦点状态(按钮增加焦点样式 .z-on )' },
      { name: 'hoverdrop', type: 'Boolean', remark: '是否当鼠标 hover 时展开下拉菜单。' },
      { name: 'hidetoggle', type: 'Boolean', remark: '是否隐藏 toggle 图标。' },
      { name: 'icon', type: 'String', remark: '图标的url。支持以 "." 开头的样式名。', example: [
          function() {
          	//
            var opt1 = { type: 'button', icon: 'img/abc.gif' }; // 使用图片路径
            // 假设定义了样式: .ico-edit{background:url(img/abc.gif)}, 可以使用以下调用方式
            var opt2 = { type: 'button', icon: '.ico-edit' };
          }
      ] },
      { name: 'more', type: 'Menu | Dialog', remark: 'menu或dialog。点击按钮时展示。', example: [
          function() {
          	// 一个有下拉选项的按钮
            var opt = { type: 'button', text: '更多', more: {
              type: 'menu', nodes: [ { text: '新建' }, { text: '编辑' } ]
            } };
          }
      ] },
      { name: 'name', type: 'String', remark: '在一个 view 中设置了相同 name 的 button 将成为一组，focus 只会作用于其中一个。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。点击下拉显示右键菜单。nodes 和 more 不应同时使用。' },
      { name: 'status', type: 'String', remark: '按钮状态。可选值：<b>normal</b>, <b>disabled</b>。' },
      { name: 'target', type: 'String', remark: '指定一个 frame 内的 widget ID，使 button 的 focus 效果和绑定 widget 的显示隐藏效果。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'tip', type: 'String', remark: '浮动显示的提示文本。' }
    ],
    Methods: [
      { name: 'disable([bDisabled])', remark: '设置按钮状态为可用/禁用。', param: [
        { name: 'bDisabled', type: 'Boolean', remark: '是否禁用。', optional: true }
      ] },
      { name: 'focus([bFocus])', remark: '设置焦点状态。', param: [
        { name: 'bFocus', type: 'Boolean', remark: '默认为 true', optional: true }
      ], example: [
          function() {
          	//
            btn.focus(); // 聚焦
            btn.focus( false ); // 失去焦点
          }
      ] },
      { name: 'click()', remark: '模拟一次点击。' },
      { name: 'drop()', remark: '如果有设置 more 参数，执行此方法可以展示下拉菜单。'},
      { name: 'icon(src)', remark: '更换图标。', param: [
        { name: 'src', type: 'String', remark: '图标地址。可以是 url 地址或以"."开头的图标样式名。' }
      ] },
      { name: 'isFocus()', remark: '是否焦点状态。' },
      { name: 'isLocked()', remark: '是否锁定状态。' },
      { name: 'lock([locked])', remark: '锁定/解锁按钮。', param: [
        { name: 'locked', type: 'Boolean', remark: 'true: 锁定状态; false: 解除锁定。', optional: true }
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
      { name: '.z-first', remark: '如果按钮的父节点是buttonbar，且当前按钮为buttonbar的第一个子节点时的样式。' },
      { name: '.z-last', remark: '如果按钮的父节点是buttonbar，且当前按钮为buttonbar的最后一个子节点时的样式。' },
      { name: '.z-on', remark: '高亮的样式。如果按钮设置了 disabled:true 则无此样式。' }
    ]
  },
  "submitbutton": {
  	title: 'submitbutton',
  	remark: '默认提交按钮。<ul><li>在 text 等表单上按回车，将触发此按钮的点击事件。<li>执行 submit 命令时，默认带 lock: true 的效果。</ul>',
  	extend: 'button',
    Classes: [
      { name: '.w-submit', remark: '基础样式。' }
    ]
  },
  "menu/button": {
  	title: 'menu/button',
  	remark: 'menu菜单中的按钮。',
  	extend: 'button',
    Methods: [
      { name: 'getCommander()', remark: '获取最上层菜单的 commander 对象。' }
    ]
  },
  "buttonbar": {
  	title: 'buttonbar',
  	remark: 'button 的父类。',
  	extend: 'widget',
    Config: [
      { name: 'align', type: 'String', remark: '水平居中。可选值: <b>left</b>, <b>right</b>, <b>center</b>' },
      { name: 'dir', type: 'String', remark: '按钮排列方向。可选值: <b>h</b><font color=green>(横向,默认)</font>, <b>v</b><font color=green>(纵向)</font>' },
      { name: 'focusmultiple', type: 'Boolean', remark: '是否有多个按钮可同时设为焦点状态。' },
      { name: 'nobr', type: 'Boolean', remark: '不换行。默认为 true。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'space', type: 'Number', remark: '按钮之间的间隔。' },
      { name: 'split', type: 'Object', remark: '在按钮之间插入一个split widget。' },
      { name: 'pub', type: 'Object', remark: '按钮的默认属性。' },
      { name: 'valign', type: 'String', remark: '垂直居中。可选值: <b>top</b>, <b>bottom</b>, <b>middle</b>' }
    ],
    Methods: [
      { name: 'draggable([option])', remark: '设置所有按钮可拖拽。', param: [
        { name: 'option', type: 'Object', remark: '拖拽参数。', optional: true, param: [
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true }
       ] }
      ] },
      { name: 'droppable([option])', remark: '设置所有按钮可放置。', param: [
        { name: 'option', type: 'Object', remark: '拖拽参数。', optional: true, param: [
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true },
          { name: 'sort', type: 'Boolean', remark: '是否可排序。', optional: true },
          { name: 'drop', type: 'Function(event, ui)', remark: '当一个可接受的 draggable 被放置在 droppable 上时触发。', optional: true, param: [
          	{ name: 'event', type: 'Event', remark: '放置事件。' },
          	{ name: 'ui', type: 'Object', remark: '辅助参数。', param: [
          		{ name: 'draggable', type: 'Widget', remark: '拖拽节点。' },
          		{ name: 'droppable', type: 'Widget', remark: '放置节点。' },
          		{ name: 'type', type: 'String', remark: '放置方式。可能的值："append","before","after"。' }
          	] }
          ] }
       ] }
      ], example: [
          function() {
            // 设置按钮栏可拖拽也可放置
            vm.find( 'tree' ).draggable().droppable( {
              drop: function( ev, ui ) {
                var u = 'move.sp?act=move&from=' + ui.draggable.x.id + '&to=' + ui.droppable.x.id + '&type=' + ui.type;
                alert(u);
              }
            } );
          }
      ] },
      { name: 'getFocus()', remark: '获取焦点状态的子节点。' },
      { name: 'getLocked()', remark: '获取锁定状态的子节点。' }
    ],
    Classes: [
      { name: '.w-buttonbar', remark: '基础样式。' },
      { name: '.z-dirv', remark: '设置了 dir:"v"(按钮垂直排列) 时的样式。' },
      { name: '.z-dirh', remark: '设置了 dir:"v"(按钮水平排列) 时的样式。' }
    ]
  },
  "deck": {
  	title: 'deck',
  	remark: 'deck 功能面板。',
  	extend: 'widget',
    Config: [
      { name: 'nodes', type: 'Array', remark: 'deck 内的单元组。每个单元由一个按钮和一个 widget 构成，点击按钮展示当前单元。', example: [
          function() {
          	// 有两个单元的deck
            var opt =  {
              "type": "deck",
              "buttonheight": 40,
              "nodes": [
                {
                  "button": { "text": "按钮1", "focus": true },
                  "content": { "type": "html", "text": "面板1" }
                },
                {
                  "button": { "text": "按钮2" },
                  "content": { "type": "html", "text": "面板2" }
                } ]
            };
          }
      ] }
    ],
    Methods: [
    ]
  },
  "fieldset": {
  	title: 'fieldset',
  	remark: 'fieldset模式布局。',
  	extend: 'widget',
    Config: [
      { name: 'legend', type: 'String', remark: '标题文本。' },
      { name: 'box', type: 'Object', remark: '选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。', param: [
        { name: 'type',    type: 'String',  remark: '类型。可选值: <b>checkbox</b>, <b>radio</b>' },
        { name: 'name',    type: 'String',  remark: '表单名。' },
        { name: 'value',   type: 'String',  remark: '表单值。' },
        { name: 'text',    type: 'String',  remark: '显示文本。', optional: true },
        { name: 'checked', type: 'Booelan', remark: '是否默认选中。', optional: true },
        { name: 'target',  type: 'String | Widget', remark: '绑定 widget 或 widgetID，同步 disabled 属性。', optional: true }
      ], example: [
          function() {
          	//
            var wg = { type: 'fieldset', box: { type: 'checkbox', name: 'box', text: '选项一' } };
          }
      ] },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' }
    ],
    Classes: [
      { name: '.w-fieldset', remark: '基础样式。' }
    ]
  },
  "frame": {
  	title: 'frame',
  	remark: '帧模式布局。只显示一个子元素，其他子元素都隐藏。',
  	extend: 'widget',
    Config: [
      { name: 'dft', type: 'String', remark: '默认显示 widget 的 ID。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' }
    ],
    Methods: [
      { name: 'getFocus()', remark: '获取当前显示的 widget。' },
      { name: 'view(id)', remark: '显示某个子元素。', param: [
        { name: 'id', type: 'String | widget', remark: 'widget ID 或对象。' }
      ] }
   ],
    Classes: [
      { name: '.w-frame', remark: '基础样式。' }
    ]
  },
  "grid": {
  	title: 'grid',
  	remark: '表格。',
  	extend: 'widget',
    Config: [
      { name: 'columns', type: 'Array', remark: '可显示列的设置项。', param: [
        { name: 'align', type: 'String', remark: '水平对齐方式。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
        { name: 'cls', type: 'String', remark: '样式名。' },
        { name: 'field', type: 'String', remark: '字段名。' },
        { name: 'format', type: 'String', remark: '格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值，可返回字符串或widget的json对象)。' },
        { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
        { name: 'highlight', type: 'Object', remark: '高亮关键词的配置。', param: [
          { name: 'key', type: 'String', remark: '关键词。' },
          { name: 'keycls', type: 'String', remark: '关键词样式名。' },
          { name: 'matchlength', type: 'Number', remark: '切词长度。' }
        ] },
        { name: 'minwidth', type: 'Number', remark: '列的最小宽度。只能用整数。' },
        { name: 'maxwidth', type: 'Number', remark: '列的最大宽度。只能用整数。' },
        { name: 'style', type: 'String', remark: '样式。' },
        { name: 'sort', type: 'Boolean | Object', remark: '设置当前列为可点击排序。如果设为true，则以当前列的值为排序依据。', param: [
          { name: 'field', type: 'String', remark: '排序字段名。' },
          { name: 'isnumber', type: 'Boolean', remark: '是否按数字方式排序。' },
          { name: 'status', type: 'String', remark: '当前排序状态。可选值: <b>desc</b>, <b>asc</b>。' },
          { name: 'src', type: 'Boolean', remark: '后端排序URL。点击标题将访问此地址，支持$0变量(可选值:<b>desc</b>, <b>asc</b>)。' }
        ] },
        { name: 'tip', type: 'Boolean | Object', remark: '浮动提示的字段名。如果设为true，使用当前字段值作为提示内容。', param: [
          { name: 'field', type: 'String', remark: '提示字段名。' }
        ] },
        { name: 'valign', type: 'String', remark: '垂直对齐方式。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
        { name: 'width', type: 'String | Percent | Number', remark: '列的宽度。可选值: *, 百分比, 整数。' }
      ], example: [
          function() {
          	// 使用文本格式化的列
            var wg = { type: 'grid', columns: [ { func: 'text', field: 'C1', width: 500, format: '<a href=view.jsp?id=$C0>$C1</a>' } ] };
          },
          function() {
          	// 使用 js 语法格式化的列
            // this 是当前行对象，本例中的 this.x.data.C0 也可用 $C0 来替代
            var wg = { type: 'grid', columns: [ { func: 'text', field: 'C1', width: 500, format: 'javascript:return myFormat(this.x.data.C0,$C1)' } ] };
          }
      ] },
      { name: 'combo', type: 'Object', remark: '设置当前的 tree 为某个 combobox 或 onlinebox 的数据选项表。', param: [
      	{ name: 'field', type: 'String', remark: '字段参数。', param: [
          { name: 'value', type: 'String', remark: '值字段名。' },
          { name: 'text', type: 'String', remark: '文本字段名。' },
          { name: 'search', type: 'String', remark: '搜索字段名。' },
          { name: 'remark', type: 'String', remark: '备注字段名。' },
          { name: 'forbid', type: 'String', remark: '禁用字段名。' }
        ] },
        { name: 'keepshow', type: 'Boolean', remark: '设置为true，无论是否有匹配到内容，都始终显示搜索结果框。' }
      ] },
      { name: 'escape', type: 'Boolean', remark: 'html内容转义。' },
      { name: 'face', type: 'String', remark: '表格行的样式。可选值: <b>line</b>(默认值，横线), <b>dot</b>(虚线), <b>cell</b>(横线和竖线), <b>none</b>(无样式)。' },
      { name: 'focusmultiple', type: 'Boolean', remark: '是否有多选的点击高亮效果。' },
      { name: 'limit', type: 'Int', remark: '最多显示多少行。如果需要前端翻页，可设置这个属性。' },
      { name: 'nobr', type: 'Boolean', remark: '内容不换行。' },
      { name: 'pub', type: 'Object', remark: '为每一行设置默认属性', example: [
          function() {
            // 设置每一行的高度为40，并绑定点击事件
            // 事件中的 this 是点击那一行的 grid/tr 对象
            var wg = { type: 'grid', defaults: { height: 40, on: { click: 'alert(this.x.data.C0)' } } };
          }
      ] },
      { name: 'rows', type: 'Array', remark: '行数据。支持简易模式和完整模式。', example: [
          function() {
          	// 简易模式的行数据
            var wg = {
              type: 'grid',
              columns: [
                { func: 'text', field: 'C0', width: '*' },
                { func: 'text', field: 'C1', width: '*' }
              ],
              rows: [
                { C0: '111', C1: '222' },
                { C0: '333', C1: '444' }
              ]
            };
          },
          function() {
          	// 完整模式的行数据
            var wg = {
              type: 'grid',
              columns: [
                { func: 'text', field: 'C0', width: '*' },
                { func: 'text', field: 'C1', width: '*' }
              ],
              rows: [
                { type: 'grid/tr', data: { C0: '111', C1: '222' } },
                { type: 'grid/tr', data: { C0: '333', C1: '444' } }
              ]
            };
          }
      ] },
      { name: 'resizable', type: 'Boolean', remark: '是否可以拖动表头调整列宽。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'tbody', type: 'Object', remark: '表格内容配置。', param: [
        { name: 'rows', type: 'Array', remark: '表格内容数据。' }
      ] },
      { name: 'thead', type: 'Object', remark: '表头配置。', param: [
        { name: 'fix', type: 'Boolean', remark: '是否固定表头。' },
        { name: 'rows', type: 'Array', remark: '表头数据。' }
      ] }
    ],
    Methods: [
      { name: 'draggable([option])', remark: '设置所有行可拖拽。', param: [
        { name: 'option', type: 'Object', remark: '拖拽参数。', optional: true, param: [
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true }
       ] }
      ] },
      { name: 'droppable([option])', remark: '设置所有行可放置。', param: [
        { name: 'option', type: 'Object', remark: '拖拽参数。', optional: true, param: [
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true },
          { name: 'sort', type: 'Boolean', remark: '是否可排序。', optional: true },
          { name: 'drop', type: 'Function(event, ui)', remark: '当一个可接受的 draggable 被放置在 droppable 上时触发。', optional: true, param: [
          	{ name: 'event', type: 'Event', remark: '放置事件。' },
          	{ name: 'ui', type: 'Object', remark: '辅助参数。', param: [
          		{ name: 'draggable', type: 'Widget', remark: '拖拽节点。' },
          		{ name: 'droppable', type: 'Widget', remark: '放置节点。' },
          		{ name: 'type', type: 'String', remark: '放置方式。可能的值："append","before","after"。' }
          	] }
          ] }
       ] }
      ], example: [
          function() {
            // 设置grid可拖拽也可放置
            vm.find( 'grid' ).draggable().droppable( {
              drop: function( ev, ui ) {
                var u = 'move.sp?act=move&from=' + ui.draggable.x.id + '&to=' + ui.droppable.x.id + '&type=' + ui.type;
                alert(u);
              }
            } );
          }
      ] },
      { name: 'row(data)', remark: '获取符合条件的某一行。', param: [
        { name: 'data', type: 'Object | Number', remark: '用来查询的字段对象，或行的序列号。' }
      ] },
      { name: 'rows([data])', remark: '获取符合条件的所有行。返回一个由若干 tr widget 实例组成的数组集合。', param: [
        { name: 'data', type: 'Object', remark: '用来查询的字段对象。', optional: true }
      ], example: [
          function() {
            // 获取所有行，并循环显示所有行的html
            var r = vm.find( 'myGrid' ).rows();
            for ( var i = 0; i < r.length; i ++ ) {
            	alert( r[ i ].$().outerHTML );
            }
          },
          function() {
            // 获取所有字段 C1 值为 "001" 的行
            var r = vm.find( 'myGrid' ).rows( { C1: '001' } );
            alert( r.length );
          }
      ] },
      { name: 'headrow([data])', remark: '获取标题行中符合条件的某一行。', param: [
        { name: 'data', type: 'Object | Number', remark: '用来查询的字段对象，或行的序列号。默认值为0。', optional: true }
      ] },
      { name: 'rowsData([data])', remark: '获取符合条件的所有行的 data json 的数组。', param: [
        { name: 'data', type: 'Object', remark: '用来查询的字段对象。', optional: true }
      ] },
      { name: 'getCheckedAll()', remark: '获取所有选中行，返回一个数组。' },
      { name: 'getEchoRows()', remark: '获取所有可显示的行，返回一个数组。' },
      { name: 'getFocus()', remark: '获取焦点行。' },
      { name: 'getFocusAll()', remark: '获取所有焦点行，返回一个数组。' },
      { name: 'focusRow(target)', remark: '设置焦点行。', param: [
        { name: 'target', type: 'Object | Number', remark: '查询目标行的字段对象，或目标行的序列号。' }
      ] },
      { name: 'insertRow(data, [target])', remark: '新增行。', param: [
        { name: 'data', type: 'Object | Array', remark: '新增行的JSON数据。同时新增多行可以用数组。' },
        { name: 'target', type: 'Object | Number', remark: '查询目标行的字段对象，或目标行的序列号。新增行的位置将在目标行之前。如果不设置此参数，新增位置为末尾。', optional: true }
      ], example: [
          function() {
            // { C1: '000' } 是新增行的数据，把它新增到 { C1: '001' } 之前。本例和下例效果相同。
            vm.find( 'myGrid' ).insertRow( { C1: '000' }, { C1: '001' } );
          },
          function() {
            // { C1: '000' } 是新增行的数据，把它新增到 { C1: '001' } 之前。本例和上例效果相同。
            var r = vm.find( 'myGrid' ).row( { C1: '001' } );
            r.before( { C1: '000' } );
          }
      ] },
      { name: 'updateRow(data, target)', remark: '更新行。', param: [
        { name: 'data', type: 'Object', remark: '更新行的JSON数据。' },
        { name: 'target', type: 'Object | Number', remark: '查询目标行的字段对象，或目标行的序列号。' }
      ] },
      { name: 'deleteRow(target)', remark: '删除行。', param: [
        { name: 'target', type: 'Object | Number', remark: '查询目标行的字段对象，或目标行的序列号。' }
      ] },
      { name: 'deleteAllRows()', remark: '删除所有行。', param: [
      ] },
      { name: 'moveRow(target, index)', remark: '移动行。', param: [
        { name: 'target', type: 'Object', remark: '查询目标行的字段对象，或目标行的序列号。' },
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
        { name: 'data', type: 'Object', remark: '包含一列的数据，格式为 grid json。' },
        { name: 'index', type: 'Number | String', remark: '如果是数字，表示插入到序列号为 index 的那一列之前；如果是字符串，表示插入到 column.field == index 的那一列之前。如果不传此参数，表示插入到最后。', optional: true }
      ], example: [
          function() {
            // 插入到最后一列
            vm.find( 'myGrid' ).insertColumn( {
            	columns: [
            	  { field: 'C3', width: 100 }
            	],
            	thead: {
            	  rows: [ { C3: 'C3-title' } ]
            	},
            	rows: [
            	  { C3: 'C3-content0' },
            	  { C3: 'C3-content1' }
            	]
            } );
          }
      ] },
      { name: 'updateColumn(data, index)', remark: '更新一列。', param: [
        { name: 'data', type: 'Object', remark: '包含一列的数据，格式为 grid json。' },
        { name: 'index', type: 'Number | String', remark: '如果是数字，表示更新序列号为 index 的那一列；如果是字符串，表示更新 column.field == index 的那一列。' }
      ], example: [
          function() {
            // 更新 C3 字段
            vm.find( 'myGrid' ).updateColumn( {
            	columns: [
            	  { field: 'C3', width: 100 }
            	],
            	thead: {
            	  rows: [ { C3: 'C3-title' } ]
            	},
            	rows: [
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
            vm.find( 'myGrid' ).deleteColumn( 0 );
          }
      ] },
      { name: 'page(index)', remark: '翻页。', param: [
        { name: 'index', type: 'Number', remark: '页数。从 0 开始计数。' }
      ] },
      { name: 'isScrollBottom()', remark: '滚动条是否滚动到了底部。' }
    ],
    Classes: [
      { name: '.w-grid', remark: '基础样式。' }
    ]
  },
  "horz": {
  	title: 'horz',
  	remark: '子节点按水平方向排列的布局widget。子节点的高度默认为100%；宽度可以设置数字,百分比,*。如果宽度设为-1，表示自适应宽度。',
  	extend: 'widget',
    Config: [
      { name: 'align', type: 'String', remark: '水平对齐方式。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'valign', type: 'String', remark: '垂直对齐方式。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。<br>一般情况下，如果希望纵向滚动，内部子节点高度应该设为-1；如果希望横向滚动，子节点宽度应该设为-1。' },
      { name: 'swipedown', type: 'String', remark: '下拉刷新的URL地址。', mobile: true }
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
      { name: '.w-horz', remark: '基础样式。' }
    ]
  },
  "vert": {
  	title: 'vert',
  	remark: '子节点按垂直方向排列的布局widget。子节点的宽度默认为100%；高度可以设置数字,百分比,*。如果高度设为-1，表示自适应高度。',
  	extend: 'horz',
  	deprecate: '.w-horz',
    Classes: [
      { name: '.w-vert', remark: '基础样式。' }
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
      { name: 'isModified([range], [original])', remark: '检测表单是否有修改，对照的值为默认值。如果有修改则返回被修改的表单widget。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true },
        { name: 'original', type: 'Boolean', remark: '设置为true，检测表单是否有修改，对照的值为初始值。', optional: true }
      ] },
      { name: 'saveModified([range])', remark: '把表单当前的值设置为默认值。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true }
      ] },
      { name: 'valid([group], [range])', remark: '表单验证。验证通过返回true；验证出错将提示错误信息，并返回false。', param: [
        { name: 'group', type: 'String', remark: '验证组名。默认值为 "default"。', optional: true },
        { name: 'range', type: 'String', remark: '验证范围，某个 widget 的 ID。', optional: true }
      ] }
    ]
  },
  "view": {
  	title: 'view',
  	remark: '视图对象。',
  	extend: 'widget',
  	deprecate: 'ownerView',
    Config: [
      { name: 'id', type: 'String', remark: 'View 设置 id 后将产生一个 path。并可通过 VM( path ) 方法获取view。', example: [
          function() {
          	// 在页面上生成一个view，并通过 path 来获取
            $.widget( { type: 'view', id: 'index', width: 500, height: 500, src: 'a.sp' } ).render( document.body );
            alert( VM( '/index' ) );
          }
      ] },
      { name: 'src', type: 'String', remark: '加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。src 参数和 node 参数不应同时使用。src 通过 ajax 加载；node 是直接展示。', example: [
          function() {
          	//
            var opt = { type: 'view', src: 'index.sp' }
          }
      ] },
      { name: 'base', type: 'String', remark: '给当前view里的所有ajax请求指定一个默认地址。' },
      { name: 'node', type: 'Object', remark: 'View的子节点，直接展示的内容。', example: [
          function() {
          	//
            var opt = { type: 'view', node: { type: 'html', text: '内容..' } }
          }
      ] }
    ],
    Event: [
      { name: 'load', remark: '数据加载完毕并展示后触发。', example: [
          function() {
          	// view加载完毕后显示path
            var opt = { type: 'view', id: 'myview', src: 'abc.sp', on: { load: "alert(this.path)" } };
          }
      ] }
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
        { name: 'range', type: 'HTMLElement | widget', remark: '指定获取表单的范围，可以是html元素或widget对象。', optional: true }
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
      { name: 'reload([src])', remark: '重新装载view。', param: [
        { name: 'src', type: 'String', remark: '获取数据的URL地址。', optional: true }
      ] },
      { name: 'resetForm([range], [empty])', remark: '重置表单。', param: [
        { name: 'range', type: 'String', remark: 'widget ID，多个用逗号隔开。指定表单的范围。', optional: true },
        { name: 'empty', type: 'Boolean', remark: '设置为true，强制清空值。', optional: true }
      ] },
      { name: 'isModified([range], [original])', remark: '检测表单是否有修改，对照的值为默认值。如果有修改则返回 true。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true },
        { name: 'original', type: 'Boolean', remark: '设置为true，检测表单是否有修改，对照的值为初始值。', optional: true }
      ] },
      { name: 'saveModified([range])', remark: '把表单当前的值设置为默认值。', param: [
        { name: 'range', type: 'String', remark: 'widget ID。多个 widget ID 用逗号隔开。', optional: true }
      ] },
      { name: 'valid([group], [range])', remark: '表单验证。验证通过返回true；验证出错将提示错误信息，并返回false。', param: [
        { name: 'group', type: 'String', remark: '验证组名。默认值为 "default"。', optional: true },
        { name: 'range', type: 'String', remark: '验证范围，某个 widget 的 ID。', optional: true }
      ] }
    ],
    Classes: [
      { name: '.w-view', remark: '基础样式。' }
    ]
  },
  "tr": {
  	title: 'tr',
  	remark: '表格行。',
  	extend: 'widget',
    Config: [
      { name: 'data', type: 'Object', remark: '行数据。' },
      { name: 'focus', type: 'Boolean', remark: '是否高亮。' },
      { name: 'src', type: 'String', remark: '可展开内容的地址。这个 src 应当返回一个 view' },
      { name: 'rows', type: 'Array', remark: '子节点数组。这些子节点也应该是 tr 类型。' }
    ],
    Event: [
      { name: 'collapse', remark: '收起时触发。' },
      { name: 'expand', remark: '展开时触发。' },
      { name: 'load', remark: '经 src 加载子节点完毕时触发。' }
    ],
    Properties: [
      { name: 'rootNode', type: 'Grid', remark: 'tr所属的grid。' }
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
    ]
  },
  "td": {
  	title: 'td',
  	remark: '表格单元格。',
  	extend: 'widget',
    Config: [
      { name: 'colspan', type: 'Number', remark: '跨行数。' },
      { name: 'rowspan', type: 'Number', remark: '跨列数。' },
      { name: 'align', type: 'String', remark: '水平对齐。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
      { name: 'valign', type: 'String', remark: '垂直对齐。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
      { name: 'node', type: 'Object', remark: '子节点。' },
      { name: 'text', type: 'String', remark: '显示文本。' }
    ]
  },
  "album": {
  	title: 'album',
  	remark: '图片集。',
  	extend: 'widget',
    Config: [
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。' },
      { name: 'format', type: 'String', remark: '格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'focusmultiple', type: 'Boolean', remark: '是否可多选。' },
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。album的子节点类型为"img"' },
      { name: 'pub', type: 'Object', remark: '子节点的默认配置项。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'space', type: 'Number', remark: '图片之间的间隔。' }
    ],
    Methods: [
      { name: 'focusAll([bFocus])', remark: '使子节点全选/全不选。', param: [
        { name: 'bFocus', type: 'Boolean', optional: true, remark: '是否全选。' }
      ] },
      { name: 'getFocus()', remark: '获取焦点图widget。' },
      { name: 'getFocusAll()', remark: '获取所有焦点图widget，返回一个数组。' }
    ],
    Classes: [
      { name: '.w-album', remark: '基础样式。' },
      { name: '.z-face-straight', remark: '当设置参数 face:"straight" 时的样式。' }
    ]
  },
  "img": {
  	title: 'img',
  	remark: '图片。img 是 album 的专属子节点类型。',
  	extend: 'widget',
    Config: [
      { name: 'box', type: 'Object', remark: '选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。', param: [
        { name: 'type',    type: 'String',  remark: '类型。可选值: <b>checkbox</b>, <b>radio</b>' },
        { name: 'name',    type: 'String',  remark: '表单名。' },
        { name: 'value',   type: 'String',  remark: '表单值。' },
        { name: 'text',    type: 'String',  remark: '显示文本。', optional: true },
        { name: 'checked', type: 'Booelan', remark: '是否默认选中。', optional: true },
        { name: 'target',  type: 'String | Widget', remark: '绑定 widget 或 widgetID，同步 disabled 属性。', optional: true }
      ] },
      { name: 'description', type: 'String', remark: '图片说明。当 album face="straight" 时会显示说明。' },
      { name: 'face', type: 'String', remark: '图片展现方式。可选值: <b>none</b>, <b>straight</b>。' },
      { name: 'focusable', type: 'Boolean', remark: '是否可选中。' },
      { name: 'imgwidth', type: 'Number | String', remark: '图片宽度。' },
      { name: 'imgheight', type: 'Number | String', remark: '图片高度。' },
      { name: 'textwidth', type: 'Number | String', remark: '文本宽度。' },
      { name: 'nobr', type: 'Boolean', remark: '文本是否换行。' },
      { name: 'src', type: 'String', remark: '图片地址。' },
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
      { name: '.z-on', remark: '焦点高亮样式。需要设置属性 focusable:true' }
    ]
  },
  "tree": {
  	title: 'tree',
  	remark: '树。',
  	extend: 'widget',
    Config: [
      { name: 'combo', type: 'Object', remark: '设置当前的 tree 为某个 combobox 或 onlinebox 的数据选项表。', param: [
      	{ name: 'field', type: 'String', remark: '字段参数。', param: [
          { name: 'value', type: 'String', remark: '值字段名。' },
          { name: 'text', type: 'String', remark: '文本字段名。' },
          { name: 'search', type: 'String', remark: '搜索字段名。' },
          { name: 'remark', type: 'String', remark: '备注字段名。' },
          { name: 'forbid', type: 'String', remark: '禁用字段名。' }
        ] },
        { name: 'keepshow', type: 'Boolean', remark: '设置为true，无论是否有匹配到内容，都始终显示搜索结果框。' }
      ] },
      { name: 'ellipsis', type: 'Boolean', remark: '设置为true，树节点文本超出可视范围部分以省略号显示。' },
      { name: 'escape', type: 'Boolean', remark: '是否对html内容转义。' },
      { name: 'hiddens', type: 'Array', remark: '隐藏表单的数组。' },
      { name: 'highlight', type: 'Object', remark: '高亮关键词的配置。', param: [
        { name: 'key', type: 'String', remark: '关键词。' },
        { name: 'keycls', type: 'String', remark: '关键词样式名。' },
        { name: 'matchlength', type: 'Number', remark: '切词长度。' }
      ] },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' },
      { name: 'pub', type: 'Object', remark: '子节点的默认配置项。' },
      { name: 'src', type: 'String', remark: '获取子节点的 URL 地址。' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' }
    ],
    Methods: [
      { name: 'draggable([option])', remark: '设置所有leaf可拖拽。', param: [
        { name: 'option', type: 'Object', remark: '拖拽参数。', optional: true, param: [
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true }
       ] }
      ] },
      { name: 'droppable([option])', remark: '设置所有leaf可放置。', param: [
        { name: 'option', type: 'Object', remark: '拖拽参数。', optional: true, param: [
          { name: 'scope', type: 'String', remark: '用于组合配套 draggable 和 droppable 项。一个与 droppable 带有相同的 scope 值的 draggable 会被该 droppable 接受。多个scope用逗号隔开。', optional: true },
          { name: 'sort', type: 'Boolean', remark: '是否可排序。', optional: true },
          { name: 'drop', type: 'Function(event, ui)', remark: '当一个可接受的 draggable 被放置在 droppable 上时触发。', optional: true, param: [
          	{ name: 'event', type: 'Event', remark: '放置事件。' },
          	{ name: 'ui', type: 'Object', remark: '辅助参数。', param: [
          		{ name: 'draggable', type: 'Widget', remark: '拖拽节点。' },
          		{ name: 'droppable', type: 'Widget', remark: '放置节点。' },
          		{ name: 'type', type: 'String', remark: '放置方式。可能的值："append","before","after"。' }
          	] }
          ] }
       ] }
      ], example: [
          function() {
            // 设置树可拖拽也可放置
            vm.find( 'tree' ).draggable().droppable( {
              drop: function( ev, ui ) {
                var u = 'move.sp?act=move&from=' + ui.draggable.x.id + '&to=' + ui.droppable.x.id + '&type=' + ui.type;
                alert(u);
              }
            } );
          }
      ] },
      { name: 'getFocus()', remark: '获取焦点状态的 leaf。' },
      { name: 'openTo(src, [sync], [fn])', remark: '通过src请求获取一个 json，并按照这个 json 的格式显示树。每个 leaf 节点都必须设置 id。', param: [
        { name: 'src',  type: 'String',  remark: '获取 tree json 的地址。' },
        { name: 'sync', type: 'Boolean',  remark: '是否同步。' },
        { name: 'fn', type: 'Function',  remark: '请求结束后执行的回调函数。' }
      ] },
      { name: 'reload()', remark: '重新装载子节点。' },
      { name: 'reloadForAdd([sync], [fn])', remark: '重新读取当前节点的 src 获取子节点数据，如果有新的子节点，将会显示这些新节点。', param: [
        { name: 'sync', type: 'Booelan', optional: true, remark: '是否同步。true: 同步; false: 异步。' },
        { name: 'fn', type: 'Function', optional: true, remark: '节点更新完毕后执行的回调函数。' }
      ], example: [
          function() {
          	// 
            vm.find( 'myLeaf' ).reloadForAdd();
          }
      ] }
    ],
    Classes: [
      { name: '.w-tree', remark: '基础样式。' }
    ]
  },
  "leaf": {
  	title: 'leaf',
  	remark: '树节点。',
  	extend: 'widget',
    Config: [
      { name: 'box', type: 'Object', remark: '选项表单，类型是 checkbox 或 radio。取消或勾选这个 box，将同步 fieldset 内部所有表单的状态。', param: [
        { name: 'type',    type: 'String',  remark: '类型。可选值: <b>checkbox</b>, <b>radio</b>, <b>triplebox</b>' },
        { name: 'name',    type: 'String',  remark: '表单名。' },
        { name: 'value',   type: 'String',  remark: '表单值。' },
        { name: 'text',    type: 'String',  remark: '显示文本。', optional: true },
        { name: 'checked', type: 'Booelan', remark: '是否默认选中。', optional: true },
        { name: 'target',  type: 'String | Widget', remark: '绑定 widget 或 widgetID，同步 disabled 属性。', optional: true }
      ] },
      { name: 'focus', type: 'Boolean', remark: '是否焦点状态。' },
      { name: 'hidetoggle', type: 'Boolean', remark: '是否隐藏 toggle 图标。' },
      { name: 'highlight', type: 'Object', remark: '高亮关键词的配置。', param: [
        { name: 'key', type: 'String', remark: '关键词。' },
        { name: 'keycls', type: 'String', remark: '关键词样式名。' },
        { name: 'matchlength', type: 'Number', remark: '切词长度。' }
      ] },
      { name: 'icon', type: 'String', remark: '图标。可使用图片url地址，或以 "." 开头的样式名。' },
      { name: 'format', type: 'String', remark: '格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。' },
      { name: 'nodes', type: 'Array', remark: '子节点集合。' },
      { name: 'open', type: 'Boolean', remark: '是否展开状态。' },
      { name: 'src', type: 'String', remark: '获取子节点的 URL 地址。' },
      { name: 'status', type: 'String', remark: '节点状态。可选值：<b>normal</b>, <b>disabled</b>。' },
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
          	// 
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
      { name: '.z-folder', remark: '有子节点时的样式。' }
    ]
  },
  "html": {
  	title: 'html',
  	remark: '展示html内容。',
  	extend: 'widget',
    Config: [
      { name: 'align', type: 'String', remark: '水平对齐。可选值: <b>left</b>, <b>center</b>, <b>right</b>' },
      { name: 'valign', type: 'String', remark: '垂直对齐。可选值: <b>top</b>, <b>middle</b>, <b>bottom</b>' },
      { name: 'scroll', type: 'Boolean', remark: '是否有滚动条。' },
      { name: 'text', type: 'String', remark: 'html内容。支持 &lt;d:wg&gt; 标签。' },
      { name: 'thumbwidth', type: 'Number | String', remark: '设置内容区域所有图片的最大宽度。点击图片可以预览大图。' }
    ],
    Methods: [
      { name: 'text(content)', remark: '更新内容。', param: [
        { name: 'content', type: 'String', remark: 'HTML内容。支持 &lt;d:wg&gt; 标签。' }
      ] }
    ],
    Classes: [
      { name: '.w-html', remark: '基础样式。' }
    ]
  },
  "grid/leaf": {
  	title: 'grid/leaf',
  	remark: '用于grid的树节点。',
  	extend: 'leaf',
    Methods: [
      { name: 'tr()', remark: '获取leaf所在的tr行对象。' }
    ]
  },
  "grid/rownum": {
  	title: 'grid/rownum',
  	remark: '用于grid的自增数字字段。',
  	extend: 'widget',
    Config: [
      { name: 'start', type: 'Number', remark: '初始值。默认值为1' }
    ],
    Classes: [
      { name: '.w-grid-rownum', remark: '基础样式。' }
    ]
  },
  "progress": {
  	title: 'progress',
  	remark: '进度条。',
  	extend: 'widget',
    Config: [
      { name: 'delay', type: 'Number', remark: '延迟访问 src 。单位:秒。' },
      { name: 'percent', type: 'Number', remark: '进度值。范围从 0 到 100。' },
      { name: 'src', type: 'String', remark: '访问这个地址返回一个命令json。如果返回的是 progress json，当前实例将被替换。' },
      { name: 'text', type: 'String', remark: '显示文本。' }
    ],
    Methods: [
      { name: 'stop()', remark: '停止由 delay 参数引起的延迟。' },
      { name: 'start()', remark: '继续 delay 参数的延迟执行。' }
    ],
    Classes: [
      { name: '.w-progress', remark: '基础样式。' }
    ]
  },
  "ewin": {
  	title: 'ewin',
  	remark: '生成一个内嵌窗口。',
  	extend: 'widget',
    Config: [
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
    ]
  },
  "toggle": {
  	title: 'toggle',
  	remark: '展开收拢的工具条。',
  	extend: 'widget',
    Config: [
      { name: 'icon', type: 'String', optional: true, remark: '收拢时的图标。可用 . 开头的样式名，或图片路径。' },
      { name: 'openicon', type: 'String', optional: true, remark: '展开时的图标。可用 . 开头的样式名，或图片路径。' },
      { name: 'hr', type: 'Boolean', optional: true, remark: '显示一条水平线。' },
      { name: 'open', type: 'Boolean', optional: true, remark: '设置初始状态为展开或收拢。如果设置了此参数，会产生一个toggle图标' },
      { name: 'target', type: 'String', remark: '绑定要展开收拢的 widget ID。多个用逗号隔开。' },
      { name: 'text', type: 'String', remark: '显示文本。' }
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
      { name: '.z-collapse', remark: '当 toggle 收起时生效。' }
    ]
  },
  "split": {
  	title: 'split',
  	remark: '分割线。可用于 vert, horz, menu, buttonbar 中。',
  	extend: 'widget',
    Config: [
      { name: 'range', type: 'String', optional: true, remark: '设置拖动调整大小的前后范围。只在父节点为 vert, horz 时可用。此参数由2-3个数字组成，以逗号隔开。第一个数字表示前一个节点的最小size，第二个数字表示后一个节点的最小size，第三个数字可选，表示 toggle 节点的初始size。', example: [
          function() {
            // 横向布局的3个widget, 拖动中间的分割线调整大小
            var opt = { type: 'horz', nodes:[
                { type: 'html',  width: '*', text: 'aaa' },
                { type: 'split', width: 1, style: 'background:blue', range: '100,100' },
                { type: 'html',  width: '*', text: 'bbb' }
            ] }
          }
      ] },
      { name: 'icon',  type: 'String', optional: true, remark: '收拢图标。图片地址url，或是以点 "." 开头的样式名。' },
      { name: 'openicon',  type: 'String', optional: true, remark: '展开图标。图片地址url，或是以点 "." 开头的样式名。' },
      { name: 'target',  type: 'String', optional: true, remark: '指定展开收拢的节点位置。可选值: <b>prev</b><font color=green>(默认,前节点)</font>, <b>next</b><font color=green>(后节点)</font>。本参数配合 icon openicon 参数一起使用。' },
      { name: 'text',  type: 'String', optional: true, remark: '显示文本。' }
    ],
    Classes: [
      { name: '.w-split', remark: '基础样式。' }
    ]
  },
  "page/mini": {
  	title: 'page/mini',
  	remark: '小按钮风格的翻页工具条。',
  	extend: 'widget',
    Config: [
      { name: 'align', type: 'String', optional: true, remark: '水平居中。可选值: <b>left</b>, <b>right</b>, <b>center</b>' },
      { name: 'btncls', type: 'String', optional: true, remark: '按钮样式。' },
      { name: 'btncount', type: 'Number', optional: true, remark: '数字页数按钮的数量。' },
      { name: 'btnsumpage', type: 'Boolean', optional: true, remark: '显示总页数按钮。' },
      { name: 'currentpage', type: 'Number', remark: '当前页数。(起始值为1)' },
      { name: 'info', type: 'String', optional: true, remark: '显示总条数和总页数等信息。' },
      { name: 'jump', type: 'Boolean', optional: true, remark: '显示一个可填写页数的表单。' },
      { name: 'name', type: 'String', optional: true, remark: '如果设置了name，将生成一个隐藏表单项，值为当前页数。' },
      { name: 'nofirstlast', type: 'Boolean', optional: true, remark: '不显示"首页"和"尾页"两个按钮。' },
      { name: 'src', type: 'String', optional: true, remark: '点击页数按钮将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表页数。支持以 "javascript:" 开头的JS语句。' },
      { name: 'sumpage', type: 'Number', remark: '总页数。(起始值为1)' },
      { name: 'setting', type: 'Array', remark: 'button数组。生成一个配置按钮和下拉菜单。', example: [
          function() {
            //
            var opt = { type: 'page/mini', setting: [
            	{ "text": "显示设置" },
            	{ "text": "每页显示" }
            ] }
          }
      ] },
      { name: 'target', type: 'Boolean', optional: true, remark: '绑定一个支持前端翻页的widget(例如grid)。' },
      { name: 'transparent', type: 'Boolean', optional: true, remark: '设置为true，可去除边框背景等预设样式。' }
    ],
    Methods: [
      { name: 'val([page])', remark: '设置/获取当前页数。', param: [
        { name: 'page', type: 'Number', remark: '当前页数。如果设置了此参数，则跳转到这一页；如果不设置此参数，则返回当前页数值。' }
      ] }
    ],
    Classes: [
      { name: '.w-page', remark: '基础样式。' },
      { name: '.w-page-mini', remark: '基础样式。' }
    ]
  },
  "page/text": {
  	title: 'page/text',
  	remark: '文本风格的翻页工具条。',
  	extend: 'page/mini',
  	deprecate: '.w-page-mini',
    Config: [
      { name: 'labelfirst', type: 'String', optional: true, remark: '"首页"的文本。' },
      { name: 'labellast', type: 'String', optional: true, remark: '"尾页"的文本。' },
      { name: 'labelnext', type: 'String', optional: true, remark: '"下一页"的文本。' },
      { name: 'labelprev', type: 'String', optional: true, remark: '"上一页"的文本。' }
    ],
    Classes: [
      { name: '.w-page-text', remark: '基础样式。' }
    ]
  },
  "page/buttongroup": {
  	title: 'page/buttongroup',
  	remark: '组合按钮风格的翻页工具条。',
  	extend: 'page/mini',
  	deprecate: 'nofirstlast,.w-page-mini,transparent',
    Config: [
      { name: 'btncount', type: 'Number', optional: true, remark: '中间是否有显示页数的按钮。值为0或1。' },
      { name: 'dropalign', type: 'String', optional: true, remark: '下拉按钮的位置，可选值: <b>left</b>, <b>center</b>, <b>right</b>。' }
    ],
    Classes: [
      { name: '.w-page-buttongroup', remark: '基础样式。' }
    ]
  },
  "calendar/date": {
  	title: 'calendar/date',
  	remark: '日历。',
  	extend: 'widget',
    Config: [
      { name: 'date', type: 'Number', optional: true, remark: '以此日期为基准显示一个月的日期。格式 yyyy-mm-dd' },
      { name: 'focusdate', type: 'Number', optional: true, remark: '高亮显示的某一日期。格式 yyyy-mm-dd' },
      { name: 'src', type: 'String', optional: true, remark: '点击日期将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表日期。' },
      { name: 'css', type: 'Object', optional: true, remark: '设置一组日期样式。', example: [
          function() {
            // 按顺序对应value中值为Y的日期设置绿色字体，值为N的日期设置红色字体
            var opt = { type: 'canlendar/date', css: { value: 'NNNNNNNNYYNNNNNNNNYYYYYYYNNNNNN', N: 'color:red;', Y : 'color:green;' } }
          }
      ] },
      { name: 'pub', type: 'Object', optional: true, remark: '日期按钮的公共设置。', example: [
          function() {
            // 点击日期按钮显示日期值。
            var opt = { type: 'canlendar/date', pub: { on: { click: 'alert(this.val())' } } }
          }
      ] }
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
    ]
  },
  "calendar/week": {
  	title: 'calendar/week',
  	remark: '周历。',
  	extend: 'calendar/date',
  	deprecate: '.w-calendar-date',
    Config: [
      { name: 'date', type: 'Number', optional: true, remark: '以此日期为基准显示一年的周列表。格式 yyyy-mm-dd' },
      { name: 'focusdate', type: 'Number', optional: true, remark: '高亮显示的某一周。格式 yyyy-mm-dd' }
    ],
    Classes: [
      { name: '.w-calendar-week', remark: '基础样式。' }
    ]
  },
  "calendar/month": {
  	title: 'calendar/month',
  	remark: '月历。',
  	extend: 'calendar/date',
  	deprecate: '.w-calendar-date',
    Config: [
      { name: 'date', type: 'Number', optional: true, remark: '以此日期为基准显示一年的月列表。格式 yyyy-mm' },
      { name: 'focusdate', type: 'Number', optional: true, remark: '高亮显示的某一月。格式 yyyy-mm' }
    ],
    Classes: [
      { name: '.w-calendar-month', remark: '基础样式。' }
    ]
  },
  "calendar/year": {
  	title: 'calendar/year',
  	remark: '年历。',
  	extend: 'calendar/date',
  	deprecate: '.w-calendar-date',
    Config: [
      { name: 'date', type: 'Number', optional: true, remark: '以此日期为基准显示十年的年份列表。格式 yyyy' },
      { name: 'focusdate', type: 'Number', optional: true, remark: '高亮显示的某一年。格式 yyyy' }
    ],
    Classes: [
      { name: '.w-calendar-year', remark: '基础样式。' }
    ]
  },
  "text": {
  	title: 'text',
  	remark: '单行文本输入框。',
  	extend: 'widget',
    Config: [
      { name: 'label', type: 'String', optional: true, remark: '字段描述。这个描述用于校验提示中。' },
      { name: 'name', type: 'String', remark: '表单名。' },
      { name: 'placeholder', type: 'String', optional: true, remark: '当表单没有值时显示的提示文本。' },
      { name: 'status', type: 'String', optional: true, remark: '表单状态。可选值: <b>normal</b><font color=green>(默认)</font>, <b>readonly</b><font color=green>(只读，不验证数据)</font>, <b>validonly</b><font color=green>(只读，验证数据)</font>, <b>disabled</b><font color=green>(禁用)</font>。' },
      { name: 'tip', type: 'Boolean | string', remark: '提示信息。如果设置为true，提示内容为当前的文本。' },
      { name: 'transparent', type: 'Boolean', optional: true, remark: '设置为true，表单将成为无边框无背景的状态。' },
      { name: 'value', type: 'String', remark: '表单值。' },
      { name: 'validate', type: 'Object', optional: true, remark: '表单校验选项。',  param: [
        { name: 'required', type: 'Boolean', remark: '必填。' },
        { name: 'requiredtext', type: 'String', remark: '必填提示文本。' },
        { name: 'pattern', type: 'String', remark: '正则表达式。' },
        { name: 'patterntext', type: 'String', remark: '正则提示文本。' },
        { name: 'compare', type: 'String', remark: '另一个表单的name。用于简单的比较。' },
        { name: 'comparemode', type: 'String', remark: '比较符号，可选值: > >= < <= ==。' },
        { name: 'comparetext', type: 'String', remark: '比较提示文本。' },
        { name: 'minlength', type: 'Number', remark: '最小字节数。' },
        { name: 'minlengthtext', type: 'String', remark: '最小字节数提示文本。' },
        { name: 'maxlength', type: 'Number', remark: '最大字节数。用于 text textarea password' },
        { name: 'maxlengthtext', type: 'String', remark: '最大字节数提示文本。' },
        { name: 'minvalue', type: 'String', remark: '最小值。用于 spinner date' },
        { name: 'minvaluetext', type: 'String', remark: '最小值提示文本。' },
        { name: 'maxvalue', type: 'String', remark: '最大值。用于 spinner date' },
        { name: 'maxvaluetext', type: 'String', remark: '最大值提示文本。' },
        { name: 'minsize', type: 'Number', remark: '最少选择几项。用于 checkbox' },
        { name: 'minsizetext', type: 'String', remark: '最少选择几项提示文本。' },
        { name: 'maxsize', type: 'Number', remark: '最多选择几项。用于 checkbox' },
        { name: 'maxsizetext', type: 'String', remark: '最多选择几项提示文本。' },
        { name: 'beforenow', type: 'Boolean', remark: '不能大于当前时间。用于 date' },
        { name: 'beforenowtext', type: 'String', remark: '不能大于当前时间的显示文本。' },
        { name: 'afternow', type: 'Boolean', remark: '不能小于当前时间。用于 date' },
        { name: 'afternowtext', type: 'String', remark: '不能小于当前时间的显示文本。' },
        { name: 'method', type: 'String', remark: 'JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。' }
      ] },
      { name: 'validategroup', type: 'Object', optional: true, remark: '附加的表单校验选项。' }
    ],
    Event: [
      { name: 'change', remark: '改变文本值时触发。' },
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
      { name: 'isModified([original])', remark: '检测表单是否有修改。', param: [
        { name: 'original', type: 'Boolean', remark: '设为true，检测表单是否有修改，对照参考的值为初始值。', optional: true }
      ] },
      { name: 'saveModified()', remark: '把当前的表单值设置默认值。' },
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
      { name: '.w-form', remark: '所有表单 widget 具有此样式。' },
      { name: '.w-input', remark: '所有类似 text 的表单 widget (有一个外框，包含显示表单值)具有此样式。像 radio checkbox 等不具备此特征的表单就没有这个样式。' },
      { name: '.z-on', remark: '处于焦点状态时的样式。' },
      { name: '.z-required', remark: '必填状态的样式。' },
      { name: '.z-trans', remark: '设置 transparent:true 时的样式。' },
      { name: '.z-ds', remark: '设置 status 为 readonly,validonly,disabled 时的样式。' },
      { name: '.z-err', remark: '表单验证出错时的样式。' }
    ]
  },
  "textarea": {
  	title: 'textarea',
  	remark: '多行文本输入框。',
  	extend: 'text',
  	deprecate: '.w-text',
    Classes: [
      { name: '.w-textarea', remark: '基础样式。' }
    ]
  },
  "password": {
  	title: 'password',
  	remark: '密码输入框。',
  	extend: 'text',
  	deprecate: '.w-text',
    Config: [
      { name: 'autocomplete', type: 'Boolean', optional: true, remark: '是否允许自动填充保存的密码。默认值为false。' }
    ],
    Classes: [
      { name: '.w-password', remark: '基础样式。' }
    ]
  },
  "checkboxgroup": {
  	title: 'checkboxgroup',
  	remark: '复选表单组。',
  	extend: 'text',
  	deprecate: 'placeholder,tip,transparent,value,focus,focusEnd,.z-trans,.w-text,.w-input,status',
    Config: [
      { name: 'dir', type: 'String', remark: '排列方向。可选值: <b>h</b><font color=green>(横向,默认)</font>,<b>v</b><font color=green>(纵向)</font>' },
      { name: 'options', type: 'Array', remark: 'checkbox 节点数组。' },
      { name: 'targets', type: 'Array', optional: true, remark: '和 checkbox 一一对应的节点数组。勾选复选框将改变 target 节点的 disabled 状态。' },
      { name: 'pub', type: 'Object', optional: true, remark: 'checkbox 的默认参数。这里的参数不会应用于 targets 中的 widget。 ', example: [
          function() {
            // 设置 checkbox 宽度为 50%, 可以形成整齐的两列排列
            var box = { type: 'checkbox', name: 'box', pub: { width: '50%' }, options: [ { text: '选项1' }, { text: '选项2' } ] }
          }
      ] },
      { name: 'space', type: 'Number', optional: true, remark: '当设置了 targets，再设置 space 可调整行间距。' }
    ],
    Methods: [
      { name: 'checkAll([checked])', remark: '设置全选/不选。', param: [
        { name: 'checked', type: 'Boolean', remark: '是否可用。', optional: true }
      ] }
    ],
    Classes: [
      { name: '.w-checkboxgroup', remark: '基础样式。' }
    ]
  },
  "checkbox": {
  	title: 'checkbox',
  	remark: '复选项。',
  	extend: 'text',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.w-input,.z-trans,.z-on',
    Config: [
      { name: 'bubble', type: 'Boolean', remark: '点击事件是否冒泡。用于 leaf 或 tr 的选项box。', optional: true },
      { name: 'checked', type: 'Boolean', remark: '是否选中。', optional: true },
      { name: 'nobr', type: 'Boolean', remark: '设置为true，文本不换行。', optional: true },
      { name: 'sync', type: 'String', remark: '选中状态跟父节点同步，用于 leaf 或 tr 的选项box。可选值: <b>click</b><font color=green>(点击父节点，box也触发点击)</font>, <b>focus</b><font color=green>(父节点聚焦则box则选中，父节点失去焦点则box未选中)</font>', optional: true },
      { name: 'target', type: 'String', remark: 'widget ID。使这个 widget 和当前 option 的 disabled 状态同步。', optional: true },
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
      { name: 'getSibling([checked])', remark: '获取所有相同name的兄弟节点。', param: [
        { name: 'checked', type: 'Boolean', remark: '设为 true，获取所有选中的同名节点；设为 false，获取所有未选的同名节点。不设此参数，获取所有同名节点。', optional: true }
      ] }
    ],
    Classes: [
      { name: '.w-checkbox', remark: '基础样式。' }
    ]
  },
  "triplebox": {
  	title: 'triplebox',
  	remark: '有三种状态的复选项。',
  	deprecate: 'focus,focusEnd,placeholder,transparent,checked,.w-text,.z-trans',
  	extend: 'checkbox',
    Config: [
      { name: 'checkall', type: 'Boolean', remark: '设为 true 时，点击它可以全选/全不选其他相同name的triplebox。一组同name的triplebox中只能有一个设置checkall参数。' },
      { name: 'partialsubmit', type: 'Boolean', remark: '设为 true 时，半选状态也会提交数据。' },
      { name: 'checkstate', type: 'Number', remark: '选中状态。可选值: <b>0</b><font color=green>(未选)</font>，<b>1</b><font color=green>(选中)</font>，<b>2</b><font color=green>(半选)</font>' }
    ],
    Methods: [
      { name: 'check([checked])', remark: '设置选中状态。', param: [
        { name: 'checked', type: 'Number', remark: '选中状态。可选值: <b>0</b><font color=green>(未选)</font>，<b>1</b><font color=green>(选中)</font>，<b>2</b><font color=green>(半选)</font>', optional: true }
      ] }
    ]
  },
  "radiogroup": {
  	title: 'radiogroup',
  	remark: '单选表单组。',
  	extend: 'checkboxgroup',
  	deprecate: 'placeholder,tip,transparent,value,focus,focusEnd,.z-trans,.w-text,.w-input,status',
    Config: [
      { name: 'options', type: 'Array', remark: 'radio 节点数组。' },
      { name: 'targets', type: 'Array', optional: true, remark: '和 radio 一一对应的节点数组。勾选单选框将改变 target 节点的 disabled 状态' },
      { name: 'pub', type: 'Object', optional: true, remark: 'radio 的默认参数。这里的参数不会应用于 targets 中的 widget。 ' }
    ],
    Classes: [
      { name: '.w-radiogroup', remark: '基础样式。' }
    ]
  },
  "radio": {
  	title: 'radio',
  	remark: '单选项。',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.w-input,.z-trans,.z-on',
  	extend: 'checkbox',
    Classes: [
      { name: '.w-radio', remark: '基础样式。' }
    ]
  },
  "grid/radio": {
  	title: 'grid/radio',
  	remark: 'grid 内部专用的 radio。选中状态与 tr 的 focus 效果同步。',
  	deprecate: 'focus,focusEnd,placeholder,transparent,.w-text,.w-input,.z-trans,.z-on',
  	extend: 'radio'
  },
  "select": {
  	title: 'select',
  	remark: '下拉选择表单。',
  	deprecate: 'focusEnd,placeholder,transparent,.w-text,.z-trans',
  	extend: 'text',
    Methods: [
      { name: 'getFocusOption()', remark: '获取当前的option选项对象。' },
      { name: 'getPrevOption()', remark: '获取上一个option选项对象。' },
      { name: 'getNextOption()', remark: '获取下一个option选项对象。' }
    ],
    Classes: [
      { name: '.w-select', remark: '基础样式。' }
    ]
  },
  "date": {
  	title: 'date',
  	remark: '日期选择表单。',
  	extend: 'text',
  	deprecate: '.w-text',
    Config: [
      { name: 'format', type: 'String', remark: '日期格式: yyyy-mm-dd hh:ii:ss' }
    ],
    Classes: [
      { name: '.w-date', remark: '基础样式。' }
    ]
  },
  "hidden": {
  	title: 'hidden',
  	remark: '隐藏表单。',
  	extend: 'text',
  	deprecate: 'focus,focusEnd,.w-text,.z-trans,.z-on,placeholder,tip,transparent'
  },
  "rate": {
  	title: 'rate',
  	remark: '评分表单。',
  	extend: 'text',
  	deprecate: 'focus,focusEnd,.w-text,.z-trans,placeholder,tip,transparent',
    Config: [
      { name: 'value', type: 'String', remark: '表单值。从0 - 10。' }
    ]
  },
  "range": {
  	title: 'range',
  	remark: '指定范围的表单组合。',
  	extend: 'widget',
    Config: [
      { name: 'title', type: 'String', optional: true, remark: '字段描述。' },
      { name: 'begin', type: 'Object', remark: '开始值的表单。' },
      { name: 'end', type: 'Object', remark: '结束值的表单。' }
    ]
  },
  "muldate": {
  	title: 'muldate',
  	remark: '可多选的日期选择表单。',
  	extend: 'date',
  	deprecate: '.w-text,.w-date',
    Classes: [
      { name: '.w-muldate', remark: '基础样式。' }
    ]
  },
  "slider": {
  	title: 'slider',
  	remark: '滑块。',
  	extend: 'text',
    Config: [
      { name: 'tip', type: 'Boolean | String', optional: true, remark: '拖动滑块时显示的tip。支持${0}参数表示当前值。' }
    ],
  	deprecate: '.w-text,.z-trans,placeholder,transparent,focus,focusEnd,warn',
    Classes: [
      { name: '.w-slider', remark: '基础样式。' }
    ]
  },
  "spinner": {
  	title: 'spinner',
  	remark: '数字输入框。',
  	extend: 'text',
    Config: [
      { name: 'step', type: 'Number', optional: true, remark: '递增/递减的数值。' }
    ],
  	deprecate: '.w-text',
    Classes: [
      { name: '.w-spinner', remark: '基础样式。' }
    ]
  },
  "xbox": {
  	title: 'xbox',
  	remark: '下拉选择表单。',
  	extend: 'text',
  	deprecate: 'focus,.w-text',
    Config: [
      { name: 'multiple', type: 'Boolean', remark: '是否多选模式。' },
      { name: 'options', type: 'Array', remark: '下拉选项数组。', param: [
      	{ name: 'text', type: 'String', remark: '文本。' },
      	{ name: 'value', type: 'String', remark: '值。' },
      	{ name: 'icon', type: 'String', optional: true, remark: '图标。' }
      ] }
    ],
    Classes: [
      { name: '.w-xbox', remark: '基础样式。' }
    ],
    Methods: [
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
        { name: 'index', type: 'Number', remark: '选项序号。可替换指定的选项。' }
      ] }
    ]
  },
  "imgbox": {
  	title: 'imgbox',
  	remark: '图片下拉选择表单。',
  	extend: 'xbox',
  	deprecate: 'focus,placeholder,tip,transparent,.w-text,.z-trans',
    Config: [
      { name: 'imgwidth', type: 'Number', remark: '图标宽度。' },
      { name: 'imgheight', type: 'Number', remark: '图标高度。' },
      { name: 'options', type: 'Array', remark: '下拉选项数组。', param: [
      	{ name: 'icon', type: 'String', remark: '图标。' },
      	{ name: 'text', type: 'String', remark: '文本。', optional: true },
      	{ name: 'value', type: 'String', remark: '值。' }
      ] }
    ],
    Classes: [
      { name: '.w-imgbox', remark: '基础样式。' }
    ],
    Methods: [
      { name: 'getFocusOption()', remark: '获取当前的选项对象。' },
      { name: 'getPrevOption()', remark: '获取上一个选项对象。' },
      { name: 'getNextOption()', remark: '获取下一个选项对象。' },
      { name: 'setOptions(opt)', remark: '设置下拉选项。', param: [
        { name: 'opt', type: 'Array', remark: '选项数组。' }
      ] }
    ]
  },
  "pickbox": {
  	title: 'pickbox',
  	remark: '选择框。',
  	extend: 'text',
  	deprecate: '.w-text',
    Config: [
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'picker', type: 'Object', remark: 'dialog 参数。其中 dialog 的 src 支持变量 <b>$value</b><font color=green>(值)</font> 和 <b>$text</b><font color=green>(文本)</font>。' }
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
    ]
  },
  "onlinebox": {
  	title: 'onlinebox',
  	remark: '在线匹配关键词，并显示推荐列表。',
  	extend: 'text',
  	deprecate: '.w-text',
    Config: [
      { name: 'delay', type: 'Number', remark: '输入字符时的延迟查询时间。单位:毫秒。' },
      { name: 'dropsrc', type: 'String', remark: '显示下拉列表的 view src。' },
      { name: 'multiple', type: 'Boolean', remark: '多选模式。' },
      { name: 'picker', type: 'Object', remark: 'dialog 参数。其中 dialog 的 src 支持变量 <b>$value</b><font color=green>(值)</font> 和 <b>$text</b><font color=green>(文本)</font>。' },
      { name: 'separator', type: 'String', remark: '文本选项分隔符。默认是逗号。' },
      { name: 'src', type: 'String', remark: '在线匹配关键词的 view src。支持变量 <b>$value</b><font color=green>(值)</font> 和 <b>$text</b><font color=green>(文本)</font>。' }
    ],
    Methods: [
      { name: 'search(text)', remark: '根据关键词弹出对话框选择器。' },
      { name: 'complete(obj)', remark: '返回完整的结果给 onlinebox，完成正在输入的文本。用于选择器中设置了 combofield 的 grid 或 tree。', param: [
        { name: 'obj', type: 'Object', remark: 'tr 或 leaf 类型的 widget。' }
      ], example: [
          function() {
            // 给 grid 绑一个点击事件，返回结果
            var grid = { type: 'grid', combofield: { value: 'C0', text: 'C1' }, pub: { on: { click: '$.dialog(this).commander.complete(this)' } } };
          }
      ] }
    ],
    Classes: [
      { name: '.w-onlinebox', remark: '基础样式。' }
    ]
  },
  "combobox": {
  	title: 'combobox',
  	remark: '可输入的有下拉选项的表单。',
  	extend: 'text',
  	deprecate: 'tip,.w-text',
    Config: [
      { name: 'delay', type: 'Number', remark: '输入字符时的延迟查询时间。单位:毫秒。' },
      { name: 'dropsrc', type: 'String', remark: '显示下拉列表的 view src。' },
      { name: 'face', type: 'String', remark: '设置已选项的外观效果。可选值: <b>default</b>, <b>tag</b>' },
      { name: 'node', type: 'Object', remark: '包含候选项数据的 view。' },
      { name: 'multiple', type: 'Boolean', remark: '是否多选模式。' },
      { name: 'loadingtext', type: 'String', remark: '加载数据时显示的文本。' },
      { name: 'src', type: 'String', remark: '通过这个 src 加载候选项数据到本地。支持变量 <b>$value</b><font color=green>(值)</font> 和 <b>$text</b><font color=green>(文本)</font>。' },
      { name: 'suggest', type: 'Boolean', remark: '设为 true，在线搜索关键词模式。设为 false，缓存搜索模式。默认值为 false。' },
      { name: 'nobr', type: 'Boolean', remark: '设为 true，已选项不换行。' },
      { name: 'picker', type: 'Object', remark: '选择器 dialog 参数。dialog 的 src 支持变量 <b>$value</b><font color=green>(值)</font> 和 <b>$text</b><font color=green>(文本)</font>。' },
      { name: 'pub', type: 'Object', remark: '用于 combobox/option 的默认参数。', example: [
          function() {
            // 设置每个已选项的宽度为 100，并绑定点击事件，显示选项的值
            var cmbx = { type: 'combobox', pub: { width: 100, on: { click: 'alert(this.x.value)' } } };
          }
      ] },
      { name: 'strict', type: 'Boolean', remark: '设为 true，如果存在没有匹配成功的选项，则不能通过表单验证。设为false，允许存在没有匹配成功的选项。默认值是true。' },
      { name: 'text', type: 'String', remark: '初始化时显示的文本。如果设置了此参数，就要和 value 值一一对应。一般只设置 value 就可以，仅当 src 是 tree 模式的数据岛，并且 value 在 tree 的初始数据中匹配不到时才需要定义 text。' },
      { name: 'value', type: 'String', remark: '表单值。多个用逗号隔开。' }
    ],
    Methods: [
      { name: 'suggest(text)', remark: '根据关键词弹出对话框选择器。' },
      { name: 'complete(obj)', remark: '返回完整的结果给 combobox，完成正在输入的文本。用于选择器中设置了 combofield 的 grid 或 tree。', param: [
        { name: 'obj', type: 'Object', remark: 'tr 或 leaf 类型的 widget。' }
      ], example: [
          function() {
            // 给 grid 绑一个点击事件，返回结果
            var grid = { type: 'grid', combofield: { value: 'C0', text: 'C1' }, pub: { on: { click: '$.dialog(this).commander.complete(this)' } } };
          }
      ] },
      { name: 'text()', remark: '获取文本。' },
      { name: 'queryText()', remark: '获取正在输入的文本。' }
    ],
    Classes: [
      { name: '.w-combobox', remark: '基础样式。' }
    ]
  },
  "combobox/option": {
  	title: 'combobox/option',
  	remark: 'combobox 的已选项。它由 combobox 自动生成，没有显式定义。可以通过 combobox 的 pub 属性来设置它的参数。',
  	extend: 'widget',
    Config: [
      { name: 'value', type: 'String', remark: '值。' },
      { name: 'text', type: 'String', remark: '文本。' },
      { name: 'data', type: 'JSON', remark: 'tr 或 leaf 的 data。' }
    ],
    Methods: [
      { name: 'close()', remark: '删除。' },
      { name: 'val()', remark: '获取值。' },
      { name: 'text()', remark: '获取文本。' }
    ]
  },
  "linkbox": {
  	title: 'linkbox',
  	remark: '可输入的有下拉选项的表单。如果设置了dblclick事件，已选项的样式为带下划线的链接。',
  	extend: 'combobox',
  	deprecate: 'pub,w-text,w-combobox',
    Config: [
      { name: 'strict', type: 'Boolean', remark: '设为 true，如果存在没有匹配成功的选项，则不能通过表单验证。设为false，允许存在没有匹配成功的选项。默认值是false。' },
      { name: 'separator', type: 'String', remark: '文本选项分隔符。默认是逗号。' }
    ],
    Classes: [
      { name: '.w-combobox', remark: '基础样式。' }
    ]
  },
  "upload/file": {
  	title: 'upload/file',
  	remark: '上传附件。',
  	extend: 'text',
  	deprecate: 'focus,focusEnd,placeholder,tip,transparent,.w-text,.w-input,.z-trans,.z-on',
    Config: [
      { name: 'dir', type: 'String', remark: '附件排列方向。可选值: <b>h</b><font color=green>(横向,默认)</font>, <b>v</b><font color=green>(纵向)</font>' },
      { name: 'file_upload_limit', type: 'Number', remark: '最多可上传数量。' },
      { name: 'file_size_limit', type: 'String', remark: '单个附件最大体积。如 "50M"。' },
      { name: 'file_types', type: 'String', remark: '允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"' },
      { name: 'upload_url', type: 'String', remark: '上传地址。' +
      	'<br>上传成功返回JSON格式: { "id": "ID", "name": "名称", "size": "字节数", "url": "地址", "thumbnail": "缩略图地址" } <font color=green>//id 和 name 必填</font>' +
      	'<br>上传失败返回JSON格式: { "error": true, "text": "失败原因" }' },
      { name: 'down_url', type: 'String', remark: '下载地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量(对应变量值取自 json 格式的 value)。' },
      { name: 'remove_url', type: 'String', remark: '删除附件的地址。支持 $xxx 变量(对应变量值取自 json 格式的 value)。' },
      { name: 'upload_button', type: 'Array', remark: '上传按钮的数组。' },
      { name: 'value_button', type: 'Array', remark: '附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。' },
      { name: 'value', type: 'String', remark: '值。' }
    ]
  },
  "upload/image": {
  	title: 'upload/image',
  	remark: '上传图片。',
  	extend: 'upload/file',
    Config: [
      { name: 'thumbnail_url', type: 'String', remark: '缩略图地址。支持 $xxx 变量(对应变量值取自 json 格式的 value)。' },
      { name: 'value', type: 'String', remark: '值。' }
    ]
  },
  "upload/button": {
  	title: 'upload/button',
  	remark: '上传按钮。upload/file 和 upload/image 的专用按钮。',
  	extend: 'button'
  },
  "cmd": {
  	title: 'cmd',
  	remark: '命令集合。',
    Config: [
      { name: 'delay', type: 'Number', remark: '延迟执行。单位:秒。' },
      { name: 'nodes', type: 'Array', remark: '命令集合的数组。' },
      { name: 'path', type: 'String', remark: '指定一个 view path，当前的命令集由这个 view 作为执行主体。' },
      { name: 'target', type: 'String', remark: '指定一个 widget id，当前的命令集由这个 widget 作为执行主体。path 和 target 同时指定时，相当于 VM( path ).find( target ).cmd( args ); ' }
    ]
  },
  "js": {
  	title: 'js',
  	remark: 'js命令。',
    Config: [
      { name: 'text', type: 'String', remark: 'js语句。' }
    ]
  },
  "ajax": {
  	title: 'ajax',
  	remark: '发送一个 http 请求到服务器。服务端应当返回一个命令格式JSON。',
    Config: [
      { name: 'beforesend', type: 'String | Function', remark: '在发送请求之前调用的函数。如果返回false可以取消本次ajax请求。支持一个变量，<b>$ajax</b>(Ajax实例)' },
      { name: 'complete', type: 'String | Function', remark: '在得到服务器的响应后调用的函数(不论成功失败都会执行)。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'data', type: 'String | Object', remark: 'post 数据。', example: [
          function() {
            // 对象类型的数据，不需要url编码
            var cmd = { type: 'ajax', src: 'abc.sp', data: { id: '001', name: 'a b' } };
          },
          function() {
            // 字符串类型的数据，需要url编码
            var cmd = { type: 'ajax', src: 'abc.sp', data: 'id=001&name=a%20b' };
          }
      ] },
      { name: 'dataType', type: 'String', remark: '指定后台返回的数据格式。可选值: <b>json</b>(默认), <b>xml</b>, <b>text</b>' },
      { name: 'download', type: 'Boolean', remark: '设置为true，转为下载模式。' },
      { name: 'error', type: 'String | Function', remark: '在获取服务器的响应数据失败后调用的函数。支持一个变量，<b>$ajax</b>(Ajax实例)' },
      { name: 'filter', type: 'String | Function', remark: '在获取服务器的响应数据后调用的函数。本语句应当 return 一个命令JSON。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'headers', type: 'Object', remark: '一个额外的"{键:值}"对映射到请求一起发送。' },
      { name: 'loading', type: 'Boolean | String | LoadingCmd', remark: '显示一个"正在加载"的提示框。' },
      { name: 'src', type: 'String', remark: '路径。' },
      { name: 'success', type: 'String | Function', remark: '在成功获取服务器的响应数据并执行返回的命令之后调用的函数。如果设置了本参数，引擎将不会执行后台返回的命令，由业务自行处理。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)' },
      { name: 'sync', type: 'Boolean', remark: '是否同步。' }
   ]
  },
  "submit": {
  	title: 'submit',
  	remark: '提交表单数据到服务器。服务端应当返回一个命令格式JSON。',
  	extend: 'ajax',
    Config: [
      { name: 'range', type: 'String', remark: '指定一个 widget id，只提交这个 widget 内的表单数据。多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示排除 widget 内的表单数据。' },
      { name: 'validate', type: 'String', remark: '验证组名。' },
      { name: 'validateeffect', type: 'String', remark: '验证效果。可选值: <b>alert</b>, <b>red</b>, <b>none</b>', example: [
          function() {
            //把验证效果设置为弹出对话框提示，以及表单边框变成红色。
            var cmd = { type: 'submit', src: 'abc.sp', validateeffect: 'red,alert' };
          }
      ] },
      { name: 'validaterange', type: 'String', remark: '指定一个 widget id，只校验这个 widget 内的表单。多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示不校验 widget 内的表单。' }
   ]
  },
  "prepend": {
  	title: 'prepend',
  	remark: '插入命令。在某个 widget 内部前置一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      { name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。node 和 nodes 不应同时使用。' }
    ]
  },
  "append": {
  	title: 'append',
  	remark: '插入命令。在某个 widget 内部后置一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'section', type: 'String', remark: '目标类型。可选值 <b>widget</b>, <b>cmd</b>, <b>template</b>。默认值为 widget。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      { name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。node 和 nodes 不应同时使用。' }
    ]
  },
  "before": {
  	title: 'before',
  	remark: '插入命令。在某个 widget 之前插入一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      { name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。node 和 nodes 不应同时使用。' }
    ]
  },
  "after": {
  	title: 'after',
  	remark: '插入命令。在某个 widget 之后插入一个或多个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'target', type: 'String', remark: 'widget ID。' },
      { name: 'node', type: 'Object', remark: '新增的 widget 配置项。' },
      { name: 'nodes', type: 'Array', remark: '新增多个 widget 的配置项数组。node 和 nodes 不应同时使用。' }
    ]
  },
  "replace": {
  	title: 'replace',
  	remark: '替换命令。替换某个 widget。',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'node', type: 'Object', remark: '新的 widget 配置项。' },
      { name: 'section', type: 'String', remark: '目标类型。可选值 <b>widget</b>, <b>cmd</b>, <b>template</b>。默认值为 widget。' },
      { name: 'target', type: 'String', remark: 'widget ID。' }
    ]
  },
  "remove": {
  	title: 'remove',
  	remark: '删除命令。删除某个 widget。',
  	extend: 'widget',
    Config: [
      { name: 'id', type: 'String', remark: '命令ID。' },
      { name: 'section', type: 'String', remark: '目标类型。可选值 <b>widget</b>, <b>cmd</b>, <b>template</b>。默认值为 widget。' },
      { name: 'target', type: 'String', remark: 'widget ID。' }
    ]
  },
  "dialog": {
  	title: 'dialog',
  	remark: '打开一个对话框。dialog 既是命令，也是 widget。',
  	extend: 'widget',
    Config: [
      { name: 'cache', type: 'Boolean', remark: '如果设为 true, 当前窗口调用 .close() 方法关闭后，窗口处于隐藏状态并不删除，再次打开时将恢复为上次打开时的状态。' },
      { name: 'cover', type: 'Boolean', remark: '如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。' },
      { name: 'id', type: 'String', remark: 'Dialog 的 id 参数有全局性。可以通过两种方式获取 dialog 的实例: <br> 1. 可通过 view.find( id ) 方法来获取 widget。<br> 2. 通过 $.dialog( id ) 获取。', example: [
          function() {
          	//  
            vm.cmd( { type: "dialog", id: 'mydialog', width: 500, height: 400, src: 'dialog.sp' } );
            var dg1 = vm.find( 'mydialog' ); // 获取方式1
            var dg2 = $.dialog( 'mydialog' ); // 获取方式2
          }
      ] },
      { name: 'src', type: 'String', remark: '加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。src 参数和 node 参数不应同时使用。src 是需要 ajax 加载的；node 则是直接展示。', example: [
          function() {
          	//
            VM().cmd( { type: "dialog", width: 500, height: 400, src: 'dialog.sp' } );
          }
      ] },
      { name: 'node', type: 'Object', remark: 'Dialog的唯一子节点。', example: [
          function() {
          	//
            var opt = {
              type: 'dialog', width: 500, height: 400,
              node: {
                type: 'view',
                node: { type: 'html', text: '内容..' }
              }
            };
          }
      ] },
      { name: 'indent', type: 'Number', remark: '当设置了 snap 时，再设置 indent 指定相对于初始位置缩进微调多少个像素。' },
      { name: 'maxwidth', type: 'Number', remark: '最大宽度。' },
      { name: 'maxheight', type: 'Number', remark: '最大高度。' },
      { name: 'minwidth', type: 'Number', remark: '最小宽度。' },
      { name: 'minheight', type: 'Number', remark: '最小高度。' },
      { name: 'moveable', type: 'Boolean', remark: '窗口是否可用鼠标移动位置。默认值为 true。' },
      { name: 'resizable', type: 'Boolean', remark: '窗口是否可用鼠标拖动调整大小。' },
      { name: 'fullscreen', type: 'Boolean', remark: '窗口在初始化时是否最大化。' },
      { name: 'snap', type: 'HtmlElement | Widget', remark: '吸附的对象。可以是 html 元素或 widget ID。' },
      { name: 'snaptype', type: 'String', remark: '指定 snap 的位置。 <a href=javascript:; onclick="var s=this.nextSibling.style;s.display=s.display==\'none\'?\'block\':\'none\'"><b>点击查看参数说明图>></b></a><span style="display:none"><img style="border:1px solid #ccc" src=src/img/snaptype.png></span><br>可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。', example: [
          function() {
          	// 对话框吸附到 mydiv 元素，吸附方式指定为 "41,32,14,23"。系统将先尝试 "41"，如果对话框没有超出浏览器可视范围就直接显示。如果超出了，则继续尝试 "32", 依此类推。
            var opt = { type: 'dialog', width: 500, height: 400, snap: $( 'mydiv' ), snaptype: '41,32,14,23' };
          }
      ] },
      { name: 'position', type: 'Number', remark: '对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8。其中 0 为页面中心点，1-8是页面八个角落方位。' },
      { name: 'pophide', type: 'Boolean', remark: '设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。' },
      { name: 'prong', type: 'Boolean', remark: '设为 true，显示一个箭头，指向 snap 参数对象。' },
      { name: 'template', type: 'String', remark: '模板ID。' },
      { name: 'timeout', type: 'Number', remark: '定时关闭，单位:秒。' },
      { name: 'title', type: 'String', remark: '标题。如果有设置 template, 标题将显示在 template/title 中。' }
    ],
    Event: [
      { name: 'load', remark: '对话框内的 view 加载完毕后触发。', example: [
          function() {
          	// view加载完毕后显示path
            var opt = { type: 'dialog', width: 500, height: 400, src: 'abc.sp', on: { load: "alert(this.contentView.path)" } };
          }
      ] },
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
      ], example: [
          function() {
          	//
            $.dialog(this).draggable();
          }
      ] },
      { name: 'hide()', remark: '隐藏。和 show() 方法对应。' },
      { name: 'isShow()', remark: '是否可见状态。' },
      { name: 'moveTo(iLeft, iTop)', remark: '移动到指定位置。', param: [
        { name: 'iLeft', type: 'Number', remark: '左边位置。' },
        { name: 'iTop', type: 'Number', remark: '顶部位置。' }
      ] },
      { name: 'max()', remark: '窗口最大化。如果窗口已经是最大化的状态，那么将恢复到初始大小。' },
      { name: 'remove()', remark: '完全删除。调用本方法不会触发 close 事件。' },
      { name: 'show()', remark: '显示。和 hide() 方法对应。' }
    ],
    Classes: [
      { name: '.w-dialog', remark: '基础样式。' },
      { name: '.z-snap-{**}', remark: ' dialog 设置了 snap 参数时的样式。{**} 值根据 snap 结果类型而定。例如 snaptype 为 "41"，那么该样式名称则为: z-snap-41' },
      { name: '.z-mag-{*}', remark: ' dialog 设置了 snap 参数时的样式。{*} 值根据 snap 位置类型而定。x 的可能值有: t(top), r(right), b(bottom), l(left)' }
    ]
  },
  "menu": {
  	title: 'menu',
  	remark: '显示一个菜单。menu 既是命令，也是 widget。',
  	extend: 'widget',
    Config: [
      { name: 'nodes', type: 'Array', remark: '子节点集合。子节点类型是 button 或 split。' },
   ],
    Properties: [
      { name: 'commander', type: 'Widget', remark: '执行 menu 命令的 widget。即以 xxx.cmd() 方式打开的 menu, 它的 commander 就是 xxx。' },
      { name: 'ownerView', type: 'Widget', remark: 'menu 所属的 view 对象。即 menu 从哪个 view 打开，它的 ownerView 就是那个 view。' }
    ],
    Classes: [
      { name: '.w-menu', remark: '基础样式。' },
      { name: '.z-snap-{**}', remark: ' dialog 设置了 snap 参数时的样式。{**} 值根据 snap 结果类型而定。例如 snaptype 为 "41"，那么该样式名称则为: z-snap-41' },
      { name: '.z-mag-{*}', remark: ' dialog 设置了 snap 参数时的样式。{*} 值根据 snap 位置类型而定。x 的可能值有: t(top), r(right), b(bottom), l(left)' }
    ]
  },
  "alert": {
  	title: 'alert',
  	remark: '警告提示框。alert 是特殊的 dialog，既是命令，也是 widget。',
  	extend: 'widget',
    Config: [
      { name: 'btncls', type: 'String', remark: '按钮样式名。' },
      { name: 'buttons', type: 'Array', remark: '自定义的一组按钮。' },
      { name: 'cover', type: 'Boolean', remark: '如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。' },
      { name: 'id', type: 'String', remark: 'Dialog 的 id 参数有全局性。可以通过两种方式获取 dialog 的实例: <br> 1. 可通过 view.find( id ) 方法来获取 widget。<br> 2. 通过 $.dialog( id ) 获取。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'icon', type: 'String', remark: '图标。' },
      { name: 'position', type: 'Number', remark: '对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8。其中 0 为页面中心点，1-8是页面八个角落方位。' },
      { name: 'timeout', type: 'Number', remark: '定时关闭，单位:秒。' },
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
      { name: '.w-alert', remark: '基础样式。' },
      { name: '.z-snap-{**}', remark: ' dialog 设置了 snap 参数时的样式。{**} 值根据 snap 结果类型而定。例如 snaptype 为 "41"，那么该样式名称则为: z-snap-41' },
      { name: '.z-mag-{*}', remark: ' dialog 设置了 snap 参数时的样式。{*} 值根据 snap 位置类型而定。x 的可能值有: t(top), r(right), b(bottom), l(left)' }
    ]
  },
  "confirm": {
  	title: 'confirm',
  	remark: '确认对话框。confirm 是特殊的 dialog，既是命令，也是 widget。',
  	extend: 'alert',
  	deprecate: 'position,timeout,.w-alert',
    Config: [
      { name: 'yes', type: 'CommandJSON | Function', remark: '点击"确定"执行的命令或函数。' },
      { name: 'no', type: 'CommandJSON | Function', remark: '点击"取消"执行的命令或函数。' }
    ],
    Classes: [
      { name: '.w-confirm', remark: '基础样式。' }
    ]
  },
  "loading": {
  	title: 'loading',
  	remark: '显示一个"请稍候"的信息窗。',
  	extend: 'widget',
    Config: [
      { name: 'cover', type: 'Boolean', remark: '如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'node', type: 'Widget', remark: 'widget节点。' },
      { name: 'hide', type: 'Boolean', remark: '设为true，关闭 loading 窗口。' }
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
    ]
  },
  "tip": {
  	title: 'tip',
  	remark: '提示信息。tip 是特殊的 dialog，既是命令，也是 widget。',
  	extend: 'widget',
    Config: [
      { name: 'hide', type: 'Boolean', remark: '设置为true，关闭tip。' },
      { name: 'hoverdrop', type: 'Boolean', remark: '设置为true，当鼠标移开时tip自动关闭。' },
      { name: 'text', type: 'String', remark: '显示文本。' },
      { name: 'snap', type: 'String | Element | Widget', remark: 'tip 的吸附对象。可以是 widget ID, widget 对象或 HTML 元素。' },
      { name: 'snaptype', type: 'String', remark: '指定 snap 的位置。 <a href=javascript:; onclick="var s=this.nextSibling.style;s.display=s.display==\'none\'?\'block\':\'none\'"><b>点击查看参数说明图>></b></a><span style="display:none"><img style="border:1px solid #ccc" src=src/img/snaptype.png></span><br>可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。', example: [
          function() {
          	// 对话框吸附到 mydiv 元素，吸附方式指定为 "41,32,14,23"。系统将先尝试 "41"，如果对话框没有超出浏览器可视范围就直接显示。如果超出了，则继续尝试 "32", 依此类推。
            var opt = { type: 'dialog', width: 500, height: 400, snap: $( 'mydiv' ), snaptype: '41,32,14,23' };
          }
      ] },
      { name: 'timeout', type: 'Number', remark: '定时关闭，单位:秒。' },
      { name: 'closable', type: 'Boolean', remark: '是否显示关闭图标。' },
      { name: 'multiple', type: 'Boolean', remark: '是否允许多个实例存在。' },
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
      { name: '.z-snap-{**}', remark: '设置了 snap 参数时的样式。{**} 值根据 snap 结果类型而定。例如 snaptype 为 "41"，那么该样式名称则为: z-snap-41' },
      { name: '.z-mag-{*}', remark: '设置了 snap 参数时的样式。{*} 值根据 snap 位置类型而定。x 的可能值有: t(top), r(right), b(bottom), l(left)' },
      { name: '.z-x', remark: '设置了 closable:true 时的样式。' },
      { name: '.z-noprong', remark: '设置了 prong:false 时的样式。' }
    ]
  }
} );

define( './c', { name: 'cc' } );