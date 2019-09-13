# Projekt 2 im Sopra 19B

In diesem Repository entsteht euer zweites Projekt im Sopra. Für Planung, Modellierung und generelle gruppeninterne Angelegenheiten, verwendet das Wiki des Projektes. 

### Bezeichnungen von Runden, Aktionen und Zügen
Um im Code mit den Bezeichnungen nicht durcheinanderzukommen werden ab heute Abend die folgenden einheitlichen Bezeichnungen durchgesetzt:
- Aktion (Action) Eine atomare Aktion, die ein Spieler während des Spiels an der richtigen Stelle tätigen kann, bzw. die das Spiel an einer bestimmten Stelle tätigt. Sie sind die kleinste Einheit, die rückgängig gemacht oder wiederholt werden kann
- Zug (Turn) besteht aus allen Aktionen, die ein Spieler tätigen kann oder getätigt hat, bis er seinen Zug an den nächsten Spieler abgibt. Damit ist die Aktionszahl während eines Zuges variabel
- Runde (Round) ist dann abgeschlossen, wenn vom Anfangsspieler (Spieler 0) gesehen alle Spieler einmal an der Reihe waren und Spieler 0 jetzt wieder anfängt. Sie besteht also aus einem Zug für jeden Spieler



## PMD-Bericht

Wenn programmiert wird, vergesst nicht, regelmäßig den PMD-Bericht hier oder direkt in Eclipse zu überprüfen.

[PMD-Bericht](https://sopra.cs.tu-dortmund.de/bin/pmd-experimental.py?XXY=19B&GROUPNUMBER=4&PROJECT=2)

