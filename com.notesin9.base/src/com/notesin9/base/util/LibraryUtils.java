package com.notesin9.base.util;

import java.net.URL;
import java.util.Enumeration;
import org.osgi.framework.Bundle;

public class LibraryUtils {

    /**
     * Gets a resource URL from the bundle.
     *
     * @param bundle the bundle
     * @param path the path to the resource
     * @return the resource URL, or null if not found
     */
    public static URL getResourceURL(Bundle bundle, String path) {
        int fileNameIndex = path.lastIndexOf('/');
        String fileName = path.substring(fileNameIndex + 1);
        path = path.substring(0, fileNameIndex + 1);
        Enumeration<?> urls = bundle.findEntries(path, fileName, false);
        if (null != urls && urls.hasMoreElements()) {
            URL url = (URL) urls.nextElement();
            if (null != url) {
                return url;
            }
        }
        return null;
    }
}
