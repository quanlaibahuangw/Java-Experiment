<%@ page import="java.io.*, java.nio.file.*, java.util.Scanner" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录结果</title>
</head>
<body>
<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    Path path = Paths.get(getServletContext().getRealPath("/") + "/user.txt");
    boolean userExists = false;
    boolean passwordCorrect = false;

    try {
       
        if (Files.exists(path)) {
            Scanner scanner = new Scanner(path);
            while (scanner.hasNextLine()) {
                String[] userInfo = scanner.nextLine().split(",");
                if (userInfo[0].equals(username)) {
                    userExists = true;
                    passwordCorrect = userInfo[1].equals(password);
                    break;
                }
            }
            scanner.close();
        }

        if (userExists) {
            if (passwordCorrect) {
                out.println("登录成功！");
            } else {
                out.println("密码错误，请重新输入。");
            }
        } else {
            out.println("用户名不存在，请先注册。");
        }
    } catch (IOException e) {
        out.println("登录失败，请重试。");
    }
%>
</body>
</html>
