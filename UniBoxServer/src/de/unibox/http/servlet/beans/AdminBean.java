package de.unibox.http.servlet.beans;

import java.io.Serializable;

public class AdminBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6856231816699516062L;
    private String adminMenu = "";
    private String adminUrl = "";

    public AdminBean() {
    }
    
    public String getAdminMenu() {
        return adminMenu;
    }

    public void setAdminMenu(String adminMenu) {
        this.adminMenu = adminMenu;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

}
