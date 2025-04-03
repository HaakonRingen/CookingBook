# Release 3

*Denne releasen følger milestone 3 i GitLab*

*Vi har valgt å fokusere på videreutvikling av funksjonalitet for applikasjonen i vår release 3*

## Ny funksjonalitet


### Helt nytt system for oppskrifter

Nå må man logge inn (som student) for å bruke kokeboken. Har man ikke en bruker må man lage en bruker.

Når man er logget inn vil man kunne kunne se og lagre sine egne oppskrifter. Det vil si at flere kan bruke samme applikasjon, uten at man kan se alle sine andre oppskrifter.
Dette er praktisk spesielt dersom man ikke kjenner alle andre brukere av applikasjonen, og vil ha personlige oppskrifter.

Innloggingssystemet knyttes opp mot brukerhistorie 7.

### Ulike typer oppskrifter

Nå kan man velge hva slags oppskrift man legger inn; om det er frokost, lunsj, middag eller dessert. I denne forbindelsen er det opprettet følgende klasser:

- Breakfast.java
- Lunch.java
- Dinner.java (opprinnelig bare denne klassen)
- Dessert.java
- Recipe.java

**Recipe.java** er en superklasse, der alle typer av oppskrifter er subklasser, der eneste forskjell er variabeltypen *type*, som vil variere fra hva slags oppskrift det er.

I denne forbindelse er alle variabelnavn endret fra typisk "dinner" i koden til det mer abstrakte "recipe".

Tester og filskriving er også oppdatert som en følge av dette. Man har nå *Recipe* serialiser og deserialiserer, som er mer genrell, og tar hensyn til ulike typer oppskrifter. Type oppskrift lagres som en egen node, som sjekkes av deserielisereren for å avgjøre hva slags type oppskrift det er.

Utviklingen av denne funksjonaliteten knyttes opp til brukerhistorie 6.

## Nye brukerhistorier

**Brukerhistorie 6:**

    Som en bruker av denne appen ønsker jeg en mulighet for å legge til hva som helst av oppskrifter, slik at jeg oppnår en bedre kokebok som er mer anvendelig og organisert

**Brukerhistorie 7:**

    Som den 1842. brukeren av denne appen ønsker jeg ikke å se alle andres oppskrifter, men kun mine egne, slik at jeg finner igjen mine oppskrifter, fordi jeg vet at disse er best.

**Brukerhistorie 8:**

    Som en bruker av denne appen har jeg vist appen til familien min som syns at den ikke ser så veldig bra ut og at de ikke har lyst å bruke en app som ikke ser visuelt trendy ut, jeg vil derfor ha en app som ser finere ut sånn at det ser bedre ut når jeg viser andre.

**Brukerhistorie 9:**

    Som en bruker av denne appen som reiser mye rundt har jeg lyst å kunne hente dataen min fra overalt i verden, altså kunne hente oppskriftene mine fra skyet sånn at jeg kan lage mat i de ulike stedene jeg reiser til.

## CSS og andre grafiske effekt

Vi oppdaterer utseende til applikasjonen!

Det er nå et moderne, mørkt utseende i hele applikasjonen. Kode for styling ligger i resources, sammen med de korresponderende FXML-filene. Knappestruktur og farger er forbedret med generelle designprinsipper.

I tillegg er noe styling kodet. Da snakker man om blant annet knapper som forstørrer seg når musen er over knappen, og tekst som skriver seg selv.

Implementasjonen knyttes opp til brukerhistorie 8.

## REST-API

Vi går nå bort fra å skrive rett til fil fra java-fx modulen. Alle slike operasjoner skjer nå gjennom vårt rest-API, slik at data blir forespurt fra en server.

Derfor er java-fx modulen nå uavhengig av persistence-modulen. Det er nå kun service-klassen som bruker denne aktivt, for å hente og lagre til og fra fil. Denne filen er fortsatt på et JSON-format, og befinner seg nå separert fra client-siden, i restserver-modulen vår. Dermed er det nå en stor separering mellom client- og server, som gjør fremtidig vedlikehold og en eventuell oppskalering senere mye enklere og mer overkommelig.

På den andre siden fører dette til applikasjonen ikke oppfører seg som ønskelig dersom man ikke har servertilkobling. Altså må man ha serveren kjørende i bakgrunn.

Funksjonaliteten knyttes opp til brukerhistorie 9.

## Forbedret tester, kodekvalitet og dokumentasjon

Samtlige moduler har nå en større testdekningsgrad enn tidligere. JaCoCo-terskelen er hevet, til tross for at prosjektet som helhet har vokst. 

I tillegg brukes nå Checkstyle nå for å holde kodekvaliteten oppe, samt enklere vedlikehold av kodebasen senere. SpotBugs-terskelen er også hevet, fra high til medium (krav om mindre feil). Begge er satt opp med en config-fil, der for eksempel SpogBugs ignorerer enkelte varsler vi mener ikke er relevante for sikkerheten til enkelte klasser, og som er nødvendig for at klassene skal fungere som tenkt.

JUnit-testene i `core` og `persistence` er forbedret ytterligere for å øke dekningsgrad og dekke enda flere 'for' of 'if' scenarioer. I tillegg er det nå mye mer dekkende tester på `java-fx`-modulen, som nå har over 80% dekning etter ny funksjonalitet. Også `restserver`-modulen har god dekning med fokus på å dekke så mange grener som mulig.

Kodebasen i seg selv har også fått en renovering, som går relative langt forbi kravene til Checkstyle og SpotBugs. Kodelogikk er mer effektivt, og har økt lesbarhet. Prosjektet har også noe annen struktur enn tidligere, for å separere ting som har ansvar for forskjellige ting, og etter vår mening gir et strukturellt bedre prosjekt.

Dokumentasjon for prosjektet og moduler er forbedret. Det er en mer fyldig dokumentasjon som dekker mer og forklarer ting bedre, for å øke generell forståelse av prosjektet samt enklere klient-bruk.