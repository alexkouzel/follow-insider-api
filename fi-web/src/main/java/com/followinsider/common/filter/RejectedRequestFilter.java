package com.followinsider.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RejectedRequestFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);

        } catch (RequestRejectedException e) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);

        } catch (IOException ignored) {

            // This is a hack to avoid logging errors when the client closes the connection

        } catch (Exception e) {
            log.error("Failed HTTP request :: {}", e.getMessage());
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong.");
        }
    }

}
