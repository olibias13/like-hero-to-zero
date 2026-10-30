# Like Hero To Zero

Prototyp für die IU-Fallstudie IPWA02-01 (Java EE): Webanwendung zur Anzeige weltweiter CO2-Emissionen.

## Technik
JSF (Jakarta Faces 4) mit CDI, PrimeFaces, JPA mit Hibernate, MySQL 8.4, Tomcat 10.1, Java 17, Maven.

## Funktionen
- Öffentlich: Land wählen und den aktuellsten CO2-Wert sehen (ohne Login)
- Login-Bereich: Wissenschaftler:innen tragen Werte ein oder korrigieren sie

## Einrichtung
1. MySQL installieren und `database/setup.sql` ausführen (Passwort im Skript ersetzen).
2. `src/main/resources/db.properties.example` nach `db.properties` kopieren und Benutzer und Passwort eintragen.
3. Projekt als Maven-Projekt importieren und auf Tomcat 10.1 starten.
4. Beim ersten Start werden die Daten aus `src/main/resources/data/co2-emissions.csv` importiert.
5. Aufruf: http://localhost:8080/like-hero-to-zero/

## Demo-Zugang
Benutzer `forscher`, Passwort `demo1234`

## Daten
Our World in Data, CO2- und Treibhausgasemissionen (Einheit: Millionen Tonnen CO2).