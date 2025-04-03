# Fillagring

Denne modulen tar for seg hvordan fillagringen skal skje. Det er hovedsaklig repository-klassen for serveren som benytter seg aktivt av denne modulen i release 3 av prosjektet.

## Serialiseringsklasser

Serialiseringsklassene er skreddersydd, slik at vi lagrer objektene slik vi mener er mest oversiktlig og lesbart. Dette gjør at det enklere å potensielt importere andre JSON-filer eller lese JSON-filen som den er, og faktisk kunne forstå den hva den omhandler.

Siden klassene 'inneholder' andre klasser, er det dermed laget serialiserere for hver klasse, slik at alle klasser blir skrevet til fil på ønskelig måte.

## Deserialiseringsklasser

Disse klassene lager klasser av data på JSON-format. Siden vi skriver til fil på et skreddersydd JSON-format, må vi også naturligvis hente fra fil like skreddersydd, og én fil per kasse.

## Handler-klasse

`UsersHandler`-klassen er en oppsamlingsklasse som bruker `BookStudentModule`-klassen, og som enklere håndterer skriving til og fra fil. Klassen har 2 metoder, én for hver operasjon. Dermed kan andre klasser enkelt benytte seg av disse 2 metodene som klassen har, for å enklere skalere opp filskriving.

## Avhengigheter

Modulen benytter seg av følgende avhengigheter (testavhengigheter ikke inkludert):

- Jackson databind (JSON-bibliotek for parsing av data)
- Jackson core
- Jackson annitation
- Klassene i core-modulen