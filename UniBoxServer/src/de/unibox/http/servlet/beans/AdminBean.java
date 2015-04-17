package de.unibox.http.servlet.beans;

import java.io.Serializable;

/**
 * The Class AdminBean.
 */
public class AdminBean implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6856231816699516062L;

    /** The admin menu. */
    private String adminMenu;

    public AdminBean() {
        super();
    }

    /**
     * Instantiates a new admin bean.
     */
    public AdminBean(boolean isAdmin) {
        if (isAdmin) {
            this.adminMenu = "<li><a class='whiteText' id='triggerAdmin' href='#'>Administration</a></li>";
        } else {
            this.adminMenu = "";
        }
    }

    public String getAdminMenu() {
        return this.adminMenu;
    }

}
