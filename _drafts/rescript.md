---
layout: post
title: "ReScript"
---

# Hva er ReScript? Og hvorfor har noen laget det? #

> ReScript: The JavaScript-like language you have been waiting for.

Dette sitatet er fra [rescript-lang.org](https://rescript-lang.org/). Så ReScript er et programmeringsspråk som ligner på JS, og sannsynligvis føles ganske kjent for utviklere som allerede kan JS.

ReScript skal være et språk for oss som bruker JS, men som ønsker oss et språk uten alle de rare delene av språket som vi har lært oss å leve med.

For oss som ønsker oss et solid typesystem, sånn at vi kan skrive om koden vår uten å være redd for å brekke noe. Et typesystem kan også gi oss bedre editor-verktøy, med bedre code completion og mer fornuftige forslag. 

For oss som ønsker oss lette, raske og enkle byggeverktøy, som ikke tar dagesvis å konfigurere.

ReScript er laget for å gjøre dette bedre enn JS. Det ser ut som JS, og kompilerer til lesbar JS-kode som kan kjøres i nettleseren eller i node. Dette er veldig annerledes fra andre språk som kompilerer til JS. Kompilert ReScript ser ut som JS som du har skrevet selv, noe som gjør det veldig enkelt å se hva resultatet av ReScript-koden din er. 

ReScript er for oss som anerkjenner at JS er et viktig språk, men som ønsker oss noe mer av verktøyet vi bruker på daglig basis. 

# ReScript og React #

ReScript kommer med bindings til React, og du kan skrive react-programmer direkte i ReScript. Det brukes i dag i store kodebaser, og er kompatible med moderne React (>= 16.8). JSX er innebygget i språket, så du trenger ingen babel-plugins, eller noe lignende.

# Hvordan bruker jeg det? #

Så hvordan kommer vi i gang? Vi følger oppskriften under "Getting started" på [nettsiden](https://rescript-lang.org/docs/manual/latest/installation). 

Første steg er å installere `bs-platform`:

``` shell
npm install -g bs-platform
```

I neste steg laster vi ned en _project template_ fra github, installerer avhengigheter, bygger og kjører koden:

``` shell
git clone https://github.com/rescript-lang/rescript-project-template
cd rescript-project-template
npm install
npm run build
node src/Demo.bs.js
```

Dette fungerer som forventet, og skriver ut `Hello world!`. Et viktig første steg, men ikke så veldig spennende. Men allerede her kan du se hvordan ReScript kompilerer til lesbar JS, ved å åpne fila `Demo.bs.js`. 

Neste steg er å installere react, og react bindings for ReScript. 

# "Hello, React!" #

Vi følger instruksjonene for [rescript-react](https://rescript-lang.org/docs/react/latest/installation). Den forventer at vi har installert `bs-platform` og `react`. Vi må også installere react-pakken som ligger under @rescript-organisasjonen:

``` shell
npm install react@16.8.1 react-dom@16.8.1 bs-platform@8.30
npm install @rescript/react --save
```

I tillegg må vi legge til noe konfigurasjon i `bsconfig.json`, som er konfigurasjon for kompilatoren:

``` shell
{
  "reason": { "react-jsx": 3 },
  "bs-dependencies": ["@rescript/react"]
}
```

For å teste at det fungerer, lager i en enkel komponent i `src/Test.res`:

```
@react.component
let make = () => {
  <div> {React.string("Hello, React!")} </div>
}
```

For å vise dette i nettleseren lager vi `src/App.res`, som ser etter en `div` med id-en `root`, og tegner komponenten vår der, hvis den finnes:

``` reason
switch ReactDOM.querySelector("#root") {
| Some(root) => ReactDOM.render(<Test />, root)
| None => () // do nothing
}
```

Her er det flere nye ting som skjer. Vi ser at vi kan bruke `ReactDOM` uten at vi importerer den. Det gjelder også `React`-modulen. Det skyldes at `@rescript/react` importerer disse automatisk. Det gjelder også et par andre moduler, [som du kan lese mer om her](https://rescript-lang.org/docs/react/latest/installation). 

I JS er vi vant til at funksjoner returnerer `null` hvis noe ikke finnes, for eksempel at vi ikke finner noden med id-en `root`. I ReScript finnes ikke `null`, og `querySelector` returnerer heller en option-type, som enten har en verdi (`Some(root)`) eller ikke (`None`). Vi får tak på den aktuelle verdien med _pattern matching_. Ved første øyekast kan dette virke tungvint, men det sparer oss for _et lass_ med feilsituasjoner, som vi må huske på å håndtere i JS. Og som vi ofte glemmer. Du kan lese mer om [Null, Undefined and Option](https://rescript-lang.org/docs/manual/latest/null-undefined-option) og [Pattern Matching](https://rescript-lang.org/docs/manual/latest/pattern-matching-destructuring) i dokumentasjonen. 

Til slutt lager vi `src/index.html`, og lenker til den produserte js-fila vår:

``` html
<div id="root"></div>
<script src="./App.bs.js"></script>
```

JS-koden som ReScript produserer bruker commonjs som default, og den kan også konfigureres til å bruke es6-moduler. Uansett har den referanser til pakker i `node_modules`, så vi må ha en bundler av noe slag for at dette skal virke i nettleseren. Jeg har brukt [parcel](https://parceljs.org/), fordi den er enkel og rask. Kjør dev-serveren med `parcel src/index.html`, og åpne `localhost:1234`. Tada 🎉

# Hvordan fungerer det til å bygge noe mer enn "Hello world"? #
