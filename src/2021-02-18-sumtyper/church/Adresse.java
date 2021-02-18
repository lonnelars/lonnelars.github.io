package church;

import java.util.function.Function;

interface Adresse {
  <T> T match(
      Function<Gateadresse, T> gateadresse,
      Function<StedsnavnAdresse, T> stedsnavnAdresse,
      Function<BarePoststed, T> barePoststed);

  final class Gateadresse implements Adresse {
    public final String navn;
    public final String gateOgNummer;
    public final String postnummer;
    public final String poststed;

    Gateadresse(String navn, String gateOgNummer, String postnummer, String poststed) {
      this.navn = navn;
      this.gateOgNummer = gateOgNummer;
      this.postnummer = postnummer;
      this.poststed = poststed;
    }

    @Override
    public <T> T match(Function<Gateadresse, T> gateadresse, Function<StedsnavnAdresse, T> stedsnavnAdresse, Function<BarePoststed, T> barePoststed) {
      return gateadresse.apply(this);
    }
  }

  final class StedsnavnAdresse implements Adresse {
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
    public <T> T match(Function<Gateadresse, T> gateadresse, Function<StedsnavnAdresse, T> stedsnavnAdresse, Function<BarePoststed, T> barePoststed) {
      return stedsnavnAdresse.apply(this);
    }
  }

  final class BarePoststed implements Adresse {
    public final String navn;
    public final String postnummer;
    public final String poststed;

    public BarePoststed(String navn, String postnummer, String poststed) {
      this.navn = navn;
      this.postnummer = postnummer;
      this.poststed = poststed;
    }

    @Override
    public <T> T match(Function<Gateadresse, T> gateadresse, Function<StedsnavnAdresse, T> stedsnavnAdresse, Function<BarePoststed, T> barePoststed) {
      return barePoststed.apply(this);
    }
  }
}
