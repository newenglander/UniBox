package de.unibox.http.servlet.beans;

import java.io.Serializable;

/**
 * The Class AdminBean.
 */
public class AdminBean implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6856231816699516062L;

    /** The admin menu. */
    private String adminMenu = "";

    /** The admin url. */
    private String adminUrl = "";

    /**
     * Instantiates a new admin bean.
     */
    public AdminBean() {
    }

    public String getAdminMenu() {
        return this.adminMenu;
    }

    public String getAdminUrl() {
        return this.adminUrl;
    }

    public void setAdminMenu(final String adminMenu) {
        this.adminMenu = adminMenu;
    }

    public void setAdminUrl(final String adminUrl) {
        this.adminUrl = adminUrl;
    }

}
