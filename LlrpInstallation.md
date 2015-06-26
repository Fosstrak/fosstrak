# Installation #

The LLRP Commander is an Eclipse plugin which is installed via the Eclipse Update mechanism.

Eclipse Update Site URL:

```
http://fosstrak.googlecode.com/svn/llrp/eclipse_update_site
```

## Overview ##



## Requirements ##

To use the Fosstrak LLRP Commander, you need [Eclipse](http://http//www.eclipse.org/downloads/) 3.3 or later and JRE/JDK1.5 or later.

## Installation via Eclipse Update Manager ##

Please follow these installation instructions step by step to install the LLRP Commander via the Eclipse Update Manager.

1. Run the Eclipse Update Manager

![http://fosstrak.googlecode.com/svn/wikires/llrp/softwareUpdate.png](http://fosstrak.googlecode.com/svn/wikires/llrp/softwareUpdate.png)

2. Add the Fosstrak LLRP Commander update site.

![http://fosstrak.googlecode.com/svn/wikires/llrp/addUpdateSite.png](http://fosstrak.googlecode.com/svn/wikires/llrp/addUpdateSite.png)

3. Select the LLRP Commander and press _Install_

![http://fosstrak.googlecode.com/svn/wikires/llrp/installCommander.png](http://fosstrak.googlecode.com/svn/wikires/llrp/installCommander.png)

4. Follow the installation instructions (this may take some time) and restart Eclipse when the Update Manager asks you to do so.

## Manual Installation ##

To install the LLRP Commander without Eclipse Update Manager, perform the following steps:

  1. Obtain a copy of the LLRP Commander from Fosstrak's Maven repository: `http://maven-repository.fosstrak.org/releases/org/fosstrak/llrp/llrp_commander/`.
  1. Unpack the archive `llrp_commander-<<<VERSION>>.zip` into the plugins directory of your Eclipse installation.
  1. Start Eclipse.

## Activate the Plugin ##

1. To activate the plugin, you need to select the LLRP Commander perspective:

![http://fosstrak.googlecode.com/svn/wikires/llrp/openPerspective-1.png](http://fosstrak.googlecode.com/svn/wikires/llrp/openPerspective-1.png)

2. Sometimes the perspective fails to load all views. In this case, choose _Window - Show View - Other_ and select the LLRP Commander views as shown below.

![http://fosstrak.googlecode.com/svn/wikires/llrp/showView-1.png](http://fosstrak.googlecode.com/svn/wikires/llrp/showView-1.png)


## Using the Plugin ##

See the [user guide](LlrpUserGuide.md) for details.