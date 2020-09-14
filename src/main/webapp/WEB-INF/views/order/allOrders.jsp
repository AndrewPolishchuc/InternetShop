<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All orders</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Products</th>
        <th>User</th>
        <th></th>
    </tr>
    <c:forEach var="order" items="${allOrders}">
        <tr>
            <td>
                <c:out value="${order.id}"/>
            </td>
            <td>
                <c:out value="${order.products}"/>
            </td>
            <td>
                <c:out value="${order.userId}"/>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/user/order/details" method="get">
                    <input type="hidden" name="orderId" value="${order.id}">
                    <button type="submit">Details</button>
                </form>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/order/delete" method="get">
                    <input type="hidden" name="orderId" value="${order.id}">
                    <button type="submit">Delete order</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/">Home page</a><br>
<a href="${pageContext.request.contextPath}/user/all">All users</a><br>
<a href="${pageContext.request.contextPath}/product/all">All products</a><br>
<a href="${pageContext.request.contextPath}/cart">Shopping cart</a><br>
</body>
</html>
