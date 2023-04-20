<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户注册</title>
</head>
<body>
<h1>用户注册</h1>
<form action="doRegister.jsp" method="post">
    用户名：<input type="text" name="username" required><br>
    密码：<input type="password" name="password" required><br>
    性别：
    <select name="gender">
        <option value="男">男</option>
        <option value="女">女</option>
    </select>
    <br>
    <input type="submit" value="注册">
</form>
</body>
</html>
