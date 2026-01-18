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
- `StringHelper` (`com.notesin9.base.util`) - String manipulation utilities (isEmpty, isBlank, join, left/right, truncate, etc.)
- `XPagesUtil` (`com.notesin9.base.util`) - XPages context utilities (getSession, scopes, resolveVariable, etc.)

### Documentation

- `SETUP.md` - Guide for building an XPages library from scratch, including Eclipse setup
- `ROADMAP.md` - Planned features and implementation notes (resource provider, future utilities)

## Coding Conventions

- **Always use `org.openntf.domino` classes** instead of `lotus.domino` (OpenNTF handles recycling automatically)
- Utility classes use static methods
- No automated tests - testing done by deploying to Notes/Domino

## Build System

This is an Eclipse PDE project, not a Maven/Gradle project.

**To build:**
1. Import all three projects into Eclipse with PDE (Plugin Development Environment) installed
2. Use `File > Export > Plug-in Development > Deployable features` to build distributable artifacts

**Compilation output:** `bin/` directory in each plugin project

## Technical Details

- **Java Version:** JavaSE-1.8 (targeting Domino 14.5 with Java 21 in future)
- **OSGi Activation:** Lazy (loaded on-demand)
- **Library ID:** `com.notesin9.base.library`
- **Dependencies:** `com.ibm.xsp.core`, `com.ibm.xsp.designer`, `com.ibm.commons`, `com.ibm.designer.runtime`, `org.openntf.domino`

## Testing

No automated test framework is configured. Testing is done by deploying the feature to a Notes/Domino instance and consuming the library in an XPages application.
