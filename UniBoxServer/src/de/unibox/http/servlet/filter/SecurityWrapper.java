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
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * The Class SecurityWrapper is a rudimentary try to implement different layers
 * of database security.
 */
@WebFilter(urlPatterns = { "/SecurityWrapper" }, asyncSupported = true)
public class SecurityWrapper implements Filter {

    /**
     * The Class SecurityWrapperHttpServletRequest.
     */
    private static class SecurityWrapperHttpServletRequest extends
            HttpServletRequestWrapper {

        /**
         * Instantiates a new security wrapper http servlet request.
         *
         * @param request
         *            the request
         */
        public SecurityWrapperHttpServletRequest(
                final HttpServletRequest request) {
            super(request);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServletRequestWrapper#isUserInRole(java.lang
         * .String)
         */
        @Override
        public boolean isUserInRole(final String role) {
            switch (role) {
            case ADMINISTRATOR_LEVEL:
                return true;
            case USER_LEVEL:
                return true;
            default:
                return false;
            }
        }
    }

    /** The Constant ADMINISTRATOR_LEVEL. */
    public static final String ADMINISTRATOR_LEVEL = "ADMINISTRATOR";

    /** The Constant USER_LEVEL. */
    public static final String USER_LEVEL = "USER";

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
    public void doFilter(final ServletRequest servletRequest,
            final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest wrappedRequest = new SecurityWrapperHttpServletRequest(
                (HttpServletRequest) servletRequest);
        filterChain.doFilter(wrappedRequest, servletResponse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }
}