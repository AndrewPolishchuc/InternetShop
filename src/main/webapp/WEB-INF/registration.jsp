<%@ page contentType="text/html;charset=UTF-8"  %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
    <h1>New user registration:</h1>
    <h4 style="color:red">${message}</h4>
    <form method="post" action="${pageContext.request.contextPath}/registration">
        Please provide your name : <input type="text" name="name">
        <p>Please provide your login : <input type="text" name="login"></p>
        <p>Please provide your password : <input type="password" name="psw"></p>
        <p>Please repeat your password : <input type="password" name="psw-repeat"></p>
        <button type="submit">Register</button>
    </form>
</body>
</html>
