# Configuration

UniBox is providing two properties files to provide a simple way of configuration. You can find the relevant config files here:

```
%CATALINA_HOME%\webapps\UniBox\WEB-INF\config
```

## Database Configuration

```
\server.properties
```

To connect the UniBoxServer with your MySQL Database, you got to configure these parameters depending on your MySQL Setup:

```
!!! Database Configuration !!!

# Server URL/IP
DB_SERVER=localhost

# Database Name
DB_NAME=unibox

# Driver and Protocol
DB_DRIVER=com.mysql.jdbc.Driver
DB_PROTOCOL=jdbc:mysql:

# Database Credentials
DB_USER=root
DB_PASSWORD=root
```

Just fill up with valid data and restart/start your tomcat instance.

## Logging Configuration

There are several options to improve logging for the server application.

```
%CATALINA_HOME%\logs\UniBox.log
```

## Process Logging Configuration

```
\log4j.properties
```

You got several options to configure the Log4j Listener:

```
log4j.logger.UniBoxLogger=DEBUG, C, fileappender

log4j.additivity.UniBoxLogger=false
log4j.appender.C=org.apache.log4j.ConsoleAppender
log4j.appender.C.layout=org.apache.log4j.PatternLayout
log4j.appender.C.layout.ConversionPattern=[%c] [%d{dd MMM yyyy - hh:mm:ss}] %5p - %m %n

log4j.appender.fileappender=org.apache.log4j.RollingFileAppender
log4j.appender.fileappender.File=${catalina.base}/logs/UniBox.log
log4j.appender.fileappender.MaxFileSize=500KB

log4j.appender.fileappender.MaxBackupIndex=3
log4j.appender.fileappender.layout=org.apache.log4j.PatternLayout
log4j.appender.fileappender.layout.ConversionPattern=%p %t %c - %m%n
```

UniBoxServer is using several [logging levels](https://logging.apache.org/log4j/2.x/manual/customloglevels.html):

```
DEBUG
WARN
ERROR
INFO
```

Just edit the **%LEVEL%** entry in the configuration to get filtered/non-filtered loggings.

```
log4j.logger.UniBoxLogger=%LEVEL%, C, fileappender
```

[Visit the Manual to get more information](http://logging.apache.org/log4j/2.x/manual/index.html)

## Logging Level Configuration
```
\server.properties
```

To get a process based logging for e.g. debugging issues, you can activate logging for a special kind of functionality.

Maybe you just want to get a logging for all methods/actions aiming at authorization issues. Then you cam simply e.g. set LOG_AUTHENTIFICATION to true. After restarting/starting the tomcat server you will get a more consistent logging related to this topic.

```
!!! Logging Configuration !!!

# Log Handling/Process of Async Sessions
LOG_ASYNC_SESSIONS=true

# Log Authentification Process
LOG_AUTHENTIFICATION=true

# Log Communication Process
LOG_COMMUNICATION=true

# Log Database Events
LOG_DATABASE=true

# Log Game relevant Tasks
LOG_GAMEPOOL=true

# Log Request Headers for each Request
LOG_REQUEST_HEADER=true

# Log Requested URI´s and statistic values (e.g. reponse time)
LOG_REQUESTED_URI=true

# Log Thread Processes
LOG_THREADS=true
```
