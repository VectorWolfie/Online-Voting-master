package com.Controller.Admin.Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/admin/*")
public class AdminAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("uname") != null);

        String loginURI = httpRequest.getContextPath() + "/Admin/AdminLogin";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isLoginPage = httpRequest.getRequestURI().endsWith("AdminLogin.jsp");

        if (isLoggedIn && (isLoginRequest || isLoginPage)) {
            // The admin is already logged in and trying to login again,
            // so forward to the admin's homepage.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Admin/");
        } else if (!isLoggedIn || isLoginRequest) {
            // The admin is not logged in, or the request is for the login page,
            // so allow the request to proceed to the login page or continue the filter chain.
            chain.doFilter(request, response);
        } else {
            // The admin is not logged in, so authentication is required.
            // Forward to the Login page.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/AdminLogin.jsp");
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
