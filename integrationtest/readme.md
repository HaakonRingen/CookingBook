# Integrationtest

### Oversikt:

Denne integrasjonstesten for CookingBook - porsjektet er designet for å sjekke fuksjonalitet mellom GUI-et og REST-API-et. Testen skal sikre at funksjoner fungerer som forventet gjennom store deler av prosjektet. Dette er en fullstack integrasjonstest ettersom den tester hele applikasjonen fra frontend til backend.

### Før og etter testing:

Før testene kjøres, starter en Spring Boot REST-server automatisk på en lokal port. Serveren benytter seg av en backupfile for å ikke endre på dataen etter kjørte tester. (Isteden for å bruke Mock)Setter også op en ObjectMapper, en Client og en ny bruker.

Etter testene er kjørt stopper serveren automatisk. Deretter henter den backupfilen som er uendret og setter den lik orginalfilen.

### Testing:

For å kjøre integrasjonstestene, kjører man kommandoen:

   ```bash
   mvn test -DrunIntegrationTests

   ```

Kommandoen kjøres fra terminalvindu på modulens plassering.

Testen skal åpne serveren automatisk, men på grunn av manglende tilgjengelighet vet vi ikke sikkert på hvordan dette fungerer på windows OS.

1. Opprette bruker: Validerer at en ny bruker kan bli opprettet gjennom REST API-et og at dataen blir lagret som den skal

2. Hente bruker: Verifiserer at alle brukere kan bli hentet fra serveren.

3. Oppdatering av bruker data: Tester at allerede eksisterende brukere kan bli oppdatert og at oppdateringen også bli lagret korrekt.

4. Frontend: Simulerer brukerinteraksjoner i GUI, fra å lage en ny bruker til å opprette og slette en oppskrift.

5. Slette data: Verifiserer at oppskrifter kan slettes både i frontend og backend, og at det blir gjort rikitg.