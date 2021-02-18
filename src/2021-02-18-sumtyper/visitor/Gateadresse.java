package visitor;

public class Gateadresse implements Adresse {
  public final String navn;
  public final String gateOgNummer;
  public final String postnummer;
  public final String poststed;

  public Gateadresse(String navn, String gateOgNummer, String postnummer, String poststed) {
    this.navn = navn;
    this.gateOgNummer = gateOgNummer;
    this.postnummer = postnummer;
    this.poststed = poststed;
  }

  @Override
  public <T> T accept(AdresseVisitor<T> visitor) {
    return visitor.visitGateadresse(this);
  }
}
