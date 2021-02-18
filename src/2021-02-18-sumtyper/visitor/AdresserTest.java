package visitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdresserTest {
  @Test
  public void gateadresse() {
    var adresse = new Gateadresse("Per Post", "Storgata 15A", "0161", "Oslo");
    var expected = "Per Post\nStorgata 15A\n0161 Oslo";
    assertEquals(expected, Adresser.adresselapp(adresse));
  }

  @Test
  public void stedsnavnAdresse() {
    var adresse = new StedsnavnAdresse("Per Post", "Vikane", "5637", "Olve");
    var expected = "Per Post\nVikane\n5637 Olve";
    assertEquals(expected, Adresser.adresselapp(adresse));
  }

  @Test
  public void barePoststed() {
    var adresse = new BarePoststed("Per Post", "9672", "Ingøy");
    var expected = "Per Post\n9672 Ingøy";
    assertEquals(expected, Adresser.adresselapp(adresse));
  }
}