# Concept

The UniBox Server is implementing different type of messages.
```java
package de.unibox.core.network.object;

/**
 * The Enum MessageType.
 */
public enum CommunicatorMessageType {

    /** The chat. */
    CHAT,

    /** The error. */
    ERROR,

    /** The game. */
    GAME,

    /** The js command. */
    JS_COMMAND,

    /** The ping. */
    PING,

    /** The system. */
    SYSTEM,
    
    /** The plain. */
    PLAIN
}
```
Each message type has it´s own prediction. The following routes are able to deliver different kinds of messages around the registered clients. Please read carefully if you want to deliver messages beside the default messaging functions derived by the Client API or overwrite the default functionality of the frontend javascript app.


