package class_hierarchy;

public class StedsnavnAdresse extends Adresse {
  private String stedsnavn;

  @Override
  public String adresselapp() {
    return String.format("%s%n%s%n%s %s",
        this.getNavn(),
        this.getStedsnavn(),
        this.getPostnummer(),
        this.getPoststed());
  }

  public String getStedsnavn() {
    return stedsnavn;
  }

  public void setStedsnavn(String stedsnavn) {
    this.stedsnavn = stedsnavn;
  }
}
