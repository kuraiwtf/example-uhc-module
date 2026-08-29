package dev.kurai.uhc.example.slot.slot;

import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.game.slot.SlotProvider;

public final class ExampleSlotProvider implements SlotProvider {

  private final ExampleModule module;

  public ExampleSlotProvider(final ExampleModule module) {
    this.module = module;
  }

  @Override
  public int slots() {
    return this.module.getEnabledRoles().size();
  }
}
