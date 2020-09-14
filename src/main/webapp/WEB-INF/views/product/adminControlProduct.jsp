<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Page for product control by administrator</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
    </tr>
    <p></p>
    <c:forEach var="product" items="${allProducts}">
        <tr>
            <td>
                <c:out value="${product.id}"/>
            </td>
            <td>
                <c:out value="${product.name}"/>
            </td>
            <td>
                <c:out value="${product.price}"/>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/product/delete" method="post">
                    <input type="hidden" name="productId" value="${product.id}">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <form action="${pageContext.request.contextPath}/product/add" method="get">
        <button type="submit">Add product</button>
    </form>
</table>
<a href="${pageContext.request.contextPath}/">Home page</a><br>
<a href="${pageContext.request.contextPath}/user/all">All users</a><br>
<a href="${pageContext.request.contextPath}/product/all">All products</a><br>
<a href="${pageContext.request.contextPath}/cart">Shopping cart</a><br>
</body>
</html>
