# Demo Projekt zur Funktionalen Programmierung in Kotlin

## Ziel
Das Projekt illustriert durch mehrere Implementierungsvarianten der gleichen Funktionalität
den möglichen Einsatz funktionaler Programmierung in Kotlin mit Hilfe der Bibliothek Arrow 
( https://arrow-kt.io ). Um einen Vergleich zu ermöglichen, wird zuächst eine einfache Funktionalität
in einer üblichen Variante implementiert, ohne funktionale Ansätze mit Spring Boot.

## Anwendungsbeispiel
Als Anwendungsbeispiel dient ein kleiner Stundenrechner mit REST Schnittstelle. Der Stundenrechner verfügt über die 
folgende Funktionalität
* Gib ein, an welchem Tag wie viele Stunden gearbeitet wurden
* Frage ab, für welchen Zeitraum welcher Betrag in Rechnung gestellt wird.

Beispielhafte REST Anfragen sind in Datei [rest.http](http/rest.http) zu finden

Das Beispiel versucht einerseits, Aspekte einer typischen Backend-Anwendung abzubilden (Controller, Geschäftslogik und Persistenz).
Andererseits ist es so einfach wie möglich gehalten, um die hier wichtigen Aspekte hervorzuheben.

## Implementierungen

### Traditionelle Implementierung in Spring mit JDBC, Nicht Funktional
Einstiegspunkt ist Klasse [V1Controller](src/main/java/com/maranin/kotlinfundemo/v1springtraditional/V1Controller.kt). 