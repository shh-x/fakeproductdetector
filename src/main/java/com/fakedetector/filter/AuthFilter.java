package com.fakedetector.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@WebFilter("/manufacturer/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
HttpServletResponse response = (HttpServletResponse) res;

HttpSession session = request.getSession(false);
boolean loggedIn = session != null && session.getAttribute("manufacturerId") != null;
if (!loggedIn) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}

if ("POST".equals(request.getMethod())) {
    request.setCharacterEncoding("UTF-8");   // MUST come before the first getParameter()
    String sent = request.getParameter("csrf");
    String expected = (String) session.getAttribute("csrfToken");
    if (sent == null || expected == null || !MessageDigest.isEqual(
            sent.getBytes(StandardCharsets.UTF_8), expected.getBytes(StandardCharsets.UTF_8))) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
}
chain.doFilter(req, res);                          // let them through
    }
}