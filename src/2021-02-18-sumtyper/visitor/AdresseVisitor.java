package visitor;

interface AdresseVisitor<T> {
  T visitGateadresse(Gateadresse gateadresse);

  T visitStedsnavnAdresse(StedsnavnAdresse stedsnavnAdresse);

  T visitBarePoststed(BarePoststed barePoststed);
}
