package church;

public class Adresser {
  public static String adresselapp(Adresse adresse) {
    return adresse.match(
        gateadresse -> String.format("%s%n%s%n%s %s",
            gateadresse.navn,
            gateadresse.gateOgNummer,
            gateadresse.postnummer,
            gateadresse.poststed),
        stedsnavnAdresse -> String.format("%s%n%s%n%s %s",
            stedsnavnAdresse.navn,
            stedsnavnAdresse.stedsnavn,
            stedsnavnAdresse.postnummer,
            stedsnavnAdresse.poststed),
        barePoststed -> String.format("%s%n%s %s",
            barePoststed.navn,
            barePoststed.postnummer,
            barePoststed.poststed)
    );
  }
}
