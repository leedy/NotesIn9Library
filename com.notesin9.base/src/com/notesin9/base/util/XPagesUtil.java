package com.notesin9.base.util;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.openntf.domino.Database;
import org.openntf.domino.Session;

import com.ibm.xsp.designer.context.XSPContext;

/**
 * XPages utility methods for accessing context objects and scopes.
 */
public class XPagesUtil {

    /**
     * Gets the current FacesContext.
     */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Gets the current XSPContext.
     */
    public static XSPContext getXSPContext() {
        return XSPContext.getXSPContext(getFacesContext());
    }

    /**
     * Gets the current user's session.
     */
    public static Session getSession() {
        return (Session) resolveVariable("session");
    }

    /**
     * Gets a session running as the signer of the current design element.
     */
    public static Session getSessionAsSigner() {
        return (Session) resolveVariable("sessionAsSigner");
    }

    /**
     * Gets a session running as the signer with full admin access.
     */
    public static Session getSessionAsSignerWithFullAccess() {
        return (Session) resolveVariable("sessionAsSignerWithFullAccess");
    }

    /**
     * Gets the current database.
     */
    public static Database getCurrentDatabase() {
        return (Database) resolveVariable("database");
    }

    /**
     * Resolves a variable by name from the current context.
     */
    public static Object resolveVariable(String variableName) {
        FacesContext context = getFacesContext();
        return context.getApplication().getVariableResolver()
                .resolveVariable(context, variableName);
    }

    /**
     * Gets the application scope map.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getApplicationScope() {
        return (Map<String, Object>) resolveVariable("applicationScope");
    }

    /**
     * Gets the session scope map.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSessionScope() {
        return (Map<String, Object>) resolveVariable("sessionScope");
    }

    /**
     * Gets the view scope map.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getViewScope() {
        return (Map<String, Object>) resolveVariable("viewScope");
    }

    /**
     * Gets the request scope map.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getRequestScope() {
        return (Map<String, Object>) resolveVariable("requestScope");
    }

    /**
     * Gets a value from application scope.
     */
    public static Object getApplicationScopeValue(String key) {
        return getApplicationScope().get(key);
    }

    /**
     * Sets a value in application scope.
     */
    public static void setApplicationScopeValue(String key, Object value) {
        getApplicationScope().put(key, value);
    }

    /**
     * Gets a value from session scope.
     */
    public static Object getSessionScopeValue(String key) {
        return getSessionScope().get(key);
    }

    /**
     * Sets a value in session scope.
     */
    public static void setSessionScopeValue(String key, Object value) {
        getSessionScope().put(key, value);
    }

    /**
     * Gets a value from view scope.
     */
    public static Object getViewScopeValue(String key) {
        return getViewScope().get(key);
    }

    /**
     * Sets a value in view scope.
     */
    public static void setViewScopeValue(String key, Object value) {
        getViewScope().put(key, value);
    }

    /**
     * Gets a value from request scope.
     */
    public static Object getRequestScopeValue(String key) {
        return getRequestScope().get(key);
    }

    /**
     * Sets a value in request scope.
     */
    public static void setRequestScopeValue(String key, Object value) {
        getRequestScope().put(key, value);
    }

    /**
     * Gets the current user's distinguished name.
     */
    public static String getCurrentUser() {
        try {
            return getSession().getEffectiveUserName();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Gets the current user's common name.
     */
    public static String getCurrentUserCommon() {
        try {
            return getSession().createName(getSession().getEffectiveUserName()).getCommon();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Checks if the current user is anonymous.
     */
    public static boolean isUserAnonymous() {
        String user = getCurrentUser();
        return user == null || user.isEmpty() || "Anonymous".equalsIgnoreCase(user);
    }
}
