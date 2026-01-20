package com.notesin9.base.util;

/*
 * DISABLED - Activator and ResourceProvider temporarily disabled
 * to verify basic library functionality works first.
 *
 * import java.net.URL;
 * import java.util.Enumeration;
 * import org.osgi.framework.Bundle;
 *
 * public class LibraryUtils {
 *
 *     public static URL getResourceURL(Bundle bundle, String path) {
 *         int fileNameIndex = path.lastIndexOf('/');
 *         String fileName = path.substring(fileNameIndex + 1);
 *         path = path.substring(0, fileNameIndex + 1);
 *         Enumeration<?> urls = bundle.findEntries(path, fileName, false);
 *         if (null != urls && urls.hasMoreElements()) {
 *             URL url = (URL) urls.nextElement();
 *             if (null != url) {
 *                 return url;
 *             }
 *         }
 *         return null;
 *     }
 * }
 */
