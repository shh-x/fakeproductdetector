<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Dashboard</title></head>
<body>
  <h1>Welcome, <c:out value="${sessionScope.companyName}"/></h1>
  <p>Products and QR generation will appear here in the next step.</p>
  <form method="post" action="${pageContext.request.contextPath}/logout">
    <button type="submit">Log out</button>
  </form>
</body>
</html>