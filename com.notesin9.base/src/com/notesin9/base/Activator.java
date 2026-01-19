package com.notesin9.base;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {

    public static Activator instance;

    public Activator() {
        super();
        instance = this;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        System.out.println("NotesIn9 Base Library started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("NotesIn9 Base Library stopped.");
        instance = null;
        super.stop(context);
    }
}
