package visitor;

public final class Adresser {
  public static String adresselapp(Adresse adresse) {
    return adresse.accept(new AdresseVisitor<>() {
      @Override
      public String visitGateadresse(Gateadresse gateadresse) {
        return String.format("%s%n%s%n%s %s",
            gateadresse.navn,
            gateadresse.gateOgNummer,
            gateadresse.postnummer,
            gateadresse.poststed);
      }

      @Override
      public String visitStedsnavnAdresse(StedsnavnAdresse stedsnavnAdresse) {
        return String.format("%s%n%s%n%s %s",
            stedsnavnAdresse.navn,
            stedsnavnAdresse.stedsnavn,
            stedsnavnAdresse.postnummer,
            stedsnavnAdresse.poststed);
      }

      @Override
      public String visitBarePoststed(BarePoststed barePoststed) {
        return String.format("%s%n%s %s",
            barePoststed.navn,
            barePoststed.postnummer,
            barePoststed.poststed);
      }
    });
  }
}