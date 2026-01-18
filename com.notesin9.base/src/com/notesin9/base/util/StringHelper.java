package com.notesin9.base.util;

import java.util.Collection;

/**
 * String utility methods for NotesIn9 applications.
 */
public class StringHelper {

    /**
     * Checks if a string is null or empty.
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * Checks if a string is not null and not empty.
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * Checks if a string is null, empty, or contains only whitespace.
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Checks if a string is not null, not empty, and not only whitespace.
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * Returns the string, or empty string if null.
     */
    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * Returns the string, or null if empty.
     */
    public static String emptyToNull(String value) {
        return isEmpty(value) ? null : value;
    }

    /**
     * Capitalizes the first letter of a string.
     */
    public static String capitalize(String value) {
        if (isEmpty(value)) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    /**
     * Truncates a string to the specified length, adding ellipsis if truncated.
     */
    public static String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        if (maxLength <= 3) {
            return value.substring(0, maxLength);
        }
        return value.substring(0, maxLength - 3) + "...";
    }

    /**
     * Safely trims a string, returning null if input is null.
     */
    public static String safeTrim(String value) {
        return value == null ? null : value.trim();
    }

    /**
     * Returns the value if not blank, otherwise returns the default.
     */
    public static String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value;
    }

    /**
     * Joins collection elements into a string using the specified delimiter.
     */
    public static String join(Collection<?> collection, String delimiter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object item : collection) {
            if (!first) {
                sb.append(delimiter);
            }
            sb.append(item == null ? "" : item.toString());
            first = false;
        }
        return sb.toString();
    }

    /**
     * Joins multiple objects into a string using the specified delimiter.
     */
    public static String join(String delimiter, Object... objects) {
        if (objects == null || objects.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            sb.append(objects[i] == null ? "" : objects[i].toString());
        }
        return sb.toString();
    }

    /**
     * Returns the substring to the left of the first occurrence of the search string.
     * Returns the original string if searchFor is not found.
     */
    public static String left(String source, String searchFor) {
        if (isEmpty(source) || isEmpty(searchFor)) {
            return source;
        }
        int index = source.indexOf(searchFor);
        return index < 0 ? source : source.substring(0, index);
    }

    /**
     * Returns the substring to the right of the first occurrence of the search string.
     * Returns empty string if searchFor is not found.
     */
    public static String right(String source, String searchFor) {
        if (isEmpty(source) || isEmpty(searchFor)) {
            return "";
        }
        int index = source.indexOf(searchFor);
        return index < 0 ? "" : source.substring(index + searchFor.length());
    }

    /**
     * Returns the substring to the left of the last occurrence of the search string.
     * Returns the original string if searchFor is not found.
     */
    public static String leftBack(String source, String searchFor) {
        if (isEmpty(source) || isEmpty(searchFor)) {
            return source;
        }
        int index = source.lastIndexOf(searchFor);
        return index < 0 ? source : source.substring(0, index);
    }

    /**
     * Returns the substring to the right of the last occurrence of the search string.
     * Returns empty string if searchFor is not found.
     */
    public static String rightBack(String source, String searchFor) {
        if (isEmpty(source) || isEmpty(searchFor)) {
            return "";
        }
        int index = source.lastIndexOf(searchFor);
        return index < 0 ? "" : source.substring(index + searchFor.length());
    }

    /**
     * Checks if a string starts with a prefix, ignoring case.
     */
    public static boolean startsWithIgnoreCase(String source, String prefix) {
        if (source == null || prefix == null) {
            return false;
        }
        if (prefix.length() > source.length()) {
            return false;
        }
        return source.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    /**
     * Checks if a string ends with a suffix, ignoring case.
     */
    public static boolean endsWithIgnoreCase(String source, String suffix) {
        if (source == null || suffix == null) {
            return false;
        }
        if (suffix.length() > source.length()) {
            return false;
        }
        int startIndex = source.length() - suffix.length();
        return source.regionMatches(true, startIndex, suffix, 0, suffix.length());
    }

    /**
     * Removes all HTML tags from a string.
     */
    public static String stripHtmlTags(String source) {
        if (isEmpty(source)) {
            return source;
        }
        return source.replaceAll("<[^>]*>", "");
    }

    /**
     * Returns only alphanumeric characters (A-Z, a-z, 0-9) from the source string.
     */
    public static String getAlphanumericOnly(String source) {
        if (isEmpty(source)) {
            return source;
        }
        return source.replaceAll("[^A-Za-z0-9]", "");
    }

    /**
     * Creates a string by repeating a character the specified number of times.
     */
    public static String repeat(char c, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Creates a string by repeating a string the specified number of times.
     */
    public static String repeat(String str, int count) {
        if (str == null || count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
