package dev.kurai.uhc.example.camp;

import dev.kurai.uhc.example.camp.defaults.*;
import dev.kurai.uhc.module.camp.registrar.AbstractCampRegistrar;
import dev.kurai.uhc.module.camp.repository.CampRepository;

public final class ExampleCampRegistrar
    extends AbstractCampRegistrar<ExampleCamp, ExampleCampData> {

  public ExampleCampRegistrar() {
    super(new ExampleCampFactory(), new CampRepository<>());
    this.registerCamps(
        ProtagonistCamp.class,
        PrimaryAntagonistCamp.class,
        SecondaryAntagonistCamp.class,
        SolitaryCamp.class,
        DuoCamp.class);
  }

  @SafeVarargs
  private void registerCamps(final Class<? extends ExampleCamp>... camps) {
    for (final var camp : camps) {
      this.registerTeam(camp);
    }
  }
}
