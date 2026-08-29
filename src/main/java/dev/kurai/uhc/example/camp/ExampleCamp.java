package dev.kurai.uhc.example.camp;

import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.module.camp.AbstractCamp;

public abstract class ExampleCamp extends AbstractCamp<ExampleModule> {

  public ExampleCamp(final ExampleModule module) {
    super(module);
  }
}
