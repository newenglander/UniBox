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

	/**
	 * Instantiates a new admin bean.
	 */
	public AdminBean() {
		super();
	}

	/**
	 * Instantiates a new admin bean.
	 *
	 * @param isAdmin
	 *            the is admin
	 */
	public AdminBean(final boolean isAdmin) {
		if (isAdmin) {
			this.adminMenu = "<li><a class='whiteText' id='triggerAdmin' href='#'>Administration</a></li>";
		} else {
			this.adminMenu = "";
		}
	}

	/**
	 * Gets the admin menu.
	 *
	 * @return the admin menu
	 */
	public String getAdminMenu() {
		return this.adminMenu;
	}

}
