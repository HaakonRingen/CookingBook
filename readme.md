# Oversikt Java-fx modul:

- [Kort forklaring](#kort-forklaring)
- [Struktur](#struktur)
- [Avhengigheter](#avhengigheter)

## Kort forklaring

Denne modulen foretar seg alt av det grafiske for applikasjonen, pluss noen andre funksjoner som beskrives under. Generelt sett finnes det én controller per fxml-fil i `resources`, hvor kontrolleren henter logikk fra en model-klasse. For enklere oversikt og mer uavhengighet er det laget flere model-klasser, avhengig av hvilken controller-klasse som er aktiv. Denne strukturen gjør det enkelt å vedlikeholde og utvide applikasjonen ved å legge til nye scener, controller-klasser og model-klasser etter senere behov og ønsker.

## Struktur

Applikasjonen benytter seg av totale 3 ulike 'scenes':

- [Innloggingsside/oppstartsside](#innloggingsside)
- [Side for å lage en ny bruker](#ny-bruker)
- [Hovedside](#hovedside), der alle oppskrifter til den innloggede brukeren vises/opprettes

Alle controller-klassene har som nevnt sin egen model-klasse som håndterer logikk, bortsett fra innloggingssiden som i praksis ikke utfører noe særlig logikk, og utfører derfor den lille logikken selv. 

### Innloggingsside
Innloggingssiden har et innlogginsfelt, der en ID og et passord sjekkes gjennom en remote-klasse (som beksrives lengre ned). Dersom brukervalideringen blir godkjent, blir brukeren sendt til neste 'scene', som er hovedsiden. Ved feil blir brukeren varslet om at innlogginen feilet. Siden dette er all logikk som skjer på denne siden, har vi lagt at controller-klassen kaller rett på remote-klassen i stedet for at dette går gjennom en model-klasse.

### Hovedside
Hovedsiden består av flere komponenter:

- En liste over oppskrifter på toppen
- Et detaljvisningsområde på venstre side som viser informasjon om en valgt oppskrift
- Et interaktivt vindu til høyre for å legge inn oppskrifter

Når en bruker velger en oppskrift fra listen over eksisterende oppskrifter, oppdateres det detaljerte visningsfeltet nede til venstre med informasjon om den valgte oppskriften. Når en oppskrift er valgt kan brukeren også velge å slette den valgte oppskriften. Nede til høyre kan brukeren legge til en ny oppskrift ved å legge til en tittel, velge type oppskrift, en 'slik gjør du' veiledning, og oppskrifter. Man vil kunne se hvilke oppskrifter som er lagt inn, slik at man har oversikt over de registrerte oppskriftene til enhver tid. Listen over de innlagte oppskriftene skjer er lokal logikk som skjer i model-klassen, men idet man lagrer hele oppskriften blir det sendt en endringsforespørsel til server.

### Ny bruker

Denne siden lar en bruker opprette en profil basert på sin student-ID. Kontroller-klassen benytter seg av logikk i sin egen model-klasse. Dersom logikken går gjennom, blir det sendt en forespørsel til server, og en ny bruker blir opprettet. Brukeren blir varslet om det ble opprettet en bruker eller om noe feil skjedde.


### Remote-klasse

Remote-klassen håndterer all kommunikasjon med server. Den benytter seg av HTTP-forespørsler for å sende og motta data. Denne klassen er ansvarlig for å validere brukerinformasjon ved innlogging, samt hente og oppdatere oppskrifter for den innloggede brukeren. Vi har valgt å samle alle forespørsler i én og samme klasse, slik at strukturen er oversiktlig, og det er enkelt å vedlikeholde og gjøre nødvendige endringer underveis i utviklingen.

### User session-klasse

Det finnes en egen klasse som inneholder informasjon om hvilken bruker som er innlogget. Denne klassen registrerer når et innloggingsforsøk blir godkjent, og lagrer hvilken bruker dette er, slik at controller-klassen og den korresponderende model-klassen til hovedsiden vet hvem som er innlogget. Slik overføres denne informasjonen mellom de ulike sidene.

### Testing av Java-Fx 

Struktur: 
Testing av klassene i "java-fx"-mappen ligger i tre mapper.
- Testene for controllere til login, mainpage og newuser ligger under test/java/fxui/controllers
- Testen for model-klassene til mainpage og newuser ligger under test/java/fxui/models
- Testen for remoteklassen som sender http-requests ligger under test/java/fxui/dataaccess 
- Testen som sjekker "startup" til appen ligger direkete i mappen test/java/fxui under navnet "CookingBookTest"

Mocking: 
Store deler av testene baserere seg på mocking. Vi bruker @injectmocks for å spesifisere hvilken klassen vi ønsker å teste isolert. Alle andre klasser denne klassen er avhengig av merkes med @mock. Dette gjør det enkelere å skrive fokuserte og raske tester. Med mocks får vi opprettet forutsigbare miljø ved å definere hvordan mock-objektene skal oppføre seg. Ved å mocke blir det letter å identifisere feil eller problemer uten å blande inn eksterne faktorer. Ved å foreksempel mocke "CookingBookRemoteAccesss" unngår vi å gjøre faktiske HTTP-kall, men får simulert oppførselen til denne klassen. 

Coverage: 
Jacoco rapporterer om en testdekning på rundt 70% for klassene fxui.mainpage.Controller, fxui.mainpage.Model og fxui.loginpage.LoginController som drar ned det totale snittet for javafx-modulen sin testdekning. Selv om det ikke er hensiktsmessig å teste alle metoder i javafx, føler gruppen at de har fokusert på å teste de viktigste funksjonalitetene i applikasjonen. Gruppen har også lagt stor vekt på å sikre høy testdekning i kjernelaget (core), som de anser som essensielt for appens stabilitet og kvalitet.

## Avhengigheter

Modulen benytter seg av følgende avhengigheter (testavhengigheter ikke inkludert):

- JavaFX for GUI-komponenter (openjfx).
- Jackson databind (JSON-bibliotek for parsing av data)
- Klassene i core-modulen