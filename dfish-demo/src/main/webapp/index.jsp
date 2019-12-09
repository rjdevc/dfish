<%@ page language="java" contentType="text/html; charset=utf-8" %>
<!doctype html>
<html xmlns:d>
<head>
    <meta charset="text/html;utf-8">
    <link rel="shortcut icon" href="./skin/g/favicon.ico">
    <title>DFish</title>

    <script src="./dfish/dfish.js?ver=20191014"></script>
    <script>
        // 必须放在dfish.config前面调用,app中有通用配置先引入
        dfish.use( 'm/app.js' );
        dfish.config({
            skin: {
                theme: 'portal'
            },

            // 视图路径: js和css
            view_resources: {
                '/index': [ 'm/index/index.js', 'm/index/index.css' ]
            }
        });
        dfish.init({
            view : {
                id: 'index',
                template: 'index/index'
            }
        });
    </script>
</head>
<body class="f-body" scroll="no"></body>
</html>