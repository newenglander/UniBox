# UniBox

UniBox is an simple and easy to use messaging system implementing custom "games" for distributed gaming clients.

Read full Manual here: [UniBoxManual](alextape.github.io/UniBox/UniBoxManual.pdf)

## Introduction

The UniBox backend is connecting various gaming clients via http based network connections. Basically it consists of a Servlet Stack linked to a MySQL database (to store user names, passwords, rankings), an easy to use client API and a core library, to cover cross-package dependencies.

It is designed as a asynchron message mediator. Messages can be injected by simple http requests against the backend services. The backend will queue injected messages and the corresponding thread workers will deliver and receive the messages via asynchron long polling http responses based on a [comet-pattern](http://en.wikipedia.org/wiki/Comet_%28programming%29) immediatly.

In fact each concrete game represents a screened messaging channel in principle. Besides this functionality of sending messages between a several group of connected clients, the frontend priovides a simple chat box to get in bound with other player directly.

The [Wiki Page](https://github.com/AlexTape/UniBox/wiki) of this project contains all available information about the usage and the functionality of this project.

### Package Overview
| Package      | Description |JavaDoc           | Builds  |
|:-------------|:------------|:-------------:|:-----:|
|UniBoxServer| Servlet-based |[JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxServer/)|[WAR](http://alextape.github.io/UniBox/builds/UniBoxServer.war)|
|UniBoxCore| Shared dependencies for Server and Client API|[JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxCore/)|[JAR](http://alextape.github.io/UniBox/builds/UniBoxCore.jar)|
|UniBoxClient | Client API |[JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxClient/)|[JAR](http://alextape.github.io/UniBox/builds/UniBoxClient.jar)|
|UniBoxGame|Simple JavaFX Application Demo|[JavaDoc](http://alextape.github.io/UniBox/JavaDoc/UniBoxGame/)|[JAR](http://alextape.github.io/UniBox/builds/UniBoxGame.jar)|
