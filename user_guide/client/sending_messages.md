# Sending messages

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
