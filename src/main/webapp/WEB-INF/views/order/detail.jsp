<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order</title>
</head>
<body>
<table border="1">
    <tr>
        <th>Product ID</th>
        <th>Name</th>
        <th>Price</th>
    </tr>
    <c:forEach var="product" items="${order.products}">
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
        </tr>
    </c:forEach>
    <tr>
        <td>
            <label>Current order sum</label>
        </td>
        <td>
            <p><b>${currentSum}</b></p>
        </td>
    </tr>
</table>
<a href="${pageContext.request.contextPath}/">Home page</a><br>
<a href="${pageContext.request.contextPath}/product/all">All products</a><br>
<a href="${pageContext.request.contextPath}/cart">Shopping cart</a><br>
</body>
</html>
