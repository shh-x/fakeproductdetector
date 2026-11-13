package com.fakedetector.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/manufacturer/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);   // false = don't create one
        boolean loggedIn = session != null && session.getAttribute("manufacturerId") != null;

        if (!loggedIn) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;                                        // stop here: door stays shut
        }
        chain.doFilter(req, res);                          // let them through
    }
}