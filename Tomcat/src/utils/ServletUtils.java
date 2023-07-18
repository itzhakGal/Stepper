package utils;

import jakarta.servlet.ServletContext;

import stepper.role.Role;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngine;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.UserManager;
import utilWebApp.DTORole;

import java.util.HashMap;
import java.util.Map;

public class ServletUtils {

	private static boolean isAdminExists = false;
	private static int countAdmin = 0;
	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String ROLE_MANAGER_ATTRIBUTE_NAME = "roleManager";
	private static final String SYSTEM_MANAGER_ATTRIBUTE_NAME = "systemManager";
	private static final Object userManagerLock = new Object();
	private static final Object systemEngineLock = new Object();

	private static final Object roleManagerLock = new Object();

	public static RolesManager getRolesManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(ROLE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ROLE_MANAGER_ATTRIBUTE_NAME, new RolesManager());
			}
		}
		return (RolesManager) servletContext.getAttribute(ROLE_MANAGER_ATTRIBUTE_NAME);
	}

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (roleManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static SystemEngineInterface getSystemManager(ServletContext servletContext) {
		synchronized (systemEngineLock) {
			if (servletContext.getAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME, new SystemEngine());
			}
		}
		return (SystemEngineInterface) servletContext.getAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getCountAdmin() {
		return countAdmin;
	}

	public static void setCountAdmin(int countAdmin) {
		ServletUtils.countAdmin = ServletUtils.countAdmin + countAdmin;
	}

	public static boolean isAdminExists() {
		return isAdminExists;
	}

	public static void setAdminExists(boolean adminExists) {
		isAdminExists = adminExists;
	}

}
