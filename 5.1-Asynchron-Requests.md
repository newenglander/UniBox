# Asynchron Requests
## Plain Comet Listener
```
Type:           GET
URL:            /Communicator
Parameter:      -
Output:         text/html
Realm:          public
```
This request delivers a long polling http response to receive **plain** messages.

## JavaScript Comet Listener
```
Type:           GET
URL:            /Communicator/JavaScript
Parameter:      -
Output:         text/html
Realm:          public
```
This request delivers a long polling http response to receive **javascript** messages. **GAME Message will not delivered via this route**.

## Serial Comet Listener
```
Type:           GET
URL:            /Communicator/Serial
Parameter:      -
Output:         text/html
Realm:          registered
```
This request delivers a long polling http response to receive **serialized object** messages. **CHAT Messages will not delivered via this route**.
