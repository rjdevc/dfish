<%@ page language="java" contentType="text/html; charset=utf-8" %>
<!doctype html>
<html xmlns:d>
    <head>
        <meta charset="text/html;utf-8">
        <link rel="shortcut icon" href="./skin/g/favicon.ico">
        <title>DFish</title>

        <script src="./dfish/dfish.js?ver=20191014"></script>
        <script src="./skin/dfish.config.js?ver=20191014"></script>
        <script>
            dfish.use( 'm/app' );
            dfish.init({
                view : {
                    id: 'index',
                    node: 'index/index'
                }
            });
        </script>
    </head>
    <body class="f-body" scroll="no"></body>
</html>