# Receive Messages

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
