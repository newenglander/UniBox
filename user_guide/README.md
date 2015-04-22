# User Guide

## Preparing

The UserBoxClient is providing a User API.

To get in touch with the service and to play your JavaFX game against another remote player you first have to get valid credentials by your administrator. After that you can login into the backend dashboard to change your default password.

The Dashboard is displaying all available game channels and the current result statistics. Furhtermore you can create new game channels and chat with other remote players via the chat tab.

NOTE: The backend is fully responsible, so you can even use it with your smartphone or tablet as a simple but functional chat system.

## Getting started with your JavaFX Application

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

```
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

## Sending messages

Now you should be able to send different Types of Messages. Basically you just need
the sendGameMessage() method to send your game states, changes or events.

```
ClientProvider.sendChatMessage(<STRING>);
ClientProvider.sendCustomMessage(<STRING>);
ClientProvider.sendErrorMessage(<STRING>);
ClientProvider.sendGameMessage(<STRING>);
ClientProvider.sendSystemMessage(<STRING>);
```

NOTE: You got to implement your own protocol. UniBoxClient will not parse or cast any message content.

## Receiving messages

To receive messages send by the backend you got to bind a custom handler to your main stage.
The API is providing an abstract class that implements a EventHandler for incoming messages.
To get it working just copy paste this code to your derived JavaFX Application Class.

```
/**
 * UniBoxClient: bind event handler for incoming messages.
 */
ClientProvider.bind(primaryStage, new IncomingMessageHandler() {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.unibox.client.api.IncomingMessageHandler#handle(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void handle(final String user, final String msg) {

		// handle incoming messages here on <MainThread>

	}
});
```

All incoming messages will be published within the handle() method, while "user" contains the
sender name and "msg" the concrete payload e.g. the message string.

## Handle highscores

Last but not least you are able to manipulate the highscore list.

NOTE: You are not able to commit a result without being part of a game (you must join a game before).

```
ClientProvider.reportWinResult(); // +1 point
ClientProvider.reportDrawResult(); // +/- 0 points
ClientProvider.reportLoseResult(); // -1 point
```

ClientProvider.sendGameMessage(serializedObject);
```
