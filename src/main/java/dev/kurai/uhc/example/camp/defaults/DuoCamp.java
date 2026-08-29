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

@Identifier("duo")
@Name("Duo")
@DocumentReference("duo")
@Objective("Vous devez gagner avec votre §dDuo§r.")
@CampColor(color = ChatColor.LIGHT_PURPLE, dyeColor = DyeColor.PINK)
public final class DuoCamp extends ExampleCamp {

  public DuoCamp(final ExampleModule module) {
    super(module);
  }
}
