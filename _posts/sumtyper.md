---
layout: post
title: "Sumtyper"
---

# Modellering med varianter/sumtyper/sealed classes

Når vi bygger systemer, lager vi modeller av den virkelige verden. Det er mange måter å bygge disse modellene på, og noen av dem introduserer en del problemer vi helst vil være foruten. Her skal vi utforske en teknikk som kalles sumtyper, som kan redusere feilene vi introduserer i modellene våre, og gjøre systemene vi bygger mer robuste.

## Uheldig kompleksitet

Felles for alle systemer vi bygger, er at vi må ha en modell av det vi jobber med i koden. En modell er en forenklet versjon av den virkelige verden. Noen ganger er den veldig forenklet, og fokuserer bare på noen få begreper. Andre ganger har vi behov for å vite mer om alle detaljene, og modellene kan bli ekstremt kompliserte.

En annen form for kompleksitet som dukker opp i modellene våre, er ugyldige tilstander. Dette er et resultat av måten vi har modellert på, og kan føre til at koden vi skriver må ta hensyn til mange tilfeller som aldri kan oppstå i den virkelige verden. Denne typen kompleksitet er uønsket, og fører til at systemene blir vanskeligere og dyrere og vedlikeholde. Det øker også sannsynligheten for feil i koden, og for at vi ender opp i situasjoner som ikke skal være mulig. Denne typen kompleksitet er det Frederick Phillips Brooks Jr. kaller “accidental complexity”: det er kompleksitet som er skapt av oss som bygger systemet, og som ikke finnes i den virkelige verden. I denne artikkelen skal vi kalle det for “uheldig kompleksitet”, og vi skal se på en teknikk som kan begrense det.

## Et eksempel

Tenk det at du skal lage et system for kommunen, som skal levere viktig informasjon til innbyggerne. Kanskje forsøker man først å levere det digitalt, men det er ikke alle som har registrert en digital postkasse, så man må sende brev i stedet. Da må vi vite adressen til mottakeren. Vi lager et første utkast til en modell i Java:

```java
class Adresse {

  String navn;
  String veiadresse;
  String postnummer;
  String poststed;
}

```

Denne modellen fungerer for mange av innbyggerne våre, men det viser seg at ikke alle innbyggerne har en vegadresse. Noen har en matrikkeladresse i stedet, som består av et gårdsnummer og et bruksnummer. Man kan ikke sende post til en matrikkeladresse, men vi tenker oss at Posten har en tjeneste som kan oversette en matrikkeladresse til et stedsnavn, så vi ønsker å lagre denne adressen for innbyggerne våre, dersom de har det. Da kan vi utvide modellen til dette:

```java
class Adresse {

  String navn;
  String veiadresse;
  String postnummer;
  String poststed;

  Integer gårdsnummer;
  Integer bruksnummer;
}

```

Nå er tanken at man instansierer klassen med en av de to konstruktørene, avhengig av hvilken type adresse man har. De andre feltene vil har verdien null.

## Problemer med denne modellen

Tenk deg at du har fått i oppgave å skrive en funksjon som tar en adresse som input, og formaterer teksten til en adresselapp. Noe som dette:

```
Anne Olsen
Skogvegen 13A
4580 Lyngdal
```

Vi tenker oss også at dette er et stort utviklingsprosjekt, så du har ikke oversikt over alle klassene. Adresseklassen er definert i en annen modul, og den er inkludert som en avhengighet i modulen du jobber på.

Vi starter med signaturen til den nye funksjonen:

```java
public String adresselapp(Adresse adresse) {}
```

Så ser du kanskje på feltene i adresse, finner navn, veiadresse, postnummer og poststed, og lager en String av det.

```java
public static String adresselapp(Adresse adresse) {
    return String.format("%s%n%s%n%s",
    adresse.navn,
    adresse.veiadresse,
    adresse.postnummer + " " + adresse.poststed);
}
```

Nå har vi glemt matrikkeladressen. I beste fall er dette kanskje spesifisert i oppgaven, og noen skriver en test som feiler når man tester med en matrikkeladresse. I verste fall går dette rett gjennom, og blir ikke oppdaget før man får konvolutter i retur med adressen

```
null
null
null null
```

Dette problemet oppstår også når man utvider en eksisterende modell. Kanskje hadde første versjon av Adresse-klassen bare veiadresse, og da man skrev adresselapp-funksjonen fantes det ingen felter for matrikkeladressen. Men når de nye feltene blir lagt til, er det ingenting som varsler deg om at du må oppdatere funksjonen “adresselapp”. Hvis ikke du legger merke til det selv, eller prosjektet har gode rutiner for endringer, blir det nok ikke oppdaget før det feiler i produksjon.

## Uheldig kompleksitet i Adresse-klassen

Dette er et eksempel på uheldig kompleksitet. Det er et problem som ikke finnes i den virkelige verden, men som vi har innført ved å velge en dårlig modell.

Når vi analyserer problemet, ser vi at modellen vår tillater mange tilstander som ikke skal være mulig. Sagt på en annen måte: det finnes noen sannheter om adresser som ikke er bevart i modellen vår, og som skaper problemer. Et eksempel på en slik sannhet at en adresse er enten en vegadresse eller en matrikkeladresse. Modellen vår gjenspeiler ikke dette, for eksempel tillater den at vi har en instans av Adresse som har satt alle feltene, eller bare navn, for å nevne noen. Hvis vi forenkler litt, og sier at hvert felt enten er definert eller ikke definert, er det 2^6 = 64 mulige kombinasjoner av felter, og bare to av dem er gyldige. Etterhvert som du legger til flere felter, eksploderer antall kombinasjoner, og det blir praktisk umulig å dekke alt med tester.

En bedre måte å modellere dette på er med en _sumtype_.

## Sumtyper

Når vi programmerer, definerer vi nye typer ved å kombinere enklere typer. I Java gjør vi typisk dette ved å lage en ny klasse, og definerer felter i denne klassen. På denne måten setter vi sammen eksisterende typer til en ny type, og gir den et navn, så vi kan referere til den senere. Denne måten å konstruere en ny type på kalles en produkttype. Grunnen til at vi kaller den det, er at mengden av mulige verdier for denne nye typen er det kartesiske produktet av alle mulige verdier for feltene i klassen.

For eksempel, om du lager en ny klasse med tre felter av typen boolean, er antall mulige verdier 2 × 2 × 2 = 8. Som vi så i eksempelet over, blir dette fort uhåndterlig, og vi ender opp med mange muligheter som ikke finnes i den virkelige verden.

En annen måte å konstruere nye typer på kalles sumtyper. I forskjellige programmeringsspråk kalles de også tagged unions, disjoint unions og variants. Forskjellen fra produkttyper er at de består av en gitt mengde varianter, og antall mulige verdier av denne nye typen er summen av varianter. Det betyr at om vi legger til en ny variant, blir det bare en ny mulig verdi. Dette er styrken til sumtyper, og grunnen til at vi kan unngå mye av den uheldige kompleksiteten vi så tidligere i adresseeksempelet vårt.

Aller helst vil vi også skrive sumtypene på en typesikker måte, slik at vi får en kompilatorfeil om vi ikke håndterer alle variantene. Det ville forhindret feilen i eksempelet vårt, hvor vi ikke håndterer matrikkeladressen. Det vil også sørge for at vi blir nødt til å håndtere nye varianter som blir innført senere.

I eksempelet vårt burde Adresse modelleres som en sumtype med to varianter: en vegadresse og en matrikkeladresse. Men hvordan gjør man det i Java?

## Første forsøk på omskriving av adresseklassen

Det første vi kan forsøke er å la Adresse være en abstrakt klasse, og hver variant være en konkret implementasjon. Vi kan også la funksjonen adresselapp være en abstrakt metode på Adresse, og implementere den i hver av subklassene.

```java
public abstract class Adresse {

  String navn;

  public abstract String adresselapp();
}

class Vegadresse extends Adresse {

  String veiadresse;
  String postnummer;
  String poststed;
}

class MatrikkelAdresse extends Adresse {

  Integer gårdsnummer;
  Integer bruksnummer;
}

```

Dette løser et par av problemene vi hadde med den første implementasjonen. Det er tydelig at navn er felles for begge variantene, og at de andre feltene er spesifikke for hver sin variant. Metoden adresselapp er en abstrakt metode, så alle subklassene må implementere den. Hvis vi innfører en ny subklasse, vil kompilatoren si fra hvis denne metoden ikke er implementert.

Dette fungerer bra i små prosjekter, hvor vi har klassene våre i en modul, og vi har rimelig god oversikt over domenemodellen. I større prosjekter fungerer det ikke så godt lenger.

Vi kan tenke oss at en sentral klasse som Adresse vil bli brukt mange steder i systemet. Ofte er den ikke definert i modulen vi jobber med, men den er inkludert som en avhengighet til en annen modul. Kanskje er det mange andre moduler på prosjektet som avhenger av den samme klassen, og bruker den til forskjellige ting. Hvis alle som jobber med Adresse utvider klassen med sine egne metoder, blir det fort uoversiktlig. Det er mye bedre om Adresse bare er en dataklasse, altså en klasse som bare holder på data. Så kan alle skrive sine egne funksjoner som tar adresseobjekter som parametere.

## Andre forsøk: instanceof

```java
public abstract class Adresse {

  String navn;
}

class Vegadresse extends Adresse {

  String veiadresse;
  String postnummer;
  String poststed;
}

class MatrikkelAdresse extends Adresse {

  Integer gårdsnummer;
  Integer bruksnummer;
}

final class Adresser {

  public static String adresselapp(Adresse adresse) {
    if (adresse instanceof Vegadresse) {
      Vegadresse vegadresse = (Vegadresse) adresse;
      return String.format(
        "%s%n%s%n%s",
        vegadresse.navn,
        vegadresse.veiadresse,
        vegadresse.postnummer + " " + vegadresse.poststed
      );
    } else if (adresse instanceof MatrikkelAdresse) {
      String stedsnavn;
      String postnummer;
      // slå opp stedsnavn og postnummer i en ekstern tjeneste
      return String.format("%s%n%s %s", adresse.navn, postnummer, stedsnavn);
    } else {
      throw new IllegalArgumentException("ukjent variant av Adresse");
    }
  }
}

```

Dette fungerer, men ikke spesielt godt. Her bruker vi `instanceof` for å sjekke hvilken subklasse av Adresse vi faktisk har. Men denne sjekken kjøres runtime, så vi får ikke noe hjelp av typesystemet, eller kompilatoren. Hvis vi utvider Adresse med en ny variant, får vi ikke melding fra kompilatoren om hvor det må fikses. Vi har fikset problemet med alle de ugyldige kombinasjonene av data, men ellers er ikke dette noen stor forbedring.

## Vi kan implementere sumtyper med visitor pattern

[Mark Seeman har skrevet en interessant bloggartikkel, hvor han viser at Visitor pattern og sumtyper er to sider av samme sak](https://blog.ploeh.dk/2018/06/25/visitor-as-a-sum-type/). Vi kan altså implementere adressetypen vår med Visitor pattern, og realisere mange av fordelene vi er ute etter.

Vi starter med å definere et interface AdresseVisitor, med en visit-metode for hver variant:

```java
interface AdresseVisitor<T> {
  T visitVegadresse(Vegadresse vegadresse);

  T visitMatrikkelAdresse(MatrikkelAdresse matrikkelAdresse);
}

```

Så definerer vi et interface for Adresse, med en metode accept, som tar AdresseVisitor som eneste parameter:

```java
public interface Adresse {
  <T> T accept(AdresseVisitor<T> visitor);
}

```

Så kan vi implementere de to variantene, som implementerer Adresse, og kaller sin respektive visit-metode:

```java
class Vegadresse implements Adresse {

  String navn;
  String veiadresse;
  String postnummer;
  String poststed;

  @Override
  public <T> T accept(AdresseVisitor<T> visitor) {
    return visitor.visitVegadresse(this);
  }
}

class MatrikkelAdresse implements Adresse {

  String navn;
  Integer gårdsnummer;
  Integer bruksnummer;

  @Override
  public <T> T accept(AdresseVisitor<T> visitor) {
    return visitor.visitMatrikkelAdresse(this);
  }
}

```

Nå kan vi implementere adresselapp, hvor vi implementerer AdresseVisitor med en anonym klasse:

```java
public final class Adresser {

  public static String adresselapp(Adresse adresse) {
    return adresse.accept(
      new AdresseVisitor<String>() {
        @Override
        public String visitVegadresse(Vegadresse vegadresse) {
          return String.format(
            "%s%n%s%n%s %s",
            vegadresse.navn,
            vegadresse.veiadresse,
            vegadresse.postnummer,
            vegadresse.poststed
          );
        }

        @Override
        public String visitMatrikkelAdresse(MatrikkelAdresse matrikkelAdresse) {
          String stedsnavn = null;
          String postnummer = null;
          // slå opp stedsnavn og postnummer i en ekstern tjeneste
          return String.format(
            "%s%n%s %s",
            matrikkelAdresse.navn,
            postnummer,
            stedsnavn
          );
        }
      }
    );
  }
}

```

Så lenge du er kjent med Visitor pattern, fungerer dette ganske godt. Hver klasse inneholder sine egne data, og det er ikke mulig å lage instanser med ugyldige kombinasjoner av felter. Når vi skriver en ny funksjon som tar en Adresse som input, blir vi tvunget til å håndtere alle variantene, ved at vi må implementere alle visit-metodene. Vi legger til nye varianter ved å utvide AdresseVisitor med en ny visit-metode, og så lage en ny klasse for den nye varianten. På denne måten vil vi få feilmeldinger på alle steder vi er nødt til å håndtere den nye varianten, siden vi må implementere den nye visit-metoden. Så lenge vi husker å legge til en ny metode i AdresseVisitor, oppfyller dette alle kravene våre.

Men denne implementasjonen får minuspoeng for mengden kode. Det er mange klasser og interfacer for å realisere en forholdsvis enkel idé. Det er en risiko at ideen drukner i all koden, og for nye utviklere som ikke kjenner Visitor pattern, vil det nok være vanskelig å se hva som foregår her.

## Church encoding

Vi skal se på en siste teknikk, som etter min mening er den mest elegante, men som nok også er den mest uvante for javautviklere.

[Church encoding](https://en.wikipedia.org/wiki/Church_encoding) er en teknikk fra matematikken, som kort fortalt handler om at datatyper kan representeres som funksjoner. Vi skal ikke gå i dybden på det her, men vi skal utnytte resultatet til å definere adressetypen vår ved hjelp av et funksjoner.

Denne gangen definerer vi Adresse som en abstrakt klasse med en abstrakt metode, som vi kaller match. match har to inputparametere, en for hver variant.

```java
abstract class Adresse {
abstract <T> T match(
Function<Vegadresse, T> vegadresse,
Function<MatrikkelAdresse, T> matrikkeladresse
);
```

Adresse har privat konstruktør, for å gjøre den immutable.

```java
private Adresse() {
}
```

De to variantene implementerer vi som indre klasser, som arver fra Adresse.

```java
static final class Vegadresse extends Adresse {

  public final String navn;
  public final String gatenavnOgNummer;
  public final String postnummer;
  public final String poststed;

  Vegadresse(
    String navn,
    String gatenavnOgNummer,
    String postnummer,
    String poststed
  ) {
    this.navn = navn;
    this.gatenavnOgNummer = gatenavnOgNummer;
    this.postnummer = postnummer;
    this.poststed = poststed;
  }

  @Override
  <T> T match(
    Function<Vegadresse, T> vegadresse,
    Function<MatrikkelAdresse, T> matrikkeladresse
  ) {
    return vegadresse.apply(this);
  }
}

static final class MatrikkelAdresse extends Adresse {

  public final String navn;
  public final Integer gårdsnummer;
  public final Integer bruksnummer;

  MatrikkelAdresse(String navn, Integer gårdsnummer, Integer bruksnummer) {
    this.navn = navn;
    this.gårdsnummer = gårdsnummer;
    this.bruksnummer = bruksnummer;
  }

  @Override
  <T> T match(
    Function<Vegadresse, T> vegadresse,
    Function<MatrikkelAdresse, T> matrikkeladresse
  ) {
    return matrikkeladresse.apply(this);
  }
}

```

Begge variantene implementerer også match, og kaller sin respektive funksjon fra parameterene.

Med denne implementasjonen har vi implementert adresse som en sumtype, og med match-metoden får vi tilgang til dataene, uansett hvilken variant vi har. Vi kan også være sikre på at vi har dekket alle variantene, siden vi vil få en kompileringsfeil når vi legger til en ny variant. Dette er enklest å se med et eksempel:

```java
static String adresselapp(Adresse adresse) {
  return adresse.match(
      vegadresse -> String.format("%s%n%s%n%s %s",
          vegadresse.navn,
          vegadresse.gatenavnOgNummer,
          vegadresse.postnummer,
          vegadresse.poststed),
      matrikkelAdresse -> {
        String stedsnavn = "";
        String postnummer = "";
        // slå opp stedsnavn og postnummer i en ekstern tjeneste
        return String.format("%s%n%s %s",
            matrikkelAdresse.navn,
            postnummer,
            stedsnavn);
      }
  );
}

```

Vi ser at vi må definere en funksjon for hver variant, som tar den gitte varianten som input, og produserer en String. Hvis vi utvider Adresse med en ny variant, vil match ta tre funksjoner i stedet for to, og vi vil få en kompileringsfeil som minner oss på å implementere en funksjon for den nye varianten. I forhold til visitor-eksempelet er det mye mindre kode.

Church encoding er min foretrukne måte å implementere sumtyper på i Java.

## Konklusjon

Sumtyper gir oss et verktøy for å modellere mer presist, som gjør at vi sparer oss for potensielt veldig mange feilsituasjoner. Det betyr at vi ikke trenger like mange tester, og vi reduserer risikoen for feil i systemene vi bygger. Hvis vi implementerer sumtyper med Visitor eller church encoding er vi også sikre på alle variantene blir håndtert, siden vi får kompileringsfeil hvis vi har glemt noe. Det sparer oss for enda flere feil i produksjon.

Sumtyper er tungvint i Java, og det krever ganske mye kode for å få til et forholdsvis enkelt konsept. Fordelene er imidlertidig så store, at jeg mener det er verdt det. Ellers kommer sealed classes som preview i Java 15, så om ikke så lenge ser det ut som det blir støtte for det i språket. Sealed classes er også tilgjengelig i Kotlin, og i mange andre språk.

## Referanser

- [https://en.wikipedia.org/wiki/Algebraic_data_type](https://en.wikipedia.org/wiki/Algebraic_data_type)
- [https://openjdk.java.net/jeps/360](https://openjdk.java.net/jeps/360)
- [https://en.wikipedia.org/wiki/Tagged_union](https://en.wikipedia.org/wiki/Tagged_union)
- [http://blog.higher-order.com/blog/2009/08/21/structural-pattern-matching-in-java/](http://blog.higher-order.com/blog/2009/08/21/structural-pattern-matching-in-java/)
- [https://dzone.com/articles/effectively-sealed-classes-in-java](https://dzone.com/articles/effectively-sealed-classes-in-java)
- [https://blog.ploeh.dk/2018/06/25/visitor-as-a-sum-type/](https://blog.ploeh.dk/2018/06/25/visitor-as-a-sum-type/)
- [https://blog.ploeh.dk/2018/06/18/church-encoded-payment-types/](https://blog.ploeh.dk/2018/06/18/church-encoded-payment-types/)
- [https://kotlinlang.org/docs/sealed-classes.html](https://kotlinlang.org/docs/sealed-classes.html)
