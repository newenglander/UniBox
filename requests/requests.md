# Requests

```
Root:        http://server:8080/UniBox
```
All requests are defined beyond the static route of your tomcat instance. Basically we differ between **public**, **registered** and **administrative** routes. Public routes can be called by any client. Registered routes require a session and a bound user object which is attached during the authentication process and accessible through a valid cookie generated after confirmed credentials.



