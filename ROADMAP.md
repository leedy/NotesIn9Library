# NotesIn9Library Roadmap

This document tracks planned features and implementation notes for future development.

---

## IN PROGRESS: Resource Provider for Static Assets

### Current Status (2026-01-19)

**Problem:** Bundle gets stuck in STARTING state with `java.net.MalformedURLException: no protocol: com.notesin9.base` - this error occurs even with ALL extensions disabled and NO Activator.

**What we've tried (all failed with same error):**
1. ❌ Activator extending `Plugin` class
2. ❌ Activator implementing `BundleActivator` interface
3. ❌ No Activator at all (removed Bundle-Activator from MANIFEST.MF)
4. ❌ All plugin.xml extensions commented out
5. ❌ ResourceProvider using `FrameworkUtil.getBundle()` instead of Activator
6. ❌ Removed `Automatic-Module-Name` from MANIFEST.MF
7. ❌ Added `Import-Package: org.osgi.framework`
8. ❌ Simplified ResourceProvider to bare minimum

**Key observation:** The error happens even with a completely stripped-down bundle (no Activator, no extensions). This suggests the issue is NOT in our code, but in:
- Domino 14.5's Update Site NSF processing
- Something specific about how this bundle is being deployed
- Possibly a caching issue on the server
- The bundle symbolic name "com.notesin9.base" being parsed as a URL somewhere in Domino's OSGi runtime

**Next steps to try:**
1. Try changing bundle symbolic name to something different (test if name is the issue)
2. Clear OSGi workspace: `<Domino Data>\domino\workspace\.config`
3. Try direct file deployment instead of Update Site NSF (if possible)
4. Enable OSGi debug logging to get full stack trace
5. Check HCL support/forums for Domino 14.5 Update Site NSF issues
6. Compare with a known working bundle's JAR structure
7. Try building with a different version of Eclipse PDE

**Files created:**
- `com.notesin9.base/src/com/notesin9/base/resources/NotesIn9ResourceProvider.java`
- `com.notesin9.base/src/com/notesin9/base/resources/Resources.java`
- `com.notesin9.base/resources/web/css/.gitkeep`
- `com.notesin9.base/resources/web/js/.gitkeep`

**Files modified:**
- `plugin.xml` - added GlobalResourceProvider extension
- `build.properties` - added resources/ to bin.includes
- `MANIFEST.MF` - added com.notesin9.base.resources to exports

---

## Original Plan: Resource Provider for Static Assets

Serve CSS, JavaScript, and other static files from this library to consuming XPages applications.

### Goal

Provide clean URLs for resources like:
```
/xsp/.ibmxspres/.notesin9/plupload/plupload.min.js
/xsp/.ibmxspres/.notesin9/styles/custom.css
```

### Required Components

#### 1. Activator (Bundle Activator)

An OSGi Activator is required to access the bundle context at runtime. The ResourceProvider needs the Bundle object to locate resources within the plugin JAR.

**What it does:**
- Called automatically when the plugin starts/stops
- Provides access to the `Bundle` object
- Holds a static instance for easy access throughout the library

**Implementation:**

```java
package com.notesin9.base;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public static Activator instance;
    private Bundle bundle;

    @Override
    public void start(BundleContext context) throws Exception {
        instance = this;
        this.bundle = context.getBundle();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
```

**MANIFEST.MF addition:**
```
Bundle-Activator: com.notesin9.base.Activator
```

**Import-Package addition:**
```
Import-Package: org.osgi.framework
```

---

#### 2. ResourceProvider

Extends `BundleResourceProvider` to serve files from a `/resources/web/` directory in the plugin.

**Implementation:**

```java
package com.notesin9.base.resources;

import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import com.ibm.xsp.resource.BundleResourceProvider;
import com.ibm.xsp.util.LibraryUtils;
import com.notesin9.base.Activator;

public class ResourceProvider extends BundleResourceProvider {

    public static final String BUNDLE_RES_PATH = "/resources/web/";
    public static final String PREFIX = ".notesin9";
    public static final String RESOURCE_PATH = "/xsp/.ibmxspres/" + PREFIX + "/";

    public ResourceProvider() {
        super(Activator.instance.getBundle(), PREFIX);
    }

    @Override
    protected URL getResourceURL(HttpServletRequest request, String name) {
        String path = BUNDLE_RES_PATH + name;
        return LibraryUtils.getResourceURL(getBundle(), path);
    }
}
```

---

#### 3. plugin.xml Registration

Register the ResourceProvider as a GlobalResourceProvider:

```xml
<extension point="com.ibm.commons.Extension">
   <service type="com.ibm.xsp.GlobalResourceProvider"
            class="com.notesin9.base.resources.ResourceProvider" />
</extension>
```

---

#### 4. Folder Structure

```
com.notesin9.base/
├── src/
│   └── com/notesin9/base/
│       ├── Activator.java
│       └── resources/
│           └── ResourceProvider.java
├── resources/
│   └── web/
│       ├── js/
│       │   └── (javascript libraries here)
│       ├── css/
│       │   └── (stylesheets here)
│       └── img/
│           └── (images here)
└── META-INF/
    └── MANIFEST.MF
```

---

#### 5. build.properties Update

Include the resources folder in the build:

```
source.. = src/
output.. = bin/
bin.includes = META-INF/,\
               .,\
               plugin.xml,\
               resources/
```

---

#### 6. Usage in XPages

Reference resources using the clean URL:

```xml
<xp:script src="/xsp/.ibmxspres/.notesin9/js/plupload.min.js"
           clientSide="true" />

<xp:styleSheet href="/xsp/.ibmxspres/.notesin9/css/custom.css" />
```

Or use a Resources helper class:

```java
public class Resources {
    public static final String PATH = "/xsp/.ibmxspres/.notesin9/";

    public static final ScriptResource PLUPLOAD =
        new ScriptResource(PATH + "js/plupload.min.js", true);

    public static final StyleSheetResource CUSTOM_CSS =
        new StyleSheetResource(PATH + "css/custom.css");
}
```

---

### Reference

Pattern based on czarnowski.base library implementation. See:
- `com.czarnowski.base.resources.ResourceProvider`
- `com.czarnowski.base.resources.Resources`
- `com.czarnowski.base.Activator`

---

## Future Utility Classes

Potential additions to discuss:

- **Dates** - Date comparison, formatting, business day calculations
- **Names** - Domino name utilities (abbreviated, canonical, common)
- **Collections** - List/array conversion utilities
