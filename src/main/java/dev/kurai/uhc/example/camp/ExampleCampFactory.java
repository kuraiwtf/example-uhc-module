package dev.kurai.uhc.example.camp;

import dev.kurai.uhc.module.camp.factory.CampFactory;
import org.jetbrains.annotations.Contract;

final class ExampleCampFactory implements CampFactory<ExampleCamp, ExampleCampData> {

  @Contract("_ -> new")
  @Override
  public ExampleCampData provideNewInstance(final Class<? extends ExampleCamp> identifier) {
    return new ExampleCampData(identifier);
  }
}
