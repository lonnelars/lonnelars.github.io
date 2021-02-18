package class_hierarchy;

public class BarePoststed extends Adresse {
  @Override
  public String adresselapp() {
    return String.format("%s%n%s %s",
        this.getNavn(),
        this.getPostnummer(),
        this.getPoststed());
  }
}
