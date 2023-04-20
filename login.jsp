<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户登录</title>
</head>
<body>
<h1>用户登录</h1>
<form action="doLogin.jsp" method="post">
    用户名：<input type="text" name="username" required><br>
    密码：<input type="password" name="password" required><br>
    <input type="submit" value="登录">
</form>
</body>
</html>
