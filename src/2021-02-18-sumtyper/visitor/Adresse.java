package visitor;

public interface Adresse {
    <T> T accept(AdresseVisitor<T> visitor);
}