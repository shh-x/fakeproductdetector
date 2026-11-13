<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Register</title></head>
<body>
  <h1>Manufacturer Registration</h1>
  <c:if test="${not empty error}">
    <p style="color:red"><c:out value="${error}"/></p>
  </c:if>
  <form method="post" action="${pageContext.request.contextPath}/register">
    <p><label>Company name<br><input type="text" name="company" value="<c:out value='${company}'/>" required></label></p>
    <p><label>Email<br><input type="email" name="email" value="<c:out value='${email}'/>" required></label></p>
    <p><label>Password (min 8 characters)<br><input type="password" name="password" required></label></p>
    <button type="submit">Register</button>
  </form>
  <p>Already registered? <a href="${pageContext.request.contextPath}/login">Log in</a></p>
</body>
</html>