<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Login:</h1>
<h4 style="color:red">${message}</h4>
<form method="post" action="${pageContext.request.contextPath}/login">
    <p>Please provide your login : <input type="text" name="login"></p>
    <p>Please provide your password : <input type="password" name="password"></p>
    <button type="submit">Confirm</button>
</form>
<form action="${pageContext.request.contextPath}/registration" method="get">
    <button type="submit">Registration</button>
</form>
</body>
</html>
