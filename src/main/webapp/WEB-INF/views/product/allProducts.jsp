<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>All products:</title>
</head>
<body>
<table border="1">
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Price</th>
</tr>
<p></p>
<c:forEach var="product" items="${products}">
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
            <form action="${pageContext.request.contextPath}/product/buy" method="get">
                <input type="hidden" name="productId" value="${product.id}">
                <button type="submit">Buy</button>
            </form>
        </td>
    </tr>
</c:forEach>
</table>
<a href="${pageContext.request.contextPath}/cart">Shopping cart</a><br>
<a href="${pageContext.request.contextPath}/">Home page</a><br>
</body>
</html>
