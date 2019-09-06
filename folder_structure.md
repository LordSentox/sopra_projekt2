### Ordnerstruktur im Data-Ordner

```
data
  +--- maps
  |
  +--- save_games
  |
  +--- replays
  |
  +--- high_scores
  |       |
  |       +--- scores
  |       +--- replays
  settings
```

##### maps
Enthält die vom MapEditor gespeicherten und ladbaren Dateien. Sie enthalten nur Informationen darüber, wo sich Land und
wo sich Wasser befindet. Der Ordner enthält Dateien mit Namen in der Form
```data/maps/<mapName>.map```
Es handelt sich dabei um CSV-Dateien, welche für jedes Stück Land auf der Karte eine 0 enthalten, während sie für Wasser
einen - enthalten. Eine 2x2 quadratische Insel umgeben von Wasser sieht demnach in der Datei wie folgt aus:
```
-;-;-;-
-;0;0;-
-;0;0;-
-;-;-;-
```
Der Ordnerpfad soll im Code nicht als String hartgecoded werden, sondern kann mit
```java
MapController.MAP_FOLDER
```
benutzt werden.

##### save_games
Enthält die gespeicherten Spiele, welche allerdings noch nicht zuende gespielt wurden. Ihre Namen sind in der Form
```data/save_games/<game_name>.save```
Es handelt sich dabei um ein serialisiertes JavaGame, wobei der Turn-Pointer auf die zuletzt durchgeführte Aktion zeigen
sollte.
Der Ordnerpfad soll im Code nicht als String hartgecoded werden, sondern kann mit
```java
ControllerChan.SAVE_GAME_FOLDER
```
benutzt werden.

##### replays
Enthält zuendegespielte Spiele, deren Replays der Nutzer gespeichert hat. Die Dateinamen sind in der Form
```data/replays/<replayName>.replay```
Auch hierbei handelt es sich um ein serialisiertes JavaGame, wobei allerdings der Turn-Pointer statt wie bei save_game
auf den zuletzt getätigten, auf den allerersten (den initialisierenden) Zug zeigen sollte.
Der Ordnerpfad soll im Code nicht als String hartgecoded werden, sondern kann mit
```java
ControllerChan.REPLAY_FOLDER
```
benutzt werden.

##### high_scores/scores
Enthält die Bestenlisten der einzelnen Maps. Für jede Karte wird eine neue Datei angelegt. Die Dateien haben die Form
```data/high_scores/scores/<mapName>.score```
Die Dateien sind im CSV-Format, wobei jede Zeile einen HighScore-Eintrag beschreiben soll. Übersichtshalber sollten
sie in absteigender Reihenfolge nach Punkten in der Datei stehen.
```
<name>;<score>;<replayName>
<name2>;<score2>;<replayName2>
```
Der Ordnerpfad soll im Code nicht als String hartgecoded werden, sondern kann mit
```java
HighScoresController.SCORE_FOLDER
```
benutzt werden.

##### high_scores/replays
Enthält die Replays der in der HighScore-Liste gespeicherten Einträge. Sie haben das gleiche Format wie auch die normalen
Replays, lediglich durch die Dateiposition können sie unterschieden werden. Die Dateien haben den Namen
```data/high_scores/replays/<mapName>/<replayName>.replay```
Sie liegen also nochmal in Unterordnern, die dem MapNamen zugeordnet werden. replayName muss nicht für Menschen besonders
gut lesbar oder merkbar sein, sondern soll vor allem den Anspruch haben, einmalig zu sein. Ein guter Ansatz ist z.B. die
Reihenfolge in der Score-Datei für Index-Namen zu verwenden. So wird auch verhindert, dass alte Replays zwar nicht mehr
aufgerufen werden können, aber weiterhin im Dateisystem verbleiben.

Der Ordnerpfad soll im Code nicht als String hartgecoded werden, sondern kann mit
```java
HighScoresController.REPLAY_FOLDER
```
benutzt werden.

##### settings
Die Settings-Datei enthält die Einstellungen, die beim letzten Spielstart festgelegt wurden. Sie hat den Dateinamen
```data/settings```
und enthält durch Gleichheitszeichen getrennte Zeilen für die Einstellungen, welche einer Zuweisung in Java gleichen.
Beispielsweise:
```
game_volume = 34
music_volume = 10
```

Die Datei hat eine globale File in Controller-Chan, welche durch
```java
ControllerChan.SETTINGS_FILE
```
benutzt werden kann.
