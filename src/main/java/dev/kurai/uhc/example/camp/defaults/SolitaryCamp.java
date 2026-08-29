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

@Identifier("solitary")
@Name("Solitaires")
@DocumentReference("solitaires")
@Objective("Vous devez gagner §6seul§r.")
@CampColor(color = ChatColor.GOLD, dyeColor = DyeColor.ORANGE)
public final class SolitaryCamp extends ExampleCamp {

  public SolitaryCamp(final ExampleModule module) {
    super(module);
  }
}
