
## Moduler inkludert i release 1

### Core

Denne modulene har hovedlogikken for kokebokken, som har to klasser Dinner og Ingredient der Dinner er da oppskriften for hver dinner som bruker Ingredient objekter, samt har et navn, antall porsjoner og en tutorial.

### java-fx

I denne modulen har vi følgt Model-View-Controller prinsippet veldig tett og har laget 3 seperate klasser innenfor javaFX modulen, som er da:
- Controller
- Model 
- Cookingbook(view)
  
Modulen er dependant på:
- core
- persistence

### persistence

Denne modulen er ansvarlig for alt det som ikke er temporary og skal bli lagret til videre bruk av appen. Modulen er dependant på core. Den har og to klasser:
- DinnerFileWriter: som tar inn en dinner som input og den andre klassen FileHandler. 
- FileHandler: ansvarlig for å skrive til en fil og lese fra en fil. 

Fil formatet vi har valgt er da en txt file som har da alle oppskriftene som er lagret.

## Testing

### Core

Tests for:
- Dinner
- Ingredient klassen som tester metodene. 
  
Må ha mer grundig tester for neste release.

### Persistence

## Maven

Bruker Maven 3.9.9.
Har satt opp en pom fil for hver module og for hele prosjektet.

## Java

Bruker Java 17.0.12-tem.


## Issues Solved
- Opprette model for fxml filen
- Lage filklasse
- Oppdatere pom til java-fxml
- Lage generelle tester i core
- En toString formatering mangler i core
- Opprette txt-fil, for skriving til og fra fil
- Lage en initialize metode i core
- Sette opp til Eclipse Che
- fix csv til text
- Oppdater pom for core
- Maven compile i core
- Lage toString metoder
- Opprette oppskrift klasse i core
- Fix Che link
- Dokumentasjon i docs
- Fylle inn realease1.md med informasjon
- Oprrette kontroller for fxml filen
- Lage filskrivingstester
- Fikse sti til fillagring
- Lage ferdig modelklassen
- Koble eksisterende oppskrifter opp mot read-fromfile
- Lage onAction knapper i fxml
- Lage tester i java-fxml
- Rydde opp i kontrolleren