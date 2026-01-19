package com.notesin9.base.resources;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.notesin9.base.Activator;
import com.notesin9.base.util.LibraryUtils;
import com.ibm.xsp.webapp.FacesResourceServlet;
import com.ibm.xsp.webapp.resources.BundleResourceProvider;
import com.ibm.xsp.webapp.resources.URLResourceProvider;

public class ResourceProvider extends BundleResourceProvider {

    public static final String RESOURCE_NAMESPACE = "notesin9";
    public static final String BUNDLE_RES_PATH = "/resources/web/";
    public static final String STARTER_PREFIX = "." + RESOURCE_NAMESPACE;
    public static final String RESOURCE_PATH = FacesResourceServlet.RESOURCE_PREFIX + STARTER_PREFIX + "/";

    private static final long LAST_MODDATE = Activator.instance.getBundle().getLastModified();

    protected final Map<String, CacheableResource> resources = new HashMap<String, CacheableResource>();

    public ResourceProvider() {
        super(Activator.instance.getBundle(), STARTER_PREFIX);
    }

    @Override
    protected boolean shouldCacheResources() {
        return true;
    }

    @Override
    protected URL getResourceURL(HttpServletRequest request, String name) {
        String path = BUNDLE_RES_PATH + name;
        URL resourcePath = LibraryUtils.getResourceURL(getBundle(), path);
        return resourcePath;
    }

    @Override
    public synchronized URLResource addResource(String paramString, URL paramURL) {
        CacheableResource localURLResource = new CacheableResource(paramString, paramURL);
        if (shouldCacheResources()) {
            this.resources.put(paramString, localURLResource);
        }
        return localURLResource;
    }

    protected static long getLastModificationDate() {
        return LAST_MODDATE;
    }

    protected class CacheableResource extends URLResourceProvider.URLResource {

        protected CacheableResource(String paramString, URL paramURL) {
            super(paramString, paramURL);
        }

        @Override
        protected long getLastModificationDate() {
            return ResourceProvider.getLastModificationDate();
        }

        @Override
        protected boolean isResourcesModifiedSince(long paramLong) {
            long l = getLastModificationDate();
            if (l >= 0L) {
                return (paramLong < l);
            }
            return true;
        }
    }
}
