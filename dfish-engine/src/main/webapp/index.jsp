<%@ page language="java" contentType="text/html; charset=utf-8" %>
<!doctype html>
<html xmlns:d>
    <head>
        <meta charset="text/html;utf-8">
        <link rel="shortcut icon" href="./css/g/favicon.ico">
        <title>DFish开发框架</title>

        <script src="./dfish/dfish.js?ver=20170315"></script>
        <script src="./config.js?ver=20170315"></script>
        <script>
            dfish.use( 'm/app' );
            dfish.init({
                view : {
                    // 首页视图路径
                    id: 'index',
                    // 首页模板
                    template: 'index/index'
                }
            });
        </script>
    </head>
    <body class="f-body" scroll="no"></body>
</html>