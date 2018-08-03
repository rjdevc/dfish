<!doctype html>
<html xmlns:d>
<head>
<meta charset=utf-8>
<title>DFish3.0</title>
<jsp:include page="config.jsp"></jsp:include>
<script>
var anchor= window.location.hash;
if(!anchor){anchor='#';}
dfish.init( {
	view: { // 如果配置了此参数，将生成全屏的view
		src: 'demo/index?anchor='+anchor.substring(1)
	}
	
} );
// 加载业务模块
dfish.use( './m/app.js' );
</script>
</head>
<body style="margin:0;overflow:hidden;" scroll="no" class="f-body"></body>
</html>