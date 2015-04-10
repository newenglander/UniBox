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

import de.unibox.config.InternalConfig;

/**
 * The Class SecurityWrapper.
 */
@WebFilter(urlPatterns = { "/SecurityWrapper" }, asyncSupported = true)
public class SecurityWrapper implements Filter {

    /**
     * The Enum SecurityLevel. Essential enum to identify and relate userRoles
     * to privilegs of the db connections they get out of the relevant pool.
     */
    public static enum SecurityLevel {

        /** The administrator. ATM not implemented properly. */
        ADMINISTRATOR(null, "", 5, SecurityWrapper.ADMINISTRATOR_LEVEL),

        /** The user. */
        USER(InternalConfig.DB_USER, "", 15, SecurityWrapper.USER_LEVEL);

        /** The connection count. */
        private final int connectionCount;

        /** The descriptor. */
        private final String descriptor;

        /** The password. */
        private final String password;

        /** The user. */
        private final String user;

        /**
         * Instantiates a new security level.
         *
         * @param user
         *            the user
         * @param password
         *            the password
         * @param connectionCount
         *            the connection count
         * @param descriptor
         *            the descriptor
         */
        private SecurityLevel(final String user, final String password,
                final int connectionCount, final String descriptor) {
            this.user = user;
            this.password = password;
            this.connectionCount = connectionCount;
            this.descriptor = descriptor;
        }

        /**
         * Gets the connection count.
         *
         * @return the connection count
         */
        synchronized public int getConnectionCount() {
            return this.connectionCount;
        }

        /**
         * Gets the password.
         *
         * @return the password
         */
        synchronized public String getPassword() {
            return this.password;
        }

        /**
         * Gets the user.
         *
         * @return the user
         */
        synchronized public String getUser() {
            return this.user;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Enum#toString()
         */
        @Override
        synchronized public String toString() {
            return this.descriptor;
        }
    }

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