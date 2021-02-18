package visitor;

public class StedsnavnAdresse implements Adresse {
  public final String navn;
  public final String stedsnavn;
  public final String postnummer;
  public final String poststed;

  public StedsnavnAdresse(String navn, String stedsnavn, String postnummer, String poststed) {
    this.navn = navn;
    this.stedsnavn = stedsnavn;
    this.postnummer = postnummer;
    this.poststed = poststed;
  }

  @Override
  public <T> T accept(AdresseVisitor<T> visitor) {
    return visitor.visitStedsnavnAdresse(this);
  }
}
