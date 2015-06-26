#summary One-sentence summary of this page.

# Introduction #

This page explains how to build the Fosstrak middleware module from source


# Details #

Preconditions:
  * Maven 2.2.+ installed
  * subversion installed
  * java runtime installed

Steps:
  * svn checkout https://fosstrak.googlecode.com/svn/fc/trunk
  * download the following dependencies , since they are currently not available from any maven repository:
    1. javax.comm http://llk.media.mit.edu/projects/picdev/software/javaxcomm.zip
    1. eclipse webserviceUtils (needed only by fc-webclient) https://www.dropbox.com/s/i62pg37xe8s1y2f/webserviceutils.zip
```
<groupId>org.eclipse.jst.ws.consumption</groupId>
    <artifactId>webserviceutils</artifactId>
    <version>1.0.102.v200609220223</version> 
```
  * Install the dependencies locally:
```
mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> \
    -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>
```
  * mvn package