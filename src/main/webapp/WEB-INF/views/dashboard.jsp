<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Dashboard</title>
<style>
  body { font-family: system-ui, sans-serif; margin: 16px; }
  .cards { display:flex; flex-wrap:wrap; gap:12px; }
  .stat { border:1px solid #999; border-radius:8px; padding:12px 16px; min-width:110px; text-align:center; }
  .stat b { font-size:1.6rem; display:block; }
  .stat.warn b { color:#b26a00; }
  .stat.bad b { color:#b00020; }
  table { border-collapse:collapse; margin-bottom:24px; }
  th, td { border:1px solid #999; padding:6px 10px; text-align:left; }
  .wrap { overflow-x:auto; }
</style></head>
<body>
  <h1>Welcome, <c:out value="${sessionScope.companyName}"/></h1>
  <p><a href="${pageContext.request.contextPath}/manufacturer/products">Manage products and QR codes</a></p>
  <form method="post" action="${pageContext.request.contextPath}/logout">
    <button type="submit">Log out</button>
  </form>

  <c:if test="${not empty error}"><p style="color:red"><c:out value="${error}"/></p></c:if>
  <c:if test="${not empty param.updated}"><p style="color:green">Code <c:out value="${param.updated}"/>ed.</p></c:if>

  <h2>Overview</h2>
  <div class="cards">
    <div class="stat"><b>${summary.products}</b>Products</div>
    <div class="stat"><b>${summary.qrCodes}</b>QR codes</div>
    <div class="stat"><b>${summary.totalScans}</b>Total scans</div>
    <div class="stat warn"><b>${summary.suspicious}</b>Suspicious</div>
    <div class="stat bad"><b>${summary.blocked}</b>Blocked</div>
  </div>

  <h2>Scans per product</h2>
  <c:choose>
    <c:when test="${empty productStats}"><p>No products yet.</p></c:when>
    <c:otherwise>
      <div class="wrap"><table>
        <tr><th>Product</th><th>QR codes</th><th>Total scans</th><th>Most-scanned code</th></tr>
        <c:forEach var="s" items="${productStats}">
          <tr>
            <td><a href="${pageContext.request.contextPath}/manufacturer/product?id=${s.productId}"><c:out value="${s.name}"/></a></td>
            <td>${s.qrCodes}</td><td>${s.totalScans}</td><td>${s.maxScans}</td>
          </tr>
        </c:forEach>
      </table></div>
    </c:otherwise>
  </c:choose>

  <h2>Codes that need attention</h2>
  <p>Scanned more than ${threshold} times, or blocked. Many scans from <b>many different IPs</b> suggests copies exist.
     Many scans from one IP is usually just someone testing.</p>
  <c:choose>
    <c:when test="${empty suspicious}"><p>Nothing suspicious right now.</p></c:when>
    <c:otherwise>
      <div class="wrap"><table>
        <tr><th>QR #</th><th>Product</th><th>Scans</th><th>Different IPs</th><th>Last 24h</th><th>Last scan</th><th>Status</th><th></th></tr>
        <c:forEach var="s" items="${suspicious}">
          <tr>
            <td>${s.qrId}</td>
            <td><c:out value="${s.productName}"/></td>
            <td>${s.scanCount}</td><td>${s.distinctIps}</td><td>${s.last24h}</td>
            <td><c:out value="${s.lastScan}"/></td>
            <td><c:out value="${s.blocked ? 'BLOCKED' : 'ACTIVE'}"/></td>
            <td>
              <form method="post" action="${pageContext.request.contextPath}/manufacturer/qr-status">
                <input type="hidden" name="id" value="${s.qrId}">
                <input type="hidden" name="action" value="${s.blocked ? 'unblock' : 'block'}">
                <input type="hidden" name="csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
                <button type="submit">${s.blocked ? 'Unblock' : 'Block'}</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </table></div>
    </c:otherwise>
  </c:choose>

  <h2>Recent scans</h2>
  <c:choose>
    <c:when test="${empty recentScans}">
      <p>No scans yet.</p>
    </c:when>
    <c:otherwise>
      <div class="wrap"><table>
        <tr><th>Time</th><th>Product</th><th>QR #</th><th>IP</th></tr>
        <c:forEach var="r" items="${recentScans}">
          <tr>
            <td><c:out value="${r.scannedAt}"/></td>
            <td><c:out value="${r.productName}"/></td>
            <td>${r.qrId}</td>
            <td><c:out value="${r.ipAddress}"/></td>
          </tr>
        </c:forEach>
      </table></div>
    </c:otherwise>
  </c:choose>
</body>
</html>