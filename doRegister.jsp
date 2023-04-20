<%@ page import="java.io.*, java.nio.file.*, java.util.Scanner" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册结果</title>
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String gender = request.getParameter("gender");

    File file = new File(getServletContext().getRealPath("/") + "user.txt");
    boolean userExists = false;

    if (file.exists()) {
        Scanner scanner = new Scanner(file, "UTF-8");
        while (scanner.hasNextLine()) {
            String[] userInfo = scanner.nextLine().split(",");
            if (userInfo[0].equals(username)) {
                userExists = true;
                break;
            }
        }
        scanner.close();
    }

    if (userExists) {
        out.println("<p>用户已存在，请尝试其他用户名。</p>");
    } else {
        // 使用UTF-8编码保存数据到文件
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"))) {
            writer.println(username + "," + password + "," + gender);
        } catch (IOException e) {
            out.println("<p>注册失败，请稍后再试。</p>");
        }
        out.println("<p>注册成功！</p>");
    }
%>
</body>
</html>
