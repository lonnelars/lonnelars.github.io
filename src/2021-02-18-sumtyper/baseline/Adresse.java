package baseline;

public class Adresse {
  // postmottaker med gateadresse
  private String navn;
  private String gateadresse;
  private String postnummer;
  private String poststed;
  // postmottaker som benytter stedsnavn
  private String stedsnavn;
  // postmottakere som verken benytter gateadresse eller stedsnavn bruker bare feltene for navn, postnummer og poststed

  public String getNavn() {
    return navn;
  }

  public void setNavn(String navn) {
    this.navn = navn;
  }

  public String getGateadresse() {
    return gateadresse;
  }

  public void setGateadresse(String gateadresse) {
    this.gateadresse = gateadresse;
  }

  public String getPostnummer() {
    return postnummer;
  }

  public void setPostnummer(String postnummer) {
    this.postnummer = postnummer;
  }

  public String getPoststed() {
    return poststed;
  }

  public void setPoststed(String poststed) {
    this.poststed = poststed;
  }

  public String getStedsnavn() {
    return stedsnavn;
  }

  public void setStedsnavn(String stedsnavn) {
    this.stedsnavn = stedsnavn;
  }

}
