# Fosstrak TDT Download #



## Latest Release ##

| **Module** | **Version** | **Download** |
|:-----------|:------------|:-------------|
| TDT        | 1.0.0       | [tdt-1.0.0-with-dependencies.jar](https://oss.sonatype.org/content/repositories/public/org/fosstrak/tdt/tdt/1.0.0/tdt-1.0.0-with-dependencies.jar) |
| TDT        | 0.9.0       | [tdt-0.9.0-with-dependencies.bundle](https://oss.sonatype.org/content/repositories/public/org/fosstrak/tdt/tdt/0.9.0/tdt-0.9.0-with-dependencies.bundle) |
| TDT .NET   |  1.0.0 (alpha version) |  https://github.com/Zambonilli/FOSSTRAK |

The source code for this binary can be found in the Subversion repository at http://fosstrak.googlecode.com/svn/tdt/tags/

## System Requirements ##
  * JDK 1.6.0\_05 or higher

## Installation and Usage Instructions ##
Please see the [User Guide](TdtUserGuide.md) for installation and usage instructions.

## Changelog ##

### TDT .NET Version) ###
  * Mike Lohmeier contributed a TDT implementation for .NET
  * see https://github.com/Zambonilli/FOSSTRAK for more details

### Version 1.0.0 (released April 10, 2012) ###
  * Added support for TDT Specification Version 1.6
  * Updated TDTEngine.java to better handle escaped characters in URIs as well as situations where multiple prefixMatch values appear to match (the problem was introduced because bare 00 and 01 in ELEMENT\_STRING level match any header beginning with 00 or 01)
  * 

### Version 0.9.0 (released June 3, 2009) ###
  * Added support for loading TDT translation schemes from a URL. Thanks to Jochen Mader.
  * Added default constructor that loads EPCglobal TDT translation schemes from within the JAR. (Previously the schemes could only be loaded from an external file system location.) Thanks to Jochen Mader.
  * Manifest file is OSGI bundle-compliant now.
  * Replaced Castor dependency with Xerces. Thanks to Jochen Mader.

### Version 0.4.0 (released July 29, 2008) ###
  * Renamed packages from org.accada to org.fosstrak.