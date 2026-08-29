package dev.kurai.uhc.example.camp;

import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.example.annotation.DocumentReference;
import dev.kurai.uhc.example.camp.annotation.CampColor;
import dev.kurai.uhc.example.camp.annotation.Objective;
import dev.kurai.uhc.module.camp.AbstractCampData;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public final class ExampleCampData extends AbstractCampData<ExampleCamp> {

  private final String identifier;
  private final String name;

  private final String documentReference;
  private final String objective;

  private final ChatColor chatColor;
  private final DyeColor dyeColor;

  public ExampleCampData(final Class<? extends ExampleCamp> camp) {
    super(camp);
    this.identifier = camp.getAnnotation(Identifier.class).value();
    this.name = camp.getAnnotation(Name.class).value();

    final DocumentReference reference = camp.getAnnotation(DocumentReference.class);
    this.documentReference = reference == null ? this.identifier : reference.value();

    final Objective objective = camp.getAnnotation(Objective.class);
    this.objective = objective == null ? "No objective found" : objective.value();

    final var campColor = camp.getAnnotation(CampColor.class);
    this.chatColor = campColor.color();
    this.dyeColor = campColor.dyeColor();
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public String getName() {
    return this.name;
  }

  public String getDocumentReference() {
    return this.documentReference;
  }

  public String getObjective() {
    return this.objective;
  }

  public TextColor getTextColor() {
    return switch (this.chatColor) {
      case BLACK -> BLACK;
      case DARK_BLUE -> DARK_BLUE;
      case DARK_GREEN -> DARK_GREEN;
      case DARK_AQUA -> DARK_AQUA;
      case DARK_RED -> DARK_RED;
      case DARK_PURPLE -> DARK_PURPLE;
      case GOLD -> GOLD;
      case GRAY -> GRAY;
      case DARK_GRAY -> DARK_GRAY;
      case BLUE -> BLUE;
      case GREEN -> GREEN;
      case AQUA -> AQUA;
      case RED -> RED;
      case LIGHT_PURPLE -> LIGHT_PURPLE;
      case YELLOW -> YELLOW;
      case WHITE, MAGIC, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET -> WHITE;
    };
  }

  public ChatColor getChatColor() {
    return this.chatColor;
  }

  public DyeColor getDyeColor() {
    return this.dyeColor;
  }
}
