<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>All users</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Login</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>
                <c:out value="${user.id}"/>
            </td>
            <td>
                <c:out value="${user.name}"/>
            </td>
            <td>
                <c:out value="${user.login}"/>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/user/delete" method="get">
                    <input type="hidden" name="userId" value="${user.id}">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/admin/product/all">Edit products</a><br>
<a href="${pageContext.request.contextPath}/cart">Shopping cart</a><br>
<a href="${pageContext.request.contextPath}/">Home page</a><br>
<a href="${pageContext.request.contextPath}/registration">Sign up</a><br>
</body>
</html>
