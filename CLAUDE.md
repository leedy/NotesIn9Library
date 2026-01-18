# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

NotesIn9Library is an Eclipse RCP/OSGi plugin library for IBM Lotus Notes/Domino XPages applications. It provides reusable utilities and library infrastructure for XPages development.

## Architecture

The project follows a standard Eclipse PDE three-tier structure:

- **com.notesin9.base** - Core plugin bundle containing the source code
- **com.notesin9.base.feature** - Feature bundle for packaging and distribution
- **com.notesin9.base.updatesite** - Update site for deploying the feature to Notes/Domino

### Key Classes

- `NotesIn9Library` (`com.notesin9.base.library`) - XPages library registration, extends `AbstractXspLibrary`
- `Strings` (`com.notesin9.base.util`) - String manipulation utilities

## Build System

This is an Eclipse PDE project, not a Maven/Gradle project.

**To build:**
1. Import all three projects into Eclipse with PDE (Plugin Development Environment) installed
2. Use `File > Export > Plug-in Development > Deployable features` to build distributable artifacts

**Compilation output:** `bin/` directory in each plugin project

## Technical Details

- **Java Version:** JavaSE-1.8
- **OSGi Activation:** Lazy (loaded on-demand)
- **Library ID:** `com.notesin9.base.library`
- **Dependencies:** `com.ibm.xsp.core`, `com.ibm.xsp.extsn`, `com.ibm.xsp.domino`, `com.ibm.xsp.designer`

## Testing

No automated test framework is configured. Testing is done by deploying the feature to a Notes/Domino instance and consuming the library in an XPages application.
