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

@Identifier("secondary_antagonist")
@Name("Camp Méchant 2")
@DocumentReference("antagonistes-2")
@Objective("Vous devez gagner avec le camp des §eMéchants 2§r.")
@CampColor(color = ChatColor.YELLOW, dyeColor = DyeColor.YELLOW)
public final class SecondaryAntagonistCamp extends ExampleCamp {

  public SecondaryAntagonistCamp(final ExampleModule module) {
    super(module);
  }
}
