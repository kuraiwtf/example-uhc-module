package dev.kurai.uhc.example.role.defaults.solitary.test;

import com.google.common.collect.Lists;
import dev.kurai.uhc.effect.Effect;
import dev.kurai.uhc.effect.EffectHolder;
import dev.kurai.uhc.effect.EffectType;
import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.camp.defaults.SolitaryCamp;
import dev.kurai.uhc.example.description.PowerDescription;
import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.example.role.annotation.RoleCamp;
import dev.kurai.uhc.example.role.defaults.solitary.test.power.AirBumpPower;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@RoleCamp(SolitaryCamp.class)
@Identifier("test")
@Name("Test")
public final class TestRole extends ExampleRole {

  public TestRole(final UUID owner, final ExampleModule module) {
    super(owner, module);
    this.registerPower(new AirBumpPower(owner, module.getUltraHardcore()));
  }

  @Override
  public void onDistribute() {
    final EffectHoldingComponent effectComponent =
        this.profile.getComponent(EffectHoldingComponent.class);
    if (effectComponent != null) {
      final EffectHolder effectHolder = effectComponent.holder();
      effectHolder.addEffect(
          new Effect(
              "strength",
              EffectType.STRENGTH,
              this.module
                  .getUltraHardcore()
                  .effectService()
                  .effectValuePerLevel(EffectType.STRENGTH),
              Duration.ZERO));
      effectHolder.addEffect(new Effect("resistance", EffectType.RESISTANCE, 0.10, Duration.ZERO));
    }
  }

  @Override
  public Collection<PowerDescription> getPassiveAbilities() {
    return Lists.newArrayList(
        new PowerDescription(
            "Effets",
            Component.text("Vous disposez de l'effet ")
                .append(Component.text("Force I", NamedTextColor.RED))
                .append(Component.text(" ainsi que de "))
                .append(Component.text("10%", NamedTextColor.GREEN))
                .append(Component.text(" de "))
                .append(Component.text("Résistance", NamedTextColor.GREEN))
                .append(Component.text('.'))),
        new PowerDescription("Résonance", Component.text("Vous faites résoner la partie.")));
  }

  @Override
  public Collection<PowerDescription> getActiveAbilities() {
    return Collections.singletonList(
        new PowerDescription(
            "Pouvoir",
            Component.text("Ceci est une capacité"),
            new PowerDescription("Sous-Pouvoir 1", Component.text("Voici le premier sous-pouvoir")),
            new PowerDescription(
                "Sous-Pouvoir 2", Component.text("Voici le deuxième sous-pouvoir"))));
  }
}
