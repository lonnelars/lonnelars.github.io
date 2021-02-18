package data_classes;

public final class Adresser {
  public static String adresselapp(Adresse adresse) {
    if (adresse instanceof Gateadresse) {
      return String.format("%s%n%s%n%s",
          adresse.getNavn(),
          ((Gateadresse) adresse).getGateOgNummer(),
          adresse.getPostnummer() + " " + adresse.getPoststed());
    } else if (adresse instanceof StedsnavnAdresse) {
      return String.format("%s%n%s%n%s %s",
          adresse.getNavn(),
          ((StedsnavnAdresse) adresse).getStedsnavn(),
          adresse.getPostnummer(),
          adresse.getPoststed());
    } else if (adresse instanceof BarePoststed) {
      return String.format("%s%n%s %s",
          adresse.getNavn(),
          adresse.getPostnummer(),
          adresse.getPoststed());
    } else {
      throw new IllegalArgumentException("ukjent variant av Adresse");
    }
  }
}
