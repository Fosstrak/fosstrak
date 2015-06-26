# Developer Guide: Building the Code #



## Objective ##

This document addresses users that either want to build the LLRP Commander from source code or intend to extend the LLRP Commander. The document describes the basic steps it takes to assemble the plugin automatically, to generate Eclipse projects and to create the site-documentation. Please read all the steps carefully before you run the commands.

## Heap Space ##

During the build, Maven uses a lot of heap space to compile and to assemble the LLRP Commander Eclipse plugin. If you do not reserve enough memory for your VM, Maven will crash and complain about missing heap space. You can increase the heap space available to Maven via the command line.

```
set MAVEN_OPTS=-Xmx1024m
```

## Prerequisites ##

To complete the guide you need some basic building blocks to be in place.

  * [Java 1.5](http://java.com/) or later. The LLRP Commander will not build under Java 3/4.
  * [Apache Maven](http://maven.apache.org/), version 2.0.9 or higher. Make sure you can run `mvn` on the command line.
  * Subversion to checkout the project from the Fosstrak website.
  * A fresh copy of the Eclipse framework. Currently we build against the Ganymede release SR2. So please retrieve a copy of Ganymede SR2 (otherwise you will have to adjust the version numbers for the Eclipse dependencies). **Download the J2EE edition** (approx. 163 MB).

## Preparations ##

Checkout the latest version from the fosstrak subversion directory or download the source package from the fosstrak website.

```
# checkout the latest source trunk via subversion.
mkdir ~/workspace-fosstrak && cd ~/workspace-fosstrak
svn co https://fosstrak.googlecode.com/svn/llrp/trunk llrp
```

Unpack the Eclipse package to a location of your choice.

**Note**: Currently we build against the Ganymede release SR2. So please retrieve a copy of Ganymede SR2 (otherwise you will have to adjust the version numbers for the eclipse dependencies). **Download the J2EE edition** (approx. 163 MB.

```
example Windows:
   c:\tmp\eclipse

example *nix:
   /tmp/eclipse
```

We use the [Maven Eclipse plugin](http://maven.apache.org/guides/mini/guide-ide-eclipse.html) to store the Eclipse dependencies to the local Maven repository and to generate the Eclipse projects from the Maven project descriptors (pom.xml).

After you deployed eclipse to a folder of your choice open a console/terminal and deploy the Eclipse installation into your local Maven repository. Please ensure that you specify the parameter `-DstripQualifier=true`.

```
example Windows:
   mvn eclipse:to-maven -DeclipseDir=c:\tmp\eclipse -DstripQualifier=true

example *nix:
   mvn eclipse:to-maven -DeclipseDir=/tmp/eclipse -DstripQualifier=true
```

Unfortunately, SWT requires a dependency to the widget library depending on your operating system. Your locally deployed eclipse installation provides this dependency. By default, we specify the windows variant in the file `llrp-commander/pom.xml`. If you install the LLRP Commander on a Windows operating system, you will not have to modify anything and you can proceed with the packaging step.

This step is only required if you run an operating system other than Windows. Open the file `llrp-commander/pom.xml` with a text editor and search for the string `org.eclipse.swt.win32`. Comment the SWT dependency for Windows and activate the dependency for your operating system. In case your operating system is not in the examples list yet, navigate to your local maven repository and open the folder `org/eclipse/swt`. There should be another subfolder containing a `pom.xml` with the `groupId` of your SWT dependency.

```
<!-- Extract from the llrp-commander/pom.xml -->
<!-- 
        please set the swt dependency to the one of you OS.
        Notice: for the plugin runtime this dependency is not 
        required, so the plugin will be plattform independant!
-->
<!-- linux 64bit -->
<!--<dependency>
        <groupId>org.eclipse.swt.gtk.linux</groupId>
        <artifactId>x86_64</artifactId>
        <version>3.4.1</version>
</dependency>-->
<!-- linux 32bit -->
<!--
<dependency>
        <groupId>org.eclipse.swt.gtk.linux</groupId>
        <artifactId>x86_64</artifactId>
        <version>3.4.1</version>
</dependency>
-->
<!-- os x -->
<!-- 
<dependency>
        <groupId>org.eclipse.swt.carbon</groupId>
        <artifactId>macosx</artifactId>
        <version>3.4.1</version>
</dependency>
-->
<!-- windows -->
<dependency>
        <groupId>org.eclipse.swt.win32.win32</groupId>
        <artifactId>x86</artifactId>
        <version>3.4.1</version>
</dependency>   
```

## Packaging ##

To generate the eclipse plugin automatically open a command shell and navigate to the folder where you have the source code installed.

```
example Windows:
   cd c:\workspace-fosstrak\llrp
   
example *nix: 
   cd ~/workspace-fosstrak/llrp
```

Run the command `mvn package`. If you run the command for the first time, Maven will have to download and install all the missing dependencies for the plugin. This can take a while, so go and have a coffee break.

You will find the assembled package in the folder `llrp-commander/target`.

Pitfalls: Dont use the command `mvn compile`. Sometimes this command messes up with the Eclipse dependencies and therefore generates a cryptic error message.

## Eclipse ##

Maven can generate eclipse projects for you from the maven project descriptor.

```
mvn eclipse:eclipse -DM2_REPO="<PATH_TO_MAVEN_REPO>

example Windows:
   mvn eclipse:eclipse -DM2_REPO="~/.m2/repositories"

example *nix:
   mvn eclipse:eclipse -DM2_REPO="c:\Users\example\.m2\repositories"
```

If you omit the directory directive you can always specify the global classpath variable in the eclipse preferences.

Import the generated project in Eclipse project navigator. If you want to start the plugin from within Eclipse, you need to create a symbolic link from `target/lib/` to the folder `lib/` in the `llrp-commander` folder. For Windows a comprehensive guide can be found on [Wikipedia](http://en.wikipedia.org/wiki/NTFS_symbolic_link).

```
example Windows (open command line as administrator):
   cd c:\workspace-fosstrak\llrp\llrp-commander
   mklink /D lib "target\lib"

example *nix:
   cd ~/workspace-fosstrak/llrp/llrp-commander
   ln -sf target/lib .
```

Alternatively, you can copy the whole `lib` folder from the target folder into the folder `llrp-commander`. Note that you will have to update the folder whenever you change the dependencies.

If you want to clean out the Eclipse projects run:

```
mvn eclipse:clean
```

## Site ##

Maven can generate the documentation from the APT files within the `src/site` folder together with a complete documentation of the Java source code (Javadoc).

```
mvn site:stage -DstagingDirectory="<SOME_DIRECTORY>"

example Windows:
  mvn site:stage -DstagingDirectory="c:\fosstrakSites"
  
example *nix:
   mvn site:stage -DstagingDirectory="/tmp/fosstrakSites"
```

## Folder Layout ##

The LLRP Commander is split into two maven modules. `llrp-adaptor` containing the backend controlling the communication to the LLRP readers and `llrp-commander` implementing the GUI.

<pre>

llrp<br>
LICENSE.txt<br>
pom.xml                             Top-level maven2 project descriptor.<br>
src<br>
main<br>
assembly<br>
src.xml                    Assembly constructor to generate the source archive.<br>
site<br>
apt                           APT-files providing the documentation.<br>
resources                     All resources/images for the website.<br>
site.xml                      Site generation XML.<br>
<br>
llrp-adaptor                        Backend controlling communication to the LLRP readers.<br>
LICENSE.txt<br>
NOTICE.txt<br>
pom.xml<br>
src<br>
assembly<br>
bin-with-dependencies.xml   Assembly constructor to generate an archive containing all the dependencies<br>
changes<br>
changes.xml                 Changes report.<br>
main<br>
java                        Java source code.<br>
resources                   Resources folder (example: Properties files, runtime configurations, ...)<br>
site                           Sub-site generation.<br>
test                           JUnit tests.<br>
<br>
llrp-commander                       GUI frontend.<br>
build.properties<br>
LICENSE.txt<br>
NOTICE.txt<br>
plugin.xml<br>
pom.xml<br>
readerDefaultConfig.properties    Default reader configuration file.<br>
Definitions                       LLRP XSD schema.<br>
features                          Eclipse feature project.<br>
icons<br>
META-INF                          Folder providing the MANIFEST for the eclipse plugin.<br>
sampleXML                         Sample llrp messages.<br>
src<br>
changes                        Changes report.<br>
main<br>
assembly<br>
bin.xml                   Eclipse plugin assembly constructor.<br>
rcp.xml                   Eclipse RCP assembly constructor.<br>
eclipseconfig               Configuration files for the RCP standalone build.<br>
java                        Java source code.<br>
resources                   Resources folder (example: Properties files, runtime configurations, ...)<br>
site                           Sub-site generation.<br>
test                           JUnit tests.<br>
update                            Eclipse update site project.<br>
</pre>