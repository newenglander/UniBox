

# Introduction

## Why UniBox?

The UniBox project connects various gaming clients via network. Basically it consists of a Servlet-Server linked to a mysql database (storing user data, rankings), an easy to user client API and a core library, to cover cross-package dependencies.

To be prepared for any kind of firewall configurations within the network or the serving system the communication is completly processable over one single port. By default it would be the tomcat destination port e.g. 8080. But you can simply forward your default apache listener (port 80) to your tomcat instance.

The communication architecture is basically a comet pattern. Any client, regardless of which kind, can call a certain listener route and will receive a long polling http handle on which all incoming messages transfered instantly. If a client needs to send a message, it will call a parameterized (static) url to inject a new message into the communication lifecycle of the backend. The backend is able to switch messages directly to related receivers and will deliver the messages via the long polling comet routes.

## UniBox Server

The Server is providing a web-frontend to get a quick overview of the current running game sessions. Furthermore there is a chat functionality which is mostly intended to permit a communication between online/registered players before they joined an enclosed game channel.

## UniBox Client (API)




## UniBox Core

## UniBox Game (Demo)

# Installation

## Setup

## Deployment

## Running

## Mainentance






