package class_hierarchy;

public class Gateadresse extends Adresse {
  private String gateOgNummer;

  @Override
  public String adresselapp() {
    return String.format("%s%n%s%n%s",
        this.getNavn(),
        this.getGateOgNummer(),
        this.getPostnummer() + " " + this.getPoststed());
  }

  public String getGateOgNummer() {
    return gateOgNummer;
  }

  public void setGateOgNummer(String gateOgNummer) {
    this.gateOgNummer = gateOgNummer;
  }
}
