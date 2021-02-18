package visitor;

public class BarePoststed implements Adresse {
  public final String navn;
  public final String postnummer;
  public final String poststed;

  public BarePoststed(String navn, String postnummer, String poststed) {
    this.navn = navn;
    this.postnummer = postnummer;
    this.poststed = poststed;
  }

  @Override
  public <T> T accept(AdresseVisitor<T> visitor) {
    return visitor.visitBarePoststed(this);
  }
}
