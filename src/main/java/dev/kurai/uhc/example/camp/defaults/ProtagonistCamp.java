package dev.kurai.uhc.example.camp.defaults;

import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.annotation.DocumentReference;
import dev.kurai.uhc.example.camp.ExampleCamp;
import dev.kurai.uhc.example.camp.annotation.CampColor;
import dev.kurai.uhc.example.camp.annotation.Objective;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

@Identifier("protagonist")
@Name("Protagonistes")
@DocumentReference("protagonistes")
@Objective("Vous devez gagner avec le camp des §aProtagonistes§r.")
@CampColor(color = ChatColor.GREEN, dyeColor = DyeColor.LIME)
public final class ProtagonistCamp extends ExampleCamp {

  public ProtagonistCamp(final ExampleModule module) {
    super(module);
  }
}
