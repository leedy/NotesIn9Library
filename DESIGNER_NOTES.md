# Making an OSGi Plugin Visible in Domino Designer

This document explains how to create an OSGi plugin that appears as a selectable XPages Library in Domino Designer, allowing developers to add it to their applications and use its classes.

## Overview

To make your Java classes available to XPages applications, you need:

1. **Plugin Bundle** - Contains your Java code and library registration
2. **Feature** - Packages the plugin for distribution
3. **Update Site** - Deploys the feature to Notes/Domino

## Step 1: Create the Plugin Bundle

### MANIFEST.MF

The manifest defines your plugin's identity and what it exports. Location: `META-INF/MANIFEST.MF`

```
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: Your Library Name
Bundle-SymbolicName: com.yourcompany.library;singleton:=true
Bundle-Version: 1.0.0.qualifier
Export-Package: com.yourcompany.library,
 com.yourcompany.util
Bundle-Vendor: Your Company
Bundle-RequiredExecutionEnvironment: JavaSE-1.8
Bundle-ActivationPolicy: lazy
Require-Bundle: com.ibm.xsp.core,
 com.ibm.xsp.designer,
 com.ibm.commons
```

Key points:
- `singleton:=true` - Required for XPages libraries
- `Export-Package` - Lists packages that consuming applications can access
- `Bundle-ActivationPolicy: lazy` - Plugin loads only when needed
- `Require-Bundle` - Dependencies on XPages runtime

### Library Class

Create a class that extends `AbstractXspLibrary`. This is what makes your plugin appear in Designer.

```java
package com.yourcompany.library;

import com.ibm.xsp.library.AbstractXspLibrary;

public class YourLibrary extends AbstractXspLibrary {

    // This ID is what appears in xsp.properties
    public static final String LIBRARY_ID = "com.yourcompany.library";

    @Override
    public String getLibraryId() {
        return LIBRARY_ID;
    }

    @Override
    public String getPluginId() {
        return "com.yourcompany.library";  // Must match Bundle-SymbolicName
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
        return false;  // true = available to all apps, false = must be selected per-app
    }
}
```

### plugin.xml

Register your library class with the XPages runtime. Location: `plugin.xml` in plugin root.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?eclipse version="3.4"?>
<plugin>
   <extension point="com.ibm.commons.Extension">
      <service type="com.ibm.xsp.Library"
               class="com.yourcompany.library.YourLibrary"/>
   </extension>
</plugin>
```

This extension point is what tells XPages "this plugin provides a library."

### build.properties

Defines what gets included in the built JAR. Location: `build.properties` in plugin root.

```
source.. = src/
output.. = bin/
bin.includes = META-INF/,\
               .,\
               plugin.xml
```

## Step 2: Create the Feature

The feature bundles one or more plugins for distribution.

### feature.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<feature
      id="com.yourcompany.library.feature"
      label="Your Library Feature"
      version="1.0.0.qualifier">

   <description>
      Description of your library.
   </description>

   <plugin
         id="com.yourcompany.library"
         download-size="0"
         install-size="0"
         version="0.0.0"
         unpack="false"/>

</feature>
```

- `version="0.0.0"` in the plugin reference means "use whatever version is available"
- `unpack="false"` keeps the plugin as a JAR (recommended)

## Step 3: Create the Update Site

The update site is what you deploy to Notes/Domino.

### site.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<site>
   <feature id="com.yourcompany.library.feature" version="0.0.0">
      <category name="yourcompany"/>
   </feature>
   <category-def name="yourcompany" label="Your Company Libraries">
      <description>
         Libraries from Your Company.
      </description>
   </category-def>
</site>
```

## Step 4: Build the Update Site

In Eclipse:

1. Right-click the update site project
2. Select **Open Update Site Editor** (or open site.xml)
3. Click **Build All**

This generates:
- `features/` folder with feature JAR
- `plugins/` folder with plugin JAR
- `artifacts.jar` and `content.jar` metadata

## Step 5: Deploy to Notes/Domino

### Option A: Import to Designer (Development)

1. In Designer, go to **File > Application > Install**
2. Select **Search for new features to install**
3. Click **Add Folder Location** and browse to your update site folder
4. Select your feature and complete the wizard
5. Restart Designer

### Option B: Deploy to Server

Copy the update site contents to the Domino server:
- Location: `<domino>/osgi/shared/eclipse/features/` and `.../plugins/`
- Or use an NSF-based update site

Then restart the HTTP task: `restart task http`

## Step 6: Use in an Application

Once deployed, the library appears in Designer:

1. Open your NSF in Designer
2. Go to **Application Properties** (double-click the app icon)
3. Select the **XPages** tab
4. In the **XPages Libraries** section, check your library

This adds a line to `xsp.properties`:
```
xsp.library.depends=com.yourcompany.library
```

Now your application can use any classes from the exported packages.

## Troubleshooting

### Library doesn't appear in Designer

1. Check `plugin.xml` has the correct extension point
2. Verify `MANIFEST.MF` has `singleton:=true`
3. Make sure the library class is in an exported package
4. Restart Designer completely (not just refresh)

### Classes not accessible in XPages

1. Verify the package is listed in `Export-Package` in MANIFEST.MF
2. Check that dependencies are satisfied (Require-Bundle)
3. Look at the server console for class loading errors

### "Library not found" at runtime

1. The feature may not be deployed to the server
2. Check `<domino>/data/domino/workspace/logs/error-log-0.xml` for details
3. Verify the library ID matches exactly in xsp.properties

## Project Structure Reference

```
your-library/
├── com.yourcompany.library/           # Plugin bundle
│   ├── META-INF/
│   │   └── MANIFEST.MF
│   ├── src/
│   │   └── com/yourcompany/
│   │       ├── library/
│   │       │   └── YourLibrary.java   # Library registration
│   │       └── util/
│   │           └── YourUtil.java      # Your utility classes
│   ├── plugin.xml
│   └── build.properties
│
├── com.yourcompany.library.feature/   # Feature bundle
│   ├── feature.xml
│   └── build.properties
│
└── com.yourcompany.library.updatesite/ # Update site
    ├── site.xml
    ├── features/                       # Generated
    └── plugins/                        # Generated
```

## Key Extension Points

| Extension Point | Type | Purpose |
|----------------|------|---------|
| `com.ibm.commons.Extension` | `com.ibm.xsp.Library` | Register an XPages library |
| `com.ibm.commons.Extension` | `com.ibm.xsp.GlobalResourceProvider` | Serve static resources (CSS, JS) |

## Minimum Required Dependencies

For a basic XPages library, you need these in Require-Bundle:

- `com.ibm.xsp.core` - Core XPages runtime
- `com.ibm.xsp.designer` - Designer integration (for library registration)
- `com.ibm.commons` - Extension point framework

Optional but recommended:
- `org.openntf.domino` - OpenNTF Domino API (auto-recycles Domino objects)
