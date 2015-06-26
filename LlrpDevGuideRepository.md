# Developer Guide: Repository #



## Overview ##

This guide will give a short overview to design and implementation of the LLRP message repository. The message repository provides two different datastructures:
  * `Repository` storing and exporting all incomming and sent LLRP messages in full together with control structure (when the message was received, from which reader and adapter, etc.). Stored LLRP messages can be reconstructed completely from this datastructure.
  * `ROAccessReportsRepository` saving `RO_ACCESS_REPORT` LLRP messages in a serialized form. This repository gives a more convenient way, how to access the message content of LLRP messages with tag reports.

Note: The `ROAccessReportsRepository` is optional.

## Design and Implementation ##

The `Repository` and `ROAccessReportsRepository` both implement a strategy pattern allowing to exchange the underlying repository implementation transparently. At runtime, the LLRP Commander automatically invokes the "concrete strategy" depending on the configuration specified by the user.

![http://fosstrak.googlecode.com/svn/wikires/llrp/repositoryDesign.png](http://fosstrak.googlecode.com/svn/wikires/llrp/repositoryDesign.png)

### Strategy Pattern for `Repository` ###

`ResourceCenter` represents the context and Repository models the strategy.

Currently the LLRP Commander provides three different concrete strategies, namely the default repository implemented by a file-based Java Derby repository (`org.fosstrak.llrp.client.repository.sql.DerbyRepository`), the external PostgreSQL repository (`org.fosstrak.llrp.client.repository.sql.PostgreSQLRepository`) and the external MySQL database (`org.fosstrak.llrp.client.repository.sql.MySQLRepository`).

### Strategy Pattern for `ROAccessReportsRepository` ###

`Repository` represents the context and `ROAccessReportsRepository` models the strategy.

Currently the LLRP Commander uses only one concrete strategy for the two contexts Derby/MySQL implemented by `org.fosstrak.llrp.client.repository.sql.DerbyROAccessReportsRepository`.

Note: `ROAccessReportsRepository` is not accessible without `Repository`.

### Access with Java ###

Users, that like to access the Repository from within the LLRP Commander, can use the `ResourceCenter` for that purpose:

```
// how to get a handle to the Repository
Repository repository = ResourceCenter.getInstance().getRepository();
```

Access to the `ROAccessReportsRepository` can then be gained via the `Repository`:

```
// how to get a handle on the ROAccessReportsRepository:

// first get a handle of the repository:
Repository repository = ResourceCenter.getInstance().getRepository();

// now obtain a handle of the ROAccessReportsRepository
ROAccessReportsRepository roAccessReportsRepo = repository.getROAccessRepository();
```

## Derby as Backend ##

By default, the LLRP Commander uses the derby interface as its primary logging backend (zero configuration). In the Eclipse preferences page select the option "Use internal standalone Derby Database" (should be checked by default).

## MySQL as Backend ##

In order to log the LLRP messages to an external MySQL database, the LLRP Commander needs access to a database of the MySQL server. Please be aware that MySQL currently is not able to log fractional time-parts (milliseconds and smaller). So, if you plan to inspect the timestamp on such a fine grain level, you should probably consider using the PostgreSQL database.

We assume that you have MySQL installed and running on your system.

Log into the MySQL command line client as root and perform the following steps:

  1. Create the database (preferably use "llrpMsgDB" as the name).
```
mysql> CREATE DATABASE llrpMsgDB;
```
  1. Create a user that is allowed to access the database. We use username "llrp" and password "llrp".
```
mysql> GRANT CREATE,SELECT,INSERT,UPDATE,DELETE,DROP ON llrpMsgDB.* TO llrp IDENTIFIED BY 'llrp';
```
  1. Update the settings in the eclipse preferences page:
    1. Ensure to uncheck "Use internal standalone Derby Database".
    1. Specify the DB implementor
```
For the Fosstrak MySQL-connector this equals to:
org.fosstrak.llrp.client.repository.sql.MySQLRepository
```
    1. Set the JDBC Connect URL.
```
Syntax: jdbc:mysql://<HOSTNAME_OR_IP>:<PORT>/<DATABASE>

If you set the database to llrpMsgDB and the MySQL server runs on the localhost, put:
jdbc:mysql://localhost:3306/llrpMsgDB
```
    1. Set the username to "llrp" and the password to "llrp".
    1. Click on the button "Test". If no error occurs, the "Switch" button is enabled and you can switch your backend at runtime.
    1. Although the LLRP Commander supports on-the-fly exchange of the repository, we recommend you to restart eclipse in order to load the new configuration from scratch.

## PostgreSQL as Backend ##

In order to log the LLRP messages to an external PostgreSQL database, the LLRP Commander needs access to a database of the PostgreSQL server.

  1. Open the PostgreSQL admin tool (pgAdmin) and connect to your server. On nix you can alternatively use the command-line to create the database and the roles.
    1. Create a new _Login Role_ with role name "llrp" and password "llrp". In "Role Privileges" check "Can create database objects" (see appendix).
```
Command-line command to create the role in *nix
> sudo -u postgres createuser -D -A -P llrp
```
    1. Create the database (preferably use "llrpmsgdb" as the name) and set the owner to "llrp".
```
Command-line command to create the database in *nix
> sudo -u postgres createdb -O llrp llrpmsgdb
```
  1. Update the settings in the eclipse preferences page:
    1. Ensure to uncheck "Use internal standalone Derby Database".
    1. Specify the DB implementor
```
For the Fosstrak PostgreSQL-connector this equals to:
org.fosstrak.llrp.client.repository.sql.PostgreSQLRepository
```
    1. Set the JDBC Connect URL.
```
Syntax: jdbc:postgresql://<HOSTNAME_OR_IP>:<PORT>/<DATABASE>

If you set the database to llrpmsgdb and the PostgreSQL server runs on the localhost, put:
jdbc:postgresql://localhost:5432/llrpmsgdb
```
    1. Set the username to "llrp" and the password to "llrp".
    1. Click on the button "Test". If no error occurs, the "Switch" button is enabled and you can switch your backend at runtime.
    1. Although the LLRP Commander supports on-the-fly exchange of the repository, we recommend you to restart Eclipse in order to load the new configuration from scratch.

## Connect to the Database with Matlab ##

Make sure to configure the LLRP Commander to use either the MySQL or the PostgreSQL database as backend.

Connect from Matlab:

  1. You need to add the MySQL or PostgreSQL JDBC-Connector to the Java classpath of Matlab before you can establish a connection to the SQL server.
    1. Either obtain a copy of the MySQL JDBC-Connector ([download](http://dev.mysql.com/downloads/connector/j/5.1.html)) or PostgreSQL ([download](http://jdbc.postgresql.org/download.html) - please select JDBC-4 8.3.x). Store the file to a location of your choice.
    1. Add a reference in the Matlab Java `classpath.txt`. You can find the file in the folder `matlabroot\toolbox\local\classpath.txt`.
```
file: matlabroot\toolbox\local\classpath.txt
Example: add reference to the JDBC Connector
...
c:/sqlDrivers/mysql-connector-java-5.1.6.jar
c:/sqlDrivers/postgresql-8.3-603.jdbc4.jar
```
    1. Restart Matlab
  1. Establish a connection to the database:
```
Example MySQL:
>> conn = database('llrpMsgDB', 'llrp', 'llrp', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/llrpMsgDB');

Example PostgreSQL:
>> conn = database('llrpmsgdb', 'llrp', 'llrp', 'org.postgresql.Driver', 'jdbc:postgresql://localhost:3306/llrpmsgdb');
```
    1. Example how to fetch all the entries already logged into the database:
```
>> result = get(fetch(exec(conn, 'SELECT * FROM table_ro_access_reports')), 'Data');
```
    1. Example how to fetch all the EPCs:
```
>> result = get(fetch(exec(conn, 'SELECT count(EPC) FROM table_ro_access_reports')), 'Data');
```
    1. Example how to display the results:
```
>> disp(result)
```

## Appendix ##

User settings for PostgreSQL:

![http://fosstrak.googlecode.com/svn/wikires/llrp/developerPostgreUser.png](http://fosstrak.googlecode.com/svn/wikires/llrp/developerPostgreUser.png)