# Hints and Bugs

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

## Bugs
##Known Bugs:
###Internet Explorer 11
1. The Chat-Function is not working until the app is recieving the second incoming message.


