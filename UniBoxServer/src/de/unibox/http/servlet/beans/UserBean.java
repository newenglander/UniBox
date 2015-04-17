package de.unibox.http.servlet.beans;

/**
 * The Class AdminBean.
 */
public class UserBean extends AdminBean {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -256128157289188446L;

    private String username = null;

    private String session = null;

    public UserBean(String username, String session, boolean isAdmin) {
        super(isAdmin);
        this.username = username;
        this.session = session;
    }

    public UserBean() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public String getSession() {
        return session;
    }

}
