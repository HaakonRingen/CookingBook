# Dokumentasjon av REST API

## Server

Som skytjeneste har vi implementert et REST-API med Spring boot. Dette åpner opp for fjerntilgang av alt som lagres. Vi har lagt det opp slik at applikasjonen er avhengig at av serveren kjører for fungere. Applikasjonen i seg selv gjør ikke lengre noe lagring og skriving til og fra fil.

Kodebasen til serveren har en ryddig laginndeling der alle klasser har ulikt ansvar og ulike oppgaver:

- **Controller**-klassen har ansvar for å håndtere innkommende HTTP-forespørsler. Klassen bruker service-klassen til å gjennomføre forespørsler, og returnerer et svar til klienten.

- **Server**-klassen har ansvar i å utføre den såkalte "Business logic". Controller-klassen sender egentlig bare forespørslene videre til denne klassen, som igjen kaller på repository-klassen.

- **Repository**-klassen befinner seg i en egen mappe, og har ansvar for fillagring til en JSON fil som ligger i samme modul som serveren. Den henter og lagrer data til og fra denne filen i stedet for å for eksempel ha en mer tradisjonell SQL database.

For å få ønsket struktur på JSON-formatet som håndteres av controller-klassen, er det brukt 'Json properties' og Json creators' i de core-klassene som blir filbehandlet. Dette ble nødveldig for å få lik struktur som prosjektet hadde før implentasjonen av skyløsningen. Samtidig har dette gjort denne formathåndteringen uavhengig av persistence-modulen, som tidligere tok seg av filhåndtering. Nå er det bare repository-klassen som er avhengig av serialiserene og deserialiserene.

For å samle alle forespørsler til serveren er det opprettet en access-klasse i java-fx modulen i mappen 'remote'. Javafx-modelene og kontrollerene kaller på denne klassen for å kommunisere med serveren.

Serveren er designet 'stateless', som innebærer at klassene selv lagrer ikke infomasjon, men henter eller lagrer rett fra og til fil ved endringer. Dette sørger for at alle endringer som skjer, skjer også i fil, som skaper større sikkerhet og bedre pålitelighet i bruk.

Spring boot serveren har en egen modul i prosjektet, og gir fleksibilitet og mindre uavhengighet til resten av prosjektet, slik at man på sikt vil kunne kjøre serveren eksternt.

### HTTP-forespørsler

Vi har utgangspunkt i base-URLen for videre forklaring av støttende forespørsler.

Base-URL:

    http://localhost:8080/cookingbook/users

**Base-URL**

Har følgende mapping:

- Get-mapping, som responderer med et 'Users-objekt', som har til hensikt å liste opp alle eksisterende brukere.

- Post-mapping, med student-klasse som parameter, legger til/lagrer en ny bruker.

**'/{id}'**

Har følgende mapping:

- Get-mapping, som responderer med hele brukeren til ID-en, dersom det finnes en bruker med denne ID-en.

- Put-mapping som oppdaterer en eksisterende bruker. Dette blir for eksempel brukt når brukeren legger til eller sletter oppskrifter.

**'/accounts'**

Har følgende mapping:

- Get-mapping, som returnerer en hash, som brukes for å verifisere innlogging. Innloggingsystemet er på ingen måte et sikkert system.

## Testing

Modulen er testet med JUnit tester som bruker Mockito for å teste de ulike lagene i serverene helt uavhengig av hverandre. Dette fører til mindre avhengihet og forenkler vedlikehold i fremtiden.

Vi har oppnådd en høy dekningsgrad i testene. Dette sikrer at serveren fungerer som forventet under ulike forhold og scenarier. Testene dekker blant annet:

- Opprettelse av nye brukere
- Henting av eksisterende brukere
- Oppdatering av brukerinformasjon
- Håndtering av feil og unntak

Ved å bruke Spring Boot for å implementere vårt REST-API, har vi oppnådd en fleksibel og skalerbar løsning som muliggjør enkel tilgang til dataene våre. Et stateless design sikrer at alle endringer blir utført umiddelbart, noe som gir økt sikkerhet og pålitelighet. Gjennom grundig testing og bruk av verktøy som Checkstyle og SpotBugs, sikrer vi at koden vår holder høy kvalitet og er lett å vedlikeholde.

Ved å forskyve alt ansvar for lagring og oppbevaring av data til serversiden, vil det være veldig enkelt å skalere opp applikasjonen senere, ved å ha ulike applikasjoner med ulike frontendsystem, som alle benytter seg av rest-APIet på samme måte som denne applikasjonen.
