package org.web.truliance.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter
public class Filter implements jakarta.servlet.Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hsrequest = (HttpServletRequest) request;
        HttpServletResponse hsresponse = (HttpServletResponse) response;
        HttpSession session = hsrequest.getSession(false);

        hsresponse.setHeader("cache-control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        hsresponse.setHeader("pragma", "no-cache"); // HTTP 1.0.
        hsresponse.setDateHeader("expires", 0); // Proxies.

        if(hsrequest.getCookies()!=null){
            for(int i= 0; i<hsrequest.getCookies().length;i++){
                hsrequest.getCookies()[i] = eliminate_cookies(hsrequest.getCookies()[i]);
            }
        }

        chain.doFilter(request, response);
    }

    public Cookie eliminate_cookies(Cookie cookie) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);

        return cookie;
    }


    @Override
    public void destroy() {

    }

}
