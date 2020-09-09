<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Internet shop</title>
</head>
<body>
<h1>Home page</h1>
<a href="${pageContext.request.contextPath}/inject/data">Inject test data into the DB</a><br>
<a href="${pageContext.request.contextPath}/registration">Sign up</a><br>
<a href="${pageContext.request.contextPath}/user/all">All user</a><br>
<a href="${pageContext.request.contextPath}/product/all">All products</a><br>
<a href="${pageContext.request.contextPath}/product/add">Add product</a><br>
<a href="${pageContext.request.contextPath}/cart">Shopping cart</a><br>
</body>
</html>
