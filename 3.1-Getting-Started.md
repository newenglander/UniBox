# Getting started with your JavaFX Application

After you set the UniBoxClient.jar as a needed Dependency of your project
your are able to setup your needs.

First of all you got to set your Username and Password togehter with the IP of
the UniBoxServer Instance. To be flexible the API will provide different methods
for that task to fit your needs.

You can pass your credentials as parameter like

```
java -jar YourGame.jar <ip> <user> <password>
```

If you prefer that way, simple add the following calls to your main():

```java
public static void main(final String[] args) {

	// init credentials
	ClientProvider.setIp(args[0]);
	ClientProvider.setUsername(args[1]);
	ClientProvider.setPassword(args[2]);

	Application.launch(args);

}
```

If you like a more static way you can place these methods in your start() method as well:

```
public final void start(final Stage primaryStage) {

	// init credentials

	ClientProvider.setIp("192.168.0.150");
	ClientProvider.setUsername("user");
	ClientProvider.setPassword("password");

	// ...

}
```

As the credentials and the IP were set. You can call the login routine to register your
Application to the backend service.

```
ClientProvider.login();
```

Now you got a valid cookie to talk on an authorized level to the backend workers.
To start the main loop of the Client API you got to connect the local workers/threads
of your Application with the Backend.

```
ClientProvider.connect();
```
