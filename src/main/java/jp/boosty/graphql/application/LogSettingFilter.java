package jp.boosty.graphql.application;

import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LogSettingFilter implements Filter {

   private final static String IP = "ip";
   private final static String LOG_ID = "logId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {

        try {
            MDC.put(IP, servletRequest.getRemoteAddr());
            MDC.put(LOG_ID, UUID.randomUUID().toString());
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(IP);
            MDC.remove(LOG_ID);
        }
    }

    @Override
    public void destroy() {

    }
}
