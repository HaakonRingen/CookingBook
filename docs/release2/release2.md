# Release 2

*Release 2 følger milestone 2 på GitLab*

## Ny funksjonalitet

### Visning av oppskrifter
Det er nå mulig å se/vise allerede opprettede og lagrede oppskrivfer, slik at man faktisk kan bruke de ved en senere anledning. Dette gjøres ved å klikke på en oppskrift fra listen med oppskrifter, og deretter vises under.

### Sletting
Det er nå mulig å slette opprettede oppskrifter med en knapp. Trykk på oppskriften som skal fjernes, deretter **slett**

### Visning av ingredienser
Etter man har lagt til en ingrediens når man oppretter en ny oppskrift, er det nå mulig å se hva slags ingredienser som allerede er lagt til. Dette forhindrer at man legger til samme ingrediens med uhell flere ganger.

### 

## Større dekningsgrad på tester

Jacoco er nå satt til 60% dekningsgrad, slik at testene ikke vil kjøre med suksess dersom dekningsgraden er under dette. Vi har likevel mye høyere dekningsgrad enn dette.

Det er nå tester for alle klasser (relevante for å teste) i alle modulene. Det er også brukt mock tester der det har passet seg. Dette gjelder spesielt for testing av java-fx.

Vi bruker spotbugs med ulik terskel (står normalt på High) for å kvalitetssikre kode.

## Fra TXT til JSON

Vi har nå byttet ut filskriving til TXT med filskriving til JSON filer. Dette gjelder også lesing fra fil. Dette gjøres nå gjennom serialiserere og deserialiserere. Dette gjøres med Jackson. Det er en BookModule klasse som sørger for håndtering av alle serialiserere og deserialiserere. For praktiske årsaker ble det opprettet en Book klasse som fungerer som den faktiske kokeboken som inneholder en kolleksjon (faktisk en array liste) med oppskrifter. Poenget er at det er denne som skrives til fil og hentes fra fil. Boken blir skrevet som en liste av oppskrifter til JSON-filen SavedRecipes.json inne i java-fx modulen. Listen består av "dinner" objekter, der navn, fremgangsmåre og en liste av oppskrifter er inkludert. Vi har i tillegg en mellomklasse FileHandler som gjør det ekstra enkelt å håndtere filer for modulen til java-fx.

## Dokumentmetafor vs Implisitt lagring 

Ved å bruke implisitt lagring slipper brukeren å eksplisitt lagre data, da systemet automatisk håndterer dette. I vårt tilfelle trenger ikke brukeren å tenke på lagring, siden dataene automatisk sendes til fil når de trykker på "Fullfør". Eventuelle midlertidige ingredienser som legges til underveis, kan hentes opp igjen etterpå. Dette gjør brukeropplevelsen sømløs og enklere ved å eliminere behovet for manuell lagring.

## Modul for testing

Det er opprettet en egen modul **Coverage** som har som eneste funksjon å samle alt fra Jacoco lett og oversiktlig inn i target/ der. **mvn verify** er fortsatt kommandoen for å lage Jacoco-resultatene. Dette kan skje fra parent.

## Meta-info

### Arbeidsvaner

Gruppen sitter alltid sammen og koder, slik at alle kan hjelpe alle ved behov. Det er opprettet mange (store og) små issues i stedet for svært få men omfattende issues. Dette gjør at det er helt overkommelig at én person tar én issue selv.

Issues blir brukt på samme måte som Hallvard Trætteberg har illustrert. De har labels som tilsier hvor i utviklingsstadiet de befinner seg i. Issues blir alltid opprettet i open, og resten sier seg selv.

Dersom noen er nødt til å ta over en issue fra noen andre, dedikeres også issuen til overkateren dersom det gjenstår relativt mye igjen før issuen er ferdig behandlet. Co-author er noe vi sjeldent bruker, men blir brukt ved behov. Vi liker heller å hjelpe hverandre ved behov, og at dette går opp til slutt.

Vår måte å jobbe på blir derfor veldig dynamisk, ettersom den som blir ferdig med en issue starter på neste issue, sortert i kronologisk prioritert rekkefølge. Dette gjør at så alle koder, alle dokumenterer, alle tester og alle feilsøker i tilfeldig rekkefølge.

Ute i konstruksjonen av av release2 ble det implementert fast å requeste merge til main når store endringer trer i kraft.

# Sekvensdiagram

![](/pictures/Sekvensdiagram_Release2.png)