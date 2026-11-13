<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login</title></head>
<body>
  <h1>Manufacturer Login</h1>
  <c:if test="${param.registered == '1'}">
    <p style="color:green">Registration successful. Please log in.</p>
  </c:if>
  <c:if test="${not empty error}">
    <p style="color:red"><c:out value="${error}"/></p>
  </c:if>
  <form method="post" action="${pageContext.request.contextPath}/login">
    <p><label>Email<br><input type="email" name="email" value="<c:out value='${email}'/>" required></label></p>
    <p><label>Password<br><input type="password" name="password" required></label></p>
    <button type="submit">Log in</button>
  </form>
  <p>New here? <a href="${pageContext.request.contextPath}/register">Register</a></p>
</body>
</html>