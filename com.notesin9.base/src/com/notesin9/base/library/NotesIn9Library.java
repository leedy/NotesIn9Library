package com.notesin9.base.library;

import com.ibm.xsp.library.AbstractXspLibrary;

public class NotesIn9Library extends AbstractXspLibrary {

    public static final String LIBRARY_ID = "com.notesin9.base.library";

    @Override
    public String getLibraryId() {
        return LIBRARY_ID;
    }

    @Override
    public String getPluginId() {
        return "com.notesin9.base";
    }

    @Override
    public String[] getDependencies() {
        return new String[] {
            "com.ibm.xsp.core.library",
            "com.ibm.xsp.extsn.library"
        };
    }

    @Override
    public boolean isGlobalScope() {
        return false;
    }
}
