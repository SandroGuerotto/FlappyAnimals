# Bewertung Projekt 2: Team 2 - Bananas

## 🥇 Die Note ist: 5.8 (19.14 Punkte)

👫 Landrit Ahmeti (ahmetlan); Sandro Guerotto (guerosan); Safiyya Waldburger (waldbsaf); David Gerber (gerbeda3); 

https://github.zhaw.ch/PM2-IT20taZH-bles-mach-zubj/hk2-gruppe-02-bananas-projekt2-flappyanimals


## Allgemeine Anforderungen (all-or-nothing) 

> Voraussetzung für Punkterteilung: Die Applikation ist mit gradle run lauffähig. 

* [x] Applikation kann mit gradle korrekt gestartet werden.

# Projekt und Architektur 


## Projekt 
👨‍🏫  Proj: 4: (0-4): 3.7

> - Das Branching-Modell wurde definiert und korrekt eingehalten.
> - Pull Requests für Review nachvollziehbar eingesetzt
> - Alle Gruppenmitglieder haben Code beigetragen und auf GitHub eingecheckt.
> - GitHub Projekt zur Verwaltung eingesetzt und Github-Projekt hat mind. 3 Spalten. Todo, Doing, Done
> - "Beiträge (Features, Bugs, Verbesserungen) und Entscheidungen wurden über Github-Issues 
    verwaltet und dokumentiert."
> - Die Projektstruktur ist gemäss Vorgabe (src/main/java und src/main/test) 
    und zusätzliche Dateien (Ressourcen) am richtigen Ort

Folgendes habe ich festgestellt:
* Das Branching-Modell wurde definiert (Developer-Feature-Documentation-Branch-Modell) und eingehalten.
* Pull-Requests werden fürs Merging verwendet.
* Titel sind sinnvoll, gelegentlich etwas kurz
* Die Beschreibung der Pull-Requests ist oft leer. Manchmal sind sehr kurze Beschreibungen enthalten. 
  Hier sollte der Grund für den Merge erläutert werden. Zumindest sollte eine Referenz auf die enthaltenen Issues vorhanden sein
  (gelegentlich vorhanden z.B. #24). Issues sind häufig in den Commits enthalten.
* Reviews wurden relativ konsequent durchgeführt.
  Meist wir kurz bestätigt. Selten kritisiert.

* GitHub-Project wurde verwendet.
* Issues wurden für die Planung verwendet.  
  Die Beschreibung vorhanden und oft wird eine standardisierte Struktur verwendet (nicht immer).  
  Diskussionen/Kommentare fanden meist statt.  
  Issues wurden auch immer zugewiesen.  
  Labels wurde verwendet  
* Sehr viele Commits wurden mit Issues verknüpft.   

* Alle Teammitglieder haben beigetragen.
```
Inserts and Deletes in the commits for *.java *.fxml *.md *.adoc *.properties files

      Author (commits)  Insert   Del
   Sandro Guerotto (47):  4676  2033
          waldbsaf (36):  1114   357
          ahmetlan (25):  1017   224
          gerbeda3 (17):   474   453
```
* Auf den ersten Blick sieht es aus als hätte Herr Gerber weniger beigetragen (Commits/Zeilen). 
  Bei genauerer Untersuchung der Commits zeigt es sich, dass die Beiträge zumeist Dokumentation bzw. Javadoc und 
  kleinere Bereinigungen sind. Gibt es einen Grund? Individuelle Note -0.3. Andere +0.1
  



## Architektur 
👨‍🏫  Arch: 4: (0-4): 3.6

> - Architekturdokumentation ist vorhanden, vollständig und konsistent
> - Sie haben eine sinnvolle Aufteilung in Klassen gefunden.
> - Model wurde geeignet isoliert (MVC / Interfaces ...)
> - Das Klassendiagramm stimmt mit dem Code überein.

Folgendes habe ich festgestellt:
* Erläuterung der Architektur ist gut, konsistent und relativ ausführlich.
  Eine Art Bedienungsanleitung und Modeldefinition ist vorhanden.  
  Zu weiter Bemerkungen:
  * Verwendung von create-Methoden -> Factory-Methode (Inwiefern ist das nicht via Konstruktor möglich zu testen?)
  * Testen von Methoden geht auch mit package-private, da Testmethoden im gleichen Package.
* MVC ist gut umgesetzt
* Klassendiagramm ist vorhanden
  * Die einzelnen Klassen sind vom Detaillierungsgrad gut. Abstraktionslevel passt.
  * Vor allem "owns"-Beziehungen, wenig "uses".
  * Extends Beziehung zwischen GameViewController und obstacleSpawner ist im Code nicht vorhanden.
  * Raute bei GameEngine ist auf der falschen Seite, da das Game die GameEngine owned. Dito Game zu ObstacleSpawner.
  * Bei Enums müssen die Methoden values() und valueOf() nicht angegeben werden.
  * Exceptions können weggelassen werden.
* Aufteilung in Packages ist sinnvoll gewählt



# Software 

## Funktionalität 
👨‍🏫  SWGrund: 4: (0-4): 4

> - Das Programm besitzt die geplante bzw. mit dem Betreuer / der Betreuerin abgesprochene Funktionalität.
> - Ist als Prototyp einsetzbar, das Team hat sich engagiert eingebracht
> - GUI-Funktionalität und Fehlermeldungen

Folgendes habe ich festgestellt:
* Die geforderte Funktionalität ist vorhanden.
  * Spiel funktioniert gut. Gar nicht so einfach.
  * Mehrere Spielfiguren, die im Shop erstanden werden können.
  * Shop braucht mehr Artikel :-)
* Funktioniert als Prototyp.
  * Bedienung erfolgt flüssig und logisch.
* GUI Funktion ist gut 
  * Fehlermeldungen würden auf Konsole ausgegeben.


## CleanCode L1 
👨‍🏫  CcL1: 2: (0-10): 9.7

> - Namenskonventionen eingehalten
> - Namen sind sinnvoll gewählt
> - Code ist aufgeräumt (Reihenfolge, Leerzeilen, Einrücken)
> - Code ist übersichtlich gehalten (Struktur)
> - Methodengestaltung Länge
> - Klassen haben Verhalten (L1)
> - Ihre Klassen stehen für sich alleine, Verantwortlichkeiten auch in Methoden sinnvoll
> - JavaDoc vorhanden, Code ist sauber dokumentiert
> - Sie haben keine Public-Methoden, die nicht auch private sein könnten
> - Argumente werden überprüft (im Model - Konstruktoren und public Methoden)

Folgendes habe ich festgestellt:  
* Namenskonventionen sind im Wesentlichen eingehalten. 
  Nur wenige Methoden & Variablen könnten bessere Namen erhalten
  * GameViewController.running() (Verb)
* Code ist aufgeräumt und übersichtlich.  
  * Teilweise überflüssige Leerzeilen (nur wenige)
* Länge der Methoden ist akzeptabel.  
* *Verwendet Sprachfeatures, welche in Java 11 nicht enthalten sind (switch-expressions)*
* JavaDoc ist bei allen public-Methoden vorhanden (auch viele private)
* Klassenoberfläche ist ok. Könnte etwas kleiner sein (dokumentiert).
  * Methoden sollten erst public werden, wenn es nicht anders geht (refactoring).
* Argumente werden nicht überprüft (ausser in generierten equals-Methoden).  

## CleanCode L2 
👨‍🏫  CcL2: 2: (0-2): 2

> - Ausnahmen werden behandelt (L2)
> - Ressourcen werden geschlossen / try-with-resource wird eingesetzt
> - git commits/messages useful (issues referencing, gute messages, viele commits)

Folgendes habe ich festgestellt:  
* Ausnahmen werden verwendet und behandelt.
* try-with-resource wird verwendet. Files werden geschlossen
* Commit-Meldungen sind ok.

## CleanCode L3 
👨‍🏫  CcL3: 1: (0-1): 0.9

> - Lange Parameterlisten durch Builder oder Argumentobjekt ersetzen (L3)
> - Einsatz von lokalen Variablen vs. Datenfelder sinnvoll
> - Logging anstelle von System.out (L3)

Folgendes habe ich festgestellt:  
* Parameterlisten im Rahmen.
* Einsatz von Variablen/Felder ok
* Kein Logger, keine Meldungen auf Konsole mittels System.out, ausser Exceptions




# Testing


## Unit Tests
👨‍🏫  UnitTest: 2: (0-2): 2

> - Die Tests decken die Domänenlogik ab (Coverage)
> - Negativtests sind vorhanden
> - Testfälle sind sinnvoll gewählt
> - Die Tests sind gut dokumentiert

Folgendes habe ich festgestellt: 
* Tests können gestartet werden.
* Sehr ausgiebige Tests
* Businesslogik ist weitgehend abgedeckt, inklusive einige Negativtests
* Testfälle sind sinnvoll
* Tests müssen nicht unbedingt mit test... beginnen.


## Mock Tests
👨‍🏫  MockTest: 1: (0-1): 1

> - Mockito wurde verwendet
> - Wurde Mockito intensiv verwendet

Folgendes habe ich festgestellt:  
* Mockito wird sehr extensiv verwendet. 
* Inklusive einiger verify

