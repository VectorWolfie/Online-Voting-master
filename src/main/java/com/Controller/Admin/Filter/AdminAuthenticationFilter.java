package com.controller.admin.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This Java filter demonstrates how to intercept the request
 * and transform the response to implement authentication feature
 * for the website's back-end.
 *
 * @author www.codejava.net
 */
@WebFilter("/admin/*")
public class AdminAuthenticationFilter implements Filter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("uname") != null);

        String loginURI = request.getContextPath() + "/Admin/AdminLogin";

        boolean isLoginRequest = request.getRequestURI().equals(loginURI);

        boolean isLoginPage = request.getRequestURI().endsWith("AdminLogin.jsp");

        if (isLoggedIn && (isLoginRequest || isLoginPage)) {
            // The admin is already logged in and attempting to login again,
            // so forward to the admin's homepage.
            response.sendRedirect(request.getContextPath() + "/Admin/");
        } else if (isLoggedIn || isLoginRequest) {
            // Continues the filter chain, allowing the request to reach the destination.
            chain.doFilter(request, response);
        } else {
            // The admin is not logged in, so authentication is required.
            // Forward to the Login page.
            response.sendRedirect(request.getContextPath() + "/AdminLogin.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code can be added here if needed.
    }

    @Override
    public void destroy() {
        // Cleanup code can be added here if needed.
    }
}
