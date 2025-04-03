# Kokebok for matoppskrifter

Dette skal bli en kokebok, hvor man kan samle matoppskrifter på én og samme plass.

## Motivasjon og formål for prosjektet:

Matlaging er en aktivitet mange setter pris på, enten det er for daglige måltider eller spesielle anledninger. Med et mangfold av oppskrifter tilgjengelig på nettet og i bøker, kan det være utfordrende å holde oversikt over favorittoppskriftene, ingrediensene og tilberedningstidene. Kokebokappen vår har som mål å forenkle denne prosessen for brukerne. Ved å samle oppskrifter på ett sted, gi enkel tilgang til ingredienser, og tilby funksjoner som handlelister og tilpassede måltidsplaner, ønsker vi å gjøre matlagingen både enklere og mer inspirerende. Appen vil gi brukerne muligheten til å organisere sine favorittoppskrifter, oppdage nye retter, og effektivt planlegge sine måltider, og dermed forbedre deres matlagingserfaring.

## Plan for prosjektet

### Enkelt grafisk brukergrensesnitt med Java-FX

Prosjektet vil kunne åpnes som en app, hvor man kan interagere med de ulike funksjonene med maven.

### Lagring av oppskriftene

For å lagre oppskrifter vil det bli lagring til TXT lokalt, som kan hentes senere, slik at oppskriftene kan brukes ved et senere tidspunkt.

### Funksjoner for å legge inn og vise lagt inn oppskrifter

Noen av hovedfunksjonene til appen vil være å både kunne legge inn så mange oppskrifter man vil, og kunne følge disse ved å åpne dem ved en senere anledning.

## Brukerhistorier 

### Brukehistorie release1

En student har funnet en matoppskrift han gjerne vil lagre for senere bruk. Han tar i bruk kokebok-appen og legger til en kort beskrivelse av framgangsmåte med ingredienser og navn på retten. 

## App etter release:

### release1:

![App etter release1](pictures/Kokebok.png)

## release2:

![App etter release1](pictures/Kokebok.png)

### Forklaring

Skriv inn hva man vil kalle oppskriften i matrett-feltet.

Som beskrivelse kan man legge til en fremgangsmåte for oppskriften, gjerne stegvis for enklere forståelse til senere tidspunkt.

I de to tomme feltene legger man inn en ingrediens. Hvis oppskriften for eksempel krever 100g sukker, skriver man *sukker* inn i feltet til venstre, *100* i feltet til høyre, og velger *g* i drop-down menyen. Man velger og antall porsjoner i drop-down menyen. Deretter trykker man **legg til**, for å legge ingrediensen til oppskriften. Feltene vil deretter tømmes, slik at man kan legge til neste ingrediens.

Når alle ingrediensene er lagt til og oppskriften har navn, trykker man **Fullfør** for å legge til oppskriften til de lagrede oppskriftene.












