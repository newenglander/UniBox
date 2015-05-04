# Handle Highscores

Last but not least you are able to manipulate the highscore list.

NOTE: You are not able to commit a result without being part of a game (you must join a game before).

```
ClientProvider.reportWinResult(); // +1 point
ClientProvider.reportDrawResult(); // +/- 0 points
ClientProvider.reportLoseResult(); // -1 point
```
