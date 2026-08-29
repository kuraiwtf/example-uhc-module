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

@Identifier("primary_antagonist")
@Name("Camp Méchant 1")
@DocumentReference("antagonistes")
@Objective("Vous devez gagner avec le camp des §cMéchants 1§r.")
@CampColor(color = ChatColor.RED, dyeColor = DyeColor.RED)
public final class PrimaryAntagonistCamp extends ExampleCamp {

  public PrimaryAntagonistCamp(final ExampleModule module) {
    super(module);
  }
}
