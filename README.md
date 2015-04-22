# Home

The [Wiki Page](https://github.com/AlexTape/UniBox/wiki) of this project contains all available information about the usage and the functionality of this project.

##Table of Content:

1. Introduction
2. Installation
3. Usage
4. Communication Concept
5. Routing
6. Bugs

## Package Overview

- UniBoxServer (Servlet-based Server) [WAR](http://alextape.github.io/UniBox/builds/UniBoxServer.war), [JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxServer/)
- UniBoxCore (Shared dependencies for Server and Client API)[JAR](http://alextape.github.io/UniBox/builds/UniBoxCore.jar), [JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxCore/)
- UniBoxClient (Client API)[JAR](http://alextape.github.io/UniBox/builds/UniBoxClient.jar), [JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxClient/)
- UniBoxGame (Simple Demo)[JAR](http://alextape.github.io/UniBox/builds/UniBoxGame.jar), [JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxGame/)

# Introduction

## Why UniBox?

The UniBox project connects various gaming clients via network. Basically it consists of a Servlet-Server linked to a mysql database (storing user data, rankings), an easy to user client API and a core library, to cover cross-package dependencies.

## Package Overview

- UniBoxServer (Servlet-based Server)
- UniBoxCore (Shared dependencies for Server and Client API)
- UniBoxClient (Client API)
- UniBoxGame (Simple Demo)

## UniBox Server

The Server is providing a web-frontend to get a quick overview of the current running game sessions. Furthermore there is a chat functionality which is mostly intended to permit a communication between online/registered players before they joined an enclosed game channel.

## UniBox Client API

## UniBox Core

## UniBox Game

# Installation

# User Guide

## Introduction

The UserBoxClient is providing a User API.

To get in touch with the service and to play your JavaFX game against
another remote player you first have to get valid credentials by your
administrator. After that you can login into the backend dashboard to
change your default password.

The Dashboard is displaying all available game channels and the current
result statistics. Furhtermore you can create new game channels and chat
with other remote players via the chat tab.

NOTE: The backend is fully responsible, so you can even use it with your
smartphone or tablet as a simple but functional chat system.

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

### Sending messages

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

### Receiving messages

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

### Handle highscores

Last but not least you are able to manipulate the highscore list.

NOTE: You are not able to commit a result without being part of a game (you must join a game before).

```
ClientProvider.reportWinResult(); // +1 point
ClientProvider.reportDrawResult(); // +/- 0 points
ClientProvider.reportLoseResult(); // -1 point
```

## Hints

The API is providing a implementation of the native object serialization routine:

```
ObjectSerializerImpl.objectToString(final Serializable object);

ObjectSerializerImpl.stringToObject(final String string, final Class<E> clazz);
```

With these methods you are able to send objects directly:

```
AnyObject any = new AnyObject();

String serializedObject = ObjectSerializerImpl.objectToString(any);

ClientProvider.sendGameMessage(serializedObject);
```

Remember that the receiver has to know which object is arriving. So you need the Class of
the serialized object on both sides (sender and receiver).

If the receiver wants to pick the serialized object, just do this:

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

		AnyObject any = ObjectSerializerImpl.stringToObject(msg, AnyClass.class);

		// any is an exact copy of the object send by the sender

	}
});
```

Just to food your thought. You can even capsule objects:

```
ArrayList<AnyObject> list = new ArrayList<AnyObject>();
list.add(obj1);
list.add(obj2);

String serializedObject = ObjectSerializerImpl.objectToString(list);

ClientProvider.sendGameMessage(serializedObject);
```

# Communication Concept

The UniBox Server is implementing different type of messages.
```java
    public enum MessageType {

        /** The chat. */
        CHAT,

        /** The error. */
        ERROR,

        /** The game. */
        GAME,

        /** The system. */
        SYSTEM
    }
```
Each message type has it´s own prediction. The following routes are able to deliver different kinds of messages around the registered clients. Please read carefully if you want to deliver messages beside the default messaging functions derived by the Client API or overwrite the default functionality of the frontend javascript app.

### Asynchron long polling routes

```
Type:        GET
URL:         /Communicator
Parameter:   -
Output:      text/html
```
This request delivers a long polling http response to receive **plain** messages.

```
Type:        GET
URL:         /Communicator/JavaScript
Parameter:   -
Output:      text/html
```
This request delivers a long polling http response to receive **javascript** messages. **GAME Message will not delivered via this route**.

```
Type:        GET
URL:         /Communicator/Serial
Parameter:   -
Output:      text/html
```
This request delivers a long polling http response to receive **serialized object** messages. **CHAT Messages will not delivered via this route**.

### Messaging routes

```
Type:        POST
URL:         /Communicator
Parameter:   action=[connect|post]&message=[STRING]
Output:      text/html
```
This request is able to promote a new user with action=connect as a SYSTEM Message (e.g. User XY joined..). Furthermore this route is used to inject new messages with the action=post parameter. The concrete message content will be specified within the message parameter value. If anything went wrong you will get a 422 Http-State. **PLAIN Messages are currently not forwareded to any client**.

```
Type:        POST
URL:         /Communicator/JavaScript
Parameter:   action=[connect|post]&message=[STRING]
Output:      text/html
```
This request is able to promote a new user with action=connect as a SYSTEM Message (e.g. User XY joined..). Furthermore this route is used to inject new messages with the action=post parameter. The concrete message content will be specified within the message parameter value. If anything went wrong you will get a 422 Http-State. **JavaScript Messages are forwarded as CHAT Messages**.

```
Type:        POST
URL:         /Communicator/Serial
Parameter:   action=[connect|post]&message=[STRING]
Output:      text/html
```
This request is able to promote a new user with action=connect as a SYSTEM Message (e.g. User XY joined..). Furthermore this route is used to inject new messages with the action=post parameter. The concrete message content will be specified within the message parameter value. If anything went wrong you will get a 422 Http-State. **Serial Messages can be forwarded as different types, like CHAT, ERROR, GAME or SYSTEM Messages**.

# Routing


```
Root:        http://server:8080/UniBox
```
All routes are defined beyond the static route of your tomcat instance. Basically we differ between **public** and **protected** routes. Public routes can be called by any client. Protected routes require a session and a user object which are only available if the client was authenticated with a valid cookie generated after confirmed credentials during the login process.

### Public Routes

#### Get Login Screen
```
Type:        GET|POST
URL:         /Login
Parameter:   error=[STRING]
ContentType: text/html
```
This Route is a straight forward to /login.html which is a static page serving the login formular. If anything went wrong due using the webservice you will be redirected to this screen and a error will be attached as a value for the error parameter.

### Protected Routes

#### Get dashboard
```
Type:        GET|POST
URL:         /Dashboard
Parameter:   -
ContentType: text/html
```
This Route is a straight forward to /dashboard.html which is a static page serving the frontend as a javascript webapp.

#### Get game and ranking lists
```
Type:        GET
URL:         /Database
Parameter:   request=[ranking|games]
Output:      application/json
```
This route is serving direct database selections for ranking and game tables/lists.

#### Create new player
```
Type:        POST
URL:         /Database
Parameter:   create=player&name=[STRING]&adminrights=[0|1]
Output:      text/html
```
This route can be used to create a new user. To grant admin privilegs to this user the adminrights parameter can be set to true (1) or false (0). The default password will be 'user'.

#### Create new category
```
Type:        POST
URL:         /Database
Parameter:   create=category&gametitle=[STRING]&numberofplayers=[INTEGER]
Output:      text/html
```
This route can be used to create a new game category. The name of this category can be choosen and the maximal count of players able to play together in one game instance has to be set.

#### Create new game
```
Type:        POST
URL:         /Database
Parameter:   create=game&gamename=[STRING]&catid=[INTEGER]
Output:      text/html
```
This route can be used to create a new game entry. The name of this game can be choosen and the game category e.g. catid has to be set to relate the game to a concrete game category.

#### Create new queue entry
```
Type:        POST
URL:         /Database
Parameter:   create=queue&gamename=[STRING]&catid=[INTEGER]
Output:      text/html
```
*NOT_IMPLEMENTED_ATM*

#### Create new game result
```
Type:        POST
URL:         /Database
Parameter:   create=result&gameid=[INTEGER]&playerid=[[INTEGER]&scoring=[INTEGER]
Output:      text/html
```
This route can be used to create a new result entry for a finished game. To get it done, you have to set the gameid, the corresponding playerid and the scoring, which yill be set in the database.

#### Join and leave game
```
Type:        GET|POST
URL:         /Game
Parameter:   action=[join|leave|whichgame]&gameid=[INTEGER]
Output:      text/html__
```
This route can be used by any client to join or leave a concrete game during runtime, identified by the related game id. If the game is already full or another error appearing you will receive a BAD_REQUEST Http-State and "error" as plain body response. If joining/leaving was successfull you get a "OK" Http-State and a "success" body response. To get to know if you are a participant of any game, you can request the game id of the joined game with 'action=whichgame' request.

#### Logout
```
Type:        GET|POST
URL:         /Logout
Parameter:   -
Output:      text/html
```
This route can be used by any client to invalidate the corresponing session. The callback will be a redirection to the public login formular.

# Bugs

_known_bugs_

Known Bugs:
1. In Internet Explorer 11 the Chat-Function is not working until the app is recieving the third incoming message.
2.



