# Introduction #

This page illustrates how maven articfacts are deployed.

## Deploy new maven artifacts ##

  * Commit all local changes to svn (make sure you use the new google code svn)
  * Adjust your mvn settings.xml to include sonatype nexus repository credentials
  * Install gnupgp if it is not already installed
  * Make sure description, developer info and scm info are listed in the pom - otherwise, it will be rejected during the sonatype release process
  * Make sure the following parent is listed in the pom:
```
<parent>
    <groupId>org.sonatype.oss</groupId>
    <artifactId>oss-parent</artifactId>
    <version>7</version>
 </parent>
```

  * run the following mvn commands:

```
$ mvn release:clean
$ mvn release:prepare
$ mvn release:perform
```

  * log into sonatype nexus oss repo at https://oss.sonatype.org/index.html#stagingRepositories
    * search for fosstrak
    * browse and inspect files uploaded
    * click "close" button
    * click "release" button (with a small delay the artifacts are now available from the central repository)

## Deploy existing artifacts ##

  * Adjust poms to list description, developer info and scm urls (otherwise sonatype nexus repo will complain during release process)
  * Install [gnupgp](http://www.gnupg.org/) if it is not already installed
  * Adjust your mvn settings.xml to include sonatype nexus repository credentials
  * run mvn gpg:sign-and-deploy-file for the existing artifacts (see example below)
  * log into sonatype nexus oss repo at https://oss.sonatype.org/index.html#stagingRepositories
  * search for fosstrak
  * browse and inspect files uploaded
  * click "close" button
  * click "release" button (with a small delay the artifacts are now available from the central repository)

```
mvn gpg:sign-and-deploy-file 
-Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ 
-DrepositoryId=sonatype-nexus-staging -DpomFile=hal-impl-sim-0.5.0.pom 
-Dfile=hal-impl-sim-0.5.0.jar 
-Dfiles=hal-impl-sim-0.5.0-bin-with-dependencies.zip,hal-impl-sim-0.5.0-javadoc.jar,hal-impl-sim-0.5.0-sources.jar 
-Dclassifiers=bin-with-dependencies,javadoc,sources 
-Dtypes=zip,jar,jar
```
More info at:

https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide