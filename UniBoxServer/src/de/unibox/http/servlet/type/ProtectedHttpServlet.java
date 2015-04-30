package de.unibox.http.servlet.type;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.core.provider.Helper;
import de.unibox.http.servlet.beans.UserBean;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.user.AbstractUser;
import de.unibox.model.user.AdministratorUser;
import de.unibox.model.user.RegisteredUser;
import de.unibox.model.user.UserFactory;
import de.unibox.model.user.UserType;

/**
 * The Class ProtectedHttpServlet is a Security Layer for Servlets which should
 * prevent these from unauthorized access.
 */
public class ProtectedHttpServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7033488871704480169L;

	/** The error message. */
	private String errorMessage = null;

	/** The log. */
	protected Logger log = Logger.getLogger("UniBoxLogger");

	/** The this session. */
	protected HttpSession thisSession = null;

	/** The this user. */
	protected AbstractUser thisUser = null;

	/**
	 * Auth.
	 *
	 * @param request
	 *            the request
	 * @return true, if successful
	 */
	protected boolean auth(final HttpServletRequest request) {

		// no session is authed by default
		boolean isAuthed = false;

		// get session
		this.thisSession = request.getSession();

		// create an abstract user object
		if (InternalConfig.isLogAuthentification()) {
			this.log.debug(ProtectedHttpServlet.class.getSimpleName()
					+ ": session: " + this.thisSession.getId()
					+ " - login.object: "
					+ this.thisSession.getAttribute("login.object"));
		}

		try {

			// retrieve user from session
			this.thisUser = (AbstractUser) this.thisSession
					.getAttribute("login.object");
			if ((this.thisUser instanceof RegisteredUser)
					|| (this.thisUser instanceof AdministratorUser)) {
				isAuthed = true;
				if (InternalConfig.isLogAuthentification()) {
					this.log.debug(ProtectedHttpServlet.class.getSimpleName()
							+ ": access granted for "
							+ this.thisUser.toString());
				}
			} else {

				// retrieve user from database with given dataset
				final String nick = request.getParameter("nick");
				final String passwordBase64 = request.getParameter("password");

				if ((nick != null) && (passwordBase64 != null)) {

					try {

						if (!nick.isEmpty() && !passwordBase64.isEmpty()) {

							final String passwordPlain = Helper
									.decodeBase64(passwordBase64);
							final String password = Helper.md5(passwordPlain);

							// high critical logging!!! log will contain
							// password data..
							if (InternalConfig.isLogAuthentification()) {
								this.log.debug(ProtectedHttpServlet.class
										.getSimpleName()
										+ ": Try to match Nick: '"
										+ nick
										+ "' MD5: '"
										+ password
										+ "' Base64: '"
										+ passwordBase64 + "'");
							}

							final DatabaseQuery query = new DatabaseQuery();
							query.connect();

							final PreparedStatement statement = query
									.getQuery("SELECT * FROM player WHERE Name = ? AND Password = ?;");

							statement.setString(1, nick);
							statement.setString(2, password);

							final ResultSet result = statement.executeQuery();

							query.commit();

							if (result.next()) {

								final int isAdmin = result
										.getInt("AdminRights");
								UserType thisUserType = null;
								if (isAdmin == 1) {
									thisUserType = UserType.ADMINISTRATOR;
								} else {
									thisUserType = UserType.REGISTERED;
								}

								final String username = result
										.getString("Name");
								final int userID = result.getInt("PlayerID");
								this.thisUser = UserFactory
										.getUserByName(username);

								if ((this.thisUser != null)
										&& (this.thisUser.getPlayerId() == userID)) {
									if (InternalConfig.isLogAuthentification()) {
										this.log.debug(ProtectedHttpServlet.class
												.getSimpleName()
												+ ": user already logged in. received user object from UserFactory..");
									}
								} else {
									if (InternalConfig.isLogAuthentification()) {
										this.log.debug(ProtectedHttpServlet.class
												.getSimpleName()
												+ ": new user logged in. UserFactory is creating user object.");
									}
									this.thisUser = UserFactory.createUser(
											thisUserType,
											this.thisSession.getId());
									this.thisUser.setName(result
											.getString("Name"));
									this.thisUser.setPlayerId(userID);
									this.thisUser.setSessionId(this.thisSession
											.getId());
								}

								if (InternalConfig.isLogAuthentification()) {
									this.log.debug(ProtectedHttpServlet.class
											.getSimpleName()
											+ ": auth request for  "
											+ this.thisUser.getName()
											+ " - ID: "
											+ this.thisUser.getSessionId()
											+ " - Object: " + this.thisUser);
								}

								this.thisSession.setAttribute("login.object",
										this.thisUser);
								isAuthed = true;

							} else {
								if (InternalConfig.isLogAuthentification()) {
									this.log.debug(ProtectedHttpServlet.class
											.getSimpleName()
											+ ": invalid credentials. could not auth request.");
								}
								this.setErrorMessage("invalid_credentials");
							}
						} else {
							if (InternalConfig.isLogAuthentification()) {
								this.log.debug(ProtectedHttpServlet.class
										.getSimpleName()
										+ ": invalid request detected. incomplete user/pw? ");
							}
						}

					} catch (final NullPointerException e) {
						if (InternalConfig.isLogAuthentification()) {
							this.log.debug(ProtectedHttpServlet.class
									.getSimpleName()
									+ ": Error. mySQL offline?");
						}
						e.printStackTrace();
					}
				}
			}
		} catch (final Exception e) {
			if (InternalConfig.isLogAuthentification()) {
				this.log.debug(ProtectedHttpServlet.class.getSimpleName()
						+ ": transaction error. could not auth request.");
			}
			e.printStackTrace();
		}
		return isAuthed;
	}

	/**
	 * Do service.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void doService(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		super.service(request, response);
	}

	private String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * Invalid request.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void invalidRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write("BAD_REQUEST");
	}

	/**
	 * Reflect with error message.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void reflectWithErrorMessage(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		if (InternalConfig.isLogAuthentification()) {
			this.log.debug(ProtectedHttpServlet.class.getSimpleName()
					+ ": reflect with error message: " + this.getErrorMessage());
		}
		final String url = request.getRequestURL().toString();
		final String baseURL = url.substring(0, url.length()
				- request.getRequestURI().length())
				+ request.getContextPath() + "/";
		response.sendRedirect(baseURL + "?handle=" + this.getErrorMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse)
	 */
	@Override
	public void service(final ServletRequest req, final ServletResponse res)
			throws ServletException, IOException {
		HttpServletRequest request;
		HttpServletResponse response;
		try {
			request = (HttpServletRequest) req;

			if (InternalConfig.isLogRequestHeader()) {
				final Enumeration<String> headerNames = request
						.getHeaderNames();
				this.log.debug(this.getClass().getSimpleName()
						+ ": RequestHeader:");
				while (headerNames.hasMoreElements()) {
					final String headerName = headerNames.nextElement();
					this.log.debug(headerName + ": "
							+ request.getHeader(headerName));
				}
			}

			response = (HttpServletResponse) res;

			// predefine standard response
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setStatus(HttpServletResponse.SC_OK);

		} catch (final ClassCastException e) {
			throw new ServletException("non-HTTP request or response");
		}
		if (this.auth(request)) {
			this.doService(request, response);
		} else {
			if (this.getErrorMessage() != null) {
				try {
					this.reflectWithErrorMessage(request, response);
				} catch (final IOException e) {
					this.serviceDenied(request, response);
				}
			} else {
				this.serviceDenied(request, response);
			}
		}
	}

	/**
	 * Service denied.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void serviceDenied(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		if (InternalConfig.isLogAuthentification()) {
			this.log.debug(ProtectedHttpServlet.class.getSimpleName()
					+ ": service denied");
		}
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		try {
			request.getRequestDispatcher("/login.html").include(request,
					response);
		} catch (final ServletException e) {
			response.getWriter().write("Access forbidden!");
			e.printStackTrace();
		}
	}

	/**
	 * Service denied with params.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param params
	 *            the params
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void serviceDeniedWithParams(final HttpServletRequest request,
			final HttpServletResponse response, final String params)
			throws IOException {
		if (InternalConfig.isLogAuthentification()) {
			this.log.debug(ProtectedHttpServlet.class.getSimpleName()
					+ ": service denied with params: " + params);
		}
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			request.setAttribute("error", this.getErrorMessage());
			request.getRequestDispatcher("/login.html").include(request,
					response);
		} catch (final ServletException e) {
			response.getWriter().write("Access forbidden!");
			e.printStackTrace();
		}
	}

	/**
	 * Service error message.
	 *
	 * @param response
	 *            the response
	 * @param message
	 *            the message
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void serviceErrorMessage(final HttpServletResponse response,
			final String message) throws IOException {
		if (InternalConfig.isLogAuthentification()) {
			this.log.debug(ProtectedHttpServlet.class.getSimpleName()
					+ ": service error message: " + message);
		}
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(message);
		response.getWriter().flush();
		response.getWriter().close();
	}

	private void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Validate user bean.
	 */
	protected void validateUserBean() {
		final Object obj = this.thisSession.getAttribute("user.bean");
		UserBean userBean = null;
		if ((obj != null) && (obj instanceof UserBean)) {
			userBean = (UserBean) obj;
		} else {
			if (this.thisUser instanceof AdministratorUser) {
				userBean = new UserBean(this.thisUser.getName(),
						this.thisUser.getSessionId(), true);
			} else {
				userBean = new UserBean(this.thisUser.getName(),
						this.thisUser.getSessionId(), false);
			}
			this.thisSession.setAttribute("userbean", userBean);
		}
	}

}
