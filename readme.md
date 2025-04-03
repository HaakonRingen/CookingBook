# 🍲 Kokebok for matoppskrifter (Cookingbook)

Matlaging er en aktivitet mange setter pris på, enten det er for daglige måltider eller spesielle anledninger. Med et mangfold av oppskrifter tilgjengelig på nettet og i bøker, kan det være utfordrende å holde oversikt over favorittoppskriftene, ingrediensene og tilberedningstidene. Kokebokappen vår har som mål å forenkle denne prosessen for brukerne. Ved å samle oppskrifter på ett sted, gi enkel tilgang til ingredienser, og tilby funksjoner som tilpassede måltidsplaner, ønsker vi å gjøre matlagingen både enklere og mer inspirerende. Appen vil gi brukerne muligheten til å organisere sine favorittoppskrifter, oppdage nye retter, og effektivt planlegge sine måltider, og dermed forbedre deres matlagingserfaring.

Applikasjonen har et innloggingssystem som tilpasser appen etter hvem som er logget inn. Dette legger opp til kvantitativ bruk, samtidig som man bare vil kunne se sine egne oppskrifter.

Det er enkelt å lage en ny bruker, og komme seg i gang. Vi har lagt opp til at det er studenter som bruker appen vår, og bruker derfor student-ID aktivt.

Appen henter data fra server, som betyr at en server må være satt opp for at appen skal fungere.

---

## Oversikt

- [Kokebok for matoppskrifter (Cookingbook)](#kokebok-for-matoppskrifter-cookingbook)
  - [Oversikt](#oversikt)
  - [Krav](#krav)
  - [Eclipse che](#eclipse-che)
  - [Appfunksjonalitet](#appfunksjonalitet)
  - [Kjøring](#kjøring)
  - [Kodestruktur](#kodestruktur)
    - [Java-FX](#java-fx)
    - [Core](#core)
    - [Persistence](#persistence)
    - [Restserver](#restserver)
  - [Testdekning og byggverifisering](#testdekning-og-byggverifisering)
    - [JUnit-tester](#junit-tester)
    - [Integrasjonstester](#integrasjonstester)
    - [JaCoCo](#jacoco)
    - [SpotBugs og Checkstyle](#spotbugs-og-checkstyle)
- [Shippability av Appen](#shippability-av-appen)
  - [jlink](#jlink)
  - [jpackage](#jpackage)
- [Mappestruktur](#mappestruktur)
  - [Workflow](#workflow)

---

## Krav

Det stilles krav til å ha følgende:

- **Java:** Versjon 17
- **Maven:** Versjon 3.9.9

Det er en mulighet for at prosjektet kjøre med andre versjoner av java og maven, men det er ikke en garanti for dette.

## Eclipse Che

Hvis man har lagt inn en tilgangsnøkkel i Eclipse Che, kan man åpne prosjektet her:

[Åpne i Eclipse Che](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2024/gr2401/gr2401?new)

---

## Appfunksjonalitet

Kokeboken har følgende funksjonalitet:

- Stilig og moderne design som er appellerende å bruke
- Opprette en bruker med student-ID
- Logge inn med en eksisterende bruker
- **Legge til oppskrift:**
  - Man velger om man legger til frokost-, lunsj-, middag-, eller dessertoppskrift
  - Legge til ingredienser
    - Oversikt over hva slags ingredienser man er i ferd med å legge til
  - Legge til en 'slik gjør du'-tekst
- Vise og lese alle dine egne tidligere innlagte oppskrifter
- Filtrere visningen av oppskriftene basert på hvilken type oppskrift de er (lunsj, dessert etc)
- Slette oppskrifter man ikke lengre vil ha

---

## Kjøring

Det er nødvendig med 2 terminalvinduer for å kjøre appen; ett vindu for å kjøre serveren i bakgrunnen, og ett for å kjøre applikasjonen. Begge terminalene må åpnes i prosjektets rot.

1. **I vindu 1: bygg prosjektet med Maven**:

   ```bash
   cd coockbook/
   mvn clean install

   ```

2. **Naviger til `java-fx/`-mappen**:

   ```bash
   cd java-fx/

   ```

3. **I vindu 2: naviger til `restserver/`-mappen:**

   ```bash
   cd coockbook/restserver

   ```

4. **Kjør serveren:**

   ```bash
   mvn spring-boot:run
   ```

   #### Hvis port 8080 er i bruk:
   ```bash
   lsof -i :8080
   kill -9 <PID>
   ```
5. **I vindu 1: Kjør applikasjonen**:
   ```bash
   mvn javafx:run
   ```

---

## Kodestruktur

Maven-prosjektet cookingbook er oppdelt i ulike moduler, for å lage ulike layers og skille på hva som gjør hva. Dette gjør at det er enkelt å skalere, vedlikeholde og implementere ny funksjonalitet.

### Moduler

#### Java-FX

Fra denne modulen kjører man selve applikasjonen. Den inneholder alt relatert til det grafiske brukergrensesnittet. GUI-et er basert på java-fx, med fxml-filer for hver 'scene' i applikasjonen. Koden har et Model-Controller-View

> ➡️ [Mer informasjon](coockbook/java-fx/readme.md).

#### Core

Prosjektets kjernelogikk ligger i denne modulen. Alle objektene som er relatert til brukere og oppskrifter finnes her.

> ➡️ [Mer informasjon](coockbook/core/readme.md).

#### Persistence

For å lagre alle brukere og oppskrifter ble det opprinnelig laget serialiserere og deserialiserere for å gjøre dette. Alle disse ligger her. Etter implementasjonen av rest-API, er det per nå kun serveren repository-klassen på serversiden som bruker noen av disse klassene for å skrive til og fra fil. Før brukte javafx-modellene disse klassene, men dette er utfaset.

Formatet på lagring er på et egetdefinert json format.

> ➡️ [Mer informasjon](coockbook/persistence/readme.md).

#### Restserver

Dette er den nyeste modulen, som samler alt rundt rest-APIet og serveren. Herfra kjører man serveren. Rest-APIet tillater forespørsler for å hente innloggingsinformasjon, opprette nye brukere, samt oppdatere brukere ved oppretting/sletting av oppskrifter.

> ➡️ [Mer informasjon](coockbook/restserver/readme.md).

---

## Testdekning og byggverifisering

For å sikre kvaliteten på koden, har vi konfigurert prosjektet med verifiseringsverktøy som sjekker kodekvaliteten og testdekningen.

Slik fungerer det:

For å komme til prosjektet, kjører man:

```bash
cd coockbook/
```

Herfra kan man kjøre:

```bash
mvn clean verify
```

Denne kommandoen vil teste og verifisere koden i prosjektet:

### JUnit-tester

JUnit-tester for alle klasser og moduler er laget på en slik måte at testene vil feile dersom helt essensiell logikk og funksjonalitet ikke er tilstede.

### Integrasjonstester

Denne integrasjonstesten for CookingBook - porsjektet er designet for å sjekke fuksjonalitet mellom GUI-et og REST-API-et. Testen skal sikre at funksjoner fungerer som forventet gjennom store deler av prosjektet. Dette er en fullstack integrasjonstest ettersom den tester hele applikasjonen fra frontend til backend. Se mer:

> ➡️ [Mer informasjon om itegrasjonstester](./cookbook/integrationtest/readme.md)

### JaCoCo

For å verifisere at JUnit-testene har god dekningsgrad i prosjektet, brukes JaCoCo for å måle dette. JaCoCo er satt opp med en terskel på 75%, som vil si at prosjektet bygger ikke med maven dersom JUnit-testene ikke dekker minst 75% av prosjektet. Etter at `mvn verify` er kjørt i terminalen, vil dekningsresultatet vises i `coverage`-modulen, som eksisterer utelukkende for å samle testresultater. I denne mappen, under `target/site`, der kan `index.html` åpnes og viser testdekningen.

### SpotBugs og Checkstyle

Implenentasjon av SpotBugs fører til at vi kan identifisere og rette opp potensielle feil i koden, og har en medium. Checkstyle brukes for å sikre at koden følger definerte kodestandarder og retningslinjer. Begge verktøyene er integrert i byggeprosessen og vil kjøre automatisk når `mvn verify`-kommandoen utføres. Prosjektet bygges ikke dersom disse ikke er tilfredsstilt.

---

# Shippability av Appen

For å pakke og distribuere appen effektivt, støtter prosjektet **jlink** og **jpackage** for å lage bygg som er enkle å distribuere på tvers av miljøer. Følg disse stegene for å generere en kjørbar versjon av applikasjonen.

---

## jlink

1. **Naviger til hovedfolderen og kjør mvn clean**:

   ```bash
   cd coockbook/
   mvn clean

   ```

2. **Bruker jlink til å få zip file og build artifacts**:

   ```bash
   mvn -pl java-fx javafx:jlink

   ```

3. **For å finne zip filen og build artifacts naviger til:**

   ```bash
   cd java-fx
   cd target

   ```

4. **Naviger til den executable filen og execute**:

   ```bash
   cd java-fx/
   cd target/
   cd coockbook/
   cd bin/
   ./coockbook
   ```

## jpackage

1. **For å pakke prosjektet og bruke appen, bruk**:
   ```bash
   mvn -pl java-fx jpackage:jpackage
   ```

> **NB! Må kjøre stegene med jlink først**

# Mappestruktur

Prosjektet er organisert i en modulær struktur for enkel skalering, vedlikehold og utvidelse. Her er en oversikt over de viktigste mappene og filene:

### Oversikt over moduler

- **`core/`**: Inneholder kjernelogikken, med objekter og klasser for brukere og oppskrifter.
- **`java-fx/`**: Hovedmodulen for JavaFX-applikasjonen. Inneholder GUI-relaterte filer og logikk.
- **`persistence/`**: Tidligere ansvarlig for lokal datalagring. Nå brukt av serveren for å lese/lagre JSON-filer.
- **`restserver/`**: REST API-serveren for appen, håndterer forespørsler og dataoperasjoner.

### Fullstendig struktur

```
├── coockbook
│   ├── config
│   │   ├── checkstyle
│   │   │   └── checkstyle.xml
│   │   └── spotbugs
│   │       └── spotbugs_filter.xml
│   ├── core
│   │   ├── pom.xml
│   │   ├── readme.md
│   │   └── src
│   │       ├── main
│   │       │   └── java
│   │       │       ├── core
│   │       │       │   ├── Book.java
│   │       │       │   ├── Breakfast.java
│   │       │       │   ├── Dessert.java
│   │       │       │   ├── Dinner.java
│   │       │       │   ├── Ingredient.java
│   │       │       │   ├── Lunch.java
│   │       │       │   ├── Recipe.java
│   │       │       │   ├── Student.java
│   │       │       │   └── Users.java
│   │       │       └── module-info.java
│   │       └── test
│   │           └── java
│   │               └── core
│   │                   ├── BookTest.java
│   │                   ├── IngredientTest.java
│   │                   ├── RecipeTest.java
│   │                   ├── StudentTest.java
│   │                   └── UsersTest.java
│   ├── coverage
│   │   ├── pom.xml
│   ├── java-fx
│   │   ├── pom.xml
│   │   ├── readme.md
│   │   └── src
│   │       ├── main
│   │       │   ├── java
│   │       │   │   ├── fxui
│   │       │   │   │   ├── Coockingbook.java
│   │       │   │   │   ├── UserSession.java
│   │       │   │   │   ├── loginpage
│   │       │   │   │   │   └── LoginController.java
│   │       │   │   │   ├── mainpage
│   │       │   │   │   │   ├── Controller.java
│   │       │   │   │   │   └── Model.java
│   │       │   │   │   ├── newuserpage
│   │       │   │   │   │   ├── NewUserController.java
│   │       │   │   │   │   └── NewUserModel.java
│   │       │   │   │   └── remote
│   │       │   │   │       └── CookingBookRemoteAccess.java
│   │       │   │   └── module-info.java
│   │       │   └── resources
│   │       │       └── fxui
│   │       │           ├── loginpage
│   │       │           │   ├── Login.fxml
│   │       │           │   └── login.css
│   │       │           ├── mainpage
│   │       │           │   ├── HomeScreen.fxml
│   │       │           │   └── homescreen.css
│   │       │           └── newuserpage
│   │       │               ├── NewUser.fxml
│   │       │               └── newuser.css
│   │       └── test
│   │           └── java
│   │               └── fxui
│   │                   └── CookingBookTest.java
│   ├── persistence
│   │   ├── pom.xml
│   │   ├── readme.md
│   │   └── src
│   │       ├── main
│   │       │   └── java
│   │       │       ├── module-info.java
│   │       │       └── persistence
│   │       │           ├── BookDeserializer.java
│   │       │           ├── BookSerializer.java
│   │       │           ├── BookStudentModule.java
│   │       │           ├── IngredientDeserializer.java
│   │       │           ├── IngredientSerializer.java
│   │       │           ├── RecipeDeserializer.java
│   │       │           ├── RecipeSerializer.java
│   │       │           ├── StudentDeserializer.java
│   │       │           ├── StudentSerializer.java
│   │       │           ├── UserDeserializer.java
│   │       │           ├── UserSerializer.java
│   │       │           └── UsersHandler.java
│   │       └── test
│   │           └── java
│   │               └── persistence
│   │                   ├── BookStudentModuleTest.java
│   │                   └── UsersHandlerTest.java
│   ├── pom.xml
│   └── restserver
│       ├── SavedUsers.json
│       ├── pom.xml
│       ├── readme.md
│       └── src
│           ├── main
│           │   ├── java
│           │   │   ├── module-info.java
│           │   │   └── restserver
│           │   │       ├── AppConfig.java
│           │   │       ├── CookingBookController.java
│           │   │       ├── CookingBookService.java
│           │   │       ├── RestServerApplication.java
│           │   │       └── repository
│           │   │           └── CookingBookRepository.java
│           │   └── resources
│           │       └── application.properties
│           └── test
│               └── java
│                   └── restserver
│                       ├── CookingBookControllerTest.java
│                       ├── CookingBookServiceTest.java
│                       └── repository
│                           └── CookingBookRepositoryTest.java
├── devfile.yaml
├── docs
│   ├── diagrams
│   │   ├── classdiagram.puml
│   │   ├── packagediagram.puml
│   │   ├── sequencediagram.puml
│   │   └── sequencediagramfirst.puml
│   ├── release1
│   │   ├── ai-tools.md
│   │   └── release1.md
│   ├── release2
│   │   └── release2.md
│   ├── release3
│   │   ├── release3.md
│   │   └── sustainability.md
│   └── stories
│       └── stories-release2.md
├── pictures
│   ├── Kokebok.png
│   ├── Kokebok2.png
│   ├── Sekvensdiagram.png
│   └── directory_structure.png
├── readme.md
└── struktur.txt
```

## Workflow
Vi strukturerte arbeidsflyten vår gjennom ukentlige møter, der vi jobbet tett sammen for å sikre kontinuerlig fremdrift. Et av hovedprinsippene våre var å dele opp prosjektet i mindre, håndterbare oppgaver (issues) som hver enkelt deltaker kunne ta ansvar for. Dette ga oss mulighet til å jobbe parallelt, samtidig som vi oppmuntret til samarbeid og innspill fra teamet underveis.

Vårt hovedfokus var å utvikle de ulike releasene på en effektiv og gjennomtenkt måte, der alle steg i prosessen ble nøye vurdert. For å sikre god flyt i arbeidet, opprettet vi merge requests basert på de definerte issuesene. Dette gjorde det mulig for teamet å aktivt gå inn i branches, kommentere på endringer, gi tilbakemeldinger, og bidra der det var nødvendig. På denne måten fikk vi også bedre oversikt over de ulike endringene og forbedringene i prosjektet.

Når vi møtte på utfordringer – og det var flere av dem – tilpasset vi oss ved å sette opp ekstra møter i tillegg til de ukentlige faste møtene. Disse møtene ble brukt til å løse problemer raskt, avklare misforståelser, og diskutere løsninger i fellesskap. Dette gjorde at vi kunne opprettholde fremdriften og kvaliteten i arbeidet vårt.
