<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>My Products</title></head>
<body>
  <p><a href="${pageContext.request.contextPath}/manufacturer/dashboard">&larr; Dashboard</a></p>
  <h1>My Products</h1>

  <c:if test="${not empty error}">
    <p style="color:red"><c:out value="${error}"/></p>
  </c:if>

  <h2>Add a product</h2>
  <form method="post" action="${pageContext.request.contextPath}/manufacturer/products">
    <p><label>Name<br><input type="text" name="name" maxlength="100" value="<c:out value='${name}'/>" required></label></p>
    <p><label>Price<br><input type="number" name="price" step="0.01" min="0.01" value="<c:out value='${price}'/>" required></label></p>
    <p><label>Manufacture date (optional)<br><input type="date" name="date" value="<c:out value='${date}'/>"></label></p>
    <button type="submit">Add product</button>
  </form>

  <h2>Your products</h2>
  <c:choose>
    <c:when test="${empty products}"><p>No products yet.</p></c:when>
    <c:otherwise>
      <table border="1" cellpadding="6">
        <tr><th>Name</th><th>Price</th><th>Made on</th><th></th></tr>
        <c:forEach var="p" items="${products}">
          <tr>
            <td><c:out value="${p.name}"/></td>
            <td><c:out value="${p.price}"/></td>
            <td><c:out value="${p.manufactureDate}"/></td>
            <td><a href="${pageContext.request.contextPath}/manufacturer/product?id=${p.productId}">Manage QR codes</a></td>
          </tr>
        </c:forEach>
      </table>
    </c:otherwise>
  </c:choose>
</body>
</html>