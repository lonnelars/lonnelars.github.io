package baseline;

import org.junit.jupiter.api.Test;

class AdresseTest {

  @Test
  void adresselapp() {
    var adresse = new Adresse();
    adresse.setNavn("Per Johansen");
    adresse.setPostnummer("9672");
    adresse.setPoststed("Ingøy");

    System.out.println(Adresser.adresselapp(adresse));
  }
}