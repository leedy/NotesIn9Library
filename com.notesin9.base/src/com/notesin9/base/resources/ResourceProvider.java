package com.notesin9.base.resources;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.FrameworkUtil;

import com.notesin9.base.util.LibraryUtils;
import com.ibm.xsp.webapp.FacesResourceServlet;
import com.ibm.xsp.webapp.resources.BundleResourceProvider;

/**
 * Provides static resources (CSS, JS, images) from this bundle.
 * Resources are served from /resources/web/ at URL path /.ibmxspres/.notesin9/
 */
public class ResourceProvider extends BundleResourceProvider {

    public static final String RESOURCE_NAMESPACE = "notesin9";
    public static final String BUNDLE_RES_PATH = "/resources/web/";
    public static final String STARTER_PREFIX = "." + RESOURCE_NAMESPACE;
    public static final String RESOURCE_PATH = FacesResourceServlet.RESOURCE_PREFIX + STARTER_PREFIX + "/";

    public ResourceProvider() {
        super(FrameworkUtil.getBundle(ResourceProvider.class), STARTER_PREFIX);
    }

    @Override
    protected URL getResourceURL(HttpServletRequest request, String name) {
        String path = BUNDLE_RES_PATH + name;
        return LibraryUtils.getResourceURL(getBundle(), path);
    }
}
