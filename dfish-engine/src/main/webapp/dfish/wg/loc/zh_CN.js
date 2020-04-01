/*
 *	loc.js
 *	---------------------------------------------------------
 *
 *	---------------------------------------------------------
 *
 *	author: Chen MingYuan
 *	for iTASK 5.0
 *	Copyright (c) 2005 - 2006 iTASK Team
 *
 *	created : 2005-10-16
 * modified : 2006-9-21
 *
 */

define( {
yes		: '是',
no		: '否',
none	: '无',
to		: '至',
jump    : '转到',
confirm	: '确定',
search	: '搜索',
query	: '查询',
choose	: '选择',
save	: '保存',
remove	: '删除',
cancel	: '取消',
create	: '新建',
add		: '添加',
edit	: '编辑',
finish	: '完成',
refresh : '刷新',
operfail : '操作失败',
opertip	 : '操作提示',
nodata	: '暂无数据',
more 	: '更多',
field	: '字段',

browser_upgrade : '我们发现您的IE浏览器版本过低，您的浏览体验受到影响。<br>我们强烈建议您安装新版浏览器，点击图标即可下载。',
loading			: '正在加载..',
wait			: '请稍候..',
submiting		: '数据提交中，请稍候..',
uploading		: '正在上传，请稍侯..',
click_preview	: '点击预览',
toomuch_wait	: '次数过多，请稍候再试',
toomuch_wait_countdown : '次数过多，请稍候再试(<em>{0}</em>)',
preview_orginal_image: '查看原图',
auth_success: '验证成功',
auth_fail: '验证失败',

tree_movefail1	: '无法移动：目标文件夹和源文件夹相同',
tree_movefail2	: '无法移动：目标文件夹是源文件夹的子文件夹',

internet_error	: '<span{1}>[{0}] 网络连接失败，请稍候再试</span>',
server_error	: '<span{1}>[{0}] 系统异常，请联系管理员</span>',
preload_error   :  '获取预装载模板 "{0}" 失败',

page: '页',
page_text: {
	first: '首页',
	last : '尾页',
	next : '下页',
	prev : '上页'
},

print_preview : '打印预览',

placeholder_input: '请输入{0}',
placeholder_select: '请选择{0}',

debug: {
	sorry : '很抱歉，系统发生了错误',
	view_more : '点击“确定”，可在新窗口查看详细错误信息',
	no_command : '视图中没有定义此命令: {0}\nview path: {1}',
	no_combo: '选项列表(或树)没有设置combo参数\nname: {0}',
	error_template_type: '{0}\n请把此模板类型改为 "{1}"',
	error_type: 'type错误或丢失\n{0}'
},

form : {
	required:'{0}不能为空',
	onlyone: '{0}只能选择一条记录',
	maxLength: '{0}的字符长度超出了{1}个字节',
	reach_maxLength: '已到达可输入的最大字符长度',
	over_maxLength: '字符长度超出了{0}个字节',
	minLength: '{0}不能少于{1}个字节',
	maxValue: '{0}最大值为{1}',
	minValue: '{0}最小值为{1}',
	maxSize: '{0}最多选择{1}项',
	minSize: '{0}最少选择{1}项',
	pattern: '{0}格式不符合规范，或包含非法字符',
	password_weak: '弱',
	password_middle: '中',
	password_strong: '强',
	number_invalid: '{0}不是一个有效数字',
	number_integer: '{0}请填入一个整数',
	number_decimal_digit: '{0}的小数位不能超过{1}位',
	time_exceed: '{0}的年份超出有效范围。有效年份：{1}年 至 {2}年',
	time_format: '{0}的时间格式错误。正确格式范例：{1}',
	period_invalid: '{0}的起始时间应早于结束时间',
	beforeNow: '{0}不能大于当前时间',
	afterNow: '{0}不能小于当前时间',
	compare: '{3}需要满足条件：{0} {1} {2}',
	invalid_option: '存在无效选项',
	need_valid_option: '请输入一个有效选项',
	choose_atleastone: '请至少选择一项',
	complete_required: '请完成必填项',
	jigsaw_drag_right: '向右拖动滑块填充拼图',
	jigsaw_required: '请完成滑块验证',
	jigsaw_errorimg: '错误的图片格式',
	upload_loading: '正在上传，请稍候..',
	upload_invalid_file: '存在无效文件',
	no_files: '<没有文件>'
},

year	: '年',
month	: '月',
day		: '日',
sky		: '天',
hour	: '时',
minute	: '分',
second	: '秒',

calendar : {
	now: '现在',
	today	: '今天',
	weeknow	: '本周',
	monthnow: '本月',
	yearnow:  '本年',
	day_title : ['日','一','二','三','四','五','六'],
	ym : '<span class="_year">{0} 年</span> &nbsp; <span class="_month">{1} 月</span>',
	y  : '<span class="_year">{0} 年</span>',
	monthname : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
	backdate: '返回日期',
	picktime: '选择时间',
	h: '时',
	i: '分',
	s: '秒'
},

error : '错误',

ajax : {
	'xml'	: 'XML语法错误',
	'json'	: 'JSON语法错误',
	'0'		: '网络连接失败',
	'400'   : 'Bad Request\nThe request could not be understood by the server due to malformed syntax. The client SHOULD NOT repeat the request without modifications.',
	'401'   : 'Unauthorized\nThe request requires user authentication. The response MUST include a WWW-Authenticate header field containing a challenge applicable to the requested resource.',
	'403'   : 'Forbidden\nThe server understood the request, but is refusing to fulfill it. Authorization will not help and the request SHOULD NOT be repeated.',
	'404'   : 'Not Found\nThe server has not found anything matching the Request-URI. No indication is given of whether the condition is temporary or permanent.',
	'410'   : 'Not Found\nThe requested resource is no longer available at the server and no forwarding address is known. This condition is expected to be considered permanent. Clients with link editing capabilities SHOULD delete references to the Request-URI after user approval.\nIf the server does not know, or has no facility to determine, whether or not the condition is permanent, the status code 404 Not Found SHOULD be used instead. This response is cacheable unless indicated otherwise.',
	'500'	: 'Internal Server Error\nThe server encountered an unexpected condition which prevented it from fulfilling the request.',
	'501'	: 'Not Implemented\nThe server does not support the functionality required to fulfill the request. This is the appropriate response when the server does not recognize the request method and is not capable of supporting it for any resource.',
	'503'	: 'Service Unavailable\nYour web server is unable to handle your HTTP request at the time.Your web server is unable to handle your HTTP request at the time. There are a myriad of reasons why this can occur but the most common are:\n1. server crash\n2. server maintenance\n3. server overload\n4. server maliciously being attacked\n5. a website has used up its allotted bandwidth\n6. server may be forbidden to return the requested document',
	'550'	: 'Permission Denied\nThe server is stating the account you have currently logged in as does not have permission to perform the action you are attempting. You may be trying to upload to the wrong directory or trying to delete a file.',
	'12001' : 'ERROR_INTERNET_OUT_OF_HANDLES\nNo more handles could be generated at this time.',
	'12002' : 'ERROR_INTERNET_TIMEOUT\nThe request has timed out.',
	'12003' : 'ERROR_INTERNET_EXTENDED_ERROR\nAn extended error was returned from the server. This is typically a string or buffer containing a verbose error message. Call InternetGetLastResponseInfo to retrieve the error text.',
	'12004' : 'ERROR_INTERNET_INTERNAL_ERROR\nAn internal error has occurred.',
	'12005' : 'ERROR_INTERNET_INVALID_URL\nThe URL is invalid.',
	'12006' : 'ERROR_INTERNET_UNRECOGNIZED_SCHEME\nThe URL scheme could not be recognized or is not supported.',
	'12007' : 'ERROR_INTERNET_NAME_NOT_RESOLVED\nThe server name could not be resolved.',
	'12008' : 'ERROR_INTERNET_PROTOCOL_NOT_FOUND\nThe requested protocol could not be located.',
	'12009' : 'ERROR_INTERNET_INVALID_OPTION\nA request to InternetQueryOption or InternetSetOption specified an invalid option value.',
	'12010' : 'ERROR_INTERNET_BAD_OPTION_LENGTH\nThe length of an option supplied to InternetQueryOption or InternetSetOption is incorrect for the type of option specified.',
	'12011' : 'ERROR_INTERNET_OPTION_NOT_SETTABLE\n The request option cannot be set, only queried.',
	'12012' : 'ERROR_INTERNET_SHUTDOWN\nThe Win32 Internet function support is being shut down or unloaded.',
	'12013' : 'ERROR_INTERNET_INCORRECT_USER_NAME\nThe request to connect and log on to an FTP server could not be completed because the supplied user name is incorrect.',
	'12014' : 'ERROR_INTERNET_INCORRECT_PASSWORD\nThe request to connect and log on to an FTP server could not be completed because the supplied password is incorrect.',
	'12015' : 'ERROR_INTERNET_LOGIN_FAILURE\nThe request to connect to and log on to an FTP server failed.',
	'12016' : 'ERROR_INTERNET_INVALID_OPERATION\nThe requested operation is invalid.',
	'12017' : 'ERROR_INTERNET_OPERATION_CANCELLED\nThe operation was canceled, usually because the handle on which the request was operating was closed before the operation completed.',
	'12018' : 'ERROR_INTERNET_INCORRECT_HANDLE_TYPE\nThe type of handle supplied is incorrect for this operation.',
	'12019' : 'ERROR_INTERNET_INCORRECT_HANDLE_STATE\nThe requested operation cannot be carried out because the handle supplied is not in the correct state.',
	'12020' : 'ERROR_INTERNET_NOT_PROXY_REQUEST\nThe request cannot be made via a proxy.',
	'12021' : 'ERROR_INTERNET_REGISTRY_VALUE_NOT_FOUND\nA required registry value could not be located.',
	'12022' : 'ERROR_INTERNET_BAD_REGISTRY_PARAMETER\nA required registry value was located but is an incorrect type or has an invalid value.',
	'12023' : 'ERROR_INTERNET_NO_DIRECT_ACCESS\nDirect network access cannot be made at this time.',
	'12024' : 'ERROR_INTERNET_NO_CONTEXT\nAn asynchronous request could not be made because a zero context value was supplied.',
	'12025' : 'ERROR_INTERNET_NO_CALLBACK\nAn asynchronous request could not be made because a callback function has not been set.',
	'12026' : 'ERROR_INTERNET_REQUEST_PENDING\nThe required operation could not be completed because one or more requests are pending.',
	'12027' : 'ERROR_INTERNET_INCORRECT_FORMAT\nThe format of the request is invalid.',
	'12028' : 'ERROR_INTERNET_ITEM_NOT_FOUND\nThe requested item could not be located.',
	'12029' : 'ERROR_INTERNET_CANNOT_CONNECT\nThe attempt to connect to the server failed.',
	'12030' : 'ERROR_INTERNET_CONNECTION_ABORTED\nThe connection with the server has been terminated.',
	'12031' : 'ERROR_INTERNET_CONNECTION_RESET\nThe connection with the server has been reset.',
	'12032' : 'ERROR_INTERNET_FORCE_RETRY\nCalls for the Win32 Internet function to redo the request.',
	'12033' : 'ERROR_INTERNET_INVALID_PROXY_REQUEST\nThe request to the proxy was invalid.',
	'12036' : 'ERROR_INTERNET_HANDLE_EXISTS\nThe request failed because the handle already exists.',
	'12037' : 'ERROR_INTERNET_SEC_CERT_DATE_INVALID\nSSL certificate date that was received from the server is bad. The certificate is expired.',
	'12038' : 'ERROR_INTERNET_SEC_CERT_CN_INVALID\nSSL certificate common name (host name field) is incorrect. For example, if you entered www.server.com and the common name on the certificate says www.different.com.',
	'12039' : 'ERROR_INTERNET_HTTP_TO_HTTPS_ON_REDIR\nThe application is moving from a non-SSL to an SSL connection because of a redirect.',
	'12040' : 'ERROR_INTERNET_HTTPS_TO_HTTP_ON_REDIR\nThe application is moving from an SSL to a non-SSL connection because of a redirect.',
	'12041' : 'ERROR_INTERNET_MIXED_SECURITY\nIndicates that the content is not entirely secure. Some of the content being viewed may have come from unsecured servers.',
	'12042' : 'ERROR_INTERNET_CHG_POST_IS_NON_SECURE\nThe application is posting and attempting to change multiple lines of text on a server that is not secure.',
	'12043' : 'ERROR_INTERNET_POST_IS_NON_SECURE\nThe application is posting data to a server that is not secure.',
	'12110' : 'ERROR_FTP_TRANSFER_IN_PROGRESS\nThe requested operation cannot be made on the FTP session handle because an operation is already in progress.',
	'12111' : 'ERROR_FTP_DROPPED\nThe FTP operation was not completed because the session was aborted.',
	'12130' : 'ERROR_GOPHER_PROTOCOL_ERROR\nAn error was detected while parsing data returned from the gopher server.',
	'12131' : 'ERROR_GOPHER_NOT_FILE\nThe request must be made for a file locator.',
	'12132' : 'ERROR_GOPHER_DATA_ERROR\nAn error was detected while receiving data from the gopher server.',
	'12133' : 'ERROR_GOPHER_END_OF_DATA\nThe end of the data has been reached.',
	'12134' : 'ERROR_GOPHER_INVALID_LOCATOR\nThe supplied locator is not valid.',
	'12135' : 'ERROR_GOPHER_INCORRECT_LOCATOR_TYPE\nThe type of the locator is not correct for this operation.',
	'12136' : 'ERROR_GOPHER_NOT_GOPHER_PLUS\nThe requested operation can only be made against a Gopher+ server or with a locator that specifies a Gopher+ operation.',
	'12137' : 'ERROR_GOPHER_ATTRIBUTE_NOT_FOUND\nThe requested attribute could not be located.',
	'12138' : 'ERROR_GOPHER_UNKNOWN_LOCATOR\nThe locator type is unknown.',
	'12150' : 'ERROR_HTTP_HEADER_NOT_FOUND\nThe requested header could not be located.',
	'12151' : 'ERROR_HTTP_DOWNLEVEL_SERVER\nThe server did not return any headers.',
	'12152' : 'ERROR_HTTP_INVALID_SERVER_RESPONSE\nThe server response could not be parsed.',
	'12153' : 'ERROR_HTTP_INVALID_HEADER\nThe supplied header is invalid.',
	'12154' : 'ERROR_HTTP_INVALID_QUERY_REQUEST\nThe request made to HttpQueryInfo is invalid.',
	'12155' : 'ERROR_HTTP_HEADER_ALREADY_EXISTS\nThe header could not be added because it already exists.',
	'12156' : 'ERROR_HTTP_REDIRECT_FAILED\nThe redirection failed because either the scheme changed (for example, HTTP to FTP) or all attempts made to redirect failed (default is five attempts).'
},

ps : function ( a ) {
	if ( a ) {
		/*for (var i = 1, b; i < arguments.length; i ++ ) {
			if ( b = arguments[ i ] )
				a = a.replace( '{' + ( i - 1 ) + '}', String( b ).replace( /&nbsp;/gi, '' ).replace( /\s/g, '' ) );
		}*/
		var b = arguments;
		a = a.replace( /\{(\d+)\}/g, function( $0, $1 ) { var d = b[ 1 + parseFloat($1) ]; return d == null ? '' : d } );
	}
	return a;
},
end  : '结束'

} );
