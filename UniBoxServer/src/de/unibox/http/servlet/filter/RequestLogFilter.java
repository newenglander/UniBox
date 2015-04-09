package de.unibox.http.servlet.filter;

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

import de.unibox.config.InternalConfig;

/**
 * The Class RequestLogFilter.
 */
@WebFilter(urlPatterns = { "/LogFilter" }, asyncSupported = true)
public class RequestLogFilter extends InternalConfig implements Filter {

    /**
     * Instantiates a new request log filter.
     */
    public RequestLogFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        if (InternalConfig.LOG_REQUESTED_URI) {

            final StringBuilder url = new StringBuilder("");
            final StringBuilder query = new StringBuilder("?");
            final StringBuilder status = new StringBuilder("");
            final StringBuilder method = new StringBuilder("");
            if (request instanceof HttpServletRequest) {
                url.append(((HttpServletRequest) request).getRequestURL()
                        .toString());
                query.append(((HttpServletRequest) request).getQueryString());
                method.append(((HttpServletRequest) request).getMethod());
            }
            final long bef = System.currentTimeMillis();
            chain.doFilter(request, response);
            final long aft = System.currentTimeMillis();
            if (response instanceof HttpServletResponse) {
                status.append(((HttpServletResponse) response).getStatus());
            }
            if (query.toString().equals("?null")) {
                InternalConfig.log.debug("CODE: " + status + ", TIME: "
                        + (aft - bef) + "ms, TYPE: " + method + ", URL: "
                        + url.toString());
            } else {
                InternalConfig.log.debug("CODE: " + status + ", TIME: "
                        + (aft - bef) + "ms, TYPE: " + method + ", URL: "
                        + url.toString() + query.toString());
            }

        } else {
            chain.doFilter(request, response);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig fConfig) throws ServletException {
    }

}
