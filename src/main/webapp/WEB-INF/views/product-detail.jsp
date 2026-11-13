<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Product</title>
<style>
  .grid { display:flex; flex-wrap:wrap; gap:16px; }
  .card { border:1px solid #999; padding:8px; width:170px; text-align:center; font-size:14px; }
  .card img { width:150px; height:150px; }
  @media print { .noprint { display:none; } }
</style></head>
<body>
  <p class="noprint"><a href="${pageContext.request.contextPath}/manufacturer/products">&larr; My products</a></p>
  <h1><c:out value="${product.name}"/></h1>
  <p>Price: <c:out value="${product.price}"/></p>

  <div class="noprint">
    <c:if test="${not empty param.generated}"><p style="color:green">Created ${param.generated} QR codes.</p></c:if>
    <c:if test="${param.error == 'count'}"><p style="color:red">Enter a number from 1 to 100.</p></c:if>

    <form method="post" action="${pageContext.request.contextPath}/manufacturer/product?id=${product.productId}">
      <label>How many QR codes? <input type="number" name="count" min="1" max="100" value="10" required></label>
      <button type="submit">Generate</button>
    </form>
    <p><button onclick="window.print()">Print this page</button></p>
  </div>

  <h2>QR codes (${qrCodes.size()})</h2>
  <div class="grid">
    <c:forEach var="q" items="${qrCodes}">
      <div class="card">
        <img src="${pageContext.request.contextPath}/manufacturer/qr-image?id=${q.qrId}" alt="QR ${q.qrId}">
        <div>#<c:out value="${q.qrId}"/></div>
        <div>Scans: <c:out value="${q.scanCount}"/></div>
        <div><c:out value="${q.status}"/></div>
      </div>
    </c:forEach>
  </div>
</body>
</html>