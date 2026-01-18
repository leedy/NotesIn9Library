package com.notesin9.base.util;

/**
 * String utility methods for NotesIn9 applications.
 */
public class Strings {

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
}
