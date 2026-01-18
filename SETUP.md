# Building an XPages Library from Scratch

This guide explains how to create an Eclipse PDE library for IBM Notes/Domino XPages,
including how to wire the plugin, feature, and update site projects together.

## Prerequisites

- Eclipse IDE with PDE (Plugin Development Environment) installed
- IBM Domino Designer or access to XPages SDK bundles
- Java 8 or higher

---

## Eclipse Development Setup

This section walks through setting up Eclipse from scratch for XPages plugin development.

### Installing Eclipse

1. Download **Eclipse IDE for RCP and RAP Developers** from [eclipse.org/downloads](https://www.eclipse.org/downloads/packages/)
2. Extract to a location like `C:\Eclipse\eclipse-rcp` or `/opt/eclipse-rcp`
3. Launch Eclipse

**Why the RCP version?** This edition includes the Plugin Development Environment (PDE) pre-installed. PDE provides the tools needed to create, build, and export OSGi bundles. Other Eclipse editions require manually installing PDE.

### Workspace Setup

1. When Eclipse launches, create a dedicated workspace for Domino development
   - Example: `C:\Workspaces\domino-plugins` or `~/workspaces/domino-plugins`
2. Keep this workspace separate from other projects to avoid target platform conflicts
3. To import existing projects:
   - File > Import > General > Existing Projects into Workspace
   - Browse to the directory containing the plugin, feature, and update site projects
   - Select all three projects and click Finish

### Target Platform Configuration

The **target platform** defines which OSGi bundles are available at development time. Eclipse uses it to resolve dependencies like `com.ibm.xsp.core` in your MANIFEST.MF.

Without a properly configured target platform, your projects will show errors because Eclipse cannot find the XPages bundles.

#### Setting Up the Target Platform

1. Open Window > Preferences > Plug-in Development > Target Platform
2. Click **Add...** to create a new target platform
3. Select "Nothing: Start with an empty target definition" and click Next
4. Give it a name like "Domino XPages Development"
5. Click **Add...** to add locations containing bundles

#### Adding XPages/Domino Bundles

You need the XPages SDK bundles from one of these sources:

**Option A: From Notes/Domino Installation**
- Add Directory location pointing to:
  - `<Notes Install>/framework/shared/eclipse/plugins`
  - `<Notes Install>/framework/rcp/eclipse/plugins`

**Option B: From XPages SDK**
- Download the XPages SDK from OpenNTF
- Add Directory location pointing to the extracted SDK's plugin folders

6. After adding locations, click **Reload** to refresh the bundle list
7. Click **Finish**, then select your new target platform and click **Apply and Close**

### Adding External Dependencies

When your library depends on external OSGi bundles (like OpenNTF Domino API), you need to:

1. **Download the dependency's update site** (usually a ZIP file)
2. **Extract it** to a tools folder, e.g., `C:\DominoTools\openntf-domino-api`
3. **Add to target platform:**
   - Window > Preferences > Plug-in Development > Target Platform
   - Select your target platform and click **Edit...**
   - Click **Add...** > Directory
   - Browse to the `UpdateSite` folder inside the extracted dependency
   - Click **Reload** to refresh
   - Click **Finish** and **Apply and Close**
4. **Add to MANIFEST.MF:**
   - Open your plugin's `META-INF/MANIFEST.MF`
   - Go to the Dependencies tab
   - Click **Add...** under Required Plug-ins
   - Search for and select the dependency bundle
   - Save the file

### Example: Adding org.openntf.domino

The OpenNTF Domino API is a popular enhancement library for XPages development.

1. **Download:**
   - Visit [OpenNTF Domino API](https://openntf.org/main.nsf/project.xsp?r=project/OpenNTF%20Domino%20API)
   - Download the latest release (ZIP file containing the update site)

2. **Extract:**
   - Create a folder: `C:\DominoTools\openntf-domino-api`
   - Extract the ZIP contents there
   - You should see an `UpdateSite` folder with `features/` and `plugins/` subfolders

3. **Add to Target Platform:**
   - Window > Preferences > Plug-in Development > Target Platform
   - Edit your target platform
   - Add > Directory > browse to the `UpdateSite` folder
   - Reload and apply

4. **Add to Your Plugin:**
   - Open `META-INF/MANIFEST.MF`
   - Dependencies tab > Add Required Plug-ins
   - Add the bundles you need:
     - `org.openntf.domino` - Core API
     - `org.openntf.domino.xsp` - XPages integration
     - `org.openntf.formula` - Formula language support (optional)

**Source bundles for Javadoc:** The update site also includes source bundles like `org.openntf.domino.source`. These contain the Java source code and are needed for Javadoc in code completion and hover tooltips. Just include them in your target platform alongside the binary bundles - Eclipse automatically links them. Don't add source bundles to your MANIFEST.MF dependencies; they're development-time only.

### Verifying Your Setup

After configuration, verify everything is working:

1. **No red X markers** on your projects in Package Explorer
2. **Imports resolve correctly:**
   - Open a Java file that imports XPages classes
   - Hover over imports - they should show Javadoc, not "cannot be resolved"
3. **Code completion works:**
   - Type `Session session = ` in a method
   - Press Ctrl+Space - you should see XPages/Domino suggestions
4. **Dependencies tab shows bundles:**
   - Open MANIFEST.MF > Dependencies tab
   - Required Plug-ins should not show error icons

**Common issues:**
- Red X on projects: Target platform not configured or missing bundles
- "Bundle cannot be resolved": Bundle not in target platform, reload it
- Code completion missing classes: Check Export-Package in dependency's MANIFEST.MF

---

## Project Structure Overview

An XPages library consists of three interconnected projects:

```
com.yourcompany.library/           <-- Plugin (contains code)
com.yourcompany.library.feature/   <-- Feature (packages plugins)
com.yourcompany.library.updatesite/<-- Update Site (distributes features)
```

The wiring flows upward: Plugin -> Feature -> Update Site

---

## Step 1: Create the Plugin Project

1. In Eclipse: File > New > Plug-in Project
2. Project name: `com.yourcompany.library`
3. Check "Generate an activator" if you need lifecycle hooks
4. Finish the wizard

### Configure MANIFEST.MF

Edit `META-INF/MANIFEST.MF`:

```
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: Your Library Name
Bundle-SymbolicName: com.yourcompany.library;singleton:=true
Bundle-Version: 1.0.0.qualifier
Bundle-Vendor: Your Company
Bundle-RequiredExecutionEnvironment: JavaSE-1.8
Bundle-ActivationPolicy: lazy
Export-Package: com.yourcompany.library,
 com.yourcompany.library.util
Require-Bundle: org.eclipse.core.runtime,
 com.ibm.xsp.core,
 com.ibm.xsp.extsn,
 com.ibm.xsp.domino,
 com.ibm.xsp.designer
```

Key settings:
- `singleton:=true` - Required for extension point contributions
- `Bundle-ActivationPolicy: lazy` - Plugin loads only when accessed
- `Export-Package` - Lists packages visible to consuming applications
- `Require-Bundle` - Dependencies on other OSGi bundles

### Create the Library Class

Create a class that extends `AbstractXspLibrary`:

```java
package com.yourcompany.library;

import com.ibm.xsp.library.AbstractXspLibrary;

public class YourLibrary extends AbstractXspLibrary {

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
        return false;  // true = available to all apps, false = opt-in per app
    }
}
```

### Create plugin.xml

This registers your library with the XPages framework:

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

### Configure build.properties

```
source.. = src/
output.. = bin/
bin.includes = META-INF/,\
               .,\
               plugin.xml
```

---

## Step 2: Create the Feature Project

1. In Eclipse: File > New > Feature Project
2. Project name: `com.yourcompany.library.feature`
3. Finish the wizard

### Configure feature.xml

Edit `feature.xml` to include your plugin:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<feature
      id="com.yourcompany.library.feature"
      label="Your Library Feature"
      version="1.0.0.qualifier">

   <plugin
         id="com.yourcompany.library"
         download-size="0"
         install-size="0"
         version="0.0.0"
         unpack="false"/>

</feature>
```

Key settings:
- `id` in `<plugin>` must exactly match the Bundle-SymbolicName from MANIFEST.MF
- `version="0.0.0"` tells Eclipse to use whatever version the plugin currently has
- `unpack="false"` keeps the plugin as a JAR (recommended for performance)

### Configure build.properties

```
bin.includes = feature.xml
```

### Wiring: Feature -> Plugin

The connection is made in `feature.xml` through the `<plugin id="...">` element.
The id must exactly match the Bundle-SymbolicName in your plugin's MANIFEST.MF.

To add multiple plugins to a feature, add multiple `<plugin>` elements.

---

## Step 3: Create the Update Site Project

1. In Eclipse: File > New > Update Site Project
2. Project name: `com.yourcompany.library.updatesite`
3. Finish the wizard

### Configure site.xml

Edit `site.xml` to include your feature:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<site>
   <feature url="features/com.yourcompany.library.feature_1.0.0.qualifier.jar"
            id="com.yourcompany.library.feature"
            version="1.0.0.qualifier">
   </feature>
</site>
```

Key settings:
- `id` must exactly match the feature id from feature.xml
- `url` points to where the built feature JAR will be placed
- `version` should match the feature version

### Wiring: Update Site -> Feature

The connection is made in `site.xml` through the `<feature id="...">` element.
The id must exactly match the id attribute in your feature's feature.xml.

---

## Building the Update Site

### Option 1: Eclipse UI

1. Open `site.xml` in the Update Site project
2. Click "Build All" button in the Site Map editor
3. This generates:
   - `features/` folder with feature JAR
   - `plugins/` folder with plugin JAR

### Option 2: Export Wizard

1. File > Export > Plug-in Development > Deployable features
2. Select your feature
3. Choose destination directory
4. Click Finish

---

## Wiring Summary

```
┌─────────────────────────────────────────────────────────────┐
│ Update Site (site.xml)                                      │
│   <feature id="com.yourcompany.library.feature" .../>       │
│                           │                                 │
│                           │ references by id                │
│                           ▼                                 │
├─────────────────────────────────────────────────────────────┤
│ Feature (feature.xml)                                       │
│   id="com.yourcompany.library.feature"                      │
│   <plugin id="com.yourcompany.library" .../>                │
│                           │                                 │
│                           │ references by id                │
│                           ▼                                 │
├─────────────────────────────────────────────────────────────┤
│ Plugin (MANIFEST.MF)                                        │
│   Bundle-SymbolicName: com.yourcompany.library;singleton    │
│                                                             │
│ Plugin (plugin.xml)                                         │
│   <extension point="com.ibm.commons.Extension">             │
│     <service type="com.ibm.xsp.Library" class="..."/>       │
│   </extension>                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Deployment to Notes/Domino

1. Copy the contents of `features/` and `plugins/` folders to:
   - Server: `<domino>/data/domino/workspace/applications/eclipse/`
   - Designer: `<notes>/framework/shared/eclipse/`

2. Restart the server or Designer

3. In your XPages application's Xsp Properties, add the library to the dependencies

---

## Troubleshooting

**Library not appearing in Designer:**
- Verify Bundle-SymbolicName matches the plugin id in feature.xml
- Check plugin.xml has the com.ibm.commons.Extension registration
- Ensure MANIFEST.MF has `singleton:=true`

**ClassNotFoundException at runtime:**
- Verify the package is listed in Export-Package in MANIFEST.MF
- Check Require-Bundle includes all necessary dependencies

**Feature not building:**
- Ensure the plugin project has no compilation errors
- Verify version numbers are consistent (or use 0.0.0 and qualifier)
