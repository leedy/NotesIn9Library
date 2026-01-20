package com.notesin9.base;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public static Bundle bundle;

    @Override
    public void start(BundleContext context) throws Exception {
        bundle = context.getBundle();
        System.out.println("NotesIn9 Base Library started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("NotesIn9 Base Library stopped.");
        bundle = null;
    }
}
