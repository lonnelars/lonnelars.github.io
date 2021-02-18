package baseline;

public class Adresser {
  public static String adresselapp(Adresse adresse) {
    return String.format("%s%n%s%n%s %s",
        adresse.getNavn(),
        adresse.getGateadresse(),
        adresse.getPostnummer(),
        adresse.getPoststed());
  }
}
